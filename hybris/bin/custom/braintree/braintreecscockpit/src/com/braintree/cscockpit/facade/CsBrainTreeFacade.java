package com.braintree.cscockpit.facade;

import com.braintree.command.result.BrainTreeAddressResult;
import com.braintree.command.result.BrainTreePaymentMethodResult;
import com.braintree.command.result.BrainTreeUpdateCustomerResult;
import com.braintree.cscockpit.data.BrainTreeInfo;
import com.braintree.cscockpit.data.BraintreePaymentMethodData;
import com.braintree.cscockpit.data.BraintreePaymentMethodInfo;
import com.braintree.exceptions.BraintreeErrorException;
import com.braintree.hybris.data.BrainTreeResponseResultData;
import com.braintree.hybris.data.BraintreeTransactionEntryData;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.model.BrainTreeTransactionDetailModel;
import com.braintree.payment.dto.BraintreeInfo;
import com.braintreegateway.Customer;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public interface CsBrainTreeFacade
{
	/**
	 * Find transaction by several search fields and risk decision
	 *
	 * @return List of BraintreeTransactionEntry
	 * @throws AdapterException
	 *            if something went wrong
	 */
	List<BraintreeTransactionEntryData> findBrainTreeTransactions(Calendar startDate, Calendar endDate, String customer,
			String customerEmail, String transactionId, String transactionStatus, String riskDecision) throws AdapterException;
	
	/**
	 * Find transaction by several search fields
	 *
	 * @return List of BraintreeTransactionEntry
	 * @throws AdapterException
	 *            if something went wrong
	 */
	List<BraintreeTransactionEntryData> findBrainTreeTransactions(Calendar startDate, Calendar endDate, String customer,
			String customerEmail, String transactionId, String transactionStatus) throws AdapterException;

	/**
	 * Find transaction by several search fields
	 *
	 * @return List of BraintreeTransactionEntry
	 * @throws AdapterException
	 *            if something went wrong
	 */
	BraintreeTransactionEntryData findBrainTreeTransaction(String transactionId) throws AdapterException;

	/**
	 * void BT transaction from cscockpit.
	 *
	 * @return result of void operation true/false with order linked inside
	 */
	BrainTreeResponseResultData voidTransaction(BrainTreeTransactionDetailModel transaction);

	/**
	 * void BT transaction by id only for default Hybris scenario
	 *
	 */
	void voidTransaction(PaymentTransactionModel transaction) throws OrderCancelException;

	/**
	 * Clone Transaction by id and with amount and submitForSettlement
	 */
	BrainTreeResponseResultData cloneTransaction(BrainTreeTransactionDetailModel currentTransaction, String amount,
			boolean submitForSettlement);

	/**
	 * Refund transaction by id and with amount - important options
	 */
	BrainTreeResponseResultData refundTransaction(BrainTreeTransactionDetailModel currentTransaction, String amount);

	/**
	 * Refund BT transaction by id only for default Hybris scenario
	 */
	BrainTreeResponseResultData refundTransaction(PaymentTransactionModel transaction, String amount);

	/**
	 * Partial refund based on amount and order
	 */
	BrainTreeResponseResultData partialRefundTransaction(final OrderModel order, PaymentTransactionEntryModel paymentTransactionEntry , final BigDecimal amount)
			throws BraintreeErrorException;

	/**
	 * find all customers
	 */
	List<Customer> findCustomers(String customer, String customerEmail) throws AdapterException;

	/**
	 * find customer by id
	 */
	Customer findCustomer(String customerId) throws AdapterException;

	/**
	 * update customer
	 */
	BrainTreeUpdateCustomerResult updateCustomer(String id, String customerIdField, String firstNameField, String lastNameField,
			String emailField, String phoneField, String faxField, String websiteField, String companyField);

	/**
	 * create empty customer
	 * 
	 * @param brainTreeInfo
	 */
	SubscriptionResult createCustomer(BrainTreeInfo brainTreeInfo);

	/**
	 * add payment method
	 */
	BrainTreePaymentMethodResult createCreditCardPaymentMethod(final String customerId, final String paymentMethodNonce,
			final String cardholderName, final boolean isDefault, String billingAddressId);

	/**
	 * delete payment method
	 *
	 * @param token
	 */
	BrainTreePaymentMethodResult deletePaymentMethod(BrainTreePaymentInfoModel token);

	/**
	 * create new transaction
	 */
	BrainTreeResponseResultData createTransaction(BrainTreeInfo brainTreeInfo);

	/**
	 * remove custom from vault by id
	 */
	BrainTreeResponseResultData removeCustomer(String customerId);

	/**
	 * update payment Method by token
	 */
	BraintreePaymentMethodData updatePaymentMethod(String paymentMethodToken, BraintreePaymentMethodInfo braintreeInfo);

	/**
	 * Submit For Settlement transaction
	 */
	BrainTreeResponseResultData submitForSettlementTransaction(BrainTreeTransactionDetailModel currentTransaction, String value);

	/**
	 * generate client Token
	 */
	String generateClientToken();

	/**
	 * get Braintree payment info
	 */
	BraintreeInfo getBraintreeInfo();


	/**
	 * get Braintree payment info
	 */
	void saveBraintreeInfo(BraintreeInfo braintreeInfo);

	/**
	 * remove BraintreeInfo from session
	 */
	void removeBraintreeInfo();

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

	/**
	 * Authorize payment for cart
	 */

	boolean createPaymentInfo(CartModel cart, CardInfo cardInfo, double amount) throws PaymentException, ValidationException;

	void authorizePayment(AbstractOrderModel cart) throws PaymentException, ValidationException;

	PaymentTransactionEntryModel authorizePayment(final AbstractOrderModel cart, Map<String, String> customFields, BigDecimal totalAmount);

	BrainTreeResponseResultData voidTransaction(final PaymentTransactionEntryModel transaction) throws BraintreeErrorException;

	/**
	 * Calculates maximum amount available for refund at the moment
	 * 
	 * @param order
	 *           - order, for which amount will be calculated
	 * @return amount
	 */
}
