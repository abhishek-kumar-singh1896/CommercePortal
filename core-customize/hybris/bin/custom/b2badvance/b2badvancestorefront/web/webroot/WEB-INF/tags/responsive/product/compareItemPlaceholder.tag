<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="compareProducts" required="true" type="java.util.List"%>

<c:set var="maxNumCompare" value="3"/>
<c:set var="numProducts" value="${fn:length(compareProducts)}"/>
<c:if test="${numProducts lt maxNumCompare}">
	<c:set var="numPlaceholders" value="${maxNumCompare - numProducts}"/>
	<c:set var="beginIndex" value="${numProducts + 1}"/>
	<c:forEach var="current" begin="${beginIndex}" end="${maxNumCompare}">
		<div class="comp-empty-item">
			<p>${current}</p>
		</div>
	</c:forEach>
</c:if>