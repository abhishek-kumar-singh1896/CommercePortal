package com.enterprisewide.b2badvance.core.invoice.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.invoice.B2BAdvanceInvoiceService;
import com.enterprisewide.b2badvance.core.invoice.dao.B2BAdvanceInvoiceDao;
import com.enterprisewide.b2badvance.core.model.B2BAdvanceInvoiceModel;


/**
 * Default implementation of {@link B2BAdvanceInvoiceService}.
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceInvoiceServiceImpl implements B2BAdvanceInvoiceService
{
	private B2BAdvanceInvoiceDao invoiceDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public B2BAdvanceInvoiceModel getInvoice(final String invoiceNumber)
	{
		return invoiceDao.getInvoice(invoiceNumber);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SearchPageData<B2BAdvanceInvoiceModel> getInvoicesForCurrentUser(final PaginationData pagination, final UserModel user)
	{
		return invoiceDao.getInvoicesForCurrentUser(pagination, user);
	}

	protected B2BAdvanceInvoiceDao getInvoiceDao()
	{
		return invoiceDao;
	}

	@Required
	public void setInvoiceDao(final B2BAdvanceInvoiceDao invoiceDao)
	{
		this.invoiceDao = invoiceDao;
	}


}
