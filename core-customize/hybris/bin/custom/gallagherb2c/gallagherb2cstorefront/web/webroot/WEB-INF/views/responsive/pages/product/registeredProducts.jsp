<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">

	<template:page pageTitle="${pageTitle}">
	
		<div class="registered-products-out">
	
			<div class="container">
	
				<div class="row">
					<div class="col-12">
						<div class="breadcrumb-out">
							<nav aria-label="breadcrumb">
								<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
							</nav>
						</div>
					</div>
				</div>
	
				<h1 class="primary-title">
					<spring:theme code="registeredProducts.header" />
				</h1>
	
				<div class="registered-product-listing">
					<ul>
						<c:forEach items="${registeredProducts}" var="product"
							varStatus="status">
							<product:registeredProducts product="${product}" />
						</c:forEach>
					</ul>
				</div>
	
			</div>
		</div>
	
	</template:page>

</sec:authorize>