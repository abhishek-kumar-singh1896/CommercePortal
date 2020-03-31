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
            	maxDate : new Date(),
                dateFormat: 'dd/mm/yy',
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
								$("#productSuccessAlert").addClass('d-none');
							}else if(result.responseStatus == "PRODUCTNOTFOUND"){
								$("#productSuccessAlert").removeClass('d-none');
								$(".global-alerts").addClass('d-none');
								$(window).scrollTop($('.register-product-out').offset().top);
							}else{
								$("#productSuccessAlert").addClass('d-none');
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
								var region = result.registerProductForm.region;
								document.getElementById("productSkuInput").value = productSku;
								document.getElementById("serialNumberInput").value = serialNumber;
								document.getElementById("datePurchasedInput").value = datePurchased;
								document.getElementById("addressLine1Input").value = addressLine1;
								document.getElementById("addressLine2Input").value = addressLine2;
								document.getElementById("townCityInput").value = townCity;
								document.getElementById("postCodeInput").value = postCode;
								document.getElementById("countryInput").value = country;
								document.getElementById("phoneNumberInput").value = phoneNumber;
								document.getElementById("regionInput").value = region;
								var image='<img src="'+ productImage+'" alt="'+productaltText+'"/>';
								$("#phoneNumberInput").text(phoneNumber);
								$('#product-name').text(productName);
								$('#product-id').text(productCode);
								$('#product-serial').text(serialNumber);
								$('.product-image').find('img').remove();
								$('.product-image').prepend(image);
								
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
		var serialNumber = $("#serialNumber").val();
		var datePurchased = $("#datePurchased").val();
		var addressLine1 = $("#addressLine1").val();
		var addressLine2 = $("#addressLine2").val();
		var townCity = $("#townCity").val();
		var postCode = $("#postCode").val();
		var country = $("#country").val();
		var phoneNumber = $("#phoneNumber").val();
		var region = $("#region").val();
		var productDetails = {
				"productSku" : productSku,
				"serialNumber" : serialNumber,
				"datePurchased" : datePurchased,
				"addressLine1" : addressLine1,
				"addressLine2" : addressLine2,
				"townCity" : townCity,
				"postCode" : postCode,
				"country" : country,
				"phoneNumber" : phoneNumber,
				"region" : region
		}
		return JSON.stringify(productDetails);
	}
};