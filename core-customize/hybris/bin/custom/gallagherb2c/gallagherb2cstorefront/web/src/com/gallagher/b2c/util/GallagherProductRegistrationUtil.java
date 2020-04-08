/**
 *
 */
package com.gallagher.b2c.util;

import de.hybris.platform.core.model.user.UserModel;

import java.util.Collections;

import org.springframework.web.multipart.MultipartFile;

import com.gallagher.b2c.form.RegisterProductForm;
import com.gallagher.outboundservices.request.dto.RegisterProductRequest;
import com.gallagher.outboundservices.request.dto.RegisteredProductAttachmentFolder;
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
	public static void convert(final RegisterProductForm registerProductForm, final RegisterProductRequest request,
			final UserModel user)
	{
		request.setSerialID(registerProductForm.getSerialNumber());
		request.setProductID(registerProductForm.getProductSku());
		request.setStatus("1");

		final RegisteredProductPartyInformation customerInfo = new RegisteredProductPartyInformation();
		customerInfo.setPartyID("1117845");
		customerInfo.setRoleCode("60");

		request.setRegisteredProductPartyInformation(Collections.singletonList(customerInfo));

		final RegisteredProductAttachmentFolder attachmentFolder = new RegisteredProductAttachmentFolder();

		final MultipartFile file = registerProductForm.getAttachedFile();

		attachmentFolder.setName(file.getName());
		attachmentFolder.setMimeType(file.getContentType());
		attachmentFolder.setBinary("5635645275");
		attachmentFolder.setTypeCode("10001");

		request.setRegisteredProductAttachmentFolder(Collections.singletonList(attachmentFolder));

	}

}
