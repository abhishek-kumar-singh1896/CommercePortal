<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<c:url value="/bulkorder/search" var="searchUrl" />
<div class="comparison-row" id="js-comparison-row">
	<div class="heading">
		<h1><spring:theme code="productcomparison.header.compare_items" text="COMPARE PRODUCTS"/></h1>
	</div>
	<div class="clearfix" id="js-comp-item-box">
		<c:if test="${not empty comparedProducts}">
			<c:forEach items="${comparedProducts}" var="product" varStatus="status">
	        	<product:compareItem product="${product}"/>
			</c:forEach>
			<product:compareItemPlaceholder compareProducts="${comparedProducts}"/>
		</c:if>
	</div>
	<div class="button-box">
		<button type="button" class="neutral compare-product" onclick="pcShowComparePage('<c:url value="/"/>',false,false)" ><spring:theme code="productcomparison.button.compare_items" text="COMPARE ITEMS" /></button>
		<button class="neutral" type="button" onclick="ACC.compare.updateCompare(null, 'removeAll')"><spring:theme code="productcomparison.button.clear_items" text="CLEAR LIST"/></button>
		<div class="compare-tray-add">
			<div class="compare-add-input">
				<input id="addToCompareInput" type="text" class="text product js-compare-search-input" placeholder="I'm looking for"/>
				<span class="glyphicon glyphicon-search"></span>
			</div>
		</div>
	</div>
</div>