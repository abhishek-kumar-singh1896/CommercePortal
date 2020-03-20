/**
 *
 */
package com.gallagher.facades.customer.converters.populator;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.annotation.Resource;

import com.gallagher.facades.product.converters.populator.GallagherProductPrimaryImagePopulator;
import com.gallagher.facades.product.data.RegisteredProductData;
import com.gallagher.facades.product.data.RegisteredProductsData;
import com.microsoft.sqlserver.jdbc.StringUtils;


/**
 *
 */
public class GallagherRegisteredProductsPopulator implements Populator<String, RegisteredProductsData>
{

	@Resource
	ProductService productService;

	@Resource(name = "productPrimaryImagePopulator")
	GallagherProductPrimaryImagePopulator gallagherProductPrimaryImagePopulator;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final String source, final RegisteredProductsData target) throws ConversionException
	{
		final String uid = source;

		//call C4C method to get RegisteredProductData

		//for the time being, manually creating the RegisteredProductData object
		final ArrayList<RegisteredProductData> registeredProducts = new ArrayList<RegisteredProductData>();
		for (int i = 0; i <= 5; i++)
		{
			final RegisteredProductData regProductData = new RegisteredProductData();
			final ProductModel productModel = getProductService().getProductForCode("solar-fence-energizer-s10");
			final ProductData productData = new ProductData();
			getGallagherProductPrimaryImagePopulator().populate(productModel, productData);
			if (!StringUtils.isEmpty(productData.getName()) && !(productData.getImages().isEmpty()))
			{
				final ArrayList<ImageData> imageDataList = new ArrayList<>(productData.getImages());
				regProductData.setImage(imageDataList.get(0));
			}
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

			regProductData.setCode(productModel.getCode());
			regProductData.setName(productModel.getName());
			regProductData.setAttachment("this is attachment");
			regProductData.setAttachmentUrl("/");
			try
			{
				regProductData.setPurchaseDate(simpleDateFormat.parse("15/03/2020"));
				regProductData.setRegistrationDate(simpleDateFormat.parse("12/01/2020"));
			}
			catch (final ParseException e)
			{
				e.printStackTrace();
			}
			registeredProducts.add(regProductData);
		}
		target.setResgisteredProducts(registeredProducts);

	}

	protected ProductService getProductService()
	{
		return productService;
	}

	protected GallagherProductPrimaryImagePopulator getGallagherProductPrimaryImagePopulator()
	{
		return gallagherProductPrimaryImagePopulator;
	}

	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	public void setGallagherProductPrimaryImagePopulator(
			final GallagherProductPrimaryImagePopulator gallagherProductPrimaryImagePopulator)
	{
		this.gallagherProductPrimaryImagePopulator = gallagherProductPrimaryImagePopulator;
	}

}
