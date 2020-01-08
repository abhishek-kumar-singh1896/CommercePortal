<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.AbstractOrderData" %>
<%@ attribute name="brainTreePaymentInfo" required="false"
	type="com.braintree.hybris.data.BrainTreePaymentInfoData"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<spring:htmlEscape defaultHtmlEscape="true"/>
<div class="label-order">
    <spring:theme code="text.account.paymentType"/>
</div>
<div class="value-order">
	<c:choose>
		<c:when test="${brainTreePaymentInfo.paymentType eq 'BrainTreePayPalExpress' or brainTreePaymentInfo.paymentType eq 'PayPalAccount'}">
			<img
					src="https://www.paypalobjects.com/webstatic/en_US/i/buttons/pp-acceptance-small.png"
					alt="PayPal icon" />
			${fn:escapeXml(brainTreePaymentInfo.email)}
		</c:when>
        <c:when test="${brainTreePaymentInfo.paymentType eq 'ApplePayCard'}">
            <img height="28" width="56"
                src="${contextPath}/_ui/addons/braintreeb2baddon/responsive/common/images/logo_apple.png"
                alt="ApplePay icon" />
            ${fn:escapeXml(brainTreePaymentInfo.email)}
         </c:when>
		<c:otherwise>
				${fn:escapeXml(brainTreePaymentInfo.cardType)},
				${fn:escapeXml(brainTreePaymentInfo.cardNumber)}
    	</c:otherwise>
	</c:choose>
</div>

