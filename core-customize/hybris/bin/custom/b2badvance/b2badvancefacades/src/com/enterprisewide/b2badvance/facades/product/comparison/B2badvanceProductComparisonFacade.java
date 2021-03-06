/**
 *
 */
package com.enterprisewide.b2badvance.facades.product.comparison;

import com.enterprisewide.b2badvance.facades.product.comparison.data.WrapperMapVariantAttributes;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.List;


/**
 * @author Enterprise Wide
 *
 */

public interface B2badvanceProductComparisonFacade
{
	/**
	 * Return the size of the Queue.
	 *
	 * @return size of the queue
	 */

	public int size();

	/**
	 * Checks if the Queue is empty
	 *
	 * @return true if the queue is empty.
	 */

	public boolean isEmpty();

	/**
	 * Checks if the queue already has the product code. Internally, invokes the Queue.contains.
	 *
	 * @param productCode
	 *           the product code which is checked against the contents of the queue.
	 * @return true, if the productCode is present in the queue.
	 */

	public boolean contains(final String productCode);

	/**
	 * Add the product code attribute into the queue. If the queue is full, then the head is removed to accomodate for
	 * the new entry. If the queue already has the same product code (as the argument) then remove the older entry from
	 * the queue. Internally, uses the Queue.add,which always adds to the tail
	 *
	 * @param productCode
	 *           the code is added to the queue.
	 * @return a true if successfully added
	 */

	public boolean add(final String productCode);

	/**
	 * Remove the head from the queue. Internally uses the Queue.remove, which always removes from the head.
	 *
	 * @return true if successfully removed
	 */

	public String remove();

	/**
	 * Remove an object from the queue. Internally uses the Queue.remove(object), to remove an entry based on the object
	 * as key.
	 *
	 * @param productCode
	 * @return true if successfully removed.
	 */

	public boolean remove(final String productCode);

	/** Clears the queue completely. */

	public void clear();

	/** Gets product comparison list */

	public List<ProductData> getProductComparisonList();

	/** Gets products' codes from product comparison list */

	public List<String> getProductComparisonCodes();

	/** Gets product variant attributes */

	public List<WrapperMapVariantAttributes> getProductVariantAttributes(List<ProductData> productList);

}
