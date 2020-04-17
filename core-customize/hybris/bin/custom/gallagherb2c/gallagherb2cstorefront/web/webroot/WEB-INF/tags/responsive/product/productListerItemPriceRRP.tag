<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ycommerce:testId code="searchPage_price_rrp_${product.code}">

		<c:choose>
			<c:when test="${product.multidimensional and (product.priceRange.minPrice.value ne product.priceRange.maxPrice.value)}">
				<div class="currency-text"> RRP </div>
			</c:when>
			<c:when test="${not empty product.price}">
				<div class="currency-text"> RRP </div>
			</c:when>
			<c:otherwise/>
		</c:choose>

</ycommerce:testId>
