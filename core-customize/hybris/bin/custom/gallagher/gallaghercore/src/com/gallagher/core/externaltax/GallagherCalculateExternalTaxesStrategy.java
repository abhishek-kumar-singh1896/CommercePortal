/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.core.externaltax;

import de.hybris.platform.commerceservices.externaltax.CalculateExternalTaxesStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.externaltax.ExternalTaxDocument;
import de.hybris.platform.util.TaxValue;

import javax.annotation.Resource;

import com.gallagher.core.util.GallagherSovosUtil;
import com.gallagher.outboundservices.request.dto.GallagherSovosCalculateTaxRequest;
import com.gallagher.outboundservices.response.dto.GallagherSovosCalculatedTax;
import com.gallagher.outboundservices.response.dto.GallagherSovosCalculatedTaxLineItem;
import com.gallagher.outboundservices.response.dto.GallagherSovosCalculatedTaxResponse;
import com.gallagher.sovos.outboundservices.service.GallagherSovosService;


/**
 * Base {@link CalculateExternalTaxesStrategy} implementation of external tax call to return ExternalTaxDocument
 *
 * @author shishirkant
 */
public class GallagherCalculateExternalTaxesStrategy implements CalculateExternalTaxesStrategy
{
	@Resource(name = "gallagherSovosService")
	private GallagherSovosService gallagherSovosService;

	/**
	 * Gallagher implementation to return an external tax document.
	 */
	@Override
	public ExternalTaxDocument calculateExternalTaxes(final AbstractOrderModel abstractOrder)
	{
		final ExternalTaxDocument externalDocument = new ExternalTaxDocument();


		if (abstractOrder == null)
		{
			throw new IllegalStateException("Order is null. Cannot apply external tax to it.");
		}

		final GallagherSovosCalculateTaxRequest request = new GallagherSovosCalculateTaxRequest();
		GallagherSovosUtil.convert(abstractOrder, request);

		final GallagherSovosCalculatedTaxResponse response = gallagherSovosService.calculateExternalTax(request);

		for (final GallagherSovosCalculatedTaxLineItem lienItem : response.getLnRslts())
		{
			Double taxAmount = 0.0;
			for (final GallagherSovosCalculatedTax calculatedTax : lienItem.getJurRslts())
			{

				taxAmount = taxAmount + Double.valueOf(calculatedTax.getTxAmt()) + Double.valueOf(lienItem.getGrossAmt());
			}

			final TaxValue taxValue = new TaxValue(lienItem.getLnId(), taxAmount, true, taxAmount,
					abstractOrder.getCurrency() == null ? "USD" : abstractOrder.getCurrency().getIsocode());
			externalDocument.setTaxesForOrderEntry(Integer.valueOf(lienItem.getLnNm()) - 1, taxValue);
		}

		final TaxValue taxValue = new TaxValue("taxCode1", 3.0D, true, 3.0D,
				abstractOrder.getCurrency() == null ? "USD" : abstractOrder.getCurrency().getIsocode());
		externalDocument.setShippingCostTaxes(taxValue);

		return externalDocument;
	}
}
