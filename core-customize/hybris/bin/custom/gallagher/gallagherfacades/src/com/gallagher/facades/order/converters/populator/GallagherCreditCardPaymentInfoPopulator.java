package com.gallagher.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.CreditCardPaymentInfoPopulator;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;


/**
 * Overrides default populator
 *
 * @author Vikram Bishnoi
 */
public class GallagherCreditCardPaymentInfoPopulator extends CreditCardPaymentInfoPopulator
{


	@Override
	public void populate(final CreditCardPaymentInfoModel source, final CCPaymentInfoData target)
	{
		super.populate(source, target);
		target.setCardNumber(getMaskedCardNumber(source.getNumber()));
	}

	/**
	 * Masks card number.
	 *
	 * @param origCardNumber
	 *           to be maksed
	 * @return masked card number
	 */
	private String getMaskedCardNumber(final String origCardNumber)
	{
		String cardNumber = origCardNumber;
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(origCardNumber))
		{
			String lastFour = null;
			boolean maskingNeeded = false;
			cardNumber = origCardNumber.toUpperCase();

			if (origCardNumber.length() == 25 && origCardNumber.indexOf("-E") == 0)
			{
				lastFour = origCardNumber.substring(6, 10);
				maskingNeeded = true;
			}
			else if ((origCardNumber.length() == 15 || origCardNumber.length() == 16) && origCardNumber.indexOf("****") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}
			else if (origCardNumber.length() == 14 && origCardNumber.indexOf("7") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}
			else if (origCardNumber.length() == 16 && origCardNumber.indexOf("8") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}
			else if (origCardNumber.length() == 16 && origCardNumber.indexOf("11") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}
			else if (origCardNumber.length() == 17 && origCardNumber.indexOf("T") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}
			else if (origCardNumber.length() == 19 && origCardNumber.indexOf("8") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}
			else if (origCardNumber.length() == 19 && origCardNumber.indexOf("11") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}
			else if (origCardNumber.length() == 19 && origCardNumber.indexOf("T") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}
			else if (origCardNumber.length() == 20 && origCardNumber.indexOf("8") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}
			else if (origCardNumber.length() == 20 && origCardNumber.indexOf("T") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}
			else if (origCardNumber.length() == 24 && origCardNumber.indexOf("804424") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}
			else if (origCardNumber.length() == 25 && origCardNumber.indexOf("T") == 0)
			{
				lastFour = origCardNumber.substring(origCardNumber.length() - 4);
				maskingNeeded = true;
			}

			if (maskingNeeded && org.apache.commons.lang3.StringUtils.isNotEmpty(lastFour))
			{
				cardNumber = "************" + lastFour;
			}
		}
		return cardNumber;
	}
}
