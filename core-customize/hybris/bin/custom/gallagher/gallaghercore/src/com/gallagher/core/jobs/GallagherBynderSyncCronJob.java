/**
 *
 */
package com.gallagher.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.gallagher.core.constants.GallagherCoreConstants;
import com.gallagher.core.cron.bynder.BynderOauthHeaderGenerator;
import com.gallagher.core.cronjob.model.GallagherBynderSyncCronJobModel;
import com.gallagher.core.services.impl.GallagherBynderServiceImpl;
import com.gallagher.outboundservices.response.dto.GallagherBynderResponse;
import com.google.gson.GsonBuilder;


/**
 *
 */
public class GallagherBynderSyncCronJob extends AbstractJobPerformable<GallagherBynderSyncCronJobModel>
{

	//	@Resource(name = "configurationService")
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private GallagherBynderServiceImpl gallagherBynderService;

	@Override
	public PerformResult perform(final GallagherBynderSyncCronJobModel model)
	{


		try
		{
			final String baseurl = configurationService.getConfiguration().getString("bynder.base.url");
			for (final String queryparams : configurationService.getConfiguration().getString("bynder.query." + model.getCatalogId())
					.split(";"))
			{

				final HashMap<String, String> params = new HashMap<String, String>();

				if (model.getLastStartTime() != null)
				{
					//	params.put(GallagherCoreConstants.Bynder.DATE_MODIFIED,
					//			new SimpleDateFormat(GallagherCoreConstants.Bynder.DATE_FORMAT).format(model.getLastStartTime()));
				}

				final String url = getUrlforQuery(baseurl, queryparams, params);
				final RestTemplate restTemplate = new RestTemplate();
				final HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", getHeader(params));
				final HttpEntity<String> entity = new HttpEntity<String>(headers);
				final ResponseEntity<String> result2 = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

				GallagherBynderResponse[] response = null;
				try
				{
					response = new GsonBuilder().serializeNulls().create().fromJson(result2.getBody(),
							GallagherBynderResponse[].class);
					for (final GallagherBynderResponse gallagherBynderResponse : response)
					{

						if (gallagherBynderResponse.getArchive() == 0)
						{
							if (gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.IMAGE)
									|| gallagherBynderResponse.getProperty_assettype().get(0)
											.equals(GallagherCoreConstants.Bynder.IMAGE_VECTOR))
							{
								gallagherBynderService.updateMedia(model, gallagherBynderResponse);
							}
							if (gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.DOCUMENTS)
									|| gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.VIDEOS))
							{
								gallagherBynderService.updateDocumentMedia(model, gallagherBynderResponse);
							}
						}
						else
						{
							if (gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.IMAGE)
									|| gallagherBynderResponse.getProperty_assettype().get(0)
											.equals(GallagherCoreConstants.Bynder.IMAGE_VECTOR))
							{
								gallagherBynderService.deleteMedia(model, gallagherBynderResponse);
							}
							if (gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.DOCUMENTS)
									|| gallagherBynderResponse.getProperty_assettype().get(0).equals(GallagherCoreConstants.Bynder.VIDEOS))
							{
								gallagherBynderService.deleteDocumentMedia(model, gallagherBynderResponse);
							}
						}
					}
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}

			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		model.setLastStartTime(new Date());
		//model.setLastStartTime(null);
		modelService.save(model);
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	private String getHeader(final HashMap<String, String> params)
	{
		final String consumerKey = configurationService.getConfiguration().getString("bynder.access.consumerKey");
		final String consumerSecret = configurationService.getConfiguration().getString("bynder.access.consumerSecret");
		final String token = configurationService.getConfiguration().getString("bynder.access.token");
		final String tokenSecret = configurationService.getConfiguration().getString("bynder.access.tokenSecret");
		final BynderOauthHeaderGenerator generator = new BynderOauthHeaderGenerator(consumerKey, consumerSecret, token,
				tokenSecret);
		return generator.generateHeader("GET", configurationService.getConfiguration().getString("bynder.base.url"), params);
	}

	private String getUrlforQuery(final String baseurl, final String paramsString, final HashMap<String, String> params)
	{
		final String[] arr = paramsString.split(",");
		final StringBuilder url = new StringBuilder(baseurl + "?");
		for (int i = 0; i < arr.length; i++)
		{
			params.put(arr[i].split("=")[0], arr[i].split("=")[1]);
			url.append("&").append(arr[i]);

		}
		if (params.get(GallagherCoreConstants.Bynder.DATE_MODIFIED) != null)
		{
			//	url.append("&").append(GallagherCoreConstants.Bynder.DATE_MODIFIED).append("=")
			//			.append(params.get(GallagherCoreConstants.Bynder.DATE_MODIFIED));
		}
		return url.toString();

	}
}
