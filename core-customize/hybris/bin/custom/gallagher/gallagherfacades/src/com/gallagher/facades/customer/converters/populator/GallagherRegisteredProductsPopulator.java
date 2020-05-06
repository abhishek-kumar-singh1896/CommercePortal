/**
 *
 */
package com.gallagher.facades.customer.converters.populator;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;

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

	@Resource(name = "productService")
	private ProductService productService;

	private BaseSiteService baseSiteService;

	private CatalogVersionService catalogVersionService;

	private SearchRestrictionService searchRestrictionService;

	private SessionService sessionService;

	@Override
	public void populate(final GallagherRegisteredProduct source, final RegisteredProductData target) throws ConversionException
	{
		target.setCode(source.getSerialID());
		final ProductModel productModel = getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				ProductModel productModel;
				try
				{
					getSearchRestrictionService().disableSearchRestrictions();
					productModel = productService.getProductForCode(getCatalogVersion(), source.getProductID());
				}
				catch (final UnknownIdentifierException e)
				{
					productModel = null;
					LOGGER.error("Product {} not found.", source.getProductID());
				}
				finally
				{
					getSearchRestrictionService().enableSearchRestrictions();
				}
				return productModel;
			}
		});

		if (null != productModel)
		{
			final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC));
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

	protected CatalogVersionModel getCatalogVersion()
	{
		CatalogVersionModel catalogVersion = null;

		if (getBaseSiteService().getCurrentBaseSite() != null)
		{
			catalogVersion = ((CMSSiteModel) getBaseSiteService().getCurrentBaseSite()).getDefaultCatalog().getCatalogVersions()
					.stream().filter(catalog -> !catalog.getActive()).findFirst().get();
		}
		return catalogVersion;
	}


	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}


	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}


	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}


	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}


	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public SearchRestrictionService getSearchRestrictionService()
	{
		return searchRestrictionService;
	}


	public void setSearchRestrictionService(final SearchRestrictionService searchRestrictionService)
	{
		this.searchRestrictionService = searchRestrictionService;
	}
}
