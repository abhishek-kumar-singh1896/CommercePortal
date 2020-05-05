/**
 *
 */
package com.gallagher.facades.customer.converters.populator;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gallagher.facades.product.data.RegisteredProductData;
import com.gallagher.outboundservices.response.dto.GallagherRegisteredProduct;
import com.gallagher.outboundservices.response.dto.GallagherRegisteredProductAttachmentFolderItem;


/**
 *
 */
public class GallagherRegisteredProductsPopulator implements Populator<GallagherRegisteredProduct, RegisteredProductData>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GallagherRegisteredProductsPopulator.class);

	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@Override
	public void populate(final GallagherRegisteredProduct source, final RegisteredProductData target) throws ConversionException
	{
		target.setCode(source.getSerialID());

		final ProductData productData = productFacade.getProductForCodeAndOptions(source.getProductID(),
				Arrays.asList(ProductOption.BASIC));

		if (null != productData)
		{
			target.setName(productData.getName());

			final Collection<ImageData> images = productData.getImages();
			if (CollectionUtils.isNotEmpty(images))
			{
				for (final ImageData image : productData.getImages())
				{
					if (image.getFormat().equals("thumbnail"))
					{
						target.setImage(image);
						break;
					}
				}
			}
		}

		if (null != source.getRegisteredProductAttachmentFolder()
				&& null != source.getRegisteredProductAttachmentFolder().getRegisteredProductAttachmentFolderItem())
		{
			final GallagherRegisteredProductAttachmentFolderItem attachment = source.getRegisteredProductAttachmentFolder()
					.getRegisteredProductAttachmentFolderItem();

			target.setAttachment(attachment.getName());
			target.setAttachmentUrl(attachment.getDocumentLink());
		}

		final SimpleDateFormat dateFromat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		try
		{
			target.setPurchaseDate(dateFromat.parse(source.getReferenceDate()));
			target.setRegistrationDate(dateFromat.parse(source.getCreationDateTime()));
		}
		catch (final ParseException exception)
		{
			LOGGER.error("Exception occured while parsing the Date.", exception);
		}

	}


}
