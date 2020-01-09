<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:message code="laminex.page.contactus.link" var="contactUsLink"/> 

<div class="search-info-wrapper out-of-stock-message error-message" style="display: none;">
	<div class="container">
		<div class="row">
			<div class="search-info">
				<spring:theme code="text.quickOrder.page.error.outOfStock" />
			</div>
		</div>
	</div>
</div>
<div class="search-info-wrapper no-product-added-message error-message" style="display: none;">
	<div class="container">
		<div class="row">
			<div class="search-info">
				<spring:theme code="text.quickOrder.page.error.noProductAdded" />
			</div>
		</div>
	</div>
</div>
<div class="search-info-wrapper no-quantity-selected-message error-message" style="display: none;">
	<div class="container">
		<div class="row">
			<div class="search-info">
				<spring:theme code="text.quickOrder.page.error.noQuantitySelected" />
			</div>
		</div>
	</div>
</div>
<div class="search-info-wrapper not-all-dropdown-selected-message error-message" style="display: none;">
	<div class="container">
		<div class="row">
			<div class="search-info">
				<spring:theme code="text.quickOrder.page.error.notAllDropdownSelected" arguments="${contactUsLink}"/>
			</div>
		</div>
	</div>
</div>
<div class="search-info-wrapper qty-more-than-stock-message error-message" style="display: none;">
	<div class="container">
		<div class="row">
			<div class="search-info">
				<spring:theme code="text.quickOrder.page.error.quantityMoreThanStock" arguments="${contactUsLink}"/>
			</div>
		</div>
	</div>
</div>