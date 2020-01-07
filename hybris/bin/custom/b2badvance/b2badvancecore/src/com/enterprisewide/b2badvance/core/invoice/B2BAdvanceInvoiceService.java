package com.enterprisewide.b2badvance.core.invoice;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import com.enterprisewide.b2badvance.core.model.B2BAdvanceInvoiceModel;


/**
 * Service to handle invoice related data for {@link B2BAdvanceInvoiceService}
 *
 * @author Enterprise Wide
 */
public interface B2BAdvanceInvoiceService
{

	/**
	 * Returns an invoice by invoice number.
	 *
	 * @param invoiceNumber
	 *
	 * @return an invoice with specified invoice number
	 *
	 */
	B2BAdvanceInvoiceModel getInvoice(final String invoiceNumber);

	/**
	 * Returns all the invoices for the user
	 *
	 * @param pagination
	 *           holds the pagination information
	 * @param user
	 *           for whom invoices needs to be fetched
	 * @return all the invoices
	 */
	SearchPageData<B2BAdvanceInvoiceModel> getInvoicesForCurrentUser(final PaginationData pagination, final UserModel user);
}
