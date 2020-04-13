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
					<h1 class="primary-title">
						<spring:theme code="text.account.profile.updatePersonalDetails" />
					</h1>
					<div class="account-section-content mb-5 mt-5">
						<div class="account-section-form">
							<form:form action="update-profile" method="post"
								modelAttribute="updateProfileForm">

								<formElement:formSelectBoxDefaultEnabled idKey="profile.title"
									labelKey="profile.title" path="titleCode" mandatory="true"
									skipBlank="false" skipBlankMessageKey="form.select.none"
									items="${titleData}" selectCSSClass="form-control" />


								<formElement:formInputBox idKey="profile.firstName"
									labelKey="profile.firstName" path="firstName" inputCSS="text"
									mandatory="true" />


								<formElement:formInputBox idKey="profile.lastName"
									labelKey="profile.lastName" path="lastName" inputCSS="text"
									mandatory="true" />

								<div class="row mt-5">
									<div class="col-6">
										<div class="accountActions">
											<ycommerce:testId
												code="personalDetails_savePersonalDetails_button">
												<button type="submit" class="btn btn-primary btn-block">
													<spring:theme code="text.account.profile.saveUpdates"
														text="Save Updates" />
												</button>
											</ycommerce:testId>
										</div>
									</div>
									<div class="col-6">
										<div class="accountActions">
											<ycommerce:testId
												code="personalDetails_cancelPersonalDetails_button">
												<button type="button"
													class="btn btn-default btn-block backToHome">
													<spring:theme code="text.account.profile.cancel"
														text="Cancel" />
												</button>
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
