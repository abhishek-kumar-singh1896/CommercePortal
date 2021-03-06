package com.braintree.method.impl;

import static com.braintree.constants.BraintreeConstants.BRAINTREE_AUTHENTICATION_TOKEN;
import static com.braintree.constants.BraintreeConstants.BRAINTREE_PROVIDER_NAME;

import com.braintree.command.request.BrainTreeAddressRequest;
import com.braintree.command.request.BrainTreeAuthorizationRequest;
import com.braintree.command.request.BrainTreeCloneTransactionRequest;
import com.braintree.command.request.BrainTreeCreateCreditCardPaymentMethodRequest;
import com.braintree.command.request.BrainTreeCreatePaymentMethodRequest;
import com.braintree.command.request.BrainTreeCustomerRequest;
import com.braintree.command.request.BrainTreeDeletePaymentMethodRequest;
import com.braintree.command.request.BrainTreeFindMerchantAccountRequest;
import com.braintree.command.request.BrainTreeGenerateClientTokenRequest;
import com.braintree.command.request.BrainTreeRefundTransactionRequest;
import com.braintree.command.request.BrainTreeSaleTransactionRequest;
import com.braintree.command.request.BrainTreeSubmitForSettlementTransactionRequest;
import com.braintree.command.request.BrainTreeUpdateCustomerRequest;
import com.braintree.command.request.BrainTreeUpdatePaymentMethodRequest;
import com.braintree.command.result.BrainTreeAddressResult;
import com.braintree.command.result.BrainTreeCloneTransactionResult;
import com.braintree.command.result.BrainTreeCreatePaymentMethodResult;
import com.braintree.command.result.BrainTreeCustomerResult;
import com.braintree.command.result.BrainTreeFindCustomerResult;
import com.braintree.command.result.BrainTreeFindMerchantAccountResult;
import com.braintree.command.result.BrainTreeGenerateClientTokenResult;
import com.braintree.command.result.BrainTreePaymentMethodResult;
import com.braintree.command.result.BrainTreeRefundTransactionResult;
import com.braintree.command.result.BrainTreeSaleTransactionResult;
import com.braintree.command.result.BrainTreeSubmitForSettlementTransactionResult;
import com.braintree.command.result.BrainTreeUpdateCustomerResult;
import com.braintree.command.result.BrainTreeUpdatePaymentMethodResult;
import com.braintree.command.result.BrainTreeVoidResult;
import com.braintree.commands.BrainTreeCloneCommand;
import com.braintree.commands.BrainTreeCreateAddressCommand;
import com.braintree.commands.BrainTreeCreateCreditCardPaymentMethodCommand;
import com.braintree.commands.BrainTreeCreatePaymentMethodCommand;
import com.braintree.commands.BrainTreeDeletePaymentMethodCommand;
import com.braintree.commands.BrainTreeFindCustomerCommand;
import com.braintree.commands.BrainTreeFindMerchantAccountCommand;
import com.braintree.commands.BrainTreeGenerateClientTokenCommand;
import com.braintree.commands.BrainTreePartialCaptureCommand;
import com.braintree.commands.BrainTreeRefundCommand;
import com.braintree.commands.BrainTreeRemoveAddressCommand;
import com.braintree.commands.BrainTreeRemoveCustomerCommand;
import com.braintree.commands.BrainTreeSaleCommand;
import com.braintree.commands.BrainTreeSubmitForSettlementCommand;
import com.braintree.commands.BrainTreeUpdateAddressCommand;
import com.braintree.commands.BrainTreeUpdateCustomerCommand;
import com.braintree.commands.BrainTreeUpdatePaymentMethodCommand;
import com.braintree.commands.BrainTreeVoidCommand;
import com.braintree.configuration.service.BrainTreeConfigService;
import com.braintree.constants.BraintreeConstants;
import com.braintree.customer.service.BrainTreeCustomerAccountService;
import com.braintree.jalo.BrainTreePaymentInfo;
import com.braintree.method.BrainTreePaymentService;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.payment.dto.BraintreeInfo;
import com.braintree.paypal.converters.impl.BillingAddressConverter;
import com.braintree.transaction.service.BrainTreePaymentTransactionService;
import com.braintreegateway.CreditCard;
import com.braintreegateway.Customer;
import com.braintreegateway.PayPalAccount;
import com.braintreegateway.PaymentMethod;
import com.braintreegateway.exceptions.NotFoundException;

