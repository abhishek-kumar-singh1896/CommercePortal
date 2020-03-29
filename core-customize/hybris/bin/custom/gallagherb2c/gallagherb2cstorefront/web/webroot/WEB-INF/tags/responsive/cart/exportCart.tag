<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/cart/export" var="exportUrl" htmlEscape="false"/>
<div class="col-sm-6">
	<a href="${exportUrl}" class="export__cart--link">
		<spring:theme code="basket.export.csv.file" />
	</a>
</div>