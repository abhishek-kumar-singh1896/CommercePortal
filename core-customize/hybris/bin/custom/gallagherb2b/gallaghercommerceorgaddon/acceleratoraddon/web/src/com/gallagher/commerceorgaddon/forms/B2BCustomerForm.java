/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.commerceorgaddon.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateProfileForm;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Pojo for 'B2BCustomer' form.
 */
public class B2BCustomerForm extends UpdateProfileForm
{
	private boolean active;
	private String uid;
	private String parentB2BUnit;
	private List<String> parentB2BUnits;
	private Collection<String> roles = new ArrayList<String>();
	private Collection<CustomerData> approvers;
	private Collection<B2BUserGroupData> approverGroups;
	private String email;
	private String customerId;
	private boolean duplicate;
	private String objectId;

	public String getUid()
	{
		return uid;
	}

	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(final boolean active)
	{
		this.active = active;
	}

	public List<String> getParentB2BUnits()
	{
		return parentB2BUnits;
	}

	public void setParentB2BUnits(final List<String> parentB2BUnits)
	{
		this.parentB2BUnits = parentB2BUnits;
	}

	public String getParentB2BUnit()
	{
		return parentB2BUnit;
	}

	public void setParentB2BUnit(final String parentB2BUnit)
	{
		this.parentB2BUnit = parentB2BUnit;
	}


	public Collection<CustomerData> getApprovers()
	{
		return approvers;
	}

	public void setApprovers(final Collection<CustomerData> approvers)
	{
		this.approvers = approvers;
	}

	public Collection<B2BUserGroupData> getApproverGroups()
	{
		return approverGroups;
	}

	public void setApproverGroups(final Collection<B2BUserGroupData> approverGroups)
	{
		this.approverGroups = approverGroups;
	}

	public Collection<String> getRoles()
	{
		return roles;
	}

	public void setRoles(final Collection<String> roles)
	{
		this.roles = roles;
	}

	@NotNull(message = "{profile.email.invalid}")
	@Size(min = 1, max = 255, message = "{profile.email.invalid}")
	@Email(message = "{profile.email.invalid}")
	public String getEmail()
	{
		return email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public String getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(final String customerId)
	{
		this.customerId = customerId;
	}

	public void setDuplicate(final boolean duplicate)
	{
		this.duplicate = duplicate;
	}

	public boolean isDuplicate()
	{
		return duplicate;
	}

	public String getObjectId()
	{
		return objectId;
	}

	public void setObjectId(final String objectId)
	{
		this.objectId = objectId;
	}
}