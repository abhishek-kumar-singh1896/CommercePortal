<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="productData" required="false" type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ attribute name="quantity" required="false" type="java.lang.Long"%>
<div class="td price">
	<h3 class="mobile-table-heading">Price</h3>
	<div class="price-details2">
		<div id="price" class="priceValue">
				<span>${productData.price.formattedValue}</span>
		</div>
	</div>
</div>
<div class="td qty">
	<h3 class="mobile-table-heading">Quantity</h3>
	<input type="hidden" class="qtyMoreThanStock" value="<spring:theme code='text.quickOrder.page.error.quantityMoreThanStock' />" />
	<c:choose>
	<c:when test="${quantity > 0}">
	<input type="text"
		class="js-quick-order-qty disable-paste js-qty-input" value="${quantity}"
		maxlength="5" autocomplete="off" autocorrect="off" data-html="true"
		data-placement="bottom" data-updated="false" data-prevQty="0" oninput="this.value=this.value.replace(/[^0-9]/g,'');"
		data-original-title="<spring:theme code='text.quickOrder.page.error.quantityMoreThanStock' />" />
	</c:when>
	<c:otherwise>
		<input type="text"
		class="js-quick-order-qty disable-paste js-qty-input" value="${0}"
		maxlength="5" autocomplete="off" autocorrect="off" data-html="true"
		data-placement="bottom" data-updated="false" data-prevQty="0" oninput="this.value=this.value.replace(/[^0-9]/g,'');"
		 data-original-title="<spring:theme code='text.quickOrder.page.error.quantityMoreThanStock'/>" />
	</c:otherwise>
	</c:choose>
</div>

<div class="td item-total-price">
	<h3 class="mobile-table-heading">Total Price</h3>
	<div class="price-details2">
		<div id="price" class="priceValue"></div>
	</div>
</div>
<div class="remove">
	<div class="price-edit-delete">
		<a href="#" class="js-remove-quick-order-row">
			<span class="glyphicon glyphicon-remove"></span>
		</a>
	</div>
</div>
