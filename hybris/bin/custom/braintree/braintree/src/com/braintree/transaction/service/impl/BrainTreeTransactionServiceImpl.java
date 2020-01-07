package com.braintree.transaction.service.impl;

import static com.braintree.constants.BraintreeConstants.BRAINTREE_PAYMENT;
import static com.braintree.constants.BraintreeConstants.BRAINTREE_PROVIDER_NAME;
import static com.braintree.constants.BraintreeConstants.PAYPAL_PAYMENT;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.braintree.command.request.BrainTreeAuthorizationRequest;
import com.braintree.command.request.BrainTreeCreatePaymentMethodRequest;
import com.braintree.command.request.BrainTreeFindMerchantAccountRequest;
import com.braintree.command.result.BrainTreeAuthorizationResult;
import com.braintree.command.result.BrainTreeCreatePaymentMethodResult;
import com.braintree.command.result.BrainTreeRefundTransactionResult;
import com.braintree.command.result.BrainTreeSaleTransactionResult;
import com.braintree.command.result.BrainTreeVoidResult;
import com.braintree.configuration.service.BrainTreeConfigService;
import com.braintree.enums.BrainTreeCardType;
import com.braintree.hybris.data.BraintreeTransactionEntryData;
import com.braintree.method.BrainTreePaymentService;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.payment.dto.BraintreeInfo;
import com.braintree.paypal.converters.impl.BillingAddressConverter;
import com.braintree.transaction.service.BrainTreeTransactionService;
import com.google.common.collect.Lists;

import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;


public class BrainTreeTransactionServiceImpl implements BrainTreeTransactionService
{

	private static final Logger LOG = Logger.getLogger(BrainTreeTransactionServiceImpl.class);

	private final static int DEFAULT_CURRENCY_DIGIT = 2;

	private CartService cartService;
	private ModelService modelService;
	private UserService userService;
	private BrainTreePaymentService brainTreePaymentService;
	private PaymentService paymentService;
	private BillingAddressConverter billingAddressConverter;
	private CheckoutCustomerStrategy checkoutCustomerStrategy;
	private CommonI18NService commonI18NService;
	private BrainTreeConfigService brainTreeConfigService;

	@Override
	public boolean createAuthorizationTransaction()
	{
		return createAuthorizationTransaction(Collections.EMPTY_MAP);
	}

	@Override
	public boolean createAuthorizationTransaction(Map<String, String> customFields) {
		final CartModel cart = cartService.getSessionCart();
		final BrainTreeAuthorizationResult result = brainTreeAuthorise(cart, customFields, getTotalAmount(cart, null));
		return handleAuthorizationResult(result, cart);
	}

	private boolean handleAuthorizationResult (BrainTreeAuthorizationResult result, AbstractOrderModel cart)
	{
		PaymentTransactionEntryModel paymentTransactionEntry = null;
		if (result.isSuccess())
		{
			paymentTransactionEntry = createTransactionEntry(PaymentTransactionType.AUTHORIZATION, cart, result);
			saveIntent(cart);
			savePaymentTransaction(paymentTransactionEntry);
			LOG.info("[BT AUTHORIZE] Transaction with code : " + paymentTransactionEntry.getCode() + " was created with status "
					+ TransactionStatusDetails.SUCCESFULL.name());
			return true;
		}
		else
		{
			LOG.error("[BT AUTHORIZE] Failed!");
			return false;
		}
	}

	private void saveIntent(AbstractOrderModel cart)
	{
		BrainTreePaymentInfoModel brainTreePaymentInfo = (BrainTreePaymentInfoModel) cart.getPaymentInfo();
		brainTreePaymentInfo.setPayPalIntent(getBrainTreeConfigService().getIntent());
		modelService.save(brainTreePaymentInfo);
	}

	private BrainTreeAuthorizationResult brainTreeAuthorise(final AbstractOrderModel cart, Map<String, String > customFields, BigDecimal totalAmount)
	{
		final CustomerModel customer = (CustomerModel) cart.getUser();

		BrainTreeAuthorizationRequest authorizationRequest = prepareAuthorizationRequest(cart, customer, customFields, totalAmount);

		return (BrainTreeAuthorizationResult) brainTreePaymentService.authorize(authorizationRequest, customer);
	}

