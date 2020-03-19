ACC.registerProduct = {

	_autoload: [
		"clickOnRegister",
	    "bindVerifyRegisterProduct",
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

            /*$('#registerSuccess').click(function () {
                $('#confirmRegisterModal').modal('hide')
            	alert("222");
            	ACC.colorbox.close();
                $("#productSuccessAlert").removeClass('d-none').addClass('fade');
                $(window).scrollTop($('.register-product-out').offset().top);

                setTimeout(function(){
                    $("#productSuccessAlert").addClass('d-none').removeClass('fade');
                }, 7000);

            });*/
    });
	},
	bindVerifyRegisterProduct : function() {
		$('.registerProduct').click(
				function(e) {
					e.preventDefault();
					$.ajax({
						url : ACC.config.encodedContextPath + '/register-product/verify',
						type : 'POST',
						dataType : 'json',
						contentType : 'application/json',
						data : ACC.registerProduct.getJSONDataForRegisterProduct(),
						async : true,
						success : function(result) {
							if(result.responseStatus == "FAILURE"){
								showFieldErrors(result.errorsMap);
							}else{
								$('#registerProductForm').find('.form-control').removeClass('has-error').next().find('.error-text').addClass('d-none');
								var titleHeader = $('#registerProductTitle').html();
								var productCode=result.productCode;
								var productName=result.productName;
								var productImage=result.productImage;
								var productaltText=result.productaltText;
								var productSku =result.registerProductForm.productSku;
								var serialNumber =result.registerProductForm.serialNumber;
								var datePurchased =result.registerProductForm.datePurchased;
								var addressLine1 =result.registerProductForm.addressLine1;
								var addressLine2 =result.registerProductForm.addressLine2;
								var townCity =result.registerProductForm.townCity;
								var postCode =result.registerProductForm.postCode;
								var country =result.registerProductForm.country;
								var phoneNumber =result.registerProductForm.phoneNumber;
								$("#productSkuInput").text(productSku);
								$("#serialNumberInput").text(serialNumber);
								$("#datePurchasedInput").text(datePurchased);
								$("#addressLine1Input").text(addressLine1);
								$("#addressLine2Input").text(addressLine2);
								$("#townCityInput").text(townCity);
								$("#postCodeInput").text(postCode);
								$("#countryInput").text(country);
								$("#phoneNumberInput").text(phoneNumber);
								$('.product-name').text(productName);
								$('.product-id').text(productCode);
								$('.product-image').prepend('<img src="'+ productImage+'" alt="'+productaltText+'"/>');
								
								var registerPopup = $(".register-product-modal-container").html();
						        ACC.colorbox.open(titleHeader, {
						        	html:registerPopup,
						        	width:"500px", 
								    height:"600px", 
								    innerHeight:"600px",
								    onLoad:function() {
								        $('html, body').css('overflow', 'hidden'); // page scrollbars off
								    }, 
								    onClosed:function() {
								        $('html, body').css('overflow', ''); // page scrollbars on
								        $('.product-name').text("");
										$('.product-id').text("");
										$('.product-image').prepend("");
								    }
						        });
							}
						},
						error : function(jqXHR, textStatus, errorThrown) {
							// log the error to the console
							console.log("The following error occurred: "
									+ textStatus, errorThrown);
						}
					});
				});
		
		function showFieldErrors(errorsMap) {
			$('#registerProductForm').find('.form-control').removeClass('has-error').next().find('.error-text').addClass('d-none');
			for (var fieldName in errorsMap) {
				$('#' + fieldName).focus();
				$('#' + fieldName).addClass('has-error').next().find('.error-text').removeClass('d-none');
				$('#' + fieldName).next().find('.error-inner-text').text(errorsMap[fieldName]);
			}
		}

	},
	getJSONDataForRegisterProduct : function() {
		var productSku=$("#productSku").val();
//		alert("productSku>>"+productSku);
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
//		alert("productDetails>>"+JSON.stringify(productDetails));
		return JSON.stringify(productDetails);
	}/*,
	bindSubmitRegisterProduct : function() {
		alert("23");
		$('.registerSuccess').click(
				function(e) {
					alert("23");
					var formData = $("#registerProductForm");
					e.preventDefault();
					$.ajax({
						url : ACC.config.encodedContextPath + '/register-product/submit',
						type : 'POST',
						dataType : 'json',
						contentType : 'application/json',
						data : formData.serialize(),
						async : true,
						success : function(result) {
							alert("result.responseStatus>>"+result)
						},
						error : function(jqXHR, textStatus, errorThrown) {
							// log the error to the console
							console.log("The following error occurred: "
									+ textStatus, errorThrown);
						}
					});
				});
	}*/
};