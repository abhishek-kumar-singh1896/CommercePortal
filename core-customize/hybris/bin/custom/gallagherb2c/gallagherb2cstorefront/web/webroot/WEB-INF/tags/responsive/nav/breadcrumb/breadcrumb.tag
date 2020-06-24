<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="breadcrumbs" required="true" type="java.util.List"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:url value="${sitecoreHomePage}" var="homeUrl" />

<ol class="breadcrumb">
	<li class="breadcrumb-item">
		<a href="${homeUrl}"><spring:theme code="breadcrumb.home" /></a>
	</li>

	<c:forEach items="${breadcrumbs}" var="breadcrumb" varStatus="status">
		<spring:url htmlEscape="false" value="${breadcrumb.url}" var="breadcrumbUrl" />
		<c:choose>
			<c:when test="${status.last}">
				<li class="breadcrumb-item active" aria-current="page">${fn:escapeXml(breadcrumb.name)}</li>
			</c:when>
			<c:when test="${breadcrumb.url eq '#'}">
				<li class="breadcrumb-item">
					<a href="#">${fn:escapeXml(breadcrumb.name)}</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="breadcrumb-item">
					<a href="${fn:escapeXml(breadcrumbUrl)}">${fn:escapeXml(breadcrumb.name)}</a>
				</li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ol>
