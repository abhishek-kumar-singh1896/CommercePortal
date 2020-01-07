/**
 *
 */
package com.enterprisewide.b2badvance.facades.bulkorder.data;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class WrapperBulkOrderData
{
	List<BulkOrderProductData> bulkorderList = new ArrayList<BulkOrderProductData>();

	Integer defaultSize;

	public List<BulkOrderProductData> getBulkorderList()
	{
		return bulkorderList;
	}

	public void setBulkorderList(final List<BulkOrderProductData> bulkorderList)
	{
		this.bulkorderList = bulkorderList;
	}

	public WrapperBulkOrderData()
	{
		super();
	}

	public Integer getDefaultSize()
	{
		return defaultSize;
	}

	public void setDefaultSize(final Integer defaultSize)
	{
		this.defaultSize = defaultSize;
	}

	public WrapperBulkOrderData(final Integer defaultSize)
	{
		super();
		this.defaultSize = defaultSize;
	}



}
