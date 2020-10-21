package com.gallagher.b2b.storefront.controllers.pages.checkout.deliveryinstruction;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.DeliveryInstructionData;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gallagher.b2b.storefront.controllers.ControllerConstants;
import com.gallagher.b2b.storefront.forms.DeliveryInstrutionsform;
import com.gallagher.facades.checkout.flow.GallagherCheckoutDeliveryInstructionFacade;


/**
 * @author abhinavgupta03
 *
 */

@Controller
@RequestMapping(value = "/checkout")
public class DeliveryInstructionController
{
	private static final int VALID_INSTRUCTION_LENGTH = 80;
	private static final String INVALID = "invalid";
	private static final String VALID = "valid";

	@Autowired
	private GallagherCheckoutDeliveryInstructionFacade gallagherCheckoutDeliveryInstructionFacade;

	@RequestMapping(value = "/deliveryinstruction", method = RequestMethod.GET)
	public String deliveryInstructionPopup(@RequestParam("getEntryNumber")
	final Integer entryNumber, final Model model) throws CMSItemNotFoundException
	{
		final DeliveryInstrutionsform deliveryInstrutionsform = new DeliveryInstrutionsform();
		final DeliveryInstructionData deliveryInstrutionsData = new DeliveryInstructionData();
		deliveryInstrutionsData.setEntryNumber(entryNumber);

		getGallagherCheckoutDeliveryInstructionFacade().populateDeliveryInstruction(deliveryInstrutionsData);
		if (StringUtils.isNotEmpty(deliveryInstrutionsData.getDeliveryinstruction()))
		{
			deliveryInstrutionsform.setDeliveryInstruction(deliveryInstrutionsData.getDeliveryinstruction());
		}
		if (StringUtils.isNotEmpty(deliveryInstrutionsData.getCommentPlaceHolder()))
		{
			deliveryInstrutionsform.setCommentPlaceHolder(deliveryInstrutionsData.getCommentPlaceHolder());
		}
		deliveryInstrutionsform.setEntryNumber(entryNumber);
		model.addAttribute(deliveryInstrutionsform);
		return ControllerConstants.Views.Fragments.DeliveryComments.DeliveryInstructionPopup;
	}

	@ResponseBody
	@RequestMapping(value = "/setdeliveryinstruction", method = RequestMethod.POST)
	public String deliveryInstructionPopup(@ModelAttribute("deliveryInstrutionsform")
	final DeliveryInstrutionsform deliveryInstrutionsform) throws CMSItemNotFoundException
	{
		System.out.println("Inside Controller");

		final String result = VALID;

		/*
		 * if (validateDeliveryInstruction(deliveryInstrutionsform)) { result = INVALID; } else {
		 */
			final String deliveryInstruction = deliveryInstrutionsform.getDeliveryInstruction();
			final Integer entryNumber = deliveryInstrutionsform.getEntryNumber();
		getGallagherCheckoutDeliveryInstructionFacade().setDeliveryInstructions(deliveryInstruction, entryNumber);

		//}
		return result;

	}

	/**
	 * @return the gallagherCheckoutDeliveryInstructionFacade
	 */
	public GallagherCheckoutDeliveryInstructionFacade getGallagherCheckoutDeliveryInstructionFacade()
	{
		return gallagherCheckoutDeliveryInstructionFacade;
	}

	/**
	 * @param gallagherCheckoutDeliveryInstructionFacade
	 *           the gallagherCheckoutDeliveryInstructionFacade to set
	 */
	public void setGallagherCheckoutDeliveryInstructionFacade(
			final GallagherCheckoutDeliveryInstructionFacade gallagherCheckoutDeliveryInstructionFacade)
	{
		this.gallagherCheckoutDeliveryInstructionFacade = gallagherCheckoutDeliveryInstructionFacade;
	}



}
