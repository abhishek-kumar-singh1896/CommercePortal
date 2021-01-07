<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<c:set var="isUserAnonymous" value="${sessionScope.jalosession}" />

<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>
<c:set value="${not empty product.potentialPromotions}" var="hasPromotion"/>
<div class="product-item" onclick="window.location='${productUrl}'" title="${fn:escapeXml(product.name)}">
	<ycommerce:testId code="product_wholeProduct">
        		<div class="compare-box">
        				<label class="fcheckbox">
        					<input type="checkbox" name="compareCheckbox" class="compareCheckbox" value="${product.code}" <c:if test="${product.selectedForCompare}">checked</c:if> />
        				</label>
        				<span>Compare</span>
        		</div>
<%--         <a class="thumb" href="${productUrl}" title="${fn:escapeXml(product.name)}"> --%>
			<product:productPrimaryImage product="${product}" format="product"/>
<!-- 		</a> -->
		<div class="details">
			<ycommerce:testId code="product_productName">
				${ycommerce:sanitizeHTML(product.name)}
			</ycommerce:testId>

            <div class="partNumber">
					SKU: ${product.partNumber} 
			</div>

			<c:if test="${not empty product.potentialPromotions}">
				<div class="promo">
					<c:forEach items="${product.potentialPromotions}" var="promotion">
						${ycommerce:sanitizeHTML(promotion.description)}
					</c:forEach>
				</div>
			</c:if>
			<sec:authorize access="!hasRole('ROLE_B2BENDUSERGROUP')">
			<c:if test = "${not fn:contains(isUserAnonymous, 'anonymous')}">
				<ycommerce:testId code="product_productPrice">
					<div class="price"><product:productListerItemPrice product="${product}"/></div>
				</ycommerce:testId>
			</c:if>
			</sec:authorize>
			
			
			<c:forEach var="variantOption" items="${product.variantOptions}">
				<c:forEach items="${variantOption.variantOptionQualifiers}" var="variantOptionQualifier">
					<c:if test="${variantOptionQualifier.qualifier eq 'rollupProperty'}">
	                    <c:set var="rollupProperty" value="${variantOptionQualifier.value}"/>
	                </c:if>
					<c:if test="${variantOptionQualifier.qualifier eq 'thumbnail'}">
	                    <c:set var="imageUrl" value="${fn:escapeXml(variantOptionQualifier.value)}"/>
	                </c:if>
	                <c:if test="${variantOptionQualifier.qualifier eq rollupProperty}">
	                    <c:set var="variantName" value="${fn:escapeXml(variantOptionQualifier.value)}"/>
	                </c:if>
				</c:forEach>
				<img style="width: 32px; height: 32px;" src="${imageUrl}" title="${variantName}" alt="${variantName}"/>
			</c:forEach>
		</div>


		<c:set var="product" value="${product}" scope="request"/>
		<c:set var="addToCartText" value="${addToCartText}" scope="request"/>
		<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request"/>
		<c:set var="isGrid" value="true" scope="request"/>
<!-- 		<div class="addtocart"> -->
<%-- 			<div class="actions-container-for-${fn:escapeXml(component.uid)} <c:if test="${ycommerce:checkIfPickupEnabledForStore() and product.availableForPickup}"> pickup-in-store-available</c:if>"> --%>
<%-- 				<action:actions element="div" parentComponent="${component}"/> --%>
<!-- 			</div> -->
<!-- 		</div> -->
	</ycommerce:testId>
</div>