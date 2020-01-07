<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url value="/search/autocomplete/SearchBox" var="autocompleteUrl" />
<div class="quick-order-product-search-wrapper">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="quick-order-product-search ui-front">
					<form>
						<i class="glyphicon glyphicon-search"></i>
						<input type="search"
							placeholder="<spring:theme code='text.quickOrder.page.search.placeholder'/>"
							class="js-search-input-field form-control form-control ui-autocomplete-input"
							data-options="{&quot;autocompleteUrl&quot; : &quot;${autocompleteUrl}&quot;,&quot;minCharactersBeforeRequest&quot; : &quot;<spring:theme code='text.quickOrder.page.search.minCharactersBeforeRequest'/>&quot;,&quot;waitTimeBeforeRequest&quot; : &quot;<spring:theme code='text.quickOrder.page.search.waitTimeBeforeRequest'/>&quot;,&quot;displayProductImages&quot; : true,&quot;productSearchRequest&quot; : false}"
							autocomplete="off">
					</form>

					<div class="keyword-enter-suggestion">
						<spring:theme code="text.quickOrder.page.keywordEnterSuggestion" />
					</div>

					<div class="invalid-search" style="display: none;">
						<spring:theme code="text.quickOrder.page.error.invalidKeyword" />
					</div>
					<ul class="autocomplete-search-results">
					</ul>

				</div>

			</div>

		</div>
	</div>
</div>
