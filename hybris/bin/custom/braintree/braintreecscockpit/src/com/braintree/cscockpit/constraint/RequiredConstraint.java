package com.braintree.cscockpit.constraint;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;


public class RequiredConstraint implements Constraint
{

	@Override
	public void validate(final Component paramComponent, final Object paramObject) throws WrongValueException
	{
		if (paramObject instanceof String)
		{
			final String field = (String) paramObject;
			if (StringUtils.isBlank(field))
			{
				throw new WrongValueException(paramComponent, Labels.getLabel("validation.incorrect.field"));
			}
		}
	}
}
