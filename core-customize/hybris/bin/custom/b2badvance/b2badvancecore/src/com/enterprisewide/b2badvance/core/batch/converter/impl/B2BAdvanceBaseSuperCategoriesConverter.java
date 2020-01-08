package com.enterprisewide.b2badvance.core.batch.converter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dozer.CustomConverter;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;
import com.enterprisewide.b2badvance.core.dto.B2BAdvanceBaseProductCsvDTO;


/**
 * This converter class is used to form the supercategory field of baseproduct.
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceBaseSuperCategoriesConverter implements CustomConverter
{
	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destinationClass,
			final Class<?> sourceClass)
	{
		String finalVariantCat = null;
		if (source instanceof B2BAdvanceBaseProductCsvDTO)
		{
			final B2BAdvanceBaseProductCsvDTO dto = (B2BAdvanceBaseProductCsvDTO) source;

			final List<String> supercategories = new ArrayList<>();
			supercategories.add(extractCategoryCodes(dto.getProductCategories()));
			supercategories.add(extractCategoryCodes(dto.getVariantCategories()));
			supercategories.removeAll(Collections.singleton(null));

			finalVariantCat = StringUtils.join(supercategories, B2badvanceCoreConstants.COMMA);

		}

		return finalVariantCat;
	}

	/**
	 * This method creates and returns supercategory code.
	 *
	 * @param variant
	 * @param prefix
	 * @param supercategories
	 * @return String category code.
	 */
	private String extractCategoryCodes(final String variant)
	{
		String code = null;
		if (variant != null)
		{
			final List<String> valueList = getSplitList(variant);
			code = valueList.stream().map(value -> String.valueOf(formatCode(value))).collect(Collectors.joining(","));

		}
		return code;
	}

	/**
	 * This method category into a pattern.
	 *
	 * @param inputString
	 * @return
	 */
	private String formatCode(final String inputString)
	{
		return inputString.replaceAll(B2badvanceCoreConstants.SPACE, B2badvanceCoreConstants.UNDERSCORE);
	}

	/**
	 * This method is used to convert comma string to List of string.
	 *
	 * @param inputString
	 * @return List of String
	 */
	private List<String> getSplitList(final String inputString)
	{
		final String[] array = inputString.split("\\|");
		final List<String> list = Arrays.asList(array);
		list.removeAll(Collections.singleton(null));
		return list;
	}
}
