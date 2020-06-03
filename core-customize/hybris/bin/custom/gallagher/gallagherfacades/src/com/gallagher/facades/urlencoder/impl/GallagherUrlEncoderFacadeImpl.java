/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.urlencoder.impl;

import de.hybris.platform.acceleratorfacades.urlencoder.impl.DefaultUrlEncoderFacade;


/**
 * Facade implementation for UrlEncoding attributes
 */
public class GallagherUrlEncoderFacadeImpl extends DefaultUrlEncoderFacade
{


	@Override
	public String calculateAndUpdateUrlEncodingData(final String uri, final String contextPath)
	{
		return super.calculateAndUpdateUrlEncodingData(uri, contextPath).toLowerCase();
	}
}
