package com.braintree.cscockpit.widgets.renderers.utils;


import com.braintree.constants.BraintreeConstants;
import com.braintree.cscockpit.widgets.controllers.CustomerController;
import com.braintree.cscockpit.widgets.controllers.TransactionController;
import com.braintree.cscockpit.widgets.models.impl.CustomerItemWidgetModel;
import com.braintree.cscockpit.widgets.models.impl.TransactionItemWidgetModel;
import com.braintree.cscockpit.widgets.services.search.generic.query.DefaultTransactionSearchQueryBuilder;
import com.braintree.enums.BraintreeTransactionRiskDecision;
import com.braintree.enums.StoreInVault;
import com.braintree.model.BraintreeCustomerDetailsModel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextSearchCommand;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.search.SearchCommandController;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;
import de.hybris.platform.cscockpit.widgets.models.impl.TextSearchWidgetModel;
import de.hybris.platform.jalo.JaloSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;


public class UIElementUtils {

    private static final int MAX_ADDRESS_TITLE_LENGTH = 40;
    private static final String LONG_ADDRESS_FINALIZER = "...";

    private static final Set<String> RISK_DECISIONS = new HashSet<>();
    static {
        RISK_DECISIONS.add(BraintreeTransactionRiskDecision.NOTEVALUATED.getCode());
        RISK_DECISIONS.add(BraintreeTransactionRiskDecision.APPROVE.getCode());
        RISK_DECISIONS.add(BraintreeTransactionRiskDecision.REVIEW.getCode());
        RISK_DECISIONS.add(BraintreeTransactionRiskDecision.DECLINE.getCode());
    }

    private UIElementUtils() {
    }

    public static Datebox createSearchDateField(
            final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
            final Div parent, final String label, final boolean isFromDay) {
        final Label startDateLabel = new Label(LabelUtils.getLabel((Widget) widget, label, new Object[0]));
        final Datebox startDate = new Datebox();

        final Calendar cal = Calendar.getInstance();
        if (isFromDay) {
            cal.add(Calendar.DATE, -1);
        }
        startDate.setValue(cal.getTime());
        startDate.setFormat(BraintreeConstants.TRANSACTION_SEARCH_DATE_FORMAT);
        startDate.setConstraint("no empty");

        parent.appendChild(startDateLabel);
        parent.appendChild(startDate);
        return startDate;
    }

    public static void createAndAttachCustomListheader(
            final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
            final Listhead parent, final String label) {
        final Listheader colHeader = new Listheader(LabelUtils.getLabel((Widget) widget, label, new Object[0]));
        colHeader.setWidth("80px");
        parent.appendChild(colHeader);
    }

    public static void createAndAttachCustomButton(final ListboxWidget widget, final Listitem parent, final String lable,
                                                   final String style, final EventListener selectItemEventListener, final TypedObject typedObject) {
        final Listcell actionCell = new Listcell();
        actionCell.setParent(parent);
        final Button actionButton = new Button(LabelUtils.getLabel(widget, lable, new Object[0]));
        actionButton.setParent(actionCell);
        actionButton.setSclass(style);
        actionButton.addEventListener("onClick", selectItemEventListener);
        if (typedObject == null) {
            actionButton.setDisabled(Boolean.TRUE.booleanValue());
        }
    }

    public static void createAndAttachCustomButton(final ListboxWidget widget, final Listitem parent, final String lable,
                                                   final String style, final EventListener selectItemEventListener) {
        final Listcell actionCell = new Listcell();
        actionCell.setParent(parent);
        final Button actionButton = new Button(LabelUtils.getLabel(widget, lable, new Object[0]));
        actionButton.setParent(actionCell);
        actionButton.setSclass(style);
        actionButton.addEventListener("onClick", selectItemEventListener);
    }

    public static Listbox createTransactionStatusDropdownField(
            final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
            final Div parent, final String label) {
        final Label statusLabel = new Label(LabelUtils.getLabel((Widget) widget, label, new Object[0]));
        final Listbox statuses = new Listbox();
        statuses.setMold("select");
        Listitem item = statuses.appendItem("", "");
        statuses.selectItem(item);

        String selectedStatus = ((TextSearchWidgetModel)widget.getWidgetModel())
                .getSearchCommand() == null ? "" : ((TextSearchWidgetModel)widget.getWidgetModel()).getSearchCommand().getText(DefaultTransactionSearchQueryBuilder.TextField.TRANSACTION_STATUS, "");

        for (final com.braintreegateway.Transaction.Status status : com.braintreegateway.Transaction.Status.values()) {
            item = statuses.appendItem(status.name(), status.name());
            if (status.name().equals(selectedStatus)) {
                statuses.selectItem(item);
            }
        }

        parent.appendChild(statusLabel);
        parent.appendChild(statuses);
        return statuses;
    }
    
