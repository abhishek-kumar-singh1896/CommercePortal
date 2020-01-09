/**
 *
 */
package com.gallagher.b2b.storefront.forms;

import java.util.List;


/**
 * @author Enterprise Wide
 *
 */
public class BulkOrderForm
{
	private List<BulkOrderFormEntry> bulkOrderFormEntries;

	/**
	 * @return the bulkOrderFormEntries
	 */
	public List<BulkOrderFormEntry> getBulkOrderFormEntries()
	{
		return bulkOrderFormEntries;
	}

	/**
	 * @param bulkOrderFormEntries
	 *           the bulkOrderFormEntries to set
	 */
	public void setBulkOrderFormEntries(final List<BulkOrderFormEntry> bulkOrderFormEntries)
	{
		this.bulkOrderFormEntries = bulkOrderFormEntries;
	}

}
