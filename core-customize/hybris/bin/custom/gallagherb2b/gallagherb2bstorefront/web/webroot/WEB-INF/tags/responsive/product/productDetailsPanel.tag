<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isUserAnonymous" value="${sessionScope.jalosession}" />

<div class="product-details page-title">
	<ycommerce:testId
		code="productDetails_productNamePrice_label_${product.code}">
		<div class="name">${fn:escapeXml(product.name)}<span class="sku">ID</span><span
				class="code">${fn:escapeXml(product.code)}</span>
		</div>
	</ycommerce:testId>
	<br>
	<%-- <product:productReviewSummary product="${product}" showLinks="true"/> --%>
</div>
<div class="row">
	<div class="col-xs-10 col-xs-push-1 col-sm-6 col-sm-push-0 col-lg-4">
		<product:productImagePanel galleryImages="${galleryImages}" />
	</div>
	<div class="clearfix hidden-sm hidden-md hidden-lg"></div>
	<div class="col-sm-6 col-lg-8">
		<div class="product-main-info">
			<div class="row">
				<div class="col-lg-6">
					<div class="product-details">
						<%-- <product:productPromotionSection product="${product}"/> --%>
						<c:choose>
							<c:when test="${not priceOnApplication}">
								<c:if test="${not fn:contains(isUserAnonymous, 'anonymous')}">
									<ycommerce:testId
										code="productDetails_productNamePrice_label_${product.code}">
										<product:productPricePanel product="${product}" />
									</ycommerce:testId>
								</c:if>
							</c:when>
							<c:otherwise>
								<cms:pageSlot position="POA" var="component">
									<cms:component component="${component}" />
								</cms:pageSlot>
							</c:otherwise>
						</c:choose>

						<!--	<div class="description">${ycommerce:sanitizeHTML(product.summary)}</div>   -->

						<div class="short-description">${product.description}</div>

						<div class="partNumber">SKU: ${product.partNumber}</div>
					</div>
				</div>

				<div class="col-sm-12 col-md-9 col-lg-6">
					<cms:pageSlot position="VariantSelector" var="component"
						element="div" class="page-details-variants-select">
						<cms:component component="${component}" element="div"
							class="yComponentWrapper page-details-variants-select-component" />
					</cms:pageSlot>
					<c:if
						test="${not fn:contains(isUserAnonymous, 'anonymous') && not priceOnApplication}">
						<cms:pageSlot position="AddToCart" var="component" element="div"
							class="page-details-variants-select">
							<cms:component component="${component}" element="div"
								class="yComponentWrapper page-details-add-to-cart-component" />
						</cms:pageSlot>
					</c:if>
					<cms:pageSlot position="ProductCompareSection" var="component"
						element="div">
						<cms:component component="${component}" class="yComponentWrapper" />
					</cms:pageSlot>
				</div>
			</div>
		</div>

	</div>
	<%-- <div class="product-detail-download-btn">
			<button type="button" class="btn btn-primary btn-outline-secondary product-detail-print-btn">Download</button>
			<input id="downloadPDF" name="downloadPDPPDF" type="hidden" value="${product.code}"/>
	</div> --%>
</div>
<div id="print-product-new" class="print-product-new" style="display:none;" ></div>