package com.braintree.cscockpit.widgets.renderers.impl.transaction.actions;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_REFUND_ERROR_GO_TO_ORDER;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS_POSTFIX;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_REFUND_TITLE;
import static org.zkoss.util.resource.Labels.getLabel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemAppender;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.braintree.constants.BraintreecscockpitConstants;
import com.braintree.cscockpit.widgets.controllers.TransactionController;
import com.braintree.cscockpit.widgets.models.impl.TransactionItemWidgetModel;
import com.braintree.cscockpit.widgets.services.management.impl.BraintreeDefaultOrderCsManagementService;
import com.braintree.hybris.data.BrainTreeResponseResultData;
import com.braintree.model.BrainTreeTransactionDetailModel;


public class TransactionRefundWidgetRenderer extends AbstractTransactionActionsWidgetRenderer
{
	private ItemAppender<TypedObject> orderItemAppender;
	private BraintreeDefaultOrderCsManagementService orderCsManagementService;

	@Override
	protected void renderCustomFields(final BrainTreeTransactionDetailModel currentTransaction, final Div content,
			final Widget<TransactionItemWidgetModel, TransactionController> widget)
	{
		final Textbox amountField = createAmountField(widget, createDiv(content), currentTransaction.getAmount());

		final OrderModel linkedOrder = getLinkedOrder(currentTransaction);
		if (linkedOrder != null)
		{
			createButton(widget, "btngreen", getButtonBox(content, "btTransactionActionGreenButtonDiv"), "goToOrderButton",
					createGoToOrderEventListener(widget, linkedOrder));
		}
		else
		{
			createButton(widget, getButtonBox(content), "refundButton",
					createRefundEventListener(widget, currentTransaction, amountField));
		}
	}

	private OrderModel getLinkedOrder(final BrainTreeTransactionDetailModel currentTransaction)
	{
		final OrderModel linkedOrder = currentTransaction.getLinkedOrder();
		if (linkedOrder == null)
		{
			return getOrderCsManagementService().getLinkedOrderWithBraintree(currentTransaction);
		}
		return linkedOrder;
	}

	private EventListener createGoToOrderEventListener(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final OrderModel linkedOrder)
	{

		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				handleGoToOrderButtonClickEvent(widget, linkedOrder);
			}
		};
	}

	private void handleGoToOrderButtonClickEvent(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final OrderModel linkedOrder)
	{
		final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(linkedOrder);
		this.getOrderItemAppender().add(itemTypedObject, 1L);
	}


	private EventListener createRefundEventListener(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final BrainTreeTransactionDetailModel currentTransaction, final Textbox amountField)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				handleRefundButtonClickEvent(widget, event, currentTransaction, amountField);
			}
		};
	}

	private void handleRefundButtonClickEvent(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final Event event, final BrainTreeTransactionDetailModel currentTransaction, final Textbox amountField)
					throws InterruptedException
	{
		final OrderModel linkedOrder = currentTransaction.getLinkedOrder();
		if (linkedOrder != null)
		{
			Messagebox.show(getLabel(WIDGET_MESSAGE_REFUND_ERROR_GO_TO_ORDER), getLabel(WIDGET_REFUND_TITLE), Messagebox.OK,
					Messagebox.ERROR);
		}
		else
		{
			final BrainTreeResponseResultData resendResult = getCsBrainTreeFacade().refundTransaction(currentTransaction,
					amountField.getValue());
			processResult(widget, resendResult);
		}
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
			errorMessage = getLabel(
					BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_REFUND_ERROR);
		}
		Messagebox.show(errorMessage, getLabel(WIDGET_REFUND_TITLE), Messagebox.OK, Messagebox.ERROR);
	}

	@Override
	protected void showSuccessMessage(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final BrainTreeResponseResultData result) throws InterruptedException
	{
		Messagebox.show(createSuccessMessage(result), getLabel(WIDGET_REFUND_TITLE), Messagebox.OK, Messagebox.INFORMATION);
	}

	private String createSuccessMessage(final BrainTreeResponseResultData resendResult)
	{
		final String message = getLabel(
				BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_REFUND_SUCCESS);
		final String messagePostfix = getLabel(WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS_POSTFIX);
		if (StringUtils.isNotBlank(resendResult.getTransactionId()))
		{
			return String.format("%s %s: %s", message, messagePostfix, resendResult.getTransactionId());
		}
		return message;
	}

	public ItemAppender<TypedObject> getOrderItemAppender()
	{
		return orderItemAppender;
	}

	public void setOrderItemAppender(final ItemAppender<TypedObject> orderItemAppender)
	{
		this.orderItemAppender = orderItemAppender;
	}

	public BraintreeDefaultOrderCsManagementService getOrderCsManagementService()
	{
		return orderCsManagementService;
	}

	public void setOrderCsManagementService(final BraintreeDefaultOrderCsManagementService orderCsManagementService)
	{
		this.orderCsManagementService = orderCsManagementService;
	}
}
