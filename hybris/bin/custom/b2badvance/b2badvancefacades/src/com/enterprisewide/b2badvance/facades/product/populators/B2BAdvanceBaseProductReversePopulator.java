/**
 *
 */
package com.enterprisewide.b2badvance.facades.product.populators;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.GenericVariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.facades.product.data.B2BAdvanceBaseProductData;


/**
 * Populates B2BAdvanceBaseProductData into product Model
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceBaseProductReversePopulator<SOURCE extends B2BAdvanceBaseProductData, TARGET extends ProductModel>
		extends B2BAdvanceProductReversePopulator<SOURCE, TARGET>
{

	private TypeService typeService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void populate(final B2BAdvanceBaseProductData source, final ProductModel target) throws ConversionException
	{
		super.populate(source, target);
		target.setVariantType((VariantTypeModel) typeService.getComposedTypeForCode(GenericVariantProductModel._TYPECODE));
		target.getSupercategories().addAll(getCategories(source.getVariantCategories()));
	}


	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}
}
