<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

<template:page pageTitle="${pageTitle}">
    <div class="row product-category-page">
        <div class="col-md-3 col-lg-2 facetNavigation hidden-sm hidden-xs">
            <!--<nav:categoryNav pageData="${searchPageData}"/>-->
            <cms:pageSlot position="Section4" var="feature">
                <cms:component component="${feature}" element="div" class="section4 small_detail"/>
            </cms:pageSlot>
        </div>

        <div class="col-md-9 col-lg-10 product-categories">
            <div class="row simpleimagecomponent">
                <cms:pageSlot position="Section1" var="feature">
                    <cms:component component="${feature}"/>
                </cms:pageSlot>
            </div>
            <div class="row hidden-md hidden-lg refine-bar align-center">
                <product:productRefineButton styleClass="btn btn-default js-show-facets"/>
            </div>

            <div class="row">
                <cms:pageSlot position="Section2" var="feature">
                    <cms:component component="${feature}" element="div"
                                   class="simpleimagecomponent pcp-prod col-xs-12 col-sm-12"/>
                </cms:pageSlot>
            </div>
            <cms:pageSlot position="Section3" var="feature" element="div" class="col-xs-12 col-sm-12 col-md-12 col-lg-12 padding-top-10 last">
	          <cms:component component="${feature}" element="div" class="simpleimagecomponent col-xs-12 col-sm-6 col-md-4 col-lg-4 section3 cms_disp-img_slot ${(elementPos%3 == 2) ? 'last' : ''}"/>
    	    </cms:pageSlot>

        </div>
    </div>
</template:page>