var HTML = {
    DIV: "<div>",
    BR: "<br/>",
    IMG: "<img/>",
    INPUT: "<input>",
    FORM: "<form>",
    CLASS: "class",
    SRC: "src",
    ALT: "alt",
    SCRIPT: "script",
    TYPE_TEXT: "text/javascript",
    HEAD: "head",
    CURSOR: "cursor",
    POINTER: "pointer",
    CHECKED: ":checked",
    TYPE: "type",
    FOOTER: "footer"
};
var CONST = {
    ADD_TO_CART_BUTTON: ".addToCartButton",
    ADD_TO_CART_LAYER: "#cboxContent",
    ADD_TO_CART_DIV: ".addtocart",
    PAYPAL_CHECKOUT_IMAGE: ".payPalCheckoutAddToCartImage",
    SINGLE_USE_CHECKBOX: "#singleUseCheckboxAddToCart",
    MINI_CART: ".miniCart",
    MINI_CART_LAYER: "#miniCartLayer",
    PAYPAL_CHECKOUT_MINI_IMAGE: ".payPalCheckoutMiniCartImage",
    SINGLE_USE_MINI_CHECKBOX: "#singleUseCheckboxMiniCart",
    FOOTER: ".footer",
    PAYMENT_OPTIONS: "paymentOptions",
    PAYMENT_OPTIONS_CLASS: ".paymentOptions",
    FOOTER_IMG_SRC: "https://www.paypalobjects.com/webstatic/en_US/i/buttons/cc-badges-ppmcvdam.png",
    PAYMENT_OPTIONS_TEXT: "Payment options",
    GLOBAL_MESSAGES: "#globalMessages",
    VALIDATION_CLASS: ".bt_validation",
    ERROR_DIV: "<div class='alert alert-danger'>",
    PAYMENT_MEHTOD_SECTION: "[name=paymentMethodSelection]",
    BT_LIBS: ["https://js.braintreegateway.com/web/3.38.0/js/client.js",
        "https://js.braintreegateway.com/web/3.38.0/js/data-collector.js",
        "https://js.braintreegateway.com/web/3.38.0/js/paypal-checkout.js",
        "https://js.braintreegateway.com/web/3.38.0/js/apple-pay.min.js",
        "https://www.paypalobjects.com/api/checkout.js"],
    HOSTED_FIELDS: ".hostedFields",
    BILLING_ADDRESS_DATA: ".billingAddressData",
    CREADIT_CARD_LABEL_ID: "#creditCardLabelId",
    HEADLINE: 'headline',
    DISABLE_CLICK: 'disableClick',
    PAYPAL: 'paypal',
    APPLEPAY_VAL: "applePay",
    SELECT_PAYMENT: '#select_payment',
    PAYMENT_METHOD_PAYPAL: '#paymentMethodPayPal',
    PROP_CHECKED: 'checked',
    PAYMENT_TYPE: "payment_type",
    CARD_TYPE: "card_type",
    CARD_DETAILS: "card_details",
    PAYMENT_METHOD_NONCE: "bt_payment_method_nonce",
    ATTR_ACTION: "action",
    ATTR_METHOD: "method",
    ATTR_NAME: "name",
    PAYPAL_CMS_IMAGE_SELECTOR: '.paypalimage .cmsimage img',
    PAYPAL_CHECKOUT_IMAGE_SELECTOR: '.payPalCheckoutImage .cmsimage img',
    LIABILITY_SHIFTED: "liability_shifted",
    ERROR_ALERT_NEGATIVE_CSS_CLASS: 'alert alert-danger alert-dismissable',
    FORM_METHOD_POST: "POST",
    BRAINTREE_PAYMENT_FORM_ID: 'braintree-payment-form',
    SUBMIT_CILENT_ORDER_POST_FORM_ID: '#submit_silentOrderPostForm',
    SAVE_PAYMENT_INFO_ID: '#savePaymentInfo',
    SINGLE_USE_CHECKBOX_ID: '#singleUseCheckbox',
    ALERT_POSITIVE_BT_VALIDATION_CSS_CLASS: 'alert positive bt_validation',
    NUMBER_ID: "#number",
    EXPIRATION_DATE_ID: "#expiration-date",
    CVV_ID: "#cvv",
    PAYMENT_METHOD_BT_ID: '#paymentMethodBT',
    USE_DELIVERY_ADDRESS_ID: '#useDeliveryAddress',
    PAYPAL_EXPRESS_FORM_NAME: "payPalExpress",
    CUSTOM_SINGLE_USE_CHECKBOX_ID: '#customSingleUseCheckbox',
    PAYPAL_IS_VALID_ID: '#paypal_is_valid',
    PAYPAL_EMAIL_ID: '#paypal_email',
    PAYPAL_EXPRESS_ERROR_ID: '#paypal_express_error',
    CARD_HOLDER_ID: "#cardholderName",
    CONTAINER_CLASS: ".container-fluid",
    PAYPAL_VAL: "paypal",
    BT_VAL: "bt",
    PAYPAL_BUTTON_CONTAINER: ".paypal-button-in-popup",
    PAYPAL_BUTTON_CART_CONTAINER: ".paypal-button-in-cart-popup",
    PAYPAL_BUTTON: ".paypal-button",
    MARK_PAYPAL_BUTTON: "#mark-paypal-button",
    VAULT_FLOW: "vault",
    CHECKOUT_FLOW: "checkout",
    TRUE: "true",
    ON_SUCCESS: "onSuccess",
    INTENT_SALE: "sale",
    INTENT_ORDER: "order",
    APPLE_PAY_BUTTON_SELECTOR: ".apple-pay-button",
    HIDE: "hide",
    PAYMENT_METHOD_APPLE_PAY: "#paymentMethodApplePay",
    APPLE_PAY_BUTTON_CONTAINER: ".apple-pay-button-container",
    APPLEPAY_SELECTOR: "#applepay",
    INTENT_AUTHORIZE: "authorize"
};
var EVENTS = {
    HOVER: "hover",
    CLICK: "click",
    CHANGE: "change",
}

