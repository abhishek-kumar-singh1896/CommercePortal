/**
 *
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gallagher.b2c.forms.RegisterProductForm;


/**
 * @author shilpiverma
 *
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterProductController extends AbstractPageController
{
	/*
	 * @RequestMapping(value = "/check", method = RequestMethod.GET) public String productOrderForm(final Model model,
	 * final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException {
	 * System.out.println("u r awesome...."); return "check"; }
	 */

	@RequestMapping(value = "/submit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String submitSampleOrder(@RequestBody
	final RegisterProductForm registerProductForm, final Model model, final BindingResult bindingResult,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final String redirectTo = null;
		return redirectTo;
	}

}
