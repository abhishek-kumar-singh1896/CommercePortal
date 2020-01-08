package com.enterprisewide.b2badvance.facades.bulkorder.data;

import java.util.ArrayList;
import java.util.List;


public class BulkOrderImportResultData
{

	List<BulkOrderProductData> bulkorderList = new ArrayList<BulkOrderProductData>();

	List<String> csvErrors = new ArrayList<String>();

	String errorMessage;

	/**
	 * @return the bulkorderList
	 */
	public List<BulkOrderProductData> getBulkorderList()
	{
		return bulkorderList;
	}

	/**
	 * @param bulkorderList
	 *           the bulkorderList to set
	 */
	public void setBulkorderList(final List<BulkOrderProductData> bulkorderList)
	{
		this.bulkorderList = bulkorderList;
	}

	/**
	 * @return the csvErrors
	 */
	public List<String> getCsvErrors()
	{
		return csvErrors;
	}

	/**
	 * @param csvErrors
	 *           the csvErrors to set
	 */
	public void setCsvErrors(final List<String> csvErrors)
	{
		this.csvErrors = csvErrors;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *           the errorMessage to set
	 */
	public void setErrorMessage(final String errorMessage)
	{
		this.errorMessage = errorMessage;
	}


}
