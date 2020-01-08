package com.braintree.cscockpit.converters;

import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import com.braintree.cscockpit.data.BraintreePaymentMethodData;
import com.braintree.command.result.BrainTreeUpdatePaymentMethodResult;


public class BraintreePaymentMethodResponseConverter extends
		AbstractPopulatingConverter<BrainTreeUpdatePaymentMethodResult, BraintreePaymentMethodData>
{
	@Override
	public void populate(final BrainTreeUpdatePaymentMethodResult source, final BraintreePaymentMethodData target)
	{
		target.setSuccess(source.isSuccess());
		target.setErrorMessage(source.getErrorMessage());
		target.setErrorCode(source.getErrorCode());
		target.setPaymentMethod(source.getPaymentMethod());
	}
}
