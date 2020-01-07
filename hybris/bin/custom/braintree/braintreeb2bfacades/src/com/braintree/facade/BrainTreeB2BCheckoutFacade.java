/**
 *
 */
package com.braintree.facade;

import com.braintree.constants.BraintreeConstants;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.transaction.service.BrainTreeTransactionService;
import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BCommentData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BReplenishmentRecurrenceEnum;
import de.hybris.platform.b2bacceleratorfacades.order.data.TriggerData;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BAcceleratorCheckoutFacade;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;

import javax.annotation.Resource;

import com.braintree.facade.impl.BrainTreeCheckoutFacade;
import de.hybris.platform.order.InvalidCartException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import static de.hybris.platform.util.localization.Localization.getLocalizedString;


public class BrainTreeB2BCheckoutFacade extends DefaultB2BAcceleratorCheckoutFacade
{
	private static final String CART_CHECKOUT_DELIVERYADDRESS_INVALID = "cart.deliveryAddress.invalid";
	private static final String CART_CHECKOUT_DELIVERYMODE_INVALID = "cart.deliveryMode.invalid";
	private static final String CART_CHECKOUT_TRANSACTION_NOT_AUTHORIZED = "cart.transation.notAuthorized";
	private static final String CART_CHECKOUT_PAYMENTINFO_EMPTY = "cart.paymentInfo.empty";
	private static final String CART_CHECKOUT_NOT_CALCULATED = "cart.not.calculated";
	private static final String CART_CHECKOUT_TERM_UNCHECKED = "cart.term.unchecked";
	private static final String CART_CHECKOUT_NO_QUOTE_DESCRIPTION = "cart.no.quote.description";
	private static final String CART_CHECKOUT_REPLENISHMENT_NO_STARTDATE = "cart.replenishment.no.startdate";
	private static final String CART_CHECKOUT_REPLENISHMENT_NO_FREQUENCY = "cart.replenishment.no.frequency";
	private static final String CART_CHECKOUT_QUOTE_REQUIREMENTS_NOT_SATISFIED = "cart.quote.requirements.not.satisfied";

	private static final Integer MIN_HYBRIS_API_VERSION_FOR_QUOTE_VALIDATION = Integer.valueOf(6);

	@Resource(name = "brainTreeCheckoutFacade")
	private BrainTreeCheckoutFacade brainTreeCheckoutFacade;
	@Resource(name = "brainTreeTransactionService")
	private BrainTreeTransactionService brainTreeTransactionService;

	@Override
	public boolean authorizePayment(final String securityCode)
	{
		final CartModel cart = getCartService().getSessionCart();
		if (CheckoutPaymentType.ACCOUNT.equals(cart.getPaymentType()))
		{
			return super.authorizePayment(securityCode);
		}
		return brainTreeCheckoutFacade.authorizePayment(securityCode);
	}

	@Override
	protected CCPaymentInfoData getPaymentDetails()
	{
		return brainTreeCheckoutFacade.getPaymentDetails();
	}

	@Override
	public boolean setPaymentDetails(final String paymentInfoId)
	{
		return brainTreeCheckoutFacade.setPaymentDetails(paymentInfoId);
	}

	public boolean authorizePaymentForReplenishment()
	{
		return getBrainTreeTransactionService().createPaymentMethodTokenForOrderReplenishment();
	}

	@Override
	public <T extends AbstractOrderData> T placeOrder(PlaceOrderData placeOrderData) throws InvalidCartException {
		CartModel cart = brainTreeCheckoutFacade.getCartService().getSessionCart();
		String intent = StringUtils.EMPTY;
		if (cart.getPaymentInfo() != null && cart.getPaymentInfo() instanceof  BrainTreePaymentInfoModel){
			intent = ((BrainTreePaymentInfoModel)cart.getPaymentInfo()).getPayPalIntent();
		}
		if (BraintreeConstants.PAYPAL_INTENT_ORDER.equals(intent)) {
			return placeOrderIfIntentOrder(placeOrderData);
		}
		return super.placeOrder(placeOrderData);
	}

