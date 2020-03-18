<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<div>
    <div class="register-product-container">
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
</div>
<div>
    <button type="button" class="btn btn-highlight" id="registerSuccess">Register</button>
</div>
