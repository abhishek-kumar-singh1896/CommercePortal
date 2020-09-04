<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<template:styleSheets />
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="summary-overview">
	<div class="summary-content">
		<div class="container">
			<div class="date-time text-right">${todayDate}</div>
			<div class="product-title">
				<h1 class="title">${product.name}</h1>
				<div class="art-no">${product.code}</div>
			</div>
			<div class="product-desc">
				<div class="row">
					<div class="col-xs-6">
						<div class="thumbnails">
						<c:forEach items="${galleryImages}" var="container" varStatus="status">
							<c:if test="${status.index <2}">
								<figure>
									<img src="${container.productb2b.url}">
								</figure>
							</c:if>
                		</c:forEach>
						</div>
					</div>
					<div class="col-xs-6">
						<p>${product.description}</p>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 summary">
						<div class="product-summary">
							${ycommerce:sanitizeHTML(product.summary)}
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 summary">
						<div class="product-classifications">
							<c:if test="${not empty product.classifications}">
								<c:forEach items="${product.classifications}"
									var="classification">
									<div class="headline">${fn:escapeXml(classification.name)}</div>
									<table class="table">
										<tbody>
											<c:forEach items="${classification.features}" var="feature">
												<tr>
													<td class="attrib">${fn:escapeXml(feature.name)}</td>
													<td><c:forEach items="${feature.featureValues}"
															var="value" varStatus="status">
										${fn:escapeXml(value.value)}
										<c:choose>
																<c:when test="${feature.range}">
												&nbsp; ${not status.last ? '-' : fn:escapeXml(feature.featureUnit.name)}
											</c:when>
																<c:otherwise>
												&nbsp; ${fn:escapeXml(feature.featureUnit.name)}
												${not status.last ? '<br/>' : ''}
											</c:otherwise>
															</c:choose>
														</c:forEach></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</c:forEach>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
 	<img id="PDFFooterDetail" src="${siteRootUrl}/theme-securityB2B/images/PDFFooterDetail.png" style="display:none;"/></img>
	<img id="PDFFooter" src="${siteRootUrl}/theme-securityB2B/images/PDFFooter.png" style="display:none;"/></img> 
</div>  
