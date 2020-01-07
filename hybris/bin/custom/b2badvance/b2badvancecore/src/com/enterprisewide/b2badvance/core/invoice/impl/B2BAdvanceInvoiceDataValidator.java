package com.enterprisewide.b2badvance.core.invoice.impl;

import org.apache.commons.lang3.StringUtils;

import com.enterprisewide.b2badvance.core.dto.B2BAdvanceCsvDTO;
import com.enterprisewide.b2badvance.core.dto.B2BAdvanceInvoiceCsvDTO;
import com.enterprisewide.b2badvance.core.product.impl.B2BAdvanceCSVDataValidator;


/**
 * This validator class is used to validate the DTO data
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceInvoiceDataValidator extends B2BAdvanceCSVDataValidator
{

	/**
	 * This method is used to validate and filter Invoice attributes.
	 *
	 * @param dto
	 * @return
	 */
	@Override
	public B2BAdvanceCsvDTO validateCSVData(final B2BAdvanceCsvDTO source)
	{
		final B2BAdvanceInvoiceCsvDTO rawDTO = (B2BAdvanceInvoiceCsvDTO) source;
		final B2BAdvanceInvoiceCsvDTO validatedDTO = new B2BAdvanceInvoiceCsvDTO();
		if (StringUtils.isNotEmpty(rawDTO.getInvoiceNumber())
				&& (StringUtils.isNotEmpty(rawDTO.getHybrisOrderNumber()) || StringUtils.isNotEmpty(rawDTO.getUserId()))
				&& rawDTO.getDueDate() != null && StringUtils.isNotEmpty(rawDTO.getStatus()) && rawDTO.getTotalAmount() != null)
		{
			validatedDTO.setInvoiceNumber(rawDTO.getInvoiceNumber());
			validatedDTO.setHybrisOrderNumber(rawDTO.getHybrisOrderNumber());
			validatedDTO.setErpOrderNumber(rawDTO.getErpOrderNumber());
			validatedDTO.setDatePaid(rawDTO.getDatePaid());
			validatedDTO.setDueDate(rawDTO.getDueDate());
			validatedDTO.setTotalAmount(rawDTO.getTotalAmount());
			validatedDTO.setPdfUrl(rawDTO.getPdfUrl());
			validatedDTO.setStatus(rawDTO.getStatus());
			validatedDTO.setUserId(rawDTO.getUserId());

		}
		return validatedDTO;
	}

}
