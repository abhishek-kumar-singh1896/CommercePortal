/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.gallagher.core.constants.GallagherCoreConstants;
import com.gallagher.core.cron.bynder.BynderOauthHeaderGenerator;
import com.gallagher.core.cronjob.model.GallagherBynderSyncCronJobModel;
import com.gallagher.core.jobs.dao.GallagherMediaContainerDao;
import com.gallagher.core.model.BynderMediaModel;
import com.gallagher.core.services.GallagherBynderService;
import com.gallagher.outboundservices.response.dto.GallagherBynderMediaResponse;
import com.gallagher.outboundservices.response.dto.GallagherBynderResponse;
import com.google.gson.GsonBuilder;


/**
 *
 */
public class GallagherBynderServiceImpl implements GallagherBynderService
{

	private static final Logger LOGGER = Logger.getLogger(GallagherBynderServiceImpl.class);

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private MediaService mediaService;


	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	private GallagherMediaContainerDao gallagherMediaContainerDao;

	@Override
	public boolean updateMedia(final GallagherBynderSyncCronJobModel cronModel,
			final GallagherBynderResponse gallagherBynderResponse)
	{

		final CatalogVersionModel catlogmodel = catalogVersionService.getCatalogVersion(cronModel.getCatalogId(), "Staged");

		final List<MediaContainerModel> mediaContainers = gallagherMediaContainerDao
				.getMediaContainer(gallagherBynderResponse.getId(), catlogmodel.getPk());

		//delete container if exits
		if (CollectionUtils.isNotEmpty(mediaContainers))
		{
			deleteMedia(cronModel, gallagherBynderResponse);
		}
		LOGGER.info("Creating container for " + gallagherBynderResponse.getId());
		//create a new container and adding images

		//setting MediaContainerModel values
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final MediaContainerModel mediaContainerModel = modelService.create(MediaContainerModel.class);
		mediaContainerModel.setCatalogVersion(catlogmodel);
		mediaContainerModel.setQualifier(gallagherBynderResponse.getId());
		mediaContainerModel.setName(gallagherBynderResponse.getName(), Locale.ENGLISH);


		final BynderMediaModel mediaModel = modelService.create(BynderMediaModel.class);
		final MediaFolderModel folder = mediaService.getFolder(GallagherCoreConstants.Bynder.IMAGES);
		final MediaFormatModel format = mediaService.getFormat(GallagherCoreConstants.Bynder.B2BADVANCE_BF);

		mediaModel.setCode(gallagherBynderResponse.getId());
		mediaModel.setCatalogVersion(catlogmodel);
		mediaModel.setMediaFormat(format);
		mediaModel.setMediaContainer(mediaContainerModel);
		mediaModel.setFolder(folder);


		modelService.save(mediaModel);
		mediaService.setStreamForMedia(mediaModel, getImage(gallagherBynderResponse.getId()));
		LOGGER.info("Media Saved for " + gallagherBynderResponse.getId());

		//getting products and adding container to that product
		if (CollectionUtils.isNotEmpty(gallagherBynderResponse.getProperty_skus()))
		{
			final List<ProductModel> products = gallagherMediaContainerDao
					.getProductModeList(gallagherBynderResponse.getProperty_skus(), catlogmodel.getPk());
			if (CollectionUtils.isNotEmpty(products))
			{
				for (final ProductModel product : products)
				{
					final List<MediaContainerModel> existingContainers = new ArrayList<>(product.getGalleryImages());
					existingContainers.add(mediaContainerModel);
					product.setGalleryImages(existingContainers);
					modelService.save(product);
					LOGGER.info("MediaContainer " + gallagherBynderResponse.getId() + " Saved for " + product.getCode());
				}
			}
		}
		return true;
	}

	@Override
	public boolean deleteMedia(final GallagherBynderSyncCronJobModel cronModel,
			final GallagherBynderResponse gallagherBynderResponse)
	{
		final CatalogVersionModel catlogmodel = catalogVersionService.getCatalogVersion(cronModel.getCatalogId(), "Staged");
		final List<MediaContainerModel> myCustomTypesWithId = gallagherMediaContainerDao
				.getMediaContainer(gallagherBynderResponse.getId(), catlogmodel.getPk());

		if (myCustomTypesWithId.size() > 0)
		{
			final MediaContainerModel mediacontainer = myCustomTypesWithId.get(0);
			final Collection<MediaModel> medias = mediacontainer.getMedias();

			for (final MediaModel media : medias)
			{
				modelService.remove(media);
			}
			modelService.remove(mediacontainer);
		}
		LOGGER.info("Deleted Media " + gallagherBynderResponse.getId());
		return true;

	}


