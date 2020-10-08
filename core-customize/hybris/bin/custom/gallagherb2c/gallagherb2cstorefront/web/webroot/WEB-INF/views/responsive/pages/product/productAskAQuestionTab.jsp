<%@ page trimDirectiveWhitespaces="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:forEach var="action" items="${actions}">
	<c:set var="productName" value="${fn:escapeXml(product.name)}"/>
	<c:if test="${fn:contains(productName, '&')}">
		<c:set var="productName" value="${fn:replace(productName,'&','')}"/>
	</c:if>
	<li class="nav-item"><a
		class="nav-link d-flex align-content-center flex-wrap"
		href="${action.url}?product=${productName}"> <span>${title}</span>
	</a>
	<li class="nav-item">
</c:forEach>