jQuery(document).ready(function ($) {

    var deviceData;
    var client;
    var paymentMethodResponse;

    // add payment method images to footer section for all pages
    addPaymentMethodImagesToFooter();

    if (isAvailableApplePay()) {
        showApplePay();
        disableApplePay();
    } else {
        $(CONST.APPLE_PAY_BUTTON_CONTAINER).remove();
    }

    // PayPal along with Hosted Fields checkout configuration
    if ((typeof paymentMethodsPage != 'undefined')) {
        configurePayPalAlongWithHostedFields();
    }

    if ((typeof addPaymentMethodsPage != 'undefined')) {
        enableShippingAddress = "false";
        configurePayPalAlongWithHostedFields();
    }

    // PayPal Shopping Cart Shortcut checkout configuration
    if (typeof shoppingCart != 'undefined' && shoppingCart != '') {
        configurePaypalCartShortcut(CONST.PAYPAL_BUTTON);
    }

    // PayPal Mini Cart Shortcut checkout configuration
    if ($(CONST.MINI_CART).length) {
        configurePaypalMiniCartShortcut(CONST.PAYPAL_BUTTON_CART_CONTAINER);
    }

    $(CONST.PAYMENT_METHOD_BT_ID).change(function () {
        initializeBTclientSDK();
    });

    $(CONST.PAYMENT_METHOD_PAYPAL).change(function () {
        initializeBTclientSDK();
    });

    $(CONST.PAYMENT_METHOD_APPLE_PAY).change(function () {
        initializeBTclientSDK();
    });
});

function showApplePay() {
    $(CONST.APPLE_PAY_BUTTON_CONTAINER).removeClass(CONST.HIDE);
}

function disableApplePay() {
    showApplePay();
    $(CONST.APPLE_PAY_BUTTON_SELECTOR).addClass("disabled-button");
    $(CONST.APPLE_PAY_BUTTON_SELECTOR).unbind('click');
}

function enableApplePay() {
    showApplePay();
    $(CONST.APPLE_PAY_BUTTON_SELECTOR).removeClass("disabled-button");
}

function hideApplePay() {
    $(CONST.APPLE_PAY_BUTTON_SELECTOR).addClass(CONST.HIDE);
}

function isAvailableApplePay() {
    return typeof window.ApplePaySession != "undefined" && ApplePaySession.canMakePayments();
}

function addPaymentMethodImagesToFooter() {
    var paymentOptionsTextDiv = $(HTML.DIV).attr(HTML.CLASS, CONST.PAYMENT_OPTIONS);
    var br = $(HTML.BR);
    $(CONST.FOOTER).prepend(br);
    $(CONST.FOOTER).prepend(br);
    $(CONST.FOOTER).prepend(paymentOptionsTextDiv);
    var paymentOptionsImage = $(HTML.IMG);
    paymentOptionsImage
        .attr(
            HTML.SRC,
            CONST.FOOTER_IMG_SRC);
    paymentOptionsImage.attr(HTML.ALT, CONST.PAYMENT_OPTIONS_TEXT);
    $(HTML.FOOTER).children(CONST.CONTAINER_CLASS).append(paymentOptionsImage);
}

function verify3DSecure(clientInstance, paymentResponse) {
    configurePayPalAlongWithHostedFields();
    braintree.threeDSecure.create({
        client: clientInstance
    }, function (err, threeDSecure) {
        threeDSecure.verifyCard({
            amount: amount,
            nonce: paymentResponse.nonce,
            addFrame: function (err, iframe) {
                threeDSContainer = $(HTML.DIV);
                $(threeDSContainer).append(iframe);
                ACC.colorbox.open("", {
                    inline: true,
                    href: threeDSContainer,
                    onComplete: function () {
                        $(this).colorbox.resize();
                    }
                });
            },
            removeFrame: function () {
                ACC.colorbox.close();
            }
        }, function (error, response) {
            if (error) {
                show3DSecureMessage(error);
                return;
            } else {
                // 3DSecure finished
                // add 3DSecure returned nonce
                paymentResponse.nonce = response.nonce;
                var liabilityShifted = response.liabilityShifted;
                // allow process card if 3dSecureLiabilityResult is
                // skipped by merchant
                if (liabilityShifted || JSON.parse(skip3dSecureLiabilityResult)) {
                    paymentResponse.liabilityShifted = liabilityShifted;
                    processResponce(paymentResponse);
                } else {
                    show3DSecureMessage(ACC.addons.braintreeb2baddon['braintree.message.unsecured.card']);
                }
            }
        });
    });
}

function show3DSecureMessage(message) {
    $(CONST.GLOBAL_MESSAGES).children(CONST.VALIDATION_CLASS).remove();
    var errorComponent = CONST.ERROR_DIV + message + HTML.DIV;
    $(CONST.GLOBAL_MESSAGES).append(errorComponent);
}

