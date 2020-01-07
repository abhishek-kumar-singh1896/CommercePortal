/**
 *
 */
package com.enterprisewide.b2badvance.core.product.impl;

import org.apache.commons.lang3.StringUtils;

import com.enterprisewide.b2badvance.core.dto.B2BAdvanceCsvDTO;
import com.enterprisewide.b2badvance.core.dto.B2BAdvanceStockCsvDTO;


/**
 * This validator class is used to validate the DTO data
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceStockDataValidator extends B2BAdvanceCSVDataValidator
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public B2BAdvanceCsvDTO validateCSVData(final B2BAdvanceCsvDTO source)
	{
		final B2BAdvanceStockCsvDTO dto = (B2BAdvanceStockCsvDTO) source;
		B2BAdvanceStockCsvDTO validatedDTO = new B2BAdvanceStockCsvDTO();
		if (StringUtils.isNotEmpty(dto.getArticleNumber()) && StringUtils.isNotEmpty(dto.getErpArticleNumber())
				&& StringUtils.isNotEmpty(dto.getInStockStatus()) && StringUtils.isNotEmpty(dto.getWarehouseCode()))
		{
			validatedDTO = dto;
		}
		return validatedDTO;
	}
}
