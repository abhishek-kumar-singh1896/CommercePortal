<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ attribute name="metaDescription" required="false" %>
<%@ attribute name="metaKeywords" required="false" %>
<%@ attribute name="pageCss" required="false" fragment="true" %>
<%@ attribute name="pageScripts" required="false" fragment="true" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="analytics" tagdir="/WEB-INF/tags/shared/analytics" %>
<%@ taglib prefix="addonScripts" tagdir="/WEB-INF/tags/responsive/common/header" %>
<%@ taglib prefix="generatedVariables" tagdir="/WEB-INF/tags/shared/variables" %>
<%@ taglib prefix="debug" tagdir="/WEB-INF/tags/shared/debug" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="htmlmeta" uri="http://hybris.com/tld/htmlmeta"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<!DOCTYPE html>
<html lang="${fn:escapeXml(currentLanguage.isocode)}">
<head>

	<script type="text/javascript">
		var dataLayer = [];
	</script>

	<analytics:commercegtm />
	<!-- Google Tag Manager -->
	<c:if test="${not empty commerceGTMId}">	
		<script>
			(function(w, d, s, l, i) {
				w[l] = w[l] || [];
				w[l].push({
					'gtm.start' : new Date().getTime(),
					event : 'gtm.js'
				});
				var f = d.getElementsByTagName(s)[0], j = d.createElement(s), dl = l != 'dataLayer' ? '&l='
					+ l
					: '';
				j.async = true;
				j.src = 'https://www.googletagmanager.com/gtm.js?id=' + i + dl;
				f.parentNode.insertBefore(j, f);
			})(window, document, 'script', 'dataLayer', '${commerceGTMId}');
		</script>
	</c:if>
	<!-- End Google Tag Manager -->
	<title>
		${not empty pageTitle ? pageTitle : not empty cmsPage.title ? fn:escapeXml(cmsPage.title) : 'Accelerator Title'}
	</title>
	<c:if test="${not empty canonicalURL}">
		<link rel="canonical" href="${canonicalURL}" >
	</c:if>
	<c:if test="${not empty hreflangMap}">
		<c:forEach items="${hreflangMap}" var="entry">
	    	<link rel="alternate" hreflang="${entry.key}" href="${entry.value}" >
		</c:forEach>
	</c:if>
	<%-- Meta Content --%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

	<%-- Additional meta tags --%>
	<htmlmeta:meta items="${metatags}"/>

	<%-- Favourite Icon --%>
	<spring:theme code="img.favIcon" text="/" var="favIconPath"/>
	
	<c:choose>
		<%-- if empty webroot, skip originalContextPath, simply use favIconPath --%>
		<c:when test="${fn:length(originalContextPath) eq 1}" >
			<link rel="shortcut icon" type="image/x-icon" media="all" href="${favIconPath}" />
		</c:when>
		<c:otherwise>
			<link rel="shortcut icon" type="image/x-icon" media="all" href="${originalContextPath}${favIconPath}" />
		</c:otherwise>
	</c:choose>

	<%-- CSS Files Are Loaded First as they can be downloaded in parallel --%>
	<template:styleSheets/>

	<%-- Inject any additional CSS required by the page --%>
	<jsp:invoke fragment="pageCss"/>
	<analytics:analytics/>
	<generatedVariables:generatedVariables/>
</head>

<body class="${pageBodyCssClasses} ${cmsPageRequestContextData.liveEdit ? ' yCmsLiveEdit' : ''} language-${fn:escapeXml(currentLanguage.isocode)}" <c:if test="${cmsPage.uid eq 'productDetails'}">data-spy="scroll" data-target="#productDetailTab"</c:if>>

	<!-- Madwire Google Tag Manager (noscript) -->
	<c:if test="${not empty gtmId}">	
		<noscript>
			<iframe src="https://www.googletagmanager.com/ns.html?id=${gtmId}"
				height="0" width="0" style="display: none; visibility: hidden"></iframe>
		</noscript>
	</c:if>
	<c:if test="${not empty commerceGTMId}">	
		<noscript>
			<iframe src="https://www.googletagmanager.com/ns.html?id=${commerceGTMId}"
				height="0" width="0" style="display: none; visibility: hidden"></iframe>
		</noscript>
	</c:if>		
	<!-- End Madwire Google Tag Manager (noscript) -->
	<%-- Inject the page body here --%>
	<jsp:doBody/>


	<form name="accessiblityForm">
		<input type="hidden" id="accesibility_refreshScreenReaderBufferField" name="accesibility_refreshScreenReaderBufferField" value=""/>
	</form>
	<div id="ariaStatusMsg" class="skip" role="status" aria-relevant="text" aria-live="polite"></div>

	<%-- Load JavaScript required by the site --%>
	<template:javaScript/>
	
	<%-- Inject any additional JavaScript required by the page --%>
	<jsp:invoke fragment="pageScripts"/>

	<%-- Inject CMS Components from addons using the placeholder slot--%>
	<addonScripts:addonScripts/>


</body>

<debug:debugFooter/>

</html>
