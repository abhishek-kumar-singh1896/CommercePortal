<%@ page trimDirectiveWhitespaces="true" contentType="application/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="productName" value="${fn:escapeXml(product.name)}" />

{"quickOrderErrorData": [
<c:forEach items="${quickOrderErrorData}" var="quickOrderEntry" varStatus="status">
	<c:set var="productCode" value="${fn:escapeXml(quickOrderEntry.productData.code)}" />
	{
		"sku":		"${ycommerce:encodeJSON(productCode)}",
		"errorMsg": "<spring:theme code='${quickOrderEntry.errorMsg}' htmlEscape="true"/>"
	}<c:if test="${not status.last}">,</c:if>
</c:forEach>
],

"cartAnalyticsData":{"cartCode" : "${ycommerce:encodeJSON(cartCode)}","productPostPrice":"${ycommerce:encodeJSON(entry.basePrice.value)}","productName":"${ycommerce:encodeJSON(productName)}"}
,
"addToCartLayer":"<spring:escapeBody javaScriptEscape="true" htmlEscape="false">
	<spring:htmlEscape defaultHtmlEscape="true">
	<spring:theme code="text.addToCart" var="addToCartText"/>
	<c:url value="/cart" var="cartUrl"/>
	<ycommerce:testId code="addToCartPopup">
		<div id="addToCartLayer" class="add-to-cart">
            <div class="cart_popup_error_msg">
                <c:choose>
	                <c:when test="${quickOrderErrorData ne null and not empty quickOrderErrorData}">
	                	<spring:theme code="${quickOrderErrorMsg}" arguments="${fn:length(quickOrderErrorData)}" />
                    </c:when>
                    <c:when test="${multidErrorMsgs ne null and not empty multidErrorMsgs}">
                        <c:forEach items="${multidErrorMsgs}" var="multidErrorMsg">
                            <spring:theme code="${multidErrorMsg}" />
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <spring:theme code="${errorMsg}" />
                    </c:otherwise>
                </c:choose>
            </div>

            <c:choose>
                <c:when test="${modifications ne null}">
                    <c:forEach items="${modifications}" var="modification">
                        <c:set var="product" value="${modification.entry.product}" />
                        <c:set var="entry" value="${modification.entry}" />
                        <c:set var="quantity" value="${modification.quantityAdded}" />
                        <cart:popupCartItems entry="${entry}" product="${product}" quantity="${quantity}"/>
                    </c:forEach>
                </c:when>
                <c:otherwise>

                    <cart:popupCartItems entry="${entry}" product="${product}" quantity="${quantity}"/>
                </c:otherwise>
            </c:choose>
            
            <input type="hidden" id="entryID" value="${entry.entryNumber}" name="entryNumber" />
            <input type="hidden" id="productSpecificDetailsHeading" value="${product.productSpecificDetailsHeading}" name="productSpecificDetailsHeading" />
            
            <c:if test="${not empty product.productSpecificDetailsHeading}">
            	<div class="shoppingHeading">${product.productSpecificDetailsHeading}</div>
            	<div><strong><p>Please enter details<p></strong></div>
            </c:if>
            
            
            
            <c:if test="${not empty product.productSpecificDetailsHeading}">
            	<textarea name="deliveryInstructionEntry" id="deliveryInstructionEntry" rows="3" cols="30" placeholder="80 character limit" />
            	<div class="help-block textareaErrorbox">
					<span ><spring:theme code="checkout.instruction.length.invalid" /></span>
				</div>
            </c:if>

            <ycommerce:testId code="checkoutLinkInPopup">
                <a href="${cartUrl}" id = "checkoutButton" class="btn btn-primary btn-block add-to-cart-button">
	                <c:choose>
		                <c:when test="${isQuote}">
		                	<spring:theme code="quote.view" />
	                    </c:when>
	                    <c:otherwise>
	                        <spring:theme code="checkout.checkout" />
	                    </c:otherwise>
                	</c:choose>
                </a>
            </ycommerce:testId>

            <a href="" class="btn btn-default btn-block js-mini-cart-close-button">
                <spring:theme code="cart.page.continue"/>
            </a>
		</div>
	</ycommerce:testId>
	</spring:htmlEscape>
</spring:escapeBody>"
}



