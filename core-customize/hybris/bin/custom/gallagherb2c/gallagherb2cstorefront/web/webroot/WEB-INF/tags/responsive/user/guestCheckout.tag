<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true"
	type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="container">
	<div class="guest-checkout-container">

		<h1 class="primary-title">
			<spring:theme code="guest.checkout" arguments="${siteName}" />
		</h1>


		<form:form action="${action}" method="post" modelAttribute="guestForm">
			<div class="form-group">
			 <div class="form-group mb-4">
				<formElement:formInputBox idKey="guest.email" labelKey="guest.email"
					inputCSS="guestEmail" path="email" mandatory="true" />
					</div>
			</div>

			<div class="form-group mb-4">
				<label class="control-label" for="guest.confirm.email"> <spring:theme
						code="guest.confirm.email" /></label> <input
					class="confirmGuestEmail form-control" id="guest.confirm.email" />
			</div>

			<ycommerce:testId code="guest_Checkout_button">
				<div class="form-group">
					<button type="submit"
						class="btn btn-default btn-block guestCheckoutBtn">
						<spring:theme code="${actionNameKey}" />
					</button>
					<!-- 		 disabled="disabled" -->
				</div>
			</ycommerce:testId>

		</form:form>
	</div>
</div>
