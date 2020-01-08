package com.enterprisewide.b2badvance.facades.product.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.core.model.c2l.CurrencyModel;

import java.math.BigDecimal;


/**
 * Factory that creates PriceData instances for a price values. Includes a formatted price value.
 *
 * @author Enterprise Wide
 */
public class B2BAdvancePriceDataFactoryImpl extends DefaultPriceDataFactory
{

	@Override
	public PriceData create(final PriceDataType priceType, final BigDecimal value, final CurrencyModel currency)
	{

		final PriceData priceData = super.create(priceType, value, currency);
		priceData.setCurrencySymbol(currency.getSymbol());
		return priceData;
	}
}
