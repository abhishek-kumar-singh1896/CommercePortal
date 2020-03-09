<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 <div class="container">
     <div class="row">
         <div class="col-12">
             <div class="breadcrumb-out">
                 <c:if test="${fn:length(breadcrumbs) > 0}">
					<nav aria-label="breadcrumb">
						<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
					</nav>
				</c:if>
             </div>
         </div>
     </div>
</div>
<div class="product-details-container-out">
<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
<div class="container">
            <div class="row">
                <div class="col-md-6 order-last order-md-first">
                    <div class="product-detail-left-section">
                        <div class="rating-out with-rating-value">
                            <span class="star-rating">
                                <span style="width: ${fn:escapeXml(product.averageRating)*20}%;">
                                </span>
                            </span>
							<c:if test="${not empty product.averageRating}">
	                            <span class="rating-value">
	                            (<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${fn:escapeXml(product.averageRating)}"/>)
	                               <%--  (${fn:escapeXml(product.averageRating)}) --%>
	                            </span>
                            </c:if>
                        </div>
                        <h1 class="product-detail-title">${fn:escapeXml(product.name)}</h1>
                        <div class="product-code">${fn:escapeXml(product.code)}</div>
                        <div class="product-detail-desc-out">
                            <p>${ycommerce:sanitizeHTML(product.description)}</p>
                        </div>

                        <div class="d-flex">
                            <div class="price-section">
                                <div class="price-text">
                                    RRP
                                </div>
                                <div class="price-value">
                                	<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
										<product:productPricePanel product="${product}" />
									</ycommerce:testId>
                                </div>
                            </div>
                            <div class="cart-section">
                                <div class="input-group">
                                    <div class="input-group-append">
                                         <cms:pageSlot position="AddToCart" var="component" element="div" class="page-details-variants-select">
											<cms:component component="${component}" element="div" class="yComponentWrapper page-details-add-to-cart-component"/>
										</cms:pageSlot>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <cms:pageSlot position="VariantSelector" var="component" element="div" class="page-details-variants-select">
							<cms:component component="${component}" element="div" class="yComponentWrapper page-details-variants-select-component"/>
						</cms:pageSlot>
                    </div>
                </div>
                <div class="col-md-6 order-first order-md-last">
						<product:productImagePanel galleryImages="${galleryImages}" />
                </div>
            </div>
</div>
</ycommerce:testId>
<c:if test="${not empty product.logo}">
<div class="quick-reference-feature">
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <div class="row">
                <c:forEach var="media" items="${product.logo}">
                    <div class="col-4 text-center">
                        <div class="quick-reference-icon">
                            <img src="${media.url}" alt="${media.altText}">
                        </div>
                        <div class="quick-reference-description">
                            ${media.description}
                        </div>
                    </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
</c:if>
</div>
<%-- <div class="product-details page-title">
	<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
		<div class="name">${fn:escapeXml(product.name)}<span class="sku">ID</span><span class="code">${fn:escapeXml(product.code)}</span></div>
	</ycommerce:testId>
	<product:productReviewSummary product="${product}" showLinks="true"/>
</div> --%>
<%-- <div class="row">
	<div class="col-xs-10 col-xs-push-1 col-sm-6 col-sm-push-0 col-lg-4">
		<product:productImagePanel galleryImages="${galleryImages}" />
	</div>
	<div class="clearfix hidden-sm hidden-md hidden-lg"></div>
	<div class="col-sm-6 col-lg-8">
		<div class="product-main-info">
			<div class="row">
				<div class="col-lg-6">
					<div class="product-details">
						<product:productPromotionSection product="${product}"/>
						<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
							<product:productPricePanel product="${product}" />
						</ycommerce:testId>
						<div class="description">${ycommerce:sanitizeHTML(product.summary)}</div>
					</div>
				</div>

				<div class="col-sm-12 col-md-9 col-lg-6">
					<cms:pageSlot position="VariantSelector" var="component" element="div" class="page-details-variants-select">
						<cms:component component="${component}" element="div" class="yComponentWrapper page-details-variants-select-component"/>
					</cms:pageSlot>
					<cms:pageSlot position="AddToCart" var="component" element="div" class="page-details-variants-select">
						<cms:component component="${component}" element="div" class="yComponentWrapper page-details-add-to-cart-component"/>
					</cms:pageSlot>
				</div>
			</div>
		</div>

	</div>
</div> --%>