package com.braintree.cscockpit.widgets.renderers.details.impl;

import com.braintree.constants.BraintreeConstants;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.jalo.JaloSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.zkoss.zul.Div;

import com.braintree.model.BraintreeCustomerDetailsModel;


public class BraintreeCustomerDetailsListRender extends AbstractTransactionDetailsListRender
{
	@Override
	protected void populateKnownDetails(final Object context, final TypedObject item, final Widget widget,
			final Div detailContainer)
	{
		if (item != null && item.getObject() instanceof BraintreeCustomerDetailsModel)
		{
			final BraintreeCustomerDetailsModel customer = (BraintreeCustomerDetailsModel) item.getObject();

			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "id", new Object[0])),
					this.createValueLabel(customer.getId()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "company", new Object[0])),
					this.createValueLabel(customer.getCompany()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "email", new Object[0])),
					this.createValueLabel(customer.getEmail()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "phone", new Object[0])),
					this.createValueLabel(customer.getPhone()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "fax", new Object[0])),
					this.createValueLabel(customer.getFax()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "website", new Object[0])),
					this.createValueLabel(customer.getWebsite()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "firstName", new Object[0])),
					this.createValueLabel(customer.getFirstName()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "lastName", new Object[0])),
					this.createValueLabel(customer.getLastName()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "createAt", new Object[0])),
					this.createValueLabel(formedDate(customer.getCreatedAt())), detailContainer);
		}
	}

	private String formedDate(final Date createdAt)
	{
		final Locale userLocale = JaloSession.getCurrentSession().getSessionContext().getLocale();
		final DateFormat dateFormat = new SimpleDateFormat(BraintreeConstants.TRANSACTION_SEARCH_DATE_FORMAT, userLocale);
		return dateFormat.format(Long.valueOf(createdAt.getTime()));
	}

}
