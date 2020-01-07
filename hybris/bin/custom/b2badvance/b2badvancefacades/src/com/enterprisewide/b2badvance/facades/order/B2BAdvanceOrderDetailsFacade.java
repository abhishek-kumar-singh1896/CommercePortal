/**
 *
 */
package com.enterprisewide.b2badvance.facades.order;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BOrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.enterprisewide.b2badvance.facades.order.data.B2BAdvanceOrderData;

import jersey.repackaged.com.google.common.collect.Lists;


/**
 * @author aayushrohatgi
 *
 */
public class B2BAdvanceOrderDetailsFacade extends DefaultB2BOrderFacade implements B2BAdvanceOrderFacade
{

	private B2BOrderService b2bOrderService;

	private ModelService modelService;

	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceOrderDetailsFacade.class);

	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;

	/*
	 * (non-Javadoc)
	 *
	 * @see au.com.b2bAdvanceXyz.facades.order.B2bAdvanceXyzOrderFacade#getPagedOrderDetailsForStatuses(de.hybris.platform
	 * .commerceservices.search.pagedata.PageableData, de.hybris.platform.core.enums.OrderStatus[])
	 */
	@Override
	public SearchPageData<OrderData> getPagedOrderDetailsForStatuses(final PageableData pageableData,
			final OrderStatus... statuses)
	{
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final SearchPageData<OrderModel> orderResults = getCustomerAccountService().getOrderList(currentCustomer, currentBaseStore,
				statuses, pageableData);

		return convertPageData(orderResults, orderConverter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> saveOrders(final List<B2BAdvanceOrderData> orders)
	{

		getUserService().setCurrentUser(getUserService().getAdminUser());
		final Map<String, String> importErrors = new HashMap<>(orders.size());
		if (CollectionUtils.isNotEmpty(orders))
		{
			Lists.partition(orders, 50).forEach(batchOrder -> {
				try
				{
					final List<OrderModel> orderModels = processOrder(batchOrder, importErrors);
					if (CollectionUtils.isNotEmpty(orderModels))
					{
						modelService.saveAll(orderModels);
					}
				}
				catch (final IllegalArgumentException mIllEx)
				{
					LOG.error("Error no order Models are found", mIllEx);
				}
				catch (final ModelSavingException mSavEx)
				{
					LOG.error("Error while saving batch order", mSavEx);
					//importErrors.put(order.getHybrisOrderNumber(), mSavEx.getMessage());
				}

			});
		}
		return importErrors;
	}

	/**
	 * @param batchOrder
	 * @param importErrors
	 */
	private List<OrderModel> processOrder(final List<B2BAdvanceOrderData> batchOrder, final Map<String, String> importErrors)
	{
		final List<B2BAdvanceOrderData> batchOrder1 = batchOrder;
		final List<OrderModel> orderModels = new ArrayList<OrderModel>();
		batchOrder1.forEach(order -> {
			final OrderModel orderModel = b2bOrderService.getOrderForCode(order.getHybrisOrderNumber());
				if (Objects.nonNull(orderModel) && orderModel.getStatus().getCode() != order.getStatus())
				{
					orderModel.setStatus(OrderStatus.valueOf(order.getStatus()));
					orderModels.add(orderModel);
				}
				else
				{
				importErrors.put(order.getHybrisOrderNumber(), "Hybris order number doesn't exist");
				}
		});
		return orderModels;
	}


	/**
	 * @return the modelService
	 */
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *                        the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the b2bOrderService
	 */
	@Override
	public B2BOrderService getB2bOrderService()
	{
		return b2bOrderService;
	}

	/**
	 * @param b2bOrderService
	 *                           the b2bOrderService to set
	 */
	@Override
	public void setB2bOrderService(final B2BOrderService b2bOrderService)
	{
		this.b2bOrderService = b2bOrderService;
	}

	/**
	 * @return the orderConverter
	 */
	@Override
	public Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	/**
	 * @param orderConverter
	 *                          the orderConverter to set
	 */
	@Override
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}
}
