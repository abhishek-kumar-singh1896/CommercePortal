<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<div>${todayDate}</div>
<div>${product.code}</div>
<div>${ProductName}</div>
<div>${productDescription}</div>
#############################################
<c:forEach items="${galleryImages}" var="container" varStatus="varStatus">
				        	<c:if test="${not empty fn:escapeXml(container.productb2c.url)}">
		                    <li>
		                    <div class="product-status"> 
		                    <c:if test = "${not empty product.promoSticker}">
		                    test1:  ${commonResourcePath}/images/${fn:toLowerCase(product.promoSticker)}.svg
								<img src="${commonResourcePath}/images/${fn:toLowerCase(product.promoSticker)}.svg" />
							</c:if>
		                    </div>
		                    test2: ${fn:escapeXml(container.superZoom.url)}
	                            <img src="${fn:escapeXml(container.productb2c.url)}"
	                                 data-zoom-image="${fn:escapeXml(container.superZoom.url)}"
	                                 alt="${fn:escapeXml(container.thumbnailb2c.altText)}" >
		                    </li>
		                    </c:if>
		                </c:forEach>
		                
		                
		                >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
<div class="product-classifications">
	<c:if test="${not empty product.classifications}">
		<c:forEach items="${product.classifications}" var="classification">
			<div class="headline">${fn:escapeXml(classification.name)}</div>
				<table class="table">
					<tbody>
						<c:forEach items="${classification.features}" var="feature">
							<tr>
								<td class="attrib">${fn:escapeXml(feature.name)}</td>
								<td>
									<c:forEach items="${feature.featureValues}" var="value" varStatus="status">
										${fn:escapeXml(value.value)}
										<c:choose>
											<c:when test="${feature.range}">
												&nbsp; ${not status.last ? '-' : fn:escapeXml(feature.featureUnit.name)}
											</c:when>
											<c:otherwise>
												&nbsp; ${fn:escapeXml(feature.featureUnit.name)}
												${not status.last ? '<br/>' : ''}
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
		</c:forEach>
	</c:if>
</div>