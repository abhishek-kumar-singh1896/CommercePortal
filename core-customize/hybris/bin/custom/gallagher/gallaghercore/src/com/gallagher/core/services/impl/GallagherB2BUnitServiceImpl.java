/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import com.gallagher.core.services.GallagherB2BUnitService;


/**
 * @author ankituniyal
 *
 */
public class GallagherB2BUnitServiceImpl extends DefaultB2BUnitService implements GallagherB2BUnitService
{

	private static final String SECURITY_B2B_GLOBAL = "securityB2BGlobal";
	private static final String SECURITY_B2B = "securityB2B";
	private static final String SECURITY = "security";
	private static final String CURRENT_SESSION_SALES_AREA = "currentSessionSalesArea";
	private static final String CURRENT_SESSION_CUSTOMER_GROUP = "currentSessionCustomerGroup";
	public static final String CURRENCY_SESSION_ATTR_KEY = "currency".intern();
	private static final String SESSION_CART_PARAMETER_NAME = "cart";
	public static final String B2BDEFAULTDISCOUNTGROUP = "B2B_DEFAULT_DISCOUNT_GROUP";

	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private CommerceCartService commerceCartService;

	@Override
	public List<B2BUnitModel> getAllB2BUnits(final CustomerModel customer)
	{
		final Set<PrincipalGroupModel> customerGroups = customer.getGroups();
		final List<B2BUnitModel> b2bUnitList = new ArrayList<>();
		final Set<B2BUnitModel> rootB2BUnits = new HashSet<>();
		for (final PrincipalGroupModel group : customerGroups)
		{
			if (group instanceof B2BUnitModel)
			{
				rootB2BUnits.add(getRootUnit((B2BUnitModel) group));
				b2bUnitList.add((B2BUnitModel) group);
			}
		}
		if (rootB2BUnits.size() > 1)
		{
			return b2bUnitList;
		}
		else
		{
			b2bUnitList.clear();
			return b2bUnitList;
		}
	}

	@Override
	public Pair<BaseSiteModel, BaseStoreModel> getBaseSiteAndStoreForUnit(final B2BUnitModel defaultB2BUnit)
	{
		BaseSiteModel defaultSite = null;
		BaseStoreModel defaultBaseStore = null;

		if (CollectionUtils.isEmpty(defaultB2BUnit.getAddresses()))
		{
			defaultBaseStore = getBaseStoreService().getBaseStoreForUid(SECURITY_B2B_GLOBAL);
			defaultSite = getBaseSiteService().getBaseSiteForUID(SECURITY_B2B_GLOBAL);
		}
		else
		{
			final List<BaseStoreModel> baseStores = getBaseStoreService().getAllBaseStores();
			final String isoCode = defaultB2BUnit.getAddresses().iterator().next().getCountry().getIsocode();
			boolean flag = false;
			for (final BaseStoreModel baseStore : baseStores)
			{
				if (baseStore.getUid().startsWith(SECURITY) && baseStore.getUid().contains(isoCode))
				{
					String securityB2BisoCode = SECURITY_B2B;
					securityB2BisoCode = securityB2BisoCode.concat(isoCode);
					defaultBaseStore = getBaseStoreService().getBaseStoreForUid(securityB2BisoCode);
					defaultSite = getBaseSiteService().getBaseSiteForUID(securityB2BisoCode);
					flag = true;
					break;
				}
			}
			if (!flag)
			{
				defaultBaseStore = getBaseStoreService().getBaseStoreForUid(SECURITY_B2B_GLOBAL);
				defaultSite = getBaseSiteService().getBaseSiteForUID(SECURITY_B2B_GLOBAL);
			}
		}

		return Pair.of(defaultSite, defaultBaseStore);
	}

	@Override
	public void updateBranchInSession(final Session session, final UserModel currentUser)
	{
		if (currentUser instanceof B2BCustomerModel)
		{
			final Object[] branchInfo = (Object[]) getSessionService().executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public Object[] execute()
				{
					getSearchRestrictionService().disableSearchRestrictions();
					final B2BCustomerModel currentCustomer = (B2BCustomerModel) currentUser;
					final B2BUnitModel unitOfCustomer = getParent(currentCustomer);


					/**
					 * Europe1PriceFactory does not allow a user to belong to multiple price groups with themselves have
					 * different UPGs assigned see https://jira.hybris.com/browse/BTOB-488 get the upg assigned to the parent
					 * unit and set it in the context if none is assigned default to 'B2B_DEFAULT_PRICE_GROUP'
					 */
					final EnumerationValueModel userPriceGroup = (unitOfCustomer.getUserPriceGroup() != null
							? getTypeService().getEnumerationValue(unitOfCustomer.getUserPriceGroup())
							: lookupPriceGroupFromClosestParent(unitOfCustomer));

					final EnumerationValueModel userDiscountGroup = (unitOfCustomer.getUserDiscountGroup() != null
							? getTypeService().getEnumerationValue(unitOfCustomer.getUserDiscountGroup())
							: lookupDiscountGroupFromClosestParent(unitOfCustomer));

					return new Object[]
					{ getRootUnit(unitOfCustomer), getBranch(unitOfCustomer), unitOfCustomer, userPriceGroup, userDiscountGroup };
				}
			});

			getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_ROOTUNIT, branchInfo[0]);
			getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_BRANCH, branchInfo[1]);
			getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_UNIT, branchInfo[2]);
			final B2BUnitModel b2bUnit = (B2BUnitModel) branchInfo[2];
			getSessionService().setAttribute(CURRENT_SESSION_SALES_AREA, b2bUnit.getSalesArea());
			getSessionService().setAttribute(CURRENT_SESSION_CUSTOMER_GROUP, b2bUnit.getCustomerGroup());
			getSessionService().setAttribute(Europe1Constants.PARAMS.UPG, branchInfo[3]);
			getSessionService().setAttribute(Europe1Constants.PARAMS.UDG, branchInfo[4]);

			final B2BCustomerModel currentCustomer = (B2BCustomerModel) currentUser;
			final B2BUnitModel defaultUnit = currentCustomer.getDefaultB2BUnit();
			final CurrencyModel sessionCurrency;
			if (defaultUnit != null && defaultUnit.getCurrency() != null)
			{
				sessionCurrency = defaultUnit.getCurrency();
			}
			else
			{
				final BaseStoreModel store = getBaseStoreService().getCurrentBaseStore();
				sessionCurrency = store.getDefaultCurrency();
			}

			final List<CartModel> cartModels = commerceCartService.getCartsForSiteAndUser(baseSiteService.getCurrentBaseSite(),
					currentCustomer);

			if (CollectionUtils.isNotEmpty(cartModels))
			{
				final CartModel cartModel = cartModels.stream().filter(cart -> cart.getCurrency() == sessionCurrency).findFirst()
						.orElse(null);
				getSessionService().setAttribute(SESSION_CART_PARAMETER_NAME, cartModel);

			}

			getSessionService().setAttribute(CURRENCY_SESSION_ATTR_KEY, sessionCurrency);
		}
	}

	protected EnumerationValueModel lookupDiscountGroupFromClosestParent(final B2BUnitModel unitOfCustomer)
	{
		for (final B2BUnitModel unitModel : getAllParents(unitOfCustomer))
		{
			if (unitModel.getUserDiscountGroup() != null)
			{
				return getTypeService().getEnumerationValue(unitModel.getUserDiscountGroup());
			}
		}
		return getTypeService().getEnumerationValue(UserDiscountGroup._TYPECODE, B2BDEFAULTDISCOUNTGROUP);
	}

	public List<B2BUnitModel> getAllB2BData(final CustomerModel currentCustomer)
	{
		final List<B2BUnitModel> b2bUnitList = new ArrayList<>();
		final B2BCustomerModel b2bCustomer = (B2BCustomerModel) currentCustomer;
		int i = 0;
		boolean flag = false;
		String selectedUnit = null;
		final B2BUnitModel unitModel = getSessionService().getAttribute("selectedB2BUnit");
		if (unitModel != null)
		{
			selectedUnit = unitModel.getUid();
		}

		List<B2BUnitModel> rootNodes = null;
		final Set<PrincipalGroupModel> customerGroups = currentCustomer.getGroups();

		if (currentCustomer instanceof B2BCustomerModel)
		{
			rootNodes = getAllB2BUnits(currentCustomer);
		}
		if (rootNodes != null && rootNodes.size() == 0)//If Units From Same Hirearachy then add all the units to display in dropdown,since popup doesn't appear
		{
			final B2BUnitModel defaultUnit = b2bCustomer.getDefaultB2BUnit();
			if (defaultUnit != null)
			{
				defaultUnit.getUid();
			}
			for (final PrincipalGroupModel group : customerGroups)
			{
				if (group instanceof B2BUnitModel && group.equals(defaultUnit))
				{
					flag = true;
					b2bUnitList.add(0, ((B2BUnitModel) group));
				}
				else if (group instanceof B2BUnitModel)
				{
					if (flag == true)
					{
						b2bUnitList.add(++i, ((B2BUnitModel) group));
					}
					else
					{
						b2bUnitList.add(i++, ((B2BUnitModel) group));
					}
				}
			}
		}
		else
		{
			for (final B2BUnitModel unit : rootNodes)//If Units were from diiferenct hireachy then check if parent or child unit then add accordingly in list
			{
				if (StringUtils.equals(unit.getUid(), selectedUnit) && getRootUnit(unit).equals(unit))//If parent unit then add child unit also in list
				{
					final B2BUnitModel parentUnit = unit;
					b2bUnitList.add(unit);
					for (final PrincipalGroupModel group : customerGroups)
					{
						if (group instanceof B2BUnitModel && getRootUnit((B2BUnitModel) group).equals(unit)
								&& !group.equals(parentUnit))
						{

							b2bUnitList.add((B2BUnitModel) group);
						}
					}
					break;
				}
				else if (StringUtils.equals(unit.getUid(), selectedUnit))//If only child unit then add what is selected in login
				{
					b2bUnitList.add(unit);
					break;
				}
			}
		}
		return b2bUnitList;
	}


	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}



}
