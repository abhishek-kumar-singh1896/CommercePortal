ACC.registerProduct = {

	_autoload: [
		"clickOnRegister",
	    "bindSubmitRegisterProduct",
	    "getJSONDataForRegisterProduct"
	],
	clickOnRegister: function(){
		$(document).ready(function () {
			// For custom file input
            $('.custom-file-input').on('change', function () {
                //get the file name
                var fileName = $(this).val();
                //replace the "Choose a file" label
                $(this).next('.custom-file-label').html(fileName);
            });

            // Date Picker
            $('#datePurchased').datepicker({
                format: 'mm/dd/yyyy',
                autoclose: true
            });

            $('#registerSuccess').click(function () {
                $('#confirmRegisterModal').modal('hide')
                $("#productSuccessAlert").removeClass('d-none').addClass('fade');
                $(window).scrollTop($('.register-product-out').offset().top);

                setTimeout(function(){
                    $("#productSuccessAlert").addClass('d-none').removeClass('fade');
                }, 7000);

            });
    });
	},
	bindSubmitRegisterProduct : function() {
		$('.registerProduct').click(
				function() {
					
					/*ACC.common.showLoader();*/
					$.ajax({
						url : ACC.config.encodedContextPath + '/register/submit',
						type : 'POST',
						dataType : 'json',
						contentType : 'application/json',
						data : ACC.registerProduct.getJSONDataForRegisterProduct(),
						async : true,
						success : function(response) {
							
							if(response.success == null){
								alert("1");
								$("#confirmRegisterModal").modal('show');
							    /*$('#customerSignup').modal('hide');
							    ACC.common.hideLoader();
								$("#customerSignupConfirm").modal('show');
								$('.return-checkout').click(function() {
								    location.reload();
								});*/
							}
							/*else{
									$('#globalMessages span').text(response['globalError']);
									var errorFields = Object.keys(response);
									delete response['globalError'];
									for (var field in errorFields) {
										if($('#'+errorFields[field]).siblings().hasClass("selecty")){
											$('#'+errorFields[field]).siblings().find('.selecty-selected').addClass('error');
										}
										else{
											$('#'+errorFields[field]).addClass("error");
										}
										$('#'+errorFields[field]).siblings('.errortxt').text(response[errorFields[field]]);
									}
									ACC.common.hideLoader();
								//$('#customerSignup').modal();
									
							}*/
							
						},
						error : function(jqXHR, textStatus, errorThrown) {
							// log the error to the console
							console.log("The following error occurred: "
									+ textStatus, errorThrown);
							/*ACC.common.handleAccessDeniedError(jqXHR.status);*/
						}
					});
				});

	},
	getJSONDataForRegisterProduct : function() {
		/*ACC.common.resetSignUpForm();*/
		var productSku=$("#productSku").val();
		var serialNumber = $("#serialNumber").val();
		var datePurchased = $("#datePurchased").val();
		var addressLine1 = $("#addressLine1").val();
		var addressLine2 = $("#addressLine2").val();
		var townCity = $("#townCity").val();
		var postCode = $("#postCode").val();
		var country = $("#country").val();
		var phoneNumber = $("#phoneNumber").val();
		
		var productDetails = {
				"productSku" : productSku,
				"serialNumber" : serialNumber,
				"datePurchased" : datePurchased,
				"addressLine1" : addressLine1,
				"addressLine2" : addressLine2,
				"townCity" : townCity,
				"postCode" : postCode,
				"country" : country,
				"phoneNumber" : phoneNumber
		}
		
		return JSON.stringify(productDetails);
	}
};