package com.braintree.cscockpit.widgets.services.chekout;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cscockpit.exceptions.ResourceMessage;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.services.payment.impl.DefaultCsCardPaymentService;
import de.hybris.platform.payment.dto.CardInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BraintreeCsCardPaymentService extends DefaultCsCardPaymentService
{

	@Override
	public List<CreditCardPaymentInfoModel> getPreviousCreditCardPaymentInfos(final UserModel user)
	{
		return Collections.emptyList();
	}

	@Override
	public void validate(final AbstractOrderModel order, final CardInfo cardInfo, final double amount) throws ValidationException
	{
		final List<ResourceMessage> errorMessages = new ArrayList<ResourceMessage>();

		validateBillingInfo(order, cardInfo, errorMessages);
		validateAmount(order, amount, errorMessages);

		if (!errorMessages.isEmpty())
		{
			throw new ValidationException(errorMessages);
		}
	}
}