    public static Listbox createTransactionRiskDecisionDropdownField(
        final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
        final Div parent, final String label) {

        final Label riskDecisionLabel = new Label(LabelUtils.getLabel((Widget) widget, label, new Object[0]));
        final Listbox decisions = new Listbox();
        decisions.setMold("select");
        Listitem item = decisions.appendItem("", "");
        decisions.selectItem(item);

        String selectedRiskDecision = ((TextSearchWidgetModel)widget.getWidgetModel())
                .getSearchCommand() == null ? "" : ((TextSearchWidgetModel)widget.getWidgetModel()).getSearchCommand().getText(DefaultTransactionSearchQueryBuilder.TextField.RISK_DECISION, "");

        for (final String key : RISK_DECISIONS) {
            item = decisions.appendItem(key, key);
            if (key.equalsIgnoreCase(selectedRiskDecision)) {
                decisions.selectItem(item);
            }
        }

        parent.appendChild(riskDecisionLabel);
        parent.appendChild(decisions);

        return decisions;
    }

    public static Checkbox createCheckboxField(final Widget<TransactionItemWidgetModel, TransactionController> widget,
                                               final Div parent, final String label, final boolean checked) {
        final Checkbox checkbox = new Checkbox(LabelUtils.getLabel(widget, label, new Object[0]));
        checkbox.setChecked(checked);
        parent.appendChild(checkbox);
        return checkbox;
    }

    public static Textbox createTextField(
            final Widget<? extends DefaultMasterDetailListWidgetModel, ? extends WidgetController> widget, final Div parent,
            final String label, final String currentValue) {
        final Label fieldLabel = new Label(LabelUtils.getLabel(widget, label, new Object[0]));
        fieldLabel.setClass("btLabelField");
        final Textbox fieldTextBox = new Textbox(currentValue != null ? currentValue : "");
        parent.appendChild(fieldLabel);
        parent.appendChild(fieldTextBox);
        fieldTextBox.setClass("btTextField");
        return fieldTextBox;
    }


    public static Textbox createTextField(
            final InputWidget<DefaultListWidgetModel<TypedObject>, ? extends WidgetController> widget, final Div parent,
            final String label, final String currentValue) {
        final Label fieldLabel = new Label(LabelUtils.getLabel(widget, label, new Object[0]));
        fieldLabel.setClass("btLabelField");
        final Textbox fieldTextBox = new Textbox(currentValue != null ? currentValue : "");
        parent.appendChild(fieldLabel);
        parent.appendChild(fieldTextBox);
        fieldTextBox.setClass("btTextField");
        return fieldTextBox;
    }

    public static void addSeparator(final Div parent) {
        final Separator separator = new Separator();
        separator.setHeight("1px");
        parent.appendChild(separator);
    }

    public static Listbox createStoreInVaultDropdownField(final Widget<TransactionItemWidgetModel, TransactionController> widget,
                                                          final Div parent, final String label) {
        final Label statusLabel = new Label(LabelUtils.getLabel((Widget) widget, label, new Object[0]));
        statusLabel.setClass("btLabelField");
        statusLabel.setParent(parent);

        final Listbox statuses = new Listbox();
        statuses.setMold("select");
        statuses.setClass("btTextField");

        statuses.appendItem("Do not vault", StoreInVault.DO_NOT_VAULT.toString());
        statuses.appendItem("Vault always", StoreInVault.VAULT_ALWAYS.toString());
        statuses.appendItem("Vault on Success", StoreInVault.VAULT_ON_SUCCESS.toString());

        parent.appendChild(statusLabel);
        parent.appendChild(statuses);
        return statuses;
    }

    public static Listbox createCustomerAddressesListBox(final Widget<CustomerItemWidgetModel, CustomerController> widget,
                                                         final Div content, final String label, final BraintreeCustomerDetailsModel customer) {
        final List<AddressModel> addresses = customer.getAddresses();
        if (CollectionUtils.isNotEmpty(addresses)) {
            final Div parent = createDiv(content);
            parent.setClass("btCustomerAddressesListBoxDiv");
            final Label statusLabel = new Label(LabelUtils.getLabel((Widget) widget, label, new Object[0]));
            statusLabel.setClass("btCustomerAddressesListBoxLabelField");
            statusLabel.setParent(parent);

            final Listbox addressesBox = new Listbox();
            addressesBox.setMold("select");

            addressesBox.appendItem("None", null);
            for (final AddressModel address : addresses) {
                addressesBox.appendItem(createAddressTitle(address, MAX_ADDRESS_TITLE_LENGTH), address.getBrainTreeAddressId());
            }
            parent.appendChild(statusLabel);
            parent.appendChild(addressesBox);
            return addressesBox;
        }
        return null;
    }

