package com.enterprisewide.b2badvance.core.batch.converter.impl;

import org.dozer.CustomConverter;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;


/**
 * This converter class is used to create code for fields.
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceChangeControlConvertor implements CustomConverter
{

	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destinationClass,
			final Class<?> sourceClass)
	{
		String changeValue = null;
		if (source instanceof String)
		{
			switch ((String) source)
			{
				case B2badvanceCoreConstants.ENABLE:
					changeValue = B2badvanceCoreConstants.APPROVED;
					break;
				case B2badvanceCoreConstants.DELETE:
					changeValue = B2badvanceCoreConstants.UNAPPROVED;
					break;
				default:
					//ADD and CHANGE
					changeValue = B2badvanceCoreConstants.IGNORE;
					break;
			}
		}
		return changeValue;
	}
}
