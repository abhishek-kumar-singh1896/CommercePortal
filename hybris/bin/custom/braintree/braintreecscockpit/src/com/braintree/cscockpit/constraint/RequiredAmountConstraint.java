package com.braintree.cscockpit.constraint;

import static org.zkoss.util.resource.Labels.getLabel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;


public class RequiredAmountConstraint implements Constraint
{

	@Override
	public void validate(final Component paramComponent, final Object paramObject) throws WrongValueException
	{
		if (paramObject instanceof String)
		{
			final String amount = (String) paramObject;
			if (!amount.matches("\\d+(\\.\\d{1,2})?"))
			{
				throw new WrongValueException(paramComponent, getLabel("validation.incorrect.amount"));
			}
		}
	}
}
