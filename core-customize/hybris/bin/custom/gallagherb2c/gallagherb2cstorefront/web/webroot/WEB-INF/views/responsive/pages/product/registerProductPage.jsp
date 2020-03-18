<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>

<template:page pageTitle="${pageTitle}">
	<product:registerProduct/>
	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class=""/>
	</cms:pageSlot>
	<cms:pageSlot position="Section2" var="feature" element="div" >
		<cms:component component="${feature}"/>
	</cms:pageSlot>
</template:page>
