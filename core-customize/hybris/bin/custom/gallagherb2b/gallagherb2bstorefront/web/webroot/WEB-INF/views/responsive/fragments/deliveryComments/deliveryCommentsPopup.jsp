<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<spring:htmlEscape defaultHtmlEscape="true" />
<c:url value="/checkout/setdeliveryInstruction"
	var="deliveryInstructionUrl" />

<form:form method="post" modelAttribute="deliveryInstrutionsform"
	id="deliveryInstrutionsform">
	<input type="hidden" id="refreshUrl" name="refreshUrl" />
	<br>
	<input type="hidden" id="entryID"
		value="${deliveryInstrutionsform.entryNumber}" name="entryNumber" />
	<input type="hidden" id="productSpecificDetailsHeading" value="${deliveryInstrutionsform.productSpecificDetailsHeading}" name="productSpecificDetailsHeading" />
	<div><strong>${deliveryInstrutionsform.productSpecificDetailsHeading}</strong></div>
	<div><strong><p>Please enter details<p></strong></div>
	<textarea spellcheck="false" class="form-control add-comment-text-area"
		name="deliveryInstructionEntry"
		placeholder="80 character limit"
		id="deliveryInstructionEntry"
		maxlength="<spring:theme code="checkout.instruction.length.limit" />">${deliveryInstrutionsform.deliveryInstruction}</textarea>

	<div class="help-block textareaErrorbox">
		<span><spring:theme code="checkout.instruction.length.invalid" /></span>
	</div>

	<button class="btn btn-primary btn-block" id="instruction_add_button"
		type="submit" value="${deliveryInstrutionsform.entryNumber}">
		<spring:theme code="deliveryinstruction.add" />
	</button>

</form:form>