package com.braintree.cscockpit.strategy;

import com.braintree.cscockpit.data.BraintreePaymentMethodData;
import com.braintree.model.BraintreeCustomerDetailsModel;
import com.braintreegateway.PaymentMethod;


/**
 *
 * Update some customer data for render needs
 */
public interface UpdateCustomerStrategy
{
	/**
	 * remove or replace old payment method
	 */
	BraintreeCustomerDetailsModel update(BraintreeCustomerDetailsModel customer, BraintreePaymentMethodData paymentMethodData,
			String changedToken);

	/**
	 * add payment method to customer
	 */
	BraintreeCustomerDetailsModel update(BraintreeCustomerDetailsModel customer, PaymentMethod paymentMethod);


	BraintreeCustomerDetailsModel update(BraintreeCustomerDetailsModel customer, String addressId);
}
