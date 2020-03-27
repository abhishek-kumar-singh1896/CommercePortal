/**
 *
 */
package com.gallagher.core.dtos;

import java.util.Date;


/**
 * Dto to store information
 *
 * of every registered product
 *
 * @author gauravkamboj
 */
public class GallagherRegisteredProductDto
{
	private String name;
	private String code;
	private String attachment;
	private String attachmentUrl;
	private Date registrationDate;
	private Date purchaseDate;
	private byte[] image;

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *           the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

	/**
	 * @return the attachment
	 */
	public String getAttachment()
	{
		return attachment;
	}

	/**
	 * @param attachment
	 *           the attachment to set
	 */
	public void setAttachment(final String attachment)
	{
		this.attachment = attachment;
	}

	/**
	 * @return the attachmentUrl
	 */
	public String getAttachmentUrl()
	{
		return attachmentUrl;
	}

	/**
	 * @param attachmentUrl
	 *           the attachmentUrl to set
	 */
	public void setAttachmentUrl(final String attachmentUrl)
	{
		this.attachmentUrl = attachmentUrl;
	}

	/**
	 * @return the registrationDate
	 */
	public Date getRegistrationDate()
	{
		return registrationDate;
	}

	/**
	 * @param registrationDate
	 *           the registrationDate to set
	 */
	public void setRegistrationDate(final Date registrationDate)
	{
		this.registrationDate = registrationDate;
	}

	/**
	 * @return the purchaseDate
	 */
	public Date getPurchaseDate()
	{
		return purchaseDate;
	}

	/**
	 * @param purchaseDate
	 *           the purchaseDate to set
	 */
	public void setPurchaseDate(final Date purchaseDate)
	{
		this.purchaseDate = purchaseDate;
	}

	/**
	 * @return the image
	 */
	public byte[] getImage()
	{
		return image;
	}

	/**
	 * @param image
	 *           the image to set
	 */
	public void setImage(final byte[] image)
	{
		this.image = image;
	}


}