	private BrainTreeAuthorizationRequest prepareAuthorizationRequest (AbstractOrderModel cart, CustomerModel customer,
																	   Map<String,String > customFields,BigDecimal totalAmount)
	{
		final PaymentInfoModel paymentInfo = cart.getPaymentInfo();

		final String braintreeCustomerId = customer.getBraintreeCustomerId();
		validateParameterNotNullStandardMessage("paymentInfo", paymentInfo);
		String methodNonce = null;
		String deviceData = null;
		String paymentType = null;
		Boolean liabilityShifted = null;
		Boolean usePaymentMethodToken = null;
		String paymentMethodToken = null;
		Boolean threeDSecureConfiguration = null;
		Boolean advancedFraudTools = null;
		Boolean isSkip3dSecureLiabilityResult = null;
		String creditCardStatementName = null;
		String merchantAccountIdForCurrentSite = null;
		String brainTreeChannel = null;
		boolean storeInVault = false;

		if (paymentInfo instanceof BrainTreePaymentInfoModel)
		{
			methodNonce = ((BrainTreePaymentInfoModel) paymentInfo).getNonce();
			deviceData = ((BrainTreePaymentInfoModel) paymentInfo).getDeviceData();
			paymentType = ((BrainTreePaymentInfoModel) paymentInfo).getPaymentProvider();
			usePaymentMethodToken = ((BrainTreePaymentInfoModel) paymentInfo).getUsePaymentMethodToken();
			paymentMethodToken = ((BrainTreePaymentInfoModel) paymentInfo).getPaymentMethodToken();
			storeInVault = ((BrainTreePaymentInfoModel) paymentInfo).isSaved();

			if (((BrainTreePaymentInfoModel) paymentInfo).getLiabilityShifted() != null)
			{
				liabilityShifted = ((BrainTreePaymentInfoModel) paymentInfo).getLiabilityShifted();
			}
			threeDSecureConfiguration = ((BrainTreePaymentInfoModel) paymentInfo).getThreeDSecureConfiguration();
			advancedFraudTools = ((BrainTreePaymentInfoModel) paymentInfo).getAdvancedFraudTools();
			isSkip3dSecureLiabilityResult = ((BrainTreePaymentInfoModel) paymentInfo).getIsSkip3dSecureLiabilityResult();
			creditCardStatementName = ((BrainTreePaymentInfoModel) paymentInfo).getCreditCardStatementName();
			merchantAccountIdForCurrentSite = ((BrainTreePaymentInfoModel) paymentInfo).getMerchantAccountIdForCurrentSite();
			brainTreeChannel = ((BrainTreePaymentInfoModel) paymentInfo).getBrainTreeChannel();
		}

		final AddressModel shippingAddress;
		final AddressModel billingAddress;
		shippingAddress = cart.getDeliveryAddress();

		final BillingInfo shippingInfo = billingAddressConverter.convert(shippingAddress);

		final BrainTreeAuthorizationRequest authorizationRequest = new BrainTreeAuthorizationRequest(paymentInfo.getCode(), null,
				getCurrencyInstanceByCart(cart), totalAmount, shippingInfo);
		authorizationRequest.setMethodNonce(methodNonce);
		authorizationRequest.setDeviceData(deviceData);
		authorizationRequest.setPaymentType(paymentType);
		authorizationRequest.setUsePaymentMethodToken(usePaymentMethodToken);
		authorizationRequest.setPaymentMethodToken(paymentMethodToken);
		authorizationRequest.setLiabilityShifted(liabilityShifted);
		authorizationRequest.setCustomerId(braintreeCustomerId);
		authorizationRequest.setThreeDSecureConfiguration(threeDSecureConfiguration);
		authorizationRequest.setAdvancedFraudTools(advancedFraudTools);
		authorizationRequest.setIsSkip3dSecureLiabilityResult(isSkip3dSecureLiabilityResult);
		authorizationRequest.setCreditCardStatementName(creditCardStatementName);

		if (shippingAddress != null){
			authorizationRequest.setBrainTreeAddressId(shippingAddress.getBrainTreeAddressId());
		}

		if (cart.getPaymentInfo().getBillingAddress() != null)
		{
			billingAddress = cart.getPaymentInfo().getBillingAddress();
			authorizationRequest.setBrainTreeBilligAddressId(billingAddress.getBrainTreeAddressId());
			final BillingInfo billingInfo = billingAddressConverter.convert(billingAddress);
			authorizationRequest.setBillingInfo(billingInfo);
		}

		if (StringUtils.isNotEmpty(merchantAccountIdForCurrentSite))
		{
			final BrainTreeFindMerchantAccountRequest brainTreeFindMerchantAccountRequest = new BrainTreeFindMerchantAccountRequest(
					StringUtils.EMPTY);
			brainTreeFindMerchantAccountRequest.setMerchantAccount(merchantAccountIdForCurrentSite);
			if (brainTreePaymentService.findMerchantAccount(brainTreeFindMerchantAccountRequest).isMerchantAccountExist())
			{
				authorizationRequest.setMerchantAccountIdForCurrentSite(merchantAccountIdForCurrentSite);
			}
		}
		authorizationRequest.setBrainTreeChannel(brainTreeChannel);
		authorizationRequest.setStoreInVault(storeInVault);
		if (customFields.size() > 0){
			authorizationRequest.setCustomFields(customFields);
		}

		return authorizationRequest;
	}

