/**
 *
 */
package com.gallagher.b2c.form;

import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;

import javax.validation.constraints.Size;


/**
 * @author shilpiverma
 *
 */
public class GallagherAddressForm extends AddressForm
{
	private String streetNumber;

	public String getStreetNumber()
	{
		return streetNumber;
	}

	@Size(max = 10, message = "{address.streetNumber.invalid}")
	public void setStreetNumber(final String streetNumber)
	{
		this.streetNumber = streetNumber;
	}

}
