<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="galleryImages" required="true" type="java.util.List" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

<spring:htmlEscape defaultHtmlEscape="true" />

   <c:choose>
        <c:when test="${galleryImages == null || galleryImages.size() == 0}">
            <div class="carousel image-gallery__image js-gallery-image">
                <div class="item">
                    <div>
                        <spring:theme code="img.missingProductImage.responsive.product" var="imagePath" htmlEscape="false"/>
                        <c:choose>
                            <c:when test="${originalContextPath ne null}">
								<c:choose>
									<c:when test='${fn:startsWith(imagePath, originalContextPath)}'>	
										<c:url value="${imagePath}" var="imageUrl" context="/"/>
									</c:when>
									<c:otherwise>
										<c:url value="${imagePath}" var="imageUrl" context="${originalContextPath}"/>
									</c:otherwise>
								</c:choose>
                            </c:when>
                            <c:otherwise>
                                <c:url value="${imagePath}" var="imageUrl" />
                            </c:otherwise>
                        </c:choose>
                        <img class="lazyOwl" data-src="${fn:escapeXml(imageUrl)}"/>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
        <!-- Product detail slider and carousel -->
				<div class="product-detail-right-section">
				    <div id="productDetailMainSlider" class="flexslider slider-big-image">
				        <ul class="slides">
				        <c:forEach items="${galleryImages}" var="container" varStatus="varStatus">
				        	<c:if test="${not empty fn:escapeXml(container.productb2c.url)}">
		                    <li>
		                    <div class="product-status"> 
		                    <c:if test = "${not empty product.promoSticker and fn:containsIgnoreCase(product.promoSticker, 'NEW')}">
								<img src="${commonResourcePath}/images/${fn:toLowerCase(product.promoSticker)}.svg" />
							</c:if>
		                    </div>
	                            <img src="${fn:escapeXml(container.productb2c.url)}"
	                                 data-zoom-image="${fn:escapeXml(container.superZoom.url)}"
	                                 alt="${fn:escapeXml(container.thumbnailb2c.altText)}" >
		                    </li>
		                    </c:if>
		                </c:forEach>
				        </ul>
				    </div>
				    <div id="productDetailMainCarousel" class="flexslider flex-carousel">
				        <ul class="slides">
				        	<c:forEach items="${galleryImages}" var="container" varStatus="varStatus">
				        	<li>
						        <a href="#" class="item"> <img class="carousel-thumb-image" src="${fn:escapeXml(container.thumbnailb2c.url)}" alt="${fn:escapeXml(container.thumbnailb2c.altText)}"></a>
						    </li>
						    </c:forEach>
				        </ul>
				    </div>
				</div>
        </c:otherwise>
    </c:choose>
