<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="com.gallagher.facades.product.data.RegisteredProductData"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<li>
	<div class="row">
		<div class="col-md-2 col-lg-2 col-xl-1">
			<c:choose>
				<c:when test="${not empty product.image}">
					<img src="${fn:escapeXml(product.image.url)}"
						class="img-fluid product-img" alt="${fn:escapeXml(product.name)}"
						title="${fn:escapeXml(product.name)}" />
				</c:when>
				<c:otherwise>
					<theme:registeredProductImage
						code="img.missingProductImage.responsive.product"
						alt="${product.name}" title="${product.name}" />
				</c:otherwise>
			</c:choose>
		</div>
		<div class="col-md-10 col-lg-10 col-xl-11">
			<div class="product-name mb-1">${product.name }</div>
			<div class="product-id mb-1">${product.code}</div>
			<div class="mb-1 product-id">
				<span class=""><spring:theme
						code="registeredProducts.dateRegistered" /> </span> 
				<span>
					<fmt:formatDate value="${product.registrationDate}" type="date" pattern="dd/MM/yyyy"/>
				</span>
			</div>

			<div class="product-id mb-1">
				<span>
					<spring:theme code="registeredProducts.datePurchased" />
				</span>
				<span>
					<fmt:formatDate value="${product.purchaseDate}" type="date" pattern="dd/MM/yyyy"/>
				</span>
			</div>

			<div class="product-id">
				<span><spring:theme code="registeredProducts.attachment" />
				</span> <span> <a href="${fn:escapeXml(product.attachmentUrl)}" target="_blank">${product.attachment}</a>
				</span>
			</div>
		</div>
	</div>
</li>