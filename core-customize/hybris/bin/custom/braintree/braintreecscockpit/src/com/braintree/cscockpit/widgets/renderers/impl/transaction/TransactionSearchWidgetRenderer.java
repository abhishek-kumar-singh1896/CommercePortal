package com.braintree.cscockpit.widgets.renderers.impl.transaction;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.model.data.DataObject;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextSearchCommand;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemAppender;
import de.hybris.platform.cscockpit.widgets.controllers.search.SearchCommandController;
import de.hybris.platform.cscockpit.widgets.models.impl.TextSearchWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractSearcherWidgetRenderer;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

import com.braintree.cscockpit.constraint.EmailConstraint;
import com.braintree.converters.BraintreeTransactionDetailConverter;
import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.cscockpit.widgets.services.search.generic.query.DefaultTransactionSearchQueryBuilder;
import com.braintree.hybris.data.BraintreeTransactionEntryData;
import com.braintree.model.BrainTreeTransactionDetailModel;


public class TransactionSearchWidgetRenderer extends AbstractSearcherWidgetRenderer
{
	private Date transactionSearchStartDate;
	private Date transactionSearchEndDate;
	private BraintreeTransactionDetailConverter transactionDetailPopulator;
	private ItemAppender<TypedObject> detailItemAppender;
	private CsBrainTreeFacade csBrainTreeFacade;

	@Override
	protected HtmlBasedComponent createSearchPane(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget)
	{
		final Div searchPane = new Div();
		final Datebox startDate = createFromDateField(widget, searchPane);
		final Datebox endDate = createToDateField(widget, searchPane);
		final Textbox transactionId = createPayerEmailSearchField(widget, searchPane);
		UIElementUtils.addSeparator(searchPane);
		final Textbox customerID = createCustomerIDField(widget, searchPane);
		final Textbox customerEmail = createCustomerEmailField(widget, searchPane);
		final Listbox transactionStatus = UIElementUtils.createTransactionStatusDropdownField(widget, searchPane,
				"transactionStatus");
		UIElementUtils.addSeparator(searchPane);
		final Listbox riskDecision = UIElementUtils.createTransactionRiskDecisionDropdownField(widget, searchPane, "riskDecision");
		final Button searchBtn = createSearchButton(widget, searchPane);

		this.attachSearchEventListener(widget,
				createSearchEventListener(widget, transactionId, customerID, startDate, endDate, customerEmail, transactionStatus, riskDecision),
				new AbstractComponent[]
				{transactionId, customerID, customerEmail, searchBtn});
		return searchPane;
	}

	protected Textbox createPayerEmailSearchField(final ListboxWidget widget, final Div parent)
	{
		return createSearchTextField(widget, parent, "transactionId",
				DefaultTransactionSearchQueryBuilder.TextField.TRANSACTION_ID, "SearchForTransaction_TransactionID_input");
	}

	protected Textbox createCustomerIDField(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget, final Div parent)
	{
		return this.createSearchTextField(widget, parent, "customer", DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_ID,
				"SearchForTransaction_CustomerID_input");
	}

	protected Button createSearchButton(final ListboxWidget widget, final Div parent)
	{
		return createButton(widget, parent, "searchBtn", "SearchForTransaction_Search_button");
	}

	protected EventListener createSearchEventListener(final ListboxWidget widget, final InputElement transactionId,
			final InputElement customerId, final InputElement startDate, final InputElement endDate,
			final InputElement customerEmail, final Listbox transactionStatus, final Listbox riskDecision)
	{
		return new TransactionSearchWidgetRenderer.SearchEventListener(widget, transactionId, customerId, startDate, endDate,
				customerEmail, transactionStatus, riskDecision);
	}

	private Datebox createFromDateField(final ListboxWidget widget, final Div parent)
	{
		final Datebox startDate = UIElementUtils.createSearchDateField(widget, parent, "startDate", true);
		transactionSearchStartDate = startDate.getValue();
		startDate.setConstraint(new StartDateConstraint());
		return startDate;
	}

	private Datebox createToDateField(final ListboxWidget widget, final Div parent)
	{
		final Datebox endDate = UIElementUtils.createSearchDateField(widget, parent, "endDate", false);
		transactionSearchEndDate = endDate.getValue();
		endDate.setConstraint(new EndDateConstraint());
		return endDate;
	}

	protected Textbox createCustomerEmailField(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget, final Div parent)
	{
		final Textbox customerEmail = this.createSearchTextField(widget, parent, "customerEmail",
				DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_EMAIL, "SearchForTransaction_CustomerEmail_input");
		customerEmail.setConstraint(new EmailConstraint());
		return customerEmail;
	}

