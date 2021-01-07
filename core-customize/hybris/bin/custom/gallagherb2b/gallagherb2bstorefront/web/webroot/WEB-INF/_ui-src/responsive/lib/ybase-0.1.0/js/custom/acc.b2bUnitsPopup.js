ACC.b2bUnitsPopup = {

	_autoload : [ "showB2BUnits" ],

	showB2BUnits : function() {

	    $(document).ready(function(){   
	    	var showPopup = $("#showB2BUnitsPopup").val();
			if (showPopup == "true") {
		    	$.ajax({
					type : "GET",
					url : ACC.config.encodedContextPath + "/showb2bunits",
					success : function(data) {
						$('#b2bUnitsModal').append(data);
						$('#b2bUnitOverlay').modal('show');
					},
					error : function(data) {
						console.log('An error occurred.');
						console.log(data);
					},
				});
				}
			
				$('#datePurchased').datepicker({
	                dateFormat: 'dd/mm/yy',
	                autoclose: true
				});
	    	});
	    
	    $(document).on("click", ".b2bunit-submit", function(e) {
			var formData = $("#selectB2BUnitForm");
			e.preventDefault();

			$.ajax({
				type : "POST",
				url : ACC.config.encodedContextPath + "/showb2bunits/submit",
				data : formData.serialize(),
				success : function(data) {
					$('#b2bUnitOverlay').modal('hide');
					//location.reload();
					window.location.href=data;
				},
				error : function(data) {
					console.log('An error occurred.');
					console.log(data);
				},
			});

		});
	}
};