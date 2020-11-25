/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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
	private static final String WHITE_SPACE = "\\s";

	private static final String HIMAC_SHA_256 = "HmacSHA256";
	private static final String SEPARATOR = "_";
	private static final String TOKEN_TYPE = "X-Deki-Token";

	private Map<String, String> mindTouchRoleMapping;

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public void pushCustomerToMindTouch(final B2BCustomerModel customer) throws IOException, DocumentException
	{
		LOG.debug("Processing the user for Mindtouch");
		final String token = getAuthentication();
		createUserInMindTouch(token, customer);

	}


	/**
	 * This method authenticates the request
	 *
	 * @return
	 */
	private String getAuthentication() throws IOException
	{
		LOG.info("Authenticating the request to Mindtouch");
		final String key = getConfigurationService().getConfiguration().getString(MINDTOUCH_KEY);
		final String authUrl = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL);
		final String userName = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL_USERNAME);
		final String secret = getConfigurationService().getConfiguration().getString(MIND_TOUCH_SECRET);

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
	 * This method creates the user at mindtouch end
	 *
	 * @param token
	 * @param user
	 * @throws DocumentException
	 */
	private void createUserInMindTouch(final String token, final B2BCustomerModel customer) throws IOException, DocumentException
	{
		final String userName = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL_USERNAME);
		final String password = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL_PASSWORD);
		final String endPoint = getConfigurationService().getConfiguration().getString(END_POINT_URL);

		final String endPointUrl = endPoint + "/users";

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
		String responseString = StringUtils.EMPTY;

		if (responseCode == HttpURLConnection.HTTP_OK)
		{
			LOG.info("User created sucessfully in mindtouch ");
			try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
			{
				String input = StringUtils.EMPTY;
				final StringBuilder response = new StringBuilder();

				while ((input = in.readLine()) != null)
				{
					response.append(input);
				}
				LOG.debug(response.toString());
				responseString = response.toString();
			}

			final String uid = getUserID(responseString);

			final List<PrincipalGroupModel> userGroups = customer.getGroups().stream().filter(group -> isUserGroup(group))
					.collect(Collectors.toList());

			if (CollectionUtils.isNotEmpty(userGroups))
			{
				for (final PrincipalGroupModel principalGroup : userGroups)
				{
					assignGroupForUser(uid, token, principalGroup);
				}
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
		final String email = b2bCustomer.getEmailID();
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
		sb.append("<service.authentication id=\"" + 2 + "\" />");
		sb.append(ENTER);
		sb.append("</user >");
		return sb.toString();
	}

	/**
	 * This method get the userid from mindtouch response xml
	 *
	 * @param return
	 *
	 * @return
	 */
	private String getUserID(final String result) throws DocumentException
	{
		String userID = StringUtils.EMPTY;
		final Document document = DocumentHelper.parseText(result);
		final Element root = document.getRootElement();
		final List<Attribute> attributeList = root.attributes();
		for (final Attribute attr : attributeList)
		{
			if (attr.getName().equals("id"))
			{
				userID = attr.getValue();
				break;
			}
		}
		return userID;
	}


	/**
	 * This method assign group to the user.
	 *
	 * @param uid
	 * @param token
	 * @param customer
	 * @param userGroups
	 *
	 */
	private void assignGroupForUser(final String uid, final String token, final PrincipalGroupModel principalUserGroup)
			throws IOException
	{
		final String userName = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL_USERNAME);
		final String password = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL_PASSWORD);

		final String userGroup = principalUserGroup.getUid();

		final String group = StringUtils.isNotBlank(userGroup)
				? mindTouchRoleMapping.get(userGroup).replaceAll(WHITE_SPACE, "").toLowerCase()
				: StringUtils.EMPTY;

		final String groupId = getConfigurationService().getConfiguration().getString("mindtouch.group." + group);

		if (StringUtils.isBlank(groupId))
		{
			throw new IOException("Group Id is blank, cannot be assigned");
		}

		final String endPoint = getConfigurationService().getConfiguration().getString(END_POINT_URL);
		final String endPointUrl = endPoint + "/groups/" + groupId + "/users";

		final URL obj = new URL(endPointUrl);

		final String auth = userName + ":" + password;
		final byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
		final String authHeaderValue = "Basic " + new String(encodedAuth);


		final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("X-Deki-Token", token);
		con.setRequestProperty("Content-Type", "text/xml;utf-8");
		con.setRequestProperty("Authorization", authHeaderValue);

		LOG.debug("authheader " + authHeaderValue);
		// For POST only - START
		con.setDoOutput(true);

		try (final OutputStream os = con.getOutputStream())
		{
			final String userXml = "<users><user id=\"" + uid + "\"/></users>";
			LOG.debug(userXml);
			os.write(userXml.getBytes());
		}
		// For POST only - END

		final int responseCode = con.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK)
		{
			throw new IOException("Error while assigning user group in mindtouch :: " + responseCode);
		}
		LOG.debug("POST Response Code :: " + responseCode);
	}

	/**
	 * @param group
	 * @return
	 */
	private Boolean isUserGroup(final PrincipalGroupModel group)
	{
		return group instanceof UserGroupModel && !(group instanceof B2BUnitModel);
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


	/**
	 * @return the mindTouchRoleMapping
	 */
	public Map<String, String> getMindTouchRoleMapping()
	{
		return mindTouchRoleMapping;
	}


	/**
	 * @param mindTouchRoleMapping
	 *           the mindTouchRoleMapping to set
	 */
	public void setMindTouchRoleMapping(final Map<String, String> mindTouchRoleMapping)
	{
		this.mindTouchRoleMapping = mindTouchRoleMapping;
	}
}
