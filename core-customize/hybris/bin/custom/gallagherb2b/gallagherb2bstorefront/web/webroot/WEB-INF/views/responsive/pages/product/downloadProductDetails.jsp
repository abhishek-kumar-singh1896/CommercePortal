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
				<h1 class="title">${product1.name}</h1>
				<div class="art-no">${product1.code}</div>
			</div>
			<div class="product-desc">
				<div class="row">
					<div class="col-xs-6">
						<div class="thumbnails">
						<c:forEach items="${galleryImages1}" var="container1" varStatus="varStatus1">
							<figure>
								<img src="${container1.productb2b.url}">
							</figure>
                		</c:forEach>
						</div>
					</div>
					<div class="col-xs-6">
						<p>${product1.description}</p>
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
 	<img id="img1" src="${siteRootUrl}/theme-securityB2B/images/PDFFooterDetail.jpg" style="display:none;"/></img>
	<img id="img2" src="${siteRootUrl}/theme-securityB2B/images/PDFFooter.jpg" style="display:none;"/></img> 
<!-- 	<footer class="summary-footer">
        <div class="container">
        <div class="footer-details">
        <div class="row">
    <div class="col-xs-4">
        <h4 class="text-uppercase">Gallagher world headquarters</h4>
        <address>
            Kahikatea Drive, Hamilton 3206<br/>
            Private Bag 3026, Hamilton 3240<br/>
            New Zealand
        </address>
        <p class="contact-details">
        <strong>TEL:</strong> +64 7 838 9800<br/>
        <strong>EMAIL:</strong> security@gallagher.com
        </p>
</div>
<div class="col-xs-4">
<h4 class="text-uppercase">REGIONAL OFFICES</h4>
<ul class="listing">
<li>
<span class="country">New Zealand................................... </span>
<span class="contact-no">+64 7 838 9800</span>
</li>
    <li>
<span class="country">New Zealand................................... </span>
<span class="contact-no">+64 7 838 9800</span>
</li>
    <li>
<span class="country">New Zealand................................... </span>
<span class="contact-no">+64 7 838 9800</span>
</li>
    <li>
<span class="country">New Zealand................................... </span>
<span class="contact-no">+64 7 838 9800</span>
</li>
    <li>
<span class="country">New Zealand................................... </span>
<span class="contact-no">+64 7 838 9800</span>
</li>
     <li>
<span class="country">New Zealand................................... </span>
<span class="contact-no">+64 7 838 9800</span>
</li>
     <li>
<span class="country">New Zealand................................... </span>
<span class="contact-no">+64 7 838 9800</span>
</li>
     <li>
<span class="country">New Zealand................................... </span>
<span class="contact-no">+64 7 838 9800</span>
</li>
</ul>
</div>
<div class="col-xs-4">
<p class="disclaimer">
<Strong>DISCLAIMER:</Strong> This document gives certain information about products and/or services provided by Gallagher Group Limited or its related companies (referred to as “Gallagher Group”). The information is indicative only and is subject to change without notice meaning it may be out of date at any given time. Although every commercially reasonable effort has been taken to ensure the quality and accuracy of the information, Gallagher Group makes no representation as to its accuracy or completeness and it should not be relied on as such. To the extent permitted by law, all express or implied, or other representations or warranties in relation to the information are expressly excluded. Neither Gallagher Group nor any of its directors, employees or other representatives shall be responsible for any loss that you may incur, either directly or indirectly, arising from any use or decisions based on the information provided. Except where stated otherwise, the information is subject to copyright owned by Gallagher Group and you may not sell it without permission. Gallagher Group is the owner of all trademarks reproduced in this information. All trademarks which are not the property of Gallagher Group, are acknowledged. Copyright © Gallagher Group Ltd. All rights reserved.
</p>
</div>
</div></div>
            </div>
        <div class="copyright">
        <div class="container">
        <div class="row">
            <div class="col-sm-6">
            <a class="site" href="#">security.gallagher.com</a>
            </div>  
            <div class="col-sm-6 text-right">
<img title="GALLAGHER SECURITY" alt="GALLAGHER SECURITY" src="./Update Profile _ Products _ Gallagher Security NZ_files/logo-gallagher.svg">
</div>  
        </div>    
        </div>
        </div>
</footer> -->
	
</div>  
