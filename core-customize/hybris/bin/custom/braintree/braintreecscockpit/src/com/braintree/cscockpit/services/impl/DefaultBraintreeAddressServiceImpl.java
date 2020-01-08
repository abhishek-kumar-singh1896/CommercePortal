package com.braintree.cscockpit.services.impl;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;

import com.braintree.command.request.BrainTreeAddressRequest;
import com.braintree.command.result.BrainTreeAddressResult;
import com.braintree.converters.BraintreeAddressConverter;
import com.braintree.cscockpit.services.BraintreeAddressService;
import com.braintree.customer.dao.BrainTreeCustomerAccountDao;
import com.braintree.method.BrainTreePaymentService;


public class DefaultBraintreeAddressServiceImpl implements BraintreeAddressService
{
	private BrainTreeCustomerAccountDao brainTreeCustomerAccountDao;
	private BrainTreePaymentService brainTreePaymentService;
	private BraintreeAddressConverter braintreeAddressConverter;
	private UserService userService;
	private ModelService modelService;

	@Override
	public BrainTreeAddressResult addAddress(String customerId, String firstName, String lastName, String company,
			String streetAddress, String extendedAddress, String cityLocality, String stateProvinceRegion, String postalCode,
			String countryIsoCode)
	{
		final UserModel user = getUserService().getCurrentUser();
		final String braintreeCustomerId = user.getUid();
		if (braintreeCustomerId != null)
		{
			final BrainTreeAddressRequest addressRequest = new BrainTreeAddressRequest(braintreeCustomerId);
			addressRequest.setCompany(company);
			addressRequest.setCustomerId(customerId);
			addressRequest.setFirstName(firstName);
			addressRequest.setLastName(lastName);
			addressRequest.setExtendedAddress(extendedAddress);
			addressRequest.setLocality(cityLocality);
			addressRequest.setPostalCode(postalCode);
			addressRequest.setRegion(stateProvinceRegion);
			addressRequest.setStreetAddress(streetAddress);
			addressRequest.setCountryCodeAlpha2(countryIsoCode);
			return add(addressRequest, customerId);
		}
		return null;
	}

	private BrainTreeAddressResult add(BrainTreeAddressRequest addressRequest, String customerId)
	{
		CustomerModel customer = getBrainTreeCustomerAccountDao().findCustomerByBrainTreeCustomerId(customerId);
		BrainTreeAddressResult brainTreeAddressResult = getBrainTreePaymentService().createAddress(addressRequest, customer);
		if (brainTreeAddressResult != null && brainTreeAddressResult.isSuccess())
		{
			AddressModel address = getBraintreeAddressConverter().convert(brainTreeAddressResult.getAddress());
			if (customer != null && address != null)
			{
				address.setVisibleInAddressBook(Boolean.TRUE);
				address.setShippingAddress(Boolean.TRUE);
				address.setOwner(customer);
				if (address.getZone() == null)
				{
					address.setZone(addressRequest.getRegion());
				}
				getModelService().save(address);
			}
		}
		return brainTreeAddressResult;
	}

	@Override
	public BrainTreeAddressResult updateAddress(String customerId, String addressId, String firstName, String lastName,
			String company, String streetAddress, String extendedAddress, String cityLocality, String stateProvinceRegion,
			String postalCode, String countryName)
	{
		final BrainTreeAddressRequest request = new BrainTreeAddressRequest(customerId);
		request.setAddressId(addressId);
		request.setCompany(company);
		request.setExtendedAddress(extendedAddress);
		request.setFirstName(firstName);
		request.setLastName(lastName);
		request.setLocality(cityLocality);
		request.setPostalCode(postalCode);
		request.setRegion(stateProvinceRegion);
		request.setStreetAddress(streetAddress);
		request.setCountryCodeAlpha2(countryName);
		request.setCustomerId(customerId);

		return updateAddress(request, customerId, addressId);
	}

	private BrainTreeAddressResult updateAddress(BrainTreeAddressRequest request, String customerId, String addressId)
	{
		BrainTreeAddressResult brainTreeAddressResult = getBrainTreePaymentService().updateAddress(request);
		if (brainTreeAddressResult != null && brainTreeAddressResult.isSuccess())
		{
			AddressModel addressToUpdate = getAddressByBrainTreeId(customerId, addressId);
			AddressModel address = getBraintreeAddressConverter().convert(brainTreeAddressResult.getAddress());
			if (addressToUpdate != null && address != null)
			{
				addressToUpdate.setFirstname(address.getFirstname());
				addressToUpdate.setLastname(address.getLastname());
				addressToUpdate.setRegion(address.getRegion());
				addressToUpdate.setCountry(address.getCountry());
				addressToUpdate.setZone(address.getZone());
				addressToUpdate.setStreetname(address.getStreetname());
				addressToUpdate.setStreetnumber(address.getStreetnumber());
				addressToUpdate.setBrainTreeAddressId(address.getBrainTreeAddressId());
				addressToUpdate.setCompany(address.getCompany());
				addressToUpdate.setTown(address.getTown());
				addressToUpdate.setPostalcode(address.getPostalcode());
				getModelService().save(addressToUpdate);
			}
		}
		return brainTreeAddressResult;
	}
	@Override
	public BrainTreeAddressResult removeAddress(String addressId, String customerId)
	{
		final UserModel user = getUserService().getCurrentUser();
		final String braintreeCustomerId = user.getUid();
		if (braintreeCustomerId != null)
		{
			final BrainTreeAddressRequest request = new BrainTreeAddressRequest(braintreeCustomerId);
			request.setAddressId(addressId);
			request.setCustomerId(customerId);
			return remove(request);
		}
		return null;
	}

	private BrainTreeAddressResult remove(BrainTreeAddressRequest request)
	{
		BrainTreeAddressResult brainTreeAddressResult = getBrainTreePaymentService().removeAddress(request);
		if (brainTreeAddressResult != null && brainTreeAddressResult.isSuccess())
		{
			AddressModel address = getAddressByBrainTreeId(request.getCustomerId(), request.getAddressId());
			if (address != null)
			{
				getModelService().remove(address);
			}
		}
		return brainTreeAddressResult;
	}

	private AddressModel getAddressByBrainTreeId(String customerId, String addressId)
	{
		CustomerModel customer = getBrainTreeCustomerAccountDao().findCustomerByBrainTreeCustomerId(customerId);
		if (customer != null)
		{
			Collection<AddressModel> addresses = customer.getAddresses();
			for (AddressModel address : addresses)
			{
				if (address != null && addressId.equals(address.getBrainTreeAddressId()))
				{
					return address;
				}
			}
		}
		return null;
	}

	public BrainTreePaymentService getBrainTreePaymentService()
	{
		return brainTreePaymentService;
	}

	public void setBrainTreePaymentService(final BrainTreePaymentService brainTreePaymentService)
	{
		this.brainTreePaymentService = brainTreePaymentService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	public BrainTreeCustomerAccountDao getBrainTreeCustomerAccountDao()
	{
		return brainTreeCustomerAccountDao;
	}

	public void setBrainTreeCustomerAccountDao(BrainTreeCustomerAccountDao brainTreeCustomerAccountDao)
	{
		this.brainTreeCustomerAccountDao = brainTreeCustomerAccountDao;
	}

	public BraintreeAddressConverter getBraintreeAddressConverter()
	{
		return braintreeAddressConverter;
	}

	public void setBraintreeAddressConverter(BraintreeAddressConverter braintreeAddressConverter)
	{
		this.braintreeAddressConverter = braintreeAddressConverter;
	}
}
