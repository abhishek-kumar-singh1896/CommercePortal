/**
 *
 */
package com.enterprisewide.b2badvance.facades.bulkorder.converter.populator;

import com.enterprisewide.b2badvance.bulkorder.data.BulkOrderCartItemData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 */
public class BulkOrderCartItemErrorPopulator implements Populator<CartModificationData, BulkOrderCartItemData>
{

	private static final String MSG_KEY_NO_ITEMS = "noItemsAdded";
	private static final String MSG_KEY_REDUCED_NUMBER = "reducedNumberOfItemsAdded";
	private static final String MSG_KEY_SUCCESS = "success";

	@Override
	public void populate(final CartModificationData source, final BulkOrderCartItemData target) throws ConversionException
	{
		if (!source.getStatusCode().equals(MSG_KEY_SUCCESS))
		{
			target.setErrorMsg(getErrorMessageKey(source));
		}
	}

	private String getErrorMessageKey(final CartModificationData cartModificationData)
	{
		final String statusCode = cartModificationData.getStatusCode();
		if (!isKeyMessage(statusCode))
		{
			return cartModificationData.getStatusCode();
		}

		String messageKey = "basket.information.quantity.";
		if (cartModificationData.getQuantityAdded() == 0)
		{
			messageKey += MSG_KEY_NO_ITEMS + "." + cartModificationData.getStatusCode();
		}
		else if (cartModificationData.getQuantityAdded() < cartModificationData.getQuantity())
		{
			messageKey += MSG_KEY_REDUCED_NUMBER + "." + cartModificationData.getStatusCode();
		}
		else
		{
			messageKey = cartModificationData.getStatusCode();
		}

		return messageKey;
	}

	private boolean isKeyMessage(final String messageToCheck)
	{
		final Pattern pattern = Pattern.compile("\\s");
		final Matcher matcher = pattern.matcher(messageToCheck);
		return !matcher.find();
	}

}
