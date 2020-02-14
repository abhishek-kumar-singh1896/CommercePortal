ACC.productListNav = {

	_autoload: [
	    "productPageNav"
	],
	
	productPageNav: function(){
		$(document).ready(function () {
			
			var sliderElement = $('#slider-range');
			var minValue = $("#my_min");
			var maxValue = $("#my_max");
			
			sliderElement.slider({
		    
			      range: true,
			      min: 0,
			      max: 5000,
			      values: [ 0, 100 ],
			      slide: function( event, ui ) {
			    	// alert()
			        $( '#my_min' ).val( "$" + ui.values[ 0 ]);
			        $('#my_max').val( "$" + ui.values[ 1 ] );
			        /* conssole.log(minValue," : ",maxValue) */
			      },
			      stop: function(event, ui){
			    	  
			    	  if ((typeof minValue.val() !== "undefined") && (typeof maxValue.val() !== "undefined")) {
				    	  ajaxCallForPrice(minValue.val(), maxValue.val())
				    	  //alert(ACC.config.encodedContextPath +'/Animal-Management/c/Animal%20Management/'+$('#my_min').val()+'/'+$('#my_max').val());
			    	  }
			      }
		    });
		    
			minValue.val( "$" + sliderElement.slider( "values", 0 ));
			maxValue.val( "$" + sliderElement.slider( "values", 1 ));
		});
		
		function ajaxCallForPrice(min,max){
			
			var pathName = $(location).attr('pathname');
			console.log(window.location.search);
			$.ajax({
                url: pathName,
                type: 'GET',
                data: {"price": min+'-'+max},
                success: function (response) {
                  // console.log(response)
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    
                }
            });
	      }
		    
	}
}