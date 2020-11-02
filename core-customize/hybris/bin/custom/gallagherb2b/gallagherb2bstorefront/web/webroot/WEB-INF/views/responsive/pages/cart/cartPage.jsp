<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<input type="hidden" id="showRecommendationsForProducts" value="${hasRecommendations}"/>
<template:page pageTitle="${pageTitle}">

	<cart:cartValidation/>
	<cart:cartPickupValidation/>
	
	<c:if test="${not empty minimumThresholdMessage}">
		<div class="alert alert-danger alert-dismissable">
				<button class="close" aria-hidden="true" data-dismiss="alert" type="button">&times;</button>
				${minimumThresholdMessage}
		</div>
	</c:if>
	
	<c:if test="${not empty minimumQuantityThresholdMessage}">
		<div class="alert alert-danger alert-dismissable">
				<button class="close" aria-hidden="true" data-dismiss="alert" type="button">&times;</button>
				${minimumQuantityThresholdMessage}
		</div>
	</c:if>

	<div class="cart-top-bar">
        <div class="text-right">
            <spring:theme var="textHelpHtml" code="text.help" />
            <a href="" class="help js-cart-help" data-help="${fn:escapeXml(textHelpHtml)}">${textHelpHtml}
                <span class="glyphicon glyphicon-info-sign"></span>
            </a>
            <div class="help-popup-content-holder js-help-popup-content">
                <div class="help-popup-content">
                    <strong>${fn:escapeXml(cartData.code)}</strong>
                    <spring:theme var="cartHelpContentVar" code="basket.page.cartHelpContent" htmlEscape="false" />
                    <c:set var="cartHelpContentVarSanitized" value="${ycommerce:sanitizeHTML(cartHelpContentVar)}" />
                    <div>${cartHelpContentVarSanitized}</div>
                </div>
            </div>
		</div>
	</div>

	<div>
		<div>
            <cms:pageSlot position="TopContent" var="feature">
                <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
            </cms:pageSlot>
		</div>

	   <c:if test="${not empty cartData.rootGroups}">
           <cms:pageSlot position="CenterLeftContentSlot" var="feature">
                <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
           </cms:pageSlot>
        </c:if>
		
		 <c:if test="${not empty cartData.rootGroups}">
            <cms:pageSlot position="CenterRightContentSlot" var="feature">
                <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
            </cms:pageSlot>
            <cms:pageSlot position="BottomContentSlot" var="feature">
                <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
            </cms:pageSlot>
		</c:if>
				
		<c:if test="${empty cartData.rootGroups}">
            <cms:pageSlot position="EmptyCartMiddleContent" var="feature">
                <cms:component component="${feature}" element="div" class="yComponentWrapper content__empty"/>
            </cms:pageSlot>
		</c:if>
	</div>
	<div class="recommendations-modal" id="recommendationsModal">
	</div>
</template:page>
