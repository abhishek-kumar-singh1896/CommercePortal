<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<jsp:include page="../../../messages/braintreeErrorMessages.jsp" />
<c:url value="/cart/checkout" var="checkoutUrl" scope="session"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-md-7 col-lg-6 pull-right cart-actions--print">
        <div class="express-checkout">
            <div class="headline"><spring:theme code="text.expresscheckout.header"/></div>
            <strong><spring:theme code="text.expresscheckout.title"/></strong>
            <ul>
                <li><spring:theme code="text.expresscheckout.line1"/></li>
                <li><spring:theme code="text.expresscheckout.line2"/></li>
                <li><spring:theme code="text.expresscheckout.line3"/></li>
            </ul>
            <sec:authorize access="isFullyAuthenticated()">
                <c:if test="${expressCheckoutAllowed}">
                    <div class="checkbox">
                        <label>
                            <c:url value="/checkout/multi/express" var="expressCheckoutUrl" scope="session"/>
                            <input type="checkbox" class="express-checkout-checkbox" data-express-checkout-url="${expressCheckoutUrl}">
                            <spring:theme text="I would like to Express checkout" code="cart.expresscheckout.checkbox"/>
                        </label>
                     </div>
                </c:if>
           </sec:authorize>
        </div>
		<c:choose>
			<c:when test="${payPalExpressEnabled}">
                <spring:eval expression="@configurationService.configuration.getProperty('braintree.user.action')" var="userAction"/>
				<script>
					var shoppingCart = "shoppingCart";
					var clientTokenForPaypal = "${client_token}";
					var storeInVault = "${payPalCheckoutData.storeInVault}";
					var paypalIntent = "${payPalCheckoutData.intent}";
					var amount = "${payPalCheckoutData.amount}";
                    var enableShippingAddress = "${payPalCheckoutData.enableShippingAddress}";
					var locale = "${payPalCheckoutData.locale}";
					var braintreeLocale = "${braintreeLocale}";
					var currency = "${payPalCheckoutData.currency}";
					var advancedFraudToolsEnabled = "${payPalCheckoutData.advancedFraudTools}";
					var dbaName = "${payPalCheckoutData.dbaName}";
					var contextPath = "${request.contextPath}";
                    var billingAgreementDescription = "${billingAgreementDescription}";
                	var userAction="${userAction}";
                	var applePayEnabled = ${payPalCheckoutData.applePayEnabled};
                    var payPalButtonConfig = "${payPalButtonConfig}";
				</script>
				<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/client.min.js"></script>
				<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/data-collector.min.js"></script>
				<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/paypal-checkout.min.js"></script>
				<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/apple-pay.min.js"></script>
				<script type="text/javascript" src="https://www.paypalobjects.com/api/checkout.min.js" data-version-4></script>
				
           	    <sec:authorize access="hasAnyRole('ROLE_ANONYMOUS')">
           	    	<script>
               			isAnonymous=true;
               		</script>
               	</sec:authorize>
				<div class="row cart-actions">
					<div id="paypal_express_error bt_right"></div>

				</div>
				<div class="row cart-actions">
					<div class="col-sm-6">
					
					</div>
					<div class="col-sm-6">
						<div class="bt_center">
                              <c:choose>
                                  <c:when test="${payPalCheckoutData.applePayEnabled}">
                                      <div id="apple-pay-button" class="apple-pay-button apple-pay-button-container hide">
                                              <img src="${contextPath}/_ui/addons/braintreeb2baddon/responsive/common/images/apple-pay.png"
                                              class="apple-pay-button"
                                              alt="Buy with Apple Pay" />
                                      </div>
                                  </c:when>
                              </c:choose>
							<div id="paypal-button" class="paypal-button paypal_button_container"></div>
						</div>
						<div class="orSeparator bt_center">
							<spring:theme code="braintree.cart.or" />
						</div>
					</div>
				</div>
<div class="cart-actions">
            <div class="row">
                <div class="col-sm-3 col-md-4 pull-right">
                    <button class="btn btn-primary btn-block btn--continue-checkout js-continue-checkout-button"  data-checkout-url="${checkoutUrl}"><spring:theme code="checkout.checkout"/></button>
                </div>

                <div class="col-sm-5 pull-right">
                    <button class="btn btn-default btn-block btn--continue-shopping js-continue-shopping-button" data-continue-shopping-url="${continueShoppingUrl}"><spring:theme text="Continue Shopping" code="cart.page.continue"/></button>
                </div>
                <cart:saveCart/>
            </div>
        </div>

			</c:when>
			<c:otherwise>
<div class="cart-actions">
            <div class="row">
                <div class="col-sm-3 col-md-4 pull-right">
                    <button class="btn btn-primary btn-block btn--continue-checkout js-continue-checkout-button"  data-checkout-url="${checkoutUrl}"><spring:theme code="checkout.checkout"/></button>
                </div>

                <div class="col-sm-5 pull-right">
                    <button class="btn btn-default btn-block btn--continue-shopping js-continue-shopping-button" data-continue-shopping-url="${continueShoppingUrl}"><spring:theme text="Continue Shopping" code="cart.page.continue"/></button>
                </div>
                <cart:saveCart/>
            </div>
        </div>

			</c:otherwise>
		</c:choose>
	</div>
</div>

<c:if test="${showCheckoutStrategies && not empty cartData.entries}" >
    <div class="cart-actions">
        <div class="row">
            <div class="col-xs-12 col-sm-5 col-md-3 col-lg-2 pull-right">
                <input type="hidden" name="flow" id="flow"/>
                <input type="hidden" name="pci" id="pci"/>
                <select id="selectAltCheckoutFlow" class="doFlowSelectedChange form-control">
                    <option value="multistep"><spring:theme code="checkout.checkout.flow.select"/></option>
                    <option value="multistep"><spring:theme code="checkout.checkout.multi"/></option>
                    <option value="multistep-pci"><spring:theme code="checkout.checkout.multi.pci"/></option>
                </select>
                <select id="selectPciOption" class="display-none">
                    <option value=""><spring:theme code="checkout.checkout.multi.pci.select"/></option>
                    <c:if test="${!isOmsEnabled}">
                        <option value="default"><spring:theme code="checkout.checkout.multi.pci-ws"/></option>
                        <option value="hop"><spring:theme code="checkout.checkout.multi.pci-hop"/></option>
                    </c:if>
                    <option value="sop"><spring:theme code="checkout.checkout.multi.pci-sop" text="PCI-SOP" /></option>
                </select>
            </div>
        </div>
    </div>
</c:if>

