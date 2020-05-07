<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
	
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="main__inner-wrapper">
	<div class="account-section">
		<div class="yCmsContentSlot account-section-content">
			<div class="container">
				<div class="row">
					<div class="col-12">
						<div class="breadcrumb-out">
							<c:if test="${fn:length(breadcrumbs) > 0}">
								<nav aria-label="breadcrumb">
									<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
								</nav>
							</c:if>
						</div>
					</div>
				</div>
<div class="profile-container">
	<h1>
			<spring:theme code="text.account.update.email.address"/>
			</h1>

<div class="account-section-content mb-5 mt-5">
						<div class="account-section-form">
				<form:form action="update-email" method="post" modelAttribute="updateEmailForm">
				
				
					<formElement:formInputBox idKey="profile.email" labelKey="profile.email" path="email" inputCSS="text" mandatory="true" placeholder="Enter email address"/>
					<formElement:formInputBox idKey="profile.checkEmail"  labelKey="profile.checkEmail" path="chkEmail" inputCSS="text" mandatory="true" placeholder="Confirm new email address"/>
					<%-- <formElement:formPasswordBox idKey="profile.pwd" labelKey="profile.pwd" path="password" inputCSS="text form-control" mandatory="true"/>
					<input type="hidden" id="recaptchaChallangeAnswered" value="${fn:escapeXml(requestScope.recaptchaChallangeAnswered)}"/>
					<div class="form_field-elements control-group js-recaptcha-captchaaddon"></div> --%>
					
						<div class="row mt-5">
									<div class="col-6">
										<div class="accountActions">
									<ycommerce:testId code="email_saveEmail_button">
										<button type="submit" class="btn btn-primary btn-block">
											<spring:theme code="text.account.profile.saveUpdates" />
										</button>
									</ycommerce:testId>
								</div>
							</div>
							<div class="col-6">
										<div class="accountActions">
									<ycommerce:testId code="email_cancelEmail_button">
										<c:url value="/" var="homePageUrl" />
												 <a href="${fn:escapeXml(homePageUrl)}" class="btn btn-default btn-block backToHome">
													<spring:theme code="text.account.profile.cancel"
															text="Cancel" />
												 </a>
									</ycommerce:testId>
								</div>
							</div>
						</div>
					
				</form:form>
			</div>
		</div>
	</div>
</div>
</div>
</div>
</div>
