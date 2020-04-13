<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>

<c:if test="${not empty compareProducts}">
<section id="${fn:escapeXml(title)}" class="common-sub-tab-section">
    <div class="compare-section-out">
        <div class="container">
            <h4 class="small-section-title">Compare</h4>

            <div class="compare-inner-container">
                <div class="d-flex align-items-stretch">
                    <div class="left-column">
                        <ul>
                            <li class="row-height-padding">Model</li>
                            <c:forEach items="${compareProducts}" var="productReference">
                            <li class="row-height-padding d-flex align-items-center">
                                <div class="two-line-ellipses">
                                    ${productReference}
                                </div>
                            </li>
                            </c:forEach>
                            <li class="row-height-padding d-flex align-items-center">
                                <div class="two-line-ellipses">
                                    RRP
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="right-column-with-carousel">
                        <div class="flexslider compare-slider" id="compareSlider">
                            <ul class="slides">
                                <li class="selected">
                                    <div class="selcted-text">Selected</div>
                                    <c:if test = "${not empty product.promoSticker}">
                                     	<div class="product-status">
											<img src="${commonResourcePath}/images/${fn:toLowerCase(product.promoSticker)}.svg" />
										</div>
								    </c:if>
                                    <c:set var="count" value="1"/>
                                    <%-- <c:url value="${firstProduct.productData.url}" var="productUrl" /> --%>
                                    <div class="compare-list-top-section">
                                        <div class="compare-list-top-img">
	                                       	<c:forEach items="${firstProduct.productData.images}" var="medias">
		                                        <c:if test="${medias.format eq 'thumbnail'}">
		                                        <c:if test="${count lt 2}">
		                                        <%-- <a href="${fn:escapeXml(productUrl)}" title="${fn:escapeXml(firstProduct.productData.name)}"> --%>
		                                        	<img src="${medias.url}" alt="${medias.altText}">
		                                        	<c:set var="count" value="${count+1}"/>
		                                        <!-- </a> -->
	                                        	</c:if>
		                                        </c:if>
	                                        </c:forEach>
                                        </div>
                                        <div class="compare-list-product-title text-truncate">
                                            ${firstProduct.productData.name}
                                        </div>

                                        <div class="compare-list-product-id text-truncate">
                                            ${firstProduct.productData.code}
                                        </div>

                                    </div>
                                    
                                    <c:forEach var="entry" items="${firstProduct.productAttrValueMap}">
						                <%-- <tr><td><c:out value="${entry.key}"/></td> <td><c:out value="${entry.value}"/> </td></tr> --%>
						                <div class="stored-jules-col row-height-padding">
	                                        ${entry.value}
	                                    </div>
						            </c:forEach>
                                    <div class="price-col row-height-padding">
                                    	<product:productPricePanel product="${firstProduct.productData}" />
                                    </div>
                                </li>
								<c:forEach items="${comparisonProductList}" var="compareProduct">
                                <li>
                                    <c:if test = "${not empty compareProduct.productData.promoSticker}">
                                     	<div class="product-status">
											<img src="${commonResourcePath}/images/${fn:toLowerCase(product.promoSticker)}.svg" />
										</div>
								    </c:if>
								    <c:set var="count" value="1"/>
								    <%-- <c:url value="${compareProduct.productData.url}" var="productUrl" /> --%>
                                    <div class="compare-list-top-section">
                                        <div class="compare-list-top-img">
                                        <c:choose>
                                        <c:when test="${not empty compareProduct.productData.images}">
                                        	<c:forEach items="${compareProduct.productData.images}" var="medias">
		                                        <c:if test="${medias.format eq 'thumbnail'}">
		                                        <c:if test="${count lt 2}">
		                                        <%-- <a href="${fn:escapeXml(productUrl)}" title="${fn:escapeXml(compareProduct.productData.name)}"> --%>
		                                        	<img src="${medias.url}" alt="${medias.altText}">
		                                        	<c:set var="count" value="${count+1}"/>
		                                        <!-- </a> -->
	                                        	</c:if>
		                                        </c:if>
	                                        </c:forEach>
                                        </c:when>
                                        <c:otherwise>
											<theme:image code="img.missingProductImage.responsive.thumbnail" alt="check" title="check"/>
										</c:otherwise>
                                        </c:choose>
                                        </div>
                                        <div class="compare-list-product-title text-truncate">
                                            ${compareProduct.productData.name}
                                        </div>

                                        <div class="compare-list-product-id text-truncate">
                                            ${compareProduct.productData.code}
                                        </div>

                                    </div>

                                    <c:forEach var="entry" items="${compareProduct.productAttrValueMap}">
						                <div class="stored-jules-col row-height-padding">
	                                        ${entry.value}
	                                    </div>
						            </c:forEach>
                                    <div class="price-col row-height-padding">
                                    	<product:productPricePanel product="${compareProduct.productData}" />
                                    </div>
                                </li>
								</c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</c:if>