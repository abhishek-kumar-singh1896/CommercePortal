package com.braintree.cscockpit.validators;


import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cscockpit.exceptions.ResourceMessage;
import de.hybris.platform.cscockpit.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


public class DefaultBraintreeAddressValidator implements BraintreeAddressValidator
{
	@Override
	public void validate(final ObjectValueContainer container) throws ValidationException
	{
		List<ResourceMessage> errorMessages = new ArrayList<>();

		Set<ObjectValueContainer.ObjectValueHolder> objectValueHolders = container.getAllValues();

		for (ObjectValueContainer.ObjectValueHolder objectValueHolder : objectValueHolders)
		{
			validateProperties(objectValueHolder, errorMessages);
		}

		if (CollectionUtils.isNotEmpty(errorMessages))
		{
			throw new ValidationException(errorMessages);
		}
	}

	private void validateProperties(final ObjectValueContainer.ObjectValueHolder objectValueHolder,
			List<ResourceMessage> errorMessages)
	{
		if (objectValueHolder != null && objectValueHolder.getPropertyDescriptor() != null)
		{
			PropertyDescriptor propertyDescriptor = objectValueHolder.getPropertyDescriptor();
			validateProperty("Address.firstname", propertyDescriptor, objectValueHolder,
					"paymentOption.billingInfo.validation.noFirstName", errorMessages);
			validateProperty("Address.lastname", propertyDescriptor, objectValueHolder,
					"paymentOption.billingInfo.validation.noLastName", errorMessages);
			validateProperty("Address.line1", propertyDescriptor, objectValueHolder, "paymentOption.billingInfo.validation.noLine1",
					errorMessages);
			validateProperty("Address.town", propertyDescriptor, objectValueHolder, "paymentOption.billingInfo.validation.noTown",
					errorMessages);
			validateProperty("Address.postalcode", propertyDescriptor, objectValueHolder,
					"paymentOption.billingInfo.validation.noPostalCode", errorMessages);

			if ("Address.country".equals(propertyDescriptor.getQualifier()))
			{
				if (objectValueHolder.getLocalValue() == null)
				{
					errorMessages.add(new ResourceMessage("paymentOption.billingInfo.validation.noCountry"));
				}
			}
		}
	}

	void validateProperty(final String qualifier, final PropertyDescriptor propertyDescriptor,
			final ObjectValueContainer.ObjectValueHolder objectValueHolder, String errorMessage, List<ResourceMessage> errorMessages)
	{
		if (qualifier.equals(propertyDescriptor.getQualifier()))
		{
			if (StringUtils.isBlank((String) objectValueHolder.getLocalValue()))
			{
				errorMessages.add(new ResourceMessage(errorMessage));
			}
		}
	}
}
