/**
 *
 */
package com.enterprisewide.b2badvance.facades.product.populators;

import de.hybris.platform.commercefacades.product.converters.populator.ProductPopulator;
import de.hybris.platform.commercefacades.product.data.DiscountData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.facades.constants.B2badvanceFacadesConstants;


/**
 * Populates product information
 *
 * @author Enterprise Wide
 */
public class B2badvanceProductPopulator extends ProductPopulator
{
	private SessionService sessionService;

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @description This method is overridden from ProductPopulator to display the promotions price
	 */
	@Override
	public void populate(final ProductModel source, final ProductData target)
	{

		super.populate(source, target);

		target.setShortDescription(source.getShortDescription());
		target.setPartNumber(source.getPartNumber());
		if (source instanceof VariantProductModel)
		{
			target.setBaseProductName(((VariantProductModel) source).getBaseProduct().getName());
		}
		final List<DiscountData> discountRow = new ArrayList<DiscountData>();

		for (final DiscountRowModel promotionPriceRowModel : source.getEurope1Discounts())
		{
			final CurrencyModel currencyModel = sessionService.getCurrentSession().getAttribute(B2badvanceFacadesConstants.CURRENCY);
			final String sessionCurrencySymbol = currencyModel.getSymbol();
			if (promotionPriceRowModel.getCurrency() != null && promotionPriceRowModel.getCurrency().getSymbol() != null)
			{
				if (!promotionPriceRowModel.getCurrency().getSymbol().equalsIgnoreCase(sessionCurrencySymbol))
				{
					continue;
				}
			}

			final DiscountData discountData = new DiscountData();
			if (promotionPriceRowModel.getDiscountString() != null)
			{

				for (final PriceRowModel priceRowModel : source.getEurope1Prices())
				{

					if (priceRowModel.getCurrency() != null && priceRowModel.getCurrency().getSymbol() != null)
					{
						if (!priceRowModel.getCurrency().getSymbol().equalsIgnoreCase(sessionCurrencySymbol))
						{
							discountData.setSameCurrency(false);
							continue;
						}
						else
						{
							discountData.setSameCurrency(true);
						}
					}
					Double discountPrice = promotionPriceRowModel.getDiscount().getValue();

					Double netPrice;
					if (discountPrice != null && priceRowModel.getPrice() != null)
					{
						if (promotionPriceRowModel.getAbsolute())
						{

							netPrice = priceRowModel.getPrice() - discountPrice;
						}
						else
						{
							netPrice = priceRowModel.getPrice() - (priceRowModel.getPrice() * (discountPrice / 100));
						}
						final String netProductValue = priceRowModel.getCurrency().getSymbol() + String.format("%.2f", netPrice);
						discountData.setValue(netProductValue);
						//break;
					}

				}
				discountData.setDiscountString(promotionPriceRowModel.getDiscountString());
				discountRow.add(discountData);
				break;
			}

		}
		target.setTotalDiscounts(discountRow);

	}
}