ACC.registerProduct = {

	_autoload: [
		"clickOnRegister",
	    "bindSubmitRegisterProduct",
	    "getJSONDataForRegisterProduct",
	    "showRequest",
	    "bindRegisterProductForm",
	    "displayRegisterProductPopup"
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
		$('.registerProduct1').click(
				function() {
					$.ajax({
						url : ACC.config.encodedContextPath + '/register/submit',
						type : 'POST',
						dataType : 'json',
						contentType : 'application/json',
						data : ACC.registerProduct.getJSONDataForRegisterProduct(),
						async : true,
						success : function(response) {
							
							if(response.success != null){
								alert("1");
								$("#confirmRegisterModal").modal('show');
							    
							}
						},
						error : function(jqXHR, textStatus, errorThrown) {
							// log the error to the console
							console.log("The following error occurred: "
									+ textStatus, errorThrown);
							ACC.common.handleAccessDeniedError(jqXHR.status);
						}
					});
				});

	},
	bindRegisterProductForm: function () {
        var registerProductForm = $('.registerProduct_form');
        registerProductForm.ajaxForm({
        	beforeSubmit:ACC.registerProduct.showRequest,
        	success: ACC.registerProduct.displayRegisterProductPopup
         });    
        setTimeout(function(){
        	$ajaxCallEvent  = true;
         }, 2000);
     },
     showRequest: function(arr, $form, options) {  
    	 if($ajaxCallEvent)
    		{
    		 $ajaxCallEvent = false;
    		 return true;
    		}   	
    	 return false;
 
    },
    displayRegisterProductPopup: function (cartResult, statusText, xhr, formElement) {
    	$ajaxCallEvent=true;
    	if(cartResult!=null){
    		var titleHeader = $('#registerProductTitle').html();
        ACC.colorbox.open(titleHeader, {
        	html:cartResult,
        	width:"500px", 
		    height:"600px", 
		    onLoad:function() {
		        $('html, body').css('overflow', 'hidden'); // page scrollbars off
		    }, 
		    onClosed:function() {
		        $('html, body').css('overflow', ''); // page scrollbars on
		    }
        });}
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