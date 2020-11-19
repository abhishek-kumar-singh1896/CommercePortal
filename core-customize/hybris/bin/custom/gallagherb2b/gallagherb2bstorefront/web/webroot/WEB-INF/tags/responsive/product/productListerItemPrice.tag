<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>

<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<ycommerce:testId code="searchPage_price_label_${product.code}">

	<!-- if product is multidimensional with different prices, show range, else, show unique price -->
	<c:choose>
		<c:when
			test="${product.multidimensional and (product.priceRange.minPrice.value ne product.priceRange.maxPrice.value)}">
			<format:price priceData="${product.priceRange.minPrice}" /> - <format:price
				priceData="${product.priceRange.maxPrice}" />
		</c:when>

		<c:when
			test="${product.priceRange.minPrice ne null && (product.priceRange.minPrice.value eq product.priceRange.maxPrice.value)}">
			<c:if test="${product.priceRange.minPrice.value gt 0}">
			<format:fromPrice priceData="${product.priceRange.minPrice}" />
			</c:if>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when
					test="${not empty product.totalDiscounts and not empty product.totalDiscounts[0]}">

					<span class="font-size16 strike-through bold">
						<format:fromPrice priceData="${product.price}" />
					</span>

					<span class="discounted bold">${product.totalDiscounts[0].value}</span>

				</c:when>
				<c:otherwise>
					<span class="bold">
					<c:if test="${product.price.value gt 0}">
						<format:fromPrice priceData="${product.price}" />
					</c:if>
					</span>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>

</ycommerce:testId>
