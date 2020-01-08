package com.braintree.cscockpit.validators;

import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cscockpit.exceptions.ValidationException;


/**
 * Validate address form
 */
public interface BraintreeAddressValidator
{
	/**
	 * validate all important fields
	 * 
	 * @param container
	 *           - container with address properties
	 * @throws ValidationException
	 *            with ResourceMessage - list off errors
	 */
	void validate(final ObjectValueContainer container) throws ValidationException;
}
