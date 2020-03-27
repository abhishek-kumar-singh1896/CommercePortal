/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.PDPSimulatorData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.gallagher.core.model.PDPSimulatorModel;


/**
 * Populate the product data with the product's summary description
 */
public class GallagherProductSimulatorPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends AbstractProductPopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		//		productData.setSimulator(safeToString(getProductAttribute(productModel, ProductModel.SIMULATOR)));
		final PDPSimulatorModel pdpSimulator = productModel.getSimulator();
		final PDPSimulatorData simulatorData = new PDPSimulatorData();
		if (pdpSimulator != null)
		{
			simulatorData.setSimulatorDescription(pdpSimulator.getSimulatorDescription());
			simulatorData.setSimulatorURL(pdpSimulator.getSimulatorUrl());
		}
		productData.setSimulator(simulatorData);
	}
}

