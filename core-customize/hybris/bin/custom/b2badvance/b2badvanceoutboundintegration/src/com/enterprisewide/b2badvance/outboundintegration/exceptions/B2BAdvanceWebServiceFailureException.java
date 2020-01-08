/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.enterprisewide.b2badvance.outboundintegration.exceptions;

/**
 * The Class B2BAdvanceWebServiceFailureException. Used to indicate failure
 * during Web Service communication.
 */
public class B2BAdvanceWebServiceFailureException extends Exception {

	private static final long serialVersionUID = 1709139639590657730L;

	/**
	 * Instantiates a new FS web service failure exception.
	 */
	public B2BAdvanceWebServiceFailureException() {
		super();
	}

	/**
	 * Instantiates a new FS web service failure exception.
	 *
	 * @param message the message
	 */
	public B2BAdvanceWebServiceFailureException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new FS web service failure exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public B2BAdvanceWebServiceFailureException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new FS web service failure exception.
	 *
	 * @param cause the cause
	 */
	public B2BAdvanceWebServiceFailureException(final Throwable cause) {
		super(cause);
	}

}
