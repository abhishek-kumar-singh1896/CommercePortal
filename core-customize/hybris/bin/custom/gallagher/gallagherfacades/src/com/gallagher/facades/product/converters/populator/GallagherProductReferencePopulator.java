/**
 *
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductReferencePopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.util.Assert;


/**
 * @author shilpiverma
 *
 */
public class GallagherProductReferencePopulator extends ProductReferencePopulator
{

	private Converter<ProductModel, ProductData> productCon;

	/**
	 * @return the productCon
	 */
	public Converter<ProductModel, ProductData> getProductCon()
	{
		return productCon;
	}

	/**
	 * @param productCon
	 *           the productCon to set
	 */
	public void setProductCon(final Converter<ProductModel, ProductData> productCon)
	{
		this.productCon = productCon;
	}

	@Override
	public void populate(final ProductReferenceModel source, final ProductReferenceData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setDescription(source.getDescription());
		target.setPreselected(source.getPreselected());
		target.setQuantity(source.getQuantity());
		target.setReferenceType(source.getReferenceType());
		final ProductModel product = source.getTarget();
		target.setTarget(product == null ? null : productCon.convert(product));
	}
}
