<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="org-common" tagdir="/WEB-INF/tags/addons/gallaghercommerceorgaddon/responsive/common" %>
<%@ taglib prefix="company" tagdir="/WEB-INF/tags/addons/gallaghercommerceorgaddon/responsive/company" %>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/responsive/account"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:if test="${empty saveUrl}">
	<spring:url value="/my-company/organization-management/manage-costcenters/{/actionUrl}" var="saveUrl" htmlEscape="false">
		<spring:param name="actionUrl" value="${actionUrl}"/>
	</spring:url>
</c:if>

<c:if test="${empty cancelUrl}">
	<c:choose>
		<c:when test="${edit eq true }">
			<spring:url value="/my-company/organization-management/manage-costcenters/view"	var="cancelUrl" htmlEscape="false">
				<spring:param name="costCenterCode" value="${b2BCostCenterForm.originalCode}"/>
			</spring:url>		
		</c:when>
		<c:otherwise>
			<c:url value="/my-company/organization-management/manage-costcenters" var="cancelUrl"/>	
		</c:otherwise>
	</c:choose>
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
							<c:choose>
								<c:when test="${edit eq true }">
									<org-common:headline url="${cancelUrl}"
										labelKey="text.company.costCenter.edit.label" />
								</c:when>
								<c:otherwise>
									<org-common:headline url="${cancelUrl}"
										labelKey="text.company.costCenter.add.new.label" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
				<div class="row">
				    <div class="container-lg col-md-12">
				        <div class="account-section-content">
				            <div class="account-section-form">
								<company:b2bCostCenterForm cancelUrl="${cancelUrl}"
									saveUrl="${saveUrl}" b2BCostCenterForm="${b2BCostCenterForm}" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>
