<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<c:url value="/register-product/registerSuccess" var="actionURL" />
<div id="registerProductPopup">

<div>
    <div class="register-product-modal-container">
        <div class="hint-text mb-4">
            If these details do not match your product, please contact the Customer services Team
            Email: <a href="mailto:sales.nz@am.gallagher" target="_blank">sales.nz@am.gallagher</a> |
            Telephone number: 0800 731 500
        </div>

        <div class="row mb-2">
            <div class="col-md-3">
            <c:set var="count" value="1"/>
            	<c:forEach items="${product.images}" var="medias" varStatus="status">
                    <c:if test="${medias.format eq 'thumbnail'}">
                    <c:if test="${count eq 1}">
	                    <img src="${medias.url}" alt="${medias.altText}">
	                    <c:set var="count" value="2"/>
                    </c:if>
                    </c:if>
                    </c:forEach>
            </div>
            <div class="col-md-9">
                <div class="product-name">${product.name}</div>
                <div class="product-id">${product.code}</div>
            </div>
        </div>
    </div>

<form:form method="post" action="${actionURL}" id="registerProductForm1" class="registerProductSubmit_form">
	<input type="hidden" name="productSku" value="${registerProductForm.productSku}">
	<input type="hidden" name="serialNumber" value="${registerProductForm.serialNumber}">
	<input type="hidden" name="datePurchased" value="${registerProductForm.datePurchased}">
	<input type="hidden" name="addressLine1" value="${registerProductForm.addressLine1}">
	<input type="hidden" name="addressLine2" value="${registerProductForm.addressLine2}">
	<input type="hidden" name="townCity" value="${registerProductForm.townCity}">
	<input type="hidden" name="postCode" value="${registerProductForm.postCode}">
	<input type="hidden" name="country" value="${registerProductForm.country}">
	<input type="hidden" name="phoneNumber" value="${registerProductForm.phoneNumber}">
	<div class="row mt-3">
		<div class="col-12 text-right">  <button type="submit" class="btn btn-highlight" id="registerSuccess">Register</button></div>  
	</div>
</form:form>
</div>
</div>
