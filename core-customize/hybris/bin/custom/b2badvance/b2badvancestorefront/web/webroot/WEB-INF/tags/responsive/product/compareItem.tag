<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:url value="${product.url}" var="productUrl"/>
<div class="comp-item">
		<a class="thumb" href="${productUrl}" title="${product.name}">
			<product:productPrimaryImage product="${product}" format="product"/>
		</a>
		<div class="prod-desc">
				<div class="details">
					<div class="name">
						<ycommerce:testId code="product_productName">
							<a class="name" href="${productUrl}">
								<c:choose>
									<c:when test="${fn:length(product.name) > 30}">
										${fn:substring(product.name, 0, 30)}...	
									</c:when>
									<c:otherwise>
										${product.name}
									</c:otherwise>	
								</c:choose>
							</a>
						</ycommerce:testId>
					</div>
					<div class="partNumber">
							${product.partNumber}
					</div>
					<c:if test="${not empty product.potentialPromotions}">
						<div class="promo">
							<c:forEach items="${product.potentialPromotions}" var="promotion">
								${promotion.description}
							</c:forEach>
						</div>
					</c:if>
					<ycommerce:testId code="product_productPrice">
						<div class="price"><product:productListerItemPrice product="${product}"/></div>
					</ycommerce:testId>
				</div>
				<button type="button" class="btn-default btn-block" onclick="ACC.compare.updateCompare(${product.code}, 'remove')" ><spring:theme code="productcomparison.button.clear_item" text="Remove"/></button>
		</div>
</div>