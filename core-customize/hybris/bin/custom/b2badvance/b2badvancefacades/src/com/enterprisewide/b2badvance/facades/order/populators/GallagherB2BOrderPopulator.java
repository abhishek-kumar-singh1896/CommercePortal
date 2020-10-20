/**
 *
 */
package com.enterprisewide.b2badvance.facades.order.populators;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorfacades.order.populators.B2BOrderPopulator;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.collections.CollectionUtils;


/**
 *
 */
public class GallagherB2BOrderPopulator extends B2BOrderPopulator
{
	private Converter<B2BUnitModel, B2BUnitData> b2BUnitConverter;



	public Converter<B2BUnitModel, B2BUnitData> getB2BUnitConverter()
	{
		return b2BUnitConverter;
	}


	public void setB2BUnitConverter(final Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter)
	{
		b2BUnitConverter = b2bUnitConverter;
	}


	private Converter<UserModel, CustomerData> b2bCustomerConverter;


	@Override
	public void populate(final OrderModel orderModel, final OrderData orderData) throws ConversionException
	{
		for (final AbstractOrderEntryModel entry : orderModel.getEntries())
		{
			if (entry.getB2bUnit() != null)
			{
				orderData.setB2bUnit(getB2BUnitConverter().convert(entry.getB2bUnit()));
				break;
			}
		}

		if (orderModel.getDeliveryInstructions() != null)
		{
			orderData.setDeliveryInstructions(orderModel.getDeliveryInstructions());
		}

		orderData.setRequiredDeliveryDate(orderModel.getRequiredDeliveryDate());
		orderData.setPurchaseOrderNumber(orderModel.getPurchaseOrderNumber());
		orderData.setPaymentType(getB2bPaymentTypeConverter().convert(orderModel.getPaymentType()));
		if (CollectionUtils.isNotEmpty(orderModel.getB2bcomments()))
		{
			//Always get the latest comment
			orderData.setB2BComment(getB2BCommentConverter().convert(filterOutLastComment(orderModel)));
		}

		populatePermissionResults(orderModel, orderData);

		orderData.setB2bCustomerData(b2bCustomerConverter.convert(orderModel.getUser()));

		if (orderModel.getSchedulingCronJob() != null)
		{
			orderData.setJobCode(orderModel.getSchedulingCronJob().getCode());
			if (CollectionUtils.isNotEmpty(orderModel.getSchedulingCronJob().getTriggers()))
			{
				orderData.setTriggerData(
						getTriggerConverter().convert(orderModel.getSchedulingCronJob().getTriggers().iterator().next()));
			}
		}
		orderData.setQuoteExpirationDate(orderModel.getQuoteExpirationDate());
		orderData.setB2bCommentData(Converters.convertAll(orderModel.getB2bcomments(), getB2BCommentConverter()));
	}




	@Override
	public Converter<UserModel, CustomerData> getB2bCustomerConverter()
	{
		return b2bCustomerConverter;
	}


	@Override
	public void setB2bCustomerConverter(final Converter<UserModel, CustomerData> b2bCustomerConverter)
	{
		this.b2bCustomerConverter = b2bCustomerConverter;
	}

}
