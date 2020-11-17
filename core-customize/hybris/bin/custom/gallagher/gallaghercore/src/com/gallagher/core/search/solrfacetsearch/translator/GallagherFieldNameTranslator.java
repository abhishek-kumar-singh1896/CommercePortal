package com.gallagher.core.search.solrfacetsearch.translator;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider.FieldType;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;
import de.hybris.platform.solrfacetsearch.provider.QualifierProviderAware;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFieldNameTranslator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gallagher.core.search.solrfacetsearch.provider.impl.GallagherProductDiscountValueResolver;
import com.gallagher.core.search.solrfacetsearch.provider.impl.GallagherProductPriceRangeValueResolver;
import com.gallagher.core.search.solrfacetsearch.provider.impl.GallagherProductPricesValueResolver;
import com.gallagher.core.search.solrfacetsearch.provider.impl.GallagherUserDiscountGroupQualifierProvider;
import com.gallagher.core.services.GallagherSalesAreaService;


/**
 * The GallagherFieldNameTranslator class. Handles the sales Area in the indexed properties.
 *
 * @author ankituniyal
 */
public class GallagherFieldNameTranslator extends DefaultFieldNameTranslator
{
	public static final String FIELDNAME_SEPARATOR = "_";

	private GallagherSalesAreaService salesAreaService;
	private UserService userService;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private SessionService sessionService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String translateFromProperty(final SearchQuery searchQuery, final IndexedProperty indexedProperty,
			final FieldType fieldType)
	{
		final BaseSiteModel cmssite = getBaseSiteService().getCurrentBaseSite();
		if (cmssite != null && cmssite.getUid().contains("B2B"))
		{
			final IndexedType indexedType = searchQuery.getIndexedType();
			String fieldQualifier = null;
			final String valueProviderId = getValueProviderSelectionStrategy().resolveValueProvider(indexedType, indexedProperty);

			final Object valueProvider = getValueProviderSelectionStrategy().getValueProvider(valueProviderId);

			final QualifierProvider qualifierProvider = valueProvider instanceof QualifierProviderAware
					? ((QualifierProviderAware) valueProvider).getQualifierProvider()
					: null;

			QualifierProvider salesAreaQualifierProvider = valueProvider instanceof GallagherProductPricesValueResolver
					? ((GallagherProductPricesValueResolver) valueProvider)
							.getSalesAreaQualifierProvider()
					: null;

			if (salesAreaQualifierProvider == null)
			{
				salesAreaQualifierProvider = valueProvider instanceof GallagherProductPriceRangeValueResolver
						? ((GallagherProductPriceRangeValueResolver) valueProvider).getSalesAreaQualifierProvider()
						: null;
			}

			if (salesAreaQualifierProvider == null)
			{
				salesAreaQualifierProvider = valueProvider instanceof GallagherProductDiscountValueResolver
						? ((GallagherProductDiscountValueResolver) valueProvider).getSalesAreaQualifierProvider()
						: null;
			}

			QualifierProvider userPriceGroupQualifierProvider = valueProvider instanceof GallagherProductPricesValueResolver
					? ((GallagherProductPricesValueResolver) valueProvider)
							.getUserPriceGroupQualifierProvider()
					: null;

			if (userPriceGroupQualifierProvider == null)
			{
				userPriceGroupQualifierProvider = valueProvider instanceof GallagherProductPriceRangeValueResolver
						? ((GallagherProductPriceRangeValueResolver) valueProvider).getUserPriceGroupQualifierProvider()
						: null;
			}

			if (userPriceGroupQualifierProvider == null)
			{
				userPriceGroupQualifierProvider = valueProvider instanceof GallagherProductDiscountValueResolver
						? ((GallagherProductDiscountValueResolver) valueProvider).getUserDiscountGroupQualifierProvider()
						: null;
			}

			QualifierProvider customerGroupQualifierProvider = valueProvider instanceof GallagherProductPricesValueResolver
					? ((GallagherProductPricesValueResolver) valueProvider).getCustomerGroupQualifierProvider()
					: null;

			if (customerGroupQualifierProvider == null)
			{
				customerGroupQualifierProvider = valueProvider instanceof GallagherProductPriceRangeValueResolver
						? ((GallagherProductPriceRangeValueResolver) valueProvider).getCustomerGroupQualifierProvider()
						: null;
			}

			if (customerGroupQualifierProvider == null)
			{
				customerGroupQualifierProvider = valueProvider instanceof GallagherProductDiscountValueResolver
						? ((GallagherProductDiscountValueResolver) valueProvider).getCustomerGroupQualifierProvider()
						: null;
			}



			if (qualifierProvider != null && qualifierProvider.canApply(indexedProperty))
			{
				final Qualifier qualifier = qualifierProvider.getCurrentQualifier();
				fieldQualifier = qualifier != null ? qualifier.toFieldQualifier() : null;
				if ((salesAreaQualifierProvider != null && salesAreaQualifierProvider.canApply(indexedProperty))
						|| (userPriceGroupQualifierProvider != null && userPriceGroupQualifierProvider.canApply(indexedProperty))
						|| (customerGroupQualifierProvider != null && customerGroupQualifierProvider.canApply(indexedProperty)))
				{
					final UserModel user = userService.getCurrentUser();
					if (user instanceof B2BCustomerModel)
					{
						String upgSuffix = StringUtils.EMPTY;
						if (userPriceGroupQualifierProvider instanceof GallagherUserDiscountGroupQualifierProvider)
						{
							final UserPriceGroup userPriceGroup = (UserPriceGroup) getSessionService().getAttribute(Europe1Constants.PARAMS.UPG);
							if(userPriceGroup != null) {
								upgSuffix = FIELDNAME_SEPARATOR + userPriceGroup.getCode();
							}
						}

						if (salesAreaQualifierProvider != null && salesAreaQualifierProvider.canApply(indexedProperty))
						{
							final Qualifier salesAreaQualifier = salesAreaQualifierProvider.getCurrentQualifier();
							final String salesAreaFieldQualifier = salesAreaQualifier != null
									? salesAreaQualifier.toFieldQualifier()
									: StringUtils.EMPTY;
							fieldQualifier = fieldQualifier + FIELDNAME_SEPARATOR + salesAreaFieldQualifier;

						}
						if (userPriceGroupQualifierProvider != null && userPriceGroupQualifierProvider.canApply(indexedProperty))
						{
							final Qualifier upgQualifier = userPriceGroupQualifierProvider.getCurrentQualifier();
							final String upgFieldQualifier = upgQualifier != null ? upgQualifier.toFieldQualifier() : StringUtils.EMPTY;

							fieldQualifier = fieldQualifier + FIELDNAME_SEPARATOR + upgFieldQualifier + upgSuffix;

						}
						if (customerGroupQualifierProvider != null && customerGroupQualifierProvider.canApply(indexedProperty))
						{
							final Qualifier customerGroupQualifier = customerGroupQualifierProvider.getCurrentQualifier();
							final String customerGroupFieldQualifier = customerGroupQualifier != null
									? customerGroupQualifier.toFieldQualifier()
									: StringUtils.EMPTY;

							fieldQualifier = fieldQualifier + FIELDNAME_SEPARATOR + customerGroupFieldQualifier;

						}
					}
				}
			}
			return getFieldNameProvider().getFieldName(indexedProperty, fieldQualifier, fieldType);
		}
		else
		{
			return super.translateFromProperty(searchQuery, indexedProperty, fieldType);
		}
	}

	/**
	 * @return the salesAreaService
	 */
	public GallagherSalesAreaService getSalesAreaService()
	{
		return salesAreaService;
	}

	/**
	 * @param salesAreaService
	 *           the salesAreaService to set
	 */
	public void setSalesAreaService(final GallagherSalesAreaService salesAreaService)
	{
		this.salesAreaService = salesAreaService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
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

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

}