    public static Listbox createCustomerUpdateAddressesListBox(final Widget<CustomerItemWidgetModel, CustomerController> widget,
                                                               final Div content, final String label, final BraintreeCustomerDetailsModel customer) {
        final List<AddressModel> addresses = customer.getAddresses();
        if (CollectionUtils.isNotEmpty(addresses)) {
            final Div parent = createDiv(content);
            parent.setClass("btCustomDiv.btUpdateCustomerAddressDiv");
            final Label statusLabel = new Label(LabelUtils.getLabel((Widget) widget, label, new Object[0]));
            statusLabel.setClass("btUpdateCustomerAddressLabel");
            statusLabel.setParent(parent);

            final Listbox addressesBox = new Listbox();
            addressesBox.setMold("select");
            addressesBox.setStyle("width: 220px");

            addressesBox.appendItem("None", null);
            for (final AddressModel address : addresses) {
                addressesBox.appendItem(createAddressTitle(address, MAX_ADDRESS_TITLE_LENGTH), address.getBrainTreeAddressId());
            }
            parent.appendChild(statusLabel);
            parent.appendChild(addressesBox);
            return addressesBox;
        }
        return null;
    }

    public static Listbox createCountriesListBox(final Widget widget, final Div content, final String label,
                                                 final List<CountryModel> countires, final String selected) {
        final Locale userLocale = JaloSession.getCurrentSession().getSessionContext().getLocale();

        final Listbox countriesBox = new Listbox();
        if (null != countires) {
            final List<CountryModel> sortedCountries = new ArrayList<CountryModel>(countires);
            Collections.sort(sortedCountries, new LocaleCountryComparator(userLocale));

            final Div parent = createDiv(content);
            final Label statusLabel = new Label(LabelUtils.getLabel(widget, label, new Object[0]));
            statusLabel.setClass("btCountryListBoxLabel");
            statusLabel.setParent(parent);

            countriesBox.setMold("select");
            countriesBox.setWidth("310px");
            countriesBox.appendItem(LabelUtils.getLabel(widget, "empty.country", new Object[0]), " ");
            for (final CountryModel country : sortedCountries) {
                final Listitem item = countriesBox.appendItem(DefaultLocaleUtils.getCountryName(country), country.getIsocode());
                if (country.getIsocode().equals(selected)) {
                    item.setSelected(true);
                }
            }
            parent.appendChild(statusLabel);
            parent.appendChild(countriesBox);
        }
        return countriesBox;
    }

    private static String createAddressTitle(final AddressModel address, final int maxLength) {
        final StringBuilder addressStringRepresentation = new StringBuilder();
        append(addressStringRepresentation, address.getFirstname());
        append(addressStringRepresentation, address.getLastname());
        append(addressStringRepresentation, address.getCompany());
        append(addressStringRepresentation, address.getStreetnumber());
        append(addressStringRepresentation, address.getTown());
        append(addressStringRepresentation, address.getRegion() != null ? DefaultLocaleUtils.getRegionName(address.getRegion())
                                                                        : address.getZone());
        append(addressStringRepresentation, address.getPostalcode());
        append(addressStringRepresentation, address.getCountry().getIsocode());
        String addressPresentation = addressStringRepresentation.toString();
        if (maxLength < addressPresentation.length()) {
            addressPresentation = new StringBuilder(addressPresentation.substring(0, maxLength - LONG_ADDRESS_FINALIZER.length()))
                    .append(LONG_ADDRESS_FINALIZER).toString();
        }
        return addressPresentation;
    }

    public static Div createDiv(final Div content) {
        final Div cloneFieldsBox = new Div();
        cloneFieldsBox.setParent(content);
        cloneFieldsBox.setClass("btCustomDiv");
        return cloneFieldsBox;
    }

    private static void append(final StringBuilder builder, final String value) {
        if (StringUtils.isNotBlank(value)) {
            builder.append(value).append(" ");
        }
    }

    public static int compareStrings(final String s1, final String s2) {
        if (null == s1) {
            if (null == s2) {
                return 0;
            }
            return -1;
        } else if (null == s2) {
            return 1;
        }
        return s1.compareTo(s2);
    }

    private static final class LocaleCountryComparator implements Comparator<CountryModel> {

        private final Locale locale;

        LocaleCountryComparator(final Locale locale) {
            this.locale = locale;
        }

        @Override
        public int compare(final CountryModel o1, final CountryModel o2) {
            return compareStrings(o1.getName(locale), o2.getName(locale));
        }
    }


    /**
     * Wrapping elements in horizontal box, useful for forms filling
     */
    public static Hbox wrapElementInHBox(final Label label, final Component element) {
        final Hbox row = new Hbox();
        row.setWidths("45%,55%");
        row.appendChild(label);
        row.appendChild(element);
        return row;
    }

    /**
     * Formats value into "money" format
     */
    public static String formatAmount(final BigDecimal amount) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locales.getCurrent());
        decimalFormat.applyPattern("#0.00");
        return decimalFormat.format(amount);
    }

}
