/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.gallagher.core.services.GallagherMindTouchService;


/**
 * @author ankituniyal
 *
 */
public class GallagherMindTouchServiceImpl implements GallagherMindTouchService
{
	private static final Logger LOG = Logger.getLogger(GallagherMindTouchServiceImpl.class);

	private static final String ENTER = "\r\n";
	private static final String AUTHENTICATION_URL = "gallagher.mindtouch.authenticate.url";
	private static final String AUTHENTICATION_URL_USERNAME = "gallagher.mindtouch.authenticate.username";
	private static final String AUTHENTICATION_URL_PASSWORD = "gallagher.mindtouch.authenticate.password";
	private static final String END_POINT_URL = "gallagher.mindtouch.endpoint.url";
	private static final String MINDTOUCH_KEY = "gallagher.mindtouch.key";
	private static final String MIND_TOUCH_SECRET = "gallagher.mindtouch.secret";


	private static final String SECURITY_B2B_GLOBAL = "securityB2BGlobal";
	private static final String SECURITY_B2B = "securityB2B";
	private static final String SECURITY = "security";
	private static final String HIMAC_SHA_256 = "HmacSHA256";
	private static final String SEPARATOR = "_";
	private static final String TOKEN_TYPE = "X-Deki-Token";

	private String authUrl;
	private String userName;
	private String password;
	private String endPointUrl;
	private String key;
	private String secret;


	@Autowired
	private ConfigurationService configurationService;

	@PostConstruct
	private void init()
	{
		authUrl = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL);
		userName = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL_USERNAME);
		password = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL_PASSWORD);
		endPointUrl = getConfigurationService().getConfiguration().getString(END_POINT_URL);
		key = getConfigurationService().getConfiguration().getString(MINDTOUCH_KEY);
		secret = getConfigurationService().getConfiguration().getString(MIND_TOUCH_SECRET);
	}

	@Override
	public void pushCustomerToMindTouch(final B2BCustomerModel customer) throws IOException, DocumentException
	{
		LOG.debug("Processing the user for Mindtouch");
		final String token = getAuthentication();
		createUserInMindTouch(token, customer);

	}


	/**
	 * @return
	 */
	private String getAuthentication() throws IOException
	{
		LOG.info("Authenticating the request to Mindtouch");

		//user string for authentication
		final String user = '=' + userName;

		final String epoch = Long.toString(new Date().getTime() / 1000L);
		String hash = StringUtils.EMPTY;

		//Get the hash value using sha256 and epoch
		try
		{
			final Mac sha256_HMAC = Mac.getInstance(HIMAC_SHA_256);
			final SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), HIMAC_SHA_256);
			sha256_HMAC.init(secret_key);
			final String message = key + SEPARATOR + epoch + SEPARATOR + user;
			hash = Hex.encodeHexString(sha256_HMAC.doFinal(message.getBytes()));
		}
		catch (NoSuchAlgorithmException | InvalidKeyException e)
		{

			LOG.debug("Error while authentication :: " + e.getMessage());
		}

		//Preparing the token for authentication
		final String token = String.join(SEPARATOR, "tkn", key, epoch, user, hash);
		LOG.debug(token);

		//Get the auth URL
		final URL url = new URL(authUrl);

		//open the connection for url and get the response code
		final HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty(TOKEN_TYPE, token);
		final int code = con.getResponseCode();

		LOG.info("Authentication status :: " + code);
		if (code != 200)
		{
			throw new IOException("Exception while authenticting " + code);
		}
		return token;
	}


	/**
	 * @param token
	 * @param user
	 * @throws DocumentException
	 */
	private void createUserInMindTouch(final String token, final B2BCustomerModel customer) throws IOException, DocumentException
	{

		LOG.info("Begin Creating User in Mindtouch!");

		//Get the end point url
		final URL url = new URL(endPointUrl);

		final String auth = userName + ":" + password;
		final byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
		//Auth header value
		final String authHeaderValue = "Basic " + new String(encodedAuth);

		//Creating the connection for url
		final HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");

		//Setting request proeprty and header values
		con.setRequestProperty(TOKEN_TYPE, token);
		con.setRequestProperty("Content-Type", MediaType.TEXT_XML_VALUE);
		con.setRequestProperty("Authorization", authHeaderValue);
		con.setDoOutput(true);

		try (final OutputStream outputStream = con.getOutputStream())
		{
			final String userXml = getUserXml(customer);
			LOG.debug(userXml);
			outputStream.write(userXml.getBytes());
		}

		//Get the response code.
		final int responseCode = con.getResponseCode();

		//if status is 200 OK log the response
		if (responseCode == HttpURLConnection.HTTP_OK)
		{
			LOG.info("User created sucessfully in mindtouch ");
			try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
			{
				String input = StringUtils.EMPTY;
				final StringBuffer response = new StringBuffer();

				while ((input = in.readLine()) != null)
				{
					response.append(input);
				}
				LOG.debug(response.toString());
			}
		}
		else
		{
			throw new IOException("Error while creating user in mindtouch :: " + responseCode);
		}

	}


	/**
	 * This method is to prepare the xml for posting at the endpoint
	 *
	 * @param username
	 * @param email
	 * @param fullname
	 * @param group
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getUserXml(final B2BCustomerModel b2bCustomer)
	{
		final String userName = b2bCustomer.getKeycloakGUID();
		final String email = b2bCustomer.getUid();
		final String fullName = b2bCustomer.getName();

		final StringBuilder sb = new StringBuilder("<user>");
		sb.append(ENTER);
		sb.append("<username>");
		sb.append(userName);
		sb.append("</username>");
		sb.append(ENTER);
		sb.append("<email>");
		sb.append(email);
		sb.append("</email>");
		sb.append(ENTER);
		sb.append("<fullname>");
		sb.append(fullName);
		sb.append("</fullname>");
		sb.append(ENTER);
		sb.append("<permissions.user>");
		sb.append(ENTER);
		sb.append("<role>");
		sb.append("Viewer");
		sb.append("</role>");
		sb.append(ENTER);
		sb.append("</permissions.user>");
		sb.append(ENTER);
		sb.append("<service.authentication id=\"" + 2 + "\" />");
		sb.append(ENTER);
		sb.append("</user >");
		return sb.toString();
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
