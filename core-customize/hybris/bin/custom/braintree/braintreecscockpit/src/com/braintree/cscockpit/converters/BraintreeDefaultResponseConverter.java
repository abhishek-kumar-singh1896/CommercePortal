package com.braintree.cscockpit.converters;

import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import com.braintree.command.result.BrainTreeAbstractResult;
import com.braintree.hybris.data.BrainTreeResponseResultData;


public class BraintreeDefaultResponseConverter extends
		AbstractPopulatingConverter<BrainTreeAbstractResult, BrainTreeResponseResultData>
{
	@Override
	public void populate(final BrainTreeAbstractResult source, final BrainTreeResponseResultData target)
	{
		target.setSuccess(source.isSuccess());
		target.setErrorMessage(source.getErrorMessage());
		target.setErrorCode(source.getErrorCode());
	}
}
