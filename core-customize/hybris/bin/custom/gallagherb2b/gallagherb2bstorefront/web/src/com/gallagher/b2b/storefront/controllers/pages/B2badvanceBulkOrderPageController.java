/**
 *
 */
package com.gallagher.b2b.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gallagher.b2b.storefront.controllers.ControllerConstants;
import com.gallagher.b2b.storefront.forms.BulkOrderForm;
import com.gallagher.b2b.storefront.forms.BulkOrderFormEntry;


/**
 * @author Enterprise Wide
 *
 */
@Controller
public class B2badvanceBulkOrderPageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(B2badvanceBulkOrderPageController.class);
	private static final String REDIRECT_BULK_ORDER_FORM_PAGE = REDIRECT_PREFIX + "/bulkOrderForm";
	private static final String BULK_ORDER_FORM_PAGE = "bulkOrderForm";
	private static final Integer INITIAL_BULK_ORDER_FORM_SIZE = 21;

	@Autowired
	ProductService productService;

	@Autowired
	CartFacade cartFacade;

	@Autowired
	MediaService mediaService;

	@Autowired
	CatalogService catalogService;

	/**
	 * Method that initializes the order form page
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return page
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/bulkOrderForm", method = RequestMethod.GET)
	@RequireHardLogIn
	public String productOrderForm(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{

		storeCmsPageInModel(model, getContentPageForLabelOrId(BULK_ORDER_FORM_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BULK_ORDER_FORM_PAGE));
		initializeBulkOrderForm(model);
		model.addAttribute("bulkOrderTemplateDownloadURL", mediaService
				.getMedia(catalogService.getCatalogForId("securityB2BContentCatalog").getActiveCatalogVersion(), "bulkOrderTemplate")
				.getDownloadURL());
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Order.BulkOrderForm;
	}


	@RequestMapping(value = "/bulk-order-add-to-cart", method = RequestMethod.POST)
	@RequireHardLogIn
	public String addProductsToCart(final Model model, final HttpServletRequest request, final HttpServletResponse response,
			final RedirectAttributes redirectAttributes, final BulkOrderForm bulkOrderForm) throws CMSItemNotFoundException
	{

		for (final BulkOrderFormEntry entry : bulkOrderForm.getBulkOrderFormEntries())
		{
			if (StringUtils.isNotEmpty(entry.getProductCode()) && StringUtils.isNotEmpty(entry.getQuantity()))
			{
				try
				{
					final CartModificationData cartModificationData = cartFacade.addToCart(entry.getProductCode(),
							Long.parseLong(entry.getQuantity()));
					if (cartModificationData != null)
					{
						if (StringUtils.isNotEmpty(cartModificationData.getStatusMessage()))
						{
							GlobalMessages.addErrorMessage(model, cartModificationData.getStatusMessage());
						}
					}
				}
				catch (final UnknownIdentifierException e)
				{
					GlobalMessages.addErrorMessage(model, "Product with code " + entry.getProductCode() + " does not exists.");
				}
				catch (final NumberFormatException e)
				{
					GlobalMessages.addErrorMessage(model, "Please enter a valid quantity for product " + entry.getProductCode());
				}
				catch (final CommerceCartModificationException e)
				{
					LOG.warn("Couldn't add item " + entry.getProductCode() + " to cart", e);
				}
			}
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(BULK_ORDER_FORM_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BULK_ORDER_FORM_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Order.BulkOrderForm;
	}

	@RequestMapping(value = "/upload-file", method = RequestMethod.POST)
	@RequireHardLogIn
	public String uploadFileHandler(@RequestParam("file")
	final MultipartFile file, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException
	{

		if (!file.isEmpty())
		{
			try
			{
				final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
				if (!(extension.equalsIgnoreCase("xlsx")) && !(extension.equalsIgnoreCase("xls")))
				{
					GlobalMessages.addErrorMessage(model, "order.form.file.format.error");
					LOG.error("Please upload a valid excel File. You failed to upload " + file.getName());
					initializeBulkOrderForm(model);
					return handleErrorOrderForm(model);
				}
				else
				{
					final List<BulkOrderFormEntry> csvOrderForms = new ArrayList<>();
					//Creates a workbook object from the uploaded excelfile

					final XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
					//Creates a worksheet object representing the first sheet
					final XSSFSheet worksheet = workbook.getSheetAt(0);
					//Reads the data in excel file until last row is encountered

					Row row;
					// Iterate through each rows from first sheet
					final Iterator<Row> rowIterator = worksheet.iterator();

					while (rowIterator.hasNext())
					{
						row = rowIterator.next();

						// For each row, iterate through each columns
						final Iterator<Cell> cellIterator = row.cellIterator();

						final BulkOrderFormEntry entry = new BulkOrderFormEntry();
						final Cell productCode = cellIterator.next();
						productCode.setCellType(CellType.STRING);
						final Cell quantity = cellIterator.next();
						quantity.setCellType(CellType.STRING);
						entry.setProductCode(productCode.getStringCellValue());
						entry.setQuantity(quantity.getStringCellValue());
						csvOrderForms.add(entry);
					}

					if (csvOrderForms.isEmpty())
					{
						GlobalMessages.addErrorMessage(model, "order.form.file.products");
						model.addAttribute("isFileError", "true");
						LOG.error("Unable to add any products from " + file.getName()
								+ ". Either All the products were invalid or the file is not in proper format.");
						initializeBulkOrderForm(model);
						return handleErrorOrderForm(model);
					}
					else
					{
						while (csvOrderForms.size() < INITIAL_BULK_ORDER_FORM_SIZE)
						{
							csvOrderForms.add(new BulkOrderFormEntry());
						}
					}
					final BulkOrderForm bulkOrderForm = new BulkOrderForm();
					bulkOrderForm.setBulkOrderFormEntries(csvOrderForms);
					model.addAttribute("bulkOrderForm", bulkOrderForm);

				}

			}
			catch (final RuntimeException rte)
			{
				LOG.warn(rte.getMessage(), rte);
				throw rte;
			}
			catch (final Exception e)
			{
				GlobalMessages.addErrorMessage(model, "order.form.file.error");
				model.addAttribute("isFileError", "true");
				LOG.error(e.getMessage());

				// Some exceptions in the Java API do not have messages!
				LOG.error(e);

				LOG.error("You failed to upload " + file.getName() + " => " + e.getMessage());
				initializeBulkOrderForm(model);
				return handleErrorOrderForm(model);
			}
		}
		else
		{
			GlobalMessages.addErrorMessage(model, "order.form.file.empty.error");
			model.addAttribute("isFileError", "true");
			LOG.error("You failed to upload " + file.getName() + " because the file was empty.");
			initializeBulkOrderForm(model);
			return handleErrorOrderForm(model);
		}
		return redirectOrderForm(model);
	}

	protected String handleErrorOrderForm(final Model model) throws CMSItemNotFoundException
	{

		storeCmsPageInModel(model, getContentPageForLabelOrId(BULK_ORDER_FORM_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BULK_ORDER_FORM_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Order.BulkOrderForm;
	}

	protected String redirectOrderForm(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(BULK_ORDER_FORM_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BULK_ORDER_FORM_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Order.BulkOrderForm;
	}

	protected void initializeBulkOrderForm(final Model model)
	{
		final List<BulkOrderFormEntry> bulkOrderFormEntries = new ArrayList<BulkOrderFormEntry>();
		for (int i = 0; i < INITIAL_BULK_ORDER_FORM_SIZE; i++)
		{
			bulkOrderFormEntries.add(new BulkOrderFormEntry());
		}
		final BulkOrderForm bulkOrderForm = new BulkOrderForm();
		bulkOrderForm.setBulkOrderFormEntries(bulkOrderFormEntries);

		model.addAttribute("bulkOrderForm", bulkOrderForm);
	}
}