	protected BigDecimal calculateTotalAmount(final AbstractOrderModel cart)
	{
		BigDecimal totalPrice = BigDecimal.valueOf(cart.getTotalPrice().doubleValue());
		// Add the taxes to the total price if the cart is net; if the total was null taxes should be null as well
		if (Boolean.TRUE.equals(cart.getNet()) && totalPrice.compareTo(BigDecimal.ZERO) != 0 && cart.getTotalTax() != null)
		{
			totalPrice = totalPrice.add(BigDecimal.valueOf(cart.getTotalTax().doubleValue()));
		}
		return totalPrice;
	}

	@Override
	public boolean createPaymentMethodTokenForOrderReplenishment()
	{
		final CustomerModel customer = checkoutCustomerStrategy.getCurrentUserForCheckout();
		final PaymentInfoModel paymentInfo = cartService.getSessionCart().getPaymentInfo();
		if (paymentInfo instanceof BrainTreePaymentInfoModel)
		{
			if (!((BrainTreePaymentInfoModel) paymentInfo).getUsePaymentMethodToken().booleanValue())
			{
				final BrainTreeCreatePaymentMethodRequest request = new BrainTreeCreatePaymentMethodRequest(null,
						((BrainTreePaymentInfoModel) paymentInfo).getNonce(), customer.getBraintreeCustomerId());

				final BrainTreeCreatePaymentMethodResult result = brainTreePaymentService.createPaymentMethod(request);
				if (result != null)
				{
					((BrainTreePaymentInfoModel) paymentInfo).setPaymentMethodToken(result.getPaymentMethodToken());
					((BrainTreePaymentInfoModel) paymentInfo).setUsePaymentMethodToken(Boolean.TRUE);
					modelService.save(paymentInfo);
				}

			}
		}
		else
		{
			throw new AdapterException("Error during creation payment method for replenishment.");
		}

		return createAuthorizationTransaction();

	}

	@Override
	public PaymentTransactionEntryModel createAuthorizationTransaction(final AbstractOrderModel cart, Map<String, String> customFields, BigDecimal totalAmount)
	{
		PaymentTransactionEntryModel transactionEntry = null;
			final BrainTreeAuthorizationResult result = brainTreeAuthorise(cart, customFields, getTotalAmount(cart, totalAmount));

			transactionEntry = createTransactionEntry(PaymentTransactionType.AUTHORIZATION, cart, result);

			if (!result.isSuccess())
			{
				transactionEntry.setTransactionStatus(TransactionStatus.REJECTED.name());
				transactionEntry.setTransactionStatusDetails(TransactionStatusDetails.BANK_DECLINE.name());
			}

		savePaymentTransaction(transactionEntry, cart);
		return transactionEntry;
	}

