<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="referenceProduct" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:theme code="text.addToCart" var="addToCartText" />
<c:url value="${referenceProduct.url}" var="productUrl" />
<c:set value="${not empty referenceProduct.potentialPromotions}"
	var="hasPromotion" />

<c:set value="product-item" var="productTagClasses" />
<c:forEach var="tag" items="${referenceProduct.tags}">
	<c:set value="${productTagClasses} tag-${tag}" var="productTagClasses" />
</c:forEach>

<li class="product-tile"><ycommerce:testId
		code="product_wholeProduct">
		<div class="row">
			<div class="col-4 col-md-12 pr-xs-0">
			
				<div class="product-status">
				<c:if test = "${not empty referenceProduct.promoSticker}">
					<img
						src="${commonResourcePath}/images/${fn:toLowerCase(referenceProduct.promoSticker)}.svg" />
					</c:if>
				</div>
				<div class="product-img-box">
					<a class="product__list--thumb" href="${fn:escapeXml(productUrl)}"
						title="${fn:escapeXml(referenceProduct.name)}">
						<c:forEach items="${referenceProduct.images}" var="medias">
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
							${ycommerce:sanitizeHTML(referenceProduct.name)}
							
						</ycommerce:testId>
					</div>
					<div class="product-id">${referenceProduct.code}</div>
					<div class="product-description">${referenceProduct.description}</div>
				</div>
			</div>
		</div>
		<div class="product-list-footer">
			<div class="row">
				<div class="col-6">
					<c:forEach items="${referenceProduct.animalCompatibility}" var="animal">
						<span class="product-list-footer-left-icon"> <img
							src="${commonResourcePath}/images/${fn:escapeXml(animal)}.svg" />
						</span>
					</c:forEach>
				</div>
				<ycommerce:testId code="searchPage_price_label_${referenceProduct.code}">
					<div class="col-6 text-right">
						<product:productListerItemPrice product="${referenceProduct}" />
					</div>
				</ycommerce:testId>
			</div>
		</div>
		<c:set var="referenceProduct" value="${referenceProduct}" scope="request" />
		<c:set var="addToCartText" value="${addToCartText}" scope="request" />
		<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request" />
		<c:set var="isGrid" value="true" scope="request" />
		
	</ycommerce:testId></li>