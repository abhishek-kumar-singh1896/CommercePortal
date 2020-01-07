<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<c:url value="${not empty page ? page.label : urlLink}" var="encodedUrl" />
<spring:theme text="View" var="viewtext" code="general.view"/>


<div class="banner background information" 
	 style="background-image: url(${media.url});">
	<h2 class="${contentAlignment.code} text_a_${contentAlignment.code}" style="color: ${headlineColor};">${headline}</h2>
	<p class="${contentAlignment.code} text_a_justify" style="color: ${contentColor};">${content}</p>
	
	<c:if test="${not empty encodedUrl}">
		<p class="${contentAlignment.code} text_a_justify"><a href="${encodedUrl}" style="color: ${linkTextColor}">${not empty linkText ? linkText : viewtext} &#8250;</a></p>
	</c:if> 
</div>