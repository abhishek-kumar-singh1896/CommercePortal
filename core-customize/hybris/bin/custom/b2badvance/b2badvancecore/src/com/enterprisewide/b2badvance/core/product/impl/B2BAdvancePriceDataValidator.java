/**
 *
 */
package com.enterprisewide.b2badvance.core.product.impl;

import org.apache.commons.lang3.StringUtils;

import com.enterprisewide.b2badvance.core.dto.B2BAdvanceCsvDTO;
import com.enterprisewide.b2badvance.core.dto.B2BAdvancePriceCsvDTO;


/**
 * This validator class is used to validate the DTO data
 *
 * @author Enterprise Wide
 */
public class B2BAdvancePriceDataValidator extends B2BAdvanceCSVDataValidator
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public B2BAdvanceCsvDTO validateCSVData(final B2BAdvanceCsvDTO source)
	{
		final B2BAdvancePriceCsvDTO dto = (B2BAdvancePriceCsvDTO) source;
		B2BAdvancePriceCsvDTO validatedDTO = new B2BAdvancePriceCsvDTO();
		if (StringUtils.isNotEmpty(dto.getArticleNumber()) && StringUtils.isNotEmpty(dto.getErpArticleNumber())
				&& StringUtils.isNotEmpty(dto.getSalesUnit()) && dto.getPrice() != null && StringUtils.isNotEmpty(dto.getCurrency()))
		{
			validatedDTO = dto;
		}
		return validatedDTO;
	}
}
