<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="company" tagdir="/WEB-INF/tags/addons/gallaghercommerceorgaddon/responsive/company" %>
<%@ taglib prefix="org-common" tagdir="/WEB-INF/tags/addons/gallaghercommerceorgaddon/responsive/common"%>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/responsive/account"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-company/organization-management/manage-usergroups/create/" var="formUrl" htmlEscape="false"/>
<spring:url value="/my-company/organization-management/manage-usergroups/" var="cancelUrl" htmlEscape="false"/>

<template:page pageTitle="${pageTitle}">
	<div class="account-section content-inner">
		<div class="row">
			<div class="col-md-2 col-md-offset-1 left-nav-menu">
				<account:accountLeftNavigation />
			</div>
			<div class="col-sm-12 col-md-8 right-nav-content">
				<div>
					<org-common:headline url="${cancelUrl}"
						labelKey="text.company.manageUsergroups.createUserGroup.title" />
				</div>

				<company:b2bUserGroupForm formUrl="${formUrl}"
					b2BUserGroupForm="${b2BUserGroupForm}" cancelUrl="${cancelUrl}" />
			</div>
		</div>
	</div>
</template:page>
