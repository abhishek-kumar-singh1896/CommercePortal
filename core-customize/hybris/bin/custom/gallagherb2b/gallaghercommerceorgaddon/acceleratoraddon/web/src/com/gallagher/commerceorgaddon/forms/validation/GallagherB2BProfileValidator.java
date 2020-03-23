/**
 *
 */
package com.gallagher.commerceorgaddon.forms.validation;


import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ProfileValidator;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.gallagher.commerceorgaddon.forms.B2BCustomerForm;


/**
 * Validator for B2B Customer form
 *
 */
@Component("gallagherb2bProfileValidator")
public class GallagherB2BProfileValidator extends ProfileValidator
{

	@Override
	public void validate(final Object object, final Errors errors)
	{
		super.validate(object, errors);

		final B2BCustomerForm profileForm = (B2BCustomerForm) object;

		if (CollectionUtils.isEmpty(profileForm.getParentB2BUnits()))
		{
			errors.rejectValue("parentB2BUnits", "profile.unit.invalid");
		}

	}

}
