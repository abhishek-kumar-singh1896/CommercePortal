/**
 *
 */
package com.gallagher.core.cron.bynder;

import java.net.MalformedURLException;
import java.net.URL;

import com.bynder.sdk.configuration.Configuration;
import com.bynder.sdk.service.BynderClient;


/**
 *
 */
public class GallagherBynderClientImpl
{

	public static void main(final String[] args) throws MalformedURLException
	{

		final BynderClient client = BynderClient.Builder
				.create(new Configuration.Builder(new URL("https://media.gallagher.com")).setPermanentToken("token").build());



	}
}
