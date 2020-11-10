/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.sap.orderexchange.outbound.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PaymentCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultPaymentContributor;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.gallagher.constants.GallagherPaymentCsvColumns;


/**
 * Custom payment contributor to support the GGL requirements
 *
 * @author Vikram Bishnoi
 */
public class GallagherPaymentContributor extends DefaultPaymentContributor
{

	private ConfigurationService configurationService;

	@Override
	public List<Map<String, Object>> createRows(final OrderModel order)
	{
		final List<Map<String, Object>> result = new ArrayList<>();

		for (final PaymentTransactionModel payment : order.getPaymentTransactions())
		{
			final PaymentInfoModel paymentInfo = order.getPaymentInfo();

			final Map<String, Object> row = new HashMap<>();
			row.put(OrderCsvColumns.ORDER_ID, order.getCode());
			row.put(PaymentCsvColumns.PAYMENT_PROVIDER, payment.getPaymentProvider());

			if (paymentInfo instanceof CreditCardPaymentInfoModel)
			{
				final CreditCardPaymentInfoModel ccPaymentInfo = (CreditCardPaymentInfoModel) paymentInfo;
				if (ccPaymentInfo.getType() != null)
				{
					row.put(PaymentCsvColumns.PAYMENT_PROVIDER, configurationService.getConfiguration()
							.getString("gallagher.card.".concat(ccPaymentInfo.getType().getCode()), ccPaymentInfo.getType().getCode()));
				}
				row.put(PaymentCsvColumns.CC_OWNER, ccPaymentInfo.getCcOwner());
				row.put(PaymentCsvColumns.VALID_TO_MONTH, ccPaymentInfo.getValidToMonth());
				row.put(PaymentCsvColumns.VALID_TO_YEAR, ccPaymentInfo.getValidToYear());
				row.put(PaymentCsvColumns.SUBSCRIPTION_ID, ccPaymentInfo.getNumber());
			}

			if (CollectionUtils.isNotEmpty(payment.getEntries()))
			{
				for (final PaymentTransactionEntryModel entry : payment.getEntries())
				{
					row.put(GallagherPaymentCsvColumns.AMOUNT, entry.getAmount());
					row.put(GallagherPaymentCsvColumns.AUTHORIZATION_TIME, entry.getTime());
					row.put(GallagherPaymentCsvColumns.AUTHORIZATION_NUMBER, entry.getAUTRA());
					row.put(GallagherPaymentCsvColumns.RESULT_TEXT, entry.getRTEXT());
					row.put(GallagherPaymentCsvColumns.MERCHANT_ID, entry.getMERCH());
					row.put(GallagherPaymentCsvColumns.AVS_CODE, entry.getRCAVR());
					row.put(PaymentCsvColumns.REQUEST_ID, entry.getAUNUM());
				}
			}
			getBatchIdAttributes().forEach(row::putIfAbsent);
			row.put("dh_batchId", order.getCode());
			result.add(row);
		}

		return result;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}