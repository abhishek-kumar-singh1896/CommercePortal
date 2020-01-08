/**
 *
 */
package com.enterprisewide.b2badvance.facades.bulkorder;

import com.enterprisewide.b2badvance.bulkorder.data.BulkOrderCartItemData;
import com.enterprisewide.b2badvance.facades.bulkorder.data.BulkOrderImportResultData;
import com.enterprisewide.b2badvance.facades.bulkorder.data.BulkOrderProductData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;


/**
 * @author Enterprise Wide
 *
 */
public interface BulkOrderFacade
{
	/**
	 * Add product to bulk order
	 *
	 * @param orderNumber
	 *           product's order number in bulk order list
	 * @param qty
	 *           product's quantity
	 * @param productCode
	 *           product code
	 */
	public void add(Integer orderNumber, Integer qty, String productCode);

	/**
	 * Remove product data from bulk order list
	 *
	 * @param orderNumber
	 *           product order number in bulk order list
	 */
	public void remove(final Integer orderNumber);

	/**
	 * Clear quick order list from session
	 */
	public void clear();

	/**
	 * Gets bulk order list
	 *
	 * @param size
	 *           default bulk order list size
	 * @return list of BulkOrderProductData
	 */
	public List<BulkOrderProductData> getBulkOrderList(Integer size);

	/**
	 * Add quick order list to cart
	 *
	 * @param options
	 *           product option collection
	 * @return list of CartModificationData
	 */
	public List<CartModificationData> addBulkOrderToCart(final Collection<ProductOption> options);

	/**
	 * Gets cart item entries
	 *
	 * @param modifications
	 *           list of CartModificationData
	 * @return list of BulkOrderCartItemData
	 */
	public List<BulkOrderCartItemData> getBulkOrderCartItemEntries(final List<CartModificationData> modifications);

	/**
	 * Gets products for query string
	 *
	 * @param queryString
	 *           query string
	 * @param pageSize
	 *           page size
	 * @return list of ProductData
	 */
	public List<ProductData> getProductsForQuery(final String queryString, final int pageSize);

	public BulkOrderImportResultData importBulkOrderFromFile(final MultipartFile file);
}
