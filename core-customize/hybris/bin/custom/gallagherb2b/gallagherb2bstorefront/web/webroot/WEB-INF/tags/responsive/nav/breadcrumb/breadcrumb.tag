<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="breadcrumbs" required="true" type="java.util.List"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:url value="/" var="homeUrl" />

<ol class="breadcrumb">
	<li>
		<a href="${homeUrl}"><spring:theme code="breadcrumb.home" /></a>
	</li>

	<c:forEach items="${breadcrumbs}" var="breadcrumb" varStatus="status">
		<c:set var="displayBreadcrumb" value="${fn:escapeXml(breadcrumb.name)}"/>
   		<c:set var="length" value="${fn:length(displayBreadcrumb)}"/>
    		<c:if test="${fn:startsWith(displayBreadcrumb, 'sec|')}">
    			<c:set var="displayBreadcrumb" value="${fn:substring(displayBreadcrumb, 4, length)}"/>
    		</c:if>
		<spring:url htmlEscape="false" value="${breadcrumb.url}" var="breadcrumbUrl" />
		<c:choose>
			<c:when test="${status.last}">
				<li class="active">${displayBreadcrumb}</li>
			</c:when>
			<c:when test="${breadcrumb.url eq '#'}">
				<li>
					<a href="#">${displayBreadcrumb}</a>
				</li>
			</c:when>
			<c:otherwise>
				<li>
					<a href="${fn:escapeXml(breadcrumbUrl)}">${displayBreadcrumb}</a>
				</li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ol>
