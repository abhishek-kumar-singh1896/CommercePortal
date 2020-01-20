package com.gallagher.backoffice.handler;

/**
 * @author shilpiverma
 *
 */

import de.hybris.platform.b2badvancebackoffice.data.CreateCustomerForm;
import de.hybris.platform.jalo.user.Customer;

//import de.hybris.platform.b2badvancebackoffice.data.CreateCustomerForm;

import java.util.Map;

import org.apache.log4j.Logger;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
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

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final CreateCustomerForm createCustomerForm = null;
		/* final CreateCustomerForm cc = */
		final Customer d = adapter.getWidgetInstanceManager().getModel().getValue("newCust", Customer.class);

		//		final String email = cc.getEmail();
		//		System.out.println("shilpi>>>" + email);
		final ConfigurableFlowController controller = (ConfigurableFlowController) adapter.getWidgetInstanceManager()
				.getWidgetslot().getAttribute("widgetController");
		adapter.done();
		final CreateCustomerForm cc11 = adapter.getWidgetInstanceManager().getModel().getValue("newCust", CreateCustomerForm.class);
		final String email1 = cc11.getEmail();
		System.out.println("shilpi11>>>" + email1);
		doSomething();
		LOG.info("shilpi ...........................................................");
		if ("step1".equals(controller.getCurrentStep().getId()))
		{
			createCustomerForm.setCustomerID("shilpi");
			createCustomerForm.setDescription("description");
			createCustomerForm.setName("shilp_name");
			createCustomerForm.setUid("shilpi_uid");
			adapter.getWidgetInstanceManager().getModel().setValue("newCust", createCustomerForm);
			controller.getRenderer().refreshView();
			adapter.custom();
		}
		return;
	}

	public void doSomething()
	{
		final String ss = "shilpi";
	}
}
