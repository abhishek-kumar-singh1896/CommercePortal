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
			    	 $('.flexslider').flexslider({
			    		    animation: "slide",
			    		    animationLoop: true,
			    		    itemWidth: 210,
			    		    itemMargin: 10,
			    		    minItems: 1,
			    		    maxItems: 4
			    		  });
			    	 $('#myProductModal').show();
			    	 $('.close').click(function(){
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