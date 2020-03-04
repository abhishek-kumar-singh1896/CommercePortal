ACC.productList = {
	$(document).ready(function ()
	{  		
	    $("#_asmPersonifyForm input[name='customerName'], input[name='customerId']").hover(function() {
				$("#asmHover").remove();
				var item = ( $(this).attr('data-hover') )? jQuery.parseJSON($(this).attr('data-hover')) : $(this).data( "hover" );
				var disabled = ( $(this).attr('data-hover') )? "disabled" : "";

				if( !(item === null || item === undefined) ) {
					$(this)
	            		.after(
	            			$('<div style="margin-left:240px">')
	                    		.attr('id', 'asmHover')
	                    		.addClass(disabled)
	                    		.append(
	                    			$('<span>').addClass('name').text(item.name),
	                    			$('<span>').addClass('email').text(item.email),
	                    			$('<span>').addClass('date').text(item.date),
	                    			$('<span>').addClass('card').text(item.card)
	                    	)
	            		);
				}
			}, function () {
				removeAsmHover();
			}
	    );

	});	
		
}