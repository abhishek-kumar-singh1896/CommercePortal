<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="searchPageData" required="true"
	type="de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData"%>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<div class="modal-updated refine-filter-modal" id="refineFilter"
	tabindex="-1" role="dialog" aria-labelledby="refineFilterTitle"
	aria-hidden="true">
	<div class="modal-dialog modal-dialog-scrollable" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true"> <spring:theme
							code="productList.refinements.close" /> <span class="cross-icon">
							&times; </span>
					</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="product-list-sidebar-out js-product-facet">
					<div class="sidebar-filter-section d-flex flex-column justify-content-center">
						<c:if test="${fn:length(searchPageData.breadcrumbs) >0}">  
							<div>
                            	<a href="javascript:void(0)" class="clear-filter">
	                                <span class="cross-icon">
		                                <svg>
		                                	<use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
		                                </svg>
	                                </span>
                                    <spring:theme code="productList.refinements.clearFilters" />
                                </a>
                            </div>
						</c:if>				
					</div>
					<nav:facetNavAppliedFiltersResponsive pageData="${searchPageData}" />
					<nav:facetNavRefinementsResponsive pageData="${searchPageData}" />
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-highlight btn-block"
					data-dismiss="modal">
					<spring:theme code="productList.refinements.save" />
				</button>
			</div>
		</div>
	</div>
</div>