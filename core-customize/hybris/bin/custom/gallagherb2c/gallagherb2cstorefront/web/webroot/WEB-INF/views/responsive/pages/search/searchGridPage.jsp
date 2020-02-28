<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<template:page pageTitle="${pageTitle}">

<nav:refineFilterModalResponsive searchPageData="${searchPageData}"/>


	<div class="product-list">

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

	<storepickup:pickupStorePopup />

</template:page>