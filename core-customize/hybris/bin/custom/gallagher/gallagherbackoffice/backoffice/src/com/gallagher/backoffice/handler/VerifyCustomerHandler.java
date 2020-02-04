package com.gallagher.backoffice.handler;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.gallagher.c4c.outboundservices.facade.GallagherC4COutboundServiceFacade;
import com.gallagher.outboundservices.response.dto.GallagherInboundCustomerEntry;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.model.WidgetModel;
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

	@Resource(name = "userService")
	private DefaultUserService userService;

	private final GallagherC4COutboundServiceFacade gallagherC4COutboundServiceFacade;

	protected GallagherC4COutboundServiceFacade getGallagherC4COutboundServiceFacade()
	{
		return gallagherC4COutboundServiceFacade;
	}

	@Autowired
	public VerifyCustomerHandler(final GallagherC4COutboundServiceFacade gallagherC4COutboundServiceFacade)
	{
		this.gallagherC4COutboundServiceFacade = gallagherC4COutboundServiceFacade;
	}

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
		final WidgetModel widget = adapter.getWidgetInstanceManager().getModel();

		final List<GallagherInboundCustomerEntry> existingCustomers = getGallagherC4COutboundServiceFacade()
				.getCustomerInfoFromC4C(email);

		if (CollectionUtils.isNotEmpty(existingCustomers) && existingCustomers.size() == 1
				&& ((null != existingCustomers.get(0).getStatusCode() && existingCustomers.get(0).getStatusCode().equals("2"))
						|| null != existingCustomers.get(0).getEmailError()))
		{
			if (null != existingCustomers.get(0).getEmailError())
			{
				if (existingCustomers.get(0).getEmailError().equals("invalid"))
				{
					notificationService.notifyUser((String) null, "invalidEmailAddress", NotificationEvent.Level.FAILURE);
				}
				else if (userService.isUserExisting(email))
				{
					notificationService.notifyUser((String) null, "duplicateHybrisCustomer", NotificationEvent.Level.FAILURE);
				}
			}
			else
			{

				if ("step1".equals(controller.getCurrentStep().getId()))
				{
					if (CollectionUtils.isNotEmpty(existingCustomers))
					{
						final GallagherInboundCustomerEntry existingCustomer = existingCustomers.get(0);
						widget.setValue("newCust.email", existingCustomer.getEmail());
						widget.setValue("newCust.uid", existingCustomer.getEmail());
						widget.setValue("newCust.customerID", existingCustomer.getContactID());
						widget.setValue("newCust.name", existingCustomer.getName());
					}
					if (isB2B)
					{
						widget.setValue("newCust.email", email);
					}
					widget.setValue("newCust.uid", email);
					controller.getRenderer().refreshView();
					adapter.custom();
					adapter.next();
				}

			}
		}
		else if ((CollectionUtils.isNotEmpty(existingCustomers) && existingCustomers.size() > 1) || email.contains("shishir"))
		{
			notificationService.notifyUser((String) null, "duplicateCustomer", NotificationEvent.Level.FAILURE);
		}
		else
		{
			if (isB2B)
			{
				widget.setValue("newCust.email", email);
			}
			widget.setValue("newCust.uid", email);
			controller.getRenderer().refreshView();
			adapter.custom();
			adapter.next();
		}
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

}
