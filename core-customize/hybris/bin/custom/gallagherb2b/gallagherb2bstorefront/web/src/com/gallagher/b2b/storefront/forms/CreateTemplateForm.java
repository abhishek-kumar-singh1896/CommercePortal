/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.b2b.storefront.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.AddToCartOrderForm;


/**
 * Form for processing quick order form.
 *
 * @author Enterprise Wide
 */
public class CreateTemplateForm extends AddToCartOrderForm
{

	private String templateName;

	private String templateDesc;

	public String getTemplateName()
	{
		return templateName;
	}

	public void setTemplateName(final String templateName)
	{
		this.templateName = templateName;
	}

	public String getTemplateDesc()
	{
		return templateDesc;
	}

	public void setTemplateDesc(final String templateDesc)
	{
		this.templateDesc = templateDesc;
	}
}
