/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.braintree.web;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cscockpit.utils.LabelUtils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.braintree.constants.BraintreecscockpitConstants;
import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.payment.dto.BraintreeInfo;


/**
 * Servlet for managing hosted fields
 */
public class BraintreeHOPServlet extends HttpServlet
{
	private static final Logger LOG = LoggerFactory.getLogger(BraintreeHOPServlet.class);

	private static final String BRAINTREE_HOP_PAGE = "/WEB-INF/pages/braintree-hop.jsp";
	private static final String CLIENT_TOKEN = "clientToken";
	private static final String PAYMENT_METHOD_NONCE = "payment_method_nonce";
	private static final String CARDHOLDER = "cardholder";
	private static final String CHECK_KEY = "check";
	private static final String VALID_KEY = "valid";
	private static final String INVALID_KEY = "invalid";

	@Autowired
	private CsBrainTreeFacade braintreeFacade;

	@Override
	public void init() throws ServletException
	{
		LOG.debug("Autowire dependencies for servlet during initialization");
		Registry.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
	}

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
	{
		getBraintreeFacade().removeBraintreeInfo();
		final String clientToken = braintreeFacade.generateClientToken();
		LOG.debug("Generated Braintree client token: {}", clientToken);

		request.setAttribute(CLIENT_TOKEN, clientToken);

		//TODO: IP - Try to find better way to get localized messages
		request.setAttribute("paymentMethodCheckMessage",
				LabelUtils.getLabel(BraintreecscockpitConstants.BraintreeHOP.VALIDATION_PAYMENT_METHOD, CHECK_KEY, new Object[0]));
		request.setAttribute("paymentMethodValidMessage",
				LabelUtils.getLabel(BraintreecscockpitConstants.BraintreeHOP.VALIDATION_PAYMENT_METHOD, VALID_KEY, new Object[0]));
		request.setAttribute("paymentMethodInvalidMessage",
				LabelUtils.getLabel(BraintreecscockpitConstants.BraintreeHOP.VALIDATION_PAYMENT_METHOD, INVALID_KEY, new Object[0]));

		request.getRequestDispatcher(BRAINTREE_HOP_PAGE).forward(request, response);
	}

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException
	{
		final BraintreeInfo braintreeInfo = extractBraintreeInfo(request);

		LOG.debug("Received braintree payment info method nonce: {}", braintreeInfo.getNonce());
		getBraintreeFacade().saveBraintreeInfo(braintreeInfo);

		request.getRequestDispatcher(BRAINTREE_HOP_PAGE).forward(request, response);
	}

	protected BraintreeInfo extractBraintreeInfo(final HttpServletRequest request)
	{
		final BraintreeInfo braintreeInfo = new BraintreeInfo();
		braintreeInfo.setNonce(request.getParameter(PAYMENT_METHOD_NONCE));
		braintreeInfo.setCardholderName(request.getParameter(CARDHOLDER));

		return braintreeInfo;
	}

	/**
	 * @return the braintreeFacade
	 */
	public CsBrainTreeFacade getBraintreeFacade()
	{
		return braintreeFacade;
	}

	/**
	 * @param braintreeFacade
	 *           the braintreeFacade to set
	 */
	public void setBraintreeFacade(final CsBrainTreeFacade braintreeFacade)
	{
		this.braintreeFacade = braintreeFacade;
	}
}