	@Override
	protected void renderListbox(final Listbox listBox, final ListboxWidget widget, final HtmlBasedComponent rootContainer)
	{
		final TextSearchWidgetModel widgetModel = (TextSearchWidgetModel) widget.getWidgetModel();
		if (widgetModel != null)
		{
			final List items = widgetModel.getItems();
			if (items != null && !items.isEmpty())
			{
				final Listhead headRow = new Listhead();
				headRow.setParent(listBox);
				populateHeaderRow(widget, headRow);
				UIElementUtils.createAndAttachCustomListheader(widget, headRow, "actions");
				UIElementUtils.createAndAttachCustomListheader(widget, headRow, "actions");

				final Iterator itemsIterator = items.iterator();
				while (itemsIterator.hasNext())
				{
					final DataObject metaItem = (DataObject) itemsIterator.next();
					final Listitem row = new Listitem();
					row.setParent(listBox);
					row.setSclass("csSearchResultRow");
					populateDataRow(row, (BraintreeTransactionEntryData) metaItem.getItem());
					addSelectOrderButton(widget, metaItem, row);
					addTransactionDetailsButton(widget, metaItem, row);
				}
			}
		}
	}

	private void addSelectOrderButton(final ListboxWidget widget, final DataObject metaItem, final Listitem row)
	{
		final TypedObject order = getCockpitTypeService().wrapItem(metaItem.getMeta(ItemModel.class));
		final EventListener selectItemEventListener = createSelectItemEventListener(widget, order);
		row.addEventListener("onOK", selectItemEventListener);
		UIElementUtils.createAndAttachCustomButton(widget, row, "selectBtn", "btngreen", selectItemEventListener, order);
	}

	private void addTransactionDetailsButton(final ListboxWidget widget, final DataObject metaItem, final Listitem row)
	{
		if (metaItem.getItem() instanceof BraintreeTransactionEntryData)
		{
			final EventListener selectItemEventListener = this.createSelectDetailItemEventListener(widget, metaItem);
			row.addEventListener("onOK", selectItemEventListener);
			UIElementUtils.createAndAttachCustomButton(widget, row, "detailBtn", "btnblue", selectItemEventListener);
		}
	}

	protected void populateDataRow(final Listitem row, final BraintreeTransactionEntryData item)
	{
		if (item != null)
		{
			final Listcell timestampCell = new Listcell(item.getDate());
			final Listcell transactionCell = new Listcell(item.getId());
			final Listcell typeCell = new Listcell(item.getType());
			final Listcell statusCell = new Listcell(item.getStatus());
			final Listcell customerCell = new Listcell(item.getCustomer());
			final Listcell paymentInfoCell = new Listcell(item.getPaymentInfo());
			final Listcell amountCell = new Listcell(item.getAmount());
			final Listcell riskDecisionCell = new Listcell(item.getRiskDecision());

			row.appendChild(transactionCell);
			row.appendChild(timestampCell);
			row.appendChild(typeCell);
			row.appendChild(statusCell);
			row.appendChild(customerCell);
			row.appendChild(paymentInfoCell);
			row.appendChild(amountCell);
			row.appendChild(riskDecisionCell);
		}
	}

	protected void populateHeaderRow(final Widget widget, final Listhead row)
	{
		final String[] columns = new String[]
		{"transactionId", "timestamp", "type", "status", "customerName", "paymentInfo", "amount", "riskDecision"};
		for (final String column : columns)
		{
			final Listheader header = new Listheader(LabelUtils.getLabel(widget, column, new Object[0]));
			header.setTooltiptext(column);
			row.appendChild(header);
		}
	}

	protected EventListener createSelectDetailItemEventListener(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			final DataObject metaItem)
	{
		return new TransactionSelectItemEventListener(widget, metaItem);
	}

