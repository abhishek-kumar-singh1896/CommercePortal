/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.enterprisewide.b2badvanceacceleratoraddon.forms;

/**
 * Pojo for 'place order' form.
 */
public class DeliveryMethodForm {
	private String deliveryMethod;

	private String deliveryInstructions;

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	/**
	 * @return the deliveryInstructions
	 */
	public String getDeliveryInstructions() {
		return deliveryInstructions;
	}

	/**
	 * @param deliveryInstructions
	 *            the deliveryInstructions to set
	 */
	public void setDeliveryInstructions(final String deliveryInstructions) {
		this.deliveryInstructions = deliveryInstructions;
	}

}
