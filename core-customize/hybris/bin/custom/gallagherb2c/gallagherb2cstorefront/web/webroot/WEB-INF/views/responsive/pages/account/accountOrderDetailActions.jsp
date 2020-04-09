<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/orders" var="orderHistoryUrl" htmlEscape="false"/>

<div class="row mb-5">
    <div class="col-12 col-md-5 col-lg-4 pull-right">
        <ycommerce:testId code="orderDetails_backToOrderHistory_button">
            <div class="orderBackBtn">
                <button type="button" class="btn btn-default btn-block" data-back-to-orders="${fn:escapeXml(orderHistoryUrl)}">
                    <spring:theme code="text.account.orderDetails.backToOrderHistory"/>
                </button>
            </div>
        </ycommerce:testId>
    </div>
</div>
