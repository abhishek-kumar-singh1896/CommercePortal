<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="org-common" tagdir="/WEB-INF/tags/addons/gallaghercommerceorgaddon/responsive/common" %>
<%@ taglib prefix="company" tagdir="/WEB-INF/tags/addons/gallaghercommerceorgaddon/responsive/company"%>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/responsive/account"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:if test="${empty saveUrl}">
	<spring:url value="/my-company/organization-management/manage-budgets" var="cancelUrl" htmlEscape="false"/>
	<spring:url value="/my-company/organization-management/manage-budgets/add" var="saveUrl" htmlEscape="false"/>
</c:if>

<template:page pageTitle="${pageTitle}">
	<div class="account-section content-inner">
		<div class="row">
			<div class="col-md-2 col-md-offset-1 hidden-xs">
				<account:accountLeftNavigation />
			</div>
			<div class="col-md-8">
				<div>
					<org-common:headline url="${cancelUrl}"
						labelKey="text.company.budget.create.title.label" />
				</div>

				<company:b2bBudgetForm cancelUrl="${cancelUrl}" saveUrl="${saveUrl}"
					b2BBudgetForm="${b2BBudgetForm}" />

			</div>
		</div>
	</div>
</template:page>
