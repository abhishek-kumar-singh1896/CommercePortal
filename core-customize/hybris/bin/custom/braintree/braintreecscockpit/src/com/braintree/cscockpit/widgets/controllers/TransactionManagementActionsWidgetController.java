package com.braintree.cscockpit.widgets.controllers;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;


public interface TransactionManagementActionsWidgetController extends WidgetController
{
	boolean isVoidPossible();

	boolean canUserVoid();

	boolean isClonePossible();

	boolean canUserClone();

	boolean isRefundPossible();

	boolean canUserRefund();

	boolean isSubmitForSettlementPossible();

	boolean canUserSubmitForSettlement();

	TypedObject getTransaction();

	void setTransaction(TypedObject typedObject);
}
