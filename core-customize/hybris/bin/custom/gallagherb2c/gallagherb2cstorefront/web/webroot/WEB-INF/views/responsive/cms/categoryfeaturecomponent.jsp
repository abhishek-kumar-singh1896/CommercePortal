<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url value="${url}" var="componentLinkUrl"/>
<c:set value="${not empty component.title ? component.title : component.category.name}" var="componentTitle"/>
<c:set value="${not empty component.description ? component.description : component.category.description}" var="componentDescription"/>

<div class="col-12 col-sm-12 col-md-3 mb-4">
	  <div class="image-section">
	    <a href="${componentLinkUrl}">
	      <img title="${componentTitle}" alt="${componentTitle}" src="${not empty component.media.url ? component.media.url : component.category.thumbnail.url}">
	    </a>
	  </div>
	  <div class="title-section">
	    <a href="${componentLinkUrl}">${componentTitle}</a>
	  </div>
	  <div class="description-section">
	    ${componentDescription}
	  </div>
</div>
