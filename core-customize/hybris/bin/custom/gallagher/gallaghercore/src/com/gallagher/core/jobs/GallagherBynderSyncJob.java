/**
 *
 */
package com.gallagher.core.jobs;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.gallagher.core.constants.GallagherCoreConstants;
import com.gallagher.core.cron.bynder.BynderOauthHeaderGenerator;
import com.gallagher.core.cronjob.model.GallagherBynderSyncCronJobModel;
import com.gallagher.core.services.GallagherBynderService;
import com.gallagher.outboundservices.response.dto.GallagherBynderResponse;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;


/**
 * This class is used to fetch images, documents and videos from BYNDER and store it in SAP Commerce
 *
 * @author Anuj Manocha
 */
public class GallagherBynderSyncJob extends AbstractJobPerformable<GallagherBynderSyncCronJobModel>
{

	private static final Logger LOGGER = Logger.getLogger(GallagherBynderSyncJob.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	private GallagherBynderService gallagherBynderService;

	@Override
	public PerformResult perform(final GallagherBynderSyncCronJobModel model)
	{

		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		try
		{
			final Date date = new Date();
			final String baseurl = configurationService.getConfiguration().getString(GallagherCoreConstants.Bynder.BASE_URL);
			for (final String queryparams : configurationService.getConfiguration()
					.getString(GallagherCoreConstants.Bynder.BYNDER_QUERY + model.getCatalogId()).split(";"))
			{


				final StringBuilder queryparamsBuilder = new StringBuilder(queryparams);
				if (model.getLastStartTime() != null)
				{
					queryparamsBuilder.append("&").append(GallagherCoreConstants.Bynder.DATE_MODIFIED).append("=")
							.append(new SimpleDateFormat(GallagherCoreConstants.Bynder.DATE_FORMAT).format(model.getLastStartTime()));
				}

				final String url = baseurl + "?" + queryparamsBuilder.toString();
				final Map<String, String> params = getQueryParams(queryparamsBuilder.toString());
				final RestTemplate restTemplate = new RestTemplate();
				final HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", getHeader(params));
				final HttpEntity<String> entity = new HttpEntity<String>(headers);
				LOGGER.info("URL " + url);
				final ResponseEntity<String> responseStr = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
				LOGGER.info("Response " + responseStr.getBody());
				final GallagherBynderResponse[] response = new GsonBuilder().serializeNulls().create().fromJson(responseStr.getBody(),
						GallagherBynderResponse[].class);
				handleAssets(model, response);
			}
			model.setLastStartTime(date);
			modelService.save(model);
		}
		catch (final JsonSyntaxException | RestClientException e)
		{
			e.printStackTrace();
			result = new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
		return result;
	}

	/**
	 * @param model
	 * @param response
	 */
	private void handleAssets(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse[] response)
	{
		final CatalogVersionModel catalog = catalogVersionService.getCatalogVersion(model.getCatalogId(), "Staged");
		for (final GallagherBynderResponse gallagherBynderResponse : response)
		{
			final List<String> skus = getSKUs(gallagherBynderResponse, model);
			if (gallagherBynderResponse.getArchive() == 0)
			{
				if (gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.IMAGE)
						|| gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.IMAGE_VECTOR))
				{
					gallagherBynderService.updateMedia(model, gallagherBynderResponse, skus, catalog);
				}
				if (gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.DOCUMENTS))
				{
					gallagherBynderService.updateDocumentMedia(model, gallagherBynderResponse, skus, catalog);
				}
				if (gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.VIDEOS))
				{
					gallagherBynderService.updateVideoMedia(model, gallagherBynderResponse, skus, catalog);
				}
			}
			else
			{
				if (gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.IMAGE)
						|| gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.IMAGE_VECTOR))
				{
					gallagherBynderService.deleteMedia(model, gallagherBynderResponse, catalog);
				}
				if (gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.DOCUMENTS))
				{
					gallagherBynderService.deleteDocumentMedia(model, gallagherBynderResponse, catalog);
				}
				if (gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.VIDEOS))
				{
					gallagherBynderService.deleteVideoMedia(model, gallagherBynderResponse, skus, catalog);
				}
			}
		}
	}

	/**
	 * Returns the SKUs
	 *
	 * @param gallagherBynderResponse
	 *           to get the SKU from correct field
	 * @param cronJob
	 *           to get the catalog
	 * @return sku list
	 */
	private List<String> getSKUs(final GallagherBynderResponse gallagherBynderResponse,
			final GallagherBynderSyncCronJobModel cronJob)
	{
		List<String> skus;
		if ("amB2CMasterProductCatalog".equals(cronJob.getCatalogId()))
		{
			skus = gallagherBynderResponse.getProperty_skus();
		}
		else
		{
			skus = gallagherBynderResponse.getProperty_part_numbers();
		}
		return skus;
	}

	private String getHeader(final Map<String, String> params)
	{
		final String consumerKey = configurationService.getConfiguration().getString(GallagherCoreConstants.Bynder.CONSUMER_KEY);
		final String consumerSecret = configurationService.getConfiguration()
				.getString(GallagherCoreConstants.Bynder.CONSUMER_SECRET);
		final String token = configurationService.getConfiguration().getString(GallagherCoreConstants.Bynder.ACCESS_TOKEN);
		final String tokenSecret = configurationService.getConfiguration().getString(GallagherCoreConstants.Bynder.TOKEN_SECRET);
		final BynderOauthHeaderGenerator generator = new BynderOauthHeaderGenerator(consumerKey, consumerSecret, token,
				tokenSecret);
		return generator.generateHeader(HttpMethod.GET.name(),
				configurationService.getConfiguration().getString(GallagherCoreConstants.Bynder.BASE_URL), params);
	}

	private Map<String, String> getQueryParams(final String paramsString)
	{
		final Map<String, String> params = new HashMap<>();
		final String[] arr = paramsString.split("&");
		for (int i = 0; i < arr.length; i++)
		{
			final String[] keyVal = arr[i].split("=");
			params.put(keyVal[0], keyVal[1]);

		}
		return params;
	}
}
