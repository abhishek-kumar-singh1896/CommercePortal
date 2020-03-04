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
import de.hybris.platform.servicelayer.media.MediaIOException;
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
	public Boolean updateMedia(final GallagherBynderSyncCronJobModel cronModel,
			final GallagherBynderResponse gallagherBynderResponse)
	{

		final CatalogVersionModel catlogmodel = catalogVersionService.getCatalogVersion(cronModel.getCatalogId(), "Staged");

		final List<MediaContainerModel> mediaContainerModellist = gallagherMediaContainerDao
				.getMediaContainer(gallagherBynderResponse.getId(), catlogmodel.getPk());

		//container exits
		if (mediaContainerModellist.size() > 0)
		{
			deleteMedia(cronModel, gallagherBynderResponse);
		}
		//container deleted creating new container and adding images

		final String _qualifier = gallagherBynderResponse.getId();

		//setting MediaContainerModel values
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final MediaContainerModel mediaContainerModel = modelService.create(MediaContainerModel.class);
		mediaContainerModel.setCatalogVersion(catlogmodel);
		mediaContainerModel.setQualifier(_qualifier);
		mediaContainerModel.setName(gallagherBynderResponse.getName(), Locale.ENGLISH);


		final BynderMediaModel mediaModel = modelService.create(BynderMediaModel.class);
		final MediaFolderModel folder = mediaService.getFolder(GallagherCoreConstants.Bynder.IMAGES);
		final MediaFormatModel format = mediaService.getFormat(GallagherCoreConstants.Bynder.B2BADVANCE_BF);

		mediaModel.setCode(gallagherBynderResponse.getId());
		mediaModel.setCatalogVersion(catlogmodel);
		mediaModel.setMediaFormat(format);
		mediaModel.setMediaContainer(mediaContainerModel);
		mediaModel.setFolder(folder);


		try
		{
			modelService.save(mediaModel);
			mediaService.setStreamForMedia(mediaModel, getImage(gallagherBynderResponse.getId()));

			//getting products and adding container to that product
			final List<ProductModel> productModelList = gallagherMediaContainerDao
					.getProductModeList(gallagherBynderResponse.getProperty_skus(), catlogmodel.getPk());

			for (final ProductModel productModel : productModelList)
			{
				final List<MediaContainerModel> listofexitingmodels = new ArrayList<MediaContainerModel>();
				for (final MediaContainerModel container : productModel.getGalleryImages())
				{
					listofexitingmodels.add(container);
				}
				listofexitingmodels.add(mediaContainerModel);
				productModel.setGalleryImages(listofexitingmodels);
				modelService.save(productModel);
			}

		}
		catch (MediaIOException | IllegalArgumentException e)
		{
			e.printStackTrace();
		}



		return true;
	}

	@Override
	public Boolean deleteMedia(final GallagherBynderSyncCronJobModel cronModel,
			final GallagherBynderResponse gallagherBynderResponse)
	{
		final CatalogVersionModel catlogmodel = catalogVersionService.getCatalogVersion(cronModel.getCatalogId(), "Staged");

		final List<MediaContainerModel> myCustomTypesWithId = gallagherMediaContainerDao
				.getMediaContainer(gallagherBynderResponse.getId(), catlogmodel.getPk());

		if (myCustomTypesWithId.size() > 0)
		{
			final MediaContainerModel mediacontainer = myCustomTypesWithId.get(0);
			final Collection<MediaModel> model = mediacontainer.getMedias();

			for (final MediaModel mediaModel : model)
			{

				if (mediaModel.getMediaFormat().getQualifier().equals(GallagherCoreConstants.Bynder.B2BADVANCE_BF))
				{
					try
					{
						//mediaService.removeDataFromMedia(mediaModel);
						modelService.remove(mediaModel);
						modelService.remove(mediacontainer);
					}
					catch (MediaIOException | IllegalArgumentException e)
					{
						e.printStackTrace();
					}
				}
			}

		}
		return true;

	}


	@Override
	public Boolean updateDocumentMedia(final GallagherBynderSyncCronJobModel cronModel,
			final GallagherBynderResponse gallagherBynderResponse)
	{
		final CatalogVersionModel catlogmodel = catalogVersionService.getCatalogVersion(cronModel.getCatalogId(), "Staged");

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
		final String _qualifier = gallagherBynderResponse.getId();
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final BynderMediaModel mediaModel = modelService.create(BynderMediaModel.class);
		final MediaFolderModel folder = mediaService.getFolder(GallagherCoreConstants.Bynder.DOCUMENTS);
		mediaModel.setCode(gallagherBynderResponse.getId());
		mediaModel.setCatalogVersion(catlogmodel);
		mediaModel.setFolder(folder);

		try
		{
			modelService.save(mediaModel);
			mediaService.setStreamForMedia(mediaModel, getImage(gallagherBynderResponse.getId()));

			//getting products and adding container to that product
			final List<ProductModel> productModelList = gallagherMediaContainerDao
					.getProductModeList(gallagherBynderResponse.getProperty_skus(), catlogmodel.getPk());

			for (final ProductModel productModel : productModelList)
			{
				final List<MediaModel> mediaModelList = new ArrayList<MediaModel>(productModel.getData_sheet());
				mediaModelList.add(mediaModel);
				productModel.setData_sheet(mediaModelList);
				modelService.save(productModel);
			}

		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}


		return true;
	}

	@Override
	public Boolean deleteDocumentMedia(final GallagherBynderSyncCronJobModel cronModel,
			final GallagherBynderResponse gallagherBynderResponse)
	{
		final CatalogVersionModel catlogmodel = catalogVersionService.getCatalogVersion(cronModel.getCatalogId(), "Staged");

		try
		{
			final MediaModel mediaModel1 = mediaService.getMedia(catlogmodel, gallagherBynderResponse.getId());
			modelService.remove(mediaModel1);
		}
		catch (final UnknownIdentifierException e)
		{
			e.getMessage();
		}
		return true;
	}

	@Override
	public Boolean updateInsertIconMedia(final GallagherBynderSyncCronJobModel cronModel,
			final GallagherBynderResponse gallagherBynderResponse)
	{
		final CatalogVersionModel catlogmodel = catalogVersionService.getCatalogVersion(cronModel.getCatalogId(), "Staged");

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
		final String _qualifier = gallagherBynderResponse.getId();
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final BynderMediaModel mediaModel = modelService.create(BynderMediaModel.class);
		final MediaFolderModel folder = mediaService.getFolder(GallagherCoreConstants.Bynder.IMAGES);
		mediaModel.setCode(gallagherBynderResponse.getId());
		mediaModel.setCatalogVersion(catlogmodel);
		mediaModel.setFolder(folder);

		try
		{
			modelService.save(mediaModel);
			mediaService.setStreamForMedia(mediaModel, getImage(gallagherBynderResponse.getId()));

			//getting products and adding container to that product
			final List<ProductModel> productModelList = gallagherMediaContainerDao
					.getProductModeList(gallagherBynderResponse.getProperty_skus(), catlogmodel.getPk());

			for (final ProductModel productModel : productModelList)
			{
				final List<MediaModel> mediaModelList = new ArrayList<MediaModel>(productModel.getLogo());
				mediaModelList.add(mediaModel);
				productModel.setLogo(mediaModelList);
				modelService.save(productModel);
			}

		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}


		return true;
	}

	private InputStream getImage(final String id)
	{

		final RestTemplate restTemplate2 = new RestTemplate();
		final String url = MessageFormat.format(configurationService.getConfiguration().getString("bynder.media.download.url"), id);


		final HashMap<String, String> params = new HashMap<String, String>();
		final HttpHeaders headers2 = new HttpHeaders();
		final String consumerKey = configurationService.getConfiguration().getString("bynder.access.consumerKey");
		final String consumerSecret = configurationService.getConfiguration().getString("bynder.access.consumerSecret");
		final String token = configurationService.getConfiguration().getString("bynder.access.token");
		final String tokenSecret = configurationService.getConfiguration().getString("bynder.access.tokenSecret");

		final BynderOauthHeaderGenerator generator = new BynderOauthHeaderGenerator(consumerKey, consumerSecret, token,
				tokenSecret);
		final String header = generator.generateHeader("GET", url, params);
		headers2.set("Authorization", header);
		final HttpEntity<String> entity = new HttpEntity<String>(headers2);
		final ResponseEntity<String> result = restTemplate2.exchange(url, HttpMethod.GET, entity, String.class);
		final GallagherBynderMediaResponse gallagherBynderMediaResponse = new GsonBuilder().serializeNulls().create()
				.fromJson(result.getBody(), GallagherBynderMediaResponse.class);
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
}
