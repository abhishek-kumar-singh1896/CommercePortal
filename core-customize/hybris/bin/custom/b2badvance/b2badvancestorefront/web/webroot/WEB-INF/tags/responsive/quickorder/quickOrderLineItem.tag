<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="quickorder" tagdir="/WEB-INF/tags/responsive/quickorder"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ attribute name="productData" required="false" type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ attribute name="quantity" required="false" type="java.lang.Long"%>
<%@ attribute name="entryNumber" required="false" type="java.lang.Integer"%>
<spring:message code="laminex.page.contactus.link" var="contactUsLink" />

<div class="tr tr-quickOrder">
	<input type="hidden" value="${entryNumber}" class="entrynumber">
	<input type="hidden" class="currentSite" value="${currentSite }" />
	<input type="hidden" value="${currentSite }" class="currentSite" />
	<c:choose>
		<c:when test="${not empty productData}">
			<c:set value="${productData.baseProduct eq null}" var="isBaseProduct" />
			<c:set value="${productData}" var="resultProduct" />
		</c:when>
		<c:otherwise>
			<c:set value="${product.baseProduct eq null}" var="isBaseProduct" />
			<c:set value="${product}" var="resultProduct" />
		</c:otherwise>
	</c:choose>
	<div class="td item">
		<product:productPrimaryImage product="${resultProduct}" format="cartIcon" />
		<div class="js-product-info">
					<div class="prod-name">${isBaseProduct? resultProduct.name : resultProduct.baseProductName} </div>
					<div class="product-code" style="display: none">${resultProduct.code}</div>
					<input type="hidden" value="${resultProduct.baseProduct}" class="baseproduct-code">
					<input type="hidden" value="${resultProduct.stock.stockLevel}" class="available-stock">
					<input type="hidden" value="${resultProduct.stock.stockLevelStatus.code}" class="stock-level-status">
				  	<input type="hidden" value="${resultProduct.price.value}" class="base-price">
					<input type="hidden" value="${resultProduct.price.currencySymbol}" class="currency-symbol">
					<input type="hidden" value="${locale}" class="locale">
	
		</div>
	</div>
	<quickorder:quickorderVariantSelector product="${resultProduct}" />
	   
	 <quickorder:quickorderPricingDiv productData="${resultProduct}" quantity="${quantity}"/>
	

</div>