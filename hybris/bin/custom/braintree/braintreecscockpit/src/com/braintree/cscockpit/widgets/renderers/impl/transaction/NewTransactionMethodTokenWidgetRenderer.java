package com.braintree.cscockpit.widgets.renderers.impl.transaction;

import de.hybris.platform.cockpit.widgets.Widget;

import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.braintree.cscockpit.constraint.RequiredAmountConstraint;
import com.braintree.cscockpit.constraint.RequiredConstraint;
import com.braintree.cscockpit.data.BrainTreeInfo;
import com.braintree.cscockpit.widgets.controllers.TransactionController;
import com.braintree.cscockpit.widgets.models.impl.TransactionItemWidgetModel;
import com.braintree.hybris.data.BrainTreeResponseResultData;


public class NewTransactionMethodTokenWidgetRenderer extends AbstractNewTransactionWidgetRenderer
{
	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final HtmlBasedComponent htmlBasedComponent)
	{
		final Div content = new Div();

		addPrettyTitle(widget, content, "paymentDetails");

		final Div paymentDetailsContent = new Div();
		paymentDetailsContent.setParent(content);

		final Textbox token = createTextField(widget, paymentDetailsContent, "token", "required");
		token.setConstraint(new RequiredConstraint());

		final Textbox amount = createTextField(widget, paymentDetailsContent, "amount", "required");
		amount.setConstraint(new RequiredAmountConstraint());
		final Textbox tax = createTextField(widget, paymentDetailsContent, "tax");
		final Textbox custom = createTextField(widget, paymentDetailsContent, "custom", "exampleCustom");
		custom.setMultiline(true);

		createButton(widget, content, "saveButton", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				NewTransactionMethodTokenWidgetRenderer.this.handleSaveButtonClickEvent(widget, token, amount, tax, custom);
			}
		});

		return content;
	}

	private void handleSaveButtonClickEvent(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final Textbox token, final Textbox amount, final Textbox tax, final Textbox custom)
	{
		final BrainTreeInfo brainTreeInfo = new BrainTreeInfo();
		brainTreeInfo.setAmount(amount.getValue()).setTax(tax.getValue());
		setCustomFields(brainTreeInfo, custom);
		brainTreeInfo.setPaymentMethodToken(token.getValue());

		final BrainTreeResponseResultData transaction = getCsBrainTreeFacade().createTransaction(brainTreeInfo);
		processResult(widget, transaction);
	}

}
