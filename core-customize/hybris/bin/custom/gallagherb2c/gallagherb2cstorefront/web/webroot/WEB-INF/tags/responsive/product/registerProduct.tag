<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%-- <%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%> --%>
	<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
<c:url value="/register/submit" var="actionURL" />
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

            <div id="productSuccessAlert" class="alert alert-success show d-none" role="alert">
                <strong>Your product is registered successfully.
                    <!-- <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button> -->
            </div>

            <h1 class="primary-title">
                Product Registration
            </h1>
            <div class="register-product-desc">
                <p>If you are from Europe please go to <a href="http://www.gallagher.eu"
                        target="_blank">www.gallagher.eu</a> to register your product and obtain your energizer warranty
                    extension</p>
            </div>

            <h2 class="secondary-title mt-5">Product will be registered under (${user.firstName}&nbsp${user.lastName})</h2>
			<%-- <form:form method="post" action="${actionURL}" id="registerProductForm"> --%>
            <div class="row">
                <div class="col-md-6">
                    <div class="row">
                        <div class="col-12 mb-4">
                            <label for="productSku" class="common-form-label">Product SKU*</label>
                            <input type="text" class="form-control common-input" id="productSku">
                            <%-- <div class="error-label">
                                <span class="error-icon">
                                    <svg>
                                        <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
                                    </svg>
                                </span>
                                <span class="error-text">Please fill</span>
                            </div> --%>
                        </div>

                        <div class="col-12 mb-4">
                            <label for="serialNumber" class="common-form-label">Serial number*</label>
                            <input type="number" pattern="\d*" maxlength="10" class="form-control common-input" id="serialNumber">
                        </div>

                        <div class="col-12 mb-4">
                            <label for="datePurchased" class="common-form-label">Date purchased (dd/mm/yyyy)*</label>
                            <input type="text" class="form-control common-input" id="datePurchased">
                            <%-- <div class="error-label">
                                <span class="error-icon">
                                    <svg>
                                        <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
                                    </svg>
                                </span>
                                <span class="error-text">Please fill</span>
                            </div> --%>
                        </div>
                    </div>
                </div>
            </div>

            <h2 class="secondary-title">Your Details</h2>

            <div class="row">
                <div class="col-md-6 mb-4">
                    <label for="addressLine1" class="common-form-label">Address Line 1*</label>
                    <input type="text" class="form-control common-input" id="addressLine1">
                </div>
                <div class="col-md-6 mb-4">
                    <label for="addressLine2" class="common-form-label">Address Line 2</label>
                    <input type="text" class="form-control common-input" id="addressLine2">
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-4">
                    <label for="townCity" class="common-form-label">Town/ City</label>
                    <input type="text" class="form-control common-input" id="townCity">
                </div>
                <div class="col-md-6 mb-4">
                    <label for="postCode" class="common-form-label">Post Code*</label>
                    <input type="text" class="form-control common-input" id="postCode">
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-4">
                    <label for="country" class="common-form-label">Country*</label>
                    <select name="country" id="country" class="form-control js-example-basic-single">
                        <option value="0">New Zealand</option>
                        <option value="1">Australia</option>
                        <option value="0">Canada (EN)</option>
                        <option value="1">Canada (FR)</option>
                        <option value="1">Latin America</option>
                        <option value="0">Global</option>
                    </select>
                </div>
                <div class="col-md-6 mb-4">
                    <label for="phoneNumber" class="common-form-label">Phone number</label>
                    <input type="text" class="form-control common-input" id="phoneNumber">
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-4">
                    <label for="attachReceipt" class="common-form-label">Attach a Receipt</label>
                    <div class="input-group">
                        <div class="custom-file">
                            <input type="file" class="custom-file-input" id="attachReceipt">
                            <label class="custom-file-label" for="attachReceipt"
                                aria-describedby="Attach a Receipt">Choose file</label>
                        </div>
                    </div>
                    <div class="hint-text">Max file size: 5mb, File type: jpg, png, pdf</div>

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
                <button type="submit" class="btn btn-primary registerProduct" data-toggle="modal"
                    data-target="#confirmRegisterModal">Register</button>
            </div>
           <%-- </form:form> --%>
        </div>
    </div>
    
    <!-- Modal Confirm Product Register -->
   <div class="modal fade" id="confirmRegisterModal" tabindex="-1" role="dialog"
        aria-labelledby="confirmRegisterModalTitle" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="confirmRegisterModalTitle">You are about to Register</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="register-product-modal-container">
                        <div class="hint-text mb-4">
                            If these details do not match your product, please contact the Customer services Team
                            Email: <a href="mailto:sales.nz@am.gallagher" target="_blank">sales.nz@am.gallagher</a> |
                            Telephone number: 0800 731 500
                        </div>

                        <div class="row mb-2">
                            <div class="col-md-3">
                                <img src="img/fence-product-img.png" class="img-fluid" alt="product image">
                            </div>
                            <div class="col-md-9">
                                <div class="product-name">Fence Energizer B180</div>
                                <div class="product-id">G36010</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-highlight" id="registerSuccess">Register</button>
                </div>
            </div>
        </div>
    </div>
    </sec:authorize>