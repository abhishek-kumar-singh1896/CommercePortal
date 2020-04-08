/**
 *
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductReferencePopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * @author shilpiverma
 *
 */
public class GallagherProductReferencePopulator extends ProductReferencePopulator
{

	private Populator<ProductModel, ProductData> classificationPopulator;

	public Populator<ProductModel, ProductData> getClassificationPopulator()
	{
		return classificationPopulator;
	}

	@Required
	public void setClassificationPopulator(final Populator<ProductModel, ProductData> classificationPopulator)
	{
		this.classificationPopulator = classificationPopulator;
	}

	@Override
	public void populate(final ProductReferenceModel source, final ProductReferenceData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		super.populate(source, target);
		if (ProductReferenceTypeEnum.UPSELLING.equals(source.getReferenceType()))
		{
			getClassificationPopulator().populate(source.getSource(), target.getTarget());
		}
	}
}
