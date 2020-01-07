<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<jsp:include page="../../../messages/braintreeErrorMessages.jsp" />
<spring:eval expression="@configurationService.configuration.getProperty('braintree.store.in.vault')" var="storeInVault"/>
<div class="container">
	<div class="account-section-content	 account-section-content-small">
		<div class="account-addressbook">
		<c:if test="${(payPalConfigurationData.storeInVault eq 'onSuccess' || payPalConfigurationData.storeInVault) && payPalConfigurationData.intent ne 'order'}">
			<c:if test="${empty selectedAddressCode}">
				<spring:theme code="account.payment.selectAddress" text="Please select Billing Address for Payment Method"/></div>
				<br/>
			</c:if>
			<c:if test="${empty deliveryAddresses}">
				<br/>
				<div class="emptyMessage"><spring:theme code="account.payment.noAddress" text="No Saved Addresses"/></div>
				<div class="col-xs-12 col-sm-6 col-md-5 accountAddAddress">
					<a href="add-address" class="btn btn-primary btn-block"><spring:theme code="text.account.addressBook.addAddress" text="Add New Address"/></a>
				</div>
			</c:if>
			<c:if test="${empty selectedAddressCode}">
				<c:if test="${not empty deliveryAddresses}">
					<button id="viewAddressBook" class="btn btn-default js-address-book" type="button"> Address Book </button>
					<div id="savedAddressListHolder" class="clear">
						<div id="addressbook">
							<c:forEach items="${deliveryAddresses}" var="deliveryAddress" varStatus="status">
								<div class="addressEntry">
									<form action="${request.contextPath}/my-account/add-payment-method" method="GET">
										<input type="hidden" name="selectedAddressCode" value="${deliveryAddress.id}" />
										<ul>
											<li>
												<strong>${fn:escapeXml(deliveryAddress.title)}&nbsp;
														${fn:escapeXml(deliveryAddress.firstName)}&nbsp;
														${fn:escapeXml(deliveryAddress.lastName)}</strong>
												<br>
													${fn:escapeXml(deliveryAddress.line1)}&nbsp;
													${fn:escapeXml(deliveryAddress.line2)}
												<br>
													${fn:escapeXml(deliveryAddress.town)}
												<c:if test="${not empty deliveryAddress.region.name}">
													&nbsp;${fn:escapeXml(deliveryAddress.region.name)}
												</c:if>
												<br>
													${fn:escapeXml(deliveryAddress.country.name)}&nbsp;
													${fn:escapeXml(deliveryAddress.postalCode)}
											</li>
										</ul>
										<button type="submit" class="btn btn-primary btn-block">
											<spring:theme code="account.payment.address.useThisAddress" text="Use this Address"/>
										</button>
									</form>
								</div>
							</c:forEach>
						</div>
					</div>
				</c:if>
			</c:if>

			<c:if test="${not empty selectedAddressCode}">

				<div class="account-section-header">
					<spring:theme code="account.payment.address.addPaymentMethod.title" text="Add Payment Method"/>
				</div>
				<br>
				<c:choose>
					<c:when test="${payPalStandardEnabled}">
						<div style="overflow: auto;" id="paypal">
							<c:choose>
								<c:when test="${hostedFieldsEnable}">
									<input id="paymentMethodPayPal" type="radio"
										   name="paymentMethodSelection" value="paypal"
										   class="paypalselection" checked="true" />
								</c:when>
								<c:otherwise>
									<input type="hidden" name="onlyPayPalSelected" value="true"/>
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
							<c:if test="${payPalStandardEnabled}">
								<input id="paymentMethodBT" type="radio" name="paymentMethodSelection" value="bt"
									   class="paypalselection"/>
							</c:if>
							<c:if test="${not empty paymentsImagesURL}">
								<c:forEach items="${paymentsImagesURL}" var="url">
									<img src="${url.value}" alt="${url.key}" />
								</c:forEach>
							</c:if>
							<input type="hidden" value="false" class="text" name="paypal_is_valid" id="paypal_is_valid">
						</div>
					</c:when>
					<c:otherwise>
						<div style="overflow: auto;"></div>
					</c:otherwise>
				</c:choose>
				<ycommerce:testId code="paymentDetailsForm">
					<div class="account-section-content">
						<form:form id="braintree-payment-form"
								   action="${request.contextPath}/my-account/add-payment-method" method="POST">
							<div class="hostedFields">
								<div class="account-section-header">
									<spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentCard" text="Card Details"/>
								</div>
								<div class="description">
									<spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.enterYourCardDetails" text="Please enter your card details for payment"/></br>
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
									<label for="cvv" class="control-label ">
										<spring:theme code="braintree.text.cc.cvv" /></label>
									<div id="cvv" class="controls"></div>
									<label for="expiration-date" class="control-label ">
										<spring:theme code="braintree.text.cc.expiration.date" /></label>
									<div id="expiration-date" class="controls"></div>
								</div>
							</div>
							<br/>

							<input type="hidden" name="paypal_email" id="paypal_email"/>
							<input type="hidden" name="selectedAddressCode" id="selectedAddressCode" value="${selectedAddressCode}"/>
							<div class="form-additionals"/>
							<div class="form-actions">
									<div class="col-md-2 col-lg-3">
										<c:url value="/my-account/payment-details"  var="accountPaymentMethodUrl" />
										<a class="btn btn-block btn-default" href="${accountPaymentMethodUrl}">
											<spring:theme code="account.add.paymentMethod.cancel" text="Cancel" />
										</a>
									</div>
									<div class="col-md-2 col-lg-3">
										<button class="btn btn-primary btn-block" id="submit_silentOrderPostForm" type="submit">
											<spring:theme code="account.add.paymentMethod.save" text="Save" />
										</button>
										<div id="mark-paypal-button" class="paypal_button_container btn btn-block"></div>
									</div>
							</div>
						</form:form>
					</div>
					</div>
				</ycommerce:testId>
			</c:if>
			</c:if>
		</div>
	</div>
