package com.enterprisewide.b2badvance.core.batch.product.media.attribute.service.impl;

import com.enterprisewide.b2badvance.core.batch.product.media.attribute.service.B2badvanceProductMediaAttributeService;
import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * The B2badvanceProductMediaAttributeServiceImpl class. Implementation of
 * {@link B2badvanceProductMediaAttributeService}
 * 
 * @author Enterprise Wide
 *
 */
public class B2badvanceProductMediaAttributeServiceImpl implements B2badvanceProductMediaAttributeService {

	private static final Logger LOG = Logger.getLogger(B2badvanceProductMediaAttributeServiceImpl.class);

	@Autowired
	private ModelService modelService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private MediaContainerService mediaContainerService;

	@Autowired
	private ImageFormatMapping imageFormatMapping;

	private String mainImageFormat;

	private String thumbnailFormat;

	private List<String> thumbnailsFormat;

	private List<String> detailsImagesFormat;
	
	private List<String> normalImagesFormat;

	private List<String> otherImagesFormat;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProductMediaAttributes(ProductModel product) {
		Assert.notNull(product, "Product cannot be null.");
		final MediaContainerModel primaryImageMediaContainer = getPrimaryImageMediaContainer(product);
		if (primaryImageMediaContainer != null) {
			setProductMainImage(product, primaryImageMediaContainer);
			setProductThumbNail(product, primaryImageMediaContainer);
			setProductThumbNails(product, primaryImageMediaContainer);
			setProductDetailsMedias(product, primaryImageMediaContainer);
			setProductNormalMedias(product, primaryImageMediaContainer);
			setProductOtherMedias(product, primaryImageMediaContainer);
		} else {
			LOG.error("No primary/main image media container found for product code : " + product.getCode());
		}

	}

	/**
	 * Method to get the primary image media container of the product
	 * 
	 * @param productModel
	 *            the product
	 * @return the primary image media container
	 */
	private MediaContainerModel getPrimaryImageMediaContainer(final ProductModel productModel) {
		final MediaModel picture = (MediaModel) getProductAttribute(productModel, ProductModel.PICTURE);
		MediaContainerModel primaryMediaContainer = null;
		if (picture == null) {
			LOG.error("No primary/main image found for prodcut code : " + productModel.getCode());
		} else {
			primaryMediaContainer = picture.getMediaContainer();
		}
		return primaryMediaContainer;
	}

	/**
	 * Method to get the product media by image format from the media container
	 * provided
	 * 
	 * @param product
	 *            the product
	 * @param mediaContainer
	 *            the media container
	 * @param imageFormat
	 *            the image format to get required media
	 * @return the media for the specified format
	 */
	private MediaModel getMediaByFormat(final ProductModel product, final MediaContainerModel mediaContainer,
                                        String imageFormat) {
		MediaModel media = null;
		try {
			final String mediaFormatQualifier = imageFormatMapping.getMediaFormatQualifierForImageFormat(imageFormat);
			if (mediaFormatQualifier != null) {
				final MediaFormatModel mediaFormat = mediaService.getFormat(mediaFormatQualifier);
				if (mediaFormat != null) {
					media = mediaContainerService.getMediaForFormat(mediaContainer, mediaFormat);
				}
			}
		} catch (final ModelNotFoundException ignore) {
			//ignore
			LOG.info(String.format("No media found for format %s for product code - %s and medai container qualifier - %s",
					imageFormat, product.getCode(), mediaContainer.getQualifier()));
		}
		return media;

	}

	/**
	 * Get an attribute value from a product. If the attribute value is null and
	 * the product is a variant then the same attribute will be requested from
	 * the base product.
	 *
	 * @param productModel
	 *            the product
	 * @param attribute
	 *            the name of the attribute to lookup
	 * @return the value of the attribute
	 */
	private Object getProductAttribute(final ProductModel productModel, final String attribute) {
		final Object value = modelService.getAttributeValue(productModel, attribute);
		if (value == null && productModel instanceof VariantProductModel) {
			final ProductModel baseProduct = ((VariantProductModel) productModel).getBaseProduct();
			if (baseProduct != null) {
				return getProductAttribute(baseProduct, attribute);
			}
		}
		return value;
	}

	/**
	 * Method the set the required format main/primary image of the product from
	 * the primary image media container
	 * 
	 * @param product
	 *            the product
	 * @param primaryImageMediaContainer
	 *            the primary image media container
	 */
	private void setProductMainImage(final ProductModel product, final MediaContainerModel primaryImageMediaContainer) {
		MediaModel media = getMediaByFormat(product, primaryImageMediaContainer, mainImageFormat);
		if (media != null) {
			product.setPicture(media);
			modelService.save(product);
		}
	}

