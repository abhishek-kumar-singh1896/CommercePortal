package com.braintree.order.capture.partial.services;

import com.braintree.exceptions.BraintreeErrorException;
import de.hybris.platform.core.model.order.OrderModel;

import java.math.BigDecimal;


public interface BraintreePartialCaptureService
{
	/**
	 * do partial capture, call BT, create new transactions
	 * 
	 * @param orderModel
	 *           order
	 * @param captureAmount
	 *           amount to capture
	 */

	boolean partialCapture(OrderModel orderModel, BigDecimal captureAmount) throws BraintreeErrorException;

	boolean partialCapture(OrderModel orderModel, BigDecimal captureAmount, String authorizeTransactionID) throws BraintreeErrorException;

	/**
	 * get possible amount that can be captured
	 * 
	 * @return amount
	 */
    BigDecimal getPossibleAmountForCapture(OrderModel orderModel);

	/**
	 * check order status and sign enabled for multi capturing
	 */
	boolean isPartialCapturePossible(OrderModel orderModel);

    /**
     * when
     * @param orderModel
     */
    void continueOrderProcess(OrderModel orderModel);

    boolean isValidTransactionId(OrderModel orderModel, String transactionId);
}
