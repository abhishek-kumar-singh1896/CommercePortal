/**
 *
 */
package com.enterprisewide.b2badvance.facades.product.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.variants.model.GenericVariantProductModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.facades.product.data.B2BAdvanceVariantProductData;


/**
 * Populates B2BAdvanceVariantProductData into generic variant product Model
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceVariantProductReversePopulator<SOURCE extends B2BAdvanceVariantProductData, TARGET extends GenericVariantProductModel>
		extends B2BAdvanceProductReversePopulator<SOURCE, TARGET>
{
	private ProductService productService;

	/**
	 * {@inheritDoc}
	 */
	public void populate(final B2BAdvanceVariantProductData source, final GenericVariantProductModel target)
			throws ConversionException
	{
		super.populate(source, target);
		try
		{
			target.setBaseProduct(productService.getProductForCode(getCatalogVersion(), source.getBaseProduct()));
		}
		catch (UnknownIdentifierException | AmbiguousIdentifierException unIdEx)
		{
			throw new ConversionException(String.format("Base product not found %s", source.getBaseProduct()), unIdEx);
		}

		if (CollectionUtils.isNotEmpty(source.getVariantValueCategories()))
		{
			List<CategoryModel> categories;
			if (target.getSupercategories() == null)
			{
				categories = new ArrayList<>();
			}
			else
			{
				categories = new ArrayList<>(target.getSupercategories());
			}
			target.setSupercategories(categories);
			categories.addAll(getCategories(source.getVariantValueCategories()));
		}

	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}
}
