
function configureAutocomplete() {

    var url = $("#qoUrl").val() + '/search';
    var minChars = $("#qoMinimumCharactersForSearch").val();
    var resultSize = $("#qoResultSize").val();
    $('.multi-add-product input.product').each(function () {

        var num = $(this).attr('id').replace(/^\D+/g, '');
        
        //------------------------------------------
        configureSingleAutocompleteRow(num, minChars, resultSize, url);
        //---------------------------------------------
        qoAutocompleteSetFocus();
    });
       
}

function reconfigureSingleAutocompleteRow(num) {
	var url = $("#qoUrl").val() + '/search';
    var minChars = $("#qoMinimumCharactersForSearch").val();
    var resultSize = $("#qoResultSize").val();
    configureSingleAutocompleteRow(num, minChars, resultSize, url);
}

function configureSingleAutocompleteRow(num, minChars, resultSize, url) {
	var selectUrl = $("#qoUrl").val() + '/select';
	var counter = 0;
	var autoccc = $("#qo_" + num).autocomplete({
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
        	 
        	 var liObj = ($(".ui-autocomplete").find("li").find("a.ui-state-active"));
        	
        	 var code = liObj.find('.code').text();
             var name = liObj.find('.name').text();
             var manufacturer = liObj.find('.manufacturer').text();
             var imageUrl = liObj.find('.imageCartUrl').val();
        	 /*var cnt = liObj.id.replace(/^\D+/g, '');
        	 var code = $('#liCode_' + cnt).text();
        	 var name = $('#liName_' + cnt).text();
        	 var imageUrl = $('#liImage_' + cnt).children('img').attr('src');*/
             
        	 var divContent = qoGenerateTdDivContent(code, manufacturer, name, imageUrl);
             document.getElementById('td_' + num).innerHTML = divContent;

             //set qty = 1
             if ($('#qty_' + num).val() == '') {
                 $('#qty_' + num).val('1');
             }
             var qty = $('#qty_' + num).val();
             $('#code_' + num).val(code);
             $.get(selectUrl, {
                 code: code,
                 qty: qty,
                 order: num
             });
             $('.ui-autocomplete').hide();
             $("#qoRemove_" + num).removeAttr("disabled");
             $(".ui-autocomplete").find("li.ui-state-focus").remove();
             qoAutocompleteSetFocus();
        }

     }).data("ui-autocomplete")._renderItem = function (ul, item) {
		//console.log(item);
		$("#isValidProduct").val('true');
         var imageurl = '';
         var imageCartUrl = '';
         counter++;
         if(item.images != null){
        	 for (i = 0; i < item.images.length; i++) {
                 if (item.images[i].format == "thumbnail") {
                     imageurl = item.images[i].url;
                 }
                 if (item.images[i].format == "cartIcon") {
                	 imageCartUrl = item.images[i].url;
                 }
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

function qoGenerateTdDivContent(code, manufacturer, name, imageUrl) {
	var divContent = "<div style='clear: both; width:150px;'>" 
        +"<div class='pr-img'><img src='" + imageUrl + "' alt='' /></div>"
        +"<div class='pr-code'>" + code + "</div>";
        if(manufacturer != null){
        	divContent= divContent + "<div class='pr-manufacturer'>" + manufacturer + "</div>";
        }        
        divContent=divContent +"<div class='pr-name'>" + name + "</div></div>";
	return divContent;
}

function addNewQoRow() {
    var table = document.getElementById("qoTable");
    var size = $("#qoTableSizeId").val();
    var minChars = $("#qoMinimumCharactersForSearch").val();
    var url = $("#qoUrl").val() + '/search';
    var resultSize = $("#qoResultSize").val();
    var orderNum = 0;
    orderNum = ++size;

    var row = table.insertRow(size);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);

    cell1.innerHTML = "<input type='text' class='text qtt' id='qty_" + orderNum + "' />";
    cell1.onchange = function () {
        qoUpdateQty(orderNum);
    };
    cell1.className = "col-1-left";

    cell2.innerHTML = "<input type='text' class='text product' value='' id='qo_" + orderNum + "' />";
    cell2.id = 'td_' + orderNum;
    cell2.className = "col-2-center";
    
    cell3.innerHTML = "<a class='cross-button' onclick='qoRemove(" + orderNum + ")' " +
    	"disabled='disabled' id='qoRemove_" + orderNum + "'/>"
        "<input type='hidden' id='code_" + orderNum + "' value='' />";
    cell3.className = "col-3-right";
    
    $("#qoTableSizeId").val(orderNum);
    
    configureSingleAutocompleteRow(orderNum, minChars, resultSize, url);
    $("#qo_" + orderNum).focus();
}

function qoUpdateQty(orderNumber) {
    var order = orderNumber;
    var qty = $('#qty_' + order).val();
    if (!isInt(qty)) {
        $('#qty_' + order).val('1');
        qty = '1';
    }
    var code = $('#code_' + order).val();
    if (code != '') {
        var selectUrl = $("#qoUrl").val() + '/select';
        $.get(selectUrl, {
            code: code,
            qty: qty,
            order: orderNumber
        });
    }
}

function qoRemove(orderNumber) {
	var focus = true;
	qoRemoveWithFocus(orderNumber, focus);
}

function qoRemoveWithFocus(orderNumber, focus) {
    var order = orderNumber;
    var code = $('#code_' + order).val();
    if (code != '') {
        var removeUrl = $("#qoUrl").val() + '/remove';
        $.get(removeUrl, {
            order: order
        });
        $('#qty_' + order).val('');
        var divContent = "<div class='ui-front'><input type='text' class='text product' value='' id='qo_" + orderNumber + "' />";
       	document.getElementById('td_' + orderNumber).innerHTML = divContent;
                
        $('#code_' + order).val('');
        $("#qoRemove_" + orderNumber).attr("disabled", "disabled");	
        reconfigureSingleAutocompleteRow(orderNumber);
        if (focus) {
        	qoAutocompleteSetFocus();
        }
    }
}

function isInt(n) {
    var intRegex = /^\d+$/;
    return intRegex.test(n);
}

function qoAddToCart(url) {
	$("#qoAddToCart").attr("disabled", "disabled");		
	// $('#cart_popup').hide();
	$.get(url + '/addToCart', function(data) {
		if($("#isValidProduct").val() == "true"){
			$('#rollover_cart_popup').html(data.cartPopupHtml);
			var qoTableSize = $("#qoTableSizeId").val();
			for (var i = 1; i <= qoTableSize; i++){
				qoRemoveWithFocus(i, false);
			}
			$("#qoAddToCart").removeAttr("disabled");
			
			
			
			
			/*$('#rollover_cart_popup').fadeIn();*/
			$('#addToCartLayer').remove();

	        if (typeof ACC.minicart.updateMiniCartDisplay == 'function') {
	            ACC.minicart.updateMiniCartDisplay();
	        }
	        var titleHeader = $('#addToCartTitle').html();

	        ACC.colorbox.open(titleHeader, {
	            html: data.cartPopupHtml,
	            width: "460px"
	        });
	        
	        var productCode = $('[name=productCodePost]').val();
	        var quantityField = $('[name=qty]').val();

	        var quantity = 1;
	        if (quantityField != undefined) {
	            quantity = quantityField;
	        }
 
			qoAutocompleteSetFocus();
			$("#isValidProduct").val('false');
			
		}else{
			var errorMsg = document.getElementById('wrongProductCode');
			$("#qoAddToCart").removeAttr("disabled");
			qoShowErrors(errorMsg.value);
		}
		
	});
	
}
function qoAutocompleteSetFocus() {
	document.getElementById('msgBox').style.display = "none";
	$('#msgBoxForRows').removeClass('qoRowsWithErrorsBlock');
	$('#msgBoxForRows').addClass('qoRowsWithErrors');
	$('.multi-add-product input.product:not(:disabled)').first().blur();
	$('.multi-add-product input.product:not(:disabled)').first().focus();
}

function qoClickBrowse() {
	$('#fileBrowseBtn').click();
}

function qoUploadFile() {
	
	$("#dummyBrowseBtn").attr("disabled", "disabled");
	var validExts = new Array(".xlsx");
	var filesList = document.getElementById('fileBrowseBtn');
	var fileExt = $(filesList).val().substring($(filesList).val().lastIndexOf('.'));
	//
	if (validExts.indexOf(fileExt) < 0) {
		var wrongExtMsg = document.getElementById("errWrongExt");
		qoShowErrors(wrongExtMsg.value);
		$("#dummyBrowseBtn").removeAttr("disabled");
	} else {
		var url = document.getElementById('qoUrl').value;
		
		$.ajaxFileUpload({
			url : url + '/upload',
			data: {'CSRFToken' : ACC.config.CSRFToken},
			secureuri : false,
			fileElementId : 'fileBrowseBtn',
			/*dataType : 'xml',*/
			success : function(data, status) {
				populateSucessData(data);
				
			},
			error : function(data, status, e) {
				console.log(e);
				$("#dummyBrowseBtn").removeAttr("disabled");
			}
		})
	}
	
	return false;

}

function populateSucessData(data){
	
	var dataJson;
	if(data.body ===undefined){
		dataJson= data;
	}else{
		dataJson= $.parseJSON(data.body.children[0].innerHTML);	
	}
	var errorMsg = dataJson.errorMessage;
	if (errorMsg != null && errorMsg != '') {
		console.log(errorMsg);
		qoShowErrors(null);
		$("#dummyBrowseBtn").removeAttr("disabled");
	} else {
		var list = dataJson.bulkorderList;
		if (list.length > 0) {
			
			$.each(list, function(i, obj) {
				var order = obj.orderNumber;
				
				if (obj.productData != null) {
				var qty = obj.quantity;
				var code = obj.productData.code;
	             var name = obj.productData.name;
	             var manufacturer = obj.productData.manufacturer;
	             var imageurl = '';
	             $("#isValidProduct").val('true');
	             if(obj.productData.images!=null){
		             for (i = 0; i < obj.productData.images.length; i++) {
		            	 if (obj.productData.images[i].format == "thumbnail") {
		                     imageurl = obj.productData.images[i].url;
		                     break;
		                 }
		             }
	             }
	             
	             var divContent = qoGenerateTdDivContent(code, manufacturer, name, imageurl);
				
				var rowExists = $('#td_' + order).val();
				if (rowExists == null) {
					//create row
					var table = document.getElementById("qoTable");
				    var size = $("#qoTableSizeId").val();
				    var minChars = $("#qoMinimumCharactersForSearch").val();
				    var orderNum = 0;
				    orderNum = ++size;

				    var row = table.insertRow(size);
				    var cell1 = row.insertCell(0);
				    var cell2 = row.insertCell(1);
				    var cell3 = row.insertCell(2);

				    cell1.innerHTML = "<input type='text' class='text qtt' id='qty_" + order + "' value='" + qty + "'/>";
				    cell1.onchange = function () {
				        qoUpdateQty(order);
				    };

				    cell2.innerHTML = divContent;
				    cell2.id = 'td_' + order;
				    cell3.innerHTML = "<input type='button' value='X' onclick='qoRemove(" + order + ")' " +
				    	" id='qoRemove_" + order + "'/>"
				        "<input type='hidden' id='code_" + order + "' value='' />";
				    $("#qoTableSizeId").val(order);
					
				} else {
					document.getElementById('td_' + order).innerHTML = divContent;
					 $('#qty_' + order).val(qty);
		             
		             $('#code_' + order).val(code);
		             $('.ui-autocomplete').hide();
		             $("#qoRemove_" + order).removeAttr("disabled");
				}
				
				}
			});
			qoAutocompleteSetFocus();
		}
		if (dataJson.csvErrors.length > 0) {
			var csvErrors = '';
			var count = 0;
			$.each(dataJson.csvErrors, function(i, obj) {
				csvErrors = csvErrors + '<li>' + obj + '</li>';
				count ++;
			});
				//console.log(csvErrors);
			var msg = document.getElementById("notImportedRowsError").value;
			var errDivContent = '<a href=\"#\" onclick=\"qoShowRows()\">' + count + '</a> ' + msg;
			document.getElementById('msgBox').innerHTML = '<p>' + errDivContent + '</p>';
			$(document.getElementById('msgBox')).fadeIn();
			
			document.getElementById('msgBoxForRowsList').innerHTML =  csvErrors ;
			$('#msgBoxForRows').removeClass('qoRowsWithErrors');
			$('#msgBoxForRows').addClass('qoRowsWithErrorsBlock');
			}
			else if(errorMsg == null || errorMsg == ''){
			var msg = document.getElementById("rowsImportedSuccessMsg").value;
			document.getElementById('msgBox').innerHTML = '<p id="success">' + msg + '</p>';
			$(document.getElementById('msgBox')).fadeIn();
			}
		$("#dummyBrowseBtn").removeAttr("disabled");
	}
}

function qoShowRows() {
	$('#msgBoxForRows').toggleClass('qoRowsWithErrors qoRowsWithErrorsBlock');
}

function qoShowErrors(msg) {
	if (msg == null) {
		msg = document.getElementById('unknownError').value;
	}
	document.getElementById('msgBox').innerHTML = '<p>' + msg + '</p>';
	$(document.getElementById('msgBox')).fadeIn();
	$('#msgBoxForRows').removeClass('qoRowsWithErrorsBlock');
	$('#msgBoxForRows').addClass('qoRowsWithErrors');
}