package com.braintree.cscockpit.widgets.renderers.impl.transaction.actions;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.*;
import static org.zkoss.util.resource.Labels.getLabel;

import com.braintree.constants.BraintreeConstants;
import com.braintree.cscockpit.widgets.controllers.TransactionManagementActionsWidgetController;
import com.braintree.hybris.data.BraintreeTransactionEntryData;
import com.braintree.transaction.service.BrainTreePaymentTransactionService;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.BusinessProcessService;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.braintree.cscockpit.widgets.controllers.TransactionController;
import com.braintree.cscockpit.widgets.models.impl.TransactionItemWidgetModel;
import com.braintree.hybris.data.BrainTreeResponseResultData;
import com.braintree.model.BrainTreeTransactionDetailModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;


public class TransactionSubmitForSettlementWidgetRenderer extends AbstractTransactionActionsWidgetRenderer
{

	private BrainTreePaymentTransactionService brainTreePaymentTransactionService;


	@Override
	protected void renderCustomFields(final BrainTreeTransactionDetailModel currentTransaction, final Div content,
			final Widget<TransactionItemWidgetModel, TransactionController> widget)
	{
		final Textbox amountField = createAmountField(widget, createDiv(content), currentTransaction.getAmount());

		createButton(widget, getButtonBox(content), "submitForSettlementButton",
				createSubmitForSettlementEventListener(widget, currentTransaction, amountField));
	}

	private EventListener createSubmitForSettlementEventListener(
			final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final BrainTreeTransactionDetailModel currentTransaction, final Textbox amountField)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				TransactionSubmitForSettlementWidgetRenderer.this.handleSfSButtonClickEvent(widget, event, currentTransaction,
						amountField);
			}
		};
	}

	private void handleSfSButtonClickEvent(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final Event event, final BrainTreeTransactionDetailModel currentTransaction, final Textbox amountField)
	{
		continueSubmitOrder(currentTransaction, amountField);
		getPopupWidgetHelper().getCurrentPopup().onClose();
		try {
			showMessage();
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
	}

	private void showMessage() throws InterruptedException
	{
		Messagebox.show(getLabel(WIDGET_MESSAGE_TRANSACTION_SFS_REFRESH), getLabel(WIDGET_TRANSACTION_SFS_TITLE), Messagebox.OK, Messagebox.INFORMATION);
	}

	@Override
	protected void showSuccessMessage(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final BrainTreeResponseResultData result) throws InterruptedException
	{
		Messagebox.show(getLabel(WIDGET_MESSAGE_TRANSACTION_SFS_SUCCESS) + " "+
				getLabel(WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS_POSTFIX)+ ": " + result.getTransactionId(),
				getLabel(WIDGET_TRANSACTION_SFS_TITLE), Messagebox.OK, Messagebox.INFORMATION);
	}

	@Override
	protected void showErrorMessage(final BrainTreeResponseResultData result) throws InterruptedException
	{
		String errorMessage;
		if (StringUtils.isNotBlank(result.getErrorMessage()))
		{
			errorMessage = result.getErrorMessage();
		}
		else
		{
			errorMessage = getLabel(WIDGET_MESSAGE_TRANSACTION_SFS_ERROR);
		}
		Messagebox.show(errorMessage, getLabel(WIDGET_TRANSACTION_SFS_TITLE), Messagebox.OK, Messagebox.ERROR);
	}

	private void continueSubmitOrder(final BrainTreeTransactionDetailModel currentTransaction, final Textbox amountField)
	{
		getBrainTreePaymentTransactionService().continueSubmitOrder(currentTransaction, new BigDecimal(amountField.getValue()));
	}

	public BrainTreePaymentTransactionService getBrainTreePaymentTransactionService() {
		return brainTreePaymentTransactionService;
	}

	public void setBrainTreePaymentTransactionService(BrainTreePaymentTransactionService brainTreePaymentTransactionService) {
		this.brainTreePaymentTransactionService = brainTreePaymentTransactionService;
	}
}
