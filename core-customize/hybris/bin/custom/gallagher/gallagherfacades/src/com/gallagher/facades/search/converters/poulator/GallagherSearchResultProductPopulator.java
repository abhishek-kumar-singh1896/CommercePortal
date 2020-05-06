/**
 *
 */
package com.gallagher.facades.search.converters.poulator;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;



/**
 * @author gauravkamboj
 *
 */
public class GallagherSearchResultProductPopulator extends SearchResultProductPopulator
{

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		super.populate(source, target);
		final String marketingDescription = this.<String> getValue(source, "marketingDescription");
		if (!StringUtils.isEmpty(marketingDescription))
		{
			target.setName(marketingDescription);
		}
		if (!StringUtils.isEmpty(this.<String> getValue(source, "promoSticker")))
		{
			target.setPromoSticker((this.<String> getValue(source, "promoSticker")).toLowerCase());
		}

		if (CollectionUtils.isNotEmpty(this.<List<String>> getValue(source, "animalCompatibility")))
		{
			final List<String> animalList = this.<List<String>> getValue(source, "animalCompatibility");
			animalList.replaceAll(String::toLowerCase);
			target.setAnimalCompatibility(animalList);
		}
		if (!StringUtils.isEmpty(this.<String> getValue(source, "plpProductDescription")))
		{
			target.setPlpProductDescription(this.<String> getValue(source, "plpProductDescription"));
		}
		target.setPartNumber(this.<String> getValue(source, "partNumber"));
		target.setCategoryPaths(this.<List<String>> getValue(source, "categoryPath"));
		target.setModifiedTime(this.<Date> getValue(source, "modifiedtime"));

	}

}
