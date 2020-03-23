/**
 *
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gallagher.b2c.controllers.ControllerConstants;
import com.gallagher.core.forms.B2CCustomerPreferenceForm;


/**
 * Controller for customer preference overlay
 *
 * @author abhishek
 */
@Controller
public class MyPreferencesController extends AbstractPageController
{

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(MyPreferencesController.class);

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@RequestMapping(value = "/preferences", method = RequestMethod.GET)
	public String setUpCustomerPreference(final Model model) throws CMSItemNotFoundException
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

		final B2CCustomerPreferenceForm customerPreferences = new B2CCustomerPreferenceForm();

		customerPreferences.setNewsLetters(true);
		customerPreferences.setEvents(true);
		customerPreferences.setProductPromo(true);
		customerPreferences.setProductRelease(true);
		customerPreferences.setProductUpdate(true);

		model.addAttribute("preferences", customerPreferences);

		return ControllerConstants.Views.Fragments.Preference.CustomerPreferencePopup;

	}

	@RequestMapping(value = "/submitPreferences", method = RequestMethod.POST)
	public String populateCustomerPreferences(@ModelAttribute("preferences")
	final B2CCustomerPreferenceForm preferences)
	{

		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

		currentCustomer.setNewsLetters(preferences.isNewsLetters());
		currentCustomer.setEvents(preferences.isEvents());
		currentCustomer.setProductPromo(preferences.isProductPromo());
		currentCustomer.setProductRelease(preferences.isProductRelease());
		currentCustomer.setProductUpdate(preferences.isProductUpdate());
		currentCustomer.setIsUserExist(true);

		modelService.save(currentCustomer);

		return REDIRECT_PREFIX + ROOT;
	}
}
