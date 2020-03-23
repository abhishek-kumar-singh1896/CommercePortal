<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="overlay">
	<div class="row ">
		<div class="col-12">
                                           
			<div class="sub-title text-break text-normal"><spring:theme code="text.preference.overlay.pageSubTitle" /></div>
			<form:form modelAttribute="preferences" method="post"
				action="submitPreferences" id="customerPreferences">

				<div class="form-check mb-2">
					<form:checkbox class="form-check-input" id="newsLetters"
						path="newsLetters" />
					<label class="form-check-label common-form-label" for="newsLetters">
						<spring:theme code="text.preference.overlay.newsLetter" />
					</label>
				</div>

				<div class="form-check mb-2">
					<form:checkbox class="form-check-input" id="events" path="events" />
					<label class="form-check-label common-form-label" for="events">
						<spring:theme code="text.preference.overlay.events" />
					</label>
				</div>

				<div class="form-check mb-2">
					<form:checkbox class="form-check-input" id="productRelease"
						path="productRelease" />
					<label class="form-check-label common-form-label" for="productRelease">
						<spring:theme code="text.preference.overlay.productRelease" />
					</label>
				</div>

				<div class="form-check mb-2">
					<form:checkbox class="form-check-input" id="productUpdate"
						path="productUpdate" />
					<label class="form-check-label common-form-label" for="productUpdate">
						<spring:theme code="text.preference.overlay.productUpdate" />
					</label>
				</div>

				<div class="form-check mb-2">
					<form:checkbox class="form-check-input" id="productPromo"
						path="productPromo" />
					<label class="form-check-label common-form-label" for="productPromo">
						<spring:theme code="text.preference.overlay.productPromo" />
					</label>
				</div>

			<div class="mt-4">
							<button type="submit" class="btn btn-primary preference-submit">Submit</button>
							</div>
							
			</form:form>


		</div>
	</div>
</div>




