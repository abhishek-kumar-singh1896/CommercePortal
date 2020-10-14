ACC.viewAlternativeProducts = {

    _autoload: [ "showAlternativeProducts" ],

 showAlternativeProducts : function(){
	 
	
		 $(document).on("click", ".view-alternative-products", function (e){
			 
			   var productCode = $(this).attr('data-product-code');
			 $.ajax({
				 type : "GET",
			     url : ACC.config.encodedContextPath + "/p/"+productCode+"/showAlternativeProducts",
			     
			     success : function(data)
			     {
			    	 $('#viewAlternativeProductPopUp').append(data);
			    	 var is_iPad = navigator.userAgent.match(/iPad/i) != null;
			    	 var productLength = $('ul.productsPopUp > li').length;
			    	 var widthLength = $('ul.productsPopUp > li').width();
			    	 var width = 250*productLength;
			    	 
			    	 if(!is_iPad){
			    		 if ( $('ul.productsPopUp li').length > 1 ) { 
					    		$('.popUpTitle').css('width', width + "px");
					    	 }
					    	 else {
					    		 $('.popUpTitle').css('width', '250px');
					       }
			    	 }
			    	 else{
			    		 if ( $('ul.productsPopUp li').length > 1 ) { 
					    		$('.popUpTitle').css('width', "auto");
					    	 }
					    	 else {
					    		 $('.popUpTitle').css('width', '250px');
					       }
			    	 }
			    	 
			    	 $('.flexslider').flexslider({
			    		    animation: "slide",
			    		    animationLoop: true,
			    		    slideshow: false,
			    		    itemWidth: 250,
			    		    itemMargin: 10,
			    		    minItems: 1,
			    		    maxItems: 4
			    		  });
			    	 $('#myProductModal').show();
			    	 $('.close').click(function(){
				    	 $('#viewAlternativeProductPopUp').empty();
			    	        $('.modaldata').hide();
			    	     });
			    	 
			     },
			     error : function(data)
			     {
			    	 console.log('An error occured');
			    	 console.log(data);
			     },
		 });
		});
 }
};