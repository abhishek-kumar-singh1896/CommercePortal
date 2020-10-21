ACC.common = {
		_autoload : ["bindDeliveryInstructionsLink"],
	currentCurrency: $("main").data('currencyIsoCode') || "USD",
	processingMessage: $("<img src='" + ACC.config.commonResourcePath + "/images/spinner.gif'/>"),


	blockFormAndShowProcessingMessage: function (submitButton)
	{
		var form = submitButton.parents('form:first');
		form.block({ message: ACC.common.processingMessage });
	},

	refreshScreenReaderBuffer: function ()
	{
		// changes a value in a hidden form field in order
		// to trigger a buffer update in a screen reader
		$('#accesibility_refreshScreenReaderBufferField').attr('value', new Date().getTime());
	},

	checkAuthenticationStatusBeforeAction: function (actionCallback)
	{
		$.ajax({
			url: ACC.config.authenticationStatusUrl,
			statusCode: {
				401: function () {
					location.href = ACC.config.loginUrl;
				}
			},
			success: function (data) {
				if (data == "authenticated") {
					actionCallback();
				}
			}
		});
	},
	
	showLoader : function () {
	       $('.loader').show();
	    },
	    
	hideLoader : function () {
	       $('.loader').hide();
	    },
	    
	    bindDeliveryInstructionsLink : function() {
			$(document).on("click",".delivery-instructions-popup",
							function(e) {e.preventDefault();
								ACC.colorbox
										.open($(this).data("cboxTitle"),{
													href : $(this).data("link"),
													width : "400px",
													fixed : true,
													top : 50,
													initialWidth: "0px",
													initialHeight: "0px",
													reposition: false,
													onComplete: function() {
														$.colorbox.resize();
														ACC.common.refreshScreenReaderBuffer();
														if($(".page-orderTemplatePage").length >0){
															$("#deliveryInstructionEntry").val("");
															}
													},
													fixed: true
												});
								
							});
			
			$(window).resize(function(){
			    $.colorbox.resize({
			      width: "400px"
			    });
			});
		},
	    
	    submitInstruction : function() {
			var isValid = false;
			var entryNumber = $("#entryID").val();
			var deliveryInstruction = $("#deliveryInstructionEntry").val().trim();
			var regex = /^[A-Za-z0-9\u2000-\u206F\u2E00-\u2E7F\\'!"#$%&()*+,\-.\/:;<=>?@\[\]^_`{|}~+\s.+]+$/;
			if(deliveryInstruction.length==0 || regex.test(deliveryInstruction))
			{
				$(".textareaCharactersErrorbox").hide();
			$.ajax({
				type : "POST",
				url : ACC.config.encodedContextPath + "/checkout/setdeliveryinstruction",
				async: false,
				data : {
					"entryNumber" : entryNumber,
					"deliveryInstruction" : deliveryInstruction
				},
				success : function(data) {
					var response = JSON.stringify(data);
					response=response.replace(/\"/g, '')
					
					if(response === "valid")
					{
						$(".textareaErrorbox").hide();
						isValid = true;
					}
					else
					{
						$(".textareaErrorbox").show();
						isValid = false;
					}
					
				},
				error:function(data) {
					isValid = false;
				},
			});
			}
			else
				{
					$("#cboxContent").height("+=40");
					$("#cboxLoadedContent").height("+=20");
					$(".textareaCharactersErrorbox").show();
					isValid = false;
				}
			return isValid;
		}
};


$(document).on("change", ".add-to-cart #deliveryInstructionEntry", function() {
	var returnVal = ACC.common.submitInstruction();
});


/* Extend jquery with a postJSON method */
jQuery.extend({
	postJSON: function (url, data, callback)
	{
		return jQuery.post(url, data, callback, "json");
	}
});

// add a CSRF request token to POST ajax request if its not available
$.ajaxPrefilter(function (options, originalOptions, jqXHR)
{
	// Modify options, control originalOptions, store jqXHR, etc
	if (options.type === "post" || options.type === "POST")
	{
		var noData = (typeof options.data === "undefined");
		if (noData)
		{
			options.data = "CSRFToken=" + ACC.config.CSRFToken;
		}
		else
		{
			var patt1 = /application\/json/i;
			if (options.data instanceof window.FormData)
			{
				options.data.append("CSRFToken", ACC.config.CSRFToken);
			}
			// if its a json post, then append CSRF to the header. 
			else if (patt1.test(options.contentType))
			{
				jqXHR.setRequestHeader('CSRFToken', ACC.config.CSRFToken);
			}
			else if (options.data.indexOf("CSRFToken") === -1)
			{
				options.data = options.data + "&" + "CSRFToken=" + ACC.config.CSRFToken;
			}
		}
		
	}
});
