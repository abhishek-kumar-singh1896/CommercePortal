<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<template:page pageTitle="${pageTitle}">

	<div class="product-list">
		<cms:pageSlot position="SearchResultsEmptyBanner" var="feature" element="div"
			class="product-grid-section1-slot">
			<cms:component component="${feature}" element="div"
				class="yComponentWrapper map product-grid-section1-component" />
		</cms:pageSlot>
	

		<!-- nav in search result page -->
		<div class="search-result-tab-out">
			<div class="search-result-inner-container">
				<div class="container">
					<ul class="nav nav-tabs" id="searchResultTab" role="tablist">
						<li class="nav-item"><a class="nav-link active"
							id="products-tab" data-toggle="tab" href="#products" role="tab"
							aria-controls="products" aria-selected="true"> <spring:theme
									code="text.searchTab.products" />
						</a></li>
						<li class="nav-item"><a class="nav-link" id="solutions-tab"
							href="${sitecoreSolutionPageData}"> <spring:theme
									code="text.searchTab.solutions" />
						</a></li>
						<li class="nav-item"><a class="nav-link" id="support-tab"
							href="${sitecoreSupportPageData}"> <spring:theme
									code="text.searchTab.support" />
						</a></li>
					</ul>
				</div>
			</div>


			<div class="container">
				<div class="tab-content" id="searchResultTabContent">
					<div class="tab-pane fade show active" id="products"
						role="tabpanel" aria-labelledby="products-tab">

						<c:url value="/" var="homePageUrl" />
						<cms:pageSlot position="SideContent" var="feature" element="div" class="side-content-slot cms_disp-img_slot searchEmptyPageTop">
							<cms:component component="${feature}" element="div" class="no-space yComponentWrapper searchEmptyPageTop-component"/>
						</cms:pageSlot>
						
						<div class="search-empty">
							<div class="headline">
								<spring:theme code="search.no.results" arguments="${searchPageData.freeTextSearch}" var="noSearchResults" htmlEscape="false"/>
								${ycommerce:sanitizeHTML(noSearchResults)}
							</div>
							<a class="btn btn-default  js-shopping-button" href="${fn:escapeXml(homePageUrl)}">
								<spring:theme code="general.continue.shopping" text="Continue Shopping"/>
							</a>
						</div>
						
						<cms:pageSlot position="MiddleContent" var="comp" element="div" class="searchEmptyPageMiddle">
							<cms:component component="${comp}" element="div" class="yComponentWrapper searchEmptyPageMiddle-component"/>
						</cms:pageSlot>
					
						<cms:pageSlot position="BottomContent" var="comp" element="div" class="searchEmptyPageBottom">
							<cms:component component="${comp}" element="div" class="yComponentWrapper searchEmptyPageBottom-component"/>
						</cms:pageSlot>
						
					</div>
				</div>
			</div>
		</div>
	</div>

</template:page>