	@Override
	public boolean updateDocumentMedia(final GallagherBynderSyncCronJobModel cronModel,
			final GallagherBynderResponse gallagherBynderResponse)
	{
		final CatalogVersionModel catlogmodel = catalogVersionService.getCatalogVersion(cronModel.getCatalogId(), "Staged");
		LOGGER.info("Updating Media for " + gallagherBynderResponse.getId());
		try
		{
			final MediaModel mediaModel1 = mediaService.getMedia(catlogmodel, gallagherBynderResponse.getId());
			modelService.remove(mediaModel1);
		}
		catch (final UnknownIdentifierException e)
		{
			e.getMessage();
		}

		//setting MediaModel values
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final BynderMediaModel mediaModel = modelService.create(BynderMediaModel.class);
		final MediaFolderModel folder = mediaService.getFolder(GallagherCoreConstants.Bynder.DOCUMENTS);
		mediaModel.setCode(gallagherBynderResponse.getId());
		mediaModel.setCatalogVersion(catlogmodel);
		mediaModel.setFolder(folder);


		modelService.save(mediaModel);
		mediaService.setStreamForMedia(mediaModel, getImage(gallagherBynderResponse.getId()));

		//getting products and adding container to that product
		final List<ProductModel> products = gallagherMediaContainerDao
				.getProductModeList(gallagherBynderResponse.getProperty_skus(), catlogmodel.getPk());

		for (final ProductModel product : products)
		{
			final List<MediaModel> mediaModelList = new ArrayList<MediaModel>(product.getData_sheet());
			mediaModelList.add(mediaModel);
			product.setData_sheet(mediaModelList);
			modelService.save(product);
			LOGGER.info("Media " + gallagherBynderResponse.getId() + " Saved for " + product.getCode());
		}

		return true;
	}

	@Override
	public boolean deleteDocumentMedia(final GallagherBynderSyncCronJobModel cronModel,
			final GallagherBynderResponse gallagherBynderResponse)
	{
		final CatalogVersionModel catalog = catalogVersionService.getCatalogVersion(cronModel.getCatalogId(), "Staged");
		final MediaModel media = mediaService.getMedia(catalog, gallagherBynderResponse.getId());
		modelService.remove(media);
		LOGGER.info("Deleted Media " + gallagherBynderResponse.getId());
		return true;
	}

	private InputStream getImage(final String id)
	{

		final GallagherBynderMediaResponse gallagherBynderMediaResponse = getFileUrl(id);
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final InputStream stream = null;
		byte[] response = null;

		try
		{
			final InputStream in = new BufferedInputStream(new URL(gallagherBynderMediaResponse.getS3_file()).openStream());
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf)))
			{
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			response = out.toByteArray();

		}
		catch (final IOException e)
		{
			e.printStackTrace();
			return null;
		}

		return new ByteArrayInputStream(response);

	}

	/**
	 * @param id
	 * @return
	 */
	private GallagherBynderMediaResponse getFileUrl(final String id)
	{
		final RestTemplate restTemplate2 = new RestTemplate();
		final String url = MessageFormat.format(configurationService.getConfiguration().getString("bynder.media.download.url"), id);
		final HashMap<String, String> params = new HashMap<String, String>();

		final HttpHeaders httpheaders = new HttpHeaders();
		final BynderOauthHeaderGenerator generator = getHeaderGenerator();
		final String header = generator.generateHeader("GET", url, params);
		httpheaders.set("Authorization", header);
		final HttpEntity<String> entity = new HttpEntity<String>(httpheaders);
		final ResponseEntity<String> result = restTemplate2.exchange(url, HttpMethod.GET, entity, String.class);
		final GallagherBynderMediaResponse gallagherBynderMediaResponse = new GsonBuilder().serializeNulls().create()
				.fromJson(result.getBody(), GallagherBynderMediaResponse.class);
		return gallagherBynderMediaResponse;
	}

	/**
	 * @return
	 */
	private BynderOauthHeaderGenerator getHeaderGenerator()
	{
		final String consumerKey = configurationService.getConfiguration().getString("bynder.access.consumerKey");
		final String consumerSecret = configurationService.getConfiguration().getString("bynder.access.consumerSecret");
		final String token = configurationService.getConfiguration().getString("bynder.access.token");
		final String tokenSecret = configurationService.getConfiguration().getString("bynder.access.tokenSecret");

		final BynderOauthHeaderGenerator generator = new BynderOauthHeaderGenerator(consumerKey, consumerSecret, token,
				tokenSecret);
		return generator;
	}
}
