<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:theme code="text.addToCart" var="addToCartText" />
<c:url value="${product.url}" var="productUrl" />
<c:set value="${not empty product.potentialPromotions}"
	var="hasPromotion" />

<c:set value="product-item" var="productTagClasses" />
<c:forEach var="tag" items="${product.tags}">
	<c:set value="${productTagClasses} tag-${tag}" var="productTagClasses" />
</c:forEach>

<%-- <div class="${fn:escapeXml(productTagClasses)}"> --%>
<li class="product-tile" onclick="window.location='${productUrl}'" title="${fn:escapeXml(product.name)}">
	<ycommerce:testId
		code="product_wholeProduct">
		<div class="row">
			<div class="col-4 col-md-12 pr-xs-0">
			
				<div class="product-status">
				<c:if test = "${not empty product.promoSticker}">
					<img
						src="${commonResourcePath}/images/${fn:escapeXml(product.promoSticker)}.svg" />
					<%-- 	<spring:theme code="text.productStatus.new" /> --%>
					
					</c:if>
				</div>
				<div class="product-img-box">
<%-- 					<a class="product__list--thumb" href="${fn:escapeXml(productUrl)}" title="${fn:escapeXml(product.name)}"> --%>
						<product:productPrimaryImage
							product="${product}" format="product" />
<!-- 					</a>	 -->
				</div>
			</div>
			<!-- 		<div class="details"> -->
			<div class="col-8 col-md-12">
				<div class="product-name-desc-out">
					<div class="product-name">
						<ycommerce:testId code="product_productName">
							${ycommerce:sanitizeHTML(product.name)}
							
						</ycommerce:testId>
					</div>
					<div class="product-id">${product.code}</div>
					<div class="product-description">${product.plpProductDescription}</div>
				</div>
			</div>
		</div>
		<div class="product-list-footer">
			<div class="row">
				<div class="col-5 pr-0">
					<c:forEach items="${product.animalCompatibility}" begin="0" end="1" var="animal" >
						<span class="product-list-footer-left-icon"> <img
							src="${commonResourcePath}/images/animals/${fn:escapeXml(animal)}.svg" />
						</span>
					</c:forEach>
				</div>
				<ycommerce:testId code="searchPage_price_label_${product.code}">
					<div class="col-7 text-right pl-0">
						<product:productListerItemPrice product="${product}" />
					</div>
				</ycommerce:testId>
			</div>
		</div>

		<%-- <c:if test="${not empty product.summary}">
			<div class="product__listing--description">${ycommerce:sanitizeHTML(product.summary)}</div>
		</c:if> --%>

		<%-- 		<c:if test="${not empty product.potentialPromotions}"> --%>
		<!-- 						<div class="promo"> -->
		<%-- 							<c:forEach items="${product.potentialPromotions}" var="promotion"> --%>
		<%-- 						${ycommerce:sanitizeHTML(promotion.description)} --%>
		<%-- 					</c:forEach> --%>
		<!-- 						</div> -->
		<%-- 					</c:if> --%>

		<%-- 					<ycommerce:testId code="product_productPrice"> --%>
		<!-- 						<div class="price"> -->
		<%-- 							<product:productListerItemPrice product="${product}" /> --%>
		<!-- 						</div> -->
		<%-- 					</ycommerce:testId> --%>
		<%-- 					<c:forEach var="variantOption" items="${product.variantOptions}"> --%>
		<%-- 						<c:forEach items="${variantOption.variantOptionQualifiers}" --%>
		<%-- 							var="variantOptionQualifier"> --%>
		<%-- 							<c:if --%>
		<%-- 								test="${variantOptionQualifier.qualifier eq 'rollupProperty'}"> --%>
		<%-- 								<c:set var="rollupProperty" --%>
		<%-- 									value="${variantOptionQualifier.value}" /> --%>
		<%-- 							</c:if> --%>
		<%-- 							<c:if test="${variantOptionQualifier.qualifier eq 'thumbnail'}"> --%>
		<%-- 								<c:set var="imageUrlHtml" --%>
		<%-- 									value="${fn:escapeXml(variantOptionQualifier.value)}" /> --%>
		<%-- 							</c:if> --%>
		<%-- 							<c:if --%>
		<%-- 								test="${variantOptionQualifier.qualifier eq rollupProperty}"> --%>
		<%-- 								<c:set var="variantNameHtml" --%>
		<%-- 									value="${fn:escapeXml(variantOptionQualifier.value)}" /> --%>
		<%-- 							</c:if> --%>
		<%-- 						</c:forEach> --%>
		<%-- 						<img style="width: 32px; height: 32px;" src="${imageUrlHtml}" --%>
		<%-- 							title="${variantNameHtml}" alt="${variantNameHtml}" /> --%>
		<%-- 					</c:forEach> --%>
		<!-- 				</div> -->


		<c:set var="product" value="${product}" scope="request" />
		<c:set var="addToCartText" value="${addToCartText}" scope="request" />
		<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request" />
		<c:set var="isGrid" value="true" scope="request" />
		<!-- 		<div class="addtocart"> -->
		<%-- 			<div class="actions-container-for-${fn:escapeXml(component.uid)} <c:if test="${ycommerce:checkIfPickupEnabledForStore() and product.availableForPickup}"> pickup-in-store-available</c:if>"> --%>
		<%-- 				<action:actions element="div" parentComponent="${component}"/> --%>
		<!-- 			</div> -->
		<!-- 		</div> -->
	</ycommerce:testId></li>