function configurePayPalAlongWithHostedFields() {

    // remove paypal validation messages
    $(CONST.GLOBAL_MESSAGES).children(CONST.VALIDATION_CLASS).remove();

    // add select payment method event
    $(CONST.PAYMENT_MEHTOD_SECTION).change(function () {
        paymentMethod();
    });

    // render appropriate payment method
    selectPaymentMethod();

}

function addBrainTreeLibrary(onBrainTreeLibraryLoaded) {
    for (var i = 0; i < CONST.BT_LIBS.length - 1; i++) {
        $.getScript(CONST.BT_LIBS[i]);
    }
    $.getScript(CONST.BT_LIBS[CONST.BT_LIBS.length - 1]).done(function (script, textStatus) {
        console.log(textStatus);
        setTimeout(function () {
            onBrainTreeLibraryLoaded();
        }, 500);
    });
}

function configurePaypalMiniCartShortcut(payPalButtonClass) {
    if (isAvailableApplePay()) {
        showApplePay();
        disableApplePay();
    } else {
        $(CONST.APPLE_PAY_BUTTON_CONTAINER).remove();
    }
    if (typeof braintree == 'undefined' || braintree == '') {
        addBrainTreeLibrary(function () {
            initialisePaypal(payPalButtonClass);
        });
    } else {
        initialisePaypal(payPalButtonClass);
    }
}

function configurePaypalCartShortcut(payPalButtonClass) {
    if (typeof braintree == 'undefined' || braintree == '') {
        addBrainTreeLibrary(function () {
            initialisePaypal(payPalButtonClass);
        });
    } else {
        initialisePaypal(payPalButtonClass);
    }
}

function paymentMethod() {
    if (isPayPalMethodSelected()) {
        $(CONST.MARK_PAYPAL_BUTTON).show();
        $(CONST.SUBMIT_CILENT_ORDER_POST_FORM_ID).hide();
        $(CONST.HOSTED_FIELDS).hide();
        $(CONST.BILLING_ADDRESS_DATA).hide();
        $(CONST.CREADIT_CARD_LABEL_ID).removeClass(CONST.HEADLINE);
        $(CONST.PAYPAL_CMS_IMAGE_SELECTOR).removeClass(CONST.DISABLE_CLICK);
        $(CONST.SELECT_PAYMENT).val(CONST.PAYPAL);
        $(CONST.GLOBAL_MESSAGES).children('.bt_validation').remove();
        $("#savePaymentInfoComponent").show();
    } else if (isApplePaySelected()) {
        $(CONST.HOSTED_FIELDS).hide();
        $(CONST.BILLING_ADDRESS_DATA).hide();
        $(CONST.SUBMIT_CILENT_ORDER_POST_FORM_ID).show();
        $(CONST.MARK_PAYPAL_BUTTON).hide();
        $(CONST.CREADIT_CARD_LABEL_ID).removeClass(CONST.HEADLINE);
        $(CONST.SELECT_PAYMENT).val(CONST.APPLEPAY_VAL);
        $(CONST.GLOBAL_MESSAGES).children('.bt_validation').remove();
        $("#savePaymentInfoComponent").hide();
        $("#savePaymentInfoComponent").prop("checked", false);
    } else {
        $(CONST.HOSTED_FIELDS).show();
        $(CONST.BILLING_ADDRESS_DATA).show();
        $(CONST.SUBMIT_CILENT_ORDER_POST_FORM_ID).show();
        $(CONST.MARK_PAYPAL_BUTTON).hide();
        $(CONST.CREADIT_CARD_LABEL_ID).addClass(CONST.HEADLINE);
        $(CONST.PAYPAL_CMS_IMAGE_SELECTOR).addClass(CONST.DISABLE_CLICK);
        $(CONST.SELECT_PAYMENT).val("bt");
        $("#savePaymentInfoComponent").show();
    }
}


function selectPaymentMethod() {
    if (typeof isCreditCardSelect != 'undefined' && isCreditCardSelect != '' && isCreditCardSelect == "true") {
        $(CONST.PAYMENT_METHOD_BT_ID).prop(CONST.PROP_CHECKED, true);
        $(CONST.SUBMIT_CILENT_ORDER_POST_FORM_ID).attr('type', 'submit');
        initializeBTclientSDK();
    } else if (typeof isSingleUseSelect != 'undefined' && isSingleUseSelect != '') {
        $(CONST.PAYMENT_METHOD_PAYPAL).prop(CONST.PROP_CHECKED, true);
        // select paypal as default
    } else {
        $(CONST.PAYMENT_METHOD_PAYPAL).prop(CONST.PROP_CHECKED, true);
        $(CONST.SUBMIT_CILENT_ORDER_POST_FORM_ID).attr('type', 'button');
        initializeBTclientSDK();
    }
    paymentMethod();
}

function processResponce(responce) {

    var submitForm = $('#' + CONST.BRAINTREE_PAYMENT_FORM_ID);

    var useDelivery = createHiddenParameter("use_delivery_address", $(
        CONST.USE_DELIVERY_ADDRESS_ID).is(HTML.CHECKED));
    var paymentType = createHiddenParameter(CONST.PAYMENT_TYPE, responce.type);
    var cardType = createHiddenParameter(CONST.CARD_TYPE, responce.details.cardType);
    var cardDetails = createHiddenParameter(CONST.CARD_DETAILS,
        responce.details.lastTwo);

    var isLiabilityShifted = '';

    var paymentNonce = createHiddenParameter(CONST.PAYMENT_METHOD_NONCE,
        responce.nonce);
    submitForm.append($(paymentNonce));
    if (typeof responce.liabilityShifted != 'undefined') {
        isLiabilityShifted = responce.liabilityShifted;
    }

    var liabilityShifted = createHiddenParameter(CONST.LIABILITY_SHIFTED,
        isLiabilityShifted);
    submitForm.append($(liabilityShifted));

    // collect device data for advanced fraud tools
    var deviceData = createHiddenParameter("device_data", this.deviceData);

    var cardholder = createHiddenParameter("cardholder", $(CONST.CARD_HOLDER_ID).val());

    submitForm.append($(deviceData));
    submitForm.append($(useDelivery));
    submitForm.append($(paymentType));
    submitForm.append($(cardType));
    submitForm.append($(cardDetails));
    submitForm.append($(cardholder));

    submitForm.submit()
}

