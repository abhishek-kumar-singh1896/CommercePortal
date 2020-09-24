/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.gallagher.core.services.GallagherMindTouchService;
import com.google.common.base.Splitter;


/**
 * @author ankituniyal
 *
 */
public class GallagherMindTouchServiceImpl implements GallagherMindTouchService
{
	private static final Logger LOG = Logger.getLogger(GallagherMindTouchServiceImpl.class);

	private static final String ENTER = "\r\n";
	private static final String ID = "id";
	private static final String B2B = "B2B";
	private static final String AUTHENTICATION_URL = "gallagher.mindtouch.authenticate.url";
	private static final String AUTHENTICATION_URL_USERNAME = "gallagher.mindtouch.authenticate.username";
	private static final String AUTHENTICATION_URL_PASSWORD = "gallagher.mindtouch.authenticate.password";
	private static final String END_POINT_URL = "gallagher.mindtouch.endpoint.url";
	private static final String ROLE_MAPPING = "gallagher.mindtouch.role.mapping";

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public void pushCustomerToMindTouch(final CustomerModel customer) throws IOException, DocumentException
	{
		LOG.debug("Processing the user ");
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		getAuthentication(httpClient);
		createUserInMindTouch(httpClient, customer);

	}


	/**
	 * @param httpClient
	 */
	private void getAuthentication(final CloseableHttpClient httpClient) throws IOException
	{

		LOG.debug("Begin Aughentication!");
		final String url = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL);
		final String userName = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL_USERNAME);
		final String password = getConfigurationService().getConfiguration().getString(AUTHENTICATION_URL_PASSWORD);
		final HttpGet get = new HttpGet(url);

		final HttpClientContext context = new HttpClientContext();
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
		context.setCredentialsProvider(credentialsProvider);

		final CloseableHttpResponse execute = httpClient.execute(get, context);
		final HttpEntity entity = execute.getEntity();
		final InputStream in = entity.getContent();
		final StringBuilder builder = new StringBuilder();
		final BufferedReader bufreader = new BufferedReader(new InputStreamReader(in));

		for (String temp = bufreader.readLine(); temp != null; temp = bufreader.readLine())
		{
			builder.append(temp);
		}
		LOG.debug(builder.toString());
	}


	/**
	 * @param httpClient
	 * @param user
	 */
	private void createUserInMindTouch(final CloseableHttpClient httpClient,
			final CustomerModel customer)
			throws IOException, DocumentException
	{

		LOG.debug("Begin Creating User!");
		final String url = getConfigurationService().getConfiguration().getString(END_POINT_URL);
		final HttpPost httpPost = new HttpPost(url);
		final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000)
				.setSocketTimeout(10000).setRedirectsEnabled(true).build();

		httpPost.setHeader("Content-Type", "application/xml;charset=utf8");

		httpPost.setConfig(requestConfig);

		/*
		 * final Collection<AddressModel> addressList = customer.getAddresses(); if (CollectionUtils.isEmpty(addressList))
		 * { LOG.debug("no regions"); return; }
		 */
		final String username = customer.getName(); // Need to check for username
		final String email = customer.getContactEmail();
		final String fullname = customer.getName();
		final UserGroupModel group = getUserGroupForUser(customer);

		final String userXml = getUserXml(username, email, fullname, group);
		final StringEntity entityParams = new StringEntity(userXml, "utf-8");
		httpPost.setEntity(entityParams);

		LOG.debug("Executing the httpPost");
		final HttpResponse httpResponse = httpClient.execute(httpPost);

		final int statusCode = httpResponse.getStatusLine().getStatusCode();
		LOG.debug("Status Code：" + statusCode);
		final HttpEntity resEntity = httpResponse.getEntity();
		final String result = EntityUtils.toString(resEntity);

		if (statusCode == 200)
		{
			final String userID = customer.getName();/* getUserID(result); */
			customer.setUid(userID);
			assignGroupForUser(httpClient, customer);
		}
		else
		{
			LOG.debug(username + " is error creation!");
			httpClient.close();
		}
	}

	protected UserGroupModel getUserGroupForUser(final CustomerModel userModel)
	{
		final Optional<UserGroupModel> userGroupOptional = userModel.getGroups().stream().filter(
				group -> group instanceof UserGroupModel
						&& group.getLocName().startsWith(B2B))
				.map(UserGroupModel.class::cast).findFirst();
		return userGroupOptional.isPresent() ? userGroupOptional.get() : null;
	}

	/**
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getUserID(final String result) throws DocumentException
	{
		String userID = StringUtils.EMPTY;
		final Document document = DocumentHelper.parseText(result);
		final Element root = document.getRootElement();
		final List<Attribute> attributeList = root.attributes();
		for (final Attribute attr : attributeList)
		{
			if (ID.equals(attr.getName()))
			{
				userID = attr.getValue();
				break;
			}
		}
		return userID;
	}

	/**
	 * @param httpClient
	 * @param customer
	 */
	private void assignGroupForUser(final CloseableHttpClient httpClient, final CustomerModel customer)
			throws IOException
	{
		final String userID = customer.getUid();
		LOG.debug("Assgining group for " + userID);

		final Collection<AddressModel> addressList = customer.getAddresses();
		final AddressModel address = CollectionUtils.isNotEmpty(addressList) ? addressList.iterator().next() : null;
		final RegionModel regionModel = address != null ? address.getRegion() : null;
		final String region = regionModel != null ? regionModel.getIsocode() : StringUtils.EMPTY;
		final StringBuilder url = new StringBuilder();

		//String url="https://supporthub.security.gallagher.com/@api/deki/groups/1/users";
		url.append("https://supporthub.security.gallagher.com/@api/deki/groups/");
		url.append(region);
		url.append("/users");


		final HttpPost httpPut = new HttpPost(url.toString());
		final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000)
				.setSocketTimeout(10000).setRedirectsEnabled(true).build();

		httpPut.setHeader("Content-Type", "application/xml;charset=utf8");
		httpPut.setConfig(requestConfig);

		final String xml = "<users><user id=\"" + userID + "\"/></users>";
		final StringEntity entityParams = new StringEntity(xml, "utf-8");

		httpPut.setEntity(entityParams);

		final HttpResponse httpResponse = httpClient.execute(httpPut);
		final int statusCode = httpResponse.getStatusLine().getStatusCode();
		LOG.debug("Status Code：" + statusCode);

		if (statusCode != 200)
		{
			LOG.debug(customer.getName() + " error assigning groups!");
		}
		else
		{
			LOG.debug(customer.getName() + " assgined group successfully!");
		}
		httpClient.close();
	}

	/**
	 * @param username
	 * @param email
	 * @param fullname
	 * @param group
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getUserXml(final String username, final String email, final String fullname,
			final UserGroupModel group)
	{
		String role = StringUtils.EMPTY;
		final String roleGroupMap = getConfigurationService().getConfiguration().getString(ROLE_MAPPING);
		if (StringUtils.isNotBlank(roleGroupMap) && group != null)
		{
			final Map<String, String> rolesMapping = getRolesMapping(roleGroupMap);
			role = rolesMapping.get(group.getUid());
		}

		final StringBuilder sb = new StringBuilder("<user>");
		sb.append(ENTER);
		sb.append("<username>");
		sb.append(username);
		sb.append("</username>");
		sb.append(ENTER);
		sb.append("<email>");
		sb.append(email);
		sb.append("</email>");
		sb.append(ENTER);
		sb.append("<fullname>");
		sb.append(fullname);
		sb.append("</fullname>");
		sb.append(ENTER);
		sb.append("<permissions.user>");
		sb.append(ENTER);
		sb.append("<role>");
		sb.append(role);
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
	 * @param roles
	 * @return
	 */
	private Map<String, String> getRolesMapping(final String property)
	{
		final Map<String, String> roleGroupMap = Splitter.on(",").omitEmptyStrings().trimResults().withKeyValueSeparator("=")
				.split(property);
		return roleGroupMap;
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
