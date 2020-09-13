<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="company" tagdir="/WEB-INF/tags/addons/gallaghercommerceorgaddon/responsive/company" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/responsive/account"%>

<spring:htmlEscape defaultHtmlEscape="true"/>

<c:if test="${empty cancelUrl}">
    <spring:url value="/my-company/organization-management/manage-permissions/view" var="cancelUrl" htmlEscape="false">
        <spring:param name="permissionCode" value="${b2BPermissionForm.originalCode}"/>
    </spring:url>
</c:if>
<c:if test="${empty saveUrl}">
    <spring:url value="/my-company/organization-management/manage-permissions/edit" var="saveUrl" htmlEscape="false">
        <spring:param name="permissionCode" value="${b2BPermissionForm.originalCode}"/>
    </spring:url>
</c:if>
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
							<a href="${fn:escapeXml(cancelUrl)}"> <span
								class="glyphicon glyphicon-chevron-left"></span>
							</a><spring:theme
									code="text.company.managePermissions.edit.page.title" />
						</div>
					</div>
				</div>
				<div class="row">
				    <div class="container-lg col-md-12">
				        <div class="account-section-content">
				            <div class="account-section-form">
								<company:b2bPermissionForm cancelUrl="${cancelUrl}"
									saveUrl="${saveUrl}" b2BPermissionForm="${b2BPermissionForm}" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>
