/**
 *
 */
package com.braintree.command.request;

import de.hybris.platform.payment.commands.request.AuthorizationRequest;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;


public class BrainTreeAuthorizationRequest extends AuthorizationRequest
{

	public BrainTreeAuthorizationRequest(final String merchantTransactionCode, final CardInfo card, final Currency currency,
			final BigDecimal totalAmount, final BillingInfo shippingInfo)
	{
		super(merchantTransactionCode, card, currency, totalAmount, shippingInfo);
	}

	private String deviceData;
	private String methodNonce;
	private String customerId;
	private String paymentType;
	private Boolean liabilityShifted;
	private String paymentMethodToken;
	private Boolean usePaymentMethodToken;
	private Boolean threeDSecureConfiguration;
	private Boolean advancedFraudTools;
	private Boolean isSkip3dSecureLiabilityResult;
	private String creditCardStatementName;
	private String merchantAccountIdForCurrentSite;
	private String brainTreeChannel;
	private boolean storeInVault;
	private String brainTreeAddressId;
	private String cardholderName;
	private BillingInfo billingInfo;
	private String brainTreeBilligAddressId;
	private Map<String, String> customFields;

	public Map<String, String> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(Map<String, String> customFields) {
		this.customFields = customFields;
	}

	public String getBrainTreeAddressId()
	{
		return brainTreeAddressId;
	}

	public void setBrainTreeAddressId(final String brainTreeAddressId)
	{
		this.brainTreeAddressId = brainTreeAddressId;
	}

	public boolean isStoreInVault()
	{
		return storeInVault;
	}

	public void setStoreInVault(final boolean storeInVault)
	{
		this.storeInVault = storeInVault;
	}

	public String getDeviceData()
	{
		return deviceData;
	}

	public void setLiabilityShifted(final Boolean liabilityShifted)
	{
		this.liabilityShifted = liabilityShifted;
	}

	public void setPaymentMethodToken(final String paymentMethodToken)
	{
		this.paymentMethodToken = paymentMethodToken;
	}

	public void setUsePaymentMethodToken(final Boolean usePaymentMethodToken)
	{
		this.usePaymentMethodToken = usePaymentMethodToken;
	}

	public void setThreeDSecureConfiguration(final Boolean threeDSecureConfiguration)
	{
		this.threeDSecureConfiguration = threeDSecureConfiguration;
	}

	public void setAdvancedFraudTools(final Boolean advancedFraudTools)
	{
		this.advancedFraudTools = advancedFraudTools;
	}

	public void setIsSkip3dSecureLiabilityResult(final Boolean isSkip3dSecureLiabilityResult)
	{
		this.isSkip3dSecureLiabilityResult = isSkip3dSecureLiabilityResult;
	}

	public void setDeviceData(final String deviceData)
	{
		this.deviceData = deviceData;
	}

	public String getMethodNonce()
	{
		return methodNonce;
	}

	public void setMethodNonce(final String methodNonce)
	{
		this.methodNonce = methodNonce;
	}

	public String getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(final String customerId)
	{
		this.customerId = customerId;
	}

	public String getPaymentType()
	{
		return paymentType;
	}

	public void setPaymentType(final String paymentType)
	{
		this.paymentType = paymentType;
	}

	public String getCreditCardStatementName()
	{
		return creditCardStatementName;
	}

	public void setCreditCardStatementName(final String creditCardStatementName)
	{
		this.creditCardStatementName = creditCardStatementName;
	}

	public String getMerchantAccountIdForCurrentSite()
	{
		return merchantAccountIdForCurrentSite;
	}

	public void setMerchantAccountIdForCurrentSite(final String merchantAccountIdForCurrentSite)
	{
		this.merchantAccountIdForCurrentSite = merchantAccountIdForCurrentSite;
	}

	public String getBrainTreeChannel()
	{
		return brainTreeChannel;
	}

	public void setBrainTreeChannel(final String brainTreeChannel)
	{
		this.brainTreeChannel = brainTreeChannel;
	}

	public Boolean getLiabilityShifted()
	{
		return liabilityShifted;
	}

	public String getPaymentMethodToken()
	{
		return paymentMethodToken;
	}

	public Boolean getUsePaymentMethodToken()
	{
		return usePaymentMethodToken;
	}

	public Boolean getThreeDSecureConfiguration()
	{
		return threeDSecureConfiguration;
	}

	public Boolean getAdvancedFraudTools()
	{
		return advancedFraudTools;
	}

	public Boolean getIsSkip3dSecureLiabilityResult()
	{
		return isSkip3dSecureLiabilityResult;
	}

	public String getCardholderName()
	{
		return cardholderName;
	}

	public void setCardholderName(final String cardholderName)
	{
		this.cardholderName = cardholderName;
	}

	public BillingInfo getBillingInfo()
	{
		return billingInfo;
	}

	public void setBillingInfo(final BillingInfo billingInfo)
	{
		this.billingInfo = billingInfo;
	}

	public String getBrainTreeBilligAddressId()
	{
		return brainTreeBilligAddressId;
	}

	public void setBrainTreeBilligAddressId(final String brainTreeBilligAddressId)
	{
		this.brainTreeBilligAddressId = brainTreeBilligAddressId;
	}

}
