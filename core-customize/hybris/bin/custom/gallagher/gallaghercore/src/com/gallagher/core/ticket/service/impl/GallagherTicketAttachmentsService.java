/**
 *
 */
package com.gallagher.core.ticket.service.impl;

import de.hybris.platform.ticket.service.UnsupportedAttachmentException;
import de.hybris.platform.ticket.service.impl.DefaultTicketAttachmentsService;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;


/**
 * @author ankituniyal
 *
 */

public class GallagherTicketAttachmentsService extends DefaultTicketAttachmentsService
{
	@Resource(name = "invalidUploadedFormats")
	private String invalidUploadedFormats;

	@Override
	protected void checkFileExtension(final String name) {

		if (FilenameUtils.isExtension(name.toLowerCase(), invalidUploadedFormats.replaceAll("\\s", "").toLowerCase().split(",")))
		{

          throw new UnsupportedAttachmentException(String.format("File %s has unsupported extension. Only [%s] allowed.", name, this.getAllowedUploadedFormats()));

       }
    }
}
