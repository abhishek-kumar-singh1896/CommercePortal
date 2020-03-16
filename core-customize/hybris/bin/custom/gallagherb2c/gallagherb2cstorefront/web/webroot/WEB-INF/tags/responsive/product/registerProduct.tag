<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%> --%>
<c:url value="/register/submit" var="actionURL" />
<div class="register-product-out">
        <div class="container">

            <div class="row">
                <div class="col-12">
                    <div class="breadcrumb-out">
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="#">Home</a></li>
                                <li class="breadcrumb-item active" aria-current="page">Product Registration</li>
                            </ol>
                        </nav>
                    </div>
                </div>
            </div>

            <div id="productSuccessAlert" class="alert alert-success show d-none" role="alert">
                <strong>Your product is registered successfully.
                    <!-- <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button> -->
            </div>

            <h1 class="primary-title">
                Register Product
            </h1>
            <div class="register-product-desc">
                <p>If you are from Europe please go to <a href="http://www.gallagher.eu"
                        target="_blank">www.gallagher.eu</a> to register your product and obtain your energizer warranty
                    extension</p>
            </div>

            <h2 class="secondary-title mt-5">Product will be registered under (John Johnson)</h2>
			<form:form method="post" action="${actionURL}" id="registerProductForm">
            <div class="row">
                <div class="col-md-6">
                    <div class="row">
                        <div class="col-12 mb-4">
                            <label for="productSku" class="common-form-label">Product Type*</label>
                            <input type="text" class="form-control common-input has-error" id="productSku">
                            <div class="error-label">
                                <span class="error-icon">
                                    <svg>
                                        <use xlink:href="img/gallagher-icons.svg#cross" />
                                    </svg>
                                </span>
                                <span class="error-text">Please fill</span>
                            </div>
                        </div>

                        <div class="col-12 mb-4">
                            <label for="serialNumber" class="common-form-label">Serial number*</label>
                            <input type="number" pattern="\d*" maxlength="10" class="form-control common-input" id="serialNumber">
                        </div>

                        <div class="col-12 mb-4">
                            <label for="datePurchased" class="common-form-label">Date purchased (dd/mm/yyyy)*</label>
                            <input type="text" class="form-control common-input has-error" id="datePurchased">
                            <div class="error-label">
                                <span class="error-icon">
                                    <svg>
                                        <use xlink:href="img/gallagher-icons.svg#cross" />
                                    </svg>
                                </span>
                                <span class="error-text">Please fill</span>
                            </div>
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
                    <select name="country" id="country" class="form-control js-example-basic-single has-error">
                        <option value="0">Country 1</option>
                        <option value="1">Country 2</option>
                        <option value="0">Country 3</option>
                        <option value="1">Country 4</option>
                        <option value="1">Country 5</option>
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
           </form:form>
        </div>
    </div>