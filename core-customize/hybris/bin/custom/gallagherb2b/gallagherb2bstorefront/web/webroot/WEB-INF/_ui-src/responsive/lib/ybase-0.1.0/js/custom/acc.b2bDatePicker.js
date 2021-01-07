ACC.b2bUnitsPopup = {

	_autoload : [ "bindDateField" ],

	bindDateField : function() {

	    $(document).ready(function(){   
	    	// Date Picker
            $('#datePurchased').datepicker({
            	
                dateFormat: 'dd/mm/yy',
                autoclose: true
            });
	    	});
	    
	    
	}
};