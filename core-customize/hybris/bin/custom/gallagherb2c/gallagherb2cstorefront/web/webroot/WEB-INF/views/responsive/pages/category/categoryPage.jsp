<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="category" tagdir="/WEB-INF/tags/responsive/category"%>


<template:page pageTitle="${pageTitle}">
    <div class="product-category-list">
      <cms:pageSlot position="Section1" var="feature" element="div"
			class="product-grid-section1-slot">
			<cms:component component="${feature}" element="div"
				class="yComponentWrapper map product-grid-section1-component" />
		</cms:pageSlot>
      <div class="container">
        <div class="product-category-list-container-out">
            <div class="row">
				<c:forEach items="${searchPageData.subCategories}" var="category">
					<category:categoryFeatureComponent categoryData="${category}" />
				</c:forEach>
            </div>
        </div>
    </div>
    </div>
</template:page>