function showSuccessPaypalMessage(nonce, email) {
    if (nonce != null) {
        $(CONST.PAYPAL_IS_VALID_ID).val("true");
        var errorComponent = $(HTML.DIV).attr(HTML.CLASS, CONST.ALERT_POSITIVE_BT_VALIDATION_CSS_CLASS);
        errorComponent.prepend(ACC.addons.braintreeb2baddon['braintree.message.paypal.error'] + email);

        $(CONST.GLOBAL_MESSAGES).append(errorComponent);
        $(CONST.PAYPAL_EMAIL_ID).val(email);
    }
    $(CONST.SUBMIT_CILENT_ORDER_POST_FORM_ID).unbind(EVENTS.CLICK);
    $(CONST.SUBMIT_CILENT_ORDER_POST_FORM_ID).trigger(EVENTS.CLICK, [true]);
}

function resetHostedFields() {
    $(CONST.NUMBER_ID).empty();
    $(CONST.EXPIRATION_DATE_ID).empty();
    $(CONST.CVV_ID).empty();
    $(CONST.CARD_HOLDER_ID).empty();
}

function getPaypalFlow() {
    if (storeInVault != '' && storeInVault != undefined && (storeInVault == CONST.ON_SUCCESS || storeInVault == CONST.TRUE)) {
        return CONST.VAULT_FLOW;
    }
    return CONST.CHECKOUT_FLOW;
}

function initializeBTclientSDK() {
    var isIntentValid = checkIntentOption(paypalIntent);
    payPalMarkButtonConfigObj = eval("({" + payPalMarkButtonConfig.replace('\"', '"') + "})");

    if (!isAvailableApplePay()) {
        $(CONST.APPLEPAY_SELECTOR).remove();
    } else {
        $(CONST.APPLEPAY_SELECTOR).removeClass("hide");
    }

    var checkout;
    var paypalOptions;

    // reset hosted fields in case repeated initialization
    resetHostedFields();

    var paypalFlow = getPaypalFlow();

    var paypalOptions = {
        flow: paypalFlow,
        enableShippingAddress: JSON.parse(enableShippingAddress),
        enableBillingAddress: true,
        locale: braintreeLocale,
        shippingAddressEditable: false
    };

    if (userAction === 'true' && paypalIntent === CONST.INTENT_SALE) {
        // paypalOptions.useraction='commit';
    }

    // configure advance fraud tools
    var dataCollector = {};

    //add payment method page
    if ((typeof addPaymentMethodsPage != 'undefined')) {

        dataCollector.paypal = true;
        paypalOptions.billingAgreementDescription = billingAgreementDescription;

    } else {
        // configure paypal connections
        if (paypalFlow == CONST.CHECKOUT_FLOW) {
            if (paypalIntent != undefined && paypalIntent !== "") {
                paypalOptions.intent = paypalIntent;
            }
            paypalOptions.amount = amount;
            paypalOptions.currency = currency;
            paypalOptions.shippingAddressOverride = {
                recipientName: recipientName,
                line1: streetAddress,
                line2: extendedAddress,
                city: locality,
                countryCode: countryCodeAlpha2,
                postalCode: postalCode,
                state: region,
                phone: phone
            }
        } else if (paypalFlow == CONST.VAULT_FLOW) {
            if (paypalIntent != undefined && paypalIntent !== "") {
                paypalOptions.intent = paypalIntent;
            }
            paypalOptions.amount = amount;
            paypalOptions.currency = currency;
            paypalOptions.shippingAddressOverride = {
                recipientName: recipientName,
                line1: streetAddress,
                line2: extendedAddress,
                city: locality,
                countryCode: countryCodeAlpha2,
                postalCode: postalCode,
                state: region,
                phone: phone
            }

            paypalOptions.billingAgreementDescription = billingAgreementDescription;

            // configure advanced fraud tools only for vaulted PayPal
            if (JSON.parse(advancedFraudToolsEnabled)) {
                dataCollector.paypal = true;
            }

            // configure display name for paypal connection
            if (typeof dbaName != 'undefined' && dbaName != '') {
                if (dbaName.indexOf('*') > -1) {
                    paypalOptions.displayName = dbaName.substr(0, dbaName.indexOf('*'));
                }
            }
        }
    }

    // Configure braintree client SDK
    if ((typeof clientToken != 'undefined' && clientToken != '')
        && (typeof braintree != 'undefined' && braintree != '')) {
        braintree.client.create({
            authorization: clientToken
        }, function (clientErr, clientInstance) {
            if (clientErr) {
                handleClientError(clientErr);
                return;
            }

            if (isBrainTreeMethodSelected()) {
                createHostedFields(clientInstance);
            }

            dataCollector.client = clientInstance;
            if (JSON.parse(advancedFraudToolsEnabled)) {
                dataCollector.paypal = true;
            }
            createDataCollector(dataCollector);

            if (isIntentValid === false) {
                var globalErrorsComponent = $(CONST.MARK_PAYPAL_BUTTON);
                globalErrorsComponent.empty();
                globalErrorsComponent.append(createErrorDiv(ACC.addons.braintreeb2baddon['braintree.message.incorect.intent']))

            } else if (isPayPalMethodSelected()) {

                braintree.paypalCheckout.create({
                    client: clientInstance
                }, function (paypalCheckoutErr, paypalCheckoutInstance) {

                    var braintreeEnvironment = clientInstance.getConfiguration().gatewayConfiguration.environment;

                    var checkoutJs = {
                        style: {
                            size: 'responsive',
                            shape: 'rect'
                        }
                    };

                    $(CONST.MARK_PAYPAL_BUTTON).html('');
                    // Start Paypal Checkout.js Javascript
                    paypal.Button.render(Object.assign({
                            env: braintreeEnvironment === 'production' ? 'production' : 'sandbox',
                            payment: function (resolve, reject) {
                                console.log("paypalOptions: " + JSON.stringify(paypalOptions, null, 1));
                                paypalCheckoutInstance.createPayment(paypalOptions, function (err, res) {
                                    if (err) {
                                        handleCreatePaymentError(err);
                                        reject(err);
                                    } else {
                                        resolve(res);
                                    }
                                });
                            },
                            style: checkoutJs.style,
                            locale: braintreeLocale,
                            // commit: userAction,

                            onAuthorize: function (data, actions) {
                                actions.close();
                                return new paypal.Promise(function (resolve, reject) {
                                    return paypalCheckoutInstance.tokenizePayment(data).then(function (payload) {
                                        processPaypalResponse(payload, false);
                                    }).catch(function (tokenizeErr) {
                                        handlePayPalExpressClientError(tokenizeErr);
                                    });
                                });
                            },
                            onCancel: function (data) {
                                console.log('Cancel:', data);
                            },
                            onError: function (err) {
                                console.error('Error: ' + err, err);
                            }
                        },
                        payPalMarkButtonConfigObj), CONST.MARK_PAYPAL_BUTTON).then(function () {
                        console.log('PayPal button ready!');
                    });
                });
            }

            if (isApplePaySelected()) {
                $(CONST.SUBMIT_CILENT_ORDER_POST_FORM_ID).unbind(EVENTS.CLICK);
                createApplePay(clientInstance, paypalOptions, CONST.SUBMIT_CILENT_ORDER_POST_FORM_ID, function (payload) {
                    showSuccessPaypalMessage(payload.nonce, payload.details.email);
                    processResponce(payload);
                });
            }
        });

    } else {
        var globalErrorsComponent = $(CONST.GLOBAL_MESSAGES);
        globalErrorsComponent.empty();
        globalErrorsComponent.append(createErrorDiv(ACC.addons.braintreeb2baddon['braintree.message.use.saved.payments']));
    }
}

