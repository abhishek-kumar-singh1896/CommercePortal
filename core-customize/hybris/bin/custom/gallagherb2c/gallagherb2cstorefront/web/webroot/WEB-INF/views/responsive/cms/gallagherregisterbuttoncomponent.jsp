<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="register-account-out">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-12">
                    <h2 class="register-account-title">${component.buttonHeading}</h2>
                    <c:url var="register" value="${component.button.url}"></c:url>
                    <button type="button" class="btn btn-primary" onclick="window.location.href='${register}'">
                    	${component.button.linkName}
                    </button>
                </div>
            </div>
        </div>
    </div>
