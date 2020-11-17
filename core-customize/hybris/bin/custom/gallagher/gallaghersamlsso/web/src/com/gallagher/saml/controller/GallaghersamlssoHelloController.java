/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.saml.controller;

import static com.gallagher.saml.constants.GallaghersamlssoConstants.PLATFORM_LOGO_CODE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gallagher.saml.service.GallaghersamlssoService;


@Controller
public class GallaghersamlssoHelloController
{
	@Autowired
	private GallaghersamlssoService gallaghersamlssoService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(final ModelMap model)
	{
		model.addAttribute("logoUrl", gallaghersamlssoService.getHybrisLogoUrl(PLATFORM_LOGO_CODE));
		return "welcome";
	}
}
