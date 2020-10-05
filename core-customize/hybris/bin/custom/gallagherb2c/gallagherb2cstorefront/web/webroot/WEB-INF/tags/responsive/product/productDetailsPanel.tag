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

                        <div class="d-flex flex-wrap">
                            <div class="price-section">
                                <div class="price-text">
                                <c:if test="${not empty product.price}">
                                    RRP
                                </c:if>
                                </div>
                                <div class="price-value mb-sm-3 mb-md-3 mb-lg-0">
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
            <div class="col-md-12">
                <div class="row">
                <c:set var="count" value="1"/>
                <c:forEach var="media" items="${product.logo}">
                	<c:if test="${count lt 6}">
                    <div class="col-4 col-sm-4 col-md-2 text-center">
                        <div class="quick-reference-icon">
                            <img src="${media.url}" alt="${media.altText}">
                            <c:set var="count" value="${count+1}"/>
                        </div>
                        <div class="quick-reference-description">
                            ${media.description}
                        </div>
                    </div>
                    </c:if>
                </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
</c:if>
</div>