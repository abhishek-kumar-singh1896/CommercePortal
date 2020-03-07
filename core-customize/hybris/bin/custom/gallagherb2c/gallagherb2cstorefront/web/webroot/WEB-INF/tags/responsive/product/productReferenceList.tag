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

<li class="product-tile"><ycommerce:testId
		code="product_wholeProduct">
		<div class="row">
			<div class="col-4 col-md-12 pr-xs-0">
			
				<div class="product-status">
				<c:if test = "${not empty product.promoSticker}">
					<img
						src="${commonResourcePath}/images/${fn:toLowerCase(product.promoSticker)}.svg" />
					</c:if>
				</div>
				<div class="product-img-box">
					<a class="product__list--thumb" href="${fn:escapeXml(productUrl)}"
						title="${fn:escapeXml(product.name)}">
						<c:forEach items="${product.images}" var="medias">
                         <c:if test="${medias.format eq 'product'}">
                         <img src="${medias.url}" alt="${medias.altText}">
                         </c:if>
                         </c:forEach>
						<%-- <product:productPrimaryImage
							product="${product}" format="product" /> --%>
					</a>
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
					<div class="product-description">${product.description}</div>
				</div>
			</div>
		</div>
		<div class="product-list-footer">
			<div class="row">
				<div class="col-6">
					<c:forEach items="${product.animalCompatibility}" var="animal">
						<span class="product-list-footer-left-icon"> <img
							src="${commonResourcePath}/images/${fn:escapeXml(animal)}.svg" />
						</span>
					</c:forEach>
				</div>
				<ycommerce:testId code="searchPage_price_label_${product.code}">
					<div class="col-6 text-right">
						<product:productListerItemPrice product="${product}" />
					</div>
				</ycommerce:testId>
			</div>
		</div>
		<c:set var="product" value="${product}" scope="request" />
		<c:set var="addToCartText" value="${addToCartText}" scope="request" />
		<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request" />
		<c:set var="isGrid" value="true" scope="request" />
		
	</ycommerce:testId></li>