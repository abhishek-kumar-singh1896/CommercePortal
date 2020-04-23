/**
 *
 */
package com.gallagher.b2c.util;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(GallagherProductRegistrationUtil.class);

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

		final SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat requestDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		try
		{
			request.setReferenceDate(requestDateFormat.format(inputDateFormat.parse(registerProductForm.getDatePurchased())));
		}
		catch (final ParseException exception)
		{
			LOGGER.error("Parse Exception occured while parsing Date Purchased.", exception);
		}

		request.setAddressLine1(registerProductForm.getAddressLine1());
		request.setAddressLine2(registerProductForm.getAddressLine2());
		request.setCity(registerProductForm.getTownCity());
		request.setDistrict(registerProductForm.getRegion());
		request.setPostalCode(registerProductForm.getPostCode());
		request.setCountry(registerProductForm.getCountry());

		request.setStatus("1");

		if (user instanceof CustomerModel)
		{
			final CustomerModel customer = (CustomerModel) user;

			final RegisteredProductPartyInformation customerInfo = new RegisteredProductPartyInformation();
			customerInfo.setPartyID(customer.getCustomerID());

			customerInfo.setRoleCode("60");

			request.setRegisteredProductPartyInformation(Collections.singletonList(customerInfo));
		}

		final MultipartFile file = registerProductForm.getAttachedFile();

		if (null != file && !file.isEmpty())
		{
			final RegisteredProductAttachmentFolder attachmentFolder = new RegisteredProductAttachmentFolder();

			attachmentFolder.setName(file.getOriginalFilename());
			attachmentFolder.setMimeType(file.getContentType());

			try
			{
				final byte[] fileData = Base64.encodeBase64(file.getBytes());
				attachmentFolder.setBinary(new String(fileData));
			}
			catch (final IOException exception)
			{
				LOGGER.error("IO Exception occured while converting File into Binary.", exception);
			}

			attachmentFolder.setTypeCode("10001");

			request.setRegisteredProductAttachmentFolder(Collections.singletonList(attachmentFolder));
		}

	}

}
