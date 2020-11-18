/**
 *
 */
package com.enterprisewide.b2badvance.facades.bulkorder.impl;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.enterprisewide.b2badvance.bulkorder.data.BulkOrderCartItemData;
import com.enterprisewide.b2badvance.core.bulkorder.services.BulkOrderService;
import com.enterprisewide.b2badvance.facades.bulkorder.BulkOrderFacade;
import com.enterprisewide.b2badvance.facades.bulkorder.data.BulkOrderImportResultData;
import com.enterprisewide.b2badvance.facades.bulkorder.data.BulkOrderProductData;
import com.enterprisewide.b2badvance.facades.bulkorder.data.WrapperBulkOrderData;


/**
 * @author Enterprise Wide
 *
 */
public class BulkOrderFacadeImpl implements BulkOrderFacade
{

	public static final String SESSION_ATTR_BULKORDER = "bulkorderProductList";

	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

	private final Logger LOG = Logger.getLogger(BulkOrderFacadeImpl.class);


	private ProductFacade productFacade;

	private SessionService sessionService;

	private CartFacade cartFacade;

	private ProductService productService;

	private Converter<ProductModel, ProductData> productConverter;

	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	private Converter<CartModificationData, BulkOrderCartItemData> bulkOrderCartItemConverter;

	private BulkOrderService bulkOrderService;

	/*
	 * (non-Javadoc)
	 *
	 * @see au.com.ciber.facades.bulkorder.BulkOrderFacade#add(au.com.ciber.facades.bulkorder.data.BulkOrderProductData)
	 */
	public void add(final BulkOrderProductData productData)
	{
		final List<BulkOrderProductData> bulkorderList = getBulkOrderListFromSession(null);
		bulkorderList.add(productData);
		final WrapperBulkOrderData wrapper = (WrapperBulkOrderData) sessionService.getAttribute(SESSION_ATTR_BULKORDER);
		Collections.sort(bulkorderList, new BulkOrderProductDataComparator());
		wrapper.setBulkorderList(bulkorderList);
		sessionService.setAttribute(SESSION_ATTR_BULKORDER, wrapper);
	}

	@Override
	public void add(final Integer orderNumber, final Integer qty, final String productCode)
	{
		final BulkOrderProductData productData = new BulkOrderProductData();
		productData.setOrderNumber(orderNumber);
		productData.setQuantity(qty);
		final ProductData product = productFacade.getProductForCodeAndOptions(productCode, PRODUCT_OPTIONS);
		productData.setProductData(product);
		remove(orderNumber);
		add(productData);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see au.com.ciber.facades.bulkorder.facade.BulkOrderFacade#remove(java.lang.String)
	 */
	@Override
	public void remove(final Integer orderNumber)
	{
		final List<BulkOrderProductData> bulkorderList = getBulkOrderListFromSession(null);
		BulkOrderProductData found = null;
		for (final BulkOrderProductData data : bulkorderList)
		{
			if (data.getOrderNumber() == orderNumber)
			{
				found = data;
				break;
			}
		}
		if (found != null)
		{
			bulkorderList.remove(found);
			final WrapperBulkOrderData wrapper = (WrapperBulkOrderData) sessionService.getAttribute(SESSION_ATTR_BULKORDER);
			wrapper.setBulkorderList(bulkorderList);
			sessionService.setAttribute(SESSION_ATTR_BULKORDER, wrapper);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see au.com.ciber.facades.bulkorder.facade.BulkOrderFacade#clear()
	 */
	@Override
	public void clear()
	{
		final Integer size = getBulkOrderListSizeFromSession();
		sessionService.removeAttribute(SESSION_ATTR_BULKORDER);
		sessionService.setAttribute(SESSION_ATTR_BULKORDER, new WrapperBulkOrderData(size));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see au.com.ciber.facades.bulkorder.facade.BulkOrderFacade#getBulkOrderList()
	 */
	@Override
	public List<BulkOrderProductData> getBulkOrderList(final Integer size)
	{
		final List<BulkOrderProductData> bulkorderList = getBulkOrderListFromSession(size);
		final List<BulkOrderProductData> l = new ArrayList<BulkOrderProductData>();
		if (bulkorderList.isEmpty())
		{
			if (size != null)
			{

				for (int i = 1; i <= size.intValue(); i++)
				{
					final BulkOrderProductData qoData = new BulkOrderProductData();
					qoData.setOrderNumber(Integer.valueOf(i));
					l.add(qoData);
				}
			}
		}
		else
		{
			Integer maxOrderNumber = bulkorderList.get(bulkorderList.size() - 1).getOrderNumber();
			maxOrderNumber = maxOrderNumber.intValue() > size.intValue() ? maxOrderNumber : size;
			for (int i = 1; i <= maxOrderNumber.intValue(); i++)
			{
				BulkOrderProductData qoData = null;
				for (final BulkOrderProductData data : bulkorderList)
				{
					if (data.getOrderNumber() != null && data.getOrderNumber().intValue() == (i))
					{
						qoData = data;
						break;
					}
				}
				if (qoData == null)
				{
					qoData = new BulkOrderProductData();
					qoData.setOrderNumber(Integer.valueOf(i));
				}
				l.add(qoData);
			}
		}
		return l;
	}

	private List<BulkOrderProductData> getBulkOrderListFromSession(final Integer size)
	{
		if (sessionService.getAttribute(SESSION_ATTR_BULKORDER) == null)
		{
			sessionService.setAttribute(SESSION_ATTR_BULKORDER, new WrapperBulkOrderData(size));
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Retrieveing Session id = " + sessionService.getCurrentSession().getSessionId());
		}
		return ((WrapperBulkOrderData) sessionService.getAttribute(SESSION_ATTR_BULKORDER)).getBulkorderList();
	}

	private Integer getBulkOrderListSizeFromSession()
	{
		final WrapperBulkOrderData data = ((WrapperBulkOrderData) sessionService.getAttribute(SESSION_ATTR_BULKORDER));
		return data.getDefaultSize();
	}

	@Override
	public List<CartModificationData> addBulkOrderToCart(final Collection<ProductOption> options)
	{
		final List<CartModificationData> cartModifications = new ArrayList<>();

		for (final BulkOrderProductData entry : getBulkOrderListFromSession(null))
		{
			final String code = entry.getProductData().getCode();
			final long quantity = entry.getQuantity().longValue();
			final CartModificationData cartModification = addItemToCart(code, quantity);
			cartModification.setEntry(prepareOrderEntryData(code, quantity, options));

			cartModifications.add(cartModification);
		}
		return cartModifications;
	}

	public CartModificationData addItemToCart(final String code, final long quantity)
	{
		try
		{
			if (quantity <= 0)
			{
				throw new CommerceCartModificationException("QUANTITY_TO_LOW");
			}
			final CartModificationData cartModification = cartFacade.addToCart(code, quantity);
			if (cartModification.getQuantityAdded() < quantity)
			{
				throw new CommerceCartModificationException(cartModification.getStatusCode());
			}
			return cartModification;
		}
		catch (final CommerceCartModificationException e)
		{
			final CartModificationData cartModification = new CartModificationData();
			cartModification.setQuantity(quantity);
			cartModification.setQuantityAdded(0);
			cartModification.setStatusCode(e.getMessage());
			return cartModification;
		}
	}

	private OrderEntryData prepareOrderEntryData(final String code, final long quantity, final Collection<ProductOption> options)
	{
		final OrderEntryData orderEntryData = new OrderEntryData();
		orderEntryData.setQuantity(Long.valueOf(quantity));

		final ProductModel productModel = getProductService().getProductForCode(code);
		final ProductData productData = getProductConverter().convert(productModel);
		getProductConfiguredPopulator().populate(productModel, productData, options);
		orderEntryData.setProduct(productData);

		return orderEntryData;
	}

	@Override
	public List<BulkOrderCartItemData> getBulkOrderCartItemEntries(final List<CartModificationData> modifications)
	{
		final List<BulkOrderCartItemData> entries = new ArrayList<BulkOrderCartItemData>();
		for (final CartModificationData modificationData : modifications)
		{
			entries.add(getBulkOrderCartItemConverter().convert(modificationData));
		}
		return entries;
	}

	public List<ProductData> getProductsForQuery(final String queryString, final int pageSize)
	{
		final List<ProductModel> productModelList = bulkOrderService.getProductsForQuery(queryString, pageSize);
		final List<ProductData> products = new ArrayList<ProductData>();
		if (productModelList != null && !productModelList.isEmpty())
		{
			for (final ProductModel model : productModelList)
			{
				final ProductData productData = getProductConverter().convert(model);
				/*
				 * final List<ProductOption> options = new ArrayList<>(Arrays.asList(ProductOption.VARIANT_FIRST_VARIANT,
				 * ProductOption.BASIC, ProductOption.URL, ProductOption.PRICE, ProductOption.SUMMARY,
				 * ProductOption.DESCRIPTION, ProductOption.GALLERY, ProductOption.CATEGORIES, ProductOption.REVIEW,
				 * ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL, ProductOption.STOCK,
				 * ProductOption.VOLUME_PRICES, ProductOption.PRICE_RANGE, ProductOption.DELIVERY_MODE_AVAILABILITY));
				 *
				 *
				 * productData = productFacade.getProductForCodeAndOptions(productData.getCode(), options);
				 */
				products.add(productData);
			}
		}
		return products;
	}

	@Override
	public BulkOrderImportResultData importBulkOrderFromFile(final MultipartFile file)
	{

		final BulkOrderImportResultData result = new BulkOrderImportResultData();


		try
		{
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
				if (row.getRowNum() == 0)
				{
					continue;
				}

				// For each row, iterate through each columns
				final Iterator<Cell> cellIterator = row.cellIterator();
				int qty = 0;
				String productCode = null;

				if (cellIterator.hasNext())
				{
					final Cell quantity = cellIterator.next();
					quantity.setCellType(CellType.STRING);
					try
					{
						qty = Integer.parseInt(quantity.getStringCellValue());
						if (qty < 0)
						{
							qty = 0;
						}
					}
					catch (final Exception e)
					{
						qty = 0;
					}
				}
				else
				{
					// set 1 as default quantity
					qty = 1;
				}
				if (cellIterator.hasNext())
				{
					final Cell productCell = cellIterator.next();
					productCell.setCellType(CellType.STRING);
					productCode = productCell.getStringCellValue();
				}

				if (productCode != null && !productCode.trim().isEmpty())
				{
					try
					{
						final ProductData productData = productFacade.getProductForCodeAndOptions(productCode, null);
						if (productData != null)
						{
							final BulkOrderProductData data = getFirstAvailableOrderNumberForProduct(productCode, qty);
							add(data.getOrderNumber(), data.getQuantity(), productCode);
						}
					}
					catch (final Exception e)
					{
						result.getCsvErrors().add(createErrorRow(row));
					}
				}
				else
				{
					result.getCsvErrors().add(createErrorRow(row));
				}

			}
		}
		catch (final Exception e)
		{
			result.setErrorMessage("bulkorder.unknown_error");
		}
		final Integer size = getBulkOrderListSizeFromSession();
		result.setBulkorderList(getBulkOrderList(size));
		return result;
	}

	/**
	 * Method to create the error row to be displayed
	 *
	 * @param row
	 * @return
	 */
	private String createErrorRow(final Row row)
	{
		final Iterator<Cell> cellIterator = row.cellIterator();
		String errorMessage = "";
		int cellCounter = 0;
		while (cellIterator.hasNext() && row.getCell(cellCounter) != null)
		{
			errorMessage = errorMessage.concat(row.getCell(cellCounter).toString()) + "  ";
			cellCounter++;

		}
		return errorMessage;
	}

	private BulkOrderProductData getFirstAvailableOrderNumberForProduct(final String productCode, final int qty)
	{
		final List<BulkOrderProductData> bulkorderList = getBulkOrderListFromSession(null);
		BulkOrderProductData result = new BulkOrderProductData();
		int orderNumber = 1;
		int i = 0;
		if (bulkorderList != null && !bulkorderList.isEmpty())
		{
			boolean foundOrderNumber = false;
			for (i = 0; i < bulkorderList.size(); i++)
			{
				final BulkOrderProductData qoData = bulkorderList.get(i);
				if ((i + 1) < qoData.getOrderNumber().intValue())
				{
					if (!foundOrderNumber)
					{
						foundOrderNumber = true;
						orderNumber = i + 1;
					}
				}
				if (qoData.getProductData().getCode().equals(productCode))
				{
					result = qoData;
					result.setQuantity(Integer.valueOf(result.getQuantity().intValue() + qty));
					return result;
				}
			}
			if (!foundOrderNumber)
			{
				orderNumber = i + 1;
			}
		}

		result.setOrderNumber(Integer.valueOf(orderNumber));
		result.setQuantity(Integer.valueOf(qty));
		return result;
	}

	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	public ProductService getProductService()
	{
		return productService;
	}

	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	public Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	public CartFacade getCartFacade()
	{
		return cartFacade;
	}

	public ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator()
	{
		return productConfiguredPopulator;
	}

	public void setProductConfiguredPopulator(
			final ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator)
	{
		this.productConfiguredPopulator = productConfiguredPopulator;
	}

	public Converter<CartModificationData, BulkOrderCartItemData> getBulkOrderCartItemConverter()
	{
		return bulkOrderCartItemConverter;
	}

	public void setBulkOrderCartItemConverter(
			final Converter<CartModificationData, BulkOrderCartItemData> bulkOrderCartItemConverter)
	{
		this.bulkOrderCartItemConverter = bulkOrderCartItemConverter;
	}

	public BulkOrderService getBulkOrderService()
	{
		return bulkOrderService;
	}

	public void setBulkOrderService(final BulkOrderService bulkOrderService)
	{
		this.bulkOrderService = bulkOrderService;
	}

}

class BulkOrderProductDataComparator implements Comparator<BulkOrderProductData>
{
	@Override
	public int compare(final BulkOrderProductData a, final BulkOrderProductData b)
	{
		return a.getOrderNumber().intValue() < b.getOrderNumber().intValue() ? -1 : 1;
	}
}
