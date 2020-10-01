<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageData" required="true"
	type="de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when test="${pageData.facets.size() > 0}">
		<spring:theme code="search.nav.facetTitle.new"/>
	</c:when>
	<c:otherwise>
		<spring:theme code="search.nav.facets.noFilters"/>
	</c:otherwise>
</c:choose>
<c:forEach items="${pageData.facets}" var="facet" varStatus="i">
	<c:choose>
		<c:when test="${facet.code eq 'availableInStores'}">
			<nav:facetNavRefinementStoresFacetResponsive facetData="${facet}"
				userLocation="${userLocation}" />
		</c:when>
		<c:otherwise>

			<c:set var="flag" value="" />
			<c:if test="${i.first}">
				<c:set var="flag" value="show" />
			</c:if>
			<nav:facetNavRefinementFacetResponsive facetData="${facet}"
				collapseflag="${flag}" />
		</c:otherwise>
	</c:choose>
</c:forEach>



