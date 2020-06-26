<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<spring:htmlEscape defaultHtmlEscape="true" />



<spring:url value="/cart/miniCart/{/totalDisplay}"
	var="refreshMiniCartUrl" htmlEscape="false">
	<spring:param name="totalDisplay" value="${totalDisplay}" />
</spring:url>
<spring:url value="/cart/rollover/{/componentUid}"
	var="rolloverPopupUrl" htmlEscape="false">
	<spring:param name="componentUid" value="${component.uid}" />
</spring:url>
<c:url value="/cart" var="cartUrl" />



<button id="btnGroupDrop1" type="button"
	class="btn right-btn js-mini-cart-link"
	data-mini-cart-url="${fn:escapeXml(rolloverPopupUrl)}"
	data-mini-cart-refresh-url="${fn:escapeXml(refreshMiniCartUrl)}"
	data-mini-cart-name="<spring:theme code="text.cart"/>"
	data-mini-cart-empty-name="<spring:theme code="popup.cart.empty"/>"
	data-mini-cart-items-text="<spring:theme code="basket.items"/>"
	aria-haspopup="true" aria-expanded="false">
	<div class="nav-cart">
		<span class="mini-cart-link">
			<svg class="cart-icon">
            <use
					xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cart" />
        </svg>
        <c:if test="${totalItems eq 0}">
        <c:set var="hiddencomponent" value="hidden" />
        </c:if>
         <span class="info-number" ${hiddencomponent}>
        	<c:if test="${totalItems gt 0}">
				 <span class="info-text">
						${totalItems lt 10 ? fn:escapeXml(totalItems) : "9+"} </span>
				
			</c:if>
			</span>
		</span>
	</div>
	<div class="mini-cart-container js-mini-cart-container"></div>
</button>