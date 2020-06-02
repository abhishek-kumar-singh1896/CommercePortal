/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.variants.model.GenericVariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.log4j.Logger;

import com.gallagher.core.dao.GallagherProductProcessingDao;
import com.gallagher.core.services.GallagherProductProcessingService;


/**
 * Implementation of GallagherProductProcessingService
 *
 * @author shishirkant
 *
 */
public class GallagherProductProcessingServiceImpl implements GallagherProductProcessingService
{

	@Resource(name = "typeService")
	private TypeService typeService;

	@Resource(name = "modelService")
	protected ModelService modelService;

	@Resource(name = "mediaService")
	protected MediaService mediaService;

	@Resource(name = "productService")
	protected ProductService productService;

	@Resource(name = "categoryService")
	protected CategoryService categoryService;

	@Resource(name = "baseSiteService")
	protected BaseSiteService baseSiteService;

	@Resource(name = "catalogVersionService")
	protected CatalogVersionService catalogVersionService;

	@Resource(name = "gallagherProductProcessingDao")
	protected GallagherProductProcessingDao gallagherProductProcessingDao;

	@Resource(name = "baseStoreService")
	protected BaseStoreService baseStoreService;

	private static final Logger LOGGER = Logger.getLogger(GallagherProductProcessingServiceImpl.class);
	private static final String STAGED = "Staged";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createBaseProduct(final String catalogId, final Date lastStartTime)
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalogId, STAGED);

		final List<ProductModel> products = gallagherProductProcessingDao.getProductsForConversion(catalogVersion, lastStartTime);

		final Set<Locale> locales = new HashSet<>();

		for (final ProductModel product : products)
		{
			final String baseProductCode = product.getBaseProductCode();

			final List<BaseStoreModel> baseStores = new ArrayList<>();
			baseStores.addAll(product.getBaseStores());

			if (product.isEligibleForLatAm() && catalogId.contains("B2C"))
			{
				baseStores.add(baseStoreService.getBaseStoreForUid("amB2CLatAm"));
			}

			for (final BaseStoreModel baseStore : baseStores)
			{
				for (final LanguageModel language : baseStore.getLanguages())
				{
					locales.add(LocaleUtils.toLocale(language.getIsocode()));
				}
			}

			try
			{
				final ProductModel existingProduct = productService.getProductForCode(catalogVersion, baseProductCode);

				LOGGER.info("Base Product with code " + baseProductCode + " and CatalogVersion " + catalogId
						+ " found, Updating the information.");

				for (final Locale locale : locales)
				{
					existingProduct.setName(product.getName(locale), locale);
					existingProduct.setDescription(product.getDescription(locale), locale);
				}

				final Collection<BaseStoreModel> combinedBaseStores = CollectionUtils.union(baseStores,
						existingProduct.getBaseStores());

				existingProduct.setBaseStores(combinedBaseStores);
				existingProduct.setSupercategories(getCategories(product.getSupercategories()));
				modelService.save(existingProduct);
			}
			catch (final UnknownIdentifierException exception)
			{
				LOGGER.info("Base Product with code " + baseProductCode + " and CatalogVersion " + catalogId
						+ " not found, Creating new one.");

				final ProductModel newProduct = new ProductModel();
				newProduct.setCode(baseProductCode);

				for (final Locale locale : locales)
				{
					newProduct.setName(product.getName(locale), locale);
					newProduct.setDescription(product.getDescription(locale), locale);
				}

				newProduct.setBaseStores(baseStores);
				newProduct.setCatalogVersion(catalogVersion);
				newProduct.setSupercategories(getCategories(product.getSupercategories()));
				newProduct
						.setVariantType((VariantTypeModel) typeService.getComposedTypeForCode(GenericVariantProductModel._TYPECODE));
				modelService.save(newProduct);
			}
		}
	}

	/**
	 * Returns the Collection of Categories applicable for Base Product
	 *
	 * @param supercategories
	 * @return categories
	 */
	private Collection<CategoryModel> getCategories(final Collection<CategoryModel> supercategories)
	{
		final List<CategoryModel> categories = new ArrayList<>();

		for (final CategoryModel category : supercategories)
		{
			if (category instanceof VariantValueCategoryModel)
			{
				categories.addAll(((VariantValueCategoryModel) category).getSupercategories());
			}
			else
			{
				categories.add(category);
			}
		}

		return categories;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createAndProcessVariantProduct(final String catalogId, final Date lastStartTime)
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalogId, STAGED);

		final List<ProductModel> products = gallagherProductProcessingDao.getVariantProductsForTransformation(catalogVersion,
				lastStartTime);
		boolean success = true;
		for (final ProductModel product : products)
		{
			try
			{
				final String variantProductCode = product.getCode();
				final String baseProductCode = product.getBaseProductCode();

				final List<BaseStoreModel> baseStores = new ArrayList<>();
				baseStores.addAll(product.getBaseStores());

				if (product.isEligibleForLatAm() && catalogId.contains("B2C"))
				{
					baseStores.add(baseStoreService.getBaseStoreForUid("amB2CLatAm"));
				}

				final Map<BaseStoreModel, CatalogVersionModel> storeCatalogMap = processVariantProductForCode(variantProductCode,
						baseProductCode, baseStores, catalogId);

				for (final BaseStoreModel baseStore : baseStores)
				{
					final CatalogVersionModel regionalCatalogVersion = storeCatalogMap.get(baseStore);

					final ProductModel baseProduct = productService.getProductForCode(regionalCatalogVersion, baseProductCode);

					try
					{
						final ProductModel existingProduct = productService.getProductForCode(regionalCatalogVersion,
								variantProductCode);

						LOGGER.info("Variant Product with code " + variantProductCode + " and CatalogVersion "
								+ regionalCatalogVersion.getCatalog().getId() + " found, Updating the information.");

						final GenericVariantProductModel existingVariantProduct = (GenericVariantProductModel) existingProduct;
						existingVariantProduct.setApprovalStatus(ArticleApprovalStatus.APPROVED);

						for (final LanguageModel language : baseStore.getLanguages())
						{
							variantProductProcessing(existingVariantProduct, language, product);
						}

						existingVariantProduct.setVariantForImage(product.isVariantForImage());

						final Collection<CategoryModel> variantCategories = getVariantCategories(product.getSupercategories(),
								regionalCatalogVersion);
						if (!CollectionUtils.isEmpty(variantCategories))
						{
							existingVariantProduct.setSupercategories(variantCategories);
						}
						else
						{
							LOGGER.error("Product with code " + variantProductCode + " and CatalogVersion "
									+ regionalCatalogVersion.getCatalog().getId()
									+ " is not saved due to variant categories are not proper.");
							continue;
						}

						populateVariantData(product, existingVariantProduct);

						populateImages(product, existingVariantProduct, regionalCatalogVersion);

						modelService.save(existingVariantProduct);

					}
					catch (final UnknownIdentifierException exception)
					{
						LOGGER.info("Variant Product with code " + variantProductCode + " and CatalogVersion "
								+ regionalCatalogVersion.getCatalog().getId() + " not found, Creating new one.");

						final GenericVariantProductModel newVariantProduct = new GenericVariantProductModel();
						newVariantProduct.setCode(variantProductCode);
						newVariantProduct.setApprovalStatus(ArticleApprovalStatus.APPROVED);

						for (final LanguageModel language : baseStore.getLanguages())
						{
							variantProductProcessing(newVariantProduct, language, product);
						}

						newVariantProduct.setVariantForImage(product.isVariantForImage());
						newVariantProduct.setCatalogVersion(regionalCatalogVersion);

						final Collection<CategoryModel> variantCategories = getVariantCategories(product.getSupercategories(),
								regionalCatalogVersion);
						if (!CollectionUtils.isEmpty(variantCategories))
						{
							newVariantProduct.setSupercategories(variantCategories);
						}
						else
						{
							LOGGER.error("Product with code " + variantProductCode + " and CatalogVersion "
									+ regionalCatalogVersion.getCatalog().getId()
									+ " is not saved due to variant categories are not proper.");
							continue;
						}

						newVariantProduct.setBaseProduct(baseProduct);

						populateVariantData(product, newVariantProduct);

						populateImages(product, newVariantProduct, regionalCatalogVersion);

						modelService.save(newVariantProduct);
					}

				}
			}
			catch (final Exception ex)
			{
				success = false;
				LOGGER.error("There is some problem while transforming Product [" + product.getCode() + "]", ex);
			}
		}
		return success;
	}

	/**
	 * @param existingVariantProduct
	 * @param language
	 * @param product
	 */
	private void variantProductProcessing(final GenericVariantProductModel existingVariantProduct, final LanguageModel language,
			final ProductModel product)
	{
		final Locale locale = LocaleUtils.toLocale(language.getIsocode());
		if (locale != null)
		{
			existingVariantProduct.setName(product.getName(locale), locale);
			existingVariantProduct.setSummary(product.getSummary(locale), locale);
			existingVariantProduct.setDescription(product.getDescription(locale), locale);
		}
		if (null != language.getFallbackLanguages())
		{
			final Locale fallbackLocale = LocaleUtils.toLocale(language.getFallbackLanguages().get(0).getIsocode());
			existingVariantProduct.setName(product.getName(fallbackLocale), fallbackLocale);
			existingVariantProduct.setSummary(product.getSummary(fallbackLocale), fallbackLocale);
			existingVariantProduct.setDescription(product.getDescription(fallbackLocale), fallbackLocale);
		}

	}

	/**
	 * @param product
	 * @param variantProduct
	 */
	private void populateVariantData(final ProductModel product, final GenericVariantProductModel variantProduct)
	{
		final List<ProductFeatureModel> productFeatures = product.getFeatures();
		final List<ProductFeatureModel> newProductFeatures = new ArrayList<>();
		final List<ProductFeatureModel> oldProductFeatures = variantProduct.getFeatures();

		if (CollectionUtils.isNotEmpty(oldProductFeatures))
		{
			modelService.removeAll(oldProductFeatures);
		}
		if (CollectionUtils.isNotEmpty(productFeatures))
		{
			for (final ProductFeatureModel productFeature : productFeatures)
			{
				final ProductFeatureModel newProductFeature = modelService.clone(productFeature);

				newProductFeature.setProduct(variantProduct);

				newProductFeatures.add(newProductFeature);
			}
			modelService.saveAll(newProductFeatures);
		}

	}

	/**
	 * Populate the Images from Master Variant Product to Regional Variant Product
	 *
	 * @param product
	 * @param variantProduct
	 * @param regionalCatalogVersion
	 */
	private void populateImages(final ProductModel product, final GenericVariantProductModel variantProduct,
			final CatalogVersionModel regionalCatalogVersion)
	{
		final MediaModel picture = product.getPicture();
		final MediaModel thumbnail = product.getThumbnail();
		final Collection<MediaModel> detail = product.getDetail();
		final Collection<MediaModel> others = product.getOthers();
		final Collection<MediaModel> normal = product.getNormal();
		final Collection<MediaModel> thumbnails = product.getThumbnails();
		final Collection<MediaContainerModel> galleryImages = product.getGalleryImages();
		final Collection<MediaModel> data_sheet = product.getData_sheet();

		if (null != picture)
		{
			try
			{
				variantProduct.setPicture(mediaService.getMedia(regionalCatalogVersion, picture.getCode()));
			}
			catch (final UnknownIdentifierException exception)
			{
				LOGGER.info("Picture Media with code " + picture.getCode() + " and CatalogVersion "
						+ regionalCatalogVersion.getCatalog().getId() + " not found, Ignoring...");
			}
		}
		if (null != thumbnail)
		{
			try
			{
				variantProduct.setThumbnail(mediaService.getMedia(regionalCatalogVersion, thumbnail.getCode()));
			}
			catch (final UnknownIdentifierException exception)
			{
				LOGGER.info("Thumbnail Media with code " + thumbnail.getCode() + " and CatalogVersion "
						+ regionalCatalogVersion.getCatalog().getId() + " not found, Ignoring...");
			}
		}
		if (!CollectionUtils.isEmpty(detail))
		{
			final Set<MediaModel> mediaSet = new HashSet<>();
			for (final MediaModel media : detail)
			{
				try
				{
					mediaSet.add(mediaService.getMedia(regionalCatalogVersion, media.getCode()));
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.info("Media for Detail with code " + media.getCode() + " and CatalogVersion "
							+ regionalCatalogVersion.getCatalog().getId() + " not found, Ignoring...");
				}
			}
			variantProduct.setDetail(mediaSet);
		}
		if (!CollectionUtils.isEmpty(others))
		{
			final Set<MediaModel> mediaSet = new HashSet<>();
			for (final MediaModel media : others)
			{
				try
				{
					mediaSet.add(mediaService.getMedia(regionalCatalogVersion, media.getCode()));
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.info("Media for Others with code " + media.getCode() + " and CatalogVersion "
							+ regionalCatalogVersion.getCatalog().getId() + " not found, Ignoring...");
				}
			}
			variantProduct.setOthers(mediaSet);
		}
		if (!CollectionUtils.isEmpty(data_sheet))
		{
			final Set<MediaModel> mediaSet = new HashSet<>();
			for (final MediaModel media : data_sheet)
			{
				try
				{
					mediaSet.add(mediaService.getMedia(regionalCatalogVersion, media.getCode()));
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.info("Media for data sheet with code " + media.getCode() + " and CatalogVersion "
							+ regionalCatalogVersion.getCatalog().getId() + " not found, Ignoring...");
				}
			}
			variantProduct.setData_sheet(mediaSet);
		}
		if (!CollectionUtils.isEmpty(normal))
		{
			final Set<MediaModel> mediaSet = new HashSet<>();
			for (final MediaModel media : normal)
			{
				try
				{
					mediaSet.add(mediaService.getMedia(regionalCatalogVersion, media.getCode()));
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.info("Media for Normal with code " + media.getCode() + " and CatalogVersion "
							+ regionalCatalogVersion.getCatalog().getId() + " not found, Ignoring...");
				}
			}
			variantProduct.setNormal(mediaSet);
		}
		if (!CollectionUtils.isEmpty(thumbnails))
		{
			final Set<MediaModel> mediaSet = new HashSet<>();
			for (final MediaModel media : thumbnails)
			{
				try
				{
					mediaSet.add(mediaService.getMedia(regionalCatalogVersion, media.getCode()));
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.info("Media for Thumbnails with code " + media.getCode() + " and CatalogVersion "
							+ regionalCatalogVersion.getCatalog().getId() + " not found, Ignoring...");
				}
			}
			variantProduct.setThumbnails(mediaSet);
		}
		if (!CollectionUtils.isEmpty(galleryImages))
		{
			final List<MediaContainerModel> mediaContainerList = new ArrayList<>();
			for (final MediaContainerModel mediaContainer : galleryImages)
			{
				final MediaContainerModel existingMediaContainer = gallagherProductProcessingDao
						.getMediaContainerForQualifier(mediaContainer.getQualifier(), regionalCatalogVersion);
				if (null != existingMediaContainer)
				{
					mediaContainerList.add(existingMediaContainer);
				}
				else
				{
					LOGGER.info("Media Container with qualifier " + mediaContainer.getQualifier() + " and CatalogVersion "
							+ regionalCatalogVersion.getCatalog().getId() + " not found, Ignoring...");
				}
			}
			variantProduct.setGalleryImages(mediaContainerList);
		}

	}

	/**
	 * Returns the Collection of Categories applicable for Variant Product
	 *
	 * @param supercategories
	 * @param regionalCatalogVersion
	 * @return variantCategories
	 */
	private Collection<CategoryModel> getVariantCategories(final Collection<CategoryModel> supercategories,
			final CatalogVersionModel regionalCatalogVersion)
	{
		final List<CategoryModel> variantCategories = new ArrayList<>();

		for (final CategoryModel category : supercategories)
		{
			if (category instanceof VariantValueCategoryModel)
			{
				try
				{
					variantCategories.add(categoryService.getCategoryForCode(regionalCatalogVersion, category.getCode()));
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error("Category with code " + category.getCode() + " and CatalogVersion "
							+ regionalCatalogVersion.getCatalog().getId() + " not found.");
					return Collections.emptyList();
				}
			}
		}

		return variantCategories;
	}

	/**
	 * Returns the Map of BaseStoreModel & CatalogVersionModel and will process the Variant Product for Approval Status
	 *
	 * @param variantProductCode
	 * @param baseProductCode
	 * @param baseStores
	 * @param catalogId
	 * @return storeCatalogMap
	 */
	private Map<BaseStoreModel, CatalogVersionModel> processVariantProductForCode(final String variantProductCode,
			final String baseProductCode, final Collection<BaseStoreModel> baseStores, final String catalogId)
	{
		final List<CatalogVersionModel> allAvailableCatalogVersionForCode = gallagherProductProcessingDao
				.getAvailableCatalogVersionForCode(variantProductCode);

		final Map<BaseStoreModel, CatalogVersionModel> storeCatalogMap = new HashMap<>();

		final List<CatalogVersionModel> unapprovedForCatalogVersions = new ArrayList<>();

		for (final BaseStoreModel baseStore : baseStores)
		{
			final CatalogVersionModel regionalCatalogVersion = baseSiteService
					.getProductCatalogs(baseStore.getCmsSites().iterator().next()).get(0).getCatalogVersions().stream()
					.filter(catalog -> !catalog.getActive()).findFirst().get();
			storeCatalogMap.put(baseStore, regionalCatalogVersion);
		}

		for (final CatalogVersionModel catalogVersion : allAvailableCatalogVersionForCode)
		{
			if (!catalogVersion.getCatalog().getId().contains("Master") && null != catalogVersion.getActive()
					&& !catalogVersion.getActive() && !storeCatalogMap.containsKey(catalogVersion)
					&& (catalogId.contains("B2B") && catalogVersion.getCatalog().getId().contains("B2B")
							|| catalogId.contains("B2C") && catalogVersion.getCatalog().getId().contains("B2C")))
			{
				unapprovedForCatalogVersions.add(catalogVersion);
			}
		}

		unapproveVariantProductForCode(variantProductCode, baseProductCode, unapprovedForCatalogVersions);

		return storeCatalogMap;
	}

	/**
	 * Will mark the Approval Status as Unapproved for Variant Product and Base Product accordingly.
	 *
	 * @param variantProductCode
	 * @param baseProductCode
	 * @param unapprovedForCatalogVersions
	 */
	private void unapproveVariantProductForCode(final String variantProductCode, final String baseProductCode,
			final List<CatalogVersionModel> unapprovedForCatalogVersions)
	{
		if (CollectionUtils.isNotEmpty(unapprovedForCatalogVersions))
		{
			for (final CatalogVersionModel unapprovedForCatalogVersion : unapprovedForCatalogVersions)
			{
				try
				{
					final GenericVariantProductModel variantProduct = (GenericVariantProductModel) productService
							.getProductForCode(unapprovedForCatalogVersion, variantProductCode);
					variantProduct.setApprovalStatus(ArticleApprovalStatus.UNAPPROVED);
					modelService.save(variantProduct);

					final ProductModel baseProduct = productService.getProductForCode(unapprovedForCatalogVersion, baseProductCode);


					final List<GenericVariantProductModel> approvedVariantProducts = gallagherProductProcessingDao
							.getApprovedVariantProductsForBaseProduct(baseProduct, unapprovedForCatalogVersion);
					if (CollectionUtils.isEmpty(approvedVariantProducts))
					{
						baseProduct.setApprovalStatus(ArticleApprovalStatus.UNAPPROVED);
						modelService.save(baseProduct);
					}

				}
				catch (final UnknownIdentifierException exception)
				{
					// Ignore
				}
			}
		}

	}
}
