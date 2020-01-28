package com.gallagher.backoffice.handler;

import de.hybris.platform.b2b.model.B2BCustomerModel;
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
		String email;
		boolean isB2B = false;
		if (adapter.getWidgetInstanceManager().getModel().getValue("newCust", CustomerModel.class) instanceof B2BCustomerModel)
		{
			isB2B = true;
			email = adapter.getWidgetInstanceManager().getModel().getValue("newCust.email", String.class);
		}
		else
		{
			email = adapter.getWidgetInstanceManager().getModel().getValue("newCust.uid", String.class);
		}

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
			if (CollectionUtils.isNotEmpty(existingCustomers) && existingCustomers.size() > 1)
			{
				notificationService.notifyUser((String) null, "duplicateCustomer", NotificationEvent.Level.FAILURE);
			}
			else
			{
				if ("step1".equals(controller.getCurrentStep().getId()))
				{
					if (CollectionUtils.isNotEmpty(existingCustomers))
					{
						//TODO add data from C4C
					}
					if (isB2B)
					{
						adapter.getWidgetInstanceManager().getModel().setValue("newCust.email", email);
					}
					adapter.getWidgetInstanceManager().getModel().setValue("newCust.uid", email);
					controller.getRenderer().refreshView();
					adapter.custom();
					adapter.next();
				}
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
