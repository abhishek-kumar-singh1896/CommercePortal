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

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.commerceservices.externaltax.CalculateExternalTaxesStrategy;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.externaltax.ExternalTaxDocument;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
	/**
	 *
	 */
	private static final String GGL_ITEMTAX_CODE = "ggl.itemtax.code";

	@Resource(name = "gallagherSovosService")
	private GallagherSovosService gallagherSovosService;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "modelService")
	private ModelService modelService;

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

		abstractOrder.setTaxTransactionDocId(response.getTxwTrnDocId());

		if (CollectionUtils.isNotEmpty(response.getLnRslts()))
		{
			for (final GallagherSovosCalculatedTaxLineItem lienItem : response.getLnRslts())
			{
				final List<TaxValue> taxValues = new ArrayList<>();

				if (CollectionUtils.isNotEmpty(lienItem.getJurRslts()))
				{
					for (final GallagherSovosCalculatedTax calculatedTax : lienItem.getJurRslts())
					{
						final String taxCode = siteConfigService.getString(GGL_ITEMTAX_CODE.concat(calculatedTax.getTxJurUIDJurTp()),
								StringUtils.EMPTY);
						taxValues.add(getTaxValue(abstractOrder, taxCode, calculatedTax.getTxRate(), calculatedTax.getTxAmt()));
					}
				}
				else if (StringUtils.isNotEmpty(lienItem.getTxAmt()) && Double.valueOf(lienItem.getTxAmt()) == 0.0)
				{
					taxValues.add(getTaxValue(abstractOrder, "1", "0", lienItem.getTxAmt()));

				}

				externalDocument.setTaxesForOrderEntry(Integer.valueOf(lienItem.getLnId()), taxValues);
			}
		}

		updateAddressGeoCode(abstractOrder.getDeliveryAddress());
		updateAddressGeoCode(abstractOrder.getPaymentAddress());

		final List<WarehouseModel> warehouses = abstractOrder.getStore().getWarehouses();
		if (CollectionUtils.isNotEmpty(warehouses))
		{
			final Collection<PointOfServiceModel> pointOfServices = warehouses.get(0).getPointsOfService();

			if (CollectionUtils.isNotEmpty(pointOfServices))
			{
				final AddressModel sFAddress = pointOfServices.iterator().next().getAddress();

				if (null != sFAddress)
				{
					updateAddressGeoCode(sFAddress);
				}
			}

		}

		return externalDocument;
	}


	/**
	 * Returns tax value for the data received from Sovos
	 *
	 * @param abstractOrder
	 *
	 * @param taxJurisdictionCode
	 *           of the returned tax
	 * @param taxCode
	 *           taxCode
	 * @param taxRate
	 *           rate of tax
	 * @param amount
	 *           total calculated tax for this entry
	 * @return Tax Value
	 */
	private TaxValue getTaxValue(final AbstractOrderModel abstractOrder, final String taxCode, final String taxRate,
			final String amount)
	{
		final CurrencyModel currency = abstractOrder.getCurrency();

		final StringBuilder taxValueString = new StringBuilder();
		taxValueString.append(taxCode);
		//		if (StringUtils.isNotEmpty(taxJurisdictionCode))
		//		{
		//			taxValueString.append("_").append(taxJurisdictionCode);
		//		}
		taxValueString.append(" : ").append(Double.valueOf(taxRate) * 100);
		final Double taxAmount = Double.valueOf(amount);
		final TaxValue taxValue = new TaxValue(taxValueString.toString(), taxAmount, true, taxAmount,
				currency == null ? "USD" : currency.getIsocode());
		return taxValue;
	}

	/**
	 * Gallagher implementation to update geoCode of address
	 */
	private void updateAddressGeoCode(final AddressModel address)
	{
		if (address != null && StringUtils.isEmpty(address.getGeoCode()))
		{
			address.setGeoCode(gallagherSovosService.getGeoCode(address));
			modelService.save(address);
		}
	}

}