function isApplePaySelected() {
    return $('input[name=paymentMethodSelection]:radio:checked').val() == CONST.APPLEPAY_VAL || $('input[name=onlyApplePaySelected]').val() == 'true';
}

function isBrainTreeMethodSelected() {
    return $('input[name=paymentMethodSelection]:radio:checked').val() == CONST.BT_VAL || !payPalStandardEnabled;
}

function isPayPalMethodSelected() {
    return $('input[name=paymentMethodSelection]:radio:checked').val() == CONST.PAYPAL_VAL || $('input[name=onlyPayPalSelected]').val() == 'true';
}

function createPayPal(clientInstance, bindOnClickFunction) {
    braintree.paypal.create({
        client: clientInstance
    }, function (createErr, paypalInstance) {
        if (createErr) {
            handleClientError(createErr);
            return;
        }

        bindOnClickFunction(paypalInstance);
    });
}

function createApplePay(clientInstance, options, buttonSelector, processResponseCallback) {
    braintree.applePay.create({
        client: clientInstance
    }, function (applePayErr, applePayInstance) {
        if (applePayErr) {
            console.error('Error creating applePayInstance:', applePayErr);
            return;
        }
        var createApplePaySession = function () {
            if (typeof paymentMethodsPage != 'undefined') {
                var paymentRequest = applePayInstance.createPaymentRequest({
                    total: {
                        label: options.flow,
                        amount: amount
                    },
                    requiredShippingContactFields: ['email']
                });
            } else {
                var paymentRequest = applePayInstance.createPaymentRequest({
                    requiredBillingContactFields: ['postalAddress', 'name'],
                    requiredShippingContactFields: ['postalAddress', 'name', 'phone', 'email'],
                    total: {
                        label: options.flow,
                        amount: amount
                    }
                });
            }

            var session = new ApplePaySession(1, paymentRequest);

            session.onvalidatemerchant = function (event) {
                applePayInstance.performValidation({
                    validationURL: event.validationURL,
                    displayName: "Electronics"
                }, function (error, merchantSession) {
                    if (error) {
                        // Handle errors
                        console.error(error);
                        session.abort();
                        return;
                    }
                    session.completeMerchantValidation(merchantSession);
                });
            };
            session.onpaymentauthorized = function (event) {

                applePayInstance.tokenize({
                    token: event.payment.token
                }, function (error, payload) {
                    if (error) {
                        // Handle errors
                        console.error(error);
                        session.completePayment(ApplePaySession.STATUS_FAILURE);
                        return;
                    }

                    var shippingContact = event.payment.shippingContact;
                    var billingContact = event.payment.billingContact;

                    session.completePayment(ApplePaySession.STATUS_SUCCESS);

                    var payloadCopy = {};
                    payloadCopy.nonce = payload.nonce;
                    payloadCopy.details = {};
                    payloadCopy.type = payload.type;
                    payloadCopy.details.email = shippingContact.emailAddress;

                    if (typeof paymentMethodsPage == 'undefined') {
                        payloadCopy.details.firstName = shippingContact.givenName;
                        payloadCopy.details.lastName = shippingContact.familyName;
                        payloadCopy.details.phone = shippingContact.phoneNumber;


                        payloadCopy.details.billingAddress = {
                            line1: billingContact.addressLines[0],
                            line2: billingContact.addressLines[1],
                            city: billingContact.locality,
                            state: billingContact.administrativeArea.toUpperCase(),
                            postalCode: billingContact.postalCode,
                            countryCode: billingContact.countryCode.toUpperCase()
                        };

                        payloadCopy.details.shippingAddress = {
                            line1: shippingContact.addressLines[0],
                            line2: shippingContact.addressLines[1],
                            city: shippingContact.locality,
                            state: shippingContact.administrativeArea.toUpperCase(),
                            postalCode: shippingContact.postalCode,
                            countryCode: shippingContact.countryCode.toUpperCase()
                        };
                    }

                    processResponseCallback(payloadCopy);
//                        processApplePayResponse(payloadCopy);
//                    }
                });
            };
            session.begin();
        };

        var promise = ApplePaySession.canMakePaymentsWithActiveCard(applePayInstance.merchantIdentifier);
        promise.then(function (canMakePaymentsWithActiveCard) {
            if (!canMakePaymentsWithActiveCard) {
                disableApplePay();
                createApplePaySession = null;
                return;
            } else {
//                $(buttonSelector).removeClass(CONST.HIDE);
                enableApplePay();
            }
        });

        $(buttonSelector).unbind(EVENTS.CLICK);
        $(buttonSelector).click(function () {
            if (createApplePaySession != null) {
                createApplePaySession();
            }
        });
    });
}

