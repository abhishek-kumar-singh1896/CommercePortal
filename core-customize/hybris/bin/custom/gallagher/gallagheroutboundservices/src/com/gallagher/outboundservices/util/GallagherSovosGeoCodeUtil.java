/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.util;

import de.hybris.platform.core.model.user.AddressModel;

import com.gallagher.outboundservices.request.dto.GallagherSovosGeoCodeRequest;


/**
 * @author gauravkamboj
 */
public class GallagherSovosGeoCodeUtil
{
	/**
	 * Convert AddressModel into GallagherSovosGeoCodeRequest
	 *
	 * @param address
	 * @param request
	 */
	public static void convert(final AddressModel address, final GallagherSovosGeoCodeRequest request)
	{
		request.setCity(address.getTown());
		request.setCountry(address.getCountry().getName());
		request.setStateProv(address.getRegion().getName());
		request.setPstlCd(address.getPostalcode());
	}
}
