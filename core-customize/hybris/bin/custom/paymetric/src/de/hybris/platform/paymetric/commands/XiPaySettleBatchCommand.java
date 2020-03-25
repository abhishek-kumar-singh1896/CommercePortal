/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2018 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.paymetric.commands;

import de.hybris.platform.paymetric.commands.request.SettleBatchRequest;
import de.hybris.platform.paymetric.commands.result.SettleBatchResult;
import de.hybris.platform.paymetric.constants.PaymetricConstants;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.axis.AxisFault;
import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paymetric.xslt.CTransformer;
import com.paymetric.xslt.CXmlHelper;
import com.primesys.xipayws.CTimer;
import com.primesys.xipayws.CXiPay;

import Paymetric.XiPaySoap30.message.ITransactionHeader;


/**
 * This class is responsible for submitting an array of XiPay transactions for settlement using the {@link SettleBatchRequest}
 */
@SuppressWarnings("unused")
public class XiPaySettleBatchCommand extends AbstractCommand<SettleBatchRequest, SettleBatchResult>
{
	private static Logger LOG = null;
	private static final String XSLT_REFRESH = "paymetric.xslt.settle-batch.load";
	private static final String XIPAYRQ = "xiPaySettleBatchRQ";
	private static final String XIPAYRS = "xiPaySettleBatchRS";
	private static CTransformer xsltToXiPay = null;
	private static CTransformer xsltFromXiPay = null;
	private SessionService sessionService;


	/**
	 * THIS PUBLIC CONSTRUCTOR IS RESPONSIBLE FOR LOADING THE HYBRIS CONFIGURATION VALUES AT SERVER STARTUP
	 */
	public XiPaySettleBatchCommand()
	{
		super();
		if (LOG == null)
		{
			LOG = LoggerFactory.getLogger(this.getClass());
			CXmlHelper.addAliases("SettleBatchRequest", SettleBatchRequest.class, LOG);
			CXmlHelper.addAliases("SettleBatchResult", SettleBatchResult.class, LOG);
			PaymetricConstants.setXsltRefresh(XSLT_REFRESH, "true");
		}
	}

	@Override
	public Logger getLogger()
	{
		return LOG;
	}

	@Override
	protected synchronized void loadXslt()
	{
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Load/Reload and Compile/Recompile XSLT?
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (PaymetricConstants.getXsltRefresh(XSLT_REFRESH))
		{
			final CTimer timer = (isProfiling.get() ? new CTimer() : null);
			final String strWhere = PaymetricConstants.getWhere("loadXslt");
			String strXslt = null;

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Initialize and compile our XSLTs
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			try
			{
				strXslt = readXslt(PaymetricConstants.getXsltSettleBatchToXiPay());
				xsltToXiPay = new CTransformer();
				xsltToXiPay.Configure(strXslt);

				strXslt = readXslt(PaymetricConstants.getXsltSettleBatchFromXiPay());
				xsltFromXiPay = new CTransformer();
				xsltFromXiPay.Configure(strXslt);
			}
			catch (final Exception ex)
			{
				PaymetricConstants.forcedLog(LOG, strWhere, "Exception", ex);
			}
			finally
			{
				if (timer != null)
				{
					PaymetricConstants.forcedLog(LOG, strWhere, "Duration=" + timer.getElapsedTimeString());
				}
			}
		}
	}

	/**
	 * method to get the SessionService instance
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * method to set the SessionService instance via spring configuration
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


	@Override
	protected ITransactionHeader[] translateRequest(final SettleBatchRequest request)
	{
		final CTimer timer = (isProfiling.get() ? new CTimer() : null);
		final String strWhere = PaymetricConstants.getWhere("translateRequest");
		final Session session = (sessionService.hasCurrentSession() ? sessionService.getCurrentSession() : null);
		ITransactionHeader[] xiTran = null;


		try
		{
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Convert the request object to XML
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			String xmlPayload = CXmlHelper.toXml(request, LOG);
			final Document xmlDoc = CXmlHelper.DocumentFromString(xmlPayload, LOG);
			final Element root = xmlDoc.getRootElement();

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Construct the missing XML payload for the request
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (session != null)
			{
				final Map<String, String> nodes = (HashMap<String, String>) session.getAttribute(XIPAYRQ);
				if (nodes != null)
				{
					final Element customFields = element("customFields");
					for (final Entry<String, String> item : nodes.entrySet())
					{
						customFields.addContent(element(item.getKey(), item.getValue()));
					}
					root.addContent(customFields);
				}
			}

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Construct the FINAL xml to be used during the XSLT transform
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			xmlPayload = CXmlHelper.DocumentToString(xmlDoc, LOG);
			if (isLogging.get())
			{
				PaymetricConstants.forcedLog(LOG, strWhere, "Hybris REQUEST - FINAL\n" + xmlPayload);
			}

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Convert the request object to XML
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			final String strTran = xsltToXiPay.transform(xmlPayload);

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Convert the XML to an ITransactionHeader object
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			xiTran = CXmlHelper.fromXml(strTran, LOG);
		}
		catch (final Exception ex)
		{
			PaymetricConstants.forcedLog(LOG, strWhere, "Exception", ex);
		}
		finally
		{
			if (timer != null)
			{
				PaymetricConstants.forcedLog(LOG, strWhere, "Duration=" + timer.getElapsedTimeString());
			}
		}

		return xiTran;
	}

	@Override
	protected ITransactionHeader[] callXiPay(final CXiPay xiPay, final ITransactionHeader[] xiTran)
	{
		final CTimer timer = (isProfiling.get() ? new CTimer() : null);
		final String strWhere = PaymetricConstants.getWhere("callXiPay");
		ITransactionHeader[] xiTranRS = null;


		try
		{
			xiTranRS = xiPay.SettleBatch(xiTran, xiTran[0].getBatchID());
		}
		catch (final AxisFault ex)
		{
			xiTranRS = xiTran;
			xiTranRS[0].setStatusCode(-1);
			xiTranRS[0].setMessage("XiPay communication failure");
		}
		catch (final Exception ex)
		{
			xiTranRS = xiTran;
			xiTranRS[0].setStatusCode(-2);
			xiTranRS[0].setMessage("General exception:" + ex.getMessage());
			PaymetricConstants.forcedLog(LOG, strWhere, "Exception", ex);
		}
		finally
		{
			if (timer != null)
			{
				PaymetricConstants.forcedLog(LOG, strWhere, "Duration=" + timer.getElapsedTimeString());
			}
		}

		return xiTranRS;
	}

	@Override
	protected SettleBatchResult translateResponse(final ITransactionHeader[] xiTran)
	{
		final CTimer timer = (isProfiling.get() ? new CTimer() : null);
		final String strWhere = PaymetricConstants.getWhere("translateResponse");
		SettleBatchResult result = null;


		try
		{
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Convert the XiPay response to XML
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			final String xmlPayload = CXmlHelper.toXml(xiTran, LOG);

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Convert the XiPay response XML to Hybris XML response
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			final String strHybris = xsltFromXiPay.transform(xmlPayload);

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Convert the Hybris XML response to a SettleBatchResult object
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			result = CXmlHelper.fromXml(strHybris, LOG);
		}
		catch (final Exception ex)
		{
			PaymetricConstants.forcedLog(LOG, strWhere, "Exception", ex);
		}
		finally
		{
			if (timer != null)
			{
				PaymetricConstants.forcedLog(LOG, strWhere, "Duration=" + timer.getElapsedTimeString());
			}
		}

		return result;
	}
}