function createHostedFields(clientInstance) {
    braintree.hostedFields.create({
            client: clientInstance,
            styles: {
                // Styling element state
                ":focus": {
                    "color": "blue"
                },
                ".valid": {
                    "color": "green"
                },
                ".invalid": {
                    "color": "red"
                }
            },
            fields: {
                number: {
                    selector: CONST.NUMBER_ID
                },
                expirationDate: {
                    selector: CONST.EXPIRATION_DATE_ID,
                    placeholder: "MM/YY"
                },
                cvv: {
                    selector: CONST.CVV_ID
                }
            }
        },
        function (hostedFieldsErr, hostedFieldsInstance) {
            if (hostedFieldsErr) {
                handleClientError(hostedFieldsErr);
                return;
            }

            $(CONST.SUBMIT_CILENT_ORDER_POST_FORM_ID).unbind(EVENTS.CLICK);
            // Add a click event listener to PayPal image
            $(CONST.SUBMIT_CILENT_ORDER_POST_FORM_ID).click(function () {
                // initialize paypal authorization
                hostedFieldsInstance.tokenize(function (tokenizeErr, payload) {
                    if (tokenizeErr) {
                        handleClientError(tokenizeErr)
                    } else {
                        if ((typeof secure3d != 'undefined' && JSON.parse(secure3d)) && ("CreditCard" == payload.type)) {
                            verify3DSecure(clientInstance, payload);
                        } else {
                            processResponce(payload);
                        }
                    }
                });
            });
        }
    );
}

function createDataCollector(configObj) {
    braintree.dataCollector.create(configObj, function (error, dataCollectorInstance) {
        if (error) {
            handleClientError(error);
            return;
        } else {
            deviceData = dataCollectorInstance.deviceData;
        }
    });
}

function processPaypalResponse(paypalResponse) {
    var payPalCheckoutForm = createPayPalExpressCheckoutForm(paypalResponse);
    payPalCheckoutForm.submit();
}

function processApplePayResponse(response) {
    processPaypalResponse(response, false);
}

