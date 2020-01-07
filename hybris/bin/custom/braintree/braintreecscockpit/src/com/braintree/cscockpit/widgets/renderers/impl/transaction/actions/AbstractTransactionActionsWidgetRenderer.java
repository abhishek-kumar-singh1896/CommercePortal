package com.braintree.cscockpit.widgets.renderers.impl.transaction.actions;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemAppender;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.enumeration.EnumerationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.braintree.cscockpit.constraint.AmountConstraint;
import com.braintree.converters.BraintreeTransactionDetailConverter;
import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.controllers.TransactionController;
import com.braintree.cscockpit.widgets.models.impl.TransactionItemWidgetModel;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.hybris.data.BrainTreeResponseResultData;
import com.braintree.model.BrainTreeTransactionDetailModel;


public abstract class AbstractTransactionActionsWidgetRenderer
		extends
			AbstractCsWidgetRenderer<Widget<TransactionItemWidgetModel, TransactionController>>
{
	protected static final Logger LOG = Logger.getLogger(AbstractTransactionActionsWidgetRenderer.class);
	private PopupWidgetHelper popupWidgetHelper;
	private EnumerationService enumerationService;
	private CsBrainTreeFacade csBrainTreeFacade;
	private ItemAppender<TypedObject> detailItemAppender;
	private BraintreeTransactionDetailConverter transactionDetailPopulator;

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final HtmlBasedComponent htmlBasedComponent)
	{
		final Div content = new Div();
		final TypedObject transaction = widget.getWidgetController().getCurrentTransaction();
		if (transaction != null && transaction.getObject() instanceof BrainTreeTransactionDetailModel)
		{
			final BrainTreeTransactionDetailModel currentTransaction = (BrainTreeTransactionDetailModel) transaction.getObject();

			createTransactionIdField(widget, createDiv(content), currentTransaction.getId());
			renderCustomFields(currentTransaction, content, widget);
		}
		else
		{
			final Label dummyLabel = new Label(LabelUtils.getLabel(widget, "noTransactionSelected", new Object[0]));
			dummyLabel.setParent(content);
		}
		return content;
	}

	protected abstract void renderCustomFields(BrainTreeTransactionDetailModel currentTransaction, Div content,
			Widget<TransactionItemWidgetModel, TransactionController> widget);

	protected Div getButtonBox(final Div content)
	{
		final Div buttonBox = new Div();
		buttonBox.setClass("btTransactionActionButtonDiv");
		buttonBox.setParent(content);
		return buttonBox;
	}

	protected Div getButtonBox(final Div content, final String style)
	{
		final Div buttonBox = new Div();
		buttonBox.setClass(style);
		buttonBox.setParent(content);
		return buttonBox;
	}

	protected Textbox createTransactionIdField(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final Div parent, final String transactionId)
	{
		final Textbox textbox = UIElementUtils.createTextField(widget, parent, "transactionId", transactionId);
		textbox.setReadonly(true);
		return textbox;
	}

	protected Div createDiv(final Div content)
	{
		final Div cloneFieldsBox = new Div();
		cloneFieldsBox.setParent(content);
		cloneFieldsBox.setClass("btTransactionActionDiv");
		return cloneFieldsBox;
	}

	protected void createButton(final Widget<TransactionItemWidgetModel, TransactionController> widget, final String style,
			final HtmlBasedComponent component, final String buttonLabelName, final EventListener eventListener)
	{
		final Button button = new Button();
		button.setLabel(LabelUtils.getLabel(widget, buttonLabelName));
		button.setParent(component);
		button.setStyle(style);
		if (eventListener != null)
		{
			button.addEventListener(Events.ON_CLICK, eventListener);
		}
	}

	protected void createButton(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final HtmlBasedComponent component, final String buttonLabelName, final EventListener eventListener)
	{
		createButton(widget, "btnblue", component, buttonLabelName, eventListener);
	}

	protected Textbox createAmountField(final Widget<TransactionItemWidgetModel, TransactionController> widget, final Div content,
			final String amount)
	{
		final Textbox transactionAmount = UIElementUtils.createTextField(widget, content, "transactionAmount", getAmount(amount));
		transactionAmount.setConstraint(new AmountConstraint());
		return transactionAmount;
	}

	private String getAmount(final String amount)
	{
		final String[] split = amount.split(" ");
		return split[0];
	}

	protected void processResult(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final BrainTreeResponseResultData result)
	{
		try
		{
			if (result.isSuccess())
			{
				showSuccessMessage(widget, result);
				handlePostProcess(result);
			}
			else
			{
				showErrorMessage(result);
			}
		}
		catch (final InterruptedException e)
		{
			LOG.debug("Errors occurred while showing message box!", e);
		}
	}

	private void handlePostProcess(final BrainTreeResponseResultData result)
	{
		getPopupWidgetHelper().dismissCurrentPopup();
		final BrainTreeTransactionDetailModel detailModel = getTransactionDetailPopulator().convert(
				result.getTransactionEntryData());
		final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(detailModel);
		getDetailItemAppender().add(itemTypedObject, 1L);
	}

	protected abstract void showErrorMessage(BrainTreeResponseResultData result) throws InterruptedException;

	protected abstract void showSuccessMessage(Widget<TransactionItemWidgetModel, TransactionController> widget,
			BrainTreeResponseResultData result) throws InterruptedException;

	protected EnumerationService getEnumerationService()
	{
		return this.enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
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

	public ItemAppender<TypedObject> getDetailItemAppender()
	{
		return detailItemAppender;
	}

	@Required
	public void setDetailItemAppender(final ItemAppender<TypedObject> detailItemAppender)
	{
		this.detailItemAppender = detailItemAppender;
	}

	public BraintreeTransactionDetailConverter getTransactionDetailPopulator()
	{
		return transactionDetailPopulator;
	}

	@Required
	public void setTransactionDetailPopulator(final BraintreeTransactionDetailConverter transactionDetailPopulator)
	{
		this.transactionDetailPopulator = transactionDetailPopulator;
	}
}
