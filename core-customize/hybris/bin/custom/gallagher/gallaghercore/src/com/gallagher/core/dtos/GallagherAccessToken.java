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

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public String getSubjectId()
	{
		return subjectId;
	}

	public void setSubjectId(final String subjectId)
	{
		this.subjectId = subjectId;
	}
}
