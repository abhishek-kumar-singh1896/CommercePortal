/**
 *
 */
package com.gallagher.b2c.util;

import de.hybris.platform.core.model.user.UserModel;

import java.util.Collections;

import com.gallagher.b2c.form.RegisterProductPopupForm;
import com.gallagher.outboundservices.request.dto.RegisterProductRequest;
import com.gallagher.outboundservices.request.dto.RegisteredProductPartyInformation;


/**
 * @author shishirkant
 */
public class GallagherProductRegistrationUtil
{

	/**
	 * @param user
	 * @param registerProductForm1
	 * @param register
	 */
	public static void convert(final RegisterProductPopupForm registerProductForm, final RegisterProductRequest request,
			final UserModel user)
	{
		request.setSerialID(registerProductForm.getSerialNumber1());
		request.setProductID(registerProductForm.getProductSku1());

		final RegisteredProductPartyInformation customerInfo = new RegisteredProductPartyInformation();
		customerInfo.setPartyID(user.getUid());

		request.setRegisteredProductPartyInformation(Collections.singletonList(customerInfo));

	}

}
