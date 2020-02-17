<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>

<!-- <div id="product-facet" class="hidden-sm hidden-xs product__facet js-product-facet"> -->
<div id="product-facet" class="product-list-sidebar-out">
    <nav:facetNavRefinements pageData="${searchPageData}"/>
    <nav:facetNavAppliedFilters pageData="${searchPageData}"/>
</div>