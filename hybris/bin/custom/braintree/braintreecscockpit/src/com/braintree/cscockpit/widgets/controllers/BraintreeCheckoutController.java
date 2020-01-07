package com.braintree.cscockpit.widgets.controllers;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;

import java.math.BigDecimal;
import java.util.List;


public interface BraintreeCheckoutController extends CheckoutController
{

	List<TypedObject> getBraintreePaymentInfos();

	boolean processPayment(TypedObject typedObject, BigDecimal amount) throws Exception;

	boolean isPaymentMethodAvailableInCard();

	boolean isPaymentMethodSelected(TypedObject typedObject);

}
