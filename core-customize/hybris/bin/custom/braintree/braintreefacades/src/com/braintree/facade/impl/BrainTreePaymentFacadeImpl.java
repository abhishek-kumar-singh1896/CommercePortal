package com.braintree.facade.impl;

import static com.braintree.constants.BraintreeConstants.CARD_NUMBER_MASK;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import com.braintree.command.result.BrainTreeCreatePaymentMethodResult;
import com.braintree.configuration.service.BrainTreeConfigService;
import com.braintree.hybris.data.BrainTreePaymentInfoData;
import com.braintree.hybris.data.BrainTreeSubscriptionInfoData;
import com.braintree.method.BrainTreePaymentService;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.payment.dto.BraintreeInfo;
import com.braintree.paypal.converters.impl.BillingAddressConverter;
import com.braintree.transaction.service.BrainTreeTransactionService;

import de.hybris.platform.acceleratorfacades.payment.impl.DefaultPaymentFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class BrainTreePaymentFacadeImpl extends DefaultPaymentFacade
{
    private final static Logger LOG = Logger.getLogger(BrainTreePaymentFacadeImpl.class);

	private BrainTreePaymentService brainTreePaymentService;
	private CartService cartService;
	private BaseStoreService baseStoreService;
	private CustomerAccountService customerAccountService;
	private CommerceCartService commerceCartService;
	private ModelService modelService;
	private UserService userService;
	private CustomerEmailResolutionService customerEmailResolutionService;
	private BrainTreeTransactionService brainTreeTransactionService;

	private Converter<BrainTreePaymentInfoModel, BrainTreePaymentInfoData> brainTreePaymentInfoDataConverter;
	private Converter<AddressModel, AddressData> addressConverter;
	private Converter<AddressData, AddressModel> addressReverseConverter;
	private BillingAddressConverter billingAddressConverter;
	private Converter<BrainTreeSubscriptionInfoData, BraintreeInfo> brainTreeSubscriptionInfoConverter;
	private BrainTreeConfigService brainTreeConfigService;


	public void completeCreateSubscription(final BrainTreeSubscriptionInfoData subscriptionInfo)
	{
		final CustomerModel customer = getCurrentUserForCheckout();
		final CartModel cart = getCartService().getSessionCart();
		completeCreateSubscription(subscriptionInfo, customer, cart);
	}

	public void completeCreateSubscription(final BrainTreeSubscriptionInfoData brainTreeSubscriptionInfoData,
			final CustomerModel customer, final CartModel cart)
	{
		BrainTreePaymentInfoModel paymentInfo = null;
		final AddressData addressData = brainTreeSubscriptionInfoData.getAddressData();

		if (isNotEmpty(brainTreeSubscriptionInfoData.getCardNumber()))
		{
			brainTreeSubscriptionInfoData
					.setCardNumber(String.format(CARD_NUMBER_MASK, brainTreeSubscriptionInfoData.getCardNumber()));
		}

		final AddressModel billingAddress = resolveBillingAddress(addressData, cart, customer, brainTreeSubscriptionInfoData);

		BraintreeInfo paymentMethodBrainTreeInfo = null;

		final boolean isStoreInVault = !Boolean.FALSE.toString().equals(getBrainTreeConfigService().getStoreInVaultForCurrentUser());
		if (isStoreInVault){
			brainTreeSubscriptionInfoData.setSavePaymentInfo(true);
		}
		if (brainTreeSubscriptionInfoData.isSavePaymentInfo())
		{
            LOG.debug("... inside");
			paymentMethodBrainTreeInfo = getBrainTreeSubscriptionInfoConverter().convert(brainTreeSubscriptionInfoData);
			final BrainTreeCreatePaymentMethodResult result = getBrainTreePaymentService().createPaymentMethodForCustomer(customer,
					billingAddress, paymentMethodBrainTreeInfo);
			LOG.error("result: " + result);

			checkBraintreeResult(result);

			addAdditionalPaymentMethodFields(brainTreeSubscriptionInfoData, result);
		} else {
            LOG.debug("... not inside");
        }

		final BraintreeInfo braintreeInfo = getBrainTreeSubscriptionInfoConverter().convert(brainTreeSubscriptionInfoData);
		if (paymentMethodBrainTreeInfo != null){
			braintreeInfo.setDuplicatedPayment(paymentMethodBrainTreeInfo.isDuplicatedPayment());
		}
		paymentInfo = getBrainTreeTransactionService().createSubscription(billingAddress, customer, braintreeInfo);

		paymentInfo.setUsePaymentMethodToken(Boolean.valueOf(braintreeInfo.isSavePaymentInfo()));
		paymentInfo.setPayer(brainTreeSubscriptionInfoData.getEmail());
		modelService.save(paymentInfo);
		cart.setPaymentInfo(paymentInfo);
		modelService.save(cart);
	}

	private void checkBraintreeResult(final BrainTreeCreatePaymentMethodResult paymentMethodResult) throws AdapterException
	{
		if (!paymentMethodResult.isSuccess())
		{
			throw new AdapterException(paymentMethodResult.getErrorMessage());
		}
	}

	private AddressModel resolveBillingAddress(final AddressData addressBillingData, final CartModel cart,
			final CustomerModel customer, final BrainTreeSubscriptionInfoData brainTreeSubscriptionInfoData)
	{
		AddressModel billingAddress = getModelService().create(AddressModel.class);

		if (addressBillingData != null)
		{
			billingAddress = buildBillingAddress(addressBillingData, customer);
		}
		else
		{
			// double convert instead of cloning
			final AddressData deliveryAddress = getAddressConverter().convert(cart.getDeliveryAddress());
			getAddressReverseConverter().convert(deliveryAddress, billingAddress);

			billingAddress.setBrainTreeAddressId(cart.getDeliveryAddress().getBrainTreeAddressId());
			if ( billingAddress.getEmail() == null && brainTreeSubscriptionInfoData.getEmail() != null){
				billingAddress.setEmail(brainTreeSubscriptionInfoData.getEmail());
			}
		}

		return billingAddress;
	}

	private AddressModel buildBillingAddress(final AddressData addressData, final CustomerModel customer)
	{
		final AddressModel billingAddress = getModelService().create(AddressModel.class);
		getAddressReverseConverter().convert(addressData, billingAddress);

		billingAddress.setBrainTreeAddressId(addressData.getBrainTreeAddressId());

		if (isNotEmpty(addressData.getEmail()))
		{
			billingAddress.setEmail(addressData.getEmail());
		}
		else
		{
			billingAddress.setEmail(customerEmailResolutionService.getEmailForCustomer(customer));
		}

		return billingAddress;
	}

	private void addAdditionalPaymentMethodFields(final BrainTreeSubscriptionInfoData brainTreeSubscriptionInfoData,
			final BrainTreeCreatePaymentMethodResult createPaymentMethodResult)
	{
		if (createPaymentMethodResult != null)
		{
			brainTreeSubscriptionInfoData.setPaymentMethodToken(createPaymentMethodResult.getPaymentMethodToken());
			brainTreeSubscriptionInfoData.setExpirationMonth(createPaymentMethodResult.getExpirationMonth());
			brainTreeSubscriptionInfoData.setExpirationYear(createPaymentMethodResult.getExpirationYear());
			brainTreeSubscriptionInfoData.setImageSource(createPaymentMethodResult.getImageSource());
			brainTreeSubscriptionInfoData.setCardNumber(createPaymentMethodResult.getCardNumber());
			brainTreeSubscriptionInfoData.setCardType(createPaymentMethodResult.getCardType());
			brainTreeSubscriptionInfoData.setCardholder(createPaymentMethodResult.getCardholderName());
			if (StringUtils.isNotBlank(createPaymentMethodResult.getEmail()))
			{
				brainTreeSubscriptionInfoData.setEmail(createPaymentMethodResult.getEmail());
			}
		}
	}

	public BrainTreePaymentInfoData getBrainTreePaymentInfoData()
	{
		final PaymentInfoModel paymentInfo = getCartService().getSessionCart().getPaymentInfo();
		if (paymentInfo instanceof BrainTreePaymentInfoModel)
		{

			validateParameterNotNullStandardMessage("paymentInfo", paymentInfo);

			final BrainTreePaymentInfoData paymentData = getBrainTreePaymentInfoDataConverter()
					.convert((BrainTreePaymentInfoModel) paymentInfo);
			return paymentData;
		}
		return null;
	}

	public BrainTreePaymentInfoData getBrainTreePaymentInfoData(final String orderCode)
	{
		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
		final OrderModel orderModel = getCustomerAccountService().getOrderForCode(orderCode, baseStoreModel);
		final PaymentInfoModel paymentInfo = orderModel.getPaymentInfo();
		if (paymentInfo instanceof BrainTreePaymentInfoModel)
		{

			validateParameterNotNullStandardMessage("paymentInfo", paymentInfo);

			final BrainTreePaymentInfoData paymentData = getBrainTreePaymentInfoDataConverter()
					.convert((BrainTreePaymentInfoModel) paymentInfo);
			return paymentData;
		}
		return null;
	}

	public BrainTreePaymentInfoData getBrainTreePaymentInfoDataByCart(final String cartCode)
	{
		final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartCode, getUserService().getCurrentUser());
		final BrainTreePaymentInfoModel paymentInfo = (BrainTreePaymentInfoModel) cartModel.getPaymentInfo();
		validateParameterNotNullStandardMessage("paymentInfo", paymentInfo);
		final BrainTreePaymentInfoData paymentData = getBrainTreePaymentInfoDataConverter().convert(paymentInfo);
		return paymentData;
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
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the customerAccountService
	 */
	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	/**
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	/**
	 * @return the commerceCartService
	 */
	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	/**
	 * @param commerceCartService
	 *           the commerceCartService to set
	 */
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
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
	 * @return the addressConverter
	 */
	public Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	/**
	 * @param addressConverter
	 *           the addressConverter to set
	 */
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
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
	 * @return the userService
	 */
	@Override
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Override
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	/**
	 * @return the brainTreeTransactionService
	 */
	public BrainTreeTransactionService getBrainTreeTransactionService()
	{
		return brainTreeTransactionService;
	}

	/**
	 * @param brainTreeTransactionService
	 *           the brainTreeTransactionService to set
	 */
	public void setBrainTreeTransactionService(final BrainTreeTransactionService brainTreeTransactionService)
	{
		this.brainTreeTransactionService = brainTreeTransactionService;
	}

	/**
	 * @return the customerEmailResolutionService
	 */
	public CustomerEmailResolutionService getCustomerEmailResolutionService()
	{
		return customerEmailResolutionService;
	}

	/**
	 * @param customerEmailResolutionService
	 *           the customerEmailResolutionService to set
	 */
	public void setCustomerEmailResolutionService(final CustomerEmailResolutionService customerEmailResolutionService)
	{
		this.customerEmailResolutionService = customerEmailResolutionService;
	}

	/**
	 * @return the brainTreeSubscriptionInfoConverter
	 */
	public Converter<BrainTreeSubscriptionInfoData, BraintreeInfo> getBrainTreeSubscriptionInfoConverter()
	{
		return brainTreeSubscriptionInfoConverter;
	}

	/**
	 * @param brainTreeSubscriptionInfoConverter
	 *           the brainTreeSubscriptionInfoConverter to set
	 */
	public void setBrainTreeSubscriptionInfoConverter(
			final Converter<BrainTreeSubscriptionInfoData, BraintreeInfo> brainTreeSubscriptionInfoConverter)
	{
		this.brainTreeSubscriptionInfoConverter = brainTreeSubscriptionInfoConverter;
	}

	/**
	 * @return the addressReverseConverter
	 */
	public Converter<AddressData, AddressModel> getAddressReverseConverter()
	{
		return addressReverseConverter;
	}

	/**
	 * @param addressReverseConverter
	 *           the addressReverseConverter to set
	 */
	public void setAddressReverseConverter(final Converter<AddressData, AddressModel> addressReverseConverter)
	{
		this.addressReverseConverter = addressReverseConverter;
	}

	/**
	 * @return the brainTreePaymentInfoDataConverter
	 */
	public Converter<BrainTreePaymentInfoModel, BrainTreePaymentInfoData> getBrainTreePaymentInfoDataConverter()
	{
		return brainTreePaymentInfoDataConverter;
	}

	/**
	 * @param brainTreePaymentInfoDataConverter
	 *           the brainTreePaymentInfoDataConverter to set
	 */
	public void setBrainTreePaymentInfoDataConverter(
			final Converter<BrainTreePaymentInfoModel, BrainTreePaymentInfoData> brainTreePaymentInfoDataConverter)
	{
		this.brainTreePaymentInfoDataConverter = brainTreePaymentInfoDataConverter;
	}

	public BrainTreeConfigService getBrainTreeConfigService() {
		return brainTreeConfigService;
	}

	public void setBrainTreeConfigService(BrainTreeConfigService brainTreeConfigService) {
		this.brainTreeConfigService = brainTreeConfigService;
	}
}
