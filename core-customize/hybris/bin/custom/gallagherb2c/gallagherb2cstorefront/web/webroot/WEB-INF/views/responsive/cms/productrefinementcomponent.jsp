<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- <div id="product-facet" class="hidden-sm hidden-xs product__facet js-product-facet"> -->
<div id="product-facet" class="product-list-sidebar-out js-product-facet">
    <nav:facetNavAppliedFilters pageData="${searchPageData}"/>
    <nav:facetNavRefinements pageData="${searchPageData}"/>
</div>