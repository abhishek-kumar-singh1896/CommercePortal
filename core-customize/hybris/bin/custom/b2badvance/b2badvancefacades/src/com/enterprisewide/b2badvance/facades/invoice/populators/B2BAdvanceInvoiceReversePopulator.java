/**
 *
 */
package com.enterprisewide.b2badvance.facades.invoice.populators;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.enums.InvoiceStatusEnum;
import com.enterprisewide.b2badvance.core.model.B2BAdvanceInvoiceModel;
import com.enterprisewide.b2badvance.facades.invoice.data.InvoiceData;


/**
 * Populates invoice Data into invoice Model
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceInvoiceReversePopulator<SOURCE extends InvoiceData, TARGET extends B2BAdvanceInvoiceModel>
		implements Populator<SOURCE, TARGET>
{

	private UserService userService;
	private BaseSiteService baseSiteService;
	private EnumerationService enumerationService;
	private CustomerAccountService customerAccountService;


	/**
	 * {@inheritDoc}
	 */
	public void populate(final InvoiceData source, final B2BAdvanceInvoiceModel target) throws ConversionException
	{
		if (StringUtils.isNotEmpty(source.getHybrisOrderNumber()))
		{
			try
			{
				final OrderModel order = customerAccountService.getOrderForCode(source.getHybrisOrderNumber(),
						baseSiteService.getCurrentBaseSite().getStores().get(0));
				target.setOrder(order);
				target.setCustomer((CustomerModel) order.getUser());
			}
			catch (final ModelNotFoundException | UnknownIdentifierException mNotFoundEx)
			{
				throw new ConversionException("No order found for hybris order number " + source.getHybrisOrderNumber());
			}
			catch (final AmbiguousIdentifierException ambIdEx)
			{
				throw new ConversionException("Multiple orders found for hybris order number " + source.getHybrisOrderNumber());
			}
		}

		if (StringUtils.isNotEmpty(source.getUserId()))
		{
			try
			{
				target.setCustomer((CustomerModel) userService.getUserForUID(source.getUserId()));
			}
			catch (final ModelNotFoundException | UnknownIdentifierException mNotFoundEx)
			{
				throw new ConversionException("No user found for user id " + source.getUserId());
			}
			catch (final AmbiguousIdentifierException ambIdEx)
			{
				throw new ConversionException("Multiple users found for user id " + source.getUserId());
			}
		}

		target.setDatePaid(source.getDatePaid());
		target.setDueDate(source.getDueDate());
		target.setErpOrderNumber(source.getErpOrderNumber());
		target.setInvoiceNumber(source.getInvoiceNumber());
		target.setPdfUrl(source.getPdfUrl());
		target.setStatus(enumerationService.getEnumerationValue(InvoiceStatusEnum.class, source.getStatus()));
		target.setTotalAmount(source.getTotalAmount());
	}

	public UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	@Required
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
