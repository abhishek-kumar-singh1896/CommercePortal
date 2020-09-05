ACC.productDetail = {

    _autoload: [
        "initPageEvents",
        "bindVariantOptions",
        "bindDownloadPDPClick",
        ["bindVideoPlayPause", $(".with-video").length != 0]
    ],


    checkQtySelector: function (self, mode) {
        var input = $(self).parents(".js-qty-selector").find(".js-qty-selector-input");
        var inputVal = parseInt(input.val());
        var max = input.data("max");
        var minusBtn = $(self).parents(".js-qty-selector").find(".js-qty-selector-minus");
        var plusBtn = $(self).parents(".js-qty-selector").find(".js-qty-selector-plus");

        $(self).parents(".js-qty-selector").find(".btn").removeAttr("disabled");

        if (mode == "minus") {
            if (inputVal != 1) {
                ACC.productDetail.updateQtyValue(self, inputVal - 1)
                if (inputVal - 1 == 1) {
                    minusBtn.attr("disabled", "disabled")
                }

            } else {
                minusBtn.attr("disabled", "disabled")
            }
        } else if (mode == "reset") {
            ACC.productDetail.updateQtyValue(self, 1)

        } else if (mode == "plus") {
        	if(max == "FORCE_IN_STOCK") {
        		ACC.productDetail.updateQtyValue(self, inputVal + 1)
        	} else if (inputVal <= max) {
                ACC.productDetail.updateQtyValue(self, inputVal + 1)
                if (inputVal + 1 == max) {
                    plusBtn.attr("disabled", "disabled")
                }
            } else {
                plusBtn.attr("disabled", "disabled")
            }
        } else if (mode == "input") {
            if (inputVal == 1) {
                minusBtn.attr("disabled", "disabled")
            } else if(max == "FORCE_IN_STOCK" && inputVal > 0) {
            	ACC.productDetail.updateQtyValue(self, inputVal)
            } else if (inputVal == max) {
                plusBtn.attr("disabled", "disabled")
            } else if (inputVal < 1) {
                ACC.productDetail.updateQtyValue(self, 1)
                minusBtn.attr("disabled", "disabled")
            } else if (inputVal > max) {
                ACC.productDetail.updateQtyValue(self, max)
                plusBtn.attr("disabled", "disabled")
            }
        } else if (mode == "focusout") {
        	if (isNaN(inputVal)){
                ACC.productDetail.updateQtyValue(self, 1);
                minusBtn.attr("disabled", "disabled");
        	} else if(inputVal >= max) {
                plusBtn.attr("disabled", "disabled");
            }
        }

    },

    updateQtyValue: function (self, value) {
        var input = $(self).parents(".js-qty-selector").find(".js-qty-selector-input");
        var addtocartQty = $(self).parents(".addtocart-component").find("#addToCartForm").find(".js-qty-selector-input");
        var configureQty = $(self).parents(".addtocart-component").find("#configureForm").find(".js-qty-selector-input");
        input.val(value);
        addtocartQty.val(value);
        configureQty.val(value);
    },

    initPageEvents: function () {
    	
        $(document).on("click", '.js-qty-selector .js-qty-selector-minus', function () {
            ACC.productDetail.checkQtySelector(this, "minus");
        })

        $(document).on("click", '.js-qty-selector .js-qty-selector-plus', function () {
            ACC.productDetail.checkQtySelector(this, "plus");
        })

        $(document).on("keydown", '.js-qty-selector .js-qty-selector-input', function (e) {

            if (($(this).val() != " " && ((e.which >= 48 && e.which <= 57 ) || (e.which >= 96 && e.which <= 105 ))  ) || e.which == 8 || e.which == 46 || e.which == 37 || e.which == 39 || e.which == 9) {
            }
            else if (e.which == 38) {
                ACC.productDetail.checkQtySelector(this, "plus");
            }
            else if (e.which == 40) {
                ACC.productDetail.checkQtySelector(this, "minus");
            }
            else {
                e.preventDefault();
            }
        })

        $(document).on("keyup", '.js-qty-selector .js-qty-selector-input', function (e) {
            ACC.productDetail.checkQtySelector(this, "input");
            ACC.productDetail.updateQtyValue(this, $(this).val());

        })
        
        $(document).on("focusout", '.js-qty-selector .js-qty-selector-input', function (e) {
            ACC.productDetail.checkQtySelector(this, "focusout");
            ACC.productDetail.updateQtyValue(this, $(this).val());
        })

        $("#Size").change(function () {
            changeOnVariantOptionSelection($("#Size option:selected"));
        });

        $("#variant").change(function () {
            changeOnVariantOptionSelection($("#variant option:selected"));
        });

        $(".selectPriority").change(function () {
            window.location.href = $(this[this.selectedIndex]).val();
        });

        function changeOnVariantOptionSelection(optionSelected) {
            window.location.href = optionSelected.attr('value');
        }
    },

    bindVariantOptions: function () {
        ACC.productDetail.bindCurrentStyle();
        ACC.productDetail.bindCurrentSize();
        ACC.productDetail.bindCurrentType();
    },

	bindVideoPlayPause : function() {
		window.onload = function() {
			setTimeout(function() {
				$('.slide video').bind('play pause', function(e) {
					var currentVideo = $(this)[0];
					if (currentVideo.paused === true) {
						$(currentVideo.closest('.slide')).find('.overlayText').show();
					} else {
						$(currentVideo.closest('.slide')).find('.overlayText').hide();
					}
				});
			}, 1000);

		};
	},
	
    bindCurrentStyle: function () {
        var currentStyle = $("#currentStyleValue").data("styleValue");
        var styleSpan = $(".styleName");
        if (currentStyle != null) {
            styleSpan.text(": " + currentStyle);
        }
    },

    bindCurrentSize: function () {
        var currentSize = $("#currentSizeValue").data("sizeValue");
        var sizeSpan = $(".sizeName");
        if (currentSize != null) {
            sizeSpan.text(": " + currentSize);
        }
    },

    bindCurrentType: function () {
        var currentSize = $("#currentTypeValue").data("typeValue");
        var sizeSpan = $(".typeName");
        if (currentSize != null) {
            sizeSpan.text(": " + currentSize);
        }
    },
    bindDownloadPDPClick : function(e){
    	
    	$(document).on('click', '.product-detail-print-btn', function(e){
    		var productNumber=document.getElementById("downloadPDF").value;
    	ACC.productDetail.printProduct(productNumber);
		});	
	},
	
	printProduct : function(productNumber) {
		var url = ACC.config.encodedContextPath+'/p/'+productNumber+'/downloadProductDetails';
		 		$.ajax({
        	url: url,
            type: 'GET',
           async: true,
            success: function (data) {
               $('#print-product-new').html(data);
               $('#print-product-new').css('display','block');
       	     	var pdfFooterDetail = document.getElementById('PDFFooterDetail');
       	     	var pdfFooter = document.getElementById('PDFFooter');
       	     	var quotes = document.getElementById('print-product-new');
                   html2canvas(quotes).then(function (canvas){
                      var imgData = canvas.toDataURL('image/png');
                      var doc = new jsPDF('p', 'pt','a4');
                      var imgWidth = 600; 
             	      var pageHeight = 840;  
             	      var imgHeight = (canvas.height * imgWidth / canvas.width)+10;
                      if(imgHeight<630){
                 	      doc.addImage(imgData, 'PNG', 0, 0, imgWidth, imgHeight);
                 	      doc.addImage(pdfFooterDetail, 'JPEG', 0, 640, imgWidth, 140);
                 	      doc.addImage(pdfFooter, 'JPEG', 0, 780, imgWidth, 60);
                      }else{
                    	  if(imgHeight<700 && imgHeight>630){
                    		  doc.addImage(imgData, 'PNG', 0, 0, imgWidth, imgHeight-70);
                     	      doc.addImage(pdfFooterDetail, 'JPEG', 0, 640, imgWidth, 140);
                     	      doc.addImage(pdfFooter, 'JPEG', 0, 780, imgWidth, 60);
                    	  }else{
                    		  var heightLeft = imgHeight;
                     	      var position = 0;
                     	      doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight-70);
                     	      doc.addImage(pdfFooter, 'JPEG', 0, 780, imgWidth, 60);
                     	      heightLeft -= pageHeight;
                    	      while (heightLeft >= 0) {
                    	    	if(heightLeft<pageHeight){
                    	    		pageNo=pageNo+1;
                        	        position = heightLeft - imgHeight;
                        	        doc.addPage();
                        	        doc.addImage(imgData, 'PNG', 0, position+100, imgWidth, imgHeight-210);
                        	        heightLeft -= pageHeight;
                        	        doc.addImage(pdfFooterDetail, 'JPEG', 0, 640, imgWidth, 140);
                         	        doc.addImage(pdfFooter, 'JPEG', 0, 780, imgWidth, 60);
                    	    	}else{
                    	    		pageNo=pageNo+1;
                        	        position = heightLeft - imgHeight;
                        	        doc.addPage();
                        	        doc.addImage(imgData, 'PNG', 0, position+100, imgWidth, imgHeight-70);
                        	        heightLeft -= pageHeight;
                         	        doc.addImage(pdfFooter, 'JPEG', 0, 780, imgWidth, 60);
                    	    	}
                    	      }
                    	  }
                    	  }
             	      doc.save( 'ProductDetails-'+productNumber+'.pdf');
                });
               $('#print-product-new').css('display','none');
               $('.print-product-new').html("");
               
               },
            error: function (jqXHR, textStatus, errorThrown) {
                // log the error to the console
                console.log("The following error occurred: " + textStatus, errorThrown);
            }
		
		 });
	}
};