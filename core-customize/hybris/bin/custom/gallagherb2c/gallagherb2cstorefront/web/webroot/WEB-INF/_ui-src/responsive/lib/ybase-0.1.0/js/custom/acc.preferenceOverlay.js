ACC.header = {

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
					overlayClose:false,
					onLoad: function() {
					    $('#cboxClose').remove();
					}
				});
			}
		});
	}
};
