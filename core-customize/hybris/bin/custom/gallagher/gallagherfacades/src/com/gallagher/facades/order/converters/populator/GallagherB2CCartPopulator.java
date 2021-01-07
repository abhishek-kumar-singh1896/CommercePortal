/**
 *
 */
package com.gallagher.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.CartPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * @author shilpiverma
 *
 */
public class GallagherB2CCartPopulator<T extends CartData> extends CartPopulator<T>
{

	@Override
	public void populate(final CartModel source, final T target) throws ConversionException
	{
		addCommon(source, target);
		addTotals(source, target);
		addEntries(source, target);
		addPromotions(source, target);
		addSavedCartData(source, target);
		addEntryGroups(source, target);
		addComments(source, target);
		target.setGuid(source.getGuid());
		target.setTotalUnitCount(calcTotalUnitCount(source));

		if (source.getQuoteReference() != null)
		{
			target.setQuoteData(getQuoteConverter().convert(source.getQuoteReference()));
		}
	}

	@Override
	protected void addTotals(final AbstractOrderModel source, final AbstractOrderData prototype)
	{
		final double orderDiscountsAmount = getOrderDiscountsAmount(source);
		final double quoteDiscountsAmount = getQuoteDiscountsAmount(source);

		prototype.setTotalPrice(createPrice(source, source.getTotalPrice()));
		prototype.setTotalTax(createPrice(source, source.getTotalTax()));
		final double subTotal = source.getSubtotal().doubleValue();
		final PriceData subTotalPriceData = createPrice(source, Double.valueOf(subTotal));
		prototype.setSubTotal(subTotalPriceData);
		prototype.setSubTotalWithoutQuoteDiscounts(createPrice(source, Double.valueOf(subTotal + quoteDiscountsAmount)));
		prototype.setDeliveryCost(source.getDeliveryMode() != null ? createPrice(source, source.getDeliveryCost()) : null);
		prototype.setTotalPriceWithTax((createPrice(source, calcTotalWithTax(source))));
	}

}
