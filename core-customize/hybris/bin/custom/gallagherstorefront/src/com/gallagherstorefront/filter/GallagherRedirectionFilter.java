/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagherstorefront.filter;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.io.IOException;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import com.gallagher.core.enums.RegionCode;



/**
 *
 */
public class GallagherRedirectionFilter extends GenericFilterBean
{

	private static final Logger LOG = Logger.getLogger(GallagherRedirectionFilter.class.getName());
	private static final String FARWORD_SLASH = "/";


	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException
	{

		LOG.info("Pre Request actions...");
		try
		{
			final HttpServletRequest req = (HttpServletRequest) request;
			final HttpServletResponse res = (HttpServletResponse) response;

			System.out.println(req.getRemoteAddr());
			LOG.info("req.getRemoteAddr()" + req.getRemoteAddr());
			System.out.println(req.getHeader("X-Forwarded-For"));
			System.out.println(req.getHeader("X-Forwarded-Proto"));

			final String userAddress = request.getRemoteAddr();
			LOG.debug("Remote Address = {}" + userAddress);

			final Enumeration<String> headerNames = req.getHeaderNames();

			if (headerNames != null)
			{
				while (headerNames.hasMoreElements())
				{
					System.out.println("Header: " + req.getHeader(headerNames.nextElement()));
				}
			}

		}
		catch (final Exception e)
		{
			e.getMessage();
		}

		/* filterChain.doFilter(request, response); */
		LOG.info("Post request actions...");


		final RestTemplate restTemplate2 = new RestTemplate();
		final String url = "https://ipinfo.io/" + "72.229.28.185" + "/country";
		final String result = restTemplate2.getForObject(url, String.class);
		System.out.println("result===" + result);

		try
		{

			LOG.info("Redirecting code.......................");
			final CountryModel country = commonI18NService.getCountry(result.trim());
			final RegionCode regionCode = country.getRegionCode();
			final HttpServletRequest req = (HttpServletRequest) request;
			final String queryString = req.getQueryString();

			final String requestURI = req.getRequestURI();
			final String redirectPath = FARWORD_SLASH
					+ StringUtils.substringBefore(StringUtils.substringAfter(requestURI, FARWORD_SLASH), FARWORD_SLASH) + FARWORD_SLASH
					+ regionCode.getCode() + FARWORD_SLASH
					+ StringUtils.substringAfter(
							StringUtils.substringAfter(StringUtils.substringAfter(requestURI, FARWORD_SLASH), FARWORD_SLASH),
							FARWORD_SLASH)
					+ (StringUtils.isEmpty(queryString) ? "" : "?" + queryString);
			LOG.info("Redirecting to " + redirectPath);
		}
		catch (final Exception e)
		{
			LOG.info("Redirecting to eception" + e);
			e.printStackTrace();
			e.getMessage();
		}
		try
		{
			final HttpServletRequest req = (HttpServletRequest) request;
			final HttpServletResponse res = (HttpServletResponse) response;

			LOG.info(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/am/" + result.trim().toLowerCase()
					+ "/en");

			res.sendRedirect(req.getScheme() + "://" + "amB2CUS.local" + ":" + req.getServerPort() + "/am/"
					+ result.trim().toLowerCase() + "/en");
		}
		catch (final Exception e)
		{
			e.getMessage();
		}
	}
}
