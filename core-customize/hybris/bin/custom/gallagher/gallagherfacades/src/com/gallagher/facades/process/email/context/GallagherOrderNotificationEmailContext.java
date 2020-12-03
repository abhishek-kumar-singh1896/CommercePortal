/**
 *
 */
package com.gallagher.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import com.enterprisewide.b2badvance.facades.process.email.context.OrderNotificationEmailContext;


/**
 *
 */
public class GallagherOrderNotificationEmailContext extends OrderNotificationEmailContext
{

	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;



	@Override
	public Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	@Override
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		orderData = getOrderConverter().convert(orderProcessModel.getOrderEntry());
		getOrder().setFormattedOrderDate(orderProcessModel.getFormattedOrderDate());
		put(EMAIL, getCustomerEmailResolutionService().getEmailForCustomer(getCustomer(orderProcessModel)));
		put(DISPLAY_NAME, getCustomer(orderProcessModel).getDisplayName());

	}

	@Override
	public OrderData getOrder()
	{
		orderData = super.getOrder();
		return orderData;
	}

	@Override
	protected BaseSiteModel getSite(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrderEntry().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final OrderProcessModel orderProcessModel)
	{
		return (CustomerModel) orderProcessModel.getOrderEntry().getUser();
	}

	@Override
	protected LanguageModel getEmailLanguage(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrderEntry().getLanguage();
	}

}
