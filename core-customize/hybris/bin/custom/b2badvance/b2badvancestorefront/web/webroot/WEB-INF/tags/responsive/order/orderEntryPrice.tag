<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<%@ attribute name="orderEntry" required="true"
	type="de.hybris.platform.commercefacades.order.data.OrderEntryData"%>

<%-- if product is multidimensional with different prices, show range, else, show unique price --%>
<c:choose>
	<c:when
		test="${not orderEntry.product.multidimensional or (orderEntry.product.priceRange.minPrice.value eq orderEntry.product.priceRange.maxPrice.value)}">
		<c:choose>
			<c:when
				test="${orderEntry.basePrice.value ne orderEntry.sellingPrice.value}">
				<span class="font-size12 actual bold strike-through"> <format:price
						priceData="${orderEntry.basePrice}" displayFreeForZero="true" /></span>
				<span class="discounted bold"> <format:price
						priceData="${orderEntry.sellingPrice}" displayFreeForZero="true" /></span>
			</c:when>
			<c:otherwise>
				<format:price priceData="${orderEntry.basePrice}"
					displayFreeForZero="true" />
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<format:price priceData="${orderEntry.product.priceRange.minPrice}" displayFreeForZero="true" />
                    -
        <format:price priceData="${orderEntry.product.priceRange.maxPrice}" displayFreeForZero="true" />
	</c:otherwise>
</c:choose>