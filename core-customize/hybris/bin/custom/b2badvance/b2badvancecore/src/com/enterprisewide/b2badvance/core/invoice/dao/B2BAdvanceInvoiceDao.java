package com.enterprisewide.b2badvance.core.invoice.dao;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import com.enterprisewide.b2badvance.core.model.B2BAdvanceInvoiceModel;


/**
 * Dao to retrieve invoice related data for {@link com.enterprisewide.b2badvance.core.invoice.B2BAdvanceInvoiceService}.
 */
public interface B2BAdvanceInvoiceDao extends GenericDao<B2BAdvanceInvoiceModel>
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
	 * Returns the invoices for particular user
	 *
	 * @param user
	 * @param pagination
	 *
	 * @return invoices for current user
	 */
	SearchPageData<B2BAdvanceInvoiceModel> getInvoicesForCurrentUser(final PaginationData pagination, final UserModel user);
}
