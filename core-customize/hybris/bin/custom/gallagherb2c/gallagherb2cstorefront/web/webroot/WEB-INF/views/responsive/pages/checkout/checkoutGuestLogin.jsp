<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
	<cms:pageSlot position="Section3" var="feature" element="div" >
                    <cms:component component="${feature}"/>
                </cms:pageSlot>
	<sec:authorize access="hasAnyRole('ROLE_ANONYMOUS')">
	<c:url value="/login/checkout/guest" var="guestCheckoutUrl" />
	<user:guestCheckout actionNameKey="checkout.login.guestCheckout" action="${guestCheckoutUrl}"/>
	</sec:authorize>
</template:page>



