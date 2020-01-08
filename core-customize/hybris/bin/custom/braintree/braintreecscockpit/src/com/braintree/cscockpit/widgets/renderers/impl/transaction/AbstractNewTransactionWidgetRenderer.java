package com.braintree.cscockpit.widgets.renderers.impl.transaction;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_TRANSACTION_CREATE_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS_POSTFIX;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_TRANSACTION_CREATE_TITLE;
import static org.zkoss.util.resource.Labels.getLabel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemAppender;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.braintree.configuration.service.BrainTreeConfigService;
import com.braintree.converters.BraintreeTransactionDetailConverter;
import com.braintree.cscockpit.data.BrainTreeInfo;
import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.controllers.TransactionController;
import com.braintree.cscockpit.widgets.models.impl.TransactionItemWidgetModel;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.customfield.service.CustomFieldsService;
import com.braintree.hybris.data.BrainTreeResponseResultData;
import com.braintree.hybris.data.BraintreeTransactionEntryData;
import com.braintree.model.BrainTreeTransactionDetailModel;


public abstract class AbstractNewTransactionWidgetRenderer
		extends AbstractCsWidgetRenderer<Widget<TransactionItemWidgetModel, TransactionController>>
{
	protected static final Logger LOG = Logger.getLogger(AbstractNewTransactionWidgetRenderer.class);
	private PopupWidgetHelper popupWidgetHelper;
	private CsBrainTreeFacade csBrainTreeFacade;
	private ItemAppender<TypedObject> detailItemAppender;
	private BraintreeTransactionDetailConverter transactionDetailPopulator;
	private BrainTreeConfigService brainTreeConfigService;
	private CustomFieldsService customFieldsService;

	protected void processResult(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final BrainTreeResponseResultData result)
	{
		try
		{
			if (result.isSuccess())
			{
				showSuccessMessage(widget, result);
				getPopupWidgetHelper().dismissCurrentPopup();
				handleTransactionSelectItem(result.getTransactionEntryData());
			}
			else
			{
				showErrorMessage(result.getErrorMessage());
			}
		}
		catch (final InterruptedException e)
		{
			LOG.debug("Errors occurred while showing message box!", e);
		}
	}

	private void handleTransactionSelectItem(final BraintreeTransactionEntryData transaction)
	{
		final BrainTreeTransactionDetailModel detailModel = getTransactionDetailPopulator().convert(transaction);
		final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(detailModel);
		getDetailItemAppender().add(itemTypedObject, 1L);
	}

	private void showSuccessMessage(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final BrainTreeResponseResultData result) throws InterruptedException
	{
		Messagebox.show(getLabel(WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS) + " "
				+ getLabel(WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS_POSTFIX) + ": " + result.getTransactionId(),
				getLabel(WIDGET_TRANSACTION_CREATE_TITLE), Messagebox.OK, Messagebox.INFORMATION);
	}

	protected void showErrorMessage(String errorMessageFromResult) throws InterruptedException
	{
		String errorMessage;
		if (StringUtils.isBlank(errorMessageFromResult))
		{
			errorMessage = getLabel(WIDGET_MESSAGE_TRANSACTION_CREATE_ERROR);
		}
		else
		{
			errorMessage = errorMessageFromResult;
		}
		Messagebox.show(errorMessage, getLabel(WIDGET_TRANSACTION_CREATE_TITLE), Messagebox.OK, Messagebox.ERROR);
	}

	protected Textbox createTextField(final Widget<TransactionItemWidgetModel, TransactionController> widget, final Div parent,
			final String label)
	{
		return createTextField(widget, parent, label, null);
	}

	protected Textbox createTextField(final Widget<TransactionItemWidgetModel, TransactionController> widget, final Div parent,
			final String label, final String exampleMessageLabel)
	{
		final Div divParent = createDiv(parent);
		final Textbox textField = UIElementUtils.createTextField(widget, divParent, label, StringUtils.EMPTY);
		if (StringUtils.isNotBlank(exampleMessageLabel))
		{
			final Label titleOneLabel = new Label(LabelUtils.getLabel(widget, exampleMessageLabel, new Object[0]));
			titleOneLabel.setClass("btNewTransactionTextFieldLabel");
			titleOneLabel.setParent(divParent);
		}
		return textField;
	}

	protected Div createDiv(final Div content)
	{
		final Div cloneFieldsBox = new Div();
		cloneFieldsBox.setParent(content);
		cloneFieldsBox.setClass("btNewTransactionDiv");
		return cloneFieldsBox;
	}

	protected void setCustomFields(final BrainTreeInfo brainTreeInfo, final Textbox custom)
	{
		final String customValue = custom.getValue();
		final String[] splitByParametersPair = StringUtils.split(customValue, ",");

		final Map<String, String> defaultCustomFields = customFieldsService.getDefaultCustomFieldsMap();

		for (final String key : defaultCustomFields.keySet())
		{
			brainTreeInfo.setCustom(key, defaultCustomFields.get(key));
		}

		if (splitByParametersPair.length > 0)
		{
			for (final String parameter : splitByParametersPair)
			{
				final String[] spitedParameter = StringUtils.split(parameter, ":");
				if (spitedParameter.length == 2)
				{
					brainTreeInfo.setCustom(spitedParameter[0], spitedParameter[1]);
				}
			}
		}
	}

	protected void addPrettyTitle(final Widget<TransactionItemWidgetModel, TransactionController> widget, final Div content,
			final String label)
	{
		final Div titleBox = new Div();
		titleBox.setClass("btNewTransactionCustomPrettyTitleDiv");
		titleBox.setParent(content);

		final Label titleOneLabel = new Label(LabelUtils.getLabel(widget, label, new Object[0]));
		titleOneLabel.setParent(titleBox);
		titleOneLabel.setClass("btNewTransactionCustomPrettyTitleLabel");
	}

	protected void createButton(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final HtmlBasedComponent component, final String buttonLabelName, final EventListener eventListener)
	{
		final Div saveButtonBox = new Div();
		saveButtonBox.setClass("btNewTransactionCustomButton");
		saveButtonBox.setParent(component);

		final Button button = new Button();
		button.setLabel(LabelUtils.getLabel(widget, buttonLabelName, new Object[0]));
		button.setParent(saveButtonBox);
		button.addEventListener(Events.ON_CLICK, eventListener);
	}

	public PopupWidgetHelper getPopupWidgetHelper()
	{
		return popupWidgetHelper;
	}

	public void setPopupWidgetHelper(final PopupWidgetHelper popupWidgetHelper)
	{
		this.popupWidgetHelper = popupWidgetHelper;
	}

	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

	public void setCsBrainTreeFacade(final CsBrainTreeFacade csBrainTreeFacade)
	{
		this.csBrainTreeFacade = csBrainTreeFacade;
	}

	public ItemAppender<TypedObject> getDetailItemAppender()
	{
		return detailItemAppender;
	}

	public void setDetailItemAppender(final ItemAppender<TypedObject> detailItemAppender)
	{
		this.detailItemAppender = detailItemAppender;
	}

	public BraintreeTransactionDetailConverter getTransactionDetailPopulator()
	{
		return transactionDetailPopulator;
	}

	public void setTransactionDetailPopulator(final BraintreeTransactionDetailConverter transactionDetailPopulator)
	{
		this.transactionDetailPopulator = transactionDetailPopulator;
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

	public CustomFieldsService getCustomFieldsService()
	{
		return customFieldsService;
	}

	public void setCustomFieldsService(final CustomFieldsService customFieldsService)
	{
		this.customFieldsService = customFieldsService;
	}

}
