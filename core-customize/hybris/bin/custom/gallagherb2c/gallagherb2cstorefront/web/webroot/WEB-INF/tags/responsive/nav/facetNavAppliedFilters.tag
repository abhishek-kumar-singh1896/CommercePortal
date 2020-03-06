<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageData" required="true"
	type="de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:if test="${not empty pageData.breadcrumbs}">

	<div class="sidebar-filter-section">
		<c:if test="${fn:length(pageData.breadcrumbs) >0}">
			<div class="cross-icon-out">
				<a href="javascript:void(0)" class="clear-filter">
					<span
						class="cross-icon"> 
						<svg>
							<use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
	                    </svg>
					</span> 
					<spring:theme code="productList.refinements.clearFilters" />
				</a>
			</div>
		</c:if>
	</div>
	<div class="sidebar-section">

		<div class="sidebar-section-header with-link">
			<span class="glyphicon facet__arrow"></span>
			<spring:theme code="search.nav.applied.facets" />
		</div>
		<div class="facet__values js-facet-values">
			<ul class="facet__list">
				<c:forEach items="${pageData.breadcrumbs}" var="breadcrumb">
					<li><c:url value="${breadcrumb.removeQuery.url}"
							var="removeQueryUrl" /> &nbsp; ${breadcrumb.facetName} : <b>
							<a href="${fn:escapeXml(removeQueryUrl)}"><span
								class="glyphicon glyphicon-remove"></span>${fn:escapeXml(breadcrumb.facetValueName)}</a>
					</b></li>
				</c:forEach>
			</ul>
		</div>
	</div>

</c:if>