	protected void handleTransactionSelectItem(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			final TypedObject item) throws Exception
	{
		this.getDetailItemAppender().add(item, 1L);
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

	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

	public void setCsBrainTreeFacade(final CsBrainTreeFacade csBrainTreeFacade)
	{
		this.csBrainTreeFacade = csBrainTreeFacade;
	}

	protected class SearchEventListener extends AbstractSearchEventListener
	{
		private final transient InputElement transactionId;
		private final transient Listbox transactionStatus;
		private final transient InputElement customerId;
		private final transient InputElement customerEmail;
		private final transient InputElement startDate;
		private final transient InputElement endDate;
		private final transient Listbox riskDecision;

		public SearchEventListener(final ListboxWidget widget, final InputElement transactionId, final InputElement customerId,
				final InputElement startDate, final InputElement endDate, final InputElement customerEmail,
				final Listbox transactionStatus, Listbox riskDecision)
		{
			super(widget);
			this.transactionId = transactionId;
			this.customerId = customerId;
			this.startDate = startDate;
			this.endDate = endDate;
			this.customerEmail = customerEmail;
			this.transactionStatus = transactionStatus;
			this.riskDecision = riskDecision;
		}

		@Override
		protected void fillSearchCommand(final DefaultCsTextSearchCommand command)
		{
			if (this.transactionId != null)
			{
				command.setText(DefaultTransactionSearchQueryBuilder.TextField.TRANSACTION_ID, this.transactionId.getText());
				command.setText(DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_ID, this.customerId.getText());
				command.setText(DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_EMAIL, this.customerEmail.getText());
				command.setText(DefaultTransactionSearchQueryBuilder.TextField.START_DATE, this.startDate.getText());
				command.setText(DefaultTransactionSearchQueryBuilder.TextField.END_DATE, this.endDate.getText());

				final Listitem selectedItem = this.transactionStatus.getSelectedItem();
				if (selectedItem != null && selectedItem.getValue() != null)
				{
					command.setText(DefaultTransactionSearchQueryBuilder.TextField.TRANSACTION_STATUS, selectedItem.getValue()
							.toString());
				}
				
				final Listitem selectedRiskDecision = this.riskDecision.getSelectedItem();
				if (selectedRiskDecision != null && selectedRiskDecision.getValue() != null)
				{
					command.setText(DefaultTransactionSearchQueryBuilder.TextField.RISK_DECISION, selectedRiskDecision.getValue()
							.toString());
				}
			}
		}
	}

	protected class TransactionSelectItemEventListener implements EventListener
	{
		private final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget;
		private final DataObject metaItem;

		public TransactionSelectItemEventListener(
				final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
				final DataObject metaItem)
		{
			this.widget = widget;
			this.metaItem = metaItem;
		}

		public void onEvent(final Event event) throws Exception
		{
			final BrainTreeTransactionDetailModel originalModel = getBrainTreeTransactionDetailModel();
			if (metaItem.getMetaData() != null)
			{
				final OrderModel orderModel = metaItem.getMetaData().get(OrderModel.class);
				originalModel.setLinkedOrder(orderModel);
			}
			final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(originalModel);
			handleTransactionSelectItem(this.widget, itemTypedObject);
		}

		private BrainTreeTransactionDetailModel getBrainTreeTransactionDetailModel()
		{
			final BrainTreeTransactionDetailModel originalModel = getTransactionDetailPopulator().convert(
					(BraintreeTransactionEntryData) metaItem.getItem());

			final BraintreeTransactionEntryData actualTransaction = getCsBrainTreeFacade().findBrainTreeTransaction(
					originalModel.getId());
			if (actualTransaction != null)
			{
				return getTransactionDetailPopulator().convert(actualTransaction);
			}
			return originalModel;
		}
	}

	private class EndDateConstraint implements Constraint
	{
		@Override
		public void validate(final Component paramComponent, final Object paramObject) throws WrongValueException
		{
			if (paramObject instanceof Date)
			{
				final Date tsEndDate = (Date) paramObject;
				if (!tsEndDate.before(new Date()))
				{
					throw new WrongValueException(paramComponent, Labels.getLabel("validation.incorrect.start.date"));
				}
				else
				{
					if (null != transactionSearchStartDate && tsEndDate.before(transactionSearchStartDate))
					{
						throw new WrongValueException(paramComponent, Labels.getLabel("validation.incorrect.date.order"));
					}
					transactionSearchEndDate = tsEndDate;
				}
			}
		}
	}

	private class StartDateConstraint implements Constraint
	{
		@Override
		public void validate(final Component paramComponent, final Object paramObject) throws WrongValueException
		{
			if (paramObject instanceof Date)
			{
				final Date tsStartDate = (Date) paramObject;
				if (!tsStartDate.before(new Date()))
				{
					throw new WrongValueException(paramComponent, Labels.getLabel("validation.incorrect.start.date"));
				}
				else
				{
					if (null != transactionSearchEndDate && tsStartDate.after(transactionSearchEndDate))
					{
						throw new WrongValueException(paramComponent, Labels.getLabel("validation.incorrect.date.order"));
					}
					transactionSearchStartDate = tsStartDate;
				}
			}
		}
	}
}