import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.AuthorizationCommand;
import de.hybris.platform.payment.commands.CreateSubscriptionCommand;
import de.hybris.platform.payment.commands.factory.CommandFactory;
import de.hybris.platform.payment.commands.factory.CommandFactoryRegistry;
import de.hybris.platform.payment.commands.factory.CommandNotSupportedException;
import de.hybris.platform.payment.commands.request.AuthorizationRequest;
import de.hybris.platform.payment.commands.request.CreateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.VoidRequest;
import de.hybris.platform.payment.commands.result.AuthorizationResult;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class BrainTreePaymentServiceImpl implements BrainTreePaymentService
{
	private final static Logger LOG = Logger.getLogger(BrainTreePaymentServiceImpl.class);

	private ModelService modelService;
	private CartService cartService;
	private BrainTreeCustomerAccountService brainTreeCustomerAccountService;
	private BrainTreeConfigService brainTreeConfigService;
	private CommandFactoryRegistry commandFactoryRegistry;

	private BillingAddressConverter billingAddressConverter;
	private CheckoutCustomerStrategy checkoutCustomerStrategy;

	private BrainTreePaymentTransactionService brainTreePaymentTransactionService;

	@Override
	public AuthorizationResult authorize(final AuthorizationRequest authorizationRequest)
	{
		final CustomerModel customer = checkoutCustomerStrategy.getCurrentUserForCheckout();
		return authorize(authorizationRequest, customer);
	}

	@Override
	public AuthorizationResult authorize(final AuthorizationRequest authorizationRequest, final CustomerModel customer)
	{
	    LOG.info("authorize, authorizationRequest.getTotalAmount: " + authorizationRequest.getTotalAmount());

		try
		{
			if (!Boolean.FALSE.toString().equals(getBrainTreeConfigService().getStoreInVaultForCurrentUser())
					&& !checkIfBrainTreeCustomerExist(customer.getBraintreeCustomerId()))
			{
				saveBraintreeCustomerId(customer, getCart().getDeliveryAddress());
				((BrainTreeAuthorizationRequest) authorizationRequest).setCustomerId(customer.getBraintreeCustomerId());
			}

            LOG.info("authorizationRequest: " + authorizationRequest);

			final AuthorizationCommand command = getCommandFactory().createCommand(AuthorizationCommand.class);
			final AuthorizationResult result = command.perform(authorizationRequest);

			return result;
		}
		catch (final NotFoundException exception)
		{
			LOG.error("[BT Payment Service] Errors occured not fount some item in BrainTree(throws NotFoundException)", exception);
			throw new AdapterException("Problem occurred in Payment Provider configuration. Please contact with store support.");
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during authorization: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage());
		}
	}

	@Override
	public BrainTreeVoidResult voidTransaction(final VoidRequest voidRequest)
	{
		try
		{
			final BrainTreeVoidCommand command = getCommandFactory().createCommand(BrainTreeVoidCommand.class);
			final BrainTreeVoidResult result = command.perform(voidRequest);

			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to void transaction: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	@Override
	public BrainTreeCloneTransactionResult cloneTransaction(final BrainTreeCloneTransactionRequest request)
	{
		try
		{
			final BrainTreeCloneCommand command = getCommandFactory().createCommand(BrainTreeCloneCommand.class);
			final BrainTreeCloneTransactionResult result = command.perform(request);
			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to clone transaction: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	@Override
	public BrainTreeRefundTransactionResult refundTransaction(final BrainTreeRefundTransactionRequest request)
	{
		try
		{
			final BrainTreeRefundCommand command = getCommandFactory().createCommand(BrainTreeRefundCommand.class);
			final BrainTreeRefundTransactionResult result = command.perform(request);

			return result;
		}
		catch (final CommandNotSupportedException exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to refund transaction: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	@Override
	public BrainTreeUpdateCustomerResult updateCustomer(final BrainTreeUpdateCustomerRequest request)
	{
		try
		{
			final BrainTreeUpdateCustomerCommand command = getCommandFactory().createCommand(BrainTreeUpdateCustomerCommand.class);
			final BrainTreeUpdateCustomerResult result = command.perform(request);

			return result;
		}
		catch (final CommandNotSupportedException exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to update customer: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	@Override
	public BrainTreeSaleTransactionResult saleTransaction(final BrainTreeSaleTransactionRequest request)
	{
		try
		{
			final BrainTreeSaleCommand command = getCommandFactory().createCommand(BrainTreeSaleCommand.class);
			final BrainTreeSaleTransactionResult result = command.perform(request);

			return result;
		}
		catch (final CommandNotSupportedException exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to sate transaction: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	@Override
	public BrainTreeSaleTransactionResult partialCaptureTransaction(final BrainTreeSaleTransactionRequest request)
	{
		try
		{
			final BrainTreePartialCaptureCommand command = getCommandFactory().createCommand(BrainTreePartialCaptureCommand.class);
			final BrainTreeSaleTransactionResult result = command.perform(request);

			return result;
		}
		catch (final CommandNotSupportedException exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to sate transaction: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	@Override
	public BrainTreeCustomerResult removeCustomer(final BrainTreeCustomerRequest request)
	{
		try
		{
			final BrainTreeRemoveCustomerCommand command = getCommandFactory().createCommand(BrainTreeRemoveCustomerCommand.class);
			final BrainTreeCustomerResult result = command.perform(request);

			return result;
		}
		catch (final CommandNotSupportedException exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to remove customer: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	@Override
	public BrainTreeUpdatePaymentMethodResult updatePaymentMethod(final BrainTreeUpdatePaymentMethodRequest request)
	{
		try
		{
			final BrainTreeUpdatePaymentMethodCommand command = getCommandFactory().createCommand(
					BrainTreeUpdatePaymentMethodCommand.class);
			final BrainTreeUpdatePaymentMethodResult result = command.perform(request);

			return result;
		}
		catch (final CommandNotSupportedException exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to update payment Method: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	@Override
	public SubscriptionResult createCustomerSubscription(final CreateSubscriptionRequest subscriptionRequest)
			throws AdapterException
	{
		try
		{
			final CreateSubscriptionCommand command = getCommandFactory().createCommand(CreateSubscriptionCommand.class);
			final SubscriptionResult result = command.perform(subscriptionRequest);

			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during customer creation: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage());
		}
	}

	@Override
	public BrainTreeGenerateClientTokenResult generateClientToken(final BrainTreeGenerateClientTokenRequest clientTokenRequest)
			throws AdapterException
	{
		try
		{
			final BrainTreeGenerateClientTokenCommand command = getCommandFactory().createCommand(BrainTreeGenerateClientTokenCommand.class);

			final BrainTreeGenerateClientTokenResult result = command.perform(clientTokenRequest);
			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during token generation: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	@Override
	public BrainTreeFindCustomerResult findCustomer(final BrainTreeCustomerRequest findCustomerRequest) throws AdapterException
	{
		try
		{
			final BrainTreeFindCustomerCommand command = getCommandFactory().createCommand(BrainTreeFindCustomerCommand.class);
			final BrainTreeFindCustomerResult result = command.perform(findCustomerRequest);
			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to find customer generation: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	@Override
	public BrainTreeSubmitForSettlementTransactionResult submitForSettlementTransaction(
			final BrainTreeSubmitForSettlementTransactionRequest request)
	{
	    LOG.info("submitForSettlementTransaction");
		try
		{
			final BrainTreeSubmitForSettlementCommand command = getCommandFactory().createCommand(BrainTreeSubmitForSettlementCommand.class);
			final BrainTreeSubmitForSettlementTransactionResult result = command.perform(request);

			updateCaptureTransaction(result, request);

			return result;
		}
		catch (final CommandNotSupportedException exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to submit for settlement transaction: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	private void updateCaptureTransaction(final BrainTreeSubmitForSettlementTransactionResult result, final BrainTreeSubmitForSettlementTransactionRequest request)
	{
		List<PaymentTransactionModel> transactions = brainTreePaymentTransactionService.getTransactionsByRequestIdAndPaymentProvider(request.getTransactionId(), BraintreeConstants.BRAINTREE_PROVIDER_NAME);
		for (PaymentTransactionModel transactionModel : transactions) {
			for (PaymentTransactionEntryModel transactionEntryModel : transactionModel.getEntries()) {
				if (PaymentTransactionType.CAPTURE.equals(transactionEntryModel.getType())) {
						transactionEntryModel.setAmount(request.getAmount());
						modelService.save(transactionEntryModel);
					}
				}
			}
	}

	@Override
	public BrainTreeAddressResult createAddress(final BrainTreeAddressRequest addressRequest, final CustomerModel customer)
	{
		try
		{
			populateCustomerID(addressRequest, customer);
			final BrainTreeCreateAddressCommand command = getCommandFactory().createCommand(BrainTreeCreateAddressCommand.class);
			final BrainTreeAddressResult result = command.perform(addressRequest);
			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during address creation: " + exception.getMessage(), exception);
			return null;
		}
	}

	private void populateCustomerID(final BrainTreeAddressRequest addressRequest, final CustomerModel customer)
	{
		if (addressRequest.getCustomerId() == null)
		{
			if (customer.getBraintreeCustomerId() == null)
			{
				saveBraintreeCustomerId(customer);
				addressRequest.setCustomerId(customer.getBraintreeCustomerId());
			}
			else
			{
				addressRequest.setCustomerId(customer.getBraintreeCustomerId());
			}
		}
	}

	@Override
	public BrainTreeAddressResult updateAddress(final BrainTreeAddressRequest addressRequest)
	{
		try
		{
			final BrainTreeUpdateAddressCommand command = getCommandFactory().createCommand(BrainTreeUpdateAddressCommand.class);
			final BrainTreeAddressResult result = command.perform(addressRequest);
			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during address[" + addressRequest.getAddressId() + "] update: " + exception.getMessage(), exception);
		}
		return null;
	}

	@Override
	public BrainTreeAddressResult removeAddress(final BrainTreeAddressRequest addressRequest)
	{
		try
		{
			final BrainTreeRemoveAddressCommand command = getCommandFactory().createCommand(BrainTreeRemoveAddressCommand.class);
			final BrainTreeAddressResult result = command.perform(addressRequest);

			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during address[" + addressRequest.getAddressId() + "] creation: " + exception.getMessage(), exception);
		}
		return new BrainTreeAddressResult();
	}

	@Override
	public BrainTreePaymentInfoModel completeCreateSubscription(final CustomerModel customer, final String paymentInfoId)
	{
		final CartModel card = getCart();

		final BrainTreePaymentInfoModel paymentInfo = brainTreeCustomerAccountService.getBrainTreePaymentInfoForCode(customer,
				paymentInfoId);

		if (paymentInfo != null)
		{
			paymentInfo.setUsePaymentMethodToken(Boolean.TRUE);
			modelService.save(paymentInfo);
			card.setPaymentInfo(paymentInfo);
			modelService.save(card);

			return paymentInfo;
		}
		else
		{
			return null;
		}
	}

	@Override
	public String generateClientToken()
	{
		final String merchantAccountId = getBrainTreeConfigService().getMerchantAccountIdForCurrentSiteAndCurrency();
		return getClientToken(merchantAccountId);
	}

	@Override
	public String generateClientToken(final String site, final String currency)
	{
		final String merchantAccountId = getBrainTreeConfigService().getMerchantAccountIdForSiteAndCurrencyIsoCode(site, currency);
		return getClientToken(merchantAccountId);
	}

	@Override
	public BrainTreeCreatePaymentMethodResult createPaymentMethod(final BrainTreeCreatePaymentMethodRequest request)
	{
	    LOG.info("createPaymentMethod");
		try
		{
			final BrainTreeCreatePaymentMethodCommand command = getCommandFactory().createCommand(BrainTreeCreatePaymentMethodCommand.class);
			final BrainTreeCreatePaymentMethodResult result = command.perform(request);
			LOG.info("Created PaymentMethod, result: " + result);
			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors payment method creating: " + exception.getMessage(), exception);
			return null;
		}
	}

	@Override
	public BrainTreePaymentMethodResult createCreditCardPaymentMethod(final BrainTreeCreateCreditCardPaymentMethodRequest request)
	{
		try
		{
			final BrainTreeCreateCreditCardPaymentMethodCommand command = getCommandFactory().createCommand(
					BrainTreeCreateCreditCardPaymentMethodCommand.class);
			final BrainTreePaymentMethodResult result = command.perform(request);
			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors payment method creating: " + exception.getMessage(), exception);
			return null;
		}
	}

	@Override
	public BrainTreePaymentMethodResult deletePaymentMethod(final BrainTreeDeletePaymentMethodRequest request)
	{
		try
		{
			final BrainTreeDeletePaymentMethodCommand command = getCommandFactory().createCommand(
					BrainTreeDeletePaymentMethodCommand.class);
			final BrainTreePaymentMethodResult result = command.perform(request);
			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to delete payment method: " + exception.getMessage(), exception);
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	@Override
	public BrainTreeFindMerchantAccountResult findMerchantAccount(
			final BrainTreeFindMerchantAccountRequest brainTreeFindMerchantAccountRequest)
	{
		try
		{
			final BrainTreeFindMerchantAccountCommand command = getCommandFactory().createCommand(
					BrainTreeFindMerchantAccountCommand.class);
			final BrainTreeFindMerchantAccountResult result = command.perform(brainTreeFindMerchantAccountRequest);
			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to find merchant account: " + exception.getMessage(), exception);
			throw new AdapterException("Request failed!", exception);
		}
	}

	private CartModel getCart()
	{
		return cartService.getSessionCart();
	}

	private String getClientToken(final String merchantAccountId)
	{
		final String authenticationToken = getAuthenticationToken();
		if (authenticationToken != null && !StringUtils.EMPTY.equals(authenticationToken))
		{
			return authenticationToken;
		}

//		final String merchantAccountId = getBrainTreeConfigService().getMerchantAccountIdForCurrentSiteAndCurrency();
//		if (StringUtils.isNotEmpty(merchantAccountId))
//		{
//			final BrainTreeFindMerchantAccountRequest brainTreeFindMerchantAccountRequest = new BrainTreeFindMerchantAccountRequest(
//					StringUtils.EMPTY);
//			brainTreeFindMerchantAccountRequest.setMerchantAccount(merchantAccountId);
//			final boolean isMerchantAccountExist = findMerchantAccount(brainTreeFindMerchantAccountRequest).isMerchantAccountExist();
//			if (isMerchantAccountExist)
//			{
//				brainTreeFindMerchantAccountRequest.setMerchantAccount(merchantAccountId);
//			}
//		}
        BrainTreeGenerateClientTokenRequest brainTreeGenerateClientTokenRequest = new BrainTreeGenerateClientTokenRequest(StringUtils.EMPTY);
        if (StringUtils.isNotEmpty(merchantAccountId)) {
            brainTreeGenerateClientTokenRequest.setMerchantAccount(merchantAccountId);
        }

        final BrainTreeGenerateClientTokenResult response = generateClientToken(brainTreeGenerateClientTokenRequest);
		return response.getClientToken();
	}

	private String getAuthenticationToken()
	{
		return brainTreeConfigService.getConfigurationService().getConfiguration().getString(BRAINTREE_AUTHENTICATION_TOKEN);
	}

	private boolean checkIfBrainTreeCustomerExist(final String braintreeCustomerId)
	{
		return findBrainTreeCustomer(braintreeCustomerId).isCustomerExist();
	}

	private SubscriptionResult createCustomerSubscription(final CustomerModel customer, final AddressModel shippingAddress)
	{
		final BillingInfo billingInfo = new BillingInfo();
		if (shippingAddress != null)
		{
			billingAddressConverter.convert(shippingAddress, billingInfo);
		}
		else
		{
			billingInfo.setEmail(customer.getContactEmail());
		}

		final CreateSubscriptionRequest createSubscriptionRequest = new CreateSubscriptionRequest(null, billingInfo, null, null,
				null, null, null);

		final SubscriptionResult response = createCustomerSubscription(createSubscriptionRequest);
		return response;
	}


	private String saveBraintreeCustomerId(final CustomerModel customer, final AddressModel shippingAddress)
	{
		final SubscriptionResult result = createCustomerSubscription(customer, shippingAddress);
		customer.setBraintreeCustomerId(result.getMerchantTransactionCode());
		getModelService().save(customer);
		return result.getMerchantTransactionCode();
	}

	private String saveBraintreeCustomerId(final CustomerModel customer)
	{
		final SubscriptionResult result = createCustomerSubscription(customer, null);
		customer.setBraintreeCustomerId(result.getMerchantTransactionCode());
		getModelService().save(customer);
		return result.getMerchantTransactionCode();
	}

	private CommandFactory getCommandFactory()
	{
		return commandFactoryRegistry.getFactory(BRAINTREE_PROVIDER_NAME);
	}

	@Override
	public BrainTreeCreatePaymentMethodResult createPaymentMethodForCustomer(final CustomerModel customer,
			final AddressModel billingAddress, final BraintreeInfo braintreeInfo)
	{
		final BillingInfo billingInfo = billingAddressConverter.convert(billingAddress);

		final BrainTreeCreatePaymentMethodRequest request = new BrainTreeCreatePaymentMethodRequest(null, braintreeInfo.getNonce(),
				braintreeInfo.getCardholderName(), customer.getBraintreeCustomerId(), billingAddress.getBrainTreeAddressId(),
				billingInfo, braintreeInfo.getAmount());

		BrainTreeFindCustomerResult findCustomerResult = findBrainTreeCustomer(customer.getBraintreeCustomerId());
		if (!findCustomerResult.isCustomerExist())
		{
			final String customerId = saveBraintreeCustomerId(customer, billingAddress);
			request.setCustomerId(customerId);
			return createPaymentMethod(request);
		} else {
			return findPaymentMethod(customer, findCustomerResult.getCustomer(), braintreeInfo, request);
		}
	}

	private BrainTreeFindCustomerResult findBrainTreeCustomer(String braintreeCustomerId){
		if (StringUtils.isEmpty(braintreeCustomerId))
		{
			return new BrainTreeFindCustomerResult(false);
		}
		final BrainTreeCustomerRequest findCustomerRequest = new BrainTreeCustomerRequest(braintreeCustomerId);
		findCustomerRequest.setCustomerId(braintreeCustomerId);
		return findCustomer(findCustomerRequest);
	}

	private BrainTreeCreatePaymentMethodResult findPaymentMethod(CustomerModel customerModel, Customer customer, BraintreeInfo braintreeInfo, BrainTreeCreatePaymentMethodRequest request){

		BrainTreeCreatePaymentMethodResult result = null;

		if (BraintreeConstants.PAYPAL_PAYMENT.equals(braintreeInfo.getPaymentProvider())
				&& !BraintreeConstants.PAYPAL_INTENT_ORDER.equals(braintreeInfo.getIntent())){
			result = hasPayPalAccount(customerModel, customer.getPayPalAccounts(), braintreeInfo);
		}

		if (result == null){
			result = createPaymentMethod(request);
		}

		return result;
	}

	private BrainTreeCreatePaymentMethodResult hasPayPalAccount(CustomerModel customerModel, List<PayPalAccount> payPalAccounts, BraintreeInfo braintreeInfo){
		if (StringUtils.isNotBlank(braintreeInfo.getEmail())) {
			List<String> paymentMethodTokens = getPaymentMethodTokensForPayPalAccount(customerModel.getPaymentInfos(), braintreeInfo.getEmail());
			if (!paymentMethodTokens.isEmpty()) {
				for (PayPalAccount account : payPalAccounts) {
					for(String token : paymentMethodTokens) {
						if (token.equalsIgnoreCase(account.getToken())) {
							BrainTreeCreatePaymentMethodResult result = createPaymentMethodResult(account);
							braintreeInfo.setDuplicatedPayment(Boolean.TRUE);
							return result;
						}
					}
				}
			}
		}
		return null;
	}

	private List<String> getPaymentMethodTokensForPayPalAccount(Collection<PaymentInfoModel> paymentInfoModels, String payPalEmail){
		List<String> paymentMethodTokens = new ArrayList<>();
		for (PaymentInfoModel paymentInfo : paymentInfoModels) {
			if (isSavedPayPalAccount(paymentInfo, payPalEmail))
			{
				paymentMethodTokens.add(((BrainTreePaymentInfoModel) paymentInfo).getPaymentMethodToken());
			}
		}
		return paymentMethodTokens;
	}

	private boolean isSavedPayPalAccount(PaymentInfoModel paymentInfo, String email){
		return paymentInfo.isSaved()
				&& paymentInfo instanceof BrainTreePaymentInfoModel
				&& BraintreeConstants.PAYPAL_PAYMENT.equals(((BrainTreePaymentInfoModel) paymentInfo).getPaymentProvider())
				&& email.equals((paymentInfo).getBillingAddress().getEmail())
				&& StringUtils.isNotBlank(((BrainTreePaymentInfoModel) paymentInfo).getPaymentMethodToken());
	}

	private BrainTreeCreatePaymentMethodResult createPaymentMethodResult(PayPalAccount paymentMethod){
		BrainTreeCreatePaymentMethodResult result = new BrainTreeCreatePaymentMethodResult();
		result.setPaymentMethodToken(paymentMethod.getToken());
		result.setImageSource(paymentMethod.getImageUrl());
		result.setEmail(paymentMethod.getEmail());
		result.setSuccess(Boolean.TRUE);
		result.setCardType(StringUtils.EMPTY);
		result.setExpirationMonth(StringUtils.EMPTY);
		result.setExpirationYear(StringUtils.EMPTY);
		result.setCardNumber(StringUtils.EMPTY);
		result.setCardholderName(StringUtils.EMPTY);
		return result;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the brainTreeCustomerAccountService
	 */
	public BrainTreeCustomerAccountService getBrainTreeCustomerAccountService()
	{
		return brainTreeCustomerAccountService;
	}

	/**
	 * @param brainTreeCustomerAccountService
	 *           the brainTreeCustomerAccountService to set
	 */
	public void setBrainTreeCustomerAccountService(final BrainTreeCustomerAccountService brainTreeCustomerAccountService)
	{
		this.brainTreeCustomerAccountService = brainTreeCustomerAccountService;
	}

	/**
	 * @return the brainTreeConfigService
	 */
	public BrainTreeConfigService getBrainTreeConfigService()
	{
		return brainTreeConfigService;
	}

	/**
	 * @param brainTreeConfigService
	 *           the brainTreeConfigService to set
	 */
	public void setBrainTreeConfigService(final BrainTreeConfigService brainTreeConfigService)
	{
		this.brainTreeConfigService = brainTreeConfigService;
	}

	/**
	 * @return the commandFactoryRegistry
	 */
	public CommandFactoryRegistry getCommandFactoryRegistry()
	{
		return commandFactoryRegistry;
	}

	/**
	 * @param commandFactoryRegistry
	 *           the commandFactoryRegistry to set
	 */
	public void setCommandFactoryRegistry(final CommandFactoryRegistry commandFactoryRegistry)
	{
		this.commandFactoryRegistry = commandFactoryRegistry;
	}

	/**
	 * @return the billingAddressConverter
	 */
	public BillingAddressConverter getBillingAddressConverter()
	{
		return billingAddressConverter;
	}

	/**
	 * @param billingAddressConverter
	 *           the billingAddressConverter to set
	 */
	public void setBillingAddressConverter(final BillingAddressConverter billingAddressConverter)
	{
		this.billingAddressConverter = billingAddressConverter;
	}

	/**
	 * @return the checkoutCustomerStrategy
	 */
	public CheckoutCustomerStrategy getCheckoutCustomerStrategy()
	{
		return checkoutCustomerStrategy;
	}

	/**
	 * @param checkoutCustomerStrategy
	 *           the checkoutCustomerStrategy to set
	 */
	public void setCheckoutCustomerStrategy(final CheckoutCustomerStrategy checkoutCustomerStrategy)
	{
		this.checkoutCustomerStrategy = checkoutCustomerStrategy;
	}

	public BrainTreePaymentTransactionService getBrainTreePaymentTransactionService() {
		return brainTreePaymentTransactionService;
	}

	public void setBrainTreePaymentTransactionService(BrainTreePaymentTransactionService brainTreePaymentTransactionService) {
		this.brainTreePaymentTransactionService = brainTreePaymentTransactionService;
	}
}
