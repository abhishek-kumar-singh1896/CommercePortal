<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<section id="${fn:replace(title,' ', '')}" class="common-sub-tab-section">
    <div class="use-with-section-out">
        <div class="container">
            <h4 class="small-section-title">${fn:escapeXml(title)}</h4>
            <c:if test="${not empty product.productReferences}">
            <div class="use-with-listing">
                <ul class="clearfix">
				<c:forEach items="${product.productReferences}" var="reference">
					<c:if test="${reference.referenceType eq 'ACCESSORIES'}">
                    <li>
                        <div class="common-product-small-tile">
                            <div class="row">
                                <div class="col-lg-4 col-md-5">
                                    <div class="tile-image">
                                        <!-- <img src="img/fence-product-img.png" alt="product img"> -->
                                        <c:forEach items="${reference.target.images}" var="medias">
                                        <c:if test="${medias.format eq 'thumbnail'}">
                                        <img src="${medias.url}" alt="${medias.altText}">
                                        </c:if>
                                        </c:forEach>
										<%-- <product:productPrimaryImage product="${reference.target}" format="product" /> --%>
                                    </div>
                                </div>
                                <div class="col-lg-8 col-md-7">
                                    <div class="tile-title">${reference.target.name}</div>

                                    <div>
                                        <span class="currency-text">RRP</span>
                                        <span class="currency-value">
											<product:productListerItemPrice product="${reference.target}" />
                                        <!-- $123.00 --></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                    </c:if>
                  </c:forEach>
                </ul>
            </div>
		</c:if>
        </div>
    </div>
</section>