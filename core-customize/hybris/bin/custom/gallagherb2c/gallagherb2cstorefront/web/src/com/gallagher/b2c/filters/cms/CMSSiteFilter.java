/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.filters.cms;

import de.hybris.platform.acceleratorcms.context.ContextInformationLoader;
import de.hybris.platform.acceleratorcms.data.CmsPageRequestContextData;
import de.hybris.platform.acceleratorcms.services.CMSPageContextService;
import de.hybris.platform.acceleratorfacades.urlencoder.UrlEncoderFacade;
import de.hybris.platform.acceleratorservices.site.strategies.SiteChannelValidationStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.misc.UrlUtils;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gallagher.core.enums.RegionCode;
import com.gallagher.core.util.GallagherSiteUtil;
import com.gallagher.facades.GallagherRegionDetectionFacade;


/**
 * Responsible for setting up application - to main responsibility belongs:
 * <p>
 * <ul>
 * <li>Matches current site by current URL</li>
 * <li>Setting current site in session (through {@link CMSSiteService} )</li>
 * <li>Setting current catalog version (through {@link CMSSiteService} )</li>
 * <li>Setting session catalog versions (through {@link CatalogService} )</li>
 * </ul>
 * </p>
 * <br/>
 * <b>Note</b>: In former versions (i.e. 4.1.1 and earlier) as a preview mechanism we used
 * {@link de.hybris.platform.cms2.misc.AbstractPreviewServlet} which actually is obsolete. All necessary logic was
 * adapted and moved here. This is a spring configured filter that is executed by the PlatformFilterChain.
 */
