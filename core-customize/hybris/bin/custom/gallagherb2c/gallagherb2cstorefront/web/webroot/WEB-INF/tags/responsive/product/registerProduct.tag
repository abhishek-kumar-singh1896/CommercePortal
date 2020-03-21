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
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
	<c:url value="/register-product/verify" var="actionURL" />
	<c:url value="/register-product/submit" var="actionURL1" />
	<product:registerProductTitle />
	<div class="register-product-out">
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
			<!-- </div> -->

			<div id="productSuccessAlert" class="alert alert-success show d-none"
				role="alert">
				Your product is registered successfully.
				<!-- <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button> -->
			</div>

			<h1 class="primary-title">Product Registration</h1>
			<div class="register-product-desc">
				<p>
					<cms:pageSlot position="Section1" var="feature">
						<cms:component component="${feature}" element="div" class=""/>
					</cms:pageSlot>
				</p>
			</div>

			<h2 class="secondary-title mt-5">Product will be registered
				under (${user.firstName}&nbsp${user.lastName})</h2>
			<%--  action="${actionURL}"  --%>
			<form:form method="post" action="${actionURL}"
				id="registerProductForm" class="registerProduct_form"
				modelAttribute="registerProductForm">
				<div class="row">
					<div class="col-md-6">
						<div class="row">
							<div class="col-12 mb-4">
								<label for="productSku" class="common-form-label">Product
									SKU</label> 
									<!-- <input type="text" class="form-control common-input" id="productSku" name="productSku"/> -->
									<%-- <form:input type="text" class="form-control common-input" id="productSku" name="productSku"/> --%>
									<form:input type="text" class="form-control common-input" id="productSku" path="productSku" />
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
								<label for="serialNumber" class="common-form-label">Serial
									number</label> 
									<!-- <input type="text" maxlength="10" class="form-control common-input" id="serialNumber" name="serialNumber"> -->
									<form:input type="text" maxlength="10" class="form-control common-input" id="serialNumber" path="serialNumber" />
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
								<label for="datePurchased" class="common-form-label">Date
									purchased (dd/mm/yyyy)</label> 
									<!-- <input type="text" class="form-control common-input" id="datePurchased" name="datePurchased">  -->
									<form:input type="text" class="form-control common-input" id="datePurchased" path="datePurchased" />
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

				<h2 class="secondary-title">Your Details</h2>

				<div class="row">
					<div class="col-md-6 mb-4">
						<label for="addressLine1" class="common-form-label">Address
							Line 1</label> 
							<!-- <input type="text" class="form-control common-input" id="addressLine1" name="addressLine1"/>  -->
							<form:input type="text" class="form-control common-input" id="addressLine1" path="addressLine1" />
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
					<div class="col-md-6 mb-4">
						<label for="addressLine2" class="common-form-label">Address
							Line 2</label> 
							<!-- <input type="text" class="form-control common-input" id="addressLine2">  -->
							<form:input type="text" class="form-control common-input" id="addressLine2" path="addressLine2" />
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

				<div class="row">
					<div class="col-md-6 mb-4">
						<label for="townCity" class="common-form-label">Town/ City</label>
						<!-- <input type="text" class="form-control common-input" id="townCity" name="townCity">  -->
						<form:input type="text" class="form-control common-input" id="townCity" path="townCity" />
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
					<div class="col-md-6 mb-4">
						<label for="postCode" class="common-form-label">Post Code</label>
						<form:input type="text" class="form-control common-input" id="postCode" path="postCode" />
						
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

				<div class="row">
					<div class="col-md-6 mb-4">
						<label for="country" class="common-form-label">Country</label> 
						<form:select path="country" id="country" class="form-control js-example-basic-single">
							<c:forEach items="${Countries}" var="country" varStatus="status">
							<form:option value="0">${country.name}</form:option>
							</c:forEach>
						</form:select><div class="error-label">
									<span class="error-text d-none"> <span
										class="error-icon"> <svg>
	                                        <use
													xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
	                                    </svg>
									</span> <span class="error-inner-text"></span>
									</span>
								</div>
					</div>
					<div class="col-md-6 mb-4">
						<label for="phoneNumber" class="common-form-label">Phone
							number</label> 
							<form:input type="text" class="form-control common-input" id="phoneNumber" path="phoneNumber"/> 
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

				<div class="row">
					<div class="col-md-6 mb-4">
						<label for="attachReceipt" class="common-form-label">Attach
							a Receipt</label>
						<div class="input-group">
							<div class="custom-file">
								<input type="file" class="custom-file-input" id="attachReceipt">
								<label class="custom-file-label" for="attachReceipt"
									aria-describedby="Attach a Receipt">Choose file</label>
							</div>
						</div>
						<div class="hint-text">Max file size: 5mb, File type: jpg,
							png, pdf</div>

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
					<button type="submit" class="btn btn-primary registerProduct">Register</button>
				</div>
			</form:form>
		</div>
	</div>
	<div class="d-none">
	    <div class="register-product-modal-container">
	        <div class="hint-text mb-4">
	            <cms:pageSlot position="Section2" var="feature" element="div" >
					<cms:component component="${feature}"/>
				</cms:pageSlot>
	        </div>
	
	        <div class="row mb-2">
	            <div class="col-md-3 product-image">
	            </div>
	            <div class="col-md-9">
	                <div class="product-name"></div>
	                <div class="product-id"></div>
	            </div>
	        </div>
	        <form:form id="registerProductSubmitForm" class="registerProductSubmitForm" method="post" action="${actionURL1}" modelAttribute="registerProductSubmitForm">
				<input type="hidden" class="form-control common-input" id="productSkuInput" name="productSku1">
				<input type="hidden" class="form-control common-input" id="serialNumberInput" name="serialNumber1">
				<input type="hidden" class="form-control common-input" id="datePurchasedInput" name="datePurchased1">
				<input type="hidden" class="form-control common-input" id="addressLine1Input" name="addressLine11">
				<input type="hidden" class="form-control common-input" id="addressLine2Input" name="addressLine21">
				<input type="hidden" class="form-control common-input" id="townCityInput" name="townCity1">
				<input type="hidden" class="form-control common-input" id="postCodeInput" name="postCode1">
				<input type="hidden" class="form-control common-input" id="countryInput" name="country1">
				<input type="hidden" class="form-control common-input" id="phoneNumberInput" name="phoneNumber1">
				
	        <div class="row mt-3">
				<div class="col-12 text-right"><button type="submit" class="btn btn-highlight registerSuccess" id="registerSuccess">Register</button></div>  
			</div>
			</form:form>
	    </div>
	</div>
</sec:authorize>