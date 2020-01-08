/**
 *
 */
package com.enterprisewide.b2badvance.core.product.impl;

import org.apache.commons.lang3.StringUtils;

import com.enterprisewide.b2badvance.core.dto.B2BAdvanceBaseProductCsvDTO;
import com.enterprisewide.b2badvance.core.dto.B2BAdvanceCsvDTO;


/**
 * This validator class is used to validate the DTO data
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceProductDataValidator extends B2BAdvanceCSVDataValidator
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public B2BAdvanceCsvDTO validateCSVData(final B2BAdvanceCsvDTO source)
	{
		final B2BAdvanceBaseProductCsvDTO dto = (B2BAdvanceBaseProductCsvDTO) source;
		final B2BAdvanceBaseProductCsvDTO validatedDTO = new B2BAdvanceBaseProductCsvDTO();
		if (StringUtils.isNotEmpty(dto.getArticleNumber()) && StringUtils.isNotEmpty(dto.getErpArticleNumber())
				&& StringUtils.isNotEmpty(dto.getSalesUnit()) && StringUtils.isNotEmpty(dto.getProductCategories())
				&& StringUtils.isNotEmpty(dto.getAction()))
		{
			validatedDTO.setArticleNumber(dto.getArticleNumber());
			validatedDTO.setErpArticleNumber(dto.getErpArticleNumber());
			validatedDTO.setProductName(dto.getProductName());
			validatedDTO.setProductSummary(dto.getProductSummary());
			validatedDTO.setOnlineFrom(dto.getOnlineFrom());
			validatedDTO.setOnlineTo(dto.getOnlineTo());
			validatedDTO.setSalesUnit(dto.getSalesUnit());
			validatedDTO.setVariantCategories(dto.getVariantCategories());
			validatedDTO.setProductCategories(dto.getProductCategories());
			validatedDTO.setAction(dto.getAction());
		}
		return validatedDTO;
	}
}
