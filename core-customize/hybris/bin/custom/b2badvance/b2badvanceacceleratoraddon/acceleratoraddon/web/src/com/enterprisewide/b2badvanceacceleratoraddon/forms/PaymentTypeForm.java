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


public class PaymentTypeForm
{
	private String paymentType;
	private String costCenterId;
	private String purchaseOrderNumber;
	private String b2bUnit;
	private String requiredDeliveryDate;
	private String deliveryInstructions;
	private boolean indicator;



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


	public String getB2bUnit()
	{
		return b2bUnit;
	}

	public void setB2bUnit(final String b2bUnit)
	{
		this.b2bUnit = b2bUnit;
	}

	public String getDeliveryInstructions()
	{
		return deliveryInstructions;
	}

	/**
	 * @param deliveryInstructions
	 *           the deliveryInstructions to set
	 */
	public void setDeliveryInstructions(final String deliveryInstructions)
	{
		this.deliveryInstructions = deliveryInstructions;
	}

	public void setRequiredDeliveryDate(final String requiredDeliveryDate)
	{
		this.requiredDeliveryDate = requiredDeliveryDate;
	}

	public String getRequiredDeliveryDate()
	{
		return requiredDeliveryDate;
	}

	public boolean isIndicator()
	{
		return indicator;
	}

	public void setIndicator(final boolean indicator)
	{
		this.indicator = indicator;
	}
}