	private BigDecimal getTotalAmount(AbstractOrderModel order, BigDecimal totalAmount)
	{
		if (totalAmount != null)
		{
			return totalAmount;
		}
		return calculateTotalAmount(order);
	}

	@Override
	public PaymentTransactionEntryModel createAuthorizationTransaction(final CartModel cart)
	{
		return createAuthorizationTransaction(cart, Collections.EMPTY_MAP, null);
	}

	@Override
	public PaymentTransactionEntryModel createAuthorizationTransaction(final AbstractOrderModel cart, Map<String, String> customFields)
	{
		return createAuthorizationTransaction(cart, customFields, null);
	}

	@Override
	public PaymentTransactionEntryModel createCancelTransaction(final PaymentTransactionModel transaction,
			final BrainTreeVoidResult voidResult)
	{
		final PaymentTransactionType transactionType = PaymentTransactionType.CANCEL;
		final String newEntryCode = paymentService.getNewPaymentTransactionEntryCode(transaction, transactionType);

		final PaymentTransactionEntryModel entry = modelService.create(PaymentTransactionEntryModel.class);
		entry.setType(transactionType);
		entry.setCode(newEntryCode);
		entry.setRequestId(voidResult.getRequestId());
		entry.setPaymentTransaction(transaction);
		entry.setCurrency(resolveCurrency(voidResult.getCurrencyIsoCode()));
		entry.setAmount(formatAmount(voidResult.getAmount()));
		entry.setTransactionStatus(voidResult.getTransactionStatus().toString());
		entry.setTransactionStatusDetails(voidResult.getTransactionStatusDetails().toString());
		entry.setTime(new Date());
		modelService.saveAll(entry, transaction);

		return entry;
	}

	@Override
	public PaymentTransactionEntryModel createCancelTransaction(final PaymentTransactionModel transaction,
			final BraintreeTransactionEntryData transactionEntryData)
	{
		final PaymentTransactionType transactionType = PaymentTransactionType.CANCEL;
		final String newEntryCode = paymentService.getNewPaymentTransactionEntryCode(transaction, transactionType);

		final PaymentTransactionEntryModel entry = modelService.create(PaymentTransactionEntryModel.class);
		entry.setType(transactionType);
		entry.setCode(newEntryCode);
		entry.setRequestId(transactionEntryData.getId());
		entry.setPaymentTransaction(transaction);
		entry.setCurrency(resolveCurrency(transactionEntryData.getCurrencyIsoCode()));
		entry.setAmount(formatAmount(transactionEntryData.getTotal()));
		entry.setTransactionStatus(TransactionStatus.ACCEPTED.name());
		entry.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL.name());
		entry.setTime(new Date());
		modelService.saveAll(entry, transaction);

