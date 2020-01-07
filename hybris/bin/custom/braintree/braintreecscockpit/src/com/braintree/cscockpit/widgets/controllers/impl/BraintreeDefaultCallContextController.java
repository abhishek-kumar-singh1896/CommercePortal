package com.braintree.cscockpit.widgets.controllers.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultCallContextController;

import java.util.Collections;
import java.util.List;

import com.braintree.cscockpit.widgets.controllers.BraintreeCallContextController;
import com.braintree.customer.service.BrainTreeCustomerAccountService;
import com.braintree.model.BrainTreePaymentInfoModel;


public class BraintreeDefaultCallContextController extends DefaultCallContextController implements BraintreeCallContextController
{
	private BrainTreeCustomerAccountService customerAccountService;
	private TypedObject activeTransaction;
	private TypedObject activeCustomer;
	private TypedObject activePaymentMethod;
	private TypedObject activeAddress;

	@Override
	public TypedObject getCurrentTransaction()
	{
		return this.activeTransaction;
	}

	@Override
	public boolean setCurrentTransaction(final TypedObject transaction)
	{
		boolean changed = false;
		if (!transaction.equals(this.activePaymentMethod))
		{
			this.activeTransaction = transaction;
			this.clearCurrentOrder();
			this.clearCurrentTicket();
			this.setImpersonationContextChanged();
			changed = true;
		}
		return changed;
	}

	@Override
	public void setImpersonationContextChanged()
	{
		super.setImpersonationContextChanged();
	}

	@Override
	public TypedObject getCurrentBTCustomer()
	{
		return activeCustomer;
	}

	@Override
	public boolean setCurrentBTCustomer(final TypedObject customer)
	{
		if (this.activeCustomer == null || !this.activeCustomer.equals(customer))
		{
			this.activeCustomer = customer;
		}
		this.setImpersonationContextChanged();
		return true;
	}

	@Override
	public TypedObject getCurrentPaymentMethod()
	{
		return activePaymentMethod;
	}

	@Override
	public boolean setCurrentPaymentMethod(final TypedObject paymentMethod)
	{
		boolean changed = false;
		if (this.activePaymentMethod == null || !this.activePaymentMethod.equals(paymentMethod))
		{
			this.activePaymentMethod = paymentMethod;
			this.setImpersonationContextChanged();
			changed = true;
		}
		return changed;
	}

	@Override
	public TypedObject getCurrentAddress()
	{
		return activeAddress;
	}

	@Override
	public boolean setCurrentAddress(final TypedObject address)
	{
		boolean changed = false;
		if (this.activeAddress == null || !this.activeAddress.equals(address))
		{
			this.activeAddress = address;
			this.setImpersonationContextChanged();
			changed = true;
		}
		return changed;
	}

	@Override
	public List<TypedObject> getBrainTreePaymentInfos()
	{
		TypedObject customerTyped = this.getCurrentCustomer();
		if (customerTyped != null && customerTyped.getObject() instanceof CustomerModel)
		{
			CustomerModel customer = (CustomerModel) customerTyped.getObject();
			List<BrainTreePaymentInfoModel> validSavedCards = getCustomerAccountService().getBrainTreePaymentInfos(customer, true);
			if (!validSavedCards.isEmpty())
			{
				return this.getCockpitTypeService().wrapItems(validSavedCards);
			}
		}

		return Collections.emptyList();
	}


	@Override
	public BrainTreeCustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	public void setCustomerAccountService(BrainTreeCustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}
}
