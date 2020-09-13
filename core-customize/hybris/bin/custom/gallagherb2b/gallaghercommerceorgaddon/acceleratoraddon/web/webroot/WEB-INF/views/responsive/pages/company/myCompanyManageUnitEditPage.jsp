<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="org-common" tagdir="/WEB-INF/tags/addons/gallaghercommerceorgaddon/responsive/common" %>
<%@ taglib prefix="company" tagdir="/WEB-INF/tags/addons/gallaghercommerceorgaddon/responsive/company" %>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/responsive/account"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-company/organization-management/manage-units/edit" var="editUnitUrl" htmlEscape="false">
	<spring:param name="unit" value="${b2BUnitForm.originalUid}"/>
</spring:url>
<spring:url value="/my-company/organization-management/manage-units/details" var="cancelUrl" htmlEscape="false">
	<spring:param name="unit" value="${b2BUnitForm.originalUid}"/>
</spring:url>

<template:page pageTitle="${pageTitle}">
	<div class="account-section content-inner">
		<div class="row">
			<div class="col-md-2 col-md-offset-1 left-nav-menu">
				<account:accountLeftNavigation />
			</div>
			<div class="col-sm-12 col-md-8 right-nav-content">
				<div class="account-section-header">
				    <div class="row">
				        <div class="container-lg col-md-6">
							<org-common:headline url="${cancelUrl}"
								labelKey="text.company.manage.units.unit.edit.title"
								labelArguments="${empty b2BUnitForm.name ? b2BUnitForm.uid : b2BUnitForm.name}" />
						</div>
					</div>
				</div>
				<div class="row">
				    <div class="container-lg col-md-12">
				        <div class="account-section-content">
				            <div class="account-section-form">
								<company:b2bUnitForm formUrl="${editUnitUrl}"
									b2BUnitForm="${b2BUnitForm}" cancelUrl="${cancelUrl}" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>
