/**
 *
 */
package com.gallagher.b2b.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
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

	@Resource(name = "cmsAdminSiteService")
	protected CMSAdminSiteService cmsAdminSiteService;

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

	@RequestMapping(value = "/submit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String populateCustomerPreferences(@ModelAttribute
	final B2BUnitsForm b2bUnitsForm, final Model model, final RedirectAttributes redirectAttributes,
			final BindingResult bindingResult)
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		final List<B2BUnitModel> rootNodes = b2bUnitFacade.getAllB2BUnits(currentCustomer);
		B2BUnitModel b2bUnitModel = null;
		for (final B2BUnitModel unit : rootNodes)
		{
			if (StringUtils.equals(unit.getUid(), b2bUnitsForm.getSelectedUnit()))
			{
				getSessionService().setAttribute("selectedB2BUnit", unit);
				b2bUnitModel = unit;
			}
		}
		model.addAttribute("b2bUnitsForm", new B2BUnitsForm());
		if (null != b2bUnitModel)
		{
			((B2BCustomerModel) currentCustomer).setDefaultB2BUnit(b2bUnitModel);
			modelService.save(currentCustomer);
			b2bUnitFacade.updateBranchInSession(getSessionService().getCurrentSession(), currentCustomer);
		}
		String redirectPath = "";
		if (currentCustomer instanceof B2BCustomerModel && null != ((B2BCustomerModel) currentCustomer).getDefaultB2BUnit())
		{
			final Collection<AddressModel> addresses = ((B2BCustomerModel) currentCustomer).getDefaultB2BUnit().getAddresses();
			if (CollectionUtils.isNotEmpty(addresses))
			{
				for (final AddressModel address : addresses)
				{
					if (null != address.getCountry())
					{
						final String regionCode = address.getCountry().getSecurityRegionCode().getCode();
						try
						{
							final CMSSiteModel site = cmsAdminSiteService.getSiteForId("securityB2B" + regionCode.toUpperCase());
							redirectPath = site.getRedirectURL();
						}
						catch (final UnknownIdentifierException exception)
						{
							redirectPath = "/security/global/en";
						}
						LOGGER.info("Redirection based upon country " + redirectPath);
					}
				}
			}
		}
		return redirectPath;
	}

}
