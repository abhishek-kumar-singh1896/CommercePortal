/**
 *
 */
package com.enterprisewide.b2badvance.core.product.impl;

import org.apache.commons.lang3.StringUtils;

import com.enterprisewide.b2badvance.core.dto.B2BAdvanceCsvDTO;
import com.enterprisewide.b2badvance.core.dto.B2BAdvanceDiscountCsvDTO;


/**
 * This validator class is used to validate the DTO data
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceDiscountDataValidator extends B2BAdvanceCSVDataValidator
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public B2BAdvanceCsvDTO validateCSVData(final B2BAdvanceCsvDTO source)
	{
		final B2BAdvanceDiscountCsvDTO dto = (B2BAdvanceDiscountCsvDTO) source;
		B2BAdvanceDiscountCsvDTO validatedDTO = new B2BAdvanceDiscountCsvDTO();
		if (StringUtils.isNotEmpty(dto.getArticleNumber()) && StringUtils.isNotEmpty(dto.getErpArticleNumber())
				&& StringUtils.isNotEmpty(dto.getDiscountType()) && dto.getDiscount() != null
				&& ("PERCENTAGE".equals(dto.getDiscountType()) || StringUtils.isNotEmpty(dto.getCurrency())))
		{
			validatedDTO = dto;
			if ("PERCENTAGE".equals(dto.getDiscountType()))
			{
				validatedDTO.setCurrency(null);
			}
		}
		return validatedDTO;
	}
}
