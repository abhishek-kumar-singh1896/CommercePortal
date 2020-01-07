package com.braintree.cscockpit.widgets.controllers.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ResourceMessage;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultCheckoutController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.controllers.BraintreeCheckoutController;
import com.braintree.cscockpit.widgets.renderers.impl.checkout.utils.PaymentMethodListUtils;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.payment.dto.BraintreeInfo;


public class DefaultBraintreeCheckoutController extends DefaultCheckoutController implements BraintreeCheckoutController
{
	private CsBrainTreeFacade brainTreeFacade;

	@Override
	public List<TypedObject> getBraintreePaymentInfos()
	{
		return this.getCustomerController().getBrainTreePaymentInfos();
	}

	@Override
	public boolean processPayment(final TypedObject typedObject, final BigDecimal amount) throws Exception
	{
		if (typedObject != null && typedObject.getObject() instanceof BrainTreePaymentInfoModel)
		{
			CartModel cartModel = getCartModel();
			cartModel.setPaymentInfo((BrainTreePaymentInfoModel) typedObject.getObject());

			getModelService().save(cartModel);
			getModelService().refresh(cartModel);
			return true;
		}
		return false;
	}

	@Override
	public boolean isPaymentMethodAvailableInCard()
	{
		PaymentInfoModel paymentInfo = this.getCartModel().getPaymentInfo();
		return paymentInfo instanceof BrainTreePaymentInfoModel;
	}

	@Override
	public boolean isPaymentMethodSelected(TypedObject typedObject)
	{
		boolean braintreePaymentMethod = PaymentMethodListUtils.isBraintreePaymentMethod(typedObject);
		if (braintreePaymentMethod)
		{
			BrainTreePaymentInfoModel paymentInfoModel = (BrainTreePaymentInfoModel) typedObject.getObject();
			PaymentInfoModel paymentInfoFromCard = this.getCartModel().getPaymentInfo();
			return paymentInfoModel.equals(paymentInfoFromCard);
		}

		return false;
	}
	@Override
	public boolean processPayment(final ObjectValueContainer paymentOptionValueContainer) throws PaymentException,
			ValidationException
	{
		validateNonce();
		final PaymentOption paymentOption = createPaymentOptionFrom(paymentOptionValueContainer);

		return getBrainTreeFacade().createPaymentInfo(getCartModel(), paymentOption.getCardInfo(), paymentOption.getAmount());
	}

	private void validateNonce() throws ValidationException
	{
		try
		{
			BraintreeInfo braintreeInfo = getBrainTreeFacade().getBraintreeInfo();
			if (StringUtils.isBlank(braintreeInfo.getNonce()))
			{
				throw new ValidationException(Arrays.asList(new ResourceMessage(
						"paymentOption.billingInfo.validation.noPaymentMethodToken")));
			}
		}
		catch (IllegalStateException e)
		{
			throw new ValidationException(Arrays.asList(new ResourceMessage("paymentOption.billingInfo.validation.noBraintreeInfo")));
		}
	}
	@Override
	public BraintreeDefaultCallContextController getCustomerController()
	{
		return (BraintreeDefaultCallContextController) super.getCustomerController();
	}

	@Override
	public TypedObject placeOrder() throws ValidationException
	{
		// Create Authorization transaction and execute standard flow
		final CartModel cart = getCartModel();

		try
		{
			getBrainTreeFacade().authorizePayment(cart);
			cart.setStatus(OrderStatus.CREATED);
		}
		catch (final PaymentException e)
		{
			throw new ValidationException(Collections.<ResourceMessage> emptyList(), e);
		}

		return super.placeOrder();
	}



	public CsBrainTreeFacade getBrainTreeFacade()
	{
		return brainTreeFacade;
	}

	public void setBrainTreeFacade(final CsBrainTreeFacade brainTreeFacade)
	{
		this.brainTreeFacade = brainTreeFacade;
	}
}
