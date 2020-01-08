/**
 *
 */
package com.braintree.actions.replenishment;

import com.braintree.customfield.service.CustomFieldsService;
import com.enterprisewide.b2badvanceacceleratoraddon.actions.replenishment.AuthorizePaymentAction;
import de.hybris.platform.b2bacceleratorservices.model.process.ReplenishmentProcessModel;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;

import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.transaction.service.BrainTreeTransactionService;


public class BrainTreeAuthorizePaymentAction extends AuthorizePaymentAction
{
	private BrainTreeTransactionService brainTreeTransactionService;

	private CustomFieldsService customFieldsService;

	@Override
	public de.hybris.platform.processengine.action.AbstractSimpleDecisionAction.Transition executeAction(
			final ReplenishmentProcessModel process) throws Exception
	{
		final BusinessProcessParameterModel clonedCartParameter = processParameterHelper.getProcessParameterByName(process, "cart");
		final CartModel clonedCart = (CartModel) clonedCartParameter.getValue();
		getModelService().refresh(clonedCart);

		final ImpersonationContext context = new ImpersonationContext();
		context.setOrder(clonedCart);
		return getImpersonationService().executeInContext(context,
				new ImpersonationService.Executor<Transition, ImpersonationService.Nothing>()
				{
					@Override
					public Transition execute()
					{
						if (clonedCart.getPaymentInfo() instanceof BrainTreePaymentInfoModel)
						{
							final PaymentTransactionEntryModel paymentTransactionEntryModel = brainTreeTransactionService
									.createAuthorizationTransaction(clonedCart, customFieldsService.getDefaultCustomFieldsMap());
							if (paymentTransactionEntryModel == null
									|| !TransactionStatus.ACCEPTED.name().equals(paymentTransactionEntryModel.getTransactionStatus()))
							{
								clonedCart.setStatus(OrderStatus.B2B_PROCESSING_ERROR);
								modelService.save(clonedCart);
								return Transition.NOK;
							}
						}
						return Transition.OK;
					}
				});
	}

	/**
	 * @return the brainTreeTransactionService
	 */
	public BrainTreeTransactionService getBrainTreeTransactionService()
	{
		return brainTreeTransactionService;
	}

	/**
	 * @param brainTreeTransactionService
	 *           the brainTreeTransactionService to set
	 */
	public void setBrainTreeTransactionService(final BrainTreeTransactionService brainTreeTransactionService)
	{
		this.brainTreeTransactionService = brainTreeTransactionService;
	}

	public CustomFieldsService getCustomFieldsService() {
		return customFieldsService;
	}

	public void setCustomFieldsService(CustomFieldsService customFieldsService) {
		this.customFieldsService = customFieldsService;
	}
}
