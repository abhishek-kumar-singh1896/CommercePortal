<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="right-section-content">
    ${component.buttonHeading}
</div>

<c:url var="PLPButton" value="${component.button.url}"></c:url>
<button type="button" class="btn btn-highlight btn-block" onclick="window.location.href='${PLPButton}'">
	${component.button.linkName}
</button>
