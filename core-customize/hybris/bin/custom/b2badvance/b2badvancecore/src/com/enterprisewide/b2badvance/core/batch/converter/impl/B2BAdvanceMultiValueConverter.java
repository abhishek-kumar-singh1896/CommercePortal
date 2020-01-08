package com.enterprisewide.b2badvance.core.batch.converter.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.CustomConverter;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;


/**
 * This converter class is used to split multiple value.
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceMultiValueConverter implements CustomConverter
{

	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destinationClass,
			final Class<?> sourceClass)
	{
		String code = null;
		if (source instanceof String)
		{
			final List<String> stringList = getSplitList((String) source);
			code = formatCode(StringUtils.join(stringList, B2badvanceCoreConstants.COMMA));
		}
		return code;
	}

	/**
	 * This method format category into a pattern.
	 *
	 * @param inputString
	 * @return String, code of given input by replacing space with underscore.
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
