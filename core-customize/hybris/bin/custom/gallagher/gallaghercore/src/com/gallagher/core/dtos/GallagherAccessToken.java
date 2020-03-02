/**
 *
 */
package com.gallagher.core.dtos;

/**
 * Gallagher Access token dto for setting
 *
 * token values
 *
 * @author abhishek
 */
public class GallagherAccessToken
{

	private String name;
	private String email;
	private String subjectId;

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
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email
	 *           the email to set
	 */
	public void setEmail(final String email)
	{
		this.email = email;
	}

	/**
	 * @return the subjectId
	 */
	public String getSubjectId()
	{
		return subjectId;
	}

	/**
	 * @param subjectId
	 *           the subjectId to set
	 */
	public void setSubjectId(final String subjectId)
	{
		this.subjectId = subjectId;
	}




}
