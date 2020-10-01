package com.gallagher.backoffice.handler;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gallagher.c4c.outboundservices.facade.GallagherC4COutboundServiceFacade;
import com.gallagher.keycloak.outboundservices.service.GallagherKeycloakService;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaLogicHandler;


/**
 * @author ankituniyal
 *
 *         This handler pushes the customer to keycloak, mindtouch and C4C when the user info is updated from backoffice
 *
 */
public class GallagherEditB2BCustomerHandler extends DefaultEditorAreaLogicHandler
{
	private static final Logger LOG = Logger.getLogger(GallagherEditB2BCustomerHandler.class);

	private static final String KEY_CLOAK_ID_EXISTS = "Key Cloak Id already present";
	private static final String EMAIL_ALREADY_PRESENT = "duplicateHybrisCustomer";
	private static final String SECURITY_B2B_GLOBAL = "securityB2BGlobal";
	private static final String INPUT_OBJECT = "$_inputObject";
	private static final String SECURITY_B2B = "securityB2B";
	private static final String SECURITY = "security";

	private GallagherC4COutboundServiceFacade gallagherC4COutboundServiceFacade;
	private Converter<UserModel, CustomerData> customerConverter;
	private GallagherKeycloakService gallagherKeycloakService;
	private NotificationService notificationService;
	private StoreSessionFacade storeSessionFacade;
	private BaseStoreService baseStoreService;
	private BaseSiteService baseSiteService;
	private SessionService sessionService;
	private ModelService modelService;

	@Override
	public Object performSave(final WidgetInstanceManager widgetInstanceManager, final Object currentObject)
			throws ObjectSavingException
	{
		if (currentObject instanceof B2BCustomerModel)
		{
				final B2BCustomerModel b2bCustomerModel = (B2BCustomerModel) currentObject;
				final CustomerData b2bCustomer = getCustomerConverter().convert(b2bCustomerModel);
				b2bCustomer.setKeycloakGUID(b2bCustomerModel.getKeycloakGUID());
				final String email = b2bCustomer.getUid();

				LOG.debug("Checking if the customer is already present in keyCloak and C4C");
				final Boolean isEmailValid = isCustomerEmailValid(widgetInstanceManager, email);

				if (BooleanUtils.isFalse(isEmailValid))
				{
					/* getNotificationService().notifyUser(StringUtils.EMPTY, EMAIL_ALREADY_PRESENT, Level.FAILURE, null); */
					throw new ObjectSavingException(email, EMAIL_ALREADY_PRESENT, new Throwable(KEY_CLOAK_ID_EXISTS));
				}

				LOG.debug("Updating user profile in keycloak");

				if (isB2BCustomerModified(widgetInstanceManager, b2bCustomerModel))
				{
					getGallagherKeycloakService().updateKeyCloakUserProfile(b2bCustomer);
				}

				final Context ctx = new DefaultContext();
				ctx.addAttribute(ObjectFacade.CTX_PARAM_SUPPRESS_EVENT, Boolean.TRUE);
				widgetInstanceManager.getModel().setValue(INPUT_OBJECT, b2bCustomerModel);

				return updateStoreInSession(b2bCustomerModel, ctx);
		}
		LOG.debug("Customer is not B2B ");
		return super.performSave(widgetInstanceManager, currentObject);

	}

	/**
	 * This method returns if any of email, name or b2bUnit is modified
	 *
	 * @param widgetInstanceManager
	 * @param currentObject
	 * @return
	 */
	private boolean isB2BCustomerModified(final WidgetInstanceManager widgetInstanceManager,
			final B2BCustomerModel b2bCustomerModel)
	{
			final B2BCustomerModel originalCustomerModel = widgetInstanceManager.getModel().getValue(INPUT_OBJECT,
					B2BCustomerModel.class);
			final boolean isEmailModified = originalCustomerModel.getUid().equals(b2bCustomerModel.getUid()) ? false : true;
			final boolean isNameModified = originalCustomerModel.getName().equals(b2bCustomerModel.getName()) ? false : true;
			return isEmailModified || isNameModified;
	}

	/**
	 * This method checks, if the email is changed from commerce and already present in keycloak and C4C
	 *
	 * @param widgetInstanceManager
	 * @param uid
	 * @return
	 */
	private Boolean isCustomerEmailValid(final WidgetInstanceManager widgetInstanceManager, final String email)
	{
		Boolean isCustomerEmailValid = Boolean.TRUE;
		if (BooleanUtils.isFalse(widgetInstanceManager.getTitle().contains(email)))
		{
			final String existingKeyCloakId = getGallagherKeycloakService().getKeycloakUserFromEmail(email);

			if (StringUtils.isNotBlank(existingKeyCloakId))
			{
				LOG.debug("Customer with same email already exists in keycloak::" + existingKeyCloakId);
				isCustomerEmailValid = Boolean.FALSE;
			}

			else if (CollectionUtils
					.isNotEmpty(getGallagherC4COutboundServiceFacade().getCustomerInfoFromC4C(email, StringUtils.EMPTY)))
			{
				LOG.debug("Customer with same email already exists in C4C::" + email);
				isCustomerEmailValid = Boolean.FALSE;
			}
		}
		return isCustomerEmailValid;
	}

