<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<c:set value="${product.baseProduct eq null}" var="isBaseProduct" />
<c:set var="levels" value="${product.totalVariantCategories}" />
<%-- Verify if products is a multidimensional product --%>
<div class="td item-feature">
	<c:if test="${product.multidimensional and not empty product.variantMatrix}">

		<%-- Loop on variant matrix and set variant matrix --%>
		<c:set var="dropdownindex" value="1" />
		<c:forEach begin="1" end="${levels}" varStatus="loop">
			<c:set var="i" value="0" />
			<c:forEach items="${productMatrix}" var="variantCategory">
				<c:if
					test="${(variantCategory.variantOption.code eq product.code) || fn:length(productMatrix) eq 1}">
					<c:set var="selectedIndex" value="${i}" />
				</c:if>
				<c:set var="i" value="${i + 1}" />
			</c:forEach>
			<c:choose>
				<c:when test="${loop.index eq 1}">
					<c:set var="productMatrix" value="${product.variantMatrix}" />
				</c:when>
				<c:otherwise>
					<c:set var="productMatrix"
						value="${productMatrix[selectedIndex].elements}" />
				</c:otherwise>
			</c:choose>
			<div class="substrate tablerow td${dropdownindex}">
				<input type="hidden" value="${dropdownindex}" class="loopindex">
				<c:set var="dropdownindex" value="${dropdownindex +1 }" />
				<c:forEach items="${productMatrix}" var="variantCategory">
					<c:if test="${not isValueSelected}">
						<c:set var="isValueSelected"
							value="${((variantCategory.variantOption.code eq product.code) || fn:length(productMatrix) eq 1) ? true:false}" />

					</c:if>
				</c:forEach>
				
				<select id="select1" class="select-options">
					<option value="" disabled="disabled"><spring:theme
							code="text.quickOrder.page.dropdown.emptytext" />&nbsp;${productMatrix[0].parentVariantCategory.name}
					</option>
					<c:forEach items="${productMatrix}" var="variantCategory">
						<option
							value='{"productcode":"${variantCategory.variantOption.code}","isLastDropdown":${variantCategory.isLeaf}}'
							${((variantCategory.variantOption.code eq product.code) || fn:length(productMatrix) eq 1) ? 'selected="selected"' : ''}>${fn:escapeXml(variantCategory.variantValueCategory.name)}</option>
					</c:forEach>
				</select>
			</div>
		</c:forEach>
	</c:if>
</div>