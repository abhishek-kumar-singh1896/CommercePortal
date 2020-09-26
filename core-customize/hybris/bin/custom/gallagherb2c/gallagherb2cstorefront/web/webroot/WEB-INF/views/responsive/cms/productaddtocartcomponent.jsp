<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="isForceInStock" value="${product.stock.stockLevelStatus.code eq 'inStock' and empty product.stock.stockLevel}"/>
<c:choose> 
  <c:when test="${isForceInStock}">
    <c:set var="maxQty" value="FORCE_IN_STOCK"/>
  </c:when>
  <c:otherwise>
    <c:set var="maxQty" value="${product.stock.stockLevel}"/>
  </c:otherwise>
</c:choose>

<c:set var="qtyMinus" value="1" />

<div class="addtocart-component d-flex">
		<c:if test="${empty showAddToCart ? true : showAddToCart}">
			<c:set var="buttonType">button</c:set>
			<c:if test="${product.purchasable and product.stock.stockLevelStatus.code ne 'outOfStock' }">
				<c:set var="buttonType">submit</c:set>
			</c:if>
			<div class="qty-selector js-qty-selector">
			<c:choose>
				<c:when test="${empty product.baseProduct and not empty product.variantMatrix}">
					<div class="select-variant">
						Please select a variant
					</div>
				</c:when>
				<c:otherwise>
				<c:choose>
					<c:when test="${fn:contains(buttonType, 'button')}">
						<input type="number" maxlength="3" class="form-control js-qty-selector-input add-to-cart-input" size="1" value="${fn:escapeXml(qtyMinus)}"
						   data-max="${fn:escapeXml(maxQty)}" data-min="1" name="pdpAddtoCartInput"  id="pdpAddtoCartInput" disabled="disabled"/>
					</c:when>
					<c:otherwise>
						<input type="number" maxlength="3" class="form-control js-qty-selector-input add-to-cart-input" size="1" value="${fn:escapeXml(qtyMinus)}"
						   data-max="${fn:escapeXml(maxQty)}" data-min="1" name="pdpAddtoCartInput"  id="pdpAddtoCartInput"/>
					</c:otherwise>
				</c:choose>
				</c:otherwise>
			</c:choose>
			</div>
		</c:if>
		<%-- <c:if test="${product.stock.stockLevel gt 0}">
			<c:set var="productStockLevelHtml">${fn:escapeXml(product.stock.stockLevel)}&nbsp;
				<spring:theme code="product.variants.in.stock"/>
			</c:set>
		</c:if>
		<c:if test="${product.stock.stockLevelStatus.code eq 'lowStock'}">
			<c:set var="productStockLevelHtml">
				<spring:theme code="product.variants.only.left" arguments="${product.stock.stockLevel}"/>
			</c:set>
		</c:if> 
		<c:if test="${isForceInStock}">
			<c:set var="productStockLevelHtml">
				<spring:theme code="product.variants.available"/>
			</c:set>
		</c:if>
		 <div class="stock-wrapper clearfix">
			${productStockLevelHtml}
		</div>  --%>
		<c:if test="${not empty product.baseProduct or empty product.variantMatrix}">
		 <div class="actions">
<%--         <c:if test="${multiDimensionalProduct}" > --%>
<%--                 <c:url value="${product.url}/orderForm" var="productOrderFormUrl"/> --%>
<%--                 <a href="${productOrderFormUrl}" class="btn btn-default btn-block btn-icon js-add-to-cart glyphicon-list-alt"> --%>
<%--                     <spring:theme code="order.form" /> --%>
<!--                 </a> -->
<%--         </c:if> --%>
        <action:actions element="div"  parentComponent="${component}"/>
    </div>
    </c:if>
</div>
<c:if test="${fn:contains(buttonType, 'button') 
	and (not empty product.baseProduct or empty product.variantMatrix)}">
	<div>${component.outOfStockMessage}</div>
</c:if>
