<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="account-section-header">
	<div class="row">
		<div class="container-lg col-md-6">
			<spring:theme code="text.account.profile.updatePasswordForm" />
		</div>
	</div>
</div>
<div class="row">
	<div class="container-lg col-md-12">
		<c:choose>
			<c:when test="${success}">
				<div class="global-alerts">
					<div class="alert alert-info alert-dismissable getAccAlert">
						<spring:message code="text.account.profile.updatePassword.success" /></div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="global-alerts">
					<div class="alert alert-info alert-dismissable getAccAlert">
						<spring:message code="text.account.profile.updatePassword.failure" /></div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>