package com.braintree.cscockpit.widgets.renderers.impl.transaction.actions;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_ICON_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_ICON_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_VOID_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_VOID_ORDER_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_VOID_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_VOID_VALIDATION_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_VOID_ASK_MESSAGE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_VOID_ASK_TITLE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_VOID_TITLE;
import static org.zkoss.util.resource.Labels.getLabel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.ordercancel.OrderCancelException;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;

import com.braintree.converters.BraintreeTransactionDetailConverter;
import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.controllers.TransactionManagementActionsWidgetController;
import com.braintree.cscockpit.widgets.models.impl.TransactionItemWidgetModel;
import com.braintree.cscockpit.widgets.services.management.OrderCsManagementService;
import com.braintree.hybris.data.BrainTreeResponseResultData;
import com.braintree.hybris.data.BraintreeTransactionEntryData;
import com.braintree.model.BrainTreeTransactionDetailModel;
import com.braintreegateway.Transaction;


public class TransactionManagementActionsWidgetRenderer
		extends AbstractCsWidgetRenderer<Widget<TransactionItemWidgetModel, TransactionManagementActionsWidgetController>>
{
	private static final Logger LOG = Logger.getLogger(TransactionManagementActionsWidgetRenderer.class);
	private PopupWidgetHelper popupWidgetHelper;
	private CsBrainTreeFacade csBrainTreeFacade;
	private OrderCsManagementService orderCsManagementService;
	private BraintreeTransactionDetailConverter transactionDetailPopulator;

	@Override
	protected HtmlBasedComponent createContentInternal(
			final Widget<TransactionItemWidgetModel, TransactionManagementActionsWidgetController> widget,
			final HtmlBasedComponent rootContainer)
	{
		final Div component = new Div();
		component.setSclass("orderManagementActionsWidget");

		this.createButton(widget, component, "refresh", createRefreshEventListener(widget), false, true);
		this.createButton(widget, component, "cancelTransaction", createVoidEventListener(widget),
				!widget.getWidgetController().isVoidPossible(), widget.getWidgetController().canUserVoid());
		this.createButton(widget, component, "cloneTransaction", "defaultCsCloneTransactionWidgetConfig",
				"csCloneTransaction-Popup", "csCloneTransaction", "popup.cloneTransactionCreate",
				!widget.getWidgetController().isClonePossible(), widget.getWidgetController().canUserClone());

		this.createButton(widget, component, "refundTransaction", "defaultCsRefundTransactionWidgetConfig",
				"csRefundTransaction-Popup", "csRefundTransaction", "popup.refundTransactionCreate",
				!widget.getWidgetController().isRefundPossible(), widget.getWidgetController().canUserRefund());

		this.createButton(widget, component, "submitForSettlementTransaction",
				"defaultCsSubmitForSettlementTransactionWidgetConfig", "csSubmitForSettlementTransaction-Popup",
				"csSubmitForSettlementTransaction", "popup.submitForSettlementCreate",
				!widget.getWidgetController().isSubmitForSettlementPossible(),
				widget.getWidgetController().canUserSubmitForSettlement());

		return component;
	}

	private EventListener createRefreshEventListener(
			final Widget<TransactionItemWidgetModel, TransactionManagementActionsWidgetController> widget)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				final BrainTreeTransactionDetailModel transactionDetail = (BrainTreeTransactionDetailModel) widget
						.getWidgetController().getTransaction().getObject();

				updateTransactionPage(transactionDetail, widget);
			}
		};
	}

	private EventListener createVoidEventListener(
			final Widget<TransactionItemWidgetModel, TransactionManagementActionsWidgetController> widget)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				TransactionManagementActionsWidgetRenderer.this.handleVoidButtonClickEvent(widget);
			}
		};
	}

	private void handleVoidButtonClickEvent(
			final Widget<TransactionItemWidgetModel, TransactionManagementActionsWidgetController> widget)
	{
		try
		{
			Messagebox.show(getLabel(WIDGET_VOID_ASK_MESSAGE), getLabel(WIDGET_VOID_ASK_TITLE), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new EventListener()
					{
						@Override
						public void onEvent(final Event evt) throws InterruptedException
						{
							if ("onOK".equals(evt.getName()))
							{
								cancelTransaction(widget);
							}
						}
					});
		}
		catch (final InterruptedException exception)
		{
			LOG.debug("Errors occurred while showing message box!", exception);
		}
	}


	private void cancelTransaction(final Widget<TransactionItemWidgetModel, TransactionManagementActionsWidgetController> widget)
			throws InterruptedException
	{
		try
		{
			final BrainTreeTransactionDetailModel transactionDetail = (BrainTreeTransactionDetailModel) widget.getWidgetController()
					.getTransaction().getObject();

			final OrderModel linkedOrderWithBraintree = getOrderCsManagementService().getLinkedOrderWithBraintree(transactionDetail);
			if (linkedOrderWithBraintree != null)
			{
				getOrderCsManagementService().createOrderCancellationRequest(transactionDetail, linkedOrderWithBraintree);
				Messagebox.show(getLabel(WIDGET_MESSAGE_VOID_SUCCESS), getLabel(WIDGET_VOID_TITLE), 1, WIDGET_ICON_SUCCESS);
				updateTransactionPage(transactionDetail, widget);
			}
			else
			{
				voidBraintreeTransaction(widget);
			}
		}
		catch (final OrderCancelException exception)
		{
			Messagebox.show(getLabel(WIDGET_MESSAGE_VOID_ORDER_ERROR), getLabel(WIDGET_VOID_TITLE), 1, WIDGET_ICON_ERROR);
			LOG.debug("Can not cancel order: " + exception.getMessage(), exception);
		}
		catch (final ValidationException exception)
		{
			Messagebox.show(getLabel(WIDGET_MESSAGE_VOID_VALIDATION_ERROR), getLabel(WIDGET_VOID_TITLE), 1, WIDGET_ICON_ERROR);
			LOG.debug("Validation error while order cancellation: " + exception.getMessage(), exception);
		}
	}

	private void voidBraintreeTransaction(
			final Widget<TransactionItemWidgetModel, TransactionManagementActionsWidgetController> widget)
	{
		final BrainTreeTransactionDetailModel transactionDetail = (BrainTreeTransactionDetailModel) widget.getWidgetController()
				.getTransaction().getObject();
		final BrainTreeResponseResultData resendResult = getCsBrainTreeFacade().voidTransaction(transactionDetail);

		String result;
		String icon;

		if (resendResult.isSuccess())
		{
			transactionDetail.setStatus(Transaction.Status.VOIDED.toString());
			result = getLabel(WIDGET_MESSAGE_VOID_SUCCESS);
			icon = WIDGET_ICON_SUCCESS;
		}
		else
		{
			if (StringUtils.isNotBlank(resendResult.getErrorMessage()))
			{
				result = resendResult.getErrorMessage();
			}
			else
			{
				result = getLabel(WIDGET_MESSAGE_VOID_ERROR);
			}
			icon = WIDGET_ICON_ERROR;
		}
		try
		{
			Messagebox.show(result, getLabel(WIDGET_VOID_TITLE), 1, icon);
			widget.getWidgetController().dispatchEvent((String) null, widget, (Map) null);
		}
		catch (final InterruptedException exception)
		{
			LOG.debug("Errors occurred while showing message box!", exception);
		}
	}

	protected void createButton(final Widget<TransactionItemWidgetModel, TransactionManagementActionsWidgetController> widget,
			final Div container, final String buttonLabelName, final String springWidgetName, final String popupCode,
			final String cssClass, final String popupTitleLabelName, final boolean disabled, final boolean canUsedPerformAction)
	{
		final EventListener eventListener = new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				handleButtonClickEvent(widget, event, container, springWidgetName, popupCode, cssClass, popupTitleLabelName);
			}
		};

		this.createButton(widget, container, buttonLabelName, eventListener, disabled, canUsedPerformAction);
	}

	private void createButton(final Widget<TransactionItemWidgetModel, TransactionManagementActionsWidgetController> widget,
			final HtmlBasedComponent component, final String buttonLabelName, final EventListener eventListener,
			final boolean disabled, final boolean canUserPerformAction)
	{
		if (canUserPerformAction)
		{

			final Button button = new Button();
			button.setLabel(LabelUtils.getLabel(widget, buttonLabelName, new Object[0]));
			button.setParent(component);
			button.setDisabled(disabled);
			if (eventListener != null)
			{
				button.addEventListener(Events.ON_CLICK, eventListener);
			}
		}
	}

	protected void handleButtonClickEvent(
			final Widget<TransactionItemWidgetModel, TransactionManagementActionsWidgetController> widget, final Event event,
			final Div container, final String springWidgetName, final String popupCode, final String cssClass,
			final String popupTitleLabelName)
	{
		this.getPopupWidgetHelper().createPopupWidget(container, springWidgetName, popupCode, cssClass,
				LabelUtils.getLabel(widget, popupTitleLabelName, new Object[0]), 400);
	}

	private void updateTransactionPage(final BrainTreeTransactionDetailModel transactionDetail,
			final Widget<TransactionItemWidgetModel, TransactionManagementActionsWidgetController> widget)
	{
		final BraintreeTransactionEntryData actualTransaction = getCsBrainTreeFacade()
				.findBrainTreeTransaction(transactionDetail.getId());

		final BrainTreeTransactionDetailModel originalModel = getTransactionDetailPopulator().convert(actualTransaction);

		final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(originalModel);
		widget.getWidgetController().setTransaction(itemTypedObject);
		widget.getWidgetController().dispatchEvent((String) null, widget, (Map) null);
	}

	protected PopupWidgetHelper getPopupWidgetHelper()
	{
		return this.popupWidgetHelper;
	}

	@Required
	public void setPopupWidgetHelper(final PopupWidgetHelper popupWidgetHelper)
	{
		this.popupWidgetHelper = popupWidgetHelper;
	}

	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

	@Required
	public void setCsBrainTreeFacade(final CsBrainTreeFacade csBrainTreeFacade)
	{
		this.csBrainTreeFacade = csBrainTreeFacade;
	}

	public OrderCsManagementService getOrderCsManagementService()
	{
		return orderCsManagementService;
	}

	public void setOrderCsManagementService(final OrderCsManagementService orderCsManagementService)
	{
		this.orderCsManagementService = orderCsManagementService;
	}

	public BraintreeTransactionDetailConverter getTransactionDetailPopulator()
	{
		return transactionDetailPopulator;
	}

	public void setTransactionDetailPopulator(final BraintreeTransactionDetailConverter transactionDetailPopulator)
	{
		this.transactionDetailPopulator = transactionDetailPopulator;
	}
}
