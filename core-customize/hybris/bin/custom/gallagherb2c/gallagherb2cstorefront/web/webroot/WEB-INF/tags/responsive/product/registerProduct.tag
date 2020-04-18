<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
	<c:url value="/register-product/verify" var="verifyURL" />
	<c:url value="/register-product/submit" var="submitURL" />
	<product:registerProductTitle />
	<div class="register-product-out">
		<div id="product-not-found-alert" class="d-none">

			<div class="alert alert-danger alert-dismissable getAccAlert">
				<button class="close closeAccAlert" aria-hidden="true"
					data-dismiss="alert" type="button">×</button>
				<spring:theme code="registerProduct.product.not.found.message.title"/>
			</div>
		</div>
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

			<h1 class="primary-title"><spring:theme code="header.register.product"/></h1>
			<div class="register-product-desc">
				<p>
					<cms:pageSlot position="Section1" var="feature">
						<cms:component component="${feature}" element="div" class="" />
					</cms:pageSlot>
				</p>
			</div>

			<h2 class="secondary-title mt-5"><spring:theme code="registerProduct.under.title"/>${user.firstName}&nbsp${user.lastName}</h2>
			<form:form method="post" action="${submitURL}"
				id="registerProductForm" class="registerProduct_form" enctype="multipart/form-data"
				modelAttribute="registerProductForm">
				<div class="row">
					<div class="col-md-6">
						<div class="row">
							<div class="col-12 mb-4">
								<label for="productSku" class="common-form-label"><spring:theme code="registerProduct.productSKU.title"/></label>
								<!-- <input type="text" class="form-control common-input" id="productSku" name="productSku"/> -->
								<%-- <form:input type="text" class="form-control common-input" id="productSku" name="productSku"/> --%>
								<form:input type="text" class="form-control common-input" id="productSku" path="productSku" placeholder="e.g. G12345"/>
								<div class="error-label">
									<span class="error-text d-none"> <span
										class="error-icon"> <svg>
	                                        <use
													xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
	                                    </svg>
									</span> <span class="error-inner-text"></span>
									</span>
								</div>
							</div>

							<div class="col-12 mb-4">
								<label for="serialNumber" class="common-form-label"><spring:theme code="registerProduct.serialNumber.title"/></label>
								<!-- <input type="text" maxlength="10" class="form-control common-input" id="serialNumber" name="serialNumber"> -->
								<form:input type="text" maxlength="10"
									class="form-control common-input" id="serialNumber"
									path="serialNumber"
									oninput="this.value=this.value.replace(/[^0-9]/g,'');" />
								<div class="error-label">
									<span class="error-text d-none"> <span
										class="error-icon"> <svg>
	                                        <use
													xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
	                                    </svg>
									</span> <span class="error-inner-text"></span>
									</span>
								</div>
							</div>

							<div class="col-12 mb-4">
								<label for="datePurchased" class="common-form-label"><spring:theme code="registerProduct.date.title"/></label>
								<!-- <input type="text" class="form-control common-input" id="datePurchased" name="datePurchased">  -->
								<form:input type="text" class="form-control common-input"
									id="datePurchased" path="datePurchased"
									oninput="this.value=this.value.replace(/[^0-9/]/g,'');" />
								<div class="error-label">
									<span class="error-text d-none"> <span
										class="error-icon"> <svg>
	                                        <use
													xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
	                                    </svg>
									</span> <span class="error-inner-text"></span>
									</span>
								</div>
							</div>
						</div>
					</div>
				</div>

				<h2 class="secondary-title"><spring:theme code="registerProduct.details.title"/></h2>

				<div class="row">
					<div class="col-md-6 mb-4">
						<label for="addressLine1" class="common-form-label"><spring:theme code="registerProduct.addressLine1.title"/></label>
						<!-- <input type="text" class="form-control common-input" id="addressLine1" name="addressLine1"/>  -->
						<form:input type="text" class="form-control common-input"
							id="addressLine1" path="addressLine1" />
						<div class="error-label">
							<span class="error-text d-none"> <span class="error-icon">
									<svg>
	                                        <use
											xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
	                                    </svg>
							</span> <span class="error-inner-text"></span>
							</span>
						</div>
					</div>
					<div class="col-md-6 mb-4">
						<label for="addressLine2" class="common-form-label"><spring:theme code="registerProduct.addressLine2.title"/></label>
						<!-- <input type="text" class="form-control common-input" id="addressLine2">  -->
						<form:input type="text" class="form-control common-input"
							id="addressLine2" path="addressLine2" />
						<div class="error-label">
							<span class="error-text d-none"> <span class="error-icon">
									<svg>
	                                        <use
											xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
	                                    </svg>
							</span> <span class="error-inner-text"></span>
							</span>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-6 mb-4">
						<label for="townCity" class="common-form-label"><spring:theme code="registerProduct.town.title"/></label>
						<!-- <input type="text" class="form-control common-input" id="townCity" name="townCity">  -->
						<form:input type="text" class="form-control common-input"
							id="townCity" path="townCity" />
						<div class="error-label">
							<span class="error-text d-none"> <span class="error-icon">
									<svg>
	                                        <use
											xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
	                                    </svg>
							</span> <span class="error-inner-text"></span>
							</span>
						</div>
					</div>
					<div class="col-md-6 mb-4">
						<label for="region" class="common-form-label"><spring:theme code="registerProduct.region.title"/></label>
						<!-- <input type="text" class="form-control common-input" id="townCity" name="townCity">  -->
						<form:input type="text" class="form-control common-input"
							id="region" path="region" />
						<div class="error-label">
							<span class="error-text d-none"> <span class="error-icon">
									<svg>
	                                        <use
											xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
	                                    </svg>
							</span> <span class="error-inner-text"></span>
							</span>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-6 mb-4">
						<label for="postCode" class="common-form-label"><spring:theme code="registerProduct.postCode.title"/></label>
						<form:input type="text" class="form-control common-input"
							id="postCode" path="postCode" />

						<div class="error-label">
							<span class="error-text d-none"> <span class="error-icon">
									<svg>
	                                        <use
											xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
	                                    </svg>
							</span> <span class="error-inner-text"></span>
							</span>
						</div>
					</div>
					<div class="col-md-6 mb-4">
						<label for="country" class="common-form-label"><spring:theme code="registerProduct.country.title"/></label>
						<form:select path="country" id="country"
							class="form-control js-example-basic-single">
							<form:option value="0"><spring:theme code="registerProduct.selectCountry.title"/></form:option>
							<c:forEach items="${Countries}" var="country" varStatus="status">
								<form:option value="${country.isocode}">${country.name}</form:option>
							</c:forEach>
						</form:select>
						<div class="error-label">
							<span class="error-text d-none"> <span class="error-icon">
									<svg>
	                                        <use
											xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
	                                    </svg>
							</span> <span class="error-inner-text"></span>
							</span>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-6 mb-4">

						<label for="phoneNumber" class="common-form-label"><spring:theme code="registerProduct.phone.title"/></label>
						<div class="input-group">
							<div class="input-group-prepend">
								<span class="input-group-text" id="phonePlus">+</span>
							</div>
							<input type="text" class="form-control" placeholder=""
								maxlength="10"
								oninput="this.value=this.value.replace(/[^0-9+]/g,'');"
								id="phoneNumber" aria-label="phoneNumber"
								aria-describedby="phonePlus">
						</div>
					</div>

					<div class="col-md-6 mb-4">
						<label for="attachedFile" class="common-form-label"><spring:theme code="registerProduct.attachReciept.title"/></label>
						<div class="input-group">
							<div class="custom-file">
								<form:input type="file" class="form-control custom-file-input"
							id="attachedFile" path="attachedFile" name="attachedFile"/>
								<!-- <input type="file" class="custom-file-input" id="attachReceipt"> -->
								<label class="custom-file-label" for="attachedFile"
									aria-describedby="Attach a Receipt"><spring:theme code="registerProduct.chooseFile.title"/></label>
							</div>
						</div>
						<div class="hint-text">
							<spring:theme code="text.product.registration.filesize" arguments="${fileMaxSize}"/>
						</div>

						<!-- <div class="error-label">
                        <span class="error-icon">
                            <svg>
                                <use xlink:href="img/gallagher-icons.svg#cross" />
                            </svg>
                        </span>
                        <span class="error-text">Please fill</span>
                    </div> -->

					</div>

				</div>

				<div class="mt-3">
					<input type="button" class="btn btn-primary verify-registration" value="Register"/>
               <button type="submit" class="btn btn-primary d-none register-product"><spring:theme code="registerProduct.register.title"/></button>
				</div>
			</form:form>
		</div>
	</div>
	<div class="d-none">
		<div class="register-product-modal-container">
			<div class="hint-text mb-4">
				<cms:pageSlot position="Section2" var="feature" element="div">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>

			<div class="row mb-2">
				<div class="col-3 product-image"></div>
				<div class="col-9 register-product-right-section">
					<div id="product-name" class="product-name"></div>
					<div id="product-id" class="product-name"></div>
					<div id="product-serial" class="product-id"></div>
				</div>
			</div>
				<div class="row mt-3">
					<div class="col-12 text-right">
						<input type="button" value="Register" class="btn btn-highlight verification-success"
							id="verification-success" />
					</div>
				</div>
		</div>
	</div>
</sec:authorize>