function initialisePaypal(payPalButtonContainer) {
    var isIntentValid = checkIntentOption(paypalIntent);
    if (isIntentValid === false) {
        var payPalErrorComponent = $(CONST.PAYPAL_EXPRESS_ERROR_ID);
        payPalErrorComponent.empty();
        payPalErrorComponent.append(createErrorDiv(ACC.addons.braintreeb2baddon['braintree.message.incorect.intent']));

    } else if ((typeof clientTokenForPaypal != 'undefined' && clientTokenForPaypal != '') && (typeof braintree != 'undefined' && braintree != '')) {
        var paypalFlow = getPaypalFlow();
        var isUserActionTrue;
        payPalButtonConfig = eval("({" + payPalButtonConfig.replace('\"', '"') + "})");

        if (paypalIntent === CONST.INTENT_SALE) {
            isUserActionTrue = (userAction === 'true');
        }

        var paypalOptions = {
            flow: paypalFlow,
            enableShippingAddress: JSON.parse(enableShippingAddress),
            enableBillingAddress: true,
            locale: braintreeLocale,
        };

        var dataCollector = {};
        if (paypalFlow == CONST.CHECKOUT_FLOW) {
            // configure paypal integration
            if (paypalIntent != undefined && paypalIntent !== "") {
                paypalOptions.intent = paypalIntent;
            }
            paypalOptions.amount = amount;
            paypalOptions.currency = currency;

        } else if (paypalFlow == CONST.VAULT_FLOW) {
            paypalOptions.billingAgreementDescription = billingAgreementDescription;
        }
        // configure advanced fraud tools
        dataCollector.paypal = JSON.parse(advancedFraudToolsEnabled);

        // configure display name for paypal connection
        if (typeof dbaName != 'undefined' && dbaName != '') {
            if (dbaName.indexOf('*') > -1) {
                paypalOptions.displayName = dbaName.substr(0, dbaName.indexOf('*'));
            }
        }

        braintree.client.create({
            authorization: clientTokenForPaypal
        }, function (clientErr, clientInstance) {
            if (clientErr) {
                handleClientError(clientErr);
                return;
            }

            createDataCollector({
                client: clientInstance,
                paypal: dataCollector.paypal
            });

            braintree.paypalCheckout.create({
                client: clientInstance
            }, function (paypalCheckoutErr, paypalCheckoutInstance) {

                var braintreeEnvironment = clientInstance.getConfiguration().gatewayConfiguration.environment;

                var checkoutJs = {
                    style: {
                        size: 'medium',
                        shape: 'rect'
                    }
                };

                $(payPalButtonContainer).html('');
                // Start Paypal Checkout.js Javascript
                paypal.Button.render(Object.assign({
                        env: braintreeEnvironment === 'production' ? 'production' : 'sandbox',
                        payment: function (resolve, reject) {
                            console.log("paypalOptions: " + JSON.stringify(paypalOptions, null, 1));
                            paypalCheckoutInstance.createPayment(paypalOptions, function (err, res) {
                                if (err) {
                                    reject(err);
                                } else {
                                    resolve(res);
                                }
                            });
                        },
                        style: checkoutJs.style,
                        locale: braintreeLocale,
                        commit: isUserActionTrue,

                        onAuthorize: function (data, actions) {
                            actions.close();
                            return new paypal.Promise(function (resolve, reject) {
                                return paypalCheckoutInstance.tokenizePayment(data).then(function (payload) {
                                    processPaypalResponse(payload, false);
                                }).catch(function (tokenizeErr) {
                                    handlePayPalExpressClientError(tokenizeErr);
                                });
                            });
                        },
                        onCancel: function (data) {
                            console.log('Cancel:', data);
                        },
                        onError: function (err) {
                            console.error('Error: ' + err, err);
                        }
                    },
                    payPalButtonConfig), payPalButtonContainer).then(function () {
                    console.log('PayPal button ready!');
                });

                if (applePayEnabled) {
                    createApplePay(clientInstance, paypalOptions, CONST.APPLE_PAY_BUTTON_SELECTOR, function (payload) {
                        processApplePayResponse(payload);
                    });
                }
            });
        });
    } else {
        var payPalErrorComponent = $(CONST.PAYPAL_EXPRESS_ERROR_ID);
        payPalErrorComponent.empty();
        payPalErrorComponent.append(createErrorDiv(ACC.addons.braintreeb2baddon['braintree.message.try.refresh']));
    }
}

function initPaypalCheckoutFlow(paypalInstance, config, handlePayPalError, submitPaypalResponseFunction) {

    paypalInstance.tokenize(config, function (tokenizeErr, payload) {
        if (tokenizeErr) {
            // Handle tokenization errors or premature flow closure
            handlePayPalError(tokenizeErr);
        } else {
            submitPaypalResponseFunction(payload);
        }
    });
}

function createPayPalExpressCheckoutForm(paypalResponse) {

    var payPalForm;

    if ((typeof addPaymentMethodsPage != 'undefined')) {
        payPalForm = createForm(CONST.PAYPAL_EXPRESS_FORM_NAME, ACC.config.encodedContextPath + "/braintree/paypal/checkout/add-payment-method");
    } else {
        payPalForm = createForm(CONST.PAYPAL_EXPRESS_FORM_NAME, ACC.config.encodedContextPath + "/braintree/paypal/checkout/express");
    }

    var paymentType = createHiddenParameter("payPalData", JSON.stringify(paypalResponse));
    var token = ACC.config.CSRFToken;
    var realToken = createHiddenParameter("CSRFToken", token);

    // collect device data for advanced fraud tools
    var collectDeviceData = createHiddenParameter("device_data", this.deviceData);
    payPalForm.append($(collectDeviceData));
    payPalForm.append($(paymentType));
    payPalForm.append($(realToken));
    payPalForm.appendTo('body');

    return payPalForm;
}

function activatePaypalStandardImage() {
    $(CONST.PAYPAL_CHECKOUT_IMAGE_SELECTOR).hover(function () {
        $(this).css('cursor', 'pointer');
    });
}

function selectIsSingleCheckbox() {
    if (JSON.parse(singleUse)) {
        $(CONST.SINGLE_USE_CHECKBOX_ID).prop(CONST.PROP_CHECKED, false);
        // initialize standard paypal by braintree
        initialisePaypal(CONST.PAYPAL_BUTTON);
    } else {
        $(CONST.SINGLE_USE_CHECKBOX_ID).prop(CONST.PROP_CHECKED, true);
        // initialize standard paypal by braintree
        initialisePaypal(CONST.PAYPAL_BUTTON);
    }
}


function createForm(name, action) {
    var form = $(HTML.FORM).attr(CONST.ATTR_ACTION, action)
        .attr(CONST.ATTR_NAME, name).attr(CONST.ATTR_METHOD, CONST.FORM_METHOD_POST);
    return form;
}

function createHiddenParameter(name, value) {
    var input = $(HTML.INPUT).attr(HTML.TYPE, "hidden").attr(CONST.ATTR_NAME, name).val(
        value);
    return input;
}

