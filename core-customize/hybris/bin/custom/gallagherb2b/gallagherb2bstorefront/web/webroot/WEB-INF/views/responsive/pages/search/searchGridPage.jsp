<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<template:page pageTitle="${pageTitle}">

	<div class="search-result-tab-out">
		<div class="search-result-inner-container">
			<div class="row">
				<div class="col-sm-12">
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="nav-item <c:if test="${empty technicalSupport}">active</c:if>"><a
							class="nav-link" href="#products" aria-controls="products"
							role="tab" data-toggle="tab">
								<spring:theme code="text.searchTab.product" />
							</a>
						</li>
						<li role="presentation" class="nav-item <c:if test="${not empty technicalSupport}">active</c:if>"><a class="nav-link"
							href="#technicalSupport" aria-controls="profile" role="tab"
							data-toggle="tab">
								<spring:theme code="text.searchTab.technicalSupport" />
							</a>
						</li>
						<li role="presentation" class="nav-item"><a class="nav-link"
							href="${sitecoreSolutionPageData}">
								<spring:theme code="text.searchTab.solution" />
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>


		<div class="tab-content" id="searchResultTabContent">
			<div class="tab-pane <c:if test="${empty technicalSupport}">active</c:if>" id="products" role="tabpanel">
				<div class="row">

					<div class="col-xs-3">
						<cms:pageSlot position="ProductLeftRefinements" var="feature"
							element="div" class="search-grid-page-left-refinements-slot">
							<cms:component component="${feature}" element="div"
								class="search-grid-page-left-refinements-component" />
						</cms:pageSlot>
					</div>
					<div class="col-sm-12 col-md-9">
						<cms:pageSlot position="SearchResultsGridSlot" var="feature"
							element="div" class="search-grid-page-result-grid-slot">
							<cms:component component="${feature}" element="div"
								class="search-grid-page-result-grid-component" />
						</cms:pageSlot>
					</div>
				</div>
			</div>

			<!-- technical support tab  -->
            <!-- Begin Sign-In Touchpoint, Type = login -->
           <script async="async" src="${mindtouchLoginSRC}"></script>
            <div class="tab-pane<c:if test="${not empty technicalSupport}">active</c:if>" id="technicalSupport" role="tabpanel" style="height: 600px">
                <script type="mindtouch/embed" id="${mindtouchLoginID}" data-search-query="${searchPageData.freeTextSearch}"></script>
            </div>
			<script>
                                                  document.addEventListener('mindtouch-web-widget:search:ready', ({ data }) => {
                                                    const searchWidget = data.widget;

                                                    document.addEventListener('mindtouch-web-widget:login:auth-changed', ({ data }) => {
                                                        // rerun search query
                                                        var q = searchWidget.query;
                                                        searchWidget.query = "";
                                                        searchWidget.query = q;
                                                    });
                                                  });
             </script>
		</div>
	</div>

	<storepickup:pickupStorePopup />

</template:page>