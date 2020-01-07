/**
 *
 */
package com.enterprisewide.b2badvance.facades.invoice.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.enterprisewide.b2badvance.core.model.B2BAdvanceInvoiceModel;
import com.enterprisewide.b2badvance.facades.invoice.data.InvoiceData;


/**
 * Populates invoice Model to invoice Data
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceInvoicePopulator<SOURCE extends B2BAdvanceInvoiceModel, TARGET extends InvoiceData>
		implements Populator<SOURCE, TARGET>
{

	/**
	 * {@inheritDoc}
	 */
	public void populate(final B2BAdvanceInvoiceModel source, final InvoiceData target) throws ConversionException
	{
		target.setDatePaid(source.getDatePaid());
		target.setUserId(source.getCustomer() == null ? null : source.getCustomer().getUid());
		target.setDueDate(source.getDueDate());
		target.setErpOrderNumber(source.getErpOrderNumber());
		target.setHybrisOrderNumber(source.getOrder() == null ? null : source.getOrder().getCode());
		target.setInvoiceNumber(source.getInvoiceNumber());
		target.setPdfUrl(source.getPdfUrl());
		target.setStatus(source.getStatus().getCode());
		target.setTotalAmount(source.getTotalAmount());
	}
}
