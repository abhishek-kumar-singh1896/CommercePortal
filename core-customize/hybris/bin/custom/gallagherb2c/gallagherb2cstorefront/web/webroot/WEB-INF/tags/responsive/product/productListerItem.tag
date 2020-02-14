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
<c:set value="product__list--item" var="productTagClasses" />
<c:forEach var="tag" items="${product.tags}">
	<c:set value="${productTagClasses} tag-${tag}" var="productTagClasses" />
</c:forEach>


<%--                    <li class="${fn:escapeXml(productTagClasses)}"> --%>
<li class="product-tile">
<ycommerce:testId
		code="test_searchPage_wholeProduct">
		<div class="row">
			<div class="col-4 col-md-12 pr-xs-0">
				<div class="product-status">
					<img src="${commonResourcePath}/images/${fn:escapeXml(product.promoSticker)}.svg"/>
					<%-- 	<spring:theme code="text.productStatus.new" /> --%>
				</div>
				<div class="product-img-box">
					<a class="product__list--thumb" href="${fn:escapeXml(productUrl)}"
						title="${fn:escapeXml(product.name)}">
						<product:productPrimaryImage
							product="${product}" format="thumbnail" />
					</a>
				</div>
			</div>
			<div class="col-8 col-md-12">
				<div class="product-name-desc-out">
					<div class="product-name">
						<ycommerce:testId
							code="searchPage_productName_link_${product.code}">
							<a class="product__list--name" href="${fn:escapeXml(productUrl)}">${ycommerce:sanitizeHTML(product.name)}</a>
						</ycommerce:testId>
					</div>
					<div class="product-id">${product.code}</div>
					<div class="product-description">${product.description}</div>
				</div>
			</div>
		</div>

		<div class="product-list-footer">
			<div class="row">
				<div class="col-5">
					<c:forEach items="${product.animalCompatibility}" var="animal">
						<span class="product-list-footer-left-icon">
							<img src="${commonResourcePath}/images/${fn:escapeXml(animal)}.svg"/>
						</span>
					</c:forEach>
				</div>
				<ycommerce:testId code="searchPage_price_label_${product.code}">
					<div class="col-7 text-right">
						<product:productListerItemPrice product="${product}" />
					</div>
				</ycommerce:testId>
			</div>
		</div>

		<c:if test="${not empty product.summary}">
			<div class="product__listing--description">${ycommerce:sanitizeHTML(product.summary)}</div>
		</c:if>

		<c:set var="product" value="${product}" scope="request" />
		<c:set var="addToCartText" value="${addToCartText}" scope="request" />
		<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request" />


<!-- 		<div class="addtocart"> -->
<%-- 			<div id="actions-container-for-${fn:escapeXml(component.uid)}" --%>
<!-- 				class="row"> -->
<%-- 				<action:actions element="div" parentComponent="${component}" /> --%>
<!-- 			</div> -->
<!-- 		</div> -->

	</ycommerce:testId></li>


