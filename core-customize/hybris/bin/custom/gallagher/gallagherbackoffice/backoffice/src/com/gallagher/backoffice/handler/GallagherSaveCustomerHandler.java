package com.gallagher.backoffice.handler;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.client.RestClientException;

import com.gallagher.core.events.GallagherB2BRegistrationEvent;
import com.gallagher.core.services.GallagherMindTouchService;
import com.gallagher.keycloak.outboundservices.service.GallagherKeycloakService;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * @author shishirkant
 *
 */
public class GallagherSaveCustomerHandler implements FlowActionHandler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GallagherSaveCustomerHandler.class);

	@Resource
	private NotificationService notificationService;

	@Resource(name = "userService")
	private DefaultUserService userService;

	@Resource(name = "gallagherKeycloakService")
	private GallagherKeycloakService gallagherKeycloakService;

	@Resource(name = "customerNameStrategy")
	private CustomerNameStrategy customerNameStrategy;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "eventService")
	private EventService b2bEventService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "storeSessionFacade")
	private StoreSessionFacade storeSessionFacade;

	@Resource(name = "gallagherMindTouchService")
	private GallagherMindTouchService gallagherMindTouchService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final String email = adapter.getWidgetInstanceManager().getModel().getValue("newCust.uid", String.class);
		final String name = adapter.getWidgetInstanceManager().getModel().getValue("newCust.name", String.class);

		final ConfigurableFlowController controller = (ConfigurableFlowController) adapter.getWidgetInstanceManager()
				.getWidgetslot().getAttribute("widgetController");
		final WidgetModel widget = adapter.getWidgetInstanceManager().getModel();

		try
		{
			final CustomerData customerData = createCustomerData(email, name);
			boolean isUserExist = false;

			String keycloakGUID = gallagherKeycloakService.getKeycloakUserFromEmail(customerData.getEmail());

			if (keycloakGUID != null)
			{
				isUserExist = true;
			}
			else
			{
				keycloakGUID = gallagherKeycloakService.createKeycloakUser(customerData);
			}

			widget.setValue("newCust.keycloakGUID", keycloakGUID);

			widget.setValue("newCust.uid", email);
			controller.getRenderer().refreshView();
			final B2BCustomerModel savedCustomer = pushToC4C(adapter, isUserExist);
			//adapter.custom();
			pushToMindTouch(savedCustomer);

			adapter.done();
			if (savedCustomer != null)
			{
				notificationService.notifyUser((String) null, "b2bCustomerCreated", NotificationEvent.Level.SUCCESS, savedCustomer);
			}
		}
		catch (final RestClientException | OAuth2Exception exception)
		{
			LOGGER.error("Exception occured while creationg user in Keycloak : " + exception);
			notificationService.notifyUser((String) null, "c4cConnectionError", NotificationEvent.Level.FAILURE);
		}
	}

	/**
	 * @param savedCustomer
	 */
	private void pushToMindTouch(final B2BCustomerModel savedCustomer)
	{
		try
		{
			gallagherMindTouchService.pushCustomerToMindTouch(savedCustomer);
		}
		catch (final IOException e)
		{
			LOGGER.error("IOException while creating pushing user in mindTouch :: " + e.getMessage());
		}
		catch (final DocumentException e)
		{
			LOGGER.error("Document Exception while creating pushing user in mindTouch :: " + e.getMessage());
		}
	}

	/**
	 * @param email
	 * @param name
	 * @return customerData
	 */
	private CustomerData createCustomerData(final String email, final String name)
	{
		final CustomerData customerData = new CustomerData();
		customerData.setEmail(email);

		final String[] names = customerNameStrategy.splitName(name);
		if (names != null)
		{
			customerData.setFirstName(names[0]);
			customerData.setLastName(names[1]);
		}

		return customerData;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

	private B2BCustomerModel pushToC4C(final FlowActionHandlerAdapter adapter, final boolean isUserExist)
	{
		final B2BCustomerModel b2bCustomer;
		if (adapter.getWidgetInstanceManager().getModel().getValue("newCust", CustomerModel.class) instanceof B2BCustomerModel)
		{
			final GallagherB2BRegistrationEvent b2bRegistrationEvent = new GallagherB2BRegistrationEvent();
			b2bCustomer = (B2BCustomerModel) adapter.getWidgetInstanceManager().getModel().getValue("newCust", CustomerModel.class);
			final B2BUnitModel defaultB2BUnit = adapter.getWidgetInstanceManager().getModel().getValue("newCust.defaultB2BUnit",
					B2BUnitModel.class);
			String isoCode;
			b2bRegistrationEvent.setCustomer(b2bCustomer);
			BaseStoreModel defaultBaseStore = null;
			BaseSiteModel defaultSite = null;
			if (defaultB2BUnit.getAddresses().isEmpty())
			{
				isoCode = "Global";
				defaultBaseStore = baseStoreService.getBaseStoreForUid("securityB2BGlobal");
				defaultSite = baseSiteService.getBaseSiteForUID("securityB2BGlobal");
			}
			else
			{
				final List<BaseStoreModel> baseStores = baseStoreService.getAllBaseStores();
				isoCode = defaultB2BUnit.getAddresses().iterator().next().getCountry().getIsocode();
				boolean flag = false;
				for (final BaseStoreModel baseStore : baseStores)
				{
					if (baseStore.getUid().startsWith("security") && baseStore.getUid().contains(isoCode))
					{
						String securityB2BisoCode = new String("securityB2B");
						securityB2BisoCode = securityB2BisoCode.concat(isoCode);
						defaultBaseStore = baseStoreService.getBaseStoreForUid(securityB2BisoCode);
						defaultSite = baseSiteService.getBaseSiteForUID(securityB2BisoCode);
						flag = true;
						break;
					}
				}
				if (!flag)
				{
					defaultBaseStore = baseStoreService.getBaseStoreForUid("securityB2BGlobal");
					defaultSite = baseSiteService.getBaseSiteForUID("securityB2BGlobal");
				}
			}

			b2bRegistrationEvent.setBaseStore(defaultBaseStore);
			b2bRegistrationEvent.setSite(defaultSite);

			final CurrencyModel currency = getCurrency(defaultBaseStore, b2bCustomer.getSessionCurrency());
			b2bRegistrationEvent.setCurrency(currency);
			b2bCustomer.setSessionCurrency(currency);

			final LanguageModel language = getLanguage(defaultBaseStore, b2bCustomer.getSessionLanguage());
			b2bRegistrationEvent.setLanguage(language);
			b2bCustomer.setSessionLanguage(language);

			b2bCustomer.setIsUserExist(isUserExist);


			sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public void executeWithoutResult()
				{
					baseSiteService.setCurrentBaseSite(b2bRegistrationEvent.getSite(), false);
					storeSessionFacade.setCurrentLanguage(b2bRegistrationEvent.getLanguage().getIsocode());
					storeSessionFacade.setCurrentCurrency(b2bRegistrationEvent.getCurrency().getIsocode());
					modelService.save(b2bCustomer);
					b2bEventService.publishEvent(b2bRegistrationEvent);
				}
			});
		}
		else
		{
			b2bCustomer = null;
		}
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
	 * @return the gallagherMindTouchService
	 */
	public GallagherMindTouchService getGallagherMindTouchService()
	{
		return gallagherMindTouchService;
	}

	/**
	 * @param gallagherMindTouchService
	 *           the gallagherMindTouchService to set
	 */
	public void setGallagherMindTouchService(final GallagherMindTouchService gallagherMindTouchService)
	{
		this.gallagherMindTouchService = gallagherMindTouchService;
	}
}
