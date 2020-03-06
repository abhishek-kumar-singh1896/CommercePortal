package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Contact entry
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherInboundCustomerEntry
{
	@JsonProperty("ContactID")
	private String contactID;

	@JsonProperty("FirstName")
	private String firstName;

	@JsonProperty("LastName")
	private String lastName;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("TitleCode")
	private String titleCode;

	@JsonProperty("StatusCode")
	private String statusCode;

	@JsonProperty("ObjectID")
	private String objectID;

	@JsonProperty("Email")
	private String email;

	private boolean duplicate;

	private String emailError;

	public String getContactID()
	{
		return this.contactID;
	}

	public void setContactID(final String contactID)
	{
		this.contactID = contactID;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getTitleCode()
	{
		return titleCode;
	}

	public void setTitleCode(final String titleCode)
	{
		this.titleCode = titleCode;
	}

	public String getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(final String statusCode)
	{
		this.statusCode = statusCode;
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public boolean isDuplicate()
	{
		return duplicate;
	}

	public void setDuplicate(final boolean duplicate)
	{
		this.duplicate = duplicate;
	}

	public String getEmailError()
	{
		return emailError;
	}

	public void setEmailError(final String emailError)
	{
		this.emailError = emailError;
	}

	public String getObjectID()
	{
		return objectID;
	}

	public void setObjectID(final String objectID)
	{
		this.objectID = objectID;
	}
}