</div>

<script src="https://js.braintreegateway.com/js/braintree-2.26.0.min.js"></script>
<spring:eval expression="@configurationService.configuration.getProperty('braintree.user.action')" var="userAction"/>

<script>
    var addPaymentMethodsPage = "addPaymentMethodsPage";
    var clientToken = "${client_token}";
    var isCreditCardSelect = "${is_credit_card_select}";
    var isSingleUseSelect = "${is_single_use_select}";
    var advancedFraudToolsEnabled = "${payPalConfigurationData.advancedFraudTools}";
    var environment = "${payPalConfigurationData.environment}";
    var secure3d = "${payPalConfigurationData.secure3d}";
    var skip3dSecureLiabilityResult = "${payPalConfigurationData.skip3dSecureLiabilityResult}";
    var dbaName = "${payPalConfigurationData.dbaName}";
    var storeInVault = "${payPalConfigurationData.storeInVault}";
    var locale = "${payPalConfigurationData.locale}";
    var braintreeLocale = "${payPalCheckoutData.braintreeLocale}";
    var billingAgreementDescription = "${billingAgreementDescription}";
    var paypalIntent = "${payPalConfigurationData.intent}";
    var userAction="${userAction}";
    var payPalStandardEnabled = ${payPalStandardEnabled};
    var payPalMarkButtonConfig = "${payPalMarkButtonConfig}";

</script>
<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/client.min.js"></script>
<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/data-collector.min.js"></script>
<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/hosted-fields.min.js"></script>
<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/paypal.min.js"></script>
<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/three-d-secure.min.js"></script>
<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/paypal-checkout.min.js"></script>
<script type="text/javascript" src="https://www.paypalobjects.com/api/checkout.min.js"></script>