function handleClientError(error) {
    if (typeof error != 'undefined' || error != 'undefined') {
        // skip validation error: use paypal method
        if ('User did not enter a payment method' != error.message) {
            var errorMsg = getErrorMessage(error.code) === 'undefined' ? ACC.addons.braintreeb2baddon['braintree.message.error.provider'] + ' ' + error.message : getErrorMessage(error.code);
            $(CONST.GLOBAL_MESSAGES).empty();
            $(CONST.GLOBAL_MESSAGES).append(createErrorDiv(createErrorDiv(errorMsg)));
        }
    }
}

function handleCreatePaymentError(error) {

    if ((typeof error != 'undefined' || error != 'undefined') && error.name == "BraintreeError") {
        var errorDetails = error.details.originalError.details.originalError.paymentResource.errorDetails;
        shippingErrorDetailFields = ["city", "state", "zip"];
        isShippingError = errorDetails.some(function (element) {
            return (shippingErrorDetailFields.includes(element.field) && element.issue == "Value is invalid");
        });
        var errorMessage;
        if (isShippingError) {

            errorMessage = JSON.stringify(errorDetails).replace('"zip"', '"postal code"');

            $.ajax({
                url: ACC.config.encodedContextPath + '/braintree/paypal/checkout/shippingAddressError',
                type: 'POST',
                data: {"errorMessage": errorMessage},
                success: function (result) {
                    document.location.replace(ACC.config.encodedContextPath + '/checkout/multi/delivery-address/add');
                }
            });

        } else {
            errorMessage = JSON.stringify(error);
            $(CONST.GLOBAL_MESSAGES).empty();
            $(CONST.GLOBAL_MESSAGES).append(createErrorDiv(createErrorDiv(errorMessage)));
        }
    }
}

function handlePayPalClientError(error) {
    if (typeof error != 'undefined' || error != 'undefined') {
        // skip validation error: use paypal method
        if ('User did not enter a payment method' != error.message && 'PAYPAL_POPUP_CLOSED' != error.code) {
            $('#paypal_express_error').empty();
            var messageText = error.message;
            if (typeof messageText === 'undefined' || messageText === 'undefined') {
                //Undefined error
            } else {
                var errorMsg = getErrorMessage(error.code) === 'undefined' ? ACC.addons.braintreeb2baddon['braintree.message.error.provider'] + ' ' + messageText : getErrorMessage(error.code);
                $(CONST.GLOBAL_MESSAGES).empty();
                $(CONST.GLOBAL_MESSAGES).append(createErrorDiv(createErrorDiv(errorMsg)));
            }
        }
    }
}

function handlePayPalExpressClientError(error) {
    if (typeof error != 'undefined' || error != 'undefined') {
        // skip validation error: use paypal method
        if ('User did not enter a payment method' != error.message && 'PAYPAL_POPUP_CLOSED' != error.code) {
            $('#paypal_express_error').empty();
            var messageText = error.message;
            if (typeof messageText === 'undefined' || messageText === 'undefined') {
                //Undefined error
            } else {
                var errorMsg = getErrorMessage(error.code) === 'undefined' ? ACC.addons.braintreeb2baddon['braintree.message.error.provider'] + ' ' + messageText : getErrorMessage(error.code);
                $(CONST.PAYPAL_EXPRESS_ERROR_ID).empty();
                $(CONST.PAYPAL_EXPRESS_ERROR_ID).append(createErrorDiv(errorMsg));
            }
        }
    }
}

function createErrorDiv(message) {
    var errorComponent = $(HTML.DIV).attr(HTML.CLASS, CONST.ERROR_ALERT_NEGATIVE_CSS_CLASS);
    errorComponent.prepend(message);
    return errorComponent;
}

function showRemovePaymentDetailsConfirmation(paymentInfoID) {
    $.colorbox({
        href: "#popup_confirm_payment_removal_" + paymentInfoID,
        inline: true,
        maxWidth: "100%",
        opacity: 0.7,
        width: "320px",
        onComplete: function () {
            $(this).colorbox.resize();
        }
    });
}

function bindToColorboxClose() {
    $.colorbox.close();
}

function receiveNewAddressData(selectedAddressID) {
    $.ajax({
        url: ACC.config.encodedContextPath + '/my-account/receive-address',
        type: 'GET',
        data: {"selectedAddressCode": selectedAddressID},
        success: function (result) {
            var selectedAddress = $.parseJSON(result);
            $("#title").text(selectedAddress.title);
            $("#firstName").text(selectedAddress.firstName);
            $("#lastName").text(selectedAddress.lastName);
            $("#line1").text(selectedAddress.line1);
            $("#line2").text(selectedAddress.line2);
            $("#town").text(selectedAddress.town);
            $("#postalCode").text(selectedAddress.postalCode);
            $("#country-name").text(selectedAddress.country.name);
            if (selectedAddress.region != null) {
                $("#region-name").text(selectedAddress.region.name);
            } else {
                $("#region-name").text("");
            }
            $("#billingAddressId").val(selectedAddress.id);
        }
    });
    $.colorbox.close();
}

function checkIntentOption(paypalIntent) {
    var isIntentValid = true;
    if (paypalIntent != CONST.INTENT_SALE && paypalIntent != CONST.INTENT_ORDER && paypalIntent != CONST.INTENT_AUTHORIZE) {
        isIntentValid = false;
        return isIntentValid;
    } else {
        return isIntentValid;
    }

}
