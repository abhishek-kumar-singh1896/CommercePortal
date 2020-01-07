<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>Braintree Payment Form</title>
    <style type="text/css">
        #cardholder {
        margin-right: 20px;
        width: 244px;
        position: relative;
        height: 18px;
        border: 1px solid #ddd;
        padding: 0 0.5em;
        background-color: white;
        }
        
        #number {
        margin-right: 20px;
        width: 244px;
        position: relative;
        height: 18px;
        border: 1px solid #ddd;
        padding: 0 0.5em;
        background-color: white;
        }

        #cvv {
        margin-right: 20px;
        width: 244px;
        position: relative;
        height: 18px;
        border: 1px solid #ddd;
        padding: 0 0.5em;
        background-color: white;
        }
        #expiration-date {
        margin-right: 20px;
        width: 244px;
        position: relative;
        height: 18px;
        border: 1px solid #ddd;
        padding: 0 0.5em;
        background-color: white;
        }
        #number.braintree-hosted-fields-invalid {
        border: 1px solid #FFB6C1;
        }

        #cvv.braintree-hosted-fields-invalid {
        border: 1px solid #FFB6C1;
        }

        #expiration-date.braintree-hosted-fields-invalid {
        border: 1px solid #FFB6C1;
        }
        
        .card-check{
          color: blue;
          display: none;
        }
        
        .card-valid{
          color: green;
          display: none;
        }
        
        .card-invalid{
          color: red;
          display: none;
        }

        .z-button {
        cursor:pointer;
        font-family: Arial,Helvetica,sans-serif;
        background-color:#202b4a;
        background-image:url(images/button_back.gif);
        background-repeat:repeat-x;
        border: 1px solid #ddd;
        padding: 4px 8px;
        color: #FFFFFF;
        font-size: 11px;
        margin-top: 15px;
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        -o-box-border-radius: 3px;
        -icab-border-radius: 3px;
        -khtml-border-radius: 3px;
        }

        .z-button.btnblue {
        background-image:url(images/BUTTON_BLUE_back.gif);
        }
    </style>
</head>
<body>
<div>
    <form action="" method="POST" id="braintree-payment-form">
    <input type="hidden" id="clientToken" value="${clientToken}" />
        <div >
          <input type="text" name="cardholder" id="cardholder" placeholder="Cardholder Name" maxlength="175" />
        </div>
        <br/>
        
        <div id="number"></div>
        <br/>

        <div id="cvv"></div>
        <br/>

        <div id="expiration-date"></div>
        <input type="button" id="submit" value="Validate Credit Card" class="z-button z-button.btnblue" />
	    <span id="validPaymentMethodCheck" class="card-check">${paymentMethodCheckMessage}</span>
	    <span id="validPaymentMethodSuccess" class="card-valid">${paymentMethodValidMessage}</span>
	    <span id="validPaymentMethodError" class="card-invalid">${paymentMethodInvalidMessage}</span>
    </form>
</div>

<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/client.min.js"></script>
<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/data-collector.min.js"></script>
<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/hosted-fields.min.js"></script>
<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/paypal.min.js"></script>
<script type="text/javascript" src="https://js.braintreegateway.com/web/3.38.0/js/three-d-secure.min.js"></script>

<script src="https://code.jquery.com/jquery-1.12.4.min.js" integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ=" crossorigin="anonymous"></script>
<script>

$(function(){
	 
	var clientToken = getClientToken();

	braintree.client.create({
	    authorization: clientToken
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
                    	var cardholder = $("#cardholder").val();
            			
            			$.ajax({
            				type: "POST",
            				url: "",
            				beforeSend : function(){
            					$("#validPaymentMethodCheck").show();
            				},
            				complete : function(){
            					$("#validPaymentMethodCheck").hide();
            				},
            				data: { 
            					payment_method_nonce : response.nonce,
            					cardholder : cardholder
            				},
            				success: function(data){
            					$("#validPaymentMethodError").hide();
            					$("#validPaymentMethodSuccess").show();
            				},
            				error: function(data){
            					$("#validPaymentMethodSuccess").hide();
            					$("#validPaymentMethodError").show();
            				},
            			});
                    }
                });
            });
        });
	});
});
	
function getClientToken() {
    return $("#clientToken").val();
}

</script>
</body>
</html>
