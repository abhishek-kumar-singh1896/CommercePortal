NewTransactionWidgetController = function($scope) {
	$scope.socketInput = function(socket, data) {
		braintree.client.create({
			authorization: data.clientToken
		}, function (clientErr, clientInstance) {
			if (clientErr) {
				$("#validPaymentMethodError").hide();
				$("#validPaymentMethodError").show();
				return;
			}

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
							number : {
								selector : "#number",
								placeholder : "Card Number"
							},
							expirationDate : {
								selector : "#expiration-date",
								placeholder : "MM/YY"
							},
							cvv : {
								selector : "#cvv",
								placeholder : "CVV"
							}
						}
					},
				function (hostedFieldsErr, hostedFieldsInstance) {
						if (hostedFieldsErr) {
							$("#validPaymentMethodSuccess").hide();
							$("#validPaymentMethodError").show();
							return;
						}

						$("#submit").unbind("click");
						$("#submit").attr("type", "button");
						$("#submit").click(function () {
							hostedFieldsInstance.tokenize(function (tokenizeErr, response) {
								if (tokenizeErr) {
									$("#validPaymentMethodSuccess").hide();
									$("#validPaymentMethodError").show();
								} else {
									$("#validPaymentMethodSuccess").hide();
									$("#validPaymentMethodError").hide();

									var cardholder = $("#cardholder").val();
									var nonce = response.nonce;

									$scope.adapter.cng().socketEvent("newTransactionOutput", cardholder + "|" + nonce + "|" + data.siteUID  + "|" + data.currentCurrency);
								}
							});
						});
					});
		});

	};
};

AngularCNG.init("newTransactionWidget", NewTransactionWidgetController);

AngularCNG.init = function(widgetName, controller, services) {
	return new AngularCNG( "widgets." + widgetName ).controller(controller, services);
};
