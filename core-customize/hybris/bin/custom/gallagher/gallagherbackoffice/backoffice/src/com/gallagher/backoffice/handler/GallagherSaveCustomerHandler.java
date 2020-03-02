package com.gallagher.backoffice.handler;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.client.RestClientException;

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
			final String keycloakGUID = gallagherKeycloakService.createKeycloakUser(customerData);
			widget.setValue("newCust.keycloakGUID", keycloakGUID);

			widget.setValue("newCust.uid", email);
			controller.getRenderer().refreshView();
			adapter.custom();
			adapter.done();
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

}
