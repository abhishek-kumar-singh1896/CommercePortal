/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.DiscountData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.DiscountInformation;
import de.hybris.platform.order.DiscountService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.FindDiscountValuesStrategy;
import de.hybris.platform.order.strategies.calculation.pdt.criteria.impl.DefaultDiscountValueInfoCriteria;
import de.hybris.platform.product.BaseCriteria;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.DiscountValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.facades.constants.B2badvanceFacadesConstants;


/**
 * Populate the product data with the price information
 */
public class GallagherProductDiscountPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends AbstractProductPopulator<SOURCE, TARGET>
{
	private static final Logger LOG = Logger.getLogger(GallagherProductDiscountPopulator.class);

	private DiscountService discountService;
	private SessionService sessionService;

	@Autowired
	private FindDiscountValuesStrategy findDiscountValuesStrategy;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		List<DiscountInformation> discountInformations = new ArrayList<>();
		try
		{
			final BaseCriteria pCriteria = DefaultDiscountValueInfoCriteria.buildForInfo().withProduct(productModel)
					.withDate(new Date()).withUser(sessionService.getCurrentSession()
							.getAttribute("user"))
					.withCurrency(sessionService.getCurrentSession().getAttribute("currency")).build();

			discountInformations = discountService.getDiscountInformation(pCriteria);
		}
		catch (final CalculationException e)
		{
			LOG.error("Error while calculating discount " + e.getMessage());
		}

		if (CollectionUtils.isNotEmpty(discountInformations))
		{

			final DiscountInformation info = discountInformations.get(0);
			final PriceData priceData = productData.getPrice();

			if (priceData == null)
			{
				return;
			}

			final DiscountData discountData = new DiscountData();

			final CurrencyModel currencyModel = sessionService.getCurrentSession().getAttribute(B2badvanceFacadesConstants.CURRENCY);
			final String sessionCurrencySymbol = currencyModel.getSymbol();

			final String priceRowCurrencySymbol = priceData.getCurrencySymbol();

			final boolean isSameCurrency = !priceRowCurrencySymbol.equalsIgnoreCase(sessionCurrencySymbol) ? false : true;
			discountData.setSameCurrency(isSameCurrency);

			double netPrice;
			final DiscountValue discountValue = info.getDiscountValue();
			final boolean isAbsolute = discountValue.isAbsolute();
			final boolean asTargetPrice = isAbsolute && Boolean.TRUE.equals(discountValue.isAsTargetPrice());

			if (asTargetPrice)
			{
				netPrice = discountValue.getValue();
			}
			else if (info.getDiscountValue().isAbsolute())
			{

				netPrice = priceData.getValue().doubleValue() - info.getDiscountValue().getValue();
			}
			else
			{
				netPrice = priceData.getValue().doubleValue()
						- (priceData.getValue().doubleValue() * (info.getDiscountValue().getValue() / 100));

				netPrice = netPrice > 0 ? netPrice : 0.0;
			}

			final String netProductValue = priceData.getCurrencySymbol() + String.format("%.2f", netPrice);

			discountData.setValue(netProductValue);
			discountData.setDiscountString(netProductValue);
			productData.setTotalDiscounts(Collections.singletonList(discountData));

		}
		else
		{
			productData.setPurchasable(Boolean.FALSE);
		}
	}

	/**
	 * @return the discountService
	 */
	public DiscountService getDiscountService()
	{
		return discountService;
	}

	/**
	 * @param discountService
	 *           the discountService to set
	 */
	@Required
	public void setDiscountService(final DiscountService discountService)
	{
		this.discountService = discountService;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


}
