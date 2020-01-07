/**
 *
 */
package com.enterprisewide.b2badvance.facades.invoice.impl;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.invoice.B2BAdvanceInvoiceService;
import com.enterprisewide.b2badvance.core.model.B2BAdvanceInvoiceModel;
import com.enterprisewide.b2badvance.facades.invoice.B2BAdvanceInvoiceFacade;
import com.enterprisewide.b2badvance.facades.invoice.data.InvoiceData;


/**
 * Implementation of {@link B2BAdvanceInvoiceFacade}
 */
public class B2BAdvanceInvoiceFacadeImpl implements B2BAdvanceInvoiceFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceInvoiceFacadeImpl.class);

	private UserService userService;

	private ModelService modelService;

	private B2BAdvanceInvoiceService invoiceService;

	private Converter<B2BAdvanceInvoiceModel, InvoiceData> invoiceConverter;

	private Converter<InvoiceData, B2BAdvanceInvoiceModel> invoiceReverseConverter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> saveInvoices(final List<InvoiceData> invoices)
	{
		final Map<String, String> importErrors;
		if (CollectionUtils.isNotEmpty(invoices))
		{
			importErrors = new HashMap<>(invoices.size());
			invoices.forEach(invoice -> processInvoice(invoice, importErrors));
		}
		else
		{
			importErrors = null;
		}
		return importErrors;
	}

	/**
	 * Processes invoices. Method is responsible of converting Data into Item model
	 *
	 * @param invoice
	 *           to be processed
	 * @param importErrors
	 *           to hold all the processing error
	 */
	private void processInvoice(final InvoiceData invoice, final Map<String, String> importErrors)
	{
		try
		{
			B2BAdvanceInvoiceModel invoiceModel = invoiceService.getInvoice(invoice.getInvoiceNumber());
			if (invoiceModel == null)
			{
				invoiceModel = modelService.create(B2BAdvanceInvoiceModel.class);
			}
			invoiceReverseConverter.convert(invoice, invoiceModel);
			modelService.save(invoiceModel);
		}
		catch (final ConversionException conEx)
		{
			LOG.error("Error while converting invoice data to invoice model", conEx);
			importErrors.put(invoice.getInvoiceNumber(), conEx.getMessage());
		}
		catch (final ModelSavingException mSavEx)
		{
			LOG.error("Error while saving invoice", mSavEx);
			importErrors.put(invoice.getInvoiceNumber(), mSavEx.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SearchPageData<InvoiceData> getInvoicesForCurrentUser(final PaginationData paginationData)
	{
		final SearchPageData<InvoiceData> result = new SearchPageData<>();
		final SearchPageData<B2BAdvanceInvoiceModel> invoiceModels = invoiceService.getInvoicesForCurrentUser(paginationData,
				getUserService().getCurrentUser());
		result.setPagination(invoiceModels.getPagination());
		result.setSorts(invoiceModels.getSorts());

		final List<InvoiceData> invoiceDatas = Converters.convertAll(invoiceModels.getResults(), getInvoiceConverter());
		result.setResults(invoiceDatas);
		return result;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected B2BAdvanceInvoiceService getInvoiceService()
	{
		return invoiceService;
	}

	@Required
	public void setInvoiceService(final B2BAdvanceInvoiceService invoiceService)
	{
		this.invoiceService = invoiceService;
	}

	protected Converter<InvoiceData, B2BAdvanceInvoiceModel> getInvoiceReverseConverter()
	{
		return invoiceReverseConverter;
	}

	@Required
	public void setInvoiceReverseConverter(final Converter<InvoiceData, B2BAdvanceInvoiceModel> invoiceReverseConverter)
	{
		this.invoiceReverseConverter = invoiceReverseConverter;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected Converter<B2BAdvanceInvoiceModel, InvoiceData> getInvoiceConverter()
	{
		return invoiceConverter;
	}

	@Required
	public void setInvoiceConverter(final Converter<B2BAdvanceInvoiceModel, InvoiceData> invoiceConverter)
	{
		this.invoiceConverter = invoiceConverter;
	}

}