	private B2BCustomerModel updateStoreInSession(final B2BCustomerModel b2bCustomer, final Context ctx)
	{
		final B2BUnitModel defaultB2BUnit = b2bCustomer.getDefaultB2BUnit();
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
				defaultSite = getBaseSiteService().getBaseSiteForUID(SECURITY_B2B_GLOBAL);
			}
		}

		final BaseSiteModel baseSite = defaultSite;

		final CurrencyModel currency = getCurrency(defaultBaseStore, b2bCustomer.getSessionCurrency());
		final LanguageModel language = getLanguage(defaultBaseStore, b2bCustomer.getSessionLanguage());

		b2bCustomer.setSessionCurrency(currency);
		b2bCustomer.setSessionLanguage(language);
		getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				getBaseSiteService().setCurrentBaseSite(baseSite, Boolean.FALSE);
				getStoreSessionFacade().setCurrentLanguage(language.getIsocode());
				getStoreSessionFacade().setCurrentCurrency(currency.getIsocode());
				try
				{
					getObjectFacade().save(b2bCustomer, ctx);
				}
				catch (final ObjectSavingException e)
				{
					LOG.error("Error while saving B2B customer :: " + e.getMessage());
				}
			}
		});

		return b2bCustomer;
	}


	/**
	 * Returns the currency for this customer. If user selected currency is not in allowed list of currencies then use
	 * the default currency.
	 *
	 * @param baseStore
	 *           base store
	 * @param sessionCurrency
	 *           session currency selected by user
	 * @return currency to be set
	 */
	private CurrencyModel getCurrency(final BaseStoreModel baseStore, final CurrencyModel sessionCurrency)
	{
		CurrencyModel currency;
		if (sessionCurrency == null)
		{
			currency = baseStore.getDefaultCurrency();
		}
		else
		{
			currency = baseStore.getCurrencies().contains(sessionCurrency) ? sessionCurrency : baseStore.getDefaultCurrency();
		}
		return currency;
	}

	/**
	 * Returns the language for this customer. If user selected language is not in allowed list of languages then use the
	 * default language.
	 *
	 * @param baseStore
	 *           base store
	 * @param sessionLanguage
	 *           session language selected by user
	 * @return language to be set
	 */
	private LanguageModel getLanguage(final BaseStoreModel baseStore, final LanguageModel sessionLanguage)
	{
		LanguageModel language;
		if (sessionLanguage == null)
		{
			language = baseStore.getDefaultLanguage();
		}
		else
		{
			language = baseStore.getLanguages().contains(sessionLanguage) ? sessionLanguage : baseStore.getDefaultLanguage();
		}
		return language;
	}

	/**
	 * @return the gallagherKeycloakService
	 */
	public GallagherKeycloakService getGallagherKeycloakService()
	{
		return gallagherKeycloakService;
	}

	/**
	 * @param gallagherKeycloakService
	 *           the gallagherKeycloakService to set
	 */
	public void setGallagherKeycloakService(final GallagherKeycloakService gallagherKeycloakService)
	{
		this.gallagherKeycloakService = gallagherKeycloakService;
	}

	/**
	 * @return the customerConverter
	 */
	public Converter<UserModel, CustomerData> getCustomerConverter()
	{
		return customerConverter;
	}

	/**
	 * @param customerConverter
	 *           the customerConverter to set
	 */
	public void setCustomerConverter(final Converter<UserModel, CustomerData> customerConverter)
	{
		this.customerConverter = customerConverter;
	}

	/**
	 * @return the notificationService
	 */
	public NotificationService getNotificationService()
	{
		return notificationService;
	}

	/**
	 * @param notificationService
	 *           the notificationService to set
	 */
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
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

	/**
	 * @return the storeSessionFacade
	 */
	public StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	/**
	 * @param storeSessionFacade
	 *           the storeSessionFacade to set
	 */
	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the gallagherC4COutboundServiceFacade
	 */
	public GallagherC4COutboundServiceFacade getGallagherC4COutboundServiceFacade()
	{
		return gallagherC4COutboundServiceFacade;
	}

	/**
	 * @param gallagherC4COutboundServiceFacade
	 *           the gallagherC4COutboundServiceFacade to set
	 */
	public void setGallagherC4COutboundServiceFacade(final GallagherC4COutboundServiceFacade gallagherC4COutboundServiceFacade)
	{
		this.gallagherC4COutboundServiceFacade = gallagherC4COutboundServiceFacade;
	}
}
