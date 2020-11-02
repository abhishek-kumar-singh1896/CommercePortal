/**
 *
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * @author ishaanvashishth
 *
 */
public class GallagherProductRecommendationsPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends AbstractProductPopulator<SOURCE, TARGET>
{

	private Converter<ProductModel, ProductData> productConverter;

	/**
	 * @return the productConverter
	 */
	public Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}


	/**
	 * @param productConverter
	 *           the productConverter to set
	 */
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}


	/**
	 * @return the categoryConverter
	 */
	public Converter<CategoryModel, CategoryData> getCategoryConverter()
	{
		return categoryConverter;
	}


	/**
	 * @param categoryConverter
	 *           the categoryConverter to set
	 */
	public void setCategoryConverter(final Converter<CategoryModel, CategoryData> categoryConverter)
	{
		this.categoryConverter = categoryConverter;
	}


	private Converter<CategoryModel, CategoryData> categoryConverter;


	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		if (null != source.getRecommendedCategories() && !source.getRecommendedCategories().isEmpty())
		{
			target.setRecommendedCategories(Converters.convertAll(source.getRecommendedCategories(), categoryConverter));
		}
		if (null != source.getRecommendedProducts() && !source.getRecommendedProducts().isEmpty())
		{
			target.setRecommendedProducts(Converters.convertAll(source.getRecommendedProducts(), productConverter));
		}
	}

}
