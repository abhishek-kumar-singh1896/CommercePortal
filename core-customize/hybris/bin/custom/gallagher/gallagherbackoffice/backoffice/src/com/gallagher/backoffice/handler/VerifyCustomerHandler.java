package com.gallagher.backoffice.handler;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

//import de.hybris.platform.b2badvancebackoffice.data.CreateCustomerForm;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * @author shilpiverma
 *
 */
public class VerifyCustomerHandler implements FlowActionHandler
{
	private static final Logger LOG = Logger.getLogger(VerifyCustomerHandler.class.getName());

	@Resource
	private NotificationService notificationService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		/* final CreateCustomerForm cc = */
		final String email = adapter.getWidgetInstanceManager().getModel().getValue("newCust.uid", String.class);

		final ConfigurableFlowController controller = (ConfigurableFlowController) adapter.getWidgetInstanceManager()
				.getWidgetslot().getAttribute("widgetController");

		final List<CustomerModel> existingCustomers = getExistingCustomerFromC4C(email);

		// This if needs to be removed once correct C4C endpoint connected
		if ("vikram.bishnoi@nagarro.com".equals(email))
		{
			notificationService.notifyUser((String) null, "duplicateCustomer", NotificationEvent.Level.FAILURE);
		}
		else if (CollectionUtils.isEmpty(existingCustomers) || existingCustomers.size() == 1)
		{
			if ("step1".equals(controller.getCurrentStep().getId()))
			{
				adapter.getWidgetInstanceManager().getModel().setValue("newCust.uid", email);
				controller.getRenderer().refreshView();
				adapter.custom();
				adapter.next();
			}
		}
		else
		{
			notificationService.notifyUser((String) null, "duplicateCustomer", NotificationEvent.Level.FAILURE);
		}
		return;
	}

	/**
	 * @param email
	 * @return
	 */
	private List<CustomerModel> getExistingCustomerFromC4C(final String email)
	{
		// TODO
		return null;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

}
