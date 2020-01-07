package com.braintree.cscockpit.widgets.renderers.impl.transaction.actions;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS_POSTFIX;
import static org.zkoss.util.resource.Labels.getLabel;

import de.hybris.platform.cockpit.widgets.Widget;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.braintree.configuration.service.BrainTreeConfigService;
import com.braintree.constants.BraintreecscockpitConstants;
import com.braintree.cscockpit.widgets.controllers.TransactionController;
import com.braintree.cscockpit.widgets.models.impl.TransactionItemWidgetModel;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.hybris.data.BrainTreeResponseResultData;
import com.braintree.model.BrainTreeTransactionDetailModel;


public class TransactionCloneWidgetRenderer extends AbstractTransactionActionsWidgetRenderer
{
	private BrainTreeConfigService brainTreeConfigService;

	@Override
	protected void renderCustomFields(final BrainTreeTransactionDetailModel currentTransaction, final Div content,
			final Widget<TransactionItemWidgetModel, TransactionController> widget)
	{
		final Textbox amountField = createAmountField(widget, createDiv(content), currentTransaction.getAmount());
		final Checkbox submitForSettlementField = createSubmitForSettlementField(widget, createDiv(content));

		createButton(widget, getButtonBox(content), "cloneButton",
				createCloneEventListener(widget, currentTransaction, amountField, submitForSettlementField));
	}

	private Checkbox createSubmitForSettlementField(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final Div content)
	{
		return UIElementUtils.createCheckboxField(widget, content, "submitForSettlement", getBrainTreeConfigService()
				.getSettlementConfigParameter().booleanValue());
	}

	private EventListener createCloneEventListener(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final BrainTreeTransactionDetailModel currentTransaction, final Textbox amountField,
			final Checkbox submitForSettlementField)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				TransactionCloneWidgetRenderer.this.handleCloneButtonClickEvent(widget, event, currentTransaction, amountField,
						submitForSettlementField);
			}
		};
	}

	private void handleCloneButtonClickEvent(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final Event event, final BrainTreeTransactionDetailModel currentTransaction, final Textbox amountField,
			final Checkbox submitForSettlementField)
	{
		final BrainTreeResponseResultData resendResult = getCsBrainTreeFacade().cloneTransaction(currentTransaction,
				amountField.getValue(), submitForSettlementField.isChecked());
		processResult(widget, resendResult);
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
			errorMessage = getLabel(BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_CLONE_ERROR);
		}
		Messagebox.show(errorMessage,
				getLabel(BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_CLONE_TITLE), Messagebox.OK,
				Messagebox.ERROR);
	}

	@Override
	protected void showSuccessMessage(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final BrainTreeResponseResultData result) throws InterruptedException
	{
		Messagebox.show(createdSuccessMessage(result),
				getLabel(BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_CLONE_TITLE), Messagebox.OK,
				Messagebox.INFORMATION);
	}

	private String createdSuccessMessage(final BrainTreeResponseResultData resendResult)
	{
		final String message = getLabel(BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_CLONE_SUCCESS);
		final String messagePostfix = getLabel(WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS_POSTFIX);
		if (StringUtils.isNotBlank(resendResult.getTransactionId()))
		{
			return String.format("%s %s: %s", message, messagePostfix, resendResult.getTransactionId());
		}
		return message;
	}

	/**
	 * @return the brainTreeConfigService
	 */
	public BrainTreeConfigService getBrainTreeConfigService()
	{
		return brainTreeConfigService;
	}

	/**
	 * @param brainTreeConfigService
	 *           the brainTreeConfigService to set
	 */
	public void setBrainTreeConfigService(final BrainTreeConfigService brainTreeConfigService)
	{
		this.brainTreeConfigService = brainTreeConfigService;
	}
}
