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
import de.hybris.platform.cms2.model.site.CMSSiteModel;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.gallagher.core.dtos.GallagherSovosMergedTaxDTO;
import com.gallagher.core.enums.RegionCode;
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
	private static final String CA = "ca";

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
		updateAddressGeoCode(abstractOrder.getDeliveryAddress());
		updateAddressGeoCode(abstractOrder.getPaymentAddress());
		updateWarehouseAddressGeoCode(abstractOrder);

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
				List<TaxValue> taxValues = Collections.EMPTY_LIST;

				if (CollectionUtils.isNotEmpty(lienItem.getJurRslts()))
				{
					taxValues = getTaxValues(abstractOrder, lienItem);
				}
				else if (StringUtils.isNotEmpty(lienItem.getTxAmt()) && Double.valueOf(lienItem.getTxAmt()) == 0.0)
				{
					taxValues = Collections.singletonList(getTaxValue(abstractOrder, "1", 0.0, 0.0));

				}
				externalDocument.setTaxesForOrderEntry(Integer.valueOf(lienItem.getLnId()), taxValues);
			}
		}
		return externalDocument;
	}


	/**
	 * @param abstractOrder
	 * @param lienItem
	 * @param taxValues
	 */
	private List<TaxValue> getTaxValues(final AbstractOrderModel abstractOrder, final GallagherSovosCalculatedTaxLineItem lienItem)
	{
		final List<TaxValue> taxValues = new ArrayList<>(lienItem.getJurRslts().size());
		if (RegionCode.valueOf(CA).equals(((CMSSiteModel) abstractOrder.getSite()).getRegionCode()))
		{
			populateCATaxes(abstractOrder, lienItem, taxValues);
		}
		else
		{
			for (final GallagherSovosCalculatedTax calculatedTax : lienItem.getJurRslts())
			{
				final String taxCode = siteConfigService.getString(GGL_ITEMTAX_CODE.concat(calculatedTax.getTxJurUIDJurTp()),
						StringUtils.EMPTY);
				taxValues.add(getTaxValue(abstractOrder, taxCode, calculatedTax.getTxRate(), calculatedTax.getTxAmt()));
			}
		}
		return taxValues;
	}


	/**
	 * Populates CA taxes. SAPP-691. Taxes for same jurTp are combined. For example, Sovos retruns following two entries:
	 *
	 * JurTp 1:taxRate 0.25:taxAmount 2
	 *
	 * JurTp 1:taxRate 0.1:taxAmount 0.8
	 *
	 * It will make the tax vale as : JurTp 1:taxRate 0.35:taxAmount 2.8
	 *
	 * @param abstractOrder
	 *           order to get the currency etc.
	 * @param lienItem
	 *           to get the retrieved tax values
	 * @param taxValues
	 *           to be populated
	 */
	private void populateCATaxes(final AbstractOrderModel abstractOrder, final GallagherSovosCalculatedTaxLineItem lienItem,
			final List<TaxValue> taxValues)
	{
		final Map<String, GallagherSovosMergedTaxDTO> taxValueMap = new HashMap<>();
		for (final GallagherSovosCalculatedTax calculatedTax : lienItem.getJurRslts())
		{
			if (!taxValueMap.containsKey(calculatedTax.getTxJurUIDJurTp()))
			{
				taxValueMap.put(calculatedTax.getTxJurUIDJurTp(), new GallagherSovosMergedTaxDTO());
			}
			final GallagherSovosMergedTaxDTO existingValue = taxValueMap.get(calculatedTax.getTxJurUIDJurTp());
			existingValue.setTaxRate(existingValue.getTaxRate() == null ? calculatedTax.getTxRate()
					: calculatedTax.getTxAmt() + existingValue.getTaxRate());
			existingValue.setTaxAmount(existingValue.getTaxAmount() == null ? calculatedTax.getTxAmt()
					: calculatedTax.getTxAmt() + existingValue.getTaxAmount());
		}

		taxValueMap.forEach((jurId, calculatedTax) -> {
			final String taxCode = siteConfigService.getString(GGL_ITEMTAX_CODE.concat(jurId), StringUtils.EMPTY);

			taxValues.add(getTaxValue(abstractOrder, taxCode, calculatedTax.getTaxRate(), calculatedTax.getTaxAmount()));

		});
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
	private TaxValue getTaxValue(final AbstractOrderModel abstractOrder, final String taxCode, final Double taxRate,
			final Double amount)
	{
		final CurrencyModel currency = abstractOrder.getCurrency();

		final StringBuilder taxValueString = new StringBuilder();
		taxValueString.append(taxCode);
		taxValueString.append(" : ").append(taxRate * 100);
		final TaxValue taxValue = new TaxValue(taxValueString.toString(), amount, true, amount,
				currency == null ? "USD" : currency.getIsocode());
		return taxValue;
	}

	/**
	 * Gallagher implementation to update geoCode of address
	 *
	 * @param address
	 *           to get the geo code
	 */
	private void updateAddressGeoCode(final AddressModel address)
	{
		if (address != null && StringUtils.isEmpty(address.getGeoCode()))
		{
			address.setGeoCode(gallagherSovosService.getGeoCode(address));
			modelService.save(address);
		}
	}

	/**
	 * Updates the geo code for warehouse address i.e. Ship From Address
	 *
	 * @param abstractOrder
	 *           to get the warehouse address
	 */
	private void updateWarehouseAddressGeoCode(final AbstractOrderModel abstractOrder)
	{
		final List<WarehouseModel> warehouses = abstractOrder.getStore().getWarehouses();
		if (CollectionUtils.isNotEmpty(warehouses))
		{
			final Collection<PointOfServiceModel> pointOfServices = warehouses.get(0).getPointsOfService();

			if (CollectionUtils.isNotEmpty(pointOfServices))
			{
				final AddressModel address = pointOfServices.iterator().next().getAddress();

				if (null != address)
				{
					updateAddressGeoCode(address);
				}
			}

		}

	}

}
