<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>

<template:page pageTitle="${pageTitle}">

	<nav:refineFilterModalResponsive searchPageData="${searchPageData}" />

	<div class="product-list">
		<cms:pageSlot position="SearchGridBanner" var="feature" element="div"
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
							target="_blank" href="${sitecoreSolutionPageData}"> <spring:theme
									code="text.searchTab.solutions" />
						</a></li>
						<li class="nav-item"><a class="nav-link" id="support-tab"
							target="_blank" href="${sitecoreSupportPageData}"> <spring:theme
									code="text.searchTab.support" />
						</a></li>
					</ul>
				</div>
			</div>


			<div class="container">
				<div class="tab-content" id="searchResultTabContent">
					<div class="tab-pane fade show active" id="products"
						role="tabpanel" aria-labelledby="products-tab">


						<div class="search-result-count">
						<spring:theme code="search.page.searchText"
									arguments="${searchPageData.pagination.totalNumberOfResults}, ${searchPageData.freeTextSearch}" htmlEscape="false" />
						</div>


						<div class="product-list-container-out">
							<div class="container">
								<div class="row">
									<div class="col-lg-3 d-none d-lg-block">
										<cms:pageSlot position="ProductLeftRefinements" var="feature"
											element="div" class="search-grid-page-left-refinements-slot">
											<cms:component component="${feature}" element="div"
												class="search-grid-page-left-refinements-component" />
										</cms:pageSlot>
									</div>
									<div class="col-lg-9 plr-xs-0">
										<cms:pageSlot position="SearchResultsGridSlot" var="feature"
											element="div" class="search-grid-page-result-grid-slot">
											<cms:component component="${feature}" element="div"
												class="search-grid-page-result-grid-component" />
										</cms:pageSlot>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<storepickup:pickupStorePopup />
			</div>
</template:page>