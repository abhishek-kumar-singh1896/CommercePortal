/**
 *
 */
package com.gallagher.facades.order.converters.populator;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorfacades.order.populators.B2BCartPopulator;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;


/**
 *
 */
public class GallagherB2BCartPopulator<T extends CartData> extends B2BCartPopulator<T>
{

	private Converter<B2BUnitModel, B2BUnitData> b2BUnitConverter;

	@Override
	public void populate(final CartModel source, final T target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		addDeliveryAddress(source, target);
		addDeliveryMethod(source, target);

		for (final AbstractOrderEntryModel entry : source.getEntries())
		{
			if (entry.getB2bUnit() != null)
			{
				target.setB2bUnit(getB2BUnitConverter().convert(entry.getB2bUnit()));
				break;
			}
		}

		target.setRequiredDeliveryDate(source.getRequiredDeliveryDate());
		target.setPurchaseOrderNumber(source.getPurchaseOrderNumber());
		final CheckoutPaymentType paymentType = CheckoutPaymentType.ACCOUNT;

		target.setPaymentType(getB2bPaymentTypeConverter().convert(paymentType));

		if (!CheckoutPaymentType.ACCOUNT.equals(paymentType))
		{
			addPaymentInformation(source, target);
		}

		if (CollectionUtils.isNotEmpty(source.getB2bcomments()))
		{
			target.setB2BComment(getB2BCommentConverter().convert(source.getB2bcomments().iterator().next()));
		}
		target.setB2bCustomerData(getB2bCustomerConverter().convert(source.getUser()));
		target.setQuoteAllowed(Boolean.valueOf(getB2bOrderService().isQuoteAllowed(source)));
		target.setSalesArea(StringUtils.isNotBlank(source.getSalesArea()) ? source.getSalesArea() : StringUtils.EMPTY);
	}


	public Converter<B2BUnitModel, B2BUnitData> getB2BUnitConverter()
	{
		return b2BUnitConverter;
	}

	public void setB2BUnitConverter(final Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter)
	{
		b2BUnitConverter = b2bUnitConverter;
	}

}
