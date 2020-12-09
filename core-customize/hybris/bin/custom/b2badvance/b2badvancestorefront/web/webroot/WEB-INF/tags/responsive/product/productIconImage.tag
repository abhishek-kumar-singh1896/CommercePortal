<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ attribute name="format" required="true" type="java.lang.String"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>


<c:set var="tempicon" value="0" />
<c:choose>
	<c:when test="${not empty product.images}">
		<c:forEach items="${product.images}" var="medias">
			<c:if test="${tempicon == 0 }">
				<c:if test="${medias.format eq 'thumbnail'}">
					<c:set var="tempicon" value="1" />
					<a href="${fn:escapeXml(productUrl)}"
						title="${fn:escapeXml(product.name)}"> <img
						src="${medias.url}" alt="${medias.altText}">
					</a>
				</c:if>
			</c:if>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:if test="${tempicon == 0 }">
			<c:set value="${fn:escapeXml(product.name)}" var="productNameHtml" />
			<c:set value="cartIcon" var="format" />
			<theme:image code="img.missingProductImage.responsive.${format}"
				alt="${productNameHtml}" title="${productNameHtml}" />
		</c:if>
	</c:otherwise>
</c:choose>
