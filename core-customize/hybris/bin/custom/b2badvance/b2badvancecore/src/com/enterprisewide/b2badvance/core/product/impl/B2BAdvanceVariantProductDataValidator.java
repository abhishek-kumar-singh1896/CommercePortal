/**
 *
 */
package com.enterprisewide.b2badvance.core.product.impl;

import org.apache.commons.lang3.StringUtils;

import com.enterprisewide.b2badvance.core.dto.B2BAdvanceCsvDTO;
import com.enterprisewide.b2badvance.core.dto.B2BAdvanceVariantProductCsvDTO;


/**
 * This validator class is used to validate the DTO data
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceVariantProductDataValidator extends B2BAdvanceCSVDataValidator
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public B2BAdvanceCsvDTO validateCSVData(final B2BAdvanceCsvDTO source)
	{
		final B2BAdvanceVariantProductCsvDTO dto = (B2BAdvanceVariantProductCsvDTO) source;
		final B2BAdvanceVariantProductCsvDTO validatedDTO = new B2BAdvanceVariantProductCsvDTO();
		if (StringUtils.isNotEmpty(dto.getArticleNumber()) && StringUtils.isNotEmpty(dto.getErpArticleNumber())
				&& StringUtils.isNotEmpty(dto.getSalesUnit()) && StringUtils.isNotEmpty(dto.getBaseProductArticleNumber())
				&& StringUtils.isNotEmpty(dto.getVariantValueCategories()) && StringUtils.isNotEmpty(dto.getAction()))
		{
			validatedDTO.setArticleNumber(dto.getArticleNumber());
			validatedDTO.setErpArticleNumber(dto.getErpArticleNumber());
			validatedDTO.setProductName(dto.getProductName());
			validatedDTO.setProductSummary(dto.getProductSummary());
			validatedDTO.setOnlineFrom(dto.getOnlineFrom());
			validatedDTO.setOnlineTo(dto.getOnlineTo());
			validatedDTO.setSalesUnit(dto.getSalesUnit());
			validatedDTO.setVariantValueCategories(dto.getVariantValueCategories());
			validatedDTO.setBaseProductArticleNumber(dto.getBaseProductArticleNumber());
			validatedDTO.setAction(dto.getAction());
		}
		return validatedDTO;
	}
}
