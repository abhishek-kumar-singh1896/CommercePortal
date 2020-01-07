<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>

<div class="compare-panel">
	<div class="compare-button-panel">
	<c:if test="${component.showCompareButton}">
		<spring:theme code="productcomparison.button.compare_items" text="COMPARE PRODUCTS" var="compareBtnLabel" />
		<spring:theme code="productcomparison.header.compare_items" text="Compare Products" var="heading" />
		<input type="hidden" id="compareHeading" value="${heading}"/>
		<input type="hidden" id="compareBtnLabel" value="${compareBtnLabel}"/>
		<button type="button" class="compareBtn positive" ${fn:length(pcCodes) lt 2 ? 'disabled=disabled' : ''} 
		onclick="pcShowComparePage('<c:url value="/"/>',${component.openPopup},${component.closePopupAfterAddToCart})" >
			${compareBtnLabel} (${fn:length(pcCodes)})
		</button>
	</c:if>
	</div>
	<div class="compare-checkbox">
	<c:if test="${component.showCheckbox}">
		<c:set var="comparable" value="${not empty pcCodes[product.code]}" />
		<c:url value="/compare" var="pcUrl" />
		<input type="checkbox" ${comparable ? 'checked=checked' : ''} onclick="pcUpdateComparableState('${product.code}','${pcUrl}', this, '${compareBtnLabel}')"
			style="margin-top:25px">
		<spring:theme code="productcomparison.checkbox.compare" text="Compare"/>
	</c:if>
	</div>
</div>
