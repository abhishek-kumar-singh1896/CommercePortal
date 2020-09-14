/**
 *
 */
package com.gallagher.b2b.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitNodeData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gallagher.b2b.storefront.controllers.ControllerConstants;
import com.gallagher.facades.GallagherB2BUnitFacade;


/**
 * @author shilpiverma
 *
 */
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

	@RequestMapping(value = "/showb2bunits", method = RequestMethod.GET)
	public String setUpCustomerPreference(final Model model) throws CMSItemNotFoundException
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

		if (currentCustomer instanceof B2BCustomerModel)
		{
			final B2BUnitNodeData rootNode = b2bUnitFacade.getParentUnitNode();
			model.addAttribute("b2bunits", rootNode);
		}
		return ControllerConstants.Views.Fragments.B2BUnits.B2BUnitsPopup;
	}

}
