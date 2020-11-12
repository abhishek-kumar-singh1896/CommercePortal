/**
 *
 */
package com.gallagher.facades.checkout.flow.impl;

import de.hybris.platform.b2b.model.B2BCommentModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BCheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Objects;


/**
 * @author abhisheksingh06
 *
 */
public class GallagherDefaultB2BCheckoutFacade extends DefaultB2BCheckoutFacade
{

	private B2BUnitService<B2BUnitModel, UserModel> b2BUnitService;

	@Override
	public CartData updateCheckoutCart(final CartData cartData)
	{
		final CartModel cartModel = getCart();
		if (cartModel == null)
		{
			return null;
		}
		// set payment type
		if (cartData.getPaymentType() != null)
		{
			final String newPaymentTypeCode = cartData.getPaymentType().getCode();

			// clear delivery address, delivery mode and payment details when changing payment type
			if (cartModel.getPaymentType() == null || !newPaymentTypeCode.equalsIgnoreCase(cartModel.getPaymentType().getCode()))
			{
				cartModel.setDeliveryAddress(null);
				cartModel.setDeliveryMode(null);
				cartModel.setPaymentInfo(null);
			}

			setPaymentTypeForCart(newPaymentTypeCode, cartModel);
			// cost center need to be be cleared if using card
			/*
			 * if (CheckoutPaymentType.CARD.getCode().equals(newPaymentTypeCode)) { setCostCenterForCart(null, cartModel);
			 * }
			 */
		}

		// set B2b Unit
		if (cartData.getB2bUnit() != null)
		{
			setB2bUnitForCart(cartData.getB2bUnit().getCode(), cartModel);
		}

		// set purchase order number
		if (cartData.getPurchaseOrderNumber() != null)
		{
			cartModel.setPurchaseOrderNumber(cartData.getPurchaseOrderNumber());
		}

		// set delivery address
		if (cartData.getDeliveryAddress() != null)
		{
			setDeliveryAddress(cartData.getDeliveryAddress());
		}

		if (cartData.getRequiredDeliveryDate() != null)
		{
			setRequiredDeliveryDate(cartData.getRequiredDeliveryDate(), cartModel);
		}


		// set quote request description
		if (cartData.getB2BComment() != null)
		{
			final B2BCommentModel b2bComment = getModelService().create(B2BCommentModel.class);
			b2bComment.setComment(cartData.getB2BComment().getComment());
			getB2bCommentService().addComment(cartModel, b2bComment);
		}

		getModelService().save(cartModel);
		return getCheckoutCart();

	}


	/**
	 * @param userComments
	 */


	protected void setB2bUnitForCart(final String b2bUnitDataCode, final CartModel cartModel)
	{
		final B2BPaymentTypeData paymentType = getCheckoutCart().getPaymentType();

		B2BUnitModel b2bUnitModel = null;

		/*
		 * final B2BCostCenterModel costCenterModel = null;
		 */
		if (b2bUnitDataCode != null)
		{
			b2bUnitModel = getB2BUnitService().getUnitForUid(b2bUnitDataCode);
		}

		for (final AbstractOrderEntryModel abstractOrderEntry : cartModel.getEntries())
		{
			if (!Objects.equals(abstractOrderEntry.getB2bUnit(), b2bUnitModel))
			{
				abstractOrderEntry.setB2bUnit(b2bUnitModel);
				getModelService().save(abstractOrderEntry);
			}
		}

		removeDeliveryAddress();
		removeDeliveryMode();
	}

	protected void setRequiredDeliveryDate(final String requiredDate, final CartModel cartModel)
	{
		cartModel.setRequiredDeliveryDate(requiredDate);
		getModelService().save(cartModel);
		getModelService().refresh(cartModel);
	}


	public B2BUnitService<B2BUnitModel, UserModel> getB2BUnitService()
	{
		return b2BUnitService;
	}


	public void setB2BUnitService(final B2BUnitService<B2BUnitModel, UserModel> b2bUnitService)
	{
		b2BUnitService = b2bUnitService;
	}
}
