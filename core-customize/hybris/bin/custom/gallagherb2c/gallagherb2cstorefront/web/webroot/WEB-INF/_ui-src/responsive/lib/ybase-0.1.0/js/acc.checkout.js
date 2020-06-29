ACC.checkout = {

	_autoload: [
		"bindCheckO",
		"bindForms",
		"bindSavedPayments",
		"bindTermAndCondition"
	],


	bindForms:function(){

		$(document).on("click","#addressSubmit",function(e){
			e.preventDefault();
			$('#addressForm').submit();	
		})
		
		$(document).on("click","#deliveryMethodSubmit",function(e){
			e.preventDefault();
			$('#selectDeliveryMethodForm').submit();	
		})

	},

	bindSavedPayments:function(){
		$(document).on("click",".js-saved-payments",function(e){
			e.preventDefault();

			var title = $("#savedpaymentstitle").html();

			$.colorbox({
				href: "#savedpaymentsbody",
				inline:true,
				maxWidth:"100%",
				opacity:0.7,
				//width:"320px",
				title: title,
				close:'<span class="glyphicon glyphicon-remove"></span>',
				onLoad:function() {
			        $('html, body').css('overflow', 'hidden'); // page scrollbars off
			    }, 
			    onClosed:function() {
			        $('html, body').css('overflow', ''); // page scrollbars on
			    },
				onComplete: function(){
				}
			});
		})
	},

	bindCheckO: function ()
	{
		var cartEntriesError = false;
		
		// Alternative checkout flows options
		$('.doFlowSelectedChange').change(function ()
		{
			if ('multistep-pci' == $('#selectAltCheckoutFlow').val())
			{
				$('#selectPciOption').show();
			}
			else
			{
				$('#selectPciOption').hide();

			}
		});



		$('.js-continue-shopping-button').click(function ()
		{
			var checkoutUrl = $(this).data("continueShoppingUrl");
			window.location = checkoutUrl;
		});
		
		$('.js-create-quote-button').click(function ()
		{
			$(this).prop("disabled", true);
			var createQuoteUrl = $(this).data("createQuoteUrl");
			window.location = createQuoteUrl;
		});

		
		$('.expressCheckoutButton').click(function()
				{
					document.getElementById("expressCheckoutCheckbox").checked = true;
		});
		
		$(document).on("input",".confirmGuestEmail,.guestEmail",function(){
			  
			  var orginalEmail = $(".guestEmail").val();
			  var confirmationEmail = $(".confirmGuestEmail").val();
			  
			  if(orginalEmail === confirmationEmail){
			    $(".guestCheckoutBtn").removeAttr("disabled");
			  }else{
			     $(".guestCheckoutBtn").attr("disabled","disabled");
			  }
		});
		
		$('.confirmGuestEmail').on('focusout', function() {
			  var orginalEmail = $(".guestEmail").val();
			  var confirmationEmail = $(this).val();
			  if(orginalEmail === confirmationEmail){
			    $(this).removeClass("has-error");
			    $('.error-message').hide();
			  }else{
			     $(this).addClass("has-error");
			     $('.error-message').show();
			  }
			});
		
		$('.js-continue-checkout-button').click(function ()
		{
			var checkoutUrl = $(this).data("checkoutUrl");
			
			cartEntriesError = ACC.pickupinstore.validatePickupinStoreCartEntires();
			if (!cartEntriesError)
			{
				var expressCheckoutObject = $('.express-checkout-checkbox');
				if(expressCheckoutObject.is(":checked"))
				{
					window.location = expressCheckoutObject.data("expressCheckoutUrl");
				}
				else
				{
					var flow = $('#selectAltCheckoutFlow').val();
					if ( flow == undefined || flow == '' || flow == 'select-checkout')
					{
						// No alternate flow specified, fallback to default behaviour
						window.location = checkoutUrl;
					}
					else
					{
						// Fix multistep-pci flow
						if ('multistep-pci' == flow)
						{
						flow = 'multistep';
						}
						var pci = $('#selectPciOption').val();

						// Build up the redirect URL
						var redirectUrl = checkoutUrl + '/select-flow?flow=' + flow + '&pci=' + pci;
						window.location = redirectUrl;
					}
				}
			}
			return false;
		});

		$('.js-continue-checkoutasguest-button').click(function ()
				{
					//alert('checkoutasguest');
					var checkoutUrl = $(this).data("checkoutUrl")+"/guest";
					//alert('checkoutUrl'+checkoutUrl);
					cartEntriesError = ACC.pickupinstore.validatePickupinStoreCartEntires();
					if (!cartEntriesError)
					{
						var expressCheckoutObject = $('.express-checkout-checkbox');
						if(expressCheckoutObject.is(":checked"))
						{
							window.location = expressCheckoutObject.data("expressCheckoutUrl");
						}
						else
						{
							var flow = $('#selectAltCheckoutFlow').val();
							if ( flow == undefined || flow == '' || flow == 'select-checkout')
							{
								// No alternate flow specified, fallback to default behaviour
								window.location = checkoutUrl;
							}
							else
							{
								// Fix multistep-pci flow
								if ('multistep-pci' == flow)
								{
								flow = 'multistep';
								}
								var pci = $('#selectPciOption').val();

								// Build up the redirect URL
								var redirectUrl = checkoutUrl + '/select-flow?flow=' + flow + '&pci=' + pci;
								window.location = redirectUrl;
							}
						}
					}
					return false;
				});

	},
	bindTermAndCondition : function() {

//		$(document).on("input", ".Terms-1-Condition-1", function() {
//			if ($('#Terms1').is(':checked')) {
//				$(".btn-place-order1").removeAttr("disabled");
//			} else {
//				$(".btn-place-order1").attr("disabled", true);
//			}
//
//		});
		
		$('.Terms-1-Condition-1').change(function(){
			if ($('#Terms1').is(':checked')) {
				$(".btn-place-order1").removeAttr("disabled");
			} else {
				$(".btn-place-order1").attr("disabled", true);
			}
		});
		
		$(document).on("input", ".Terms-1-Condition-1", function() {
			if ($('#Terms2').is(':checked')) {
				$(".btn-place-order2").removeAttr("disabled");
			} else {
				$(".btn-place-order2").attr("disabled", true);
			}

		});

	}

};
