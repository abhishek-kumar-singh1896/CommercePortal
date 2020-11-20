<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/responsive/account"%>

<template:page pageTitle="${pageTitle}">

	<div id="globalMessages">
		<common:globalMessages />
	</div>
	<div class="content-inner account-section">
		<div class="row">
			<div class="col-md-2 col-md-offset-1 left-nav-menu">
				<account:accountLeftNavigation />
			</div>
			<div class="col-sm-12 col-md-8 right-nav-content">
				<div class="container">
					<div class="bulkOrderNavDiv">
						<cms:component uid="HomePageBulkOrderComponent" />
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>