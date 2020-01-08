/**
 *
 */
package com.braintree.facade.impl;

import static com.braintree.constants.BraintreeConstants.PAYPAL_INTENT_ORDER;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import com.braintree.constants.BraintreeConstants;
import com.braintree.enums.BrainTreePaymentMethod;
import com.braintree.hybris.data.BrainTreeSubscriptionInfoData;
import de.hybris.platform.acceleratorfacades.order.impl.DefaultAcceleratorCheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.braintree.configuration.service.BrainTreeConfigService;
import com.braintree.hybris.data.PayPalAddressData;
import com.braintree.hybris.data.PayPalCheckoutData;
import com.braintree.method.BrainTreePaymentService;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.paypal.converters.impl.PayPalAddressDataConverter;
import com.braintree.paypal.converters.impl.PayPalCardDataConverter;
import com.braintree.transaction.service.BrainTreeTransactionService;


public class BrainTreeCheckoutFacade extends DefaultAcceleratorCheckoutFacade
{
	private static final Logger LOG = Logger.getLogger(BrainTreeCheckoutFacade.class);

	private Converter<BrainTreePaymentInfoModel, CCPaymentInfoData> brainTreePaymentInfoConverter;
	private BrainTreePaymentService brainTreePaymentService;
	private BrainTreeTransactionService brainTreeTransactionService;
	private CartService cartService;
	private UserService userService;
	private PayPalAddressDataConverter payPalAddressDataConverter;
	private PayPalCardDataConverter payPalCardDataConverter;
	private BrainTreeConfigService brainTreeConfigService;
	private BrainTreePaymentFacadeImpl brainTreePaymentFacade;

	@Override
	public boolean authorizePayment(final String securityCode)
	{
		return authorizePayment(securityCode, Collections.EMPTY_MAP);
	}

	public boolean authorizePayment(final String securityCode, Map<String, String> customFields)
	{
		if (!isAvailablePaymentAuthorization())
		{
			return authorizePaymentIfIntentOrder(customFields);
		}
		if (Boolean.FALSE.equals(brainTreeConfigService.getPayPalStandardEnabled())
				&& Boolean.FALSE.equals(brainTreeConfigService.getHostedFieldEnabled()))
		{
			LOG.info("Use default accelerator checkout flow.");
			return super.authorizePayment(securityCode);
		}

		return brainTreeTransactionService.createAuthorizationTransaction(customFields);
	}

	@Override
	public CCPaymentInfoData getPaymentDetails()
	{
		final CartModel cart = getCart();
		if (cart != null)
		{
			final PaymentInfoModel paymentInfo = cart.getPaymentInfo();
			if (paymentInfo instanceof BrainTreePaymentInfoModel)
			{
				return brainTreePaymentInfoConverter.convert((BrainTreePaymentInfoModel) paymentInfo);
			}
			else
			{
				return super.getPaymentDetails();
			}
		}

		return null;
	}


	@Override
	public boolean setPaymentDetails(final String paymentInfoId)
	{
		validateParameterNotNullStandardMessage("paymentInfoId", paymentInfoId);

		if (checkIfCurrentUserIsTheCartUser())
		{
			final CustomerModel currentUserForCheckout = getCurrentUserForCheckout();
			if (StringUtils.isNotBlank(paymentInfoId))
			{
				final BrainTreePaymentInfoModel paymentInfo = brainTreePaymentService
						.completeCreateSubscription(currentUserForCheckout, paymentInfoId);
				if (paymentInfo != null)
				{
					return true;
				}
				else
				{
					super.setPaymentDetails(paymentInfoId);
				}

			}
		}

		return false;
	}

	public String generateClientToken()
	{
		final String clientToken = brainTreePaymentService.generateClientToken();
		return clientToken;
	}

	public PayPalCheckoutData getPayPalCheckoutData()
	{
		final PayPalCheckoutData payPalCheckoutData = payPalCardDataConverter.convert(cartService.getSessionCart());

		if (cartService.getSessionCart().getDeliveryAddress() != null)
		{
			final PayPalAddressData payPalAddress = payPalAddressDataConverter
					.convert(cartService.getSessionCart().getDeliveryAddress());
			payPalCheckoutData.setShippingAddressOverride(payPalAddress);
			payPalCheckoutData.setEnvironment(brainTreeConfigService.getEnvironmentTypeName());
			payPalCheckoutData.setSecure3d(brainTreeConfigService.get3dSecureConfiguration());
			payPalCheckoutData.setSkip3dSecureLiabilityResult(brainTreeConfigService.getIsSkip3dSecureLiabilityResult());
		}
		return payPalCheckoutData;
	}

	public boolean isAvailablePaymentAuthorization()
	{
		if (isCreditCard() || isApplePay())
		{
			return true;
		}
		boolean paypalIntentIsOrder = PAYPAL_INTENT_ORDER.equals(getBrainTreeConfigService().getIntent());
		boolean config = !Boolean.FALSE.toString().equals(getBrainTreeConfigService().getStoreInVaultForCurrentUser());
		return  !(paypalIntentIsOrder && !config);
	}

	private boolean isCreditCard()
	{
		PaymentInfoModel paymentInfoModel = getCart().getPaymentInfo();
		if (paymentInfoModel != null && paymentInfoModel instanceof BrainTreePaymentInfoModel)
		{
			return BrainTreePaymentMethod.CREDITCARD.getCode().equalsIgnoreCase(((BrainTreePaymentInfoModel) paymentInfoModel).getPaymentProvider());
		}
		return false;
	}

