/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.enterprisewide.b2badvanceacceleratoraddon.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Required;

import java.util.Date;


public class PaymentTypeForm
{
	private String paymentType;
	private String costCenterId;
	private String purchaseOrderNumber;
	private Date requiredDeliveryDate;
	private String userComments;

	@NotNull(message = "{general.required}")
	@Size(min = 1, max = 255, message = "{general.required}")
	public String getPaymentType()
	{
		return paymentType;
	}

	public void setPaymentType(final String paymentType)
	{
		this.paymentType = paymentType;
	}

	public String getCostCenterId()
	{
		return costCenterId;
	}

	public void setCostCenterId(final String costCenterId)
	{
		this.costCenterId = costCenterId;
	}

	@NotNull(message = "{general.required}")
	public String getPurchaseOrderNumber()
	{
		return purchaseOrderNumber;
	}

	public void setPurchaseOrderNumber(final String purchaseOrderNumber)
	{
		this.purchaseOrderNumber = purchaseOrderNumber;
	}
	
	public Date getRequiredDeliveryDate()
	{
		return requiredDeliveryDate;
	}

	public void setRequiredDeliveryDate(final Date requiredDeliveryDate)
	{
		this.requiredDeliveryDate = requiredDeliveryDate;
	}
	
	public String getUserComments()
	{
		return userComments;
	}

	public void setUserComments(final String userComments)
	{
		this.userComments = userComments;
	}
	
	
}
