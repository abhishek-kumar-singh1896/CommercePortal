/**
 *
 */
package com.braintree.controllers.handler;

import static com.braintree.constants.Braintreeb2baddonWebConstants.PAY_PAL_RESPONSE;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
//import static org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES;
import com.fasterxml.jackson.databind.DeserializationFeature;

import com.braintree.hybris.data.PayPalDetails;
import de.hybris.platform.commercefacades.user.data.AddressData;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
//import org.codehaus.jackson.map.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.braintree.hybris.data.PayPalAddressData;
import com.braintree.hybris.data.PayPalExpressResponse;
import com.braintree.paypal.converters.impl.PayPalAddressDataConverter;


@Component
public class PayPalResponseExpressCheckoutHandler
{

	@Resource(name = "payPalAddressDataConverter")
	private PayPalAddressDataConverter payPalAddressDataConverter;

	public PayPalExpressResponse handlePayPalResponse(final HttpServletRequest request) throws IllegalArgumentException
	{
		validateParameterNotNull(request, "request cannot be null");

		final String[] jsonResponseArray = request.getParameterMap().get(PAY_PAL_RESPONSE);

		final String responseJson = jsonResponseArray[0];
		PayPalExpressResponse response = null;

		if (isNotEmpty(responseJson))
		{
			//final ObjectMapper mapper = new ObjectMapper().disable(FAIL_ON_UNKNOWN_PROPERTIES);
			final ObjectMapper mapper = new ObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			try
			{
				response = mapper.readValue(responseJson, PayPalExpressResponse.class);
			}
			catch (final Exception e)
			{
				throw new IllegalArgumentException(
						"Pay Pal express checkout response is invalid!. Please try again later or contact with payment provider support.");
			}
		}
		else
		{
			throw new IllegalArgumentException(
					"Pay Pal express checkout response is empty! Please try again later or contact with payment provider support.");
		}
		return response;
	}

    public AddressData getPayPalAddress(PayPalDetails details, final PayPalAddressData address)
    {
        final AddressData addressData = payPalAddressDataConverter.convert(address);
        addressData.setEmail(details.getEmail());
        if (StringUtils.isBlank(address.getRecipientName()))
        {
            addressData.setFirstName(details.getFirstName());
            addressData.setLastName(details.getLastName());
        }
        return addressData;
    }

}
