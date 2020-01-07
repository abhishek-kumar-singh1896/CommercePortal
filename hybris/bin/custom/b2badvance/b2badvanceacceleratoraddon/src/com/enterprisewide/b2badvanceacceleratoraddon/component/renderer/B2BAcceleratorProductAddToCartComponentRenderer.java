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
package com.enterprisewide.b2badvanceacceleratoraddon.component.renderer;

import de.hybris.platform.acceleratorcms.model.components.ProductAddToCartComponentModel;
import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import com.enterprisewide.b2badvanceacceleratoraddon.constants.B2badvanceacceleratoraddonConstants;

import java.util.Map;

import javax.servlet.jsp.PageContext;


/**
 * b2badvanceacceleratoraddon renderer for ProductAddToCartComponents
 */
public class B2BAcceleratorProductAddToCartComponentRenderer<C extends ProductAddToCartComponentModel> extends
		DefaultAddOnCMSComponentRenderer<C>
{
	private static final String COMPONENT = "component";

	@Override
	protected Map<String, Object> getVariablesToExpose(final PageContext pageContext, final C component)
	{
		final Map<String, Object> model = super.getVariablesToExpose(pageContext, component);
		model.put(COMPONENT, component);
		return model;
	}

	@Override
	protected String getAddonUiExtensionName(final C component)
	{
		return B2badvanceacceleratoraddonConstants.EXTENSIONNAME;
	}
}
