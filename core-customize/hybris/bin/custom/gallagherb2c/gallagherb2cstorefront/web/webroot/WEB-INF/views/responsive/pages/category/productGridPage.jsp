<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<template:page pageTitle="${pageTitle}">

	<!-- Modal product list refine filter -->
	<nav:refineFilterModalResponsive searchPageData="${searchPageData}"/>

	<div class="product-list">
		<cms:pageSlot position="Section1" var="feature" element="div"
			class="product-grid-section1-slot">
			<cms:component component="${feature}" element="div"
				class="yComponentWrapper map product-grid-section1-component" />
		</cms:pageSlot>

		<div class="product-list-container-out">
			<div class="container">
				<div class="row">
					<div class="col-lg-3 d-none d-lg-block">
						<cms:pageSlot position="ProductLeftRefinements" var="feature"
							element="div" class="product-grid-left-refinements-slot">
							<cms:component component="${feature}" element="div"
								class="yComponentWrapper product-grid-left-refinements-component" />
						</cms:pageSlot>
					</div>
					<div class="col-lg-9 plr-xs-0" id="post-result">
						<cms:pageSlot position="ProductGridSlot" var="feature"
							element="div" class="product-grid-right-result-slot">
							<cms:component component="${feature}" element="div"
								class="product__list--wrapper yComponentWrapper product-grid-right-result-component" />
						</cms:pageSlot>
					</div>
				</div>
			</div>
		</div>
	</div>
	<storepickup:pickupStorePopup />
</template:page>