	/**
	 * Method the set the required format thumbnail image of the product from
	 * the primary image media container
	 * 
	 * @param product
	 *            the product
	 * @param primaryImageMediaContainer
	 *            the primary image media container
	 */
	private void setProductThumbNail(final ProductModel product, final MediaContainerModel primaryImageMediaContainer) {
		MediaModel media = getMediaByFormat(product, primaryImageMediaContainer, thumbnailFormat);
		if (media != null) {
			product.setThumbnail(media);
			modelService.save(product);
		}

	}

	/**
	 * Method the set the required format thumbnails of the product from the
	 * primary image media container
	 * 
	 * @param product
	 *            the product
	 * @param primaryImageMediaContainer
	 *            the primary image media container
	 */
	private void setProductThumbNails(final ProductModel product,
			final MediaContainerModel primaryImageMediaContainer) {
		if (CollectionUtils.isNotEmpty(thumbnailsFormat)) {
			List<MediaModel> thumbnailsMediaList = new ArrayList<MediaModel>(otherImagesFormat.size());
			for (String format : thumbnailsFormat) {
				MediaModel media = getMediaByFormat(product, primaryImageMediaContainer, format);
				if (media != null) {
					thumbnailsMediaList.add(media);
				}
			}
			if (CollectionUtils.isNotEmpty(thumbnailsMediaList)) {
				product.setThumbnails(thumbnailsMediaList);
				modelService.save(product);
			}
		}
	}

	/**
	 * Method the set the required format details image of the product from the
	 * primary image media container
	 * 
	 * @param product
	 *            the product
	 * @param primaryImageMediaContainer
	 *            the primary image media container
	 */
	private void setProductDetailsMedias(final ProductModel product,
			final MediaContainerModel primaryImageMediaContainer) {
		if (CollectionUtils.isNotEmpty(detailsImagesFormat)) {
			List<MediaModel> detailsMediaList = new ArrayList<MediaModel>(detailsImagesFormat.size());
			for (String format : detailsImagesFormat) {
				MediaModel media = getMediaByFormat(product, primaryImageMediaContainer, format);
				if (media != null) {
					detailsMediaList.add(media);
				}
			}
			if (CollectionUtils.isNotEmpty(detailsMediaList)) {
				product.setDetail(detailsMediaList);
				modelService.save(product);
			}
		}
	}
	
	/**
	 * Method the set the required format details image of the product from the
	 * primary image media container
	 * 
	 * @param product
	 *            the product
	 * @param primaryImageMediaContainer
	 *            the primary image media container
	 */
	private void setProductNormalMedias(final ProductModel product,
			final MediaContainerModel primaryImageMediaContainer) {
		if (CollectionUtils.isNotEmpty(normalImagesFormat)) {
			List<MediaModel> normalMediaList = new ArrayList<MediaModel>(normalImagesFormat.size());
			for (String format : normalImagesFormat) {
				MediaModel media = getMediaByFormat(product, primaryImageMediaContainer, format);
				if (media != null) {
					normalMediaList.add(media);
				}
			}
			if (CollectionUtils.isNotEmpty(normalMediaList)) {
				product.setNormal(normalMediaList);
				modelService.save(product);
			}
		}
	}

	/**
	 * Method the set the required format other images of the product from the
	 * primary image media container
	 * 
	 * @param product
	 *            the product
	 * @param primaryImageMediaContainer
	 *            the primary image media container
	 */
	private void setProductOtherMedias(final ProductModel product,
			final MediaContainerModel primaryImageMediaContainer) {
		if (CollectionUtils.isNotEmpty(otherImagesFormat)) {
			List<MediaModel> otherMediaList = new ArrayList<MediaModel>(otherImagesFormat.size());
			for (String format : otherImagesFormat) {
				MediaModel media = getMediaByFormat(product, primaryImageMediaContainer, format);
				if (media != null) {
					otherMediaList.add(media);
				}
			}
			if (CollectionUtils.isNotEmpty(otherMediaList)) {
				product.setOthers(otherMediaList);
				modelService.save(product);
			}
		}
	}

	@Required
	public void setMainImageFormat(String mainImageFormat) {
		this.mainImageFormat = mainImageFormat;
	}

	@Required
	public void setThumbnailFormat(String thumbnailFormat) {
		this.thumbnailFormat = thumbnailFormat;
	}

	public void setThumbnailsFormat(List<String> thumbnailsFormat) {
		this.thumbnailsFormat = thumbnailsFormat;
	}
	
	public void setDetailsImagesFormat(List<String> detailsImagesFormat) {
		this.detailsImagesFormat = detailsImagesFormat;
	}
	
	public void setNormalImagesFormat(List<String> normalImagesFormat) {
		this.normalImagesFormat = normalImagesFormat;
	}

	public void setOtherImagesFormat(List<String> otherImagesFormat) {
		this.otherImagesFormat = otherImagesFormat;
	}

	

}
