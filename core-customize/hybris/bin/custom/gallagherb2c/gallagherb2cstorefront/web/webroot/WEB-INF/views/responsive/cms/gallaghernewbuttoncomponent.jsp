<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:url var="searchDealerUrl" value="${component.url}"></c:url>


<button type="submit" class="btn btn-highlight px-5"
	onclick="window.location.href = '${searchDealerUrl}'">
	${component.linkName}
	<%-- <spring:theme code="text.pdp.dealer.search" text="Search" /> --%>

</button>