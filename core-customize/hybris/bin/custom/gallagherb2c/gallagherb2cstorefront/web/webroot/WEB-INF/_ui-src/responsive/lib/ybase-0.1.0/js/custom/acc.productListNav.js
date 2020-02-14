ACC.productListNav = {

	_autoload: [
	    "productPageNav"
	],
	
	productPageNav: function(){
		$(document).ready(function () {
			
			var sliderElement = $('#slider-range');
			
			sliderElement.slider({
		    
		      range: true,
		      min: 0,
		      max: 5000,
		      values: [ 900, 1900 ],
		      slide: function( event, ui ) {
		    	// alert()
		        $( '#my_min' ).val( "$" + ui.values[ 0 ]);
		        $('#my_max').val( "$" + ui.values[ 1 ] );
		        /* conssole.log(minValue," : ",maxValue) */
		      },
		      stop: function(event, ui){
		    	  
		    	  if ((typeof $("#my_min").val() !== "undefined") && (typeof $("#my_max").val() !== "undefined")) {
			    	  //ajaxCallForPrice($('#my_min').val(), $('#my_max').val())
			    	  alert(ACC.config.encodedContextPath +'/Animal-Management/c/Animal%20Management/'+$('#my_min').val()+'/'+$('#my_max').val());
		    	  }
		      }
			
		    });
		    
		    $('#my_min').val( "$" + sliderElement.slider( "values", 0 ));
		    $('#my_max').val( "$" + sliderElement.slider( "values", 1 ));
		});
		    
	}
}