<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<c:url value="/" var="homeURL"/>
<div class="container">
	<div class="row">
		<div class="col-12">
			<div class="breadcrumb-out">
				<nav aria-label="breadcrumb">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="${homeURL}">Home</a></li>
						<li class="breadcrumb-item active" aria-current="page">Update
							Password</li>
					</ol>
				</nav>
			</div>
		</div>
	</div>
	<div class="profile-container">

		<div class="account-section-content">

			<c:choose>
				<c:when test="${success}">
					<div class="password-update-sent">
						<div class="cross-icon-out">
							<svg>
                                        <use
									xlink:href="img/gallagher-icons.svg#cross" />
                                    </svg>
						</div>
						<div class="password-sent-text">
							<spring:message
								code="text.account.profile.updatePassword.success" />
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="something-went-wrong">
						<div class="cross-icon-out">
							<svg>
                                        <use
									xlink:href="img/gallagher-icons.svg#cross" />
                                    </svg>
						</div>
						<div class="oops-text">Ooops,</div>
						<div class="something-went-wrong-text">
							<spring:message
								code="text.account.profile.updatePassword.failure" />
						</div>
					</div>
		</div>
		</c:otherwise>
		</c:choose>
	</div>
</div>
</div>

