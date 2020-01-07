package com.braintree.cscockpit.data;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import com.braintree.enums.StoreInVault;


public class BrainTreeInfo
{
	private String amount;
	private String tax;
	private final Map<String, String> custom = new HashMap<>();
	private String cardHolder;
	private String cardNumber;
	private String cvv;
	private String expirationDate;
	private String firstName;
	private String lastName;
	private String email;
	private String billingAddress;
	private String billingPostCode;
	private String shippingAddress;
	private String shippingPostCode;
	private Currency currency;
	private boolean isPaymentMethodToken;
	private String paymentMethodToken;
	private String nonce;
	private StoreInVault storeInVault;


	public String getAmount()
	{
		return amount;
	}

	public BrainTreeInfo setAmount(final String amount)
	{
		this.amount = amount;
		return this;
	}

	public String getTax()
	{
		return tax;
	}

	public BrainTreeInfo setTax(final String tax)
	{
		this.tax = tax;
		return this;
	}

	public Map<String, String> getCustom()
	{
		return custom;
	}

	public BrainTreeInfo setCustom(final String key, final String value)
	{
		this.custom.put(key, value);
		return this;
	}

	public String getCardHolder()
	{
		return cardHolder;
	}

	public BrainTreeInfo setCardHolder(final String cardHolder)
	{
		this.cardHolder = cardHolder;
		return this;
	}

	public String getCardNumber()
	{
		return cardNumber;
	}

	public BrainTreeInfo setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
		return this;
	}

	public String getCvv()
	{
		return cvv;
	}

	public BrainTreeInfo setCvv(final String cvv)
	{
		this.cvv = cvv;
		return this;
	}

	public String getExpirationDate()
	{
		return expirationDate;
	}

	public BrainTreeInfo setExpirationDate(final String expirationDate)
	{
		this.expirationDate = expirationDate;
		return this;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public BrainTreeInfo setFirstName(final String firstName)
	{
		this.firstName = firstName;
		return this;
	}

	public String getLastName()
	{
		return lastName;
	}

	public BrainTreeInfo setLastName(final String lastName)
	{
		this.lastName = lastName;
		return this;
	}

	public String getEmail()
	{
		return email;
	}

	public BrainTreeInfo setEmail(final String email)
	{
		this.email = email;
		return this;
	}

	public String getBillingAddress()
	{
		return billingAddress;
	}

	public BrainTreeInfo setBillingAddress(final String billingAddress)
	{
		this.billingAddress = billingAddress;
		return this;
	}

	public String getBillingPostCode()
	{
		return billingPostCode;
	}

	public BrainTreeInfo setBillingPostCode(final String billingPostCode)
	{
		this.billingPostCode = billingPostCode;
		return this;
	}

	public String getShippingAddress()
	{
		return shippingAddress;
	}

	public BrainTreeInfo setShippingAddress(final String shippingAddress)
	{
		this.shippingAddress = shippingAddress;
		return this;
	}

	public String getShippingPostCode()
	{
		return shippingPostCode;
	}

	public BrainTreeInfo setShippingPostCode(final String shippingPostCode)
	{
		this.shippingPostCode = shippingPostCode;
		return this;
	}

	public Currency getCurrency()
	{
		return currency;
	}

	public BrainTreeInfo setCurrency(final String currencyCode)
	{
		this.currency = Currency.getInstance(currencyCode);
		return this;
	}

	public boolean isPaymentMethodToken()
	{
		return isPaymentMethodToken;
	}

	public String getPaymentMethodToken()
	{
		return paymentMethodToken;
	}

	public BrainTreeInfo setPaymentMethodToken(final String paymentMethodToken)
	{
		this.paymentMethodToken = paymentMethodToken;
		isPaymentMethodToken = this.paymentMethodToken != null;
		return this;
	}

	public StoreInVault getStoreInVault()
	{
		return storeInVault;
	}

	public BrainTreeInfo setStoreInVault(final StoreInVault storeInVault)
	{
		this.storeInVault = storeInVault;
		return this;
	}

	public String getNonce()
	{
		return nonce;
	}

	public BrainTreeInfo setNonce(String nonce)
	{
		this.nonce = nonce;
		return this;
	}
}
