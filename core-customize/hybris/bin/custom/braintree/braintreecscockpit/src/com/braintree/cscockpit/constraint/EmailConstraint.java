package com.braintree.cscockpit.constraint;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;


public class EmailConstraint implements Constraint
{

	@Override
	public void validate(final Component paramComponent, final Object paramObject) throws WrongValueException
	{
		if (paramObject instanceof String)
		{
			final String email = (String) paramObject;
			if (StringUtils.isNotBlank(email))
			{
				if (!email.matches("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"))
				{
					throw new WrongValueException(paramComponent, Labels.getLabel("validation.incorrect.email"));
				}
			}
		}
	}
}
