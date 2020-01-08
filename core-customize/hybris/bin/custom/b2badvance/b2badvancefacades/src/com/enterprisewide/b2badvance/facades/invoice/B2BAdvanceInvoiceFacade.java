/**
 *
 */
package com.enterprisewide.b2badvance.facades.invoice;

import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;
import java.util.Map;

import com.enterprisewide.b2badvance.facades.invoice.data.InvoiceData;


/**
 * Invoice facade for B2BAdvance.
 *
 * @author Enterprise Wide
 */
public interface B2BAdvanceInvoiceFacade
{
	/**
	 * Saves list of invoices
	 *
	 * @return map of errors. Key is invoice ID and value is the error associated with that invoice
	 */
	Map<String, String> saveInvoices(final List<InvoiceData> invoices);

	/**
	 * Returns list of invoices
	 *
	 * @return invoice Data
	 */
	SearchPageData<InvoiceData> getInvoicesForCurrentUser(final PaginationData paginationData);

}