public class CMSSiteFilter extends OncePerRequestFilter implements CMSFilter
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CMSSiteFilter.class);
	private static final String FARWORD_SLASH = "/";

	protected static final int MISSING_CMS_SITE_ERROR_STATUS = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	protected static final String MISSING_CMS_SITE_ERROR_MESSAGE = "Cannot find CMSSite associated with current URL";
	protected static final String INCORRECT_CMS_SITE_CHANNEL_ERROR_MESSAGE = "Matched CMSSite for current URL has unsupported channel";

	private CMSSiteService cmsSiteService;
	private SessionService sessionService;
	private CMSPreviewService cmsPreviewService;
	private CommerceCommonI18NService commerceCommonI18NService;
	private BaseSiteService baseSiteService;
	private UrlResolver<PreviewDataModel> previewDataModelUrlResolver;
	private ContextInformationLoader contextInformationLoader;
	private CMSPageContextService cmsPageContextService;
	private SiteChannelValidationStrategy siteChannelValidationStrategy;

	@Resource(name = "urlEncoderFacade")
	private UrlEncoderFacade urlEncoderFacade;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "gallagherRegionDetectionFacade")
	private GallagherRegionDetectionFacade gallagherRegionDetectionFacade;

	@Override
	protected void doFilterInternal(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse,
			final FilterChain filterChain) throws ServletException, IOException
	{
		final String requestURL = httpRequest.getRequestURL().toString();

		final CmsPageRequestContextData cmsPageRequestContextData = getCmsPageContextService()
				.initialiseCmsPageContextForRequest(httpRequest);

		// check whether exits valid preview data
		if (cmsPageRequestContextData.getPreviewData() == null)
		{
			// process normal request (i.e. normal browser non-cmscockpit request)
			if (processNormalRequest(httpRequest, httpResponse))
			{
				// proceed filters
				filterChain.doFilter(httpRequest, httpResponse);
			}
		}
		else if (StringUtils.contains(requestURL, PREVIEW_TOKEN))
		{
			final String redirectURL = processPreviewRequest(httpRequest, cmsPageRequestContextData);

			// redirect to computed URL
			if (redirectURL.charAt(0) == '/')
			{
				final String contextPath = httpRequest.getContextPath();
				final String encodedRedirectUrl = httpResponse.encodeRedirectURL(contextPath + redirectURL);
				httpResponse.sendRedirect(encodedRedirectUrl); //NOSONAR
			}
			else
			{
				final String encodedRedirectUrl = httpResponse.encodeRedirectURL(redirectURL);
				httpResponse.sendRedirect(encodedRedirectUrl); //NOSONAR
			}

			// next filter in chain won't be invoked!!!
		}
		else
		{
			processPreviewData(httpRequest, cmsPageRequestContextData.getPreviewData());

			// proceed filters
			filterChain.doFilter(httpRequest, httpResponse);
		}
	}

	protected void processPreviewData(final HttpServletRequest httpRequest, final PreviewDataModel previewDataModel)
	{
		previewDataModel.setLanguage(filterPreviewLanguageForSite(httpRequest, previewDataModel));

		//load necessary information
		getContextInformationLoader().initializePreviewRequest(previewDataModel);
		//load fake context information
		getContextInformationLoader().loadFakeContextInformation(httpRequest, previewDataModel);
	}

	/**
	 * Processing normal request (i.e. when user goes directly to that application - not from cmscockpit)
	 * <p/>
	 * <b>Note:</b> <br/>
	 * We preparing application by setting correct:
	 * <ul>
	 * <li>Current Site</li>
	 * <li>Current Catalog Versions</li>
	 * <li>Enabled language fallback</li>
	 * </ul>
	 *
	 * @see ContextInformationLoader#initializeSiteFromRequest(String)
	 * @see ContextInformationLoader#setCatalogVersions()
	 * @param httpRequest
	 *           current request
	 * @param httpResponse
	 *           the http response
	 * @throws java.io.IOException
	 */
	protected boolean processNormalRequest(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
			throws IOException
	{
		final String queryString = httpRequest.getQueryString();
		final String currentRequestURL = httpRequest.getRequestURL().toString();
		final String requestURI = httpRequest.getRequestURI();

		//set current site
		CMSSiteModel cmsSiteModel = getCurrentCmsSite();

		final String absoluteURL = StringUtils.removeEnd(currentRequestURL, FARWORD_SLASH)
				+ (StringUtils.isBlank(queryString) ? "" : "?" + queryString);

		try
		{
			LOG.debug("Looking up site for url <" + absoluteURL + ">");
			final CMSSiteModel cmsSiteForCurrentRequest = cmsSiteService.getSiteForURL(new URL(absoluteURL));
			if (cmsSiteForCurrentRequest != null && cmsSiteModel != null
					&& !cmsSiteForCurrentRequest.getUid().equalsIgnoreCase(cmsSiteModel.getUid()))
			{
				GallagherSiteUtil.setSiteSwitched(httpRequest);
				// Remove cart from session, Clone first as a workaround since removing from session will delete cart.
				cloneCart();
				sessionService.removeAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME);
				// Set the New Site in the session.
				baseSiteService.setCurrentBaseSite(cmsSiteForCurrentRequest, true);
				// Update the URLEncoderData
				urlEncoderFacade.updateSiteFromUrlEncodingData();
			}
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.warn("CMSSite was not found. Message : " + e.getMessage());
			LOG.debug(e);

			final RegionCode regionCode = cmsSiteModel != null ? cmsSiteModel.getRegionCode()
					: gallagherRegionDetectionFacade.getRegionCode(httpRequest);

			final String redirectPath = FARWORD_SLASH
					+ StringUtils.substringBefore(StringUtils.substringAfter(requestURI, FARWORD_SLASH), FARWORD_SLASH) + FARWORD_SLASH
					+ regionCode.getCode() + FARWORD_SLASH
					+ StringUtils.substringAfter(
							StringUtils.substringAfter(StringUtils.substringAfter(requestURI, FARWORD_SLASH), FARWORD_SLASH),
							FARWORD_SLASH)
					+ (StringUtils.isEmpty(queryString) ? "" : "?" + queryString);
			LOG.info("Redirecting to " + redirectPath);
			httpResponse.sendRedirect(redirectPath);
			return false;
		}

		if (cmsSiteModel == null || StringUtils.contains(queryString, CLEAR_CMSSITE_PARAM))
		{
			cmsSiteModel = getContextInformationLoader().initializeSiteFromRequest(absoluteURL);

			if (GallagherSiteUtil.isSiteSwitched(httpRequest))
			{
				sessionService.removeAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME);
			}
		}

		if (cmsSiteModel == null)
		{
			// Failed to lookup CMS site
			httpResponse.sendError(MISSING_CMS_SITE_ERROR_STATUS, MISSING_CMS_SITE_ERROR_MESSAGE);
			return false;
		}
		else if (!getSiteChannelValidationStrategy().validateSiteChannel(cmsSiteModel.getChannel())) // Restrict to configured channel
		{
			// CMS site that we looked up was for an unsupported channel
			httpResponse.sendError(MISSING_CMS_SITE_ERROR_STATUS, INCORRECT_CMS_SITE_CHANNEL_ERROR_MESSAGE);
			return false;
		}

		if (!isActiveSite(cmsSiteModel))
		{
			throw new IllegalStateException("Site is not active. Active flag behaviour must be implement for this project.");
		}

		getContextInformationLoader().setCatalogVersions();
		//set fall back language enabled
		setFallbackLanguage(httpRequest, Boolean.TRUE);

		return true;
	}

	protected boolean isActiveSite(final CMSSiteModel site)
	{
		return site.getActive() != null && site.getActive().booleanValue();
	}

	/**
	 * Processing preview request (i.e. request with additional parameters like {@link CMSFilter#PREVIEW_TOKEN} requested
	 * from cmscockpit) )
	 * <p/>
	 * <b>Note:</b> Processing preview data in order to generate target URL, and load necessary information in user
	 * session
	 * <ul>
	 * <li>Initialize information (Active CMSSite, Catalog versions,Current catalog version ) information getting from
	 * valid Preview Data</li>
	 * <li>Load all fake information (like: User, User group, Language, Time ...)
	 * <li>Generating target URL according to Preview Data
	 * </ul>
	 *
	 * @param httpRequest
	 *           current request
	 * @return target URL
	 */
	protected String processPreviewRequest(final HttpServletRequest httpRequest,
			final CmsPageRequestContextData cmsPageRequestContextData)
	{
		final PreviewDataModel previewDataModel = cmsPageRequestContextData.getPreviewData();
		processPreviewData(httpRequest, previewDataModel);

		//generate destination URL
		final String destinationURL = generatePreviewUrl(httpRequest, previewDataModel);

		// persist changes
		previewDataModel.setResourcePath(destinationURL);
		getContextInformationLoader().storePreviewData(previewDataModel);

		final CMSPreviewTicketModel ticket = getCmsPreviewService().createPreviewTicket(previewDataModel);
		String parameterDelimiter = "?";
		if (StringUtils.contains(destinationURL, "?"))
		{
			parameterDelimiter = "&";
		}
		return destinationURL + parameterDelimiter + PREVIEW_TICKET_ID_PARAM + "=" + ticket.getId();
	}

	/**
	 * Filters the preview language to a language supported by the site. If the requested preview language is not
	 * supported, returns the default site language instead.
	 *
	 * @param httpRequest
	 *           current request
	 * @param previewDataModel
	 *           the preview data model
	 * @return LanguageModel the filtered language for previewing
	 */
	protected LanguageModel filterPreviewLanguageForSite(final HttpServletRequest httpRequest,
			final PreviewDataModel previewDataModel)
	{
		final BaseSiteModel previewSite = previewDataModel.getActiveSite() == null ? getCurrentCmsSite()
				: previewDataModel.getActiveSite();
		getBaseSiteService().setCurrentBaseSite(previewSite, false);
		final Collection<LanguageModel> siteLanguages = getCommerceCommonI18NService().getAllLanguages();
		if (siteLanguages.contains(previewDataModel.getLanguage()))
		{
			// The preview language is supported
			return previewDataModel.getLanguage();
		}
		return getCommerceCommonI18NService().getDefaultLanguage();
	}

	/**
	 * Enables or disables language fall back
	 * <p/>
	 *
	 * @param httpRequest
	 *           current request
	 * @param enabled
	 *           enabled or disabled
	 */
	protected void setFallbackLanguage(final HttpServletRequest httpRequest, final Boolean enabled)
	{
		if (getSessionService() != null)
		{
			getSessionService().setAttribute(LocalizableItem.LANGUAGE_FALLBACK_ENABLED, enabled);
			getSessionService().setAttribute(AbstractItemModel.LANGUAGE_FALLBACK_ENABLED_SERVICE_LAYER, enabled);
		}
	}

	/**
	 * Generates target URL accordingly to valid Preview Data passed as a parameter
	 * <p/>
	 *
	 * @param httpRequest
	 *           current request
	 * @param previewDataModel
	 *           valid data model contains all necessary information
	 * @return target URL
	 */
	protected String generatePreviewUrl(final HttpServletRequest httpRequest, final PreviewDataModel previewDataModel)
	{
		String generatedPreviewUrl = StringUtils.EMPTY;
		if (previewDataModel != null && StringUtils.isBlank(generatedPreviewUrl))
		{
			final AbstractPageModel abstractPageModel = previewDataModel.getPage();
			if (abstractPageModel == null)
			{
				generatedPreviewUrl = previewDataModel.getResourcePath();
			}
			else
			{
				generatedPreviewUrl = getPreviewDataModelUrlResolver().resolve(previewDataModel);
			}
		}
		if (StringUtils.isBlank(generatedPreviewUrl))
		{
			generatedPreviewUrl = UrlUtils.extractHostInformationFromRequest(httpRequest, getCmsSiteService().getCurrentSite());
		}

		return generatedPreviewUrl;
	}

	protected CMSSiteModel getCurrentCmsSite()
	{
		try
		{
			return getCmsSiteService().getCurrentSite();
		}
		catch (final JaloObjectNoLongerValidException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
			return null;
		}
	}

	private void cloneCart()
	{
		if (cartFacade.hasEntries())
		{
			final CartModel clonedCart = cartService.clone(null, null, cartService.getSessionCart(), null);
			clonedCart.setPaymentTransactions(null);
			clonedCart.setCode(null);
			modelService.save(clonedCart);
		}
	}

	/**
	 * Retrieves current mapping handler in order to generate proper target URL for CMS Page
	 * <p/>
	 *
	 * @return current mapping handler
	 */
	protected UrlResolver<PreviewDataModel> getPreviewDataModelUrlResolver()
	{
		return previewDataModelUrlResolver;
	}

	@Required
	public void setPreviewDataModelUrlResolver(final UrlResolver<PreviewDataModel> previewDataModelUrlResolver)
	{
		this.previewDataModelUrlResolver = previewDataModelUrlResolver;
	}

	protected CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	@Required
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected CMSPreviewService getCmsPreviewService()
	{
		return cmsPreviewService;
	}

	@Required
	public void setCmsPreviewService(final CMSPreviewService cmsPreviewService)
	{
		this.cmsPreviewService = cmsPreviewService;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected ContextInformationLoader getContextInformationLoader()
	{
		return contextInformationLoader;
	}

	@Required
	public void setContextInformationLoader(final ContextInformationLoader contextInformationLoader)
	{
		this.contextInformationLoader = contextInformationLoader;
	}

	protected CMSPageContextService getCmsPageContextService()
	{
		return cmsPageContextService;
	}

	@Required
	public void setCmsPageContextService(final CMSPageContextService cmsPageContextService)
	{
		this.cmsPageContextService = cmsPageContextService;
	}

	protected SiteChannelValidationStrategy getSiteChannelValidationStrategy()
	{
		return siteChannelValidationStrategy;
	}

	@Required
	public void setSiteChannelValidationStrategy(final SiteChannelValidationStrategy siteChannelValidationStrategy)
	{
		this.siteChannelValidationStrategy = siteChannelValidationStrategy;
	}
}
