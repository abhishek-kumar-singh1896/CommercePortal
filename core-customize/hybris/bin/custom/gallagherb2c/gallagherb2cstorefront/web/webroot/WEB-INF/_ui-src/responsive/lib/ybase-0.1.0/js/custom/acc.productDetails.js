ACC.productDetails = {

	_autoload: [
	    "clickOnTab",
	    "bindSubmitRegisterProduct",
	    "getJSONDataForRegisterProduct"
	],

	clickOnTab: function(){
			$(document).ready(function () {
				/*$('.write-review-link').click(function(){
					$('.tab-review .write-review').toggle();
				});*/
				
				//navigation menu js start
				var subTabOut = $('.product-detail-tab').offset();

	            $(window).bind('scroll', function () {
	                if ($(window).scrollTop() > subTabOut.top) {
	                    $('.product-detail-tab').addClass('sticky-top');
	                }
	                else {
	                    $('.product-detail-tab').removeClass('sticky-top');
	                }
	            });


	            $("#productDetailTab ul li a[href^='#']").on('click', function (e) {

	                // prevent default anchor click behavior
	                e.preventDefault();

	                // store hash
	                var hash = this.hash;

	                // animate
	                $('html, body').animate({
	                    scrollTop: $(hash).offset().top
	                }, 500, function () {

	                    // when done, add hash to url
	                    // (default click behaviour)
	                    window.location.hash = hash;
	                });

	            });
	            
	            // Product details sub tab fixed and scroll functionality
	            var subTabOut = $('.product-detail-tab').offset();
	            // console.log(subTabOut.top);

	            $(window).bind('scroll', function () {
	                if ($(window).scrollTop() > subTabOut.top) {
	                    $('.product-detail-tab').addClass('sticky-top');
	                }
	                else {
	                    $('.product-detail-tab').removeClass('sticky-top');
	                }
	            });


	            $("#productDetailTab ul li a[href^='#']").on('click', function (e) {

	                // prevent default anchor click behavior
	                e.preventDefault();

	                // store hash
	                var hash = this.hash;

	                // animate
	                $('html, body').animate({
	                    scrollTop: $(hash).offset().top
	                }, 500, function () {

	                    // when done, add hash to url
	                    // (default click behaviour)
	                    window.location.hash = hash;
	                });

	            });
	            
	         // Product variant selection
	            $('.product-variant-section ul li a').click(function () {
	                $(this).closest('ul').find('.variant-checkbox-icon svg').addClass('d-none');
	                $(this).closest('ul').find('li').removeClass('active');
	                $(this).parent().addClass('active');
	                $(this).find('.variant-checkbox-icon svg').removeClass('d-none');
	            });
	            
	            $('.write-a-review-link').click(function(){
	                $(this).toggleClass('expanded');
	                $('.write-a-review-container').toggleClass('d-none fadeInUp');

	            });
	            
	         // The video slider
	            $('#productDetailVideoCarousel').flexslider({
	                animation: "slide",
	                controlNav: false,
	                animationLoop: false,
	                slideshow: false,
	                itemWidth: 171,
	                itemMargin: 10,
	                directionNav: false,
	                asNavFor: '#productDetailVideoSlider'
	            });

	            $('#productDetailVideoSlider').flexslider({
	                animation: "slide",
	                controlNav: false,
	                animationLoop: false,
	                slideshow: false,
	                touch: true,
	                sync: "#productDetailVideoCarousel"
	            });
	            

        });
			
			 $(window).on("load", function () {
		            // The slider being synced must be initialized first
		            $('#productDetailMainCarousel').flexslider({
		                animation: "slide",
		                controlNav: false,
		                animationLoop: false,
		                slideshow: false,
		                itemWidth: 171,
		                itemMargin: 8,
		                directionNav: false,
		                asNavFor: '#productDetailMainSlider'
		            });

		            $('#productDetailMainSlider').flexslider({
		                animation: "slide",
		                controlNav: false,
		                animationLoop: false,
		                slideshow: false,
		                touch: true,
		                sync: "#productDetailMainCarousel"
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
						data : ACC.productDetails.getJSONDataForRegisterProduct(),
						async : true,
						success : function(response) {
							
							if(response.success != null){
								alert("1");
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