/**
 *
 */
package com.enterprisewide.b2badvance.facades.product;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.enterprisewide.b2badvance.facades.price.data.B2BAdvanceDiscountData;
import com.enterprisewide.b2badvance.facades.price.data.B2BAdvancePriceData;
import com.enterprisewide.b2badvance.facades.product.data.B2BAdvanceBaseProductData;
import com.enterprisewide.b2badvance.facades.product.data.B2BAdvanceVariantProductData;
import com.enterprisewide.b2badvance.facades.stock.data.B2BAdvanceStockData;


/**
 * Product facade for B2BAdvance.
 *
 * @author Vikram
 */
public interface B2BAdvanceProductFacade extends ProductFacade
{
	/**
	 * Returns the product by code. The current session data (catalog versions, user) are used, so the valid base product
	 * or variant product(if there is only one variant) for the current session parameters will be returned. Use
	 * {@link ProductFacade#getProductForOptions(ProductModel, Collection)} if you have the model already.
	 *
	 * @param code
	 *           the code of the product to be found
	 * @param options
	 *           options set that determines amount of information that will be attached to the returned product. If
	 *           empty or null default BASIC option is assumed
	 * @return the {@link ProductData}
	 * @throws IllegalArgumentException
	 *            when given product code is null
	 * @throws UnknownIdentifierException
	 *            when product with the given code is not found
	 */
	ProductData getBaseOrVariantProductForCodeAndOptions(final String code, final Collection<ProductOption> options)
			throws UnknownIdentifierException;

	/**
	 * Saves list of base products
	 *
	 * @return map of errors. Key is Article Number and value is the error associated with that product
	 */
	Map<String, String> saveBaseProducts(final List<B2BAdvanceBaseProductData> baseProducts);

	/**
	 * Saves list of variant products
	 *
	 * @return map of errors. Key is Article Number and value is the error associated with that product
	 */
	Map<String, String> saveVariantProducts(final List<B2BAdvanceVariantProductData> variantProducts);

	/**
	 * Saves list of prices
	 *
	 * @param prices
	 * @return map of errors. Key is Article Number and value is the error associated with that price
	 */
	Map<String, String> savePrices(final List<B2BAdvancePriceData> prices);

	/**
	 * Saves list of discounts
	 *
	 * @param discounts
	 * @return map of errors. Key is Article Number and value is the error associated with that discount
	 */
	Map<String, String> saveDiscounts(final List<B2BAdvanceDiscountData> discounts);

	/**
	 * Saves list of stocks
	 *
	 * @param stocks
	 * @return map of errors. Key is Article Number and value is the error associated with that stock
	 */
	Map<String, String> saveStocks(final List<B2BAdvanceStockData> stocks);
}
