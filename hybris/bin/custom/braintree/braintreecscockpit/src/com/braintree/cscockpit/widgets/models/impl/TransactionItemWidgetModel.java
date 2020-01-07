package com.braintree.cscockpit.widgets.models.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;

import org.springframework.util.ObjectUtils;

import com.braintree.model.BrainTreeTransactionDetailModel;


public class TransactionItemWidgetModel extends DefaultMasterDetailListWidgetModel<TypedObject>
{
	private TypedObject transaction;

	public TransactionItemWidgetModel()
	{
	}

	public TypedObject getTransaction()
	{
		return transaction;
	}

	private BrainTreeTransactionDetailModel getTransaction(final TypedObject transaction)
	{
		return transaction.getObject() instanceof BrainTreeTransactionDetailModel ? (BrainTreeTransactionDetailModel) transaction
				.getObject() : null;
	}

	public boolean setTransaction(final TypedObject transaction)
	{
		boolean changed = false;
		if (this.transaction != null && transaction != null)
		{
			final BrainTreeTransactionDetailModel orderA = this.getTransaction(this.transaction);
			final BrainTreeTransactionDetailModel orderB = this.getTransaction(transaction);
			changed = !ObjectUtils.nullSafeEquals(orderA.getId(), orderB.getId());
		}

		if (!ObjectUtils.nullSafeEquals(this.transaction, transaction))
		{
			changed = true;
			this.transaction = transaction;
		}

		return changed;
	}

}
