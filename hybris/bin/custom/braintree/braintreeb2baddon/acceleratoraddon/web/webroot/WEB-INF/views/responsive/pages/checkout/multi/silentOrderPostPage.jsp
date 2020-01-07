<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="multiCheckout" tagdir="/WEB-INF/tags/responsive/checkout/multi"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<jsp:include page="../../../../messages/braintreeErrorMessages.jsp" />
<spring:eval expression="@configurationService.configuration.getProperty('braintree.store.in.vault')" var="storeInVault"/>
<c:url value="${currentStepUrl}" var="choosePaymentMethodUrl" />
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">
<div class="row">
    <div class="col-sm-12" id="globalMessages"></div>
    <div class="col-sm-6">
        <div class="checkout-headline">
            <span class="glyphicon glyphicon-lock"></span>
            <spring:theme code="checkout.multi.secure.checkout"/>
        </div>
		<multiCheckout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
			<jsp:body>
                    <div class="checkout-paymentmethod">
                        <div class="checkout-indent">

                            <div class="headline"><spring:theme code="checkout.multi.paymentMethod"/></div>


		<c:choose>
		<c:when test="${payPalCheckoutData.applePayEnabled}">
                        <div style="overflow: auto;" id="applepay" class="hide">
                            <c:choose>
                                <c:when test="${hostedFieldsEnable || payPalStandardEnabled}">
                                        <input id="paymentMethodApplePay" type="radio"
                                            name="paymentMethodSelection" value="applePay"
                                            class="paypalselection"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="onlyApplePaySelected" value="true" />
                                </c:otherwise>
                            </c:choose>
                            <div class="applepay-image-container" id="applepay-container">
                                <div class="cmsimage">
                                    <img class="apple-pay-image" src="${contextPath}/_ui/addons/braintreeb2baddon/responsive/common/images/apple-pay.png"
                                        alt="Buy with ApplePay" />
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div style="overflow: auto;"></div>
                    </c:otherwise>
                </c:choose>
                <br>
                <c:choose>
			<c:when test="${payPalStandardEnabled}">
				<div style="overflow: auto;" id="paypal">
				<c:choose>
				<c:when test="${hostedFieldsEnable || applePayEnabled}">
					<input id="paymentMethodPayPal" type="radio"
													name="paymentMethodSelection" value="paypal"
													class="paypalselection" checked="true" />
						</c:when>
						<c:otherwise>
						<input type="hidden" name="onlyPayPalSelected" value="true" />
						</c:otherwise>
						</c:choose>
							<div class="paypalimage" id="paypal-container">
								<div class="cmsimage">
									<img
													src="https://www.paypalobjects.com/webstatic/en_US/i/buttons/pp-acceptance-medium.png"
													alt="Buy now with PayPal" />
								</div>
							</div>
							<div id="text" class="paypalurl">
								<a style="padding-left: 10px;"
												href="https://www.paypal.com/webapps/mpp/paypal-popup"
												title="How PayPal Works"
												onclick="javascript:window.open('https://www.paypal.com/webapps/mpp/paypal-popup','WIPaypal','toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=1060, height=700'); return false;"><spring:theme
													code="braintree.text.what.is.paypal" />?</a>
							</div>
				</div>
				</c:when>
				<c:otherwise>
					<div style="overflow: auto;"></div>
				</c:otherwise>
		</c:choose>
				<br>
		<c:choose>
				<c:when test="${hostedFieldsEnable}">
				<div style="overflow: auto;" id="braintree-container">
				<c:if test="${payPalStandardEnabled || applePayEnabled}">
					<input id="paymentMethodBT" type="radio"
												name="paymentMethodSelection" value="bt"
												class="paypalselection" />
						</c:if>
					<c:if test="${not empty paymentsImagesURL}">
						<c:forEach items="${paymentsImagesURL}" var="url">
							<img src="${url.value}" alt="${url.key}" />
						</c:forEach>
					</c:if>
					<input type="hidden" value="false" class="text"
											name="paypal_is_valid" id="paypal_is_valid">
				</div>
				</c:when>
				<c:otherwise>
					<div style="overflow: auto;"></div>
				</c:otherwise>
		</c:choose>

							    <ycommerce:testId code="paymentDetailsForm">

						<form:form id="braintree-payment-form" name="silentOrderPostForm"
													class="create_update_payment_form"

									modelAttribute="sopPaymentDetailsForm"
									action="${request.contextPath}/braintree/checkout/hop/response"
									method="POST">
							<div class="hostedFields">
								<div class="headline">
									<spring:theme
												code="checkout.multi.paymentMethod.addPaymentDetails.paymentCard" />
								</div>
								<div class="description">
									<spring:theme
												code="checkout.multi.paymentMethod.addPaymentDetails.enterYourCardDetails" />
								</div>
								<div class="control-group cardForm" style="dispaly: none;" id="cardForn">
                                   <label for="cardholderName" class="control-label ">
                                        <spring:theme code="braintree.text.cc.cardholder" />
                                     </label>
                                    <div class="controls" >
                                        <input id="cardholderName" value="" maxlength="175"/>
                                    </div>
									<label for="number" class="control-label ">
									<spring:theme code="braintree.text.cc.number" /></label>
									<div id="number" class="controls"></div>
									<label for="cvv" class="control-label "><spring:theme
													code="braintree.text.cc.cvv" /></label>
									<div id="cvv" class="controls"></div>
									<label for="expiration-date" class="control-label "><spring:theme
													code="braintree.text.cc.expiration.date" /></label>
									<div id="expiration-date" class="controls"></div>
								</div>
							</div>
							<br />
							<div id="savePaymentButton">
							<c:if test="${not empty braintreePaymentInfos}">
								<div class="form-group">
									<button type="button" class="btn btn-default btn-block js-saved-payments">
									    <spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.useSavedCard"/>
									</button>
								</div>
							</c:if>
							</div>
							<c:if test="${(payPalCheckoutData.storeInVault eq 'onSuccess' || payPalCheckoutData.storeInVault) && payPalCheckoutData.intent ne 'order'}">
								<div class="form-additionals" id="savePaymentInfoComponent">
								<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
									<formElement:formCheckbox idKey="savePaymentInfo"
										labelKey="checkout.multi.sop.savePaymentInfo"
										path="savePaymentInfo" inputCSS="" labelCSS=""
										mandatory="false" />
								</sec:authorize>
							</div>
							<script>
                                if (document.getElementById("savePaymentInfo")){
                                    document.getElementById("savePaymentInfo").checked = true;
                                    document.getElementById("savePaymentInfo").disabled = true;
                                }
                            </script>
                            </c:if>
							<div class="billingAddressData">
							<div class="headline clear">
								<spring:theme
											code="checkout.multi.paymentMethod.addPaymentDetails.billingAddress" />
							</div>

							<c:if test="${cartData.deliveryItemsQuantity > 0}">
								<div id="useDeliveryAddressData"
											data-firstname="${deliveryAddress.firstName}"
											data-lastname="${deliveryAddress.lastName}"
											data-line1="${deliveryAddress.line1}"
											data-line2="${deliveryAddress.line2}"
											data-town="${deliveryAddress.town}"
											data-postalcode="${deliveryAddress.postalCode}"
											data-countryisocode="${deliveryAddress.country.isocode}"
											data-regionisocode="${deliveryAddress.region.isocodeShort}"
											data-address-id="${deliveryAddress.id}"></div>
								<formElement:formCheckbox path="useDeliveryAddress"
											idKey="useDeliveryAddress"
											labelKey="checkout.multi.sop.useMyDeliveryAddress"
											tabindex="11" />
							</c:if>

							<input type="hidden" name="paypal_email" id="paypal_email" />
							<input type="hidden"
										value="${silentOrderPageData.parameters['billTo_email']}"
										class="text" name="billTo_email" id="billTo_email">
							<address:billAddressFormSelector
										supportedCountries="${countries}" regions="${regions}"
										tabindex="12" />
							</div>
							<div class="form-additionals"></div>

							<p>
										<spring:theme
											code="checkout.multi.paymentMethod.seeOrderSummaryForMoreInformation" />
							</p>

							<button type="submit" id="submit_silentOrderPostForm"
										class="btn btn-primary btn-block checkout-next">
										<spring:theme code="checkout.multi.paymentMethod.continue"
											text="Next" />
							</button>

							<div id="mark-paypal-button" class="paypal_button_container btn btn-block"></div>

						</form:form>
							</ycommerce:testId>

                         </div>
                    </div>

				<c:if test="${not empty braintreePaymentInfos}">
					<div id="savedpayments">
						<div id="savedpaymentstitle">
							<div class="headline">
								<span class="headline-text"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.useSavedCard"/></span>
							</div>
						</div>
						<div id="savedpaymentsbody">
							<c:forEach items="${braintreePaymentInfos}" var="paymentInfo"
								varStatus="status">
								<form
									action="${request.contextPath}/checkout/multi/payment-method/choose"
									method="GET">
									<input type="hidden" name="selectedPaymentMethodId"
										value="${paymentInfo.id}" />
										<c:choose>
											<c:when
											test="${paymentInfo.subscriptionId eq 'BrainTreePayPalExpress' or paymentInfo.subscriptionId eq 'PayPalAccount'}">
												<spring:theme code="paymentMethod.type.PayPal" /><br />
												${fn:escapeXml(paymentInfo.payer)}<br />
												<img
												src="https://www.paypalobjects.com/webstatic/en_US/i/buttons/pp-acceptance-medium.png"
												alt="PayPal icon" />
											<br />
											</c:when>
											<c:otherwise>
						                        <c:if test="${not empty paymentInfo.cardholderName}">
						                            ${fn:escapeXml(paymentInfo.cardholderName)}<br/>
						                        </c:if>
												${fn:escapeXml(paymentInfo.cardType)}<br/>
												${fn:escapeXml(paymentInfo.cardNumber)}<br/>
												<c:if test="${not empty paymentInfo.expiryMonth}">
												<spring:theme
													code="checkout.multi.paymentMethod.paymentDetails.expires"
													arguments="${fn:escapeXml(paymentInfo.expiryMonth)},${fn:escapeXml(paymentInfo.expiryYear)}" />
												<br />
												</c:if>
												<c:if test="${not empty paymentInfo.accountHolderName}">
													<img src="${fn:escapeXml(paymentInfo.accountHolderName)}"
													alt="Card Type" />
												<br />
												</c:if>
											</c:otherwise>
										</c:choose>
											<strong>${fn:escapeXml(paymentInfo.billingAddress.firstName)}&nbsp; ${fn:escapeXml(paymentInfo.billingAddress.lastName)}</strong><br />
											${fn:escapeXml(paymentInfo.billingAddress.line1)}&nbsp;
											<c:if test="${not empty paymentInfo.billingAddress.line2}">
												${fn:escapeXml(paymentInfo.billingAddress.line2)}
											</c:if><br />
											${fn:escapeXml(paymentInfo.billingAddress.town)}&nbsp; ${fn:escapeXml(paymentInfo.billingAddress.region.isocodeShort)}<br />
											${fn:escapeXml(paymentInfo.billingAddress.postalCode)}&nbsp; ${fn:escapeXml(paymentInfo.billingAddress.country.isocode)}<br />
										<button type="submit" class="btn btn-primary btn-block"
										tabindex="${(status.count * 2) - 1}">
										<spring:theme
											code="checkout.multi.paymentMethod.addPaymentDetails.useThesePaymentDetails" />
									</button>
								</form>
							</c:forEach>
						</div>
					</div>
				</c:if>

		   </jsp:body>
		</multiCheckout:checkoutSteps>
	</div>

	<div class="col-sm-6 hidden-xs">
		<multiCheckout:checkoutOrderDetails cartData="${cartData}" showDeliveryAddress="true" showPaymentInfo="false" showTaxEstimate="false" showTax="true" />
    </div>

    <div class="col-sm-12 col-lg-12">
        <cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help">
            <cms:component component="${feature}"/>
        </cms:pageSlot>
    </div>
