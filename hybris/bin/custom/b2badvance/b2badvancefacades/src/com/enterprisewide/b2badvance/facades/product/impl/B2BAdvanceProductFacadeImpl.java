package com.enterprisewide.b2badvance.facades.product.impl;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductVariantFacade;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.order.DiscountService;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.variants.model.GenericVariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.product.B2BAdvancePriceService;
import com.enterprisewide.b2badvance.facades.price.data.B2BAdvanceDiscountData;
import com.enterprisewide.b2badvance.facades.price.data.B2BAdvancePriceData;
import com.enterprisewide.b2badvance.facades.product.B2BAdvanceProductFacade;
import com.enterprisewide.b2badvance.facades.product.data.B2BAdvanceBaseProductData;
import com.enterprisewide.b2badvance.facades.product.data.B2BAdvanceVariantProductData;
import com.enterprisewide.b2badvance.facades.stock.data.B2BAdvanceStockData;


/**
 * Implementation of {@link B2BAdvanceProductFacade}
 */
public class B2BAdvanceProductFacadeImpl extends DefaultProductVariantFacade implements B2BAdvanceProductFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceProductFacadeImpl.class);

	private static final String CATALOG_VERSION = "Staged";

	private String catalog;

	private UnitService unitService;

	private StockService stockService;

	private SessionService sessionService;

	private BaseSiteService baseSiteService;

	private DiscountService discountService;

	private WarehouseService warehouseService;

	private B2BAdvancePriceService priceService;

	private CatalogVersionService catalogVersionService;

	private Converter<B2BAdvanceBaseProductData, ProductModel> productReverseConverter;

	private Converter<B2BAdvanceVariantProductData, GenericVariantProductModel> variantProductReverseConverter;

	private CatalogVersionModel catalogVersion = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductData getBaseOrVariantProductForCodeAndOptions(final String code, final Collection<ProductOption> options)
			throws UnknownIdentifierException
	{
		ProductModel productModel = getProductService().getProductForCode(code);
		productModel = variantProductCountCheck(productModel);
		final ProductData productData = getProductConverter().convert(productModel);

		if (options != null)
		{
			getProductConfiguredPopulator().populate(productModel, productData, options);
		}

		return productData;
	}

	/**
	 * Returns variant model if there is only one variant otherwise base product is returned.
	 *
	 * @param baseProductModel
	 * @return variant or base product.
	 */
	protected ProductModel variantProductCountCheck(final ProductModel baseProductModel)
	{
		ProductModel productModel = baseProductModel;
		if (null != baseProductModel.getVariants() && !baseProductModel.getVariants().isEmpty()
				&& baseProductModel.getVariants().size() == 1)
		{
			productModel = getProductService().getProductForCode(baseProductModel.getVariants().iterator().next().getCode());
		}
		return productModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> saveBaseProducts(final List<B2BAdvanceBaseProductData> baseProducts)
	{

		getUserService().setCurrentUser(getUserService().getAdminUser());
		final Map<String, String> importErrors = new HashMap<>(baseProducts.size());
		if (CollectionUtils.isNotEmpty(baseProducts))
		{
			baseProducts.forEach(product -> {
				try
				{
					processBaseProduct(product, importErrors);
				}
				catch (final ConversionException conEx)
				{
					LOG.error("Error while converting base product data to product model", conEx);
					importErrors.put(product.getArticleNumber(), conEx.getMessage());
				}
				catch (final ModelSavingException mSavEx)
				{
					LOG.error("Error while saving base product", mSavEx);
					importErrors.put(product.getArticleNumber(), mSavEx.getMessage());
				}
				catch (final AmbiguousIdentifierException amIdEx)
				{
					LOG.error("Ambiguity exists in existing base product", amIdEx);
					importErrors.put(product.getArticleNumber(), amIdEx.getMessage());
				}
			});
		}
		return importErrors;
	}

	/**
	 * Processes products. Method is responsible of converting Data into Item model
	 *
	 * @param product
	 *           to be processed
	 * @param importErrors
	 *           to hold all the processing error
	 */
	private void processBaseProduct(final B2BAdvanceBaseProductData product, final Map<String, String> importErrors)
	{
		ProductModel productModel = null;
		getUserService().setCurrentUser(getUserService().getAdminUser());
		final Collection<CategoryModel> superCategories = new ArrayList<>();
		try
		{
			productModel = getProductService().getProductForCode(getCatalogVersion(), product.getArticleNumber());
		}
		catch (final UnknownIdentifierException unIdEx)
		{
			productModel = getModelService().create(ProductModel.class);
		}
		productReverseConverter.convert(product, productModel);
		getModelService().save(productModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> saveVariantProducts(final List<B2BAdvanceVariantProductData> variantProducts)
	{
		getUserService().setCurrentUser(getUserService().getAdminUser());
		final Map<String, String> importErrors = new HashMap<>(variantProducts.size());
		if (CollectionUtils.isNotEmpty(variantProducts))
		{
			variantProducts.forEach(product -> {
				try
				{
					processVariantProduct(product, importErrors);
				}
				catch (final ConversionException conEx)
				{
					LOG.error("Error while converting variant product data to product model", conEx);
					importErrors.put(product.getArticleNumber(), conEx.getMessage());
				}
				catch (final ModelSavingException mSavEx)
				{
					LOG.error("Error while saving variant product", mSavEx);
					importErrors.put(product.getArticleNumber(), mSavEx.getMessage());
				}
				catch (final AmbiguousIdentifierException amIdEx)
				{
					LOG.error("Ambiguity exists in existing variant product", amIdEx);
					importErrors.put(product.getArticleNumber(), amIdEx.getMessage());
				}
			});
		}
		return importErrors;
	}

	/**
	 * Processes products. Method is responsible of converting Data into Item model
	 *
	 * @param product
	 *           to be processed
	 * @param importErrors
	 *           to hold all the processing error
	 */
	private void processVariantProduct(final B2BAdvanceVariantProductData product, final Map<String, String> importErrors)
	{
		GenericVariantProductModel productModel;
		getUserService().setCurrentUser(getUserService().getAdminUser());
		try
		{
			productModel = (GenericVariantProductModel) getProductService().getProductForCode(getCatalogVersion(),
					product.getArticleNumber());
		}
		catch (final UnknownIdentifierException unIdEx)
		{
			productModel = getModelService().create(GenericVariantProductModel.class);
		}
		variantProductReverseConverter.convert(product, productModel);
		getModelService().save(productModel);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> savePrices(final List<B2BAdvancePriceData> prices)
	{
		final Map<String, String> importErrors = new HashMap<>(prices.size());
		if (CollectionUtils.isNotEmpty(prices))
		{
			prices.forEach(price -> {
				try
				{
					processPrice(price, importErrors);
				}
				catch (final ModelSavingException mSavEx)
				{
					LOG.error("Error while saving price", mSavEx);
					importErrors.put(price.getArticleNumber(), mSavEx.getMessage());
				}
				catch (final AmbiguousIdentifierException amIdEx)
				{
					LOG.error("Ambiguity exists in existing price row", amIdEx);
					importErrors.put(price.getArticleNumber(), amIdEx.getMessage());
				}
			});
		}
		return importErrors;

	}

	/**
	 * Processes prices. Method is responsible of converting Data into Item model
	 *
	 * @param price
	 *           to be processed
	 * @param importErrors
	 *           to hold all the processing error
	 */
	private void processPrice(final B2BAdvancePriceData price, final Map<String, String> importErrors)
	{
		ProductModel productModel = null;
		getUserService().setCurrentUser(getUserService().getAdminUser());
		final Collection<CategoryModel> superCategories = new ArrayList<>();
		try
		{
			productModel = getProductService().getProductForCode(getCatalogVersion(), price.getArticleNumber());
			updateProductPrice(productModel, importErrors, price);
		}
		catch (final UnknownIdentifierException unIdEx)
		{
			importErrors.put(price.getArticleNumber(), unIdEx.getMessage());
		}
	}

	/**
	 * Updates the product price. It tries to find the existing price row first. If not found, a new price row gets
	 * created
	 *
	 * @param product
	 *           for which price row needs to be updated
	 * @param importErrors
	 *           to hold the import error
	 * @param price
	 *           contains the price row data
	 */
	private void updateProductPrice(final ProductModel product, final Map<String, String> importErrors,
			final B2BAdvancePriceData price)
	{
		PriceRowModel priceRow = null;
		UserPriceGroup userPriceGroup = null;
		final CurrencyModel currency = getCommonI18NService().getCurrency(price.getCurrency());
		final UnitModel unit = unitService.getUnitForCode(price.getSalesUnit());
		try
		{
			if (StringUtils.isEmpty(price.getUserPriceGroup()))
			{
				priceRow = getPriceService().getPriceRowForDate(product, currency, unit, price.getStartDate(), price.getEndDate(),
						getCatalogVersion());
			}
			else
			{
				userPriceGroup = UserPriceGroup.valueOf(price.getUserPriceGroup());
				priceRow = getPriceService().getPriceRowForDateAndUG(product, currency, unit, price.getStartDate(),
						price.getEndDate(), userPriceGroup, getCatalogVersion());
			}
			priceRow.setPrice(price.getPrice());
			priceRow.setNet(price.isIsNetPrice());
		}
		catch (final UnknownIdentifierException uIDEx)
		{
			//If no price row found matching the criteria then create a new price row
			priceRow = createPriceRow(product, unit, currency, price, userPriceGroup);
		}
		catch (final AmbiguousIdentifierException aIDEx)
		{
			LOG.error("Multiple Price Rows exists", aIDEx);
			importErrors.put(price.getArticleNumber(), "Multiple Prices found for product with same criteria");
		}
		if (priceRow != null)
		{
			getModelService().save(priceRow);
		}
	}

	/**
	 * Creates the price row
	 *
	 * @param product
	 *           for which price needs to be created
	 * @param unit
	 *           of price i.e. pieces
	 * @param prices
	 *           list containing all prices
	 * @param price
	 *           the price sent by ERP
	 * @param userPriceGroup
	 *           of the price row
	 */
	private PriceRowModel createPriceRow(final ProductModel product, final UnitModel unit, final CurrencyModel currency,
			final B2BAdvancePriceData price, final UserPriceGroup userPriceGroup)
	{
		final PriceRowModel newPriceRow = getModelService().create(PriceRowModel.class);
		newPriceRow.setCatalogVersion(getCatalogVersion());
		newPriceRow.setCurrency(currency);
		newPriceRow.setPrice(price.getPrice());
		newPriceRow.setProduct(product);
		newPriceRow.setUnit(unit);
		newPriceRow.setEndTime(price.getEndDate());
		newPriceRow.setStartTime(price.getStartDate());
		newPriceRow.setUg(userPriceGroup);
		newPriceRow.setNet(price.isIsNetPrice());
		return newPriceRow;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> saveDiscounts(final List<B2BAdvanceDiscountData> discounts)
	{
		getUserService().setCurrentUser(getUserService().getAdminUser());
		final Map<String, String> importErrors = new HashMap<>(discounts.size());
		if (CollectionUtils.isNotEmpty(discounts))
		{

			discounts.forEach(discount -> {
				try
				{
					processDiscount(discount, importErrors);
				}
				catch (final ModelSavingException mSavEx)
				{
					LOG.error("Error while saving discount", mSavEx);
					importErrors.put(discount.getArticleNumber(), mSavEx.getMessage());
				}
				catch (final AmbiguousIdentifierException amIdEx)
				{
					LOG.error("Ambiguity exists in existing discount row", amIdEx);
					importErrors.put(discount.getArticleNumber(), amIdEx.getMessage());
				}
			});
		}
		return importErrors;
	}

	/**
	 * Processes discounts. Method is responsible of converting Data into Item model
	 *
	 * @param discount
	 *           to be processed
	 * @param importErrors
	 *           to hold all the processing error
	 */
	private void processDiscount(final B2BAdvanceDiscountData discount, final Map<String, String> importErrors)
	{
		ProductModel productModel = null;
		getUserService().setCurrentUser(getUserService().getAdminUser());
		final Collection<CategoryModel> superCategories = new ArrayList<>();
		try
		{
			productModel = getProductService().getProductForCode(getCatalogVersion(), discount.getArticleNumber());
			updateProductDiscount(productModel, importErrors, discount);
		}
		catch (final UnknownIdentifierException unIdEx)
		{
			importErrors.put(discount.getArticleNumber(), unIdEx.getMessage());
		}
	}

	/**
	 * Updates the product discount. It tries to find the existing discount row first. If not found, a new discount row
	 * gets created
	 *
	 * @param product
	 *           for which discount row needs to be updated
	 * @param importErrors
	 *           to hold the import error
	 * @param price
	 *           contains the discount row data
	 */
	private void updateProductDiscount(final ProductModel product, final Map<String, String> importErrors,
			final B2BAdvanceDiscountData discount)
	{
		DiscountRowModel discountRow = null;
		UserDiscountGroup userDiscountGroup = null;
		CurrencyModel currency = null;
		if (StringUtils.isNotEmpty(discount.getCurrency()))
		{
			currency = getCommonI18NService().getCurrency(discount.getCurrency());
		}
		DiscountModel discountModel = null;
		try
		{
			if (StringUtils.isNotEmpty(discount.getDiscountCode()))
			{
				discountModel = getDiscountService().getDiscountForCode(discount.getDiscountCode());
			}
			if (StringUtils.isEmpty(discount.getUserDiscountGroup()))
			{
				discountRow = getPriceService().getDiscountRowForDate(product, discountModel, currency, discount.getStartDate(),
						discount.getEndDate(), getCatalogVersion());
			}
			else
			{
				userDiscountGroup = UserDiscountGroup.valueOf(discount.getUserDiscountGroup());
				discountRow = getPriceService().getDiscountRowForDateAndUG(product, discountModel, currency, discount.getStartDate(),
						discount.getEndDate(), userDiscountGroup, getCatalogVersion());
			}
			discountRow.setValue(discount.getDiscount());
		}
		catch (final UnknownIdentifierException uIDEx)
		{
			//If no discount row found matching the criteria then create a new discount row
			discountRow = createDiscountRow(product, currency, discount, discountModel, userDiscountGroup);
		}
		catch (final AmbiguousIdentifierException aIDEx)
		{
			LOG.error("Multiple Discount Rows exists", aIDEx);
			importErrors.put(discount.getArticleNumber(), "Multiple discounts found for product with same criteria");
		}
		if (discountRow != null)
		{
			getModelService().save(discountRow);
		}
	}

	/**
	 * Creates the discount row
	 *
	 * @param product
	 *           for which discount needs to be created
	 *
	 * @param discount
	 *           the discount sent by ERP
	 * @param userDiscountGroup
	 *           of the price row
	 */
	private DiscountRowModel createDiscountRow(final ProductModel product, final CurrencyModel currency,
			final B2BAdvanceDiscountData discount, final DiscountModel discountModel, final UserDiscountGroup userDiscountGroup)
	{
		final DiscountRowModel newDiscountRow = getModelService().create(DiscountRowModel.class);
		newDiscountRow.setCatalogVersion(getCatalogVersion());
		newDiscountRow.setCurrency(currency);
		newDiscountRow.setValue(discount.getDiscount());
		newDiscountRow.setProduct(product);
		if (discountModel == null && StringUtils.isNotEmpty(discount.getDiscountCode()))
		{
			final DiscountModel newDiscountModel = getModelService().create(DiscountModel.class);
			newDiscountModel.setCode(discount.getDiscountCode());
			newDiscountModel.setCurrency(currency);
			getModelService().save(newDiscountModel);
			newDiscountRow.setDiscount(newDiscountModel);
		}
		else
		{
			newDiscountRow.setDiscount(discountModel);
		}
		newDiscountRow.setEndTime(discount.getEndDate());
		newDiscountRow.setStartTime(discount.getStartDate());
		newDiscountRow.setUg(userDiscountGroup);
		newDiscountRow.setAsTargetPrice(false);
		return newDiscountRow;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> saveStocks(final List<B2BAdvanceStockData> stocks)
	{
		getUserService().setCurrentUser(getUserService().getAdminUser());
		final Map<String, String> importErrors = new HashMap<>(stocks.size());
		if (CollectionUtils.isNotEmpty(stocks))
		{

			stocks.forEach(stock -> {
				try
				{
					processStock(stock, importErrors);
				}
				catch (final ModelSavingException mSavEx)
				{
					LOG.error("Error while saving stock", mSavEx);
					importErrors.put(stock.getArticleNumber(), mSavEx.getMessage());
				}
			});
		}
		return importErrors;
	}

	/**
	 * Processes stocks. Method is responsible of converting Data into Item model
	 *
	 * @param stock
	 *           to be processed
	 * @param importErrors
	 *           to hold all the processing error
	 */
	private void processStock(final B2BAdvanceStockData stock, final Map<String, String> importErrors)
	{
		ProductModel productModel = null;
		final Collection<CategoryModel> superCategories = new ArrayList<>();
		try
		{
			productModel = getProductService().getProductForCode(getCatalogVersion(), stock.getArticleNumber());
			updateProductStock(productModel, importErrors, stock);
		}
		catch (final UnknownIdentifierException unIdEx)
		{
			importErrors.put(stock.getArticleNumber(), unIdEx.getMessage());
		}
	}

	/**
	 * Updates the product stock. It tries to find the existing stock first. If not found, a new stock level gets created
	 *
	 * @param product
	 *           for which discount row needs to be updated
	 * @param importErrors
	 *           to hold the import error
	 * @param stock
	 *           contains the stock data
	 */
	private void updateProductStock(final ProductModel product, final Map<String, String> importErrors,
			final B2BAdvanceStockData stock)
	{

		try
		{
			final WarehouseModel warehouse = warehouseService.getWarehouseForCode(stock.getWarehouseCode());
			StockLevelModel stockLevel = stockService.getStockLevel(product, warehouse);

			if (stockLevel == null)
			{
				stockLevel = getModelService().create(StockLevelModel.class);
				stockLevel.setWarehouse(warehouse);
				stockLevel.setProductCode(product.getCode());
			}
			stockLevel.setAvailable(stock.getStockAvailable());
			stockLevel.setInStockStatus(InStockStatus.valueOf(stock.getInStockStatus().toUpperCase()));
			getModelService().save(stockLevel);
		}
		catch (final UnknownIdentifierException uIDEx)
		{
			LOG.error("No warehouse exist with the provided code", uIDEx);
			importErrors.put(stock.getArticleNumber(), String.format("No warehouse exist with code [%s]", stock.getWarehouseCode()));
		}
		catch (final AmbiguousIdentifierException aIDEx)
		{
			LOG.error("Multiple warehouses exist with same code", aIDEx);
			importErrors.put(stock.getArticleNumber(),
					String.format("Multiple warehouses exist with code [%s]", stock.getWarehouseCode()));
		}
	}

	/**
	 * Returns the catalog version
	 *
	 * @return CatalogVersionModel the catalogVersion required
	 */
	protected CatalogVersionModel getCatalogVersion()
	{
		if (catalogVersion == null)
		{
			if (baseSiteService.getCurrentBaseSite() == null)
			{
				catalogVersion = catalogVersionService.getCatalogVersion(catalog, CATALOG_VERSION);
			}
			else
			{
				catalogVersion = ((CMSSiteModel) baseSiteService.getCurrentBaseSite()).getDefaultCatalog().getCatalogVersions()
						.stream().filter(catalogVersion -> !catalogVersion.getActive()).findFirst().get();
			}
		}
		return catalogVersion;
	}

	protected String getCatalog()
	{
		return catalog;
	}

	@Required
	public void setCatalog(final String catalog)
	{
		this.catalog = catalog;
	}

	protected UnitService getUnitService()
	{
		return unitService;
	}

	@Required
	public void setUnitService(final UnitService unitService)
	{
		this.unitService = unitService;
	}


	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected Converter<B2BAdvanceBaseProductData, ProductModel> getProductReverseConverter()
	{
		return productReverseConverter;
	}

	@Required
	public void setProductReverseConverter(final Converter<B2BAdvanceBaseProductData, ProductModel> productReverseConverter)
	{
		this.productReverseConverter = productReverseConverter;
	}

	protected Converter<B2BAdvanceVariantProductData, GenericVariantProductModel> getVariantProductReverseConverter()
	{
		return variantProductReverseConverter;
	}

	@Required
	public void setVariantProductReverseConverter(
			final Converter<B2BAdvanceVariantProductData, GenericVariantProductModel> variantProductReverseConverter)
	{
		this.variantProductReverseConverter = variantProductReverseConverter;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected DiscountService getDiscountService()
	{
		return discountService;
	}

	@Required
	public void setDiscountService(final DiscountService discountService)
	{
		this.discountService = discountService;
	}

	protected B2BAdvancePriceService getPriceService()
	{
		return priceService;
	}

	@Required
	public void setPriceService(final B2BAdvancePriceService priceService)
	{
		this.priceService = priceService;
	}

	protected StockService getStockService()
	{
		return stockService;
	}

	@Required
	public void setStockService(final StockService stockService)
	{
		this.stockService = stockService;
	}

	protected WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	@Required
	public void setWarehouseService(final WarehouseService warehouseService)
	{
		this.warehouseService = warehouseService;
	}
}
