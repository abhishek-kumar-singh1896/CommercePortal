package com.braintree.cscockpit.strategy.impl;

import de.hybris.platform.core.model.user.AddressModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.braintree.cscockpit.data.BraintreePaymentMethodData;
import com.braintree.converters.BraintreePaymentMethodConverter;
import com.braintree.cscockpit.strategy.UpdateCustomerStrategy;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.model.BraintreeCustomerDetailsModel;
import com.braintreegateway.PaymentMethod;


public class UpdatePaymentMethodForCustomerStrategyImpl implements UpdateCustomerStrategy
{
	private BraintreePaymentMethodConverter paymentMethodConverter;

	@Override
	public BraintreeCustomerDetailsModel update(final BraintreeCustomerDetailsModel customer,
			final BraintreePaymentMethodData paymentMethodData, final String changedToken)
	{
		final List<BrainTreePaymentInfoModel> paymentMethods = customer.getPaymentMethods();
		if (!CollectionUtils.isEmpty(paymentMethods))
		{
			final BrainTreePaymentInfoModel oldPaymentMethod = getOldPaymentMethod(paymentMethodData, paymentMethods, changedToken);
			if (oldPaymentMethod != null)
			{
				paymentMethods.remove(oldPaymentMethod);
				paymentMethods.add(getPaymentMethodConverter().convert(paymentMethodData.getPaymentMethod()));
			}
			processDefaultSign(paymentMethodData.getPaymentMethod(), paymentMethods);
		}
		return customer;
	}

	private BrainTreePaymentInfoModel getOldPaymentMethod(final BraintreePaymentMethodData paymentMethodData,
			final List<BrainTreePaymentInfoModel> paymentMethods, final String changedToken)
	{
		final PaymentMethod paymentMethod = paymentMethodData.getPaymentMethod();
		for (final BrainTreePaymentInfoModel paymentMethodEntry : paymentMethods)
		{
			if (paymentMethodEntry.getPaymentMethodToken().equals(paymentMethod.getToken())
					|| paymentMethodEntry.getPaymentMethodToken().equals(changedToken))
			{
				return paymentMethodEntry;
			}
		}
		return null;
	}

	@Override
	public BraintreeCustomerDetailsModel update(final BraintreeCustomerDetailsModel customer, final PaymentMethod paymentMethod)
	{
		final List<BrainTreePaymentInfoModel> paymentMethods = customer.getPaymentMethods();
		final BrainTreePaymentInfoModel paymentMethodModel = getPaymentMethodConverter().convert(paymentMethod);

		if (CollectionUtils.isEmpty(paymentMethods))
		{
			final ArrayList<BrainTreePaymentInfoModel> braintreePaymentMethodModels = new ArrayList<>();
			braintreePaymentMethodModels.add(paymentMethodModel);
			customer.setPaymentMethods(braintreePaymentMethodModels);
		}
		else
		{
			paymentMethods.add(paymentMethodModel);
		}
		processDefaultSign(paymentMethod, paymentMethods);
		return customer;
	}

	@Override
	public BraintreeCustomerDetailsModel update(final BraintreeCustomerDetailsModel customer, final String addressId)
	{
		final List<AddressModel> addresses = customer.getAddresses();
		AddressModel addressToRemove = null;
		if (CollectionUtils.isNotEmpty(addresses))
		{
			for (final AddressModel address : addresses)
			{
				if (addressId.equals(address.getBrainTreeAddressId()))
				{
					addressToRemove = address;
				}
			}
			if (addressToRemove != null)
			{
				addresses.remove(addressToRemove);
			}
		}
		return customer;
	}

	private void processDefaultSign(final PaymentMethod paymentMethod, final List<BrainTreePaymentInfoModel> paymentMethods)
	{
		if (paymentMethod.isDefault())
		{
			for (final BrainTreePaymentInfoModel paymentMethodEntry : paymentMethods)
			{
				if (!paymentMethodEntry.getPaymentMethodToken().equals(paymentMethod.getToken()))
				{
					paymentMethodEntry.setIsDefault(false);
				}
			}
		}
	}

	public BraintreePaymentMethodConverter getPaymentMethodConverter()
	{
		return paymentMethodConverter;
	}

	public void setPaymentMethodConverter(final BraintreePaymentMethodConverter paymentMethodConverter)
	{
		this.paymentMethodConverter = paymentMethodConverter;
	}
}
