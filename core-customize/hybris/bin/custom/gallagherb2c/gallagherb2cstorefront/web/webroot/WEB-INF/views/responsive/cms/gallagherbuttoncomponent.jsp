<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="right-section-content">
    ${component.buttonHeading}
</div>

<c:url var="PLPButton" value="${component.button.url}"></c:url>
<button type="button" class="btn btn-highlight btn-block" <c:choose><c:when test="${component.button.target eq 'SAMEWINDOW'}">onclick="window.location.href='${PLPButton}'"</c:when>
<c:otherwise>onclick="window.open('${PLPButton}')"</c:otherwise></c:choose>>
	${component.button.linkName}
</button>
