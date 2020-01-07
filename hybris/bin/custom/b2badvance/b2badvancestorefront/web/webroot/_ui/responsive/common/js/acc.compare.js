
ACC.compare = {

	bindCompare: function ()
	{
		$("[name='compareCheckbox']").each(function () {
			$(this).attr("autocomplete", "off");
		});
		//console.log("bind compare on document ready complete");
		$('.compareCheckbox').change(function() {
			  if ($(this).is(':checked')) {
			    ACC.compare.updateCompare($(this).val(), 'add');
			  } else {
				  ACC.compare.updateCompare($(this).val(), 'remove');
			  }
			});
		
		$('#addToCompareBtn').click(function() {
			 input = $('.compare-add-input input[type="text"]').val();
			 //console.log("input = "+ input);
			 ACC.compare.updateCompare(input, 'add');
			});
		
	},
	updateCompare: function(productCode, action)
	{
		$.ajax({
			type: 'GET',
			url: ACC.config.encodedContextPath + '/compare',
			data: { code: productCode, action: action },
			cache: false,
            success: function (data) {
            	$("#js-comp-item-box").html(data);
            	ACC.compare.showHideCompare();
            	ACC.compare.toggleProductsSelectedForCompare(productCode, action);
            	$('.compare-add-input input[type="text"]').val("");
            	if ($('#searchCompareError')!=undefined && $('#searchCompareError').attr("value")=="true"){
        			$('#searchCompareError-modal').modal();
        			setTimeout(function() { $('#searchCompareError-modal').modal('hide'); }, 3000);
        		}
            },
            error: function (jqXHR, textStatus, errorThrown) {
                // log the error to the console
                console.log("The following error occured: " + textStatus, errorThrown);
            }
        });
	},
	showHideCompare: function()
	{   
		if ($("#js-comp-item-box").find(".comp-item").length){
        	$('#js-comparison-row').show();
        	if($("#js-comp-item-box").find(".comp-item").length < 2)
        		{
        		$('.button-box .compare-product').attr('disabled','disabled');
        		}
        	else
        		{
        		$('.button-box .compare-product').removeAttr('disabled');
        		}
		} 
		else{
        	$('#js-comparison-row').hide();
		}
	},
	toggleProductsSelectedForCompare: function(productCode, action){
		if(action == 'remove'){
			if(productCode != null){
				$(":checkbox").filter(function() {
					if(this.value == productCode){
						$(this).prop('checked', false);
					}
			    });
			}			
		}
		else if(action == 'removeAll'){
			$("[name='compareCheckbox']:checked").each(function () {
				$(this).prop('checked', false);
			});
		}
	},
	bindAutocompleteSearch: function() {
		if($("#addToCompareInput").length) {
			var url = ACC.config.encodedContextPath + '/bulkorder/search';
			var minChars = 3;
			var resultSize = 4;
			var counter = 0;
			var autoccc = $("#addToCompareInput").autocomplete({
		         minLength: minChars,
		         position: {
		             my: "right top",
		             at: "right bottom"
		         },
		         source: function (request, response) {
		        	 counter = 0;
		             $.get(url, {
		                 query: request.term,
		                 resultSize: resultSize
		             }, function (data) {
		            	 
		                 response(data);
		             });
		         },
		         select: function (e, ui) {
		        	 var liObj = ($(".ui-autocomplete").find(".ui-state-active"));
		        	 var code = liObj.find('.code').text();
		             ACC.compare.updateCompare(code, 'add');
		             $('.ui-autocomplete').hide();
		         }
	
		     }).data("ui-autocomplete")._renderItem = function (ul, item) {
				//console.log(item);
				$("#isValidProduct").val('true');
		         var imageurl = '';
		         var imageCartUrl = '';
		         counter++;
		         for (i = 0; i < item.images.length; i++) {
		             if (item.images[i].format == "thumbnail") {
		                 imageurl = item.images[i].url;
		             }
		             if (item.images[i].format == "cartIcon") {
		            	 imageCartUrl = item.images[i].url;
		             }
		         }
		         return $("<li id='licnt_" + counter + "'/>")
		         .append("<a href='#'><input type='hidden' value=" + imageCartUrl + " class='imageCartUrl' /><div class='image-container' id='liImage_" + counter + "' ><img src='" + imageurl +  
		         		"' alt='' /></div><div class='product-attr'><div class='manufacturer attr'>" + item.manufacturer + 
		         		"</div><div class='code attr' id='liCode_" + counter + "'>" + item.code + 
		         		"</div><div class='name' id='liName_" + counter + "'>" + item.name + "</div></div></a>")
		         .appendTo(ul);
		     };
		}
	}
};

$(document).ready(function ()
{
	ACC.compare.bindCompare();
	ACC.compare.showHideCompare();
	ACC.compare.bindAutocompleteSearch();
});