		return entry;
	}

	@Override
	public PaymentTransactionEntryModel createRefundTransaction(final PaymentTransactionModel transaction,
			final BrainTreeRefundTransactionResult result)
	{
		final PaymentTransactionType transactionType = PaymentTransactionType.REFUND_STANDALONE;
		return createRefundTransaction(transaction, result, transactionType);
	}

	@Override
	public PaymentTransactionEntryModel createPartialRefundTransaction(final PaymentTransactionModel transaction,
			final BrainTreeRefundTransactionResult result)
	{
		final PaymentTransactionType transactionType = PaymentTransactionType.REFUND_PARTIAL;
		return createRefundTransaction(transaction, result, transactionType);
	}

	private PaymentTransactionEntryModel createRefundTransaction(final PaymentTransactionModel transaction,
			final BrainTreeRefundTransactionResult result, final PaymentTransactionType transactionType)
	{
		final String newEntryCode = paymentService.getNewPaymentTransactionEntryCode(transaction, transactionType);

		final PaymentTransactionEntryModel entry = modelService.create(PaymentTransactionEntryModel.class);
		entry.setType(transactionType);
		entry.setCode(newEntryCode);
		entry.setRequestId(result.getTransactionId());
		entry.setPaymentTransaction(transaction);
		entry.setCurrency(resolveCurrency(result.getCurrencyIsoCode()));
		entry.setAmount(formatAmount(result.getAmount()));
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		entry.setTime(new Date());
		modelService.saveAll(entry, transaction);
		return entry;
	}

	@Override
	public PaymentTransactionEntryModel createPartialCaptureTransaction(final PaymentTransactionModel transaction,
			final BrainTreeSaleTransactionResult result)
	{
		final PaymentTransactionType transactionType = PaymentTransactionType.PARTIAL_CAPTURE;
		final String newEntryCode = paymentService.getNewPaymentTransactionEntryCode(transaction, transactionType);

		final PaymentTransactionEntryModel entry = modelService.create(PaymentTransactionEntryModel.class);
		entry.setType(transactionType);
		entry.setCode(newEntryCode);
		entry.setRequestId(result.getTransactionId());
		entry.setPaymentTransaction(transaction);
		entry.setCurrency(resolveCurrency(result.getCurrencyIsoCode()));
		entry.setAmount(formatAmount(result.getAmount()));
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		entry.setTime(new Date());
		modelService.saveAll(entry, transaction);
		return entry;
	}



	protected CurrencyModel resolveCurrency(final String currencyIsoCode)
	{
		return getCommonI18NService().getCurrency(currencyIsoCode);
	}

	protected Currency getCurrencyInstanceByCart(final AbstractOrderModel cart)
	{
		return Currency.getInstance(cart.getCurrency().getIsocode());
	}

	private BigDecimal formatAmount(final BigDecimal amount)
	{
		return amount.setScale(getCurrencyDigit(), RoundingMode.HALF_EVEN);
	}

	private void savePaymentTransaction(final PaymentTransactionEntryModel paymentTransactionEntry)
	{
		final CartModel cart = cartService.getSessionCart();
		savePaymentTransaction(paymentTransactionEntry, cart);
	}

	private void savePaymentTransaction(final PaymentTransactionEntryModel paymentTransactionEntry, final AbstractOrderModel cart)
	{
		final List<PaymentTransactionModel> paymentTransactions;
		List<PaymentTransactionEntryModel> paymentTransactionEntrys;
		PaymentTransactionModel braintreePaymentTransaction = null;

		modelService.refresh(cart);
		if (!cart.getPaymentTransactions().isEmpty()) {
			paymentTransactions = Lists.newArrayList(cart.getPaymentTransactions());
		}
		else {
			paymentTransactions = Lists.newArrayList();
		}

		for (PaymentTransactionModel transaction: paymentTransactions){
			if(BRAINTREE_PROVIDER_NAME.equals(transaction.getPaymentProvider())){
				braintreePaymentTransaction = transaction;
				break;
			}
		}

		if (braintreePaymentTransaction == null){
			braintreePaymentTransaction = modelService.create(PaymentTransactionModel.class);
			braintreePaymentTransaction.setRequestId(paymentTransactionEntry.getRequestId());
			braintreePaymentTransaction.setPaymentProvider(BRAINTREE_PROVIDER_NAME);
			braintreePaymentTransaction.setPlannedAmount(paymentTransactionEntry.getAmount());

			if (cart.getPaymentInfo() != null && cart.getPaymentInfo() instanceof BrainTreePaymentInfoModel)
			{
				BrainTreePaymentInfoModel paymentInfo = (BrainTreePaymentInfoModel) cart.getPaymentInfo();
				braintreePaymentTransaction.setRequestToken(paymentInfo.getNonce());
				braintreePaymentTransaction.setInfo(paymentInfo);
			}
            paymentTransactions.add(braintreePaymentTransaction);
		}

		if (braintreePaymentTransaction.getEntries() != null){
			paymentTransactionEntrys = Lists.newArrayList(braintreePaymentTransaction.getEntries());
		} else {
			paymentTransactionEntrys = Lists.newArrayList();
		}

		paymentTransactionEntrys.add(paymentTransactionEntry);
		braintreePaymentTransaction.setEntries(paymentTransactionEntrys);
		cart.setPaymentTransactions(paymentTransactions);

		modelService.saveAll(paymentTransactionEntry, braintreePaymentTransaction, cart);
	}

	private PaymentTransactionEntryModel createTransactionEntry(final PaymentTransactionType type, final AbstractOrderModel cart,
			final BrainTreeAuthorizationResult result)
	{
		final PaymentTransactionEntryModel paymentTransactionEntry = modelService.create(PaymentTransactionEntryModel.class);

		final PaymentInfoModel paymentInfo = cart.getPaymentInfo();
		if (paymentInfo instanceof BrainTreePaymentInfoModel)
		{
			paymentTransactionEntry.setRequestToken(((BrainTreePaymentInfoModel) paymentInfo).getNonce());
		}

		paymentTransactionEntry.setType(type);
		paymentTransactionEntry.setTransactionStatus(result.getTransactionStatus().toString());
		paymentTransactionEntry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		paymentTransactionEntry.setRequestId(result.getRequestId());
		paymentTransactionEntry.setCurrency(resolveCurrency(result.getCurrency().getCurrencyCode()));


		final String code = BRAINTREE_PROVIDER_NAME + "_cart_" + cart.getCode() + "_stamp_" + System.currentTimeMillis();
		paymentTransactionEntry.setCode(code);

		paymentTransactionEntry.setAmount(result.getTotalAmount());
		paymentTransactionEntry.setTime(result.getAuthorizationTime());

		return paymentTransactionEntry;
	}

	private int getCurrencyDigit()
	{
		final CurrencyModel currency = commonI18NService.getCurrentCurrency();
		if (currency != null)
		{
			final Integer digits = currency.getDigits();
			return digits != null ? digits.intValue() : DEFAULT_CURRENCY_DIGIT;
		}
		return DEFAULT_CURRENCY_DIGIT;
	}

	@Override
	public BrainTreePaymentInfoModel createSubscription(final AddressModel billingAddress, final CustomerModel customer,
			final BraintreeInfo braintreeInfo)
	{
		validateParameterNotNull(braintreeInfo.getNonce(), "nonce cannot be null");
		validateParameterNotNull(billingAddress, "address cannot be null");
		validateParameterNotNull(customer, "customer cannot be null");

		final BrainTreePaymentInfoModel cardPaymentInfoModel = createCreditCardPaymentInfo(billingAddress, customer, braintreeInfo);

		billingAddress.setOwner(cardPaymentInfoModel);

		modelService.saveAll(billingAddress, cardPaymentInfoModel);
		modelService.refresh(customer);

		LOG.info("[BT PaymentInfo] Created card payment info with id: " + cardPaymentInfoModel.getCode());
		LOG.info("[BT PaymentInfo] Created billing address with id: " + billingAddress.getPk());

		final List<PaymentInfoModel> paymentInfoModels = new ArrayList<PaymentInfoModel>(customer.getPaymentInfos());

		if (!paymentInfoModels.contains(cardPaymentInfoModel))
		{
			paymentInfoModels.add(cardPaymentInfoModel);
			if (braintreeInfo.isSavePaymentInfo() && !braintreeInfo.isDuplicatedPayment())
			{
				customer.setPaymentInfos(paymentInfoModels);
				getModelService().save(customer);
			}

			getModelService().save(cardPaymentInfoModel);
			getModelService().refresh(customer);
		}

		return cardPaymentInfoModel;
	}

	private BrainTreePaymentInfoModel createCreditCardPaymentInfo(final AddressModel billingAddress,
			final CustomerModel customerModel, final BraintreeInfo braintreeInfo)
	{
		validateParameterNotNull(billingAddress, "billingAddress cannot be null");

		final BrainTreePaymentInfoModel cardPaymentInfoModel = modelService.create(BrainTreePaymentInfoModel.class);

		resolveBillingAddress(billingAddress, customerModel, braintreeInfo, cardPaymentInfoModel);
		billingAddress.setOwner(cardPaymentInfoModel);
		cardPaymentInfoModel.setBillingAddress(billingAddress);
		if (userService.isAnonymousUser(customerModel))
		{
			cardPaymentInfoModel.setCode(customerModel.getUid());
		}
		else
		{
			cardPaymentInfoModel.setCode(customerModel.getUid() + "_" + UUID.randomUUID());
		}
		cardPaymentInfoModel.setUser(customerModel);
		cardPaymentInfoModel.setPaymentMethodToken(braintreeInfo.getPaymentMethodToken());
		cardPaymentInfoModel.setNonce(braintreeInfo.getNonce());
		cardPaymentInfoModel.setDeviceData(braintreeInfo.getDeviceData());
		cardPaymentInfoModel.setImageSource(braintreeInfo.getImageSource());
		cardPaymentInfoModel.setExpirationMonth(braintreeInfo.getExpirationMonth());
		cardPaymentInfoModel.setExpirationYear(braintreeInfo.getExpirationYear());
		if (StringUtils.isNotEmpty(customerModel.getBraintreeCustomerId()))
		{
			cardPaymentInfoModel.setCustomerId(customerModel.getBraintreeCustomerId());
		}
		if (braintreeInfo.getLiabilityShifted() != null)
		{
			cardPaymentInfoModel.setLiabilityShifted(braintreeInfo.getLiabilityShifted());
		}
		cardPaymentInfoModel.setPaymentProvider(braintreeInfo.getPaymentProvider());
		cardPaymentInfoModel.setSaved(braintreeInfo.isSavePaymentInfo() && !braintreeInfo.isDuplicatedPayment());
		cardPaymentInfoModel.setCardNumber(braintreeInfo.getCardNumber());
		cardPaymentInfoModel.setCardholderName(braintreeInfo.getCardholderName());

		if (isNotEmpty(braintreeInfo.getCardType()))
		{
			final String brainTreeCardType = braintreeInfo.getCardType().replace("_", " ");
			cardPaymentInfoModel.setCardType(getBrainTreeCardTypeByName(brainTreeCardType));
		}

		cardPaymentInfoModel.setThreeDSecureConfiguration(getBrainTreeConfigService().get3dSecureConfiguration());
		cardPaymentInfoModel.setAdvancedFraudTools(getBrainTreeConfigService().getAdvancedFraudTools());
		cardPaymentInfoModel.setIsSkip3dSecureLiabilityResult(getBrainTreeConfigService().getIsSkip3dSecureLiabilityResult());
		cardPaymentInfoModel.setCreditCardStatementName(getBrainTreeConfigService().getCreditCardStatementName());
		cardPaymentInfoModel
				.setMerchantAccountIdForCurrentSite(getBrainTreeConfigService().getMerchantAccountIdForCurrentSiteAndCurrency());
		cardPaymentInfoModel.setBrainTreeChannel(getBrainTreeConfigService().getBrainTreeChannel());

		cardPaymentInfoModel.setUsePaymentMethodToken(Boolean.valueOf(braintreeInfo.isSavePaymentInfo()));

		return cardPaymentInfoModel;
	}

	private void resolveBillingAddress(final AddressModel billingAddress, final CustomerModel customerModel,
			final BraintreeInfo braintreeInfo, BrainTreePaymentInfoModel cardPaymentInfoModel)
	{
		if (CustomerType.GUEST.equals(customerModel.getType()) && (PAYPAL_PAYMENT.equals(braintreeInfo.getPaymentProvider()) || BRAINTREE_PAYMENT.equals(braintreeInfo.getPaymentProvider())))
		{
			cardPaymentInfoModel.setPaymentInfo(billingAddress.getEmail());
			final String email = StringUtils.substringAfter(customerModel.getUid(), "|");
			billingAddress.setEmail(StringUtils.isNotEmpty(email) ? email : customerModel.getContactEmail());
		}
	}

	protected BrainTreeCardType getBrainTreeCardTypeByName(final String brainTreeCardType)
	{
		return BrainTreeCardType.valueOf(brainTreeCardType);
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
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the brainTreePaymentService
	 */
	public BrainTreePaymentService getBrainTreePaymentService()
	{
		return brainTreePaymentService;
	}

	/**
	 * @param brainTreePaymentService
	 *           the brainTreePaymentService to set
	 */
	public void setBrainTreePaymentService(final BrainTreePaymentService brainTreePaymentService)
	{
		this.brainTreePaymentService = brainTreePaymentService;
	}

	/**
	 * @return the paymentService
	 */
	public PaymentService getPaymentService()
	{
		return paymentService;
	}

	/**
	 * @param paymentService
	 *           the paymentService to set
	 */
	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
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

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
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
}
