package com.braintree.transaction.service.impl;

import com.braintree.constants.BraintreeConstants;
import com.braintree.model.BrainTreeTransactionDetailModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.braintree.transaction.service.BrainTreePaymentTransactionService;


public class BrainTreePaymentTransactionServiceImpl implements BrainTreePaymentTransactionService
{
	private final String ORDER_PROCESS_PREFIX = "order-process";
	private final String ORDER_PROCESS_SUFFIX = "_SubmitForSettlementEvent";

	private FlexibleSearchService flexibleSearchService;
	private BusinessProcessService businessProcessService;
	private ModelService modelService;

	@Override
	public List<PaymentTransactionModel> getTransactionsByRequestIdAndPaymentProvider(String requestId, String paymentProvider) {
		final PaymentTransactionModel transactionModel = new PaymentTransactionModel();

		transactionModel.setRequestId(requestId);
		transactionModel.setPaymentProvider(paymentProvider);

		return flexibleSearchService.getModelsByExample(transactionModel);
	}

	@Override
	public PaymentTransactionModel getTransactionByRequestIdAndPaymentProvider(final String requestId, final String paymentProvider)
	{
		final List<PaymentTransactionModel> paymentTransactionModels = getTransactionsByRequestIdAndPaymentProvider(requestId, paymentProvider);
		return paymentTransactionModels == null || paymentTransactionModels.isEmpty() ? null : paymentTransactionModels.get(0);
	}

	@Override
	public void continueSubmitOrder(BrainTreeTransactionDetailModel currentTransaction, BigDecimal amount) {
		PaymentTransactionModel paymentTransactionModel = getTransactionByRequestIdAndPaymentProvider(currentTransaction.getId(), BraintreeConstants.BRAINTREE_PROVIDER_NAME);

		if (paymentTransactionModel != null)
		{
			resumeOrderProcess(paymentTransactionModel, currentTransaction, amount);
		}
	}


	private void resumeOrderProcess(PaymentTransactionModel paymentTransactionModel, BrainTreeTransactionDetailModel currentTransaction, BigDecimal amount)
	{
		OrderModel order = (OrderModel) paymentTransactionModel.getOrder();
		setAmount(paymentTransactionModel, amount);

		OrderProcessModel orderProcess = getOrderProcess(order);

		if (orderProcess != null)
		{
			getBusinessProcessService().triggerEvent(orderProcess.getCode() + ORDER_PROCESS_SUFFIX);
		}

	}

	@Override
	public void resumeOrderProcess(OrderModel order)
	{
		if (order != null) {
			OrderProcessModel orderProcess = getOrderProcess(order);

			if (orderProcess != null) {
				getBusinessProcessService().triggerEvent(orderProcess.getCode() + ORDER_PROCESS_SUFFIX);
			}
		}
	}

	private OrderProcessModel getOrderProcess(OrderModel order)
	{
		Collection<OrderProcessModel> orderProcess = order.getOrderProcess();
		for (OrderProcessModel processModel: orderProcess) {
			if (ORDER_PROCESS_PREFIX.equals(processModel.getProcessDefinitionName()))
			{
				return processModel;
			}
		}
		return null;
	}

	private void setAmount(PaymentTransactionModel paymentTransactionModel, BigDecimal amount)
	{
		for (PaymentTransactionEntryModel entry: paymentTransactionModel.getEntries()) {
			if (PaymentTransactionType.AUTHORIZATION.equals(entry.getType()))
			{
				entry.setSubmittedForSettlementAmount(amount);
				modelService.save(entry);
			}
		}
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	public void setBusinessProcessService(BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}
}
