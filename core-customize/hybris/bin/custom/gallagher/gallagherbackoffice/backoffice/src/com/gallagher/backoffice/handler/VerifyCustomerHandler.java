package com.gallagher.backoffice.handler;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;

import java.util.List;

//import de.hybris.platform.b2badvancebackoffice.data.CreateCustomerForm;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.validator.routines.EmailValidator;
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

	@Resource
	private DefaultUserService userService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		/* final CreateCustomerForm cc = */
		final String email = adapter.getWidgetInstanceManager().getModel().getValue("newCust.uid", String.class);

		final ConfigurableFlowController controller = (ConfigurableFlowController) adapter.getWidgetInstanceManager()
				.getWidgetslot().getAttribute("widgetController");

		final boolean existingHybris = getExistingCustomerFromHybris(email);
		final boolean emailValidity = isValidEmail(email);

		if (Boolean.FALSE.compareTo(emailValidity) == 0)
		{
			notificationService.notifyUser((String) null, "invalidEmailAddress", NotificationEvent.Level.FAILURE);
		}
		// This if needs to be removed once correct C4C endpoint connected
		else if ("vikram.bishnoi@nagarro.com".equals(email))
		{
			notificationService.notifyUser((String) null, "duplicateC4CCustomer", NotificationEvent.Level.FAILURE);
		}
		else if (Boolean.TRUE.compareTo(existingHybris) == 0)
		{
			notificationService.notifyUser((String) null, "duplicateHybrisCustomer", NotificationEvent.Level.FAILURE);
		}
		else
		{
			final List<CustomerModel> existingCustomers = getExistingCustomerFromC4C(email);
			if (CollectionUtils.isEmpty(existingCustomers))
			{
				if ("step1".equals(controller.getCurrentStep().getId()))
				{
					adapter.getWidgetInstanceManager().getModel().setValue("newCust.uid", email);
					controller.getRenderer().refreshView();
					adapter.custom();
					adapter.next();
				}
			}
			else if (existingCustomers.size() == 1)
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

	private boolean getExistingCustomerFromHybris(final String uid)
	{
		return userService.isUserExisting(uid);
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

	private boolean isValidEmail(final String email)
	{
		final EmailValidator eValidator = EmailValidator.getInstance();
		return eValidator.isValid(email) ? true : false;
	}

}