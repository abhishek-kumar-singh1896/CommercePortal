ACC.preference = {

	_autoload : [ "clickOnNav" ],

	clickOnNav : function() {

		$(document).ready(function() {

			var showPopup = $("#showPreferences").val();
			if (showPopup == "true") {

				ACC.colorbox.open("Communication Preferences", {

					href : ACC.config.encodedContextPath + "/preferences",
					maxWidth : "100%",
					width : "450px",
					initialWidth : "400px",
					overlayClose : false,
					onLoad : function() {
						$('#cboxClose').remove();
					}
				});
			}
		});

		$(document).on("click", ".preference-submit", function(e) {
			var formData = $("#customerPreferences");
			e.preventDefault();

			$.ajax({
				type : "POST",
				url : ACC.config.encodedContextPath + "/submitPreferences",
				data : formData.serialize(),
				success : function(data) {
					ACC.colorbox.close();
				},
				error : function(data) {
					console.log('An error occurred.');
					console.log(data);
				},
			});

		});
	}
};
