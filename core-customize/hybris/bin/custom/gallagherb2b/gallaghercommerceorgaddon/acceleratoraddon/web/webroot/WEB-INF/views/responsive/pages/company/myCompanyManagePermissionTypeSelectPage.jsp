<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/responsive/account"%>

<spring:htmlEscape defaultHtmlEscape="true"/>

<spring:url value="/my-company/organization-management/manage-permissions" var="cancelUrl" htmlEscape="false"/>

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
									code="text.company.managePermissions.create.permission.title" />
						</div>
					</div>
				</div>
				<div class="row">
				    <div class="container-lg col-md-12">
				        <div class="account-section-content">
				            <div class="account-section-form">
									<div id="addNewPermissionForm">
										<div class="row">
											<div class="col-xs-12 col-sm-8 col-md-6">
												<div class="account-select-form">
													<label> <spring:theme
															code="text.company.managePermissions.create.type.label" />
													</label> <select id="selectNewPermissionType"
														name="selectNewPermissionType" class="form-control">
														<option selected="selected"><spring:theme
																code="text.company.managePermissions.selectBox.permissionType" /></option>
														<c:forEach items="${b2bPermissionTypes}" var="b2BPermission">
															<option value="${fn:escapeXml(b2BPermission.code)}">${fn:escapeXml(b2BPermission.name)}</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
			</div>
		</div>
	</div>
</template:page>