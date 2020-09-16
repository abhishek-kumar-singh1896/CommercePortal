/**
 *
 */
package com.gallagher.b2b.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gallagher.b2b.storefront.controllers.ControllerConstants;
import com.gallagher.b2b.storefront.forms.B2BUnitsForm;
import com.gallagher.facades.GallagherB2BUnitFacade;


/**
 * @author shilpiverma
 *
 */
@Controller
@RequestMapping("/showb2bunits")
public class ShowB2BUnitsController extends AbstractPageController
{
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ShowB2BUnitsController.class);

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "b2bUnitFacade")
	protected GallagherB2BUnitFacade b2bUnitFacade;

	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String setUpCustomerPreference(final Model model) throws CMSItemNotFoundException
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

		if (currentCustomer instanceof B2BCustomerModel)
		{
			final List<B2BUnitModel> rootNodes = b2bUnitFacade.getAllB2BUnits(currentCustomer);
			model.addAttribute("b2bunits", rootNodes);
		}
		model.addAttribute("b2bUnitsForm", new B2BUnitsForm());
		return ControllerConstants.Views.Fragments.B2BUnits.B2BUnitsPopup;
	}

	@RequestMapping(value = "/submitSelectedUnit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean populateCustomerPreferences(@ModelAttribute
	final B2BUnitsForm b2bUnitsForm, final Model model, final RedirectAttributes redirectAttributes,
			final BindingResult bindingResult)
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		final List<B2BUnitModel> rootNodes = b2bUnitFacade.getAllB2BUnits(currentCustomer);
		for (final B2BUnitModel unit : rootNodes)
		{
			if (StringUtils.equals(unit.getUid(), b2bUnitsForm.getSelectedUnit()))
			{
				getSessionService().setAttribute("selectedB2BUnit", unit);
			}
		}
		model.addAttribute("b2bUnitsForm", new B2BUnitsForm());
		b2bUnitFacade.updateBranchInSession(getSessionService().getCurrentSession(), currentCustomer);
		return true;
	}

}
