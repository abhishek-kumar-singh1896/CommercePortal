package com.braintree.cscockpit.services;

import com.braintree.command.result.BrainTreeAddressResult;


public interface BraintreeAddressService
{
	/**
	 * add new braintree address
	 */
	BrainTreeAddressResult addAddress(final String customerId, final String firstName, final String lastName,
			final String company, final String streetAddress, final String extendedAddress, final String cityLocality,
			final String stateProvinceRegion, final String postalCode, final String countryIsoCode);

	/**
	 * update existing braintree address
	 */
	BrainTreeAddressResult updateAddress(final String customerId, final String addressId, final String firstName,
			final String lastName, final String company, final String streetAddress, final String extendedAddress,
			final String cityLocality, final String stateProvinceRegion, final String postalCode, final String countryName);

	/**
	 * remove address by id
	 */
	BrainTreeAddressResult removeAddress(String addressId, String customerId);
}
