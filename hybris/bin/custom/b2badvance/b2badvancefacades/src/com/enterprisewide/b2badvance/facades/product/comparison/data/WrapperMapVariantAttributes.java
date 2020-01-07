/**
 *
 */
package com.enterprisewide.b2badvance.facades.product.comparison.data;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Enterprise Wide
 *
 */
public class WrapperMapVariantAttributes
{
	/** VariantOptionQualifierData name */
	private String name;

	/** VariantOptionQualifierData qualifier */
	private String qualifier;

	/**
	 * Map with VariantOptionQualifierData values for compare products.<br/>
	 * Key = product code, value = VariantOptionQualifierData.value
	 */
	private final Map<String, String> productAttrValueMap = new HashMap<String, String>();

	/** @return the name */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/** @return the qualifier */
	public String getQualifier()
	{
		return qualifier;
	}

	/**
	 * @param qualifier
	 *           the qualifier to set
	 */
	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}

	/** @return the productAttrValueMap */
	public Map<String, String> getProductAttrValueMap()
	{
		return productAttrValueMap;
	}
}
