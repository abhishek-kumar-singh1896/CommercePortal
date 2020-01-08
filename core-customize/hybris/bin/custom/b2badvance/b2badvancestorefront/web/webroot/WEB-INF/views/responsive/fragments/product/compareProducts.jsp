<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>

<c:forEach items="${compareProducts}" var="product">
        <product:compareItem product="${product}"/>
</c:forEach>
<product:compareItemPlaceholder compareProducts="${compareProducts}"/>	
<c:if test="${not empty error}">
	<input type="hidden" value="true" id="searchCompareError">
</c:if>

<div class="modal fade hide-error-msg" id="searchCompareError-modal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="false" style="display: none;">
	<div class="modal-dialog" role="document">
		<div class="modal-content padded-alert">
			<div class="modal-body">
				<p class="body">
					${error}
				</p>
			</div>
		</div>
	</div>
</div>	