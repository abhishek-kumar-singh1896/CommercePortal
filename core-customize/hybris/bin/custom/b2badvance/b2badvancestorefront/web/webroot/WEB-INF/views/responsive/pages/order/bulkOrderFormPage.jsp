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

<template:page pageTitle="${pageTitle}">
	
	<div id="globalMessages">
		<common:globalMessages />
	</div>
	<div class="container">
		<cms:component uid="BulkOrderFormPageComponent" />
	</div>
</template:page>