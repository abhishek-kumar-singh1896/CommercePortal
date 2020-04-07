package com.gallagher.backoffice.handler;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.client.RestClientException;

import com.gallagher.core.services.GallagherCurrencyService;
import com.gallagher.core.services.GallagherLanguageService;
import com.gallagher.keycloak.outboundservices.service.GallagherKeycloakService;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import com.sap.hybris.sapcustomerb2b.outbound.B2BRegistrationEvent;


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

	@Resource(name = "gallagherCurrencyService")
	private GallagherCurrencyService gallagherCurrencyService;

	@Resource(name = "gallagherLanguageService")
	private GallagherLanguageService gallagherLanguaeService;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "eventService")
	private EventService b2bEventService;

	@Resource(name = "modelService")
	private ModelService modelService;

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
			adapter.custom();
			adapter.done();
			pushToC4C(adapter, isUserExist);
		}
		catch (final RestClientException | OAuth2Exception exception)
		{
			LOGGER.error("Exception occured while creationg user in Keycloak : " + exception);
			notificationService.notifyUser((String) null, "c4cConnectionError", NotificationEvent.Level.FAILURE);
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

	private void pushToC4C(final FlowActionHandlerAdapter adapter, final boolean isUserExist)
	{
		if (adapter.getWidgetInstanceManager().getModel().getValue("newCust", CustomerModel.class) instanceof B2BCustomerModel)
		{
			final B2BRegistrationEvent b2bRegistrationEvent = new B2BRegistrationEvent();
			final B2BCustomerModel b2bCustomer = (B2BCustomerModel) adapter.getWidgetInstanceManager().getModel().getValue("newCust",
					CustomerModel.class);
			b2bCustomer.setIsUserExist(isUserExist);
			modelService.save(b2bCustomer);
			final B2BUnitModel defaultB2BUnit = adapter.getWidgetInstanceManager().getModel().getValue("newCust.defaultB2BUnit",
					B2BUnitModel.class);
			final String isoCode = defaultB2BUnit.getAddresses().iterator().next().getCountry().getIsocode();
			String securityB2BisoCode = new String("securityB2B");
			securityB2BisoCode = securityB2BisoCode.concat(isoCode);
			b2bRegistrationEvent.setCustomer(b2bCustomer);
			b2bRegistrationEvent.setBaseStore(baseStoreService.getBaseStoreForUid(securityB2BisoCode));
			b2bRegistrationEvent.setSite(baseSiteService.getBaseSiteForUID(securityB2BisoCode));
			if (b2bCustomer.getSessionCurrency() != null)
			{
				b2bRegistrationEvent.setCurrency(b2bCustomer.getSessionCurrency());
			}
			else
			{
				b2bRegistrationEvent.setCurrency(gallagherCurrencyService.getCurrencyByIsoCode(isoCode));
			}
			if (b2bCustomer.getSessionLanguage() != null)
			{
				b2bRegistrationEvent.setLanguage(b2bCustomer.getSessionLanguage());
			}
			else
			{
				b2bRegistrationEvent.setLanguage(gallagherLanguaeService.getLanguageByisoCode("en"));
			}
			b2bEventService.publishEvent(b2bRegistrationEvent);
		}
	}
}