	private boolean isApplePay()
	{
		PaymentInfoModel paymentInfoModel = getCart().getPaymentInfo();
		if (paymentInfoModel != null && paymentInfoModel instanceof BrainTreePaymentInfoModel)
		{
			return BrainTreePaymentMethod.APPLEPAYCARD.getCode().equalsIgnoreCase(((BrainTreePaymentInfoModel) paymentInfoModel).getPaymentProvider());
		}
		return false;
	}

	public void setBrainTreePaymentInfoConverter(
			final Converter<BrainTreePaymentInfoModel, CCPaymentInfoData> brainTreePaymentInfoConverter)
	{
		this.brainTreePaymentInfoConverter = brainTreePaymentInfoConverter;
	}

	public Map<String, String> getAcceptedPaymentMethodImages()
	{
		final Map<String, String> acceptedPaymentMethodImages = brainTreeConfigService.getAcceptedPaymentMethodImages();
		return acceptedPaymentMethodImages;
	}

	private boolean authorizePaymentIfIntentOrder(Map<String, String> customFields)
	{
		createPaymentMethodIfIntentOrder(customFields);
		return Boolean.TRUE.booleanValue();
	}

	private void createPaymentMethodIfIntentOrder (Map<String, String> customFields)
	{
		CartModel cart = getCartService().getSessionCart();
		final BrainTreeSubscriptionInfoData subscriptionInfo = buildSubscriptionInfo((BrainTreePaymentInfoModel)cart.getPaymentInfo(), true);
		subscriptionInfo.setIntent(BraintreeConstants.PAYPAL_INTENT_ORDER);
		subscriptionInfo.setAmount(String.valueOf(cart.getTotalPrice()));
		brainTreePaymentFacade.completeCreateSubscription(subscriptionInfo);

		BrainTreePaymentInfoModel brainTreePaymentInfo = (BrainTreePaymentInfoModel) cart.getPaymentInfo();
		brainTreePaymentInfo.setPayPalIntent(BraintreeConstants.PAYPAL_INTENT_ORDER);
		brainTreePaymentInfo.setSaved(false);
		brainTreePaymentInfo.setCustomFields(customFields);
		getModelService().save(brainTreePaymentInfo);
	}

	private BrainTreeSubscriptionInfoData buildSubscriptionInfo(final BrainTreePaymentInfoModel paymentInfoModel, final boolean isPaymentInfoSaved)
	{
		final BrainTreeSubscriptionInfoData subscriptionInfo = new BrainTreeSubscriptionInfoData();
		subscriptionInfo.setPaymentProvider(paymentInfoModel.getPaymentProvider());
		subscriptionInfo.setCardNumber(paymentInfoModel.getCardNumber());
		subscriptionInfo.setDeviceData(paymentInfoModel.getDeviceData());
		subscriptionInfo.setNonce(paymentInfoModel.getNonce());
		subscriptionInfo.setSavePaymentInfo(isPaymentInfoSaved);
		subscriptionInfo.setCardholder(paymentInfoModel.getCardholderName());
		if (paymentInfoModel.getLiabilityShifted() != null)
		{
			subscriptionInfo.setLiabilityShifted(paymentInfoModel.getLiabilityShifted());
		}
		if (paymentInfoModel.getCardType() != null)
		{
			subscriptionInfo.setCardType(paymentInfoModel.getCardType().getCode());
		}
		if (paymentInfoModel.getBillingAddress() != null)
		{
			subscriptionInfo.setEmail(paymentInfoModel.getBillingAddress().getEmail());
		}
		return subscriptionInfo;
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
	 * @return the cartService
	 */
	@Override
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	@Override
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
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
	 * @return the payPalAddressDataConverter
	 */
	public PayPalAddressDataConverter getPayPalAddressDataConverter()
	{
		return payPalAddressDataConverter;
	}

	/**
	 * @param payPalAddressDataConverter
	 *           the payPalAddressDataConverter to set
	 */
	public void setPayPalAddressDataConverter(final PayPalAddressDataConverter payPalAddressDataConverter)
	{
		this.payPalAddressDataConverter = payPalAddressDataConverter;
	}

	/**
	 * @return the payPalCardDataConverter
	 */
	public PayPalCardDataConverter getPayPalCardDataConverter()
	{
		return payPalCardDataConverter;
	}

	/**
	 * @param payPalCardDataConverter
	 *           the payPalCardDataConverter to set
	 */
	public void setPayPalCardDataConverter(final PayPalCardDataConverter payPalCardDataConverter)
	{
		this.payPalCardDataConverter = payPalCardDataConverter;
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
	 * @return the brainTreePaymentInfoConverter
	 */
	public Converter<BrainTreePaymentInfoModel, CCPaymentInfoData> getBrainTreePaymentInfoConverter()
	{
		return brainTreePaymentInfoConverter;
	}

	public BrainTreePaymentFacadeImpl getBrainTreePaymentFacade() {
		return brainTreePaymentFacade;
	}

	public void setBrainTreePaymentFacade(BrainTreePaymentFacadeImpl brainTreePaymentFacade) {
		this.brainTreePaymentFacade = brainTreePaymentFacade;
	}
}