</div>

    <spring:eval expression="@configurationService.configuration.getProperty('braintree.user.action')" var="userAction"/>
	<script>
		var paymentMethodsPage = "paymentMethodsPage";

		var clientToken = "${client_token}";
		var isCreditCardSelect = "${is_credit_card_select}";
		var isSingleUseSelect = "${is_single_use_select}";

		var advancedFraudToolsEnabled = "${payPalCheckoutData.advancedFraudTools}";
		var environment = "${payPalCheckoutData.environment}";
		var secure3d = "${payPalCheckoutData.secure3d}";
		var skip3dSecureLiabilityResult = "${payPalCheckoutData.skip3dSecureLiabilityResult}";
		var dbaName = "${payPalCheckoutData.dbaName}";

		// only paypal specific configuration options
		var storeInVault = "${payPalCheckoutData.storeInVault}";
		var paypalIntent = "${payPalCheckoutData.intent}";
		var amount = "${payPalCheckoutData.amount}";
		var locale = "${payPalCheckoutData.locale}";
        var enableShippingAddress = "${payPalCheckoutData.enableShippingAddress}";
		var braintreeLocale = "${braintreeLocale}";
		var currency = "${payPalCheckoutData.currency}";
		var recipientName = "${payPalCheckoutData.shippingAddressOverride.recipientName}";
		var streetAddress = "${payPalCheckoutData.shippingAddressOverride.streetAddress}";
		var extendedAddress = "${payPalCheckoutData.shippingAddressOverride.extendedAddress}";
		var locality = "${payPalCheckoutData.shippingAddressOverride.locality}";
		var countryCodeAlpha2 = "${payPalCheckoutData.shippingAddressOverride.countryCodeAlpha2}";
		var postalCode = "${payPalCheckoutData.shippingAddressOverride.postalCode}";
		var region = "${payPalCheckoutData.shippingAddressOverride.region}";
		var phone = "${payPalCheckoutData.shippingAddressOverride.phone}";
        var billingAgreementDescription = "${billingAgreementDescription}";
    	var userAction="${userAction}";
		var payPalStandardEnabled=${payPalStandardEnabled};
		var applePayEnabled = ${payPalCheckoutData.applePayEnabled};
        var payPalMarkButtonConfig = "${payPalMarkButtonConfig}";
	</script>
	<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/client.min.js"></script>
	<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/data-collector.min.js"></script>
	<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/hosted-fields.min.js"></script>
	<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/paypal.min.js"></script>
	<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/apple-pay.min.js"></script>
	<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/three-d-secure.min.js"></script>
	<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/paypal-checkout.min.js"></script>
	<script type="text/javascript" src="https://www.paypalobjects.com/api/checkout.min.js"></script>

</template:page>
