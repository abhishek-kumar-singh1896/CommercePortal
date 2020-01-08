<%@ page trimDirectiveWhitespaces="true" contentType="application/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


{"cartData": {
"total": "${cartData.totalPrice.value}",
"products": [
<c:forEach items="${cartData.entries}" var="cartEntry" varStatus="status">
	{
		"sku":		"${cartEntry.product.code}",
		"name": 	"<c:out value='${cartEntry.product.name}' />",
		"qty": 		"${cartEntry.quantity}",
		"price": 	"${cartEntry.basePrice.value}",
		"categories": [
		<c:forEach items="${cartEntry.product.categories}" var="category" varStatus="categoryStatus">
			"<c:out value='${category.name}' />"<c:if test="${not categoryStatus.last}">,</c:if>
		</c:forEach>
		]
	}<c:if test="${not status.last}">,</c:if>
</c:forEach>
]
},

"quickOrderErrorData": [
<c:forEach items="${quickOrderErrorData}" var="quickOrderEntry" varStatus="status">
	{
		"sku":		"${quickOrderEntry.productData.code}",
		"errorMsg": "<spring:theme code='${quickOrderEntry.errorMsg}' htmlEscape='true' />"
	}<c:if test="${not status.last}">,</c:if>
</c:forEach>
],

"cartAnalyticsData":{"cartCode" : "${cartCode}","productPostPrice":"${entry.basePrice.value}","productName":"<c:out value='${product.name}' />"}
,
"addToCartLayer":"<spring:escapeBody javaScriptEscape="true">
	<jsp:include page="../../../messages/braintreeErrorMessages.jsp" />
	<spring:theme code="text.addToCart" var="addToCartText"/>
	<c:url value="/cart" var="cartUrl"/>
	<c:url value="/cart/checkout" var="checkoutUrl"/>
	<ycommerce:testId code="addToCartPopup">
		<div id="addToCartLayer" class="add-to-cart">
            <div class="cart_popup_error_msg">
                <c:choose>
	                <c:when test="${quickOrderErrorData ne null and not empty quickOrderErrorData}">
	                	<spring:theme code="${quickOrderErrorMsg}" arguments="${fn:length(quickOrderErrorData)}" htmlEscape="true" />
                    </c:when>
                    <c:when test="${multidErrorMsgs ne null and not empty multidErrorMsgs}">
                        <c:forEach items="${multidErrorMsgs}" var="multidErrorMsg">
                            <spring:theme code="${multidErrorMsg}" />
                        </br>
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

            <spring:eval expression="@configurationService.configuration.getProperty('braintree.user.action')" var="userAction"/>

			<script>
    			var clientTokenForPaypal = "${client_token}";
	    		var storeInVault = "${payPalCheckoutData.storeInVault}";
	    		var paypalIntent = "${payPalCheckoutData.intent}";
		    	var amount = "${payPalCheckoutData.amount}";
	    		var locale = "${payPalCheckoutData.locale}";
				var enableShippingAddress = "${payPalCheckoutData.enableShippingAddress}";
	    		var braintreeLocale = "${braintreeLocale}";
	    		var currency = "${payPalCheckoutData.currency}";
		    	var advancedFraudToolsEnabled = "${payPalCheckoutData.advancedFraudTools}";
			    var dbaName = "${payPalCheckoutData.dbaName}";
			    var billingAgreementDescription = "${billingAgreementDescription}";
    		    var userAction="${userAction}";
    		    var applePayEnabled = ${payPalCheckoutData.applePayEnabled};
				var payPalButtonConfig = "${payPalButtonConfig}";
			</script>

           	<sec:authorize access="hasAnyRole('ROLE_ANONYMOUS')">
           		<script>
           			isAnonymous=true;
           		</script>
           	</sec:authorize>

			<div id="wrapper">
				<div id="row">
				<div id="paypal_express_error"></div>
					<div id="paypayShortcut" class="bt_center">
				            <c:choose>
                                <c:when test="${payPalCheckoutData.applePayEnabled}">
                                    <div id="apple-pay-button" class="apple-pay-button-container">
                                        <img src="${contextPath}/_ui/addons/braintreeb2baddon/responsive/common/images/apple-pay.png"
                                        class="apple-pay-button" alt="Buy with Apple Pay" />
                                    </div>
                                </c:when>
                            </c:choose>
						<div id="paypal-button" class="paypal-button-in-popup paypal_button_container"></div>
					</div>
					<div id="orSeparator" class="bt_center"><spring:theme code="braintree.cart.or"/></div>
				</div>
			</div>

			
            <ycommerce:testId code="checkoutLinkInPopup">
                <a href="${cartUrl}" class="btn btn-primary btn-block add-to-cart-button">
                    <spring:theme code="checkout.checkout" />
                </a>
            </ycommerce:testId>


            <a href="" class="btn btn-default btn-block js-mini-cart-close-button">
                <spring:theme code="cart.page.continue"/>
            </a>
            <script>
				configurePaypalMiniCartShortcut(CONST.PAYPAL_BUTTON_CONTAINER);
			</script>
		</div>
	</ycommerce:testId>
</spring:escapeBody>"
}



