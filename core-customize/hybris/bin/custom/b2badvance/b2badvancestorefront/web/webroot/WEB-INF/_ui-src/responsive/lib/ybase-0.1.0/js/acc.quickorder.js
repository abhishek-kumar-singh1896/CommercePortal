var ACC = ACC ||
{}; // make sure ACC is available

if ($("#quickOrder").length > 0)
{
  ACC.quickorder = {
    _autoload: [
      "bindHandleRestore",
      "bindSearchAutocompleteToInput",
      "bindClearQuickOrderRow",
      "bindAddProductRow",
      "bindUpdateProductRow",
      "bindAddToCartClick",
      "bindResetQuickSearch",
      "bindQuantityUpdated",
      ["bindCreateTemplate", $(".js-create-template-link").length != 0],
    ],

    $quickOrderLeavePageMsg: $('#quickOrder').data('gridConfirmMessage'),
    $addToCartBtn: $('#js-add-to-cart-quick-order-btn-bottom'),
    $skuInputField: '.js-sku-input-field',
    $qtyInputField: '.js-quick-order-qty',
    $jsLiContainer: 'li.js-li-container',
    $removeQuickOrderRowBtn: '.js-remove-quick-order-row',
    $skuValidationContainer: '.js-sku-validation-container',
    $searchInputField: '.js-search-input-field',
    $addProductToQuickOrder: '.click-add-product-to-quick-order',
    $tableBodyDiv: $('.quick-order-table-body-div'),
    $createTemplateBtn: $('.js-create-template-link'),

    bindSearchAutocompleteToInput: function()
    {
      // extend the default autocomplete widget, to solve issue on
      // multiple instances of the searchbox component
      $.widget("custom.yautocomplete", $.ui.autocomplete,
      {
        _create: function()
        {
          // get instance specific options form the
          // html data attr
          var option = this.element.data("options");
          // set the options to the widget
          this._setOptions(
          {
            minLength: option.minCharactersBeforeRequest,
            displayProductImages: option.displayProductImages,
            delay: option.waitTimeBeforeRequest,
            autocompleteUrl: option.autocompleteUrl,
            productSearchRequest: option.productSearchRequest,
            source: this.source
          });
          // call the _super()
          $.ui.autocomplete.prototype._create.call(this);
          this.element.parents('.quick-order-product-search').find('.ui-autocomplete').remove();
        },
        options:
        {
          cache:
          {},
          // init cache per instance
          focus: function()
          {
            return false;
          },
          // prevent textfield
          // value replacement on
          // item focus
          select: function(event, ui)
          {
            this.value = ui.item.code;
            this.blur();
            return false;
          }
        },
        _renderItem: function(ul, item)
        {
          var self = this;
          var productSearchRequest = self.options.productSearchRequest;
          if (!productSearchRequest)
          {
            if (item.type == "productResult")
            {
              $('.no-product-found').remove();
              $('.quick-order-product-search').removeClass("error");
              var entryPresent = false;
              $('.autocomplete-search-results').find('li').each(function()
              {
                var itemCode = $(this).find('.js-add-product-to-quick-order').attr('data-product-code');
                if (item.code == itemCode)
                {
                  entryPresent = true;
                }
              });
              if (entryPresent)
              {
                return $('.autocomplete-search-results');
              }
              else if (!entryPresent)
              {
                var renderHtml = "";
                if (item.image != null)
                {
                  renderHtml += "<img src='" + item.image + "'  />";
                }
                
                var productCode;
                if (item.variantCode == null || item.variantCode == '')
                {
                  productCode = item.code;
                }
                else
                {
                  productCode = item.variantCode;
                }
                renderHtml += "<div class='prod-name'>" + item.label + "</div>";
                renderHtml += "<a href='#' class='add-to-order js-add-product-to-quick-order' data-product-code='" + productCode + "'><span>Add to quick order</span></a>";
                return $("<li class='click-add-product-to-quick-order'>").data("item.autocomplete", item).append(renderHtml).appendTo('.autocomplete-search-results');
              }
            }
            else if (item.type == "noProductResult")
            {
              $('.autocomplete-search-results').html("");
              $('.quick-order-product-search').addClass("error");
              var renderHtml = $('.invalid-search').html();
              return $("<li class='no-product-found'>").data("item.autocomplete", item).append(renderHtml).appendTo('.autocomplete-search-results');
            }
          }
        },
        source: function(request, response)
        {
          $('.autocomplete-search-results').html("");
          var self = this;
          var autoSearchData = [];
          request.term = request.term.replace(new RegExp('#', 'gi'), ' ').replace(new RegExp('<>', 'gi'), ' ');
          var term = jQuery.trim(request.term.toLowerCase());
          if (term == "")
          {
            autoSearchData.push(
            {
              value: "Product not found.",
              type: "noProductResult"
            });
            $('.autocomplete-search-results').slideDown('slow');
            return response(autoSearchData);
          }
          if (term in self.options.cache)
          {
            $('.autocomplete-search-results').slideDown('slow');
            return response(self.options.cache[term]);
          }
          $.getJSON(self.options.autocompleteUrl,
          {
            term: jQuery.trim(request.term)
          }, function(data)
          {
            if (data.products != null)
            {
              $.each(data.products, function(i, obj)
              {
                autoSearchData.push(
                {
                  value: ACC.sanitizer.sanitize(obj.name),
                  code: obj.code,
                  variantCode: obj.searchedVariant,
                  desc: ACC.sanitizer.sanitize(obj.description),
                  concatDesc: ACC.sanitizer.sanitize(obj.concatenatedDescription),
                  manufacturer: ACC.sanitizer.sanitize(obj.manufacturer),
                  url: ACC.config.encodedContextPath + obj.url,
                  type: "productResult",
                  image: (obj.images != null && self.options.displayProductImages) ? obj.images[0].url : null
                  // prevent errors if obj.images = null
                });
              });
              $('.autocomplete-search-results').slideDown('slow');
            }
            if (data.products == null || data.products.length == 0)
            {
              autoSearchData.push(
              {
                value: "Product not found.",
                type: "noProductResult"
              });
              // return request.term;
            }
            $('.ui-helper-hidden-accessible').remove();
            self.options.cache[term] = autoSearchData;
            return response(autoSearchData);
          });
        }
      });

      $search = $(ACC.quickorder.$searchInputField);
      if ($search.length > 0)
      {
        $search.yautocomplete()
      }

      $('.quick-order-product-search input').blur(function()
      {
        if (!$(this).val())
        {
          $('.autocomplete-search-results').slideUp('slow');
          $('.autocomplete-search-results').html("");
        }
      });

      $('.quick-order-product-search input').keypress(function(event)
      {
        if (event.keyCode == 13)
        {
          event.preventDefault();
        }
      });
    },

    bindResetQuickSearch: function()
    {
      $('.js-search-input-field').bind('input', ACC.quickorder.clearQuickSearch);
    },

    bindAddToCartClick: function()
    {
      ACC.quickorder.$addToCartBtn.on("click", ACC.quickorder.addToCart);
    },
    
    bindAddProductRow: function()
    {
      $('.autocomplete-search-results').on('click', ACC.quickorder.$addProductToQuickOrder, ACC.quickorder.addInputRow);
    },

    bindUpdateProductRow: function()
    {
      ACC.quickorder.$tableBodyDiv.on('click', '.selecty-options li', ACC.quickorder.updateInputRow);
    },

    bindClearQuickOrderRow: function()
    {
      ACC.quickorder.$tableBodyDiv.on("click", ACC.quickorder.$removeQuickOrderRowBtn, ACC.quickorder.clearQuickOrderRow);
    },

    bindQuantityUpdated: function()
    {
      ACC.quickorder.$tableBodyDiv.on("focusout change", ACC.quickorder.$qtyInputField, ACC.quickorder.updateQuantity);
    },
    
    bindHandleRestore: function()
    {
    	$(window).on('load', function(){
    		$(document).ready(function()
    				{
    				ACC.quickorder.applySelectyToSelectOptions();	
    				ACC.quickorder.updatePriceInfo();
    				});
    			});
    },
    
    bindCreateTemplate: function ()
	{
    	ACC.savedcarts.charactersLeftInit();
		var createTemplate = false;
        var showCreateTemplateFormCallback = function () {
        if (ACC.quickorder.isLineItemPresentCheck() && ACC.quickorder.isQuantityPresentForAllCheck())
        {
        	var title = $('#createTemplate').data("createTemplateTitle");
          
            ACC.colorbox.open(title, {
                href: "#createTemplate",
                inline: true,
                width: "620px",
                onOpen: function () {
                	
                    if ($('#templateName').val()) {
                    	ACC.quickorder.disableCreateTemplateButton(true);
                    }
                },
                onComplete: function () {
                    $(this).colorbox.resize();
                    createTemplate = false;
                },
                onClosed: function () {
                    document.getElementById("createTemplateForm").reset();
                    ACC.quickorder.disableCreateTemplateButton(false);
                    ACC.savedcarts.charactersLeftInit();
                }
            });
        }
        };

        $(document).on("click",".js-create-template-link",function(e){
            e.preventDefault();
            ACC.common.checkAuthenticationStatusBeforeAction(showCreateTemplateFormCallback);
		});
		
		$(document).on("click",'#createTemplate #cancelCreateTemplateButton', function (e) {
			e.preventDefault();
			$.colorbox.close();
		});
		
		$('#templateName').keyup(function() {		
			// enable the save cart button
	 		$('#createTemplate #createTemplateButton').prop('disabled', this.value.trim() == "" ? true : false);  	
			// limit the text length 
            var maxchars = 255;
			var value=$('#localized_val').attr('value');
			var tlength = $(this).val().length;
			remain = maxchars - parseInt(tlength);
        	$('#remain').text(value+' : '+remain);
        	 $(".error-name").text("");
		});
		
         $('#templateDescription').keyup(function() {
			var maxchars = 255;
			var value=$('#localized_val').attr('value');
			var tlength = $(this).val().length;
			remain = maxchars - parseInt(tlength);
        	$('#remainTextArea').text(value+' : '+remain);
        	 $(".error-description").text("");
		});
		
		$(document).on("click",'#createTemplate #createTemplateButton', function (e) {
			e.preventDefault();
            createTemplate = true;
            if (createTemplate) {
             	ACC.quickorder.createTemplate(ACC.config.encodedContextPath + '/my-account/quick-order-templates/createTemplate');
                }
            ACC.quickorder.disableCreateTemplateButton(true);
		});
	},

    addToCart: function()
    {
      if (ACC.quickorder.isLineItemPresentCheck() && ACC.quickorder.isQuantityPresentForAllCheck())
      {
    	ACC.quickorder.disableAddToCartButton();
        $.ajax(
        {
          url: ACC.config.encodedContextPath + '/cart/addQuickOrder',
          type: 'POST',
          dataType: 'json',
          contentType: 'application/json',
          data: ACC.quickorder.getJSONDataForAddToCart(),
          async: false,
          success: function(response)
          {
        	  ACC.quickorder.handleAddToCartSuccess(response);
          },
          error: function(jqXHR, textStatus, errorThrown)
          {
        	ACC.quickorder.enableAddToCartButton();
            // log the error to the console
            console.log("The following error occurred: " + textStatus, errorThrown);
          }
        });
      }
    },
    
    handleAddToCartSuccess: function(response)
    {
      if ($(response.quickOrderErrorData).length > 0)
      {
        ACC.quickorder.disableBeforeUnloadEvent();
        var quickOrderErrorData = response.quickOrderErrorData;
        var error = false;
        $(ACC.quickorder.$qtyInputField).each(
          function()
          {
            var entrynumber = parseInt($(this).parents('.tr')
              .find('.entrynumber').val());
            $.each(quickOrderErrorData, function(key, value)
            {
              if (value.sku == entrynumber)
              {
                projectField.addClass('error');
                error = true;
              }
            });
          });
        if (error)
        {
          $('.project-name-invalid-error').css('display', 'block');
          return false;
        }
      }

      var lookup = {};
      response.quickOrderErrorData.forEach(function(el)
      {
        lookup[el.sku] = el.errorMsg;
      });

      $(ACC.quickorder.$qtyInputField).each(function()
      {
        var parentLi = ACC.quickorder.getCurrentParentLi(this);
        var sku = ACC.quickorder.findElement(parentLi, ACC.quickorder.$skuInputField).val();
        var errorMsg = lookup[sku];

        if (errorMsg)
        {
          ACC.quickorder.findElement(parentLi, ACC.quickorder.$skuValidationContainer).text(errorMsg);
        }
        else
        {
          ACC.quickorder.findElement(parentLi, ACC.quickorder.$removeQuickOrderRowBtn).trigger("mousedown");
        }
      });

     
      ACC.quickorder.redirectToCartPage();
      
    },

    getJSONDataForAddToCart: function()
    {
      return JSON.stringify(
      {
        "cartEntries": ACC.quickorder.getJSONDataForQuickOrderEntries()
      });
    },
    
    getJSONDataForCreateTemplate: function()
    {
      var templateName = $('#templateName').val();
      var templateDesc = $('#templateDesc').val();
      return JSON.stringify(
      {
        "cartEntries": ACC.quickorder.getJSONDataForQuickOrderEntries(),
    	"templateName":templateName,
    	"templateDesc":templateDesc
      });
    },
    
    getJSONDataForQuickOrderEntries: function()
    {
      var skusAsJSON = [];
      $(ACC.quickorder.$qtyInputField).each(function()
      {
        var qty = Number($(this).val());
        var entrynumber = parseInt($(this).parents('.tr').find('.entrynumber').val());
        if (qty > 0)
        {
          var sku = $(this).parents('.tr').find('.product-code').text();
          var baseproductcode = $(this).parents('.tr').find('.baseproduct-code').val();
          var baseproductname = $(this).parents('.tr').find('.baseproduct-name').val();
          skusAsJSON.push(
          {
            "product":
            {
              "code": sku,
              "baseProductName": baseproductname,
              "baseProduct": baseproductcode
            },
            "quantity": qty,
            "entryNumber": entrynumber
          });

        }
      });
      return skusAsJSON;
    },
    

    clearQuickSearch: function()
    {
      if ($('.js-search-input-field').val().length == 0)
      {
        $('.autocomplete-search-results').slideUp('slow');
        $('.autocomplete-search-results').html("");
        $('.quick-order-product-search').removeClass("error");
      }
    },

    clearQuickOrderRow: function(event)
    {
      event.preventDefault();
     // ACC.quickorder.hideLimitExceededMessage();
      $(event.target).closest(".tr").remove();
      ACC.quickorder.updatePriceAndStock(event);
      ACC.quickorder.handleBeforeUnloadEvent();
    },

    addInputRow: function(event)
    {
      event.preventDefault();
      var productCode = $(this).find('.js-add-product-to-quick-order').data("product-code");
      ACC.quickorder.handleAddInputRow(productCode);
    },

    handleAddInputRow: function(productCode)
    {
      ACC.quickorder.hideLimitExceededMessage();
      var count = ACC.quickorder.checkNumberOfLineItems();
      if (count < $(".quick-order-container #maxLimit").val())
      {
        var url = ACC.config.encodedContextPath + '/quickOrder/productInfo';
        var method = "GET";
        $.ajax(
        {
          url: url,
          data:
          {
            code: productCode   
          },
          type: method,
          async: false,
          success: function(data)
          {
            ACC.quickorder.handleAddInputSuccess(data);
          },
          error: function(xht, textStatus, ex)
          {
            alert("Failed to get product. Error details [" + xht + ", " + textStatus + ", " + ex + "]");
          }

        });
      }
      else
      {
        ACC.quickorder.hideProductSuggestions();
        $(".limit-exceed-error-message").css("display", "block");
      }
    },

    handleAddInputSuccess: function(data)
    {
      var lastEntryNumber = -1;
      if (ACC.quickorder.$tableBodyDiv.find(".tr:last").length != 0)
      {
        lastEntryNumber = parseInt(ACC.quickorder.$tableBodyDiv.find(".tr:last").find(".entrynumber").val());
      }
      ACC.quickorder.$tableBodyDiv.append(data);
      ACC.quickorder.$tableBodyDiv.find(".tr:last").find(".entrynumber").val(lastEntryNumber + 1);
      var targetClasslist = ACC.quickorder.$tableBodyDiv.find(".tr:last").find(".tablerow").attr("class");
  	  var indexoftr = ACC.quickorder.$tableBodyDiv.find(".tr:last").index();
  	  var element = ACC.quickorder.findTargetTd(targetClasslist, indexoftr);

      ACC.quickorder.hideProductSuggestions();
      ACC.quickorder.hideNoProductAddedMessage();
      ACC.quickorder.disableAddToCartButton();
      ACC.quickorder.applySelectyToSelectOptions();
      var qtyField = ACC.quickorder.$tableBodyDiv.find(".tr:last").find(".js-quick-order-qty");
      if(element !=null && !ACC.quickorder.isAllDropdownSelectedCheck(qtyField)){
			ACC.quickorder.changeDropdown(element);
		}
      ACC.quickorder.disableEmptyDropdown(ACC.quickorder.$tableBodyDiv.find(".tr:last"));

      if(ACC.quickorder.$tableBodyDiv.find(".tr:last").find(".tablerow").length == 1)
      {
    	 if(ACC.quickorder.$tableBodyDiv.find(".tr:last").find(".tablerow").find("ul li").length == 2)
    	  {
    		 $('.selecty-options li.selected').trigger('click');
    	  }
      }
    },

    updateInputRow: function(event)
    {
      ACC.quickorder.hideLimitExceededMessage();
      if (!$(event.target).hasClass("disabled"))
      {

        var object = $(event.target).data("value");
        var url = ACC.config.encodedContextPath + '/quickOrder/productInfo';
        var method = "GET";
        $.ajax(
        {
          url: url,
          data:
          {
            code: object.productcode
          },
          type: method,
          success: function(data)
          {
            ACC.quickorder.handleUpdateInputSuccess($(event.target), object.isLastDropdown, data);
          },
          error: function(xht, textStatus, ex)
          {
            console.log("The following error occurred: " + textStatus, ex);
          }

        });
      }
    },


    handleUpdateInputSuccess: function(target, isLastDropdown, data)
    {
      var targetClasslist = target.closest(".tablerow").attr("class");
      var indexoftr = $(".tr").index(target.closest(".tr"));
      var entryNumber = parseInt(target.closest(".tr").find(".entrynumber").val());

      var wasValidRow = false;
      var qtyField = $(ACC.quickorder.$qtyInputField).get(indexoftr);
      var qty = Number($(qtyField).val());
      if (qty > 0 && $(qtyField).attr("data-updated") == "true")
      {
        wasValidRow = true;
      }

      target.closest(".tr").replaceWith(data);
      var element = ACC.quickorder.findTargetTd(targetClasslist, indexoftr);
      if (element == null)
      {
        // again try to find target.
        element = ACC.quickorder.findTargetTd(targetClasslist, indexoftr);
      }
      element.closest(".tr").find(".entrynumber").val(entryNumber);
      ACC.quickorder.applySelectyToSelectOptions();
      ACC.quickorder.changeDropdown(element);
      qtyField = $(ACC.quickorder.$qtyInputField).get(indexoftr);
      if (ACC.quickorder.isAllDropdownSelectedCheck(qtyField) || wasValidRow)
      {
        ACC.quickorder.updatePriceInfo();
      }
    },
    handleErrorsForDropDown : function(indexToMarkZero, isLastDropdown)
    {
    	 var outOfStockLineItem = ACC.quickorder.checkOutOfStockLineItems(indexToMarkZero, isLastDropdown);
         ACC.quickorder.toggleErrorMessageDisplay(outOfStockLineItem, ".out-of-stock-message");
         var allDropDownSelected = ACC.quickorder.checkLastDropDownSelectedForLineItems();
         var quantityMoreThanStockLineItem = ACC.quickorder.checkResponseBasedErrorByToolTip(".qtyMoreThanStock", indexToMarkZero);
         var noQuantityLineItem = ACC.quickorder.checkQuantityForLineItems();
         ACC.quickorder.toggleErrorMessageDisplay(noQuantityLineItem, ".no-quantity-selected-message");
         ACC.quickorder.toggleErrorMessageDisplay(quantityMoreThanStockLineItem, ".qty-more-than-stock-message");
         ACC.quickorder.toggleQuantityClassAndCartButton(outOfStockLineItem, noQuantityLineItem, quantityMoreThanStockLineItem,allDropDownSelected);

    },
 // check out of stock for all line items
	checkOutOfStockLineItems: function(indexToMarkZero, isLastDropdown)
    {

      var oOSProductPresent = [];
      var lineItemsCount = ACC.quickorder.checkNumberOfLineItems();
      for (var i = 1; i <= lineItemsCount; i++)
      {
        oOSProductPresent.push(0);
      }
      $(ACC.quickorder.$qtyInputField).each(function()
      {
    	var availableStock = $(this).parents('.tr').find('.available-stock').val();
      	if(availableStock == 0 || availableStock =="undefined")
      	{
          oOSProductPresent[i] = 1;
        }
        else
        {
          oOSProductPresent[i] = 0;
        }
      });
      //if last drop down is not selected, then product cannot be OOS
      if (!isLastDropdown)
      {
        oOSProductPresent[indexToMarkZero] = 0;
      }
      return oOSProductPresent;
    },
	
    
    // check for QtyGreaterThanStock from tooltip
    checkResponseBasedErrorByToolTip: function(className, indexToMarkZero)
    {
      var errorValidForLineItem = [];
      $(ACC.quickorder.$qtyInputField).each(function()
      {
    	var availableStock = $(this).parents('.tr').find('.available-stock').val();
    	var qty = isNaN(Number($(this).val())) ? 0 : Number($(this).val());
        if (($(this).attr("data-toggle") != "tooltip" || $(this).attr("data-original-title") == $(className).val()) && (qty > availableStock))
        {
          errorValidForLineItem.push(1);
        }
        else
        {
          errorValidForLineItem.push(0);
        }
      });
      //if this line item had previous error, changing drop down should reset the error
      //errorValidForLineItem[indexToMarkZero] = 0;
      return errorValidForLineItem;
    },
    
    //combine all the errors
    toggleQuantityClassAndCartButton: function(outOfStockLineItem,noQuantityLineItem, quantityMoreThanStockLineItem, allDropDownSelected)
    {

      var quantityErrorInLineItem = [];
      var errorInQtyField = 0;
      var lineItemsCount = ACC.quickorder.checkNumberOfLineItems();
      for (var i = 0; i < lineItemsCount; i++)
      {
        var sum = noQuantityLineItem[i] + quantityMoreThanStockLineItem[i];
        quantityErrorInLineItem.push(sum);
        errorInQtyField += sum;
      }

      //Highlight the appropriate qtyfield caused by any error
      $(ACC.quickorder.$qtyInputField).each(function(i)
      {
        if (typeof(quantityErrorInLineItem) != "undefined" && quantityErrorInLineItem[i] > 0)
        {
          $(this).addClass("error");
        }
        else
        {
          $(this).removeClass("error");
        }
        //variable to check to not to disable tooltip, once enabled
          var tooltipEnabled = false;
          
          if (quantityMoreThanStockLineItem[i] > 0)
          { 
            $(this).attr("data-toggle", "tooltip");
            $(this).attr("data-original-title", $(".qtyMoreThanStock").val());
            $(this).tooltip('enable');
            tooltipEnabled = true;
          }
          else
          {
            $(this).tooltip('disable');
            $(this).removeAttr("data-toggle");
          }

      });

      if (errorInQtyField > 0 || !allDropDownSelected)
      {
        ACC.quickorder.disableAddToCartButton();
      }
      else
      {
        ACC.quickorder.enableAddToCartButton();
      }

    },
    
    updateQuantity: function(event)
    {
    if (($(this).attr("data-prevQty") != $(this).val()))
      {
        if (ACC.quickorder.isAllDropdownSelectedCheck($(event.target)))
        {
          $(event.target).attr('data-updated', 'true');
          $(event.target).attr('data-prevQty', $(this).val()); 
         $(".not-all-dropdown-selected-message").css("display", "none");       
          ACC.quickorder.updatePriceAndStock(event);
        }
        else
        {
        	ACC.quickorder.checkLastDropDownSelectedForLineItems();
        	ACC.quickorder.disableAddToCartButton();
        }
        ACC.quickorder.hideLimitExceededMessage();    
      }
   },
    
    updatePriceAndStock: function(event)
    {
    	var availableStock = $(event.target).parents('.tr').find('.available-stock').val();
        var qty = isNaN(Number($(event.target).val())) ? 0 : Number($(event.target).val());
        var indexoftr = $(".tr").index(event.target.closest(".tr"));
   	  	if(qty <= availableStock)
   	  	{
            ACC.quickorder.updatePriceInfo();  
    	} 
   	  	else 
   	  	{
   	  	$(event.target).parents('.tr').find('.item-total-price .price-details2 .priceValue').text(""); 
   	  	$('.final-price').text("");
   	  	
   	  	}
   	  	ACC.quickorder.handleErrorsForDropDown(indexoftr,true); 	

    },
    
    
    updatePriceInfo : function()
    {
    	var totalPrice = 0;
    	var currencySymbol = $('.currency-symbol').val();
    	$(ACC.quickorder.$qtyInputField).each(function()
    	{
    			var availableStock = $(this).parents('.tr').find('.available-stock').val();
    	    	var qty = isNaN(Number($(this).val())) ? 0 : Number($(this).val());
    	    	
    	    	if(qty <= availableStock)	
    	    	{
    	    		var basePrice = $(this).parents('.tr').find('.base-price').val();
    	    		var totalItemPrice = basePrice*qty;
    	    		totalPrice = totalPrice + totalItemPrice;
                    var currencySymbol = $(this).parents('.tr').find('.currency-symbol').val();
                    if(totalItemPrice > 0)
                    {
                        $(this).parents('.tr').find('.item-total-price .price-details2 .priceValue').text(ACC.quickorder.getPriceWithPrecision(currencySymbol, totalItemPrice));
                    }
                    else
                    {
                         $(this).parents('.tr').find('.item-total-price .price-details2 .priceValue').text("");                                         
                    }
    	    		
                }         
	    });
    	 
    	if(totalPrice > 0)
    	{
            $('.final-price').text(ACC.quickorder.getPriceWithPrecision(currencySymbol, totalPrice))
    	}
    	else
    	{
            $('.final-price').text("");
       	}
    },
    findMatchingIndexOfRow: function(rowindex, entryNumber)
    {
      var matchFound = false;
      $(".entrynumber").each(function(index, value)
      {
        if (Number($(this).val()) == Number(entryNumber))
        {
          rowindex = index;
          matchFound = true;
        }
      });
      if (!matchFound)
      {
        rowindex = $(".entrynumber").length;
      }
      return rowindex;
    },

    validateQuantity: function(qtyField)
    {
      var isValid = true;
      if (qtyField != null)
      {
        var qty = isNaN(Number($(qtyField).val())) ? 0 : Number($(qtyField).val());
        if (qty < 0)
        {
          isValid = false;
        }
      }
      return isValid;
    },
   
    findTargetTd: function(targetClasslist, indexoftr)
    {
      var element;
      $(".tablerow").each(function(index)
      {
        if ($(this).hasClass(targetClasslist) && $(".tr").index($(this).closest(".tr")) == indexoftr)
        {
          element = $(this);
        }
      });
      return element;
    },

    applySelectyToSelectOptions: function()
    {
      var selectoptions = $(".select-options");
      selectoptions.each(function(i, obj)
      {
        if (!$(obj).hasClass("selecty-applied"))
        {
          $(obj).selecty();
          $(obj).addClass("selecty-applied");
        }
      });
     
    },

    changeDropdown: function(selectedtd)
    {
      var tableRow = $(selectedtd).closest(".tr");
      var selectedDropDownIndex = selectedtd.find(".loopindex").val();
      var previous = null;
      var count = tableRow.find(".loopindex").length;
      tableRow.find(".loopindex").each(function(index)
      {
        // if dropdown's index is present after selected dropdown
        if (parseInt($(this).val()) > parseInt(selectedDropDownIndex))
        {

          if ($(previous).parent().hasClass("disabled"))
          {
            $(this).parent().find('ul li:not(:first)').remove();
            $(this).parent().find("a.selecty-selected").text("N/A");
            $(this).parent().addClass("disabled");

          }
          //If dropdown is next one and have more than one values
          else if (parseInt($(this).val()) == parseInt(selectedDropDownIndex) + 1 && ($(this).parent().find('ul li:not(:first)').length > 1))
          {
            $(this).parent().find("li.selected").removeClass("selected");
            $(this).parent().find("a.selecty-selected").text($(this).parent().find("li").first().text());
          }
          // if previous dropdown was empty
          else if (previous.parent().find('ul li:not(:first)').length <= 1)
          {
            $(this).parent().find("li.selected").removeClass("selected");
            $(this).parent().find("a.selecty-selected").text($(this).parent().find("li").first().text());
          }
          // otherwise 
          else
          {
            $(this).parent().find('ul li:not(:first)').remove();
            //$(this).parent().find("a.selecty-selected").text($(this).parent().find("li").first().text());
            $(this).parent().find("a.selecty-selected").text("N/A");
            $(this).parent().addClass("disabled");
          }
        }
        previous = $(this)
      });
    },
    disableEmptyDropdown: function(tableRow)
    {
      tableRow.find(".loopindex").each(function(index)
      {
          if ($(this).parent().find('ul li:not(:first)').length <= 0)
          {
            $(this).parent().find("a.selecty-selected").text($(this).parent().find("li").first().text());
            $(this).parent().addClass("disabled");
          }
      });
    },

    handleBeforeUnloadEvent: function()
    {
      if (ACC.quickorder.isAnySkuPresent())
      {
        ACC.quickorder.disableBeforeUnloadEvent();
        ACC.quickorder.enableBeforeUnloadEvent();
      }
      else
      {
        ACC.quickorder.disableBeforeUnloadEvent();
      }
    },

    disableBeforeUnloadEvent: function()
    {
      $(window).off('beforeunload', ACC.quickorder.beforeUnloadHandler);
    },

    enableBeforeUnloadEvent: function()
    {
      $(window).on('beforeunload', ACC.quickorder.beforeUnloadHandler);
    },

    beforeUnloadHandler: function()
    {
      return ACC.quickorder.$quickOrderLeavePageMsg;
    },
    isAnySkuPresent: function()
    {
      var present = false;
      $(ACC.quickorder.$skuInputField).each(function()
      {
        var str = jQuery.trim(this.value); // .trim() may need a shim
        if (str)
        {
          present = true;
          return false;
        }
      });
      return present;
    },

    getCurrentParentLi: function(currentElement)
    {
      return $(currentElement).closest(ACC.quickorder.$jsLiContainer);
    },

    findElement: function(currentElement, toFind)
    {
      return $(currentElement).find(toFind);
    },

    redirectToCartPage: function()
    {
      var cartPageUrl = $(".quick-order-container #cartPageUrl").val()
      if (cartPageUrl)
      { //check if cart page url is not empty
        window.location.replace(cartPageUrl);
      }
    },
    
    redirectToTemplatePage: function()
    {
      var templatePageUrl = $(".quick-order-container #templatePageUrl").val()
      if (templatePageUrl)
      { //check if cart page url is not empty
        window.location.replace(templatePageUrl);
      }
    },
    
    isLineItemPresentCheck: function()
    {
      var isValid = true;
      if ($(ACC.quickorder.$qtyInputField).length <= 0)
      {
        ACC.quickorder.showNoProductAddedMessage();
        isValid = false;
      }
      return isValid
    },
    showOutOfStockProductAddedMessage: function()
    {
      $(".out-of-stock-message").css("display", "block");
      ACC.quickorder.disableAddToCartButton();
    },
    showNoProductAddedMessage: function()
    {
      $(".no-product-added-message").css("display", "block");
      ACC.quickorder.disableAddToCartButton();
    },
    hideNoProductAddedMessage: function()
    {
      $(".no-product-added-message").css("display", "none");
    },
    hideLimitExceededMessage: function()
    {
      $(".limit-exceed-error-message").css("display", "none");
    },
    hideProductSuggestions: function()
    {
      $('.autocomplete-search-results').slideUp('slow');
      $('.autocomplete-search-results').html("");
      $('.js-search-input-field').val("");
    },
    showNoQuantitySelectedMessage: function(qtyField)
    {
      qtyField.addClass("error");
      $(".no-quantity-selected-message").css("display", "block");
      ACC.quickorder.disableAddToCartButton();
    },

    // Is quantity present in the input field after all drop down check
    isQuantityPresentForAllCheck: function()
    {
      var isValid = true;

      $(ACC.quickorder.$qtyInputField).each(function()
      {
        var qty = Number($(this).val());
        if (qty <= 0)
        {
          isValid = false;
          ACC.quickorder.showNoQuantitySelectedMessage($(this));
        }
      });
      return isValid
    },
    
    //Toggle message by passing line item and error class
    toggleErrorMessageDisplay: function(lineItems, className)
    {
      var showMessage = false;
      for (var i = 0; i < lineItems.length; i++)
      {
        if (lineItems[i] > 0)
        {
          showMessage = true;
          break;
        }
      }
      if (showMessage)
      {
        $(className).css("display", "block");
      }
      else
      {
        $(className).css("display", "none");
      }
    },
    //	check qty for all lineitems if there last drop down is selected
    checkQuantityForLineItems: function()
    {
      var quantityNotPresentInLineItem = [];
      $(ACC.quickorder.$qtyInputField).each(function()
      {
        var qty = Number($(this).val());
        if (qty <= 0)
        {
          quantityNotPresentInLineItem.push(1);
        }
        else
        {
          quantityNotPresentInLineItem.push(0);
        }
      });
      return quantityNotPresentInLineItem;
    },
    
    // Check the number of items in the quick order
    checkNumberOfLineItems: function()
    {
      var noOfLineItem = 0;
      $(ACC.quickorder.$qtyInputField).each(function()
      {
        noOfLineItem++;
      });
      return noOfLineItem;
    },
    //Check if last dropdowns are seleted for all the line items
    checkLastDropDownSelectedForLineItems: function()
    {
      var showErrorMessage = false;
      $(ACC.quickorder.$qtyInputField).each(function()
      {
        var qty = Number($(this).val());
        if (qty >= 0)
        {
          tableRow = $(this).closest(".tr");
          $(tableRow).find("ul").each(function(index, value)
          {
            if ($(this).find("li:not(:first)").length > 0 && $(this).find("li.selected").length == 0)
            {
              showErrorMessage = true;
              $(this).siblings().addClass('select-error');
            }
          });
        }
      });
      if (showErrorMessage)
      {
        $(".not-all-dropdown-selected-message").css("display", "block");
      }
      else
      {
        $(".not-all-dropdown-selected-message").css("display", "none");
      }
      return !showErrorMessage;
    },
    //check if all dropdowns are selected for the passed Qty field
    isAllDropdownSelectedCheck: function(qtyField)
    {
      var isValid = true;
      if (qtyField != null)
      {
        tableRow = $(qtyField).closest(".tr");
        $(tableRow).find("ul").each(function(index, value)
        {
          if ($(this).find("li:not(:first)").length > 0 && $(this).find("li.selected").length == 0)
          {
            isValid = false;
            return false;
          }
        });
      }
      return isValid;
    },

    enableAddToCartButton: function()
    {
      ACC.quickorder.$addToCartBtn.removeAttr('disabled');
      ACC.quickorder.$createTemplateBtn.removeAttr('disabled');
    },
    disableAddToCartButton: function()
    {
      ACC.quickorder.$addToCartBtn.attr('disabled', 'disabled');
      ACC.quickorder.$createTemplateBtn.attr('disabled', 'disabled');
    },
    
	disableCreateTemplateButton: function(value) {
		ACC.quickorder.$createTemplateBtn.prop('disabled', value);
	},

    getPriceWithPrecision: function(currencySymbol, totalItemPrice)
    {
       return currencySymbol + ACC.quickorder.getLocalePrice(totalItemPrice.toFixed(2));
    },

    getLocalePrice: function(alteredPrice)
    {
      var locale = $(".locale").val();
      alteredPrice = alteredPrice.toString();
      var numberBeforeDecimal = alteredPrice.split(".")[0];
      return alteredPrice.replace(numberBeforeDecimal, parseFloat(numberBeforeDecimal).toLocaleString(locale));
    },
    createTemplate: function(targetUrl)
    {
      if (ACC.quickorder.isLineItemPresentCheck() && ACC.quickorder.isQuantityPresentForAllCheck())
      {
    	ACC.quickorder.disableAddToCartButton();
        $.ajax(
        {
          url: targetUrl,
          type: 'POST',
          dataType: 'json',
          contentType: 'application/json',
          traditional: true,
          data: ACC.quickorder.getJSONDataForCreateTemplate(),
          async: false,
          success: function(response)
          {
        	ACC.quickorder.handleTemplate(response);
        	ACC.quickorder.enableAddToCartButton();        	
          },
          error: function(jqXHR, textStatus, errorThrown)
          {
        	ACC.quickorder.enableAddToCartButton();
            // log the error to the console
            console.log("The following error occurred: " + textStatus, errorThrown);
          }
        });
      }
    },
    handleTemplate : function(response)
    { 
    	if(response.templateName != null)
    	{
	    	$('#error-template').fadeIn(); 
	        $("#templateName").addClass("error");
	        $('#error-template #close').after('<span>'+response.templateName+'</span>');
	        $('#createTemplate #createTemplateButton').prop('disabled',true);
    	}
    	
    	else if(response.templateDesc != null)
    	{
	    	$('#error-template').fadeIn(); 
	        $("#templateDesc").addClass("error");
	        $('#error-template #close').after('<span>'+response.templateDesc+'</span>');
	        $('#createTemplate #createTemplateButton').prop('disabled',true);
    	}	
    	else if(response.success != null)
    	{
    		ACC.quickorder.redirectToTemplatePage();
        }
    },
  };
}