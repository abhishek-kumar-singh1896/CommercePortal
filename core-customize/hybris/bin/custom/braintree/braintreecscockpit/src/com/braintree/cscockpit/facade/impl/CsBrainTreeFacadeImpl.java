package com.braintree.cscockpit.facade.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import static org.zkoss.util.resource.Labels.getLabel;

import com.braintree.command.request.BrainTreeCloneTransactionRequest;
import com.braintree.command.request.BrainTreeCreateCreditCardPaymentMethodRequest;
import com.braintree.command.request.BrainTreeCustomerRequest;
import com.braintree.command.request.BrainTreeDeletePaymentMethodRequest;
import com.braintree.command.request.BrainTreeFindMerchantAccountRequest;
import com.braintree.command.request.BrainTreeFindTransactionRequest;
import com.braintree.command.request.BrainTreeRefundTransactionRequest;
import com.braintree.command.request.BrainTreeSaleTransactionRequest;
import com.braintree.command.request.BrainTreeSubmitForSettlementTransactionRequest;
import com.braintree.command.request.BrainTreeUpdateCustomerRequest;
import com.braintree.command.request.BrainTreeUpdatePaymentMethodRequest;
import com.braintree.command.result.BrainTreeAddressResult;
import com.braintree.command.result.BrainTreeCloneTransactionResult;
import com.braintree.command.result.BrainTreeCustomerResult;
import com.braintree.command.result.BrainTreeFindCustomersResult;
import com.braintree.command.result.BrainTreeFindTransactionResult;
import com.braintree.command.result.BrainTreePaymentMethodResult;
import com.braintree.command.result.BrainTreeRefundTransactionResult;
import com.braintree.command.result.BrainTreeSaleTransactionResult;
import com.braintree.command.result.BrainTreeSubmitForSettlementTransactionResult;
import com.braintree.command.result.BrainTreeUpdateCustomerResult;
import com.braintree.command.result.BrainTreeUpdatePaymentMethodResult;
import com.braintree.command.result.BrainTreeVoidResult;
import com.braintree.configuration.service.BrainTreeConfigService;
import com.braintree.constants.BraintreeConstants;
import com.braintree.constants.BraintreecscockpitConstants;
import com.braintree.converters.BraintreePaymentMethodConverter;
import com.braintree.converters.BraintreeCustomerDetailsConverter;
import com.braintree.cscockpit.converters.BraintreeDefaultResponseConverter;
import com.braintree.cscockpit.converters.BraintreePaymentMethodResponseConverter;
import com.braintree.cscockpit.converters.BraintreeResponseConverter;
import com.braintree.converters.BraintreeTransactionConverter;
import com.braintree.cscockpit.data.BrainTreeInfo;
import com.braintree.cscockpit.data.BraintreePaymentMethodData;
import com.braintree.cscockpit.data.BraintreePaymentMethodInfo;
import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.services.BraintreeAddressService;
import com.braintree.cscockpit.services.CustomerSearchService;
import com.braintree.cscockpit.services.TransactionSearchService;
import com.braintree.cscockpit.widgets.services.chekout.BraintreeCsCheckoutService;
import com.braintree.customfield.service.CustomFieldsService;
import com.braintree.exceptions.BraintreeErrorException;
import com.braintree.hybris.data.BrainTreeResponseResultData;
import com.braintree.hybris.data.BraintreeTransactionData;
import com.braintree.hybris.data.BraintreeTransactionEntryData;
import com.braintree.method.BrainTreePaymentService;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.model.BrainTreeTransactionDetailModel;
import com.braintree.order.refund.partial.services.BraintreePartialRefundService;
import com.braintree.payment.dto.BraintreeInfo;
import com.braintree.payment.info.service.PaymentInfoService;
import com.braintree.transaction.service.BrainTreeTransactionService;
import com.braintreegateway.Customer;
import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.Transaction;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.request.CreateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.VoidRequest;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class CsBrainTreeFacadeImpl implements CsBrainTreeFacade
{
	private static final Logger LOG = Logger.getLogger(CsBrainTreeFacadeImpl.class);

	public static final Integer CUSTOMER_SEARCH_LIMIT = Integer.valueOf(300);

	private BraintreeAddressService braintreeAddressService;
	private TransactionSearchService transactionSearchService;
	private UserService userService;
	private BraintreeTransactionConverter brainTreeTransactionConverter;
	private BraintreeResponseConverter braintreeResponseConverter;
	private BraintreeDefaultResponseConverter defaultResponseConverter;
	private BraintreeCustomerDetailsConverter braintreeCustomerDetailsConverter;
	private BraintreePaymentMethodResponseConverter paymentMethodResponseConverter;
	private ModelService modelService;
	private BrainTreeTransactionService brainTreeTransactionService;
	private EnumerationService enumerationService;
	private BrainTreePaymentService brainTreePaymentService;
	private CustomerSearchService customerSearchService;
	private BrainTreeConfigService brainTreeConfigService;
	private PaymentInfoService paymentInfoService;
	private BraintreePaymentMethodConverter paymentMethodConverter;
	private SessionService sessionService;
	private BraintreeCsCheckoutService braintreeCsCheckoutService;
	private BraintreePartialRefundService partialRefundService;
	private CustomFieldsService customFieldsService;


	@Override
	public List<BraintreeTransactionEntryData> findBrainTreeTransactions(final Calendar startDate, final Calendar endDate,
			final String customerId, final String customerEmail, final String transactionId, final String transactionStatus, final String riskDecision)
	{
		List<BraintreeTransactionEntryData> braintreeTransactions = findBrainTreeTransactions(startDate, endDate, customerId, customerEmail, transactionId, transactionStatus);
		
		if (!"".equals(riskDecision))
		{
   		List<BraintreeTransactionEntryData> filteredTransactions = filterTransactionsByRiskDecision(braintreeTransactions, riskDecision);	
   		return filteredTransactions;
		}
		
		return braintreeTransactions;
	}
	
	private List<BraintreeTransactionEntryData> filterTransactionsByRiskDecision (List<BraintreeTransactionEntryData> braintreeTransactions, String riskDecision)
	{
		if (!braintreeTransactions.isEmpty() && riskDecision != null)
		{
			List<BraintreeTransactionEntryData> filteredTransactions = new ArrayList<BraintreeTransactionEntryData>();
			for (BraintreeTransactionEntryData transaction : braintreeTransactions)
			{
				if (transaction != null
						&& riskDecision.equalsIgnoreCase(transaction.getRiskDecision()))
				{
					filteredTransactions.add(transaction);
				}
			}
			return filteredTransactions;
		}
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public List<BraintreeTransactionEntryData> findBrainTreeTransactions(final Calendar startDate, final Calendar endDate,
			final String customerId, final String customerEmail, final String transactionId, final String transactionStatus)
	{
		final String merchantTransactionCode = getMerchantCode();
		if (merchantTransactionCode != null)
		{
			final BrainTreeFindTransactionRequest findTransactionRequest = new BrainTreeFindTransactionRequest(
					merchantTransactionCode);
			findTransactionRequest.setStartDate(startDate);
			findTransactionRequest.setEndDate(endDate);
			findTransactionRequest.setTransactionId(transactionId);
			findTransactionRequest.setTransactionStatus(transactionStatus);
			findTransactionRequest.setCustomerEmail(customerEmail);
			findTransactionRequest.setCustomerId(customerId);

			final BrainTreeFindTransactionResult transactions = getTransactionSearchService().findTransactions(
					findTransactionRequest);

			final BraintreeTransactionData convert = getBrainTreeTransactionConverter().convert(transactions);

			return convert.getTransactionEntries();
		}
		LOG.error("[BT Payment Service] Error user must be Customer type!");
		return Collections.emptyList();
	}

	@Override
	public BraintreeTransactionEntryData findBrainTreeTransaction(final String transactionId) throws AdapterException
	{
		final List<BraintreeTransactionEntryData> brainTreeTransactions = findBrainTreeTransactions(null, null, null, null,
				transactionId, null);
		if (CollectionUtils.isNotEmpty(brainTreeTransactions))
		{
			return brainTreeTransactions.get(0);
		}
		return null;
	}

	@Override
	public void voidTransaction(final PaymentTransactionModel transaction) throws OrderCancelException
	{
		validateParameterNotNullStandardMessage("transaction", transaction);
		final String merchantTransactionCode = getMerchantCode();
		if (merchantTransactionCode != null && isVoidPossible(transaction))
		{
			final VoidRequest voidRequest = new VoidRequest(merchantTransactionCode, transaction.getRequestId(), StringUtils.EMPTY,
					StringUtils.EMPTY);
			final BrainTreeVoidResult voidResult = getBrainTreePaymentService().voidTransaction(voidRequest);
			if (TransactionStatus.ACCEPTED.equals(voidResult.getTransactionStatus()))
			{
				getBrainTreeTransactionService().createCancelTransaction(transaction, voidResult);
			}
			if (!voidResult.isSuccess())
			{
				final AbstractOrderModel order = transaction.getOrder();
				throw new OrderCancelException(order.getCode(), voidResult.getErrorMessage());
			}
		}
	}

	protected boolean isVoidPossible(final PaymentTransactionModel transaction)
	{
		final BraintreeTransactionEntryData brainTreeTransaction = findBrainTreeTransaction(transaction.getRequestId());
		if (brainTreeTransaction == null)
		{
			return false;
		}

		final String status = brainTreeTransaction.getStatus();
		if (Transaction.Status.VOIDED.toString().equals(status))
		{
			final List<PaymentTransactionEntryModel> entries = transaction.getEntries();
			if (!isCancelTransactionPresentInOrder(entries))
			{
				getBrainTreeTransactionService().createCancelTransaction(transaction, brainTreeTransaction);
				return false;
			}
		}
		return true;
	}

	private boolean isCancelTransactionPresentInOrder(final List<PaymentTransactionEntryModel> entries)
	{
		for (final PaymentTransactionEntryModel transactionEntry : entries)
		{
			if (PaymentTransactionType.CANCEL.equals(transactionEntry.getType()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public BrainTreeResponseResultData voidTransaction(final BrainTreeTransactionDetailModel transaction)
	{
		validateParameterNotNullStandardMessage("transaction", transaction);
		final String merchantTransactionCode = getMerchantCode();
		if (merchantTransactionCode != null)
		{
			final VoidRequest voidRequest = new VoidRequest(merchantTransactionCode, transaction.getId(), StringUtils.EMPTY,
					StringUtils.EMPTY);
			final BrainTreeVoidResult voidResult = getBrainTreePaymentService().voidTransaction(voidRequest);
			if (TransactionStatus.ACCEPTED.equals(voidResult.getTransactionStatus()))
			{
				forceOrderCancellation(transaction, voidResult);
			}
			return getBraintreeResponseConverter().convert(voidResult);
		}
		return new BrainTreeResponseResultData();
	}

    @Override
    public BrainTreeResponseResultData voidTransaction(final PaymentTransactionEntryModel transaction) throws BraintreeErrorException {
        validateParameterNotNullStandardMessage("transaction", transaction);
        final String merchantTransactionCode = getMerchantCode();
        if (merchantTransactionCode != null) {
            final VoidRequest voidRequest = new VoidRequest(merchantTransactionCode, transaction.getRequestId(), StringUtils.EMPTY,
                    StringUtils.EMPTY);
            final BrainTreeVoidResult voidResult = getBrainTreePaymentService().voidTransaction(voidRequest);

            if (voidResult.isSuccess()) {
                transaction.setTransactionStatus(Transaction.Status.VOIDED.name());
                getModelService().save(transaction);
            } else {
                throw new BraintreeErrorException(voidResult.getErrorMessage(), voidResult.getTransactionId());
            }

            return getBraintreeResponseConverter().convert(voidResult);
        }
        return new BrainTreeResponseResultData();
    }

	private void forceOrderCancellation(final BrainTreeTransactionDetailModel transaction, final BrainTreeVoidResult voidResult)
	{
		final OrderModel linkedOrder = transaction.getLinkedOrder();
		if (linkedOrder != null)
		{
			final List<PaymentTransactionModel> paymentTransactions = linkedOrder.getPaymentTransactions();
			if (CollectionUtils.isNotEmpty(paymentTransactions))
			{
				final PaymentTransactionModel txn = paymentTransactions.iterator().next();
				getBrainTreeTransactionService().createCancelTransaction(txn, voidResult);
			}
			linkedOrder.setStatus(OrderStatus.CANCELLED);
			getModelService().save(linkedOrder);
		}
	}

	@Override
	public BrainTreeResponseResultData cloneTransaction(final BrainTreeTransactionDetailModel currentTransaction,
			final String amount, final boolean submitForSettlement)
	{
		validateParameterNotNullStandardMessage("currentTransaction", currentTransaction);
		final String merchantTransactionCode = getMerchantCode();
		if (merchantTransactionCode != null)
		{
			final BrainTreeCloneTransactionRequest request = new BrainTreeCloneTransactionRequest(merchantTransactionCode);
			request.setTransactionId(currentTransaction.getId());
			if (StringUtils.isNotBlank(amount))
			{
				request.setAmount(formedAmount(amount));
			}
			request.setSubmitForSettlement(Boolean.valueOf(submitForSettlement));
			final BrainTreeCloneTransactionResult result = getBrainTreePaymentService().cloneTransaction(request);
			return getBraintreeResponseConverter().convert(result);
		}
		return new BrainTreeResponseResultData();
	}

	@Override
	public BrainTreeResponseResultData refundTransaction(final BrainTreeTransactionDetailModel currentTransaction,
			final String amount)
	{
		validateParameterNotNullStandardMessage("currentTransaction", currentTransaction);
		final String merchantTransactionCode = getMerchantCode();
		if (merchantTransactionCode != null)
		{
			final BrainTreeRefundTransactionRequest request = new BrainTreeRefundTransactionRequest(merchantTransactionCode);
			request.setTransactionId(currentTransaction.getId());
			if (StringUtils.isNotBlank(amount))
			{
				request.setAmount(formedAmount(amount));
			}
			final BrainTreeRefundTransactionResult result = getBrainTreePaymentService().refundTransaction(request);
			if (TransactionStatus.ACCEPTED.equals(result.getTransactionStatus()))
			{
				createRefundTransaction(currentTransaction, result);
			}
			return getBraintreeResponseConverter().convert(result);
		}
		return new BrainTreeResponseResultData();
	}

	private void createRefundTransaction(final BrainTreeTransactionDetailModel currentTransaction,
			final BrainTreeRefundTransactionResult result)
	{
		final OrderModel linkedOrder = currentTransaction.getLinkedOrder();
		if (linkedOrder != null)
		{
			final List<PaymentTransactionModel> paymentTransactions = linkedOrder.getPaymentTransactions();
			if (CollectionUtils.isNotEmpty(paymentTransactions))
			{
				final PaymentTransactionModel transaction = paymentTransactions.iterator().next();
				getBrainTreeTransactionService().createRefundTransaction(transaction, result);
			}
		}
	}

	@Override
	public BrainTreeResponseResultData refundTransaction(final PaymentTransactionModel transaction, final String amount)
	{
		validateParameterNotNullStandardMessage("currentTransaction", transaction);
		final String merchantTransactionCode = getMerchantCode();
		if (merchantTransactionCode != null)
		{
			final BrainTreeRefundTransactionRequest request = new BrainTreeRefundTransactionRequest(merchantTransactionCode);
			request.setTransactionId(transaction.getRequestId());
			if (StringUtils.isNotBlank(amount))
			{
				request.setAmount(formedAmount(amount));
			}
			final BrainTreeRefundTransactionResult result = getBrainTreePaymentService().refundTransaction(request);
			if (result.isSuccess())
			{
				getBrainTreeTransactionService().createRefundTransaction(transaction, result);
			}

			return getBraintreeResponseConverter().convert(result);
		}
		return new BrainTreeResponseResultData();
	}

	@Override
	public BrainTreeResponseResultData partialRefundTransaction(final OrderModel order,
			final PaymentTransactionEntryModel paymentTransactionEntry, final BigDecimal amount) throws BraintreeErrorException
	{
		BrainTreeRefundTransactionResult response = getPartialRefundService().partialRefundTransaction(order,
				paymentTransactionEntry, amount);
		return getBraintreeResponseConverter().convert(response);
	}

	private BigDecimal formedAmount(final String amount)
	{
		return new BigDecimal(amount);
	}

	private String getMerchantCode()
	{
		final UserModel user = getUserService().getCurrentUser();
		return user.getUid();
	}

	@Override
	public BrainTreeUpdateCustomerResult updateCustomer(final String id, final String customerId, final String firstName,
			final String lastName, final String email, final String phone, final String fax, final String website,
			final String company)
	{
		final String merchantTransactionCode = getMerchantCode();
		if (merchantTransactionCode != null)
		{
			final BrainTreeUpdateCustomerRequest request = new BrainTreeUpdateCustomerRequest(merchantTransactionCode);
			request.setId(customerId);
			request.setCompany(company);
			request.setEmail(email);
			request.setFax(fax);
			request.setFirstName(firstName);
			request.setLastName(lastName);
			request.setPhone(phone);
			request.setWebsite(website);

			return getBrainTreePaymentService().updateCustomer(request);
		}
		return null;
	}

	@Override
	public SubscriptionResult createCustomer(BrainTreeInfo brainTreeInfo)
	{
		BillingInfo billingInfo = new BillingInfo();
		billingInfo.setFirstName(brainTreeInfo.getFirstName());
		billingInfo.setLastName(brainTreeInfo.getLastName());
		billingInfo.setEmail(brainTreeInfo.getEmail());
		billingInfo.setPostalCode(brainTreeInfo.getBillingPostCode());
		billingInfo.setStreet1(brainTreeInfo.getBillingAddress());
		return getBrainTreePaymentService().createCustomerSubscription(
				new CreateSubscriptionRequest(null, billingInfo, null, null, null, null, null));
	}

	@Override
	public BrainTreeResponseResultData createTransaction(final BrainTreeInfo brainTreeInfo)

	{
		validateParameterNotNullStandardMessage("brainTreeInfo", brainTreeInfo);
		final BrainTreeResponseResultData brainTreeResponseResultData = new BrainTreeResponseResultData();
		return getBraintreeResponseResultData(brainTreeInfo, brainTreeResponseResultData);
	}

	private BrainTreeResponseResultData getBraintreeResponseResultData(final BrainTreeInfo brainTreeInfo,
			final BrainTreeResponseResultData brainTreeResponseResultData)
	{
		final String merchantTransactionCode = getMerchantCode();
		if (merchantTransactionCode != null)
		{
			final BrainTreeSaleTransactionRequest request = new BrainTreeSaleTransactionRequest(merchantTransactionCode,
					createCard(brainTreeInfo), brainTreeInfo.getCurrency(), createAmount(brainTreeInfo.getAmount()),
					createShippingInfo(brainTreeInfo));

			request.setUsePaymentMethodToken(Boolean.valueOf(brainTreeInfo.isPaymentMethodToken()));
			request.setPaymentMethodToken(brainTreeInfo.getPaymentMethodToken());
			request.setTaxAmount(createAmount(brainTreeInfo.getTax()));
			request.setCustomFields(brainTreeInfo.getCustom());
			request.setCustomerEmail(brainTreeInfo.getEmail());
			request.setCustomerFirstName(brainTreeInfo.getFirstName());
			request.setCustomerLastName(brainTreeInfo.getLastName());
			request.setStoreInVault(brainTreeInfo.getStoreInVault());
			request.setMethodNonce(brainTreeInfo.getNonce());
			request.setCardholderName(brainTreeInfo.getCardHolder());

			request.setMerchantAccountIdForCurrentSite(getMerchantAccountId());
			final BrainTreeSaleTransactionResult result = getBrainTreePaymentService().saleTransaction(request);
			return getBraintreeResponseConverter().convert(result);
		}
		return brainTreeResponseResultData;
	}


	@Override
	public BrainTreeResponseResultData removeCustomer(final String customerId)
	{
		final String merchantId = getMerchantCode();
		if (merchantId != null)
		{
			validateParameterNotNullStandardMessage("customerId", customerId);
			final BrainTreeCustomerRequest brainTreeCustomerRequest = new BrainTreeCustomerRequest(merchantId);
			brainTreeCustomerRequest.setCustomerId(customerId);

			final BrainTreeCustomerResult result = getBrainTreePaymentService().removeCustomer(brainTreeCustomerRequest);
			return getDefaultResponseConverter().convert(result);
		}
		return new BrainTreeResponseResultData();
	}

	@Override
	public BraintreePaymentMethodData updatePaymentMethod(final String originalToken,
			final BraintreePaymentMethodInfo brainTreeInfo)
	{
		final String merchantId = getMerchantCode();
		if (merchantId != null)
		{
			final BrainTreeUpdatePaymentMethodRequest request = new BrainTreeUpdatePaymentMethodRequest(merchantId);

			request.setToken(brainTreeInfo.getPaymentMethodToken());
			request.setCreditCardNumber(brainTreeInfo.getCardNumber());
			request.setCardholderName(brainTreeInfo.getCardHolder());
			request.setCardExpirationDate(brainTreeInfo.getExpirationDate());
			request.setDefault(brainTreeInfo.isDefaultPaymentMethod());
			request.setNewToken(brainTreeInfo.getNewPaymentMethodToken());
			request.setBillingAddressId(brainTreeInfo.getBillingAddress());
			if (StringUtils.isNotBlank(brainTreeInfo.getCvv()))
			{
				request.setCvv(brainTreeInfo.getCvv());
			}
			final BrainTreeUpdatePaymentMethodResult result = getBrainTreePaymentService().updatePaymentMethod(request);
			if (result.isSuccess() && result.getPaymentMethod() != null)
			{
				final BrainTreePaymentInfoModel paymentInfo = getPaymentMethodConverter().convert(result.getPaymentMethod());
				if (paymentInfo != null)
				{
					getPaymentInfoService().update(originalToken, paymentInfo);
				}
			}

			return getPaymentMethodResponseConverter().convert(result);
		}
		return new BraintreePaymentMethodData();
	}

	@Override
	public BrainTreeResponseResultData submitForSettlementTransaction(final BrainTreeTransactionDetailModel transactionId,
			final String amount)
	{
		final String merchantTransactionCode = getMerchantCode();
		if (merchantTransactionCode != null)
		{
			final BrainTreeSubmitForSettlementTransactionRequest request = new BrainTreeSubmitForSettlementTransactionRequest(
					merchantTransactionCode);
			request.setTransactionId(transactionId.getId());
			if (StringUtils.isNotBlank(amount))
			{
				request.setAmount(formedAmount(amount));
			}
			final BrainTreeSubmitForSettlementTransactionResult result = getBrainTreePaymentService()
					.submitForSettlementTransaction(request);

			return getBraintreeResponseConverter().convert(result);
		}
		return new BrainTreeResponseResultData();
	}

	@Override
	public String generateClientToken()
	{
		return getBrainTreePaymentService().generateClientToken();
	}

	@Override
	public BraintreeInfo getBraintreeInfo()
	{
		final BraintreeInfo braintreeInfo = getSessionService().getAttribute(
				BraintreecscockpitConstants.BRAINTREE_PAYMENT_METHOD_NONCE_SESSION_ATTRIBUTE);

		if (braintreeInfo == null)
		{
			throw new IllegalStateException("Braintree information: nonce, device data etc, should be provided");
		}

		return braintreeInfo;
	}

	@Override
	public void saveBraintreeInfo(final BraintreeInfo braintreeInfo)
	{
		getSessionService().setAttribute(BraintreecscockpitConstants.BRAINTREE_PAYMENT_METHOD_NONCE_SESSION_ATTRIBUTE,
				braintreeInfo);
	}

	@Override
	public void removeBraintreeInfo()
	{
		getSessionService().removeAttribute(BraintreecscockpitConstants.BRAINTREE_PAYMENT_METHOD_NONCE_SESSION_ATTRIBUTE);
	}


	private String getMerchantAccountId()
	{
		final String merchantAccountId = getBrainTreeConfigService().getMerchantAccountIdForCurrentSiteAndCurrency();
		if (StringUtils.isNotEmpty(merchantAccountId))
		{
			final BrainTreeFindMerchantAccountRequest brainTreeFindMerchantAccountRequest = new BrainTreeFindMerchantAccountRequest(
					StringUtils.EMPTY);
			brainTreeFindMerchantAccountRequest.setMerchantAccount(merchantAccountId);
			final boolean isMerchantAccountExist = getBrainTreePaymentService().findMerchantAccount(
					brainTreeFindMerchantAccountRequest).isMerchantAccountExist();
			if (isMerchantAccountExist)
			{
				return merchantAccountId;
			}
		}
		return null;
	}

	//TODO refactoring
	private BigDecimal createAmount(final String amount)
	{
		if (StringUtils.isNotBlank(amount))
		{
			return new BigDecimal(amount);
		}
		return null;
	}

	//TODO refactoring
	private CardInfo createCard(final BrainTreeInfo transactionInfo)

	{
		final CardInfo cardInfo = new CardInfo();
		cardInfo.setCardHolderFullName(transactionInfo.getCardHolder());
		cardInfo.setCardNumber(transactionInfo.getCardNumber());
		cardInfo.setCv2Number(transactionInfo.getCvv());
		if (transactionInfo.getExpirationDate() != null)
		{
			final String[] mmyy = transactionInfo.getExpirationDate().split("/");
			if (mmyy.length == 2)
			{
				cardInfo.setExpirationMonth(Integer.valueOf(mmyy[0]));
				cardInfo.setExpirationYear(Integer.valueOf(mmyy[1]));
			}
		}
		final BillingInfo billingInfo = new BillingInfo();
		billingInfo.setFirstName(transactionInfo.getFirstName());
		billingInfo.setLastName(transactionInfo.getLastName());
		billingInfo.setEmail(transactionInfo.getEmail());
		billingInfo.setPostalCode(transactionInfo.getBillingPostCode());
		billingInfo.setStreet1(transactionInfo.getBillingAddress());
		cardInfo.setBillingInfo(billingInfo);
		return cardInfo;
	}

	//TODO refactoring
	private BillingInfo createShippingInfo(final BrainTreeInfo transactionInfo)
	{
		final BillingInfo shippingInfo = new BillingInfo();
		shippingInfo.setFirstName(transactionInfo.getFirstName());
		shippingInfo.setLastName(transactionInfo.getLastName());
		shippingInfo.setEmail(transactionInfo.getEmail());
		shippingInfo.setPostalCode(transactionInfo.getShippingPostCode());
		shippingInfo.setStreet1(transactionInfo.getShippingAddress());
		return shippingInfo;
	}

	@Override
	public List<Customer> findCustomers(final String customer, final String customerEmail) throws AdapterException
	{
		final UserModel user = getUserService().getCurrentUser();
		final String braintreeCustomerId = user.getUid();
		if (braintreeCustomerId != null)
		{
			final BrainTreeCustomerRequest findCustomerRequest = new BrainTreeCustomerRequest(braintreeCustomerId);
			findCustomerRequest.setCustomerEmail(customerEmail);
			findCustomerRequest.setCustomerId(customer);

			final BrainTreeFindCustomersResult customers = getCustomerSearchService().findCustomers(findCustomerRequest);
			if (customers.getCustomers().getMaximumSize() > CUSTOMER_SEARCH_LIMIT)
			{
				LOG.error("[BT Customer search]Too many results(" +
						customers.getCustomers().getMaximumSize() +
						")! Limit is " + CUSTOMER_SEARCH_LIMIT +
						" Please type customer id or customer email.");
				try {
					Messagebox.show("Too many search results (" + customers.getCustomers().getMaximumSize() +
							")! Limit is " + CUSTOMER_SEARCH_LIMIT + ". Please enter at least customer id or customer email.",
							"Customer search", Messagebox.OK, Messagebox.ERROR);
				} catch (InterruptedException e) {
					LOG.error(e.getMessage());
				}
				return Collections.emptyList();
			}

            return StreamSupport.stream(customers.getCustomers().spliterator(), false)
                    .collect(Collectors.toList());
		}
		LOG.error("[BT Payment Service] Error user must be Customer type!");
		return Collections.emptyList();
	}

	@Override
	public Customer findCustomer(final String customerId) throws AdapterException
	{
		final UserModel user = getUserService().getCurrentUser();
		final String braintreeCustomerId = user.getUid();
		if (braintreeCustomerId != null)
		{
			final BrainTreeCustomerRequest findCustomerRequest = new BrainTreeCustomerRequest(braintreeCustomerId);
			findCustomerRequest.setCustomerId(customerId);

			final BrainTreeFindCustomersResult customers = getCustomerSearchService().findCustomers(findCustomerRequest);
			if (customers.getCustomers() != null)
			{
				if (customers.getCustomers().iterator().hasNext())
				{
					return customers.getCustomers().getFirst();
				}
			}
		}
		LOG.error("[BT Payment Service] Error user must be Customer type!");
		return null;
	}

	@Override
	public BrainTreePaymentMethodResult createCreditCardPaymentMethod(final String customerId, final String paymentMethodNonce,
			final String cardholderName, final boolean isDefault, final String billingAddressId)
	{
		final UserModel user = getUserService().getCurrentUser();
		final String braintreeCustomerId = user.getUid();
		if (braintreeCustomerId != null)
		{
			final BrainTreeCreateCreditCardPaymentMethodRequest request = new BrainTreeCreateCreditCardPaymentMethodRequest(
					braintreeCustomerId);
			final PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest();

			paymentMethodRequest.paymentMethodNonce(paymentMethodNonce).customerId(customerId).cardholderName(cardholderName);
			paymentMethodRequest.options().makeDefault(Boolean.valueOf(isDefault)).done();

			if (StringUtils.isNotBlank(billingAddressId))
			{
				paymentMethodRequest.billingAddressId(billingAddressId);
			}
			request.setRequest(paymentMethodRequest);
			final BrainTreePaymentMethodResult result = getBrainTreePaymentService().createCreditCardPaymentMethod(request);
			if (result.isSuccess() && result.getPaymentMethod() != null)
			{
				final BrainTreePaymentInfoModel paymentInfo = getPaymentMethodConverter().convert(result.getPaymentMethod());
				if (paymentInfo != null)
				{
					paymentInfo.setCardholderName(result.getCardholderName());
					getPaymentInfoService().addToCustomer(paymentInfo);
				}
			}
			return result;
		}
		return null;
	}


	@Override
	public BrainTreePaymentMethodResult deletePaymentMethod(final BrainTreePaymentInfoModel paymentInfo)
	{
		final UserModel user = getUserService().getCurrentUser();
		final String braintreeCustomerId = user.getUid();
		if (braintreeCustomerId != null)
		{
			final BrainTreeDeletePaymentMethodRequest request = new BrainTreeDeletePaymentMethodRequest(braintreeCustomerId,
					paymentInfo.getPaymentMethodToken());
			final BrainTreePaymentMethodResult result = getBrainTreePaymentService().deletePaymentMethod(request);
			if (result.isSuccess())
			{
				getPaymentInfoService().remove(paymentInfo.getCustomerId(), paymentInfo.getPaymentMethodToken());
			}
			return result;
		}
		return null;
	}

	@Override
	public BrainTreeAddressResult addAddress(final String customerId, final String firstName, final String lastName,
			final String company, final String streetAddress, final String extendedAddress, final String cityLocality,
			final String stateProvinceRegion, final String postalCode, final String countryIsoCode)
	{
		return getBraintreeAddressService().addAddress(customerId, firstName, lastName, company, streetAddress, extendedAddress,
				cityLocality, stateProvinceRegion, postalCode, countryIsoCode);
	}

	@Override
	public BrainTreeAddressResult updateAddress(final String customerId, final String addressId, final String firstName,
			final String lastName, final String company, final String streetAddress, final String extendedAddress,
			final String cityLocality, final String stateProvinceRegion, final String postalCode, final String countryName)
	{
		return getBraintreeAddressService().updateAddress(customerId, addressId, firstName, lastName, company, streetAddress,
				extendedAddress, cityLocality, stateProvinceRegion, postalCode, countryName);
	}

	@Override
	public BrainTreeAddressResult removeAddress(final String addressID, final String customerId)
	{
		return getBraintreeAddressService().removeAddress(addressID, customerId);
	}

	@Override
	public boolean createPaymentInfo(final CartModel cart, final CardInfo cardInfo, final double amount) throws PaymentException,
			ValidationException
	{
		try
		{
			final BraintreeInfo braintreeInfo = getBraintreeInfo();
			braintreeInfo.setSavePaymentInfo(true);
			braintreeInfo.setPaymentProvider(BraintreeConstants.BRAINTREE_PAYMENT);
			getBraintreeCsCheckoutService().createPaymentInfo(cart, cardInfo, amount, braintreeInfo);
		}
		catch (final IllegalStateException e)
		{
			throw new PaymentException("Can't create payment method", e);
		}


		return true;
	}

	@Override
	public void authorizePayment(final AbstractOrderModel cart) throws PaymentException, ValidationException
	{
		authorizePayment(cart, customFieldsService.getDefaultCustomFieldsMap(), null);
	}

	@Override
	public PaymentTransactionEntryModel authorizePayment(final AbstractOrderModel cart, Map<String, String> customFields, BigDecimal totalAmount)
	{
		return getBrainTreeTransactionService().createAuthorizationTransaction(cart, customFields, totalAmount);
	}

	public BraintreeAddressService getBraintreeAddressService()
	{
		return braintreeAddressService;
	}

	public void setBraintreeAddressService(final BraintreeAddressService braintreeAddressService)
	{
		this.braintreeAddressService = braintreeAddressService;
	}

	public TransactionSearchService getTransactionSearchService()
	{
		return transactionSearchService;
	}

	public void setTransactionSearchService(final TransactionSearchService transactionSearchService)
	{
		this.transactionSearchService = transactionSearchService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public BraintreeTransactionConverter getBrainTreeTransactionConverter()
	{
		return brainTreeTransactionConverter;
	}

	public void setBrainTreeTransactionConverter(final BraintreeTransactionConverter brainTreeTransactionConverter)
	{
		this.brainTreeTransactionConverter = brainTreeTransactionConverter;
	}

	public BraintreeResponseConverter getBraintreeResponseConverter()
	{
		return braintreeResponseConverter;
	}

	public void setBraintreeResponseConverter(final BraintreeResponseConverter braintreeResponseConverter)
	{
		this.braintreeResponseConverter = braintreeResponseConverter;
	}

	public BraintreeDefaultResponseConverter getDefaultResponseConverter()
	{
		return defaultResponseConverter;
	}

	public void setDefaultResponseConverter(final BraintreeDefaultResponseConverter defaultResponseConverter)
	{
		this.defaultResponseConverter = defaultResponseConverter;
	}

	public BraintreeCustomerDetailsConverter getBraintreeCustomerDetailsConverter()
	{
		return braintreeCustomerDetailsConverter;
	}

	public void setBraintreeCustomerDetailsConverter(final BraintreeCustomerDetailsConverter braintreeCustomerDetailsConverter)
	{
		this.braintreeCustomerDetailsConverter = braintreeCustomerDetailsConverter;
	}

	public BraintreePaymentMethodResponseConverter getPaymentMethodResponseConverter()
	{
		return paymentMethodResponseConverter;
	}

	public void setPaymentMethodResponseConverter(final BraintreePaymentMethodResponseConverter paymentMethodResponseConverter)
	{
		this.paymentMethodResponseConverter = paymentMethodResponseConverter;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public BrainTreeTransactionService getBrainTreeTransactionService()
	{
		return brainTreeTransactionService;
	}

	public void setBrainTreeTransactionService(final BrainTreeTransactionService brainTreeTransactionService)
	{
		this.brainTreeTransactionService = brainTreeTransactionService;
	}

	public EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	public BrainTreePaymentService getBrainTreePaymentService()
	{
		return brainTreePaymentService;
	}

	public void setBrainTreePaymentService(final BrainTreePaymentService brainTreePaymentService)
	{
		this.brainTreePaymentService = brainTreePaymentService;
	}

	public CustomerSearchService getCustomerSearchService()
	{
		return customerSearchService;
	}

	public void setCustomerSearchService(final CustomerSearchService customerSearchService)
	{
		this.customerSearchService = customerSearchService;
	}

	public BrainTreeConfigService getBrainTreeConfigService()
	{
		return brainTreeConfigService;
	}

	public void setBrainTreeConfigService(final BrainTreeConfigService brainTreeConfigService)
	{
		this.brainTreeConfigService = brainTreeConfigService;
	}

	public PaymentInfoService getPaymentInfoService()
	{
		return paymentInfoService;
	}

	public void setPaymentInfoService(final PaymentInfoService paymentInfoService)
	{
		this.paymentInfoService = paymentInfoService;
	}

	public BraintreePaymentMethodConverter getPaymentMethodConverter()
	{
		return paymentMethodConverter;
	}

	public void setPaymentMethodConverter(final BraintreePaymentMethodConverter paymentMethodConverter)
	{
		this.paymentMethodConverter = paymentMethodConverter;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the braintreeCsCheckoutService
	 */
	public BraintreeCsCheckoutService getBraintreeCsCheckoutService()
	{
		return braintreeCsCheckoutService;
	}

	/**
	 * @param braintreeCsCheckoutService
	 *           the braintreeCsCheckoutService to set
	 */
	public void setBraintreeCsCheckoutService(final BraintreeCsCheckoutService braintreeCsCheckoutService)
	{
		this.braintreeCsCheckoutService = braintreeCsCheckoutService;
	}

	public BraintreePartialRefundService getPartialRefundService()
	{
		return partialRefundService;
	}

	public void setPartialRefundService(BraintreePartialRefundService partialRefundService)
	{
		this.partialRefundService = partialRefundService;
	}

	public CustomFieldsService getCustomFieldsService() {
		return customFieldsService;
	}

	public void setCustomFieldsService(CustomFieldsService customFieldsService) {
		this.customFieldsService = customFieldsService;
	}
}
