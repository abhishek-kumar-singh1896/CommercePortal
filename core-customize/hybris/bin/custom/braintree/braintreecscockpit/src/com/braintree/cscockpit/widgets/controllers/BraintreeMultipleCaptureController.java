package com.braintree.cscockpit.widgets.controllers;

import com.braintree.exceptions.BraintreeErrorException;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;

import java.math.BigDecimal;


public interface BraintreeMultipleCaptureController extends WidgetController
{
	TypedObject getOrder();

	/**
	 * Performs partial capture for order
	 */
	Boolean partialCapture(BigDecimal bigDecimal, String authorizeTransactionID) throws BraintreeErrorException;

	/**
	 * Returns amount available for capture
	 */
	BigDecimal getAmountAvailableForMultiCapture();
}