	public <T extends AbstractOrderData> T placeOrderIfIntentOrder(PlaceOrderData placeOrderData) throws InvalidCartException {
		// term must be checked
		if (!placeOrderData.getTermsCheck().equals(Boolean.TRUE))
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_TERM_UNCHECKED));
		}

		if (isValidCheckoutCart(placeOrderData))
		{
			// validate quote negotiation
			if (placeOrderData.getNegotiateQuote() != null && placeOrderData.getNegotiateQuote().equals(Boolean.TRUE))
			{
				if (StringUtils.isBlank(placeOrderData.getQuoteRequestDescription()))
				{
					throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_NO_QUOTE_DESCRIPTION));
				}
				else
				{
					final B2BCommentData b2BComment = new B2BCommentData();
					b2BComment.setComment(placeOrderData.getQuoteRequestDescription());

					final CartData cartData = new CartData();
					cartData.setB2BComment(b2BComment);

					updateCheckoutCart(cartData);
				}
			}

			// validate replenishment
			if (placeOrderData.getReplenishmentOrder() != null && placeOrderData.getReplenishmentOrder().equals(Boolean.TRUE))
			{
				if (placeOrderData.getReplenishmentStartDate() == null)
				{
					throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_REPLENISHMENT_NO_STARTDATE));
				}

				if (placeOrderData.getReplenishmentRecurrence().equals(B2BReplenishmentRecurrenceEnum.WEEKLY)
						&& CollectionUtils.isEmpty(placeOrderData.getNDaysOfWeek()))
				{
					throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_REPLENISHMENT_NO_FREQUENCY));
				}

				final TriggerData triggerData = new TriggerData();
				populateTriggerDataFromPlaceOrderData(placeOrderData, triggerData);

				return (T) scheduleOrder(triggerData);
			}

			return (T) super.placeOrder();
		}

		return null;
	}


	/**
	 * This method should not be annotated by Override annotation because DefaultB2BCheckoutFacade class in hybris versions
	 * before 6 does not contain this method with PLaceOrderData parameter. That's why it should be overloaded.
	 *
	 */
	protected boolean isValidCheckoutCart(final PlaceOrderData placeOrderData)
	{
		final CartData cartData = getCheckoutCart();
		final boolean valid = true;

		if (!cartData.isCalculated())
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_NOT_CALCULATED));
		}

		if (cartData.getDeliveryAddress() == null)
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_DELIVERYADDRESS_INVALID));
		}

		if (cartData.getDeliveryMode() == null)
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_DELIVERYMODE_INVALID));
		}

		final boolean accountPaymentType = CheckoutPaymentType.ACCOUNT.getCode().equals(cartData.getPaymentType().getCode());
		if (!accountPaymentType && cartData.getPaymentInfo() == null) {
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_PAYMENTINFO_EMPTY));
		}

		if (Boolean.TRUE.equals(placeOrderData.getNegotiateQuote()) && !cartData.getQuoteAllowed()
				&& isAvailableQuoteValidation())
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_QUOTE_REQUIREMENTS_NOT_SATISFIED));
		}

		return valid;
	}

	private boolean isAvailableQuoteValidation()
	{
		Integer apiVersion = brainTreeCheckoutFacade.getBrainTreeConfigService().getHybrisBuildApiVersion();
		return apiVersion != null && apiVersion >= MIN_HYBRIS_API_VERSION_FOR_QUOTE_VALIDATION;
	}

	public void setCardPaymentType()
	{
		final CartModel cart = getCart();
		cart.setPaymentType(CheckoutPaymentType.CARD);
		getModelService().save(cart);
	}

	public BrainTreeTransactionService getBrainTreeTransactionService() {
		return brainTreeTransactionService;
	}

	public void setBrainTreeTransactionService(BrainTreeTransactionService brainTreeTransactionService) {
		this.brainTreeTransactionService = brainTreeTransactionService;
	}
}
