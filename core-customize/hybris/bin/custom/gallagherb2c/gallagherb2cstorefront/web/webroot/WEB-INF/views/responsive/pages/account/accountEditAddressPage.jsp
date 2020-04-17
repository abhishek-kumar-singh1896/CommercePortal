<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/address-book" var="addressBookUrl"
	htmlEscape="false" />

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
						<c:choose>
							<c:when test="${edit eq true }">
								<spring:theme code="text.account.addressBook.updateAddress" />
							</c:when>
							<c:otherwise>
								<spring:theme code="text.account.addressBook.addAddress" />
							</c:otherwise>
						</c:choose>
					</h1>
					<div class="account-section-content mb-5 mt-5">
						<div class="account-section-form">
							<%-- <div class="row">
								<div class="container-lg col-md-6">
									<button type="button" class="addressBackBtn"
										data-back-to-addresses="${fn:escapeXml(addressBookUrl)}">
										<span class="glyphicon glyphicon-chevron-left"></span>
									</button>
									<span class="label">${headline}</span>
								</div>
							</div> --%>
							<div class="account-section-content">
								<div class="account-section-form">
									<address:addressFormSelector supportedCountries="${countries}"
										regions="${regions}" cancelUrl="/my-account/address-book"
										addressBook="true" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>





<address:suggestedAddresses
	selectedAddressUrl="/my-account/select-suggested-address" />

