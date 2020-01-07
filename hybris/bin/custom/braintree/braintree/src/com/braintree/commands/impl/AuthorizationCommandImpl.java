package com.braintree.commands.impl;

import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintreegateway.*;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.AuthorizationCommand;
import de.hybris.platform.payment.commands.request.AuthorizationRequest;
import de.hybris.platform.payment.commands.result.AuthorizationResult;
import de.hybris.platform.payment.dto.AvsStatus;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CvnStatus;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;

import java.util.Currency;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.braintree.command.request.BrainTreeAuthorizationRequest;
import com.braintree.command.result.BrainTreeAuthorizationResult;
import com.braintree.constants.BraintreeConstants;


public class AuthorizationCommandImpl extends AbstractCommand<AuthorizationRequest, AuthorizationResult>
		implements AuthorizationCommand
{
    private final static Logger LOG = Logger.getLogger(AuthorizationCommandImpl.class);

	private static final String AUTHORIZATION_TRANSACTION = "[AUTHORIZATION TRANSACTION] ";

	private CartService cartService;

    @Override
    public AuthorizationResult perform(final AuthorizationRequest authorizationRequest) {
        LOG.info("configured intent: " + getBrainTreeConfigService().getIntent());

        final TransactionRequest transactionRequest = translateRequest(authorizationRequest);

        final Result<Transaction> braintreeReply = getBraintreeGateway().transaction().sale(transactionRequest);

        LOG.info("braintreeReply: " + braintreeReply);
        LOG.info("braintreeReply.message: " + braintreeReply.getMessage());
        LOG.info("braintreeReply.errors: " + braintreeReply.getErrors());
        LOG.info("braintreeReply.parameters: " + braintreeReply.getParameters());

        final AuthorizationResult authorizationResult = translateResponse(braintreeReply);
        return authorizationResult;
    }

	private TransactionRequest translateRequest(final AuthorizationRequest authorizationRequest)
	{
		TransactionRequest request = null;

		if (authorizationRequest instanceof BrainTreeAuthorizationRequest)
		{
			final BrainTreeAuthorizationRequest brainTreeAuthorizationRequest = (BrainTreeAuthorizationRequest) authorizationRequest;

			request = new TransactionRequest().customerId(brainTreeAuthorizationRequest.getCustomerId())
					.amount(authorizationRequest.getTotalAmount());

			setAdditionalParameters(brainTreeAuthorizationRequest, request);

			getLoggingHandler().handleAuthorizationRequest(brainTreeAuthorizationRequest);

		}
		else
		{
			final String errorMessage = "[BT Authorization Error] Authorization Request must be Brain Tree type!";
			getLoggingHandler().getLogger().error(errorMessage);
			throw new AdapterException(errorMessage);
		}
		return request;
	}

	private void setAdditionalParameters(final BrainTreeAuthorizationRequest brainTreeAuthorizationRequest,
			final TransactionRequest request)
	{
		TransactionOptionsRequest options = request.options();
		if (BooleanUtils.isTrue(brainTreeAuthorizationRequest.getUsePaymentMethodToken()))
		{
			if (brainTreeAuthorizationRequest.getPaymentMethodToken() == null)
			{
				getLoggingHandler().getLogger().error("Error: PaymentMethodToken is null!");
				throw new IllegalArgumentException("Error during using existing payment.");
			}
			request.paymentMethodToken(brainTreeAuthorizationRequest.getPaymentMethodToken());
		}
		else
		{
			request.paymentMethodNonce(brainTreeAuthorizationRequest.getMethodNonce());
		}

		Boolean submitForSettlement = Boolean.FALSE;

		if (isAvailableSubmitForSettlement(brainTreeAuthorizationRequest) || isAvailableSubmitForSettlementForApplePay(brainTreeAuthorizationRequest))
		{
			submitForSettlement = Boolean.TRUE;
		}

			if (brainTreeAuthorizationRequest.getUsePaymentMethodToken())
		{
			options.storeInVault(Boolean.FALSE);
		}
		else if (BraintreeConstants.ON_SUCCESS.equalsIgnoreCase(getBrainTreeConfigService().getStoreInVaultForCurrentUser()))
		{
			options.storeInVaultOnSuccess(Boolean.TRUE);
		}
		else
		{
			options.storeInVault(Boolean.valueOf(getBrainTreeConfigService().getStoreInVaultForCurrentUser()));
		}

		options.submitForSettlement(submitForSettlement);


		if ((BraintreeConstants.PAY_PAL_EXPRESS_CHECKOUT.equals(brainTreeAuthorizationRequest.getPaymentType())
				|| BraintreeConstants.PAYPAL_PAYMENT.equals(brainTreeAuthorizationRequest.getPaymentType()))
				&& Boolean.FALSE.equals(Boolean.valueOf(brainTreeAuthorizationRequest.isStoreInVault()))
				&& Boolean.TRUE.equals(brainTreeAuthorizationRequest.getAdvancedFraudTools()))
		{
			request.deviceData(brainTreeAuthorizationRequest.getDeviceData());
		}
		if (BraintreeConstants.BRAINTREE_PAYMENT.equals(brainTreeAuthorizationRequest.getPaymentType()))
		{
			if (BooleanUtils.isTrue(brainTreeAuthorizationRequest.getThreeDSecureConfiguration()))
			{
				boolean threeDSecureRequired = true;
				if (BooleanUtils.isTrue(brainTreeAuthorizationRequest.getIsSkip3dSecureLiabilityResult())
						&& BooleanUtils.isFalse(brainTreeAuthorizationRequest.getLiabilityShifted()))
				{
					threeDSecureRequired = false;
				}
				options.threeDSecure().required(threeDSecureRequired);
			}
			if (Boolean.TRUE.equals(brainTreeAuthorizationRequest.getAdvancedFraudTools()))
			{
				request.deviceData(brainTreeAuthorizationRequest.getDeviceData());
			}
		}

		if ((StringUtils.isNotBlank(brainTreeAuthorizationRequest.getCreditCardStatementName())))
		{
			request.descriptor().name(brainTreeAuthorizationRequest.getCreditCardStatementName()).done();
		}

		final BillingInfo shippingInfo = brainTreeAuthorizationRequest.getShippingInfo();
		final BillingInfo billingInfo = brainTreeAuthorizationRequest.getBillingInfo();

		if (StringUtils.isNotEmpty(brainTreeAuthorizationRequest.getBrainTreeBilligAddressId()))
		{
			request.billingAddressId(brainTreeAuthorizationRequest.getBrainTreeBilligAddressId());
		}
		else
		{
			request.billingAddress().countryCodeAlpha2(billingInfo.getCountry()).region(billingInfo.getState())
					.firstName(billingInfo.getFirstName()).lastName(billingInfo.getLastName()).streetAddress(billingInfo.getStreet1())
					.extendedAddress(billingInfo.getStreet2()).locality(billingInfo.getCity()).postalCode(billingInfo.getPostalCode());
		}

		if (StringUtils.isNotEmpty(brainTreeAuthorizationRequest.getBrainTreeAddressId()))
		{
			request.shippingAddressId(brainTreeAuthorizationRequest.getBrainTreeAddressId());
		}
		else
		{
			request.shippingAddress().countryCodeAlpha2(shippingInfo.getCountry()).region(shippingInfo.getState())
					.firstName(shippingInfo.getFirstName()).lastName(shippingInfo.getLastName())
					.streetAddress(shippingInfo.getStreet1()).extendedAddress(shippingInfo.getStreet2())
					.locality(shippingInfo.getCity()).postalCode(shippingInfo.getPostalCode());
		}

		request.channel(brainTreeAuthorizationRequest.getBrainTreeChannel());
		request.merchantAccountId(brainTreeAuthorizationRequest.getMerchantAccountIdForCurrentSite());

		setCustomFields(brainTreeAuthorizationRequest, request);

		setPayee(options);

	}

	private void setPayee(TransactionOptionsRequest options) {
		String payee = StringUtils.EMPTY;
		if(StringUtils.isEmpty(getBrainTreeConfigService().getIntent()) || isIntentSale()) {
			options.payeeEmail(payee);
		}
	}

	private void setCustomFields(BrainTreeAuthorizationRequest brainTreeAuthorizationRequest, TransactionRequest request)
	{
		if (brainTreeAuthorizationRequest.getCustomFields() != null && !brainTreeAuthorizationRequest.getCustomFields().isEmpty())
		{
			for (String key:
				 brainTreeAuthorizationRequest.getCustomFields().keySet()) {
				request.customField(key, brainTreeAuthorizationRequest.getCustomFields().get(key));
			}
		}
	}

	private boolean isAvailableSubmitForSettlement(final BrainTreeAuthorizationRequest brainTreeAuthorizationRequest)
	{
		return 	!isCreditCard(brainTreeAuthorizationRequest) && !isApplePay(brainTreeAuthorizationRequest)
				&& isIntentSale();
	}

	private boolean isIntentSale() {
		return BraintreeConstants.PAYPAL_INTENT_SALE.equalsIgnoreCase(getBrainTreeConfigService().getIntent());
	}

	private boolean isApplePay(BrainTreeAuthorizationRequest brainTreeAuthorizationRequest) {
		return BraintreeConstants.APPLE_PAY_PAYMENT.equalsIgnoreCase(brainTreeAuthorizationRequest.getPaymentType());
	}

	private boolean isCreditCard(BrainTreeAuthorizationRequest brainTreeAuthorizationRequest) {
		return BraintreeConstants.BRAINTREE_PAYMENT.equalsIgnoreCase(brainTreeAuthorizationRequest.getPaymentType());
	}

	private boolean isAvailableSubmitForSettlementForApplePay(BrainTreeAuthorizationRequest brainTreeAuthorizationRequest)
	{
		return isApplePay(brainTreeAuthorizationRequest) && getBrainTreeConfigService().getSettlementConfigParameter();
	}

	private AuthorizationResult translateResponse(final Result<Transaction> braintreeReply)
	{
		final BrainTreeAuthorizationResult result = new BrainTreeAuthorizationResult();
		result.setTransactionStatus(TransactionStatus.REJECTED);
		if (braintreeReply != null)
		{
			result.setSuccess(braintreeReply.isSuccess());

			final Transaction transaction = braintreeReply.getTarget();
			List<ValidationError> errors = null;

			if (braintreeReply.isSuccess())
			{
				if (transaction != null)
				{
					result.setAuthorizationCode(transaction.getProcessorAuthorizationCode());
					result.setAvsStatus(AvsStatus.MATCHED);
					result.setCvnStatus(CvnStatus.MATCHED);

					if (transaction.getAmount() != null)
					{
						result.setTotalAmount(transaction.getAmount());
					}

					result.setAuthorizationTime(transaction.getCreatedAt().getTime());
					result.setCurrency(Currency.getInstance(transaction.getCurrencyIsoCode()));
					result.setMerchantTransactionCode(transaction.getMerchantAccountId());
					result.setRequestId(transaction.getId());
					result.setRequestToken(transaction.getId());
					result.setTransactionStatus(TransactionStatus.ACCEPTED);
					result.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL);
				}
				else
				{
					result.setTransactionStatusDetails(TransactionStatusDetails.BANK_DECLINE);
				}
			}
			else if (braintreeReply.getErrors() != null)
			{
				final StringBuilder errorMessage = new StringBuilder("[ERROR AUTHORIZATION] ");
				final StringBuilder errorMessageReason = new StringBuilder();
				if (braintreeReply.getErrors().getAllDeepValidationErrors() != null
						&& braintreeReply.getErrors().getAllDeepValidationErrors().size() > 0)
				{
					result.setTransactionStatusDetails(getCodeTranslator()
							.translateReasonCode(braintreeReply.getErrors().getAllDeepValidationErrors().get(0).getCode().code));

					errors = braintreeReply.getErrors().getAllDeepValidationErrors();
					errorMessage.append(getLoggingHandler().handleErrors(errors));
					errorMessageReason
							.append(getErrorTranslator().getMessage(braintreeReply.getErrors().getAllDeepValidationErrors().get(0)));
				}
				if (result.getTransactionStatusDetails() == null)
				{
					result.setTransactionStatusDetails(TransactionStatusDetails.NO_AUTHORIZATION_FOR_SETTLEMENT);
					errorMessage.append(braintreeReply.getMessage());
//					errorMessageReason.append("(");
					errorMessageReason.append(braintreeReply.getMessage());
//					errorMessageReason.append(")");
				}
				throw new AdapterException(errorMessageReason.toString());
			}

			getLoggingHandler().handleResult(AUTHORIZATION_TRANSACTION, transaction);
		}

		return result;
	}

	public CartService getCartService() {
		return cartService;
	}

	public void setCartService(CartService cartService) {
		this.cartService = cartService;
	}
}
