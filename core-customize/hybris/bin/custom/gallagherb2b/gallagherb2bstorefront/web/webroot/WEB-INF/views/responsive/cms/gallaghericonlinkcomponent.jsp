<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<li>
	<a href="${component.url}" <c:if test="${component.target eq 'NEWWINDOW'}">target="_blank"</c:if>>
		<cms:component	component="${component.icon}" /> 
		${component.linkName}
	</a>
</li>
