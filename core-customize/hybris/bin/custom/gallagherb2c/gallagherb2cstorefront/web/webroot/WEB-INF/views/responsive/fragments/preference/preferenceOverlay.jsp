<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:theme code="text.preference.overlay.pageTitle" var="pageTitle" />
<spring:theme code="text.preference.overlay.pageSubTitle"
	var="pageSubTitle" />
<spring:theme code="text.preference.overlay.newsLetter" var="newsLetter" />
<spring:theme code="text.preference.overlay.events" var="events" />
<spring:theme code="text.preference.overlay.productRelease"
	var="productRelease" />
<spring:theme code="text.preference.overlay.productUpdate"
	var="productUpdate" />
<spring:theme code="text.preference.overlay.productPromo"
	var="productPromo" />


<spring:htmlEscape defaultHtmlEscape="true" />

<div class="overlay">
	<div class="row ">
		<div class="col-12">
                                           
			<div class="sub-title text-break text-normal">${pageSubTitle}</div>
			<form:form modelAttribute="preferences" method="post"
				action="submit-preferences">

				<div class="form-check mb-2">
					<form:checkbox class="form-check-input" id="newsLetters"
						path="newsLetters" />
					<label class="form-check-label common-form-label" for="newsLetters">${newsLetter}</label>
				</div>

				<div class="form-check mb-2">
					<form:checkbox class="form-check-input" id="events" path="events" />
					<label class="form-check-label common-form-label" for="events"> ${events} </label>
				</div>

				<div class="form-check mb-2">
					<form:checkbox class="form-check-input" id="productRelease"
						path="productRelease" />
					<label class="form-check-label common-form-label" for="productRelease">${productRelease}</label>
				</div>

				<div class="form-check mb-2">
					<form:checkbox class="form-check-input" id="productUpdate"
						path="productUpdate" />
					<label class="form-check-label common-form-label" for="productUpdate">${productUpdate}</label>
				</div>

				<div class="form-check mb-2">
					<form:checkbox class="form-check-input" id="productPromo"
						path="productPromo" />
					<label class="form-check-label common-form-label" for="productPromo">${productPromo}</label>
				</div>

			<div class="mt-4">
							<button type="submit" class="btn btn-primary preference-submit">Submit</button>
							</div>
							
			</form:form>


		</div>
	</div>
</div>




