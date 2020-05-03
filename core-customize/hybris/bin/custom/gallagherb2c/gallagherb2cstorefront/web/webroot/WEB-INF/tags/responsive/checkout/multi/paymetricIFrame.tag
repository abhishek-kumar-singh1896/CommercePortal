<!-- BEGIN: Payment Images -->
<br />
<div>
	<!-- BEGIN: Credit Cards Image -->
	<input type="image" id="creditCardImage"
		style="padding-left: 5px; padding-bottom: 20px; height: 45px; width: 150px; display: none; outline: none;"
		src="/paymetric/XiIntercept3/credit_card_logos_32.gif" />
	<!-- END: Credit Cards Image -->
</div>
<!-- END: Payment Images -->
<!-- BEGIN: XiIntercept Credit Card Integration -->
<link rel="stylesheet" href="/paymetric/XiIntercept3/XIeCommerce3.css"
	type="text/css" />
<script type="text/javascript"
	src="/paymetric/XiIntercept3/XIeCommerce3.js"></script>
<script type="text/javascript" src="/paymetric/XiIntercept3/xml2json.js"></script>
<iframe id="xiFrameHosted" class="hosted-iframe"
	src="/paymetric/XiIntercept3/XiInterceptIFrameRQ.jsp?store=${cartData.store}"
	onload="setTimeout(function(){resizeIFrameToFitContent('xiFrameHosted');}, 50);"
	style="border: none; width: 100%; height: 100%;"> </iframe>
<script type="text/javascript">
	function resizeIFrameToFitContent(id) {
		var iFrame = document.getElementById(id);
		iFrame.style.height = (iFrame.contentWindow.document.body.scrollHeight + 5)
				+ 'px';
		iFrame.style.width = (iFrame.contentWindow.document.body.scrollWidth + 5)
				+ 'px';
	}
	function handleXiInterceptResponse(id) {
		var iField = 0;
		var fields = null;
		var field = null;
		var iNo = "";
		// If multiple IFrames, look for the iframe number
		if (id.lastIndexOf("-") != -1) {
			iNo = id.substring(id.lastIndexOf("-"));
		}
		// Get The PaymetricResponse Fields
		fields = gv_jsonObj.PaymetricResponse.Fields.FormField;
		for (iField = 0; iField < fields.length; iField++) {
			field = fields[iField];
			// Get Credit Card Data Response Fields
			if (field.Name == "Card Number")
				document.getElementById("card_accountNumber" + iNo).value = field.Value;
			else if (field.Name == "Card Holder Name")
				document.getElementById("card_nameOnCard" + iNo).value = field.Value;
			else if (field.Name == "Card Type") {
				var strValue = field.Value.toLowerCase();
				if (strValue == "vi")
					strValue = "001";
				else if (strValue == "mc")
					strValue = "002";
				else if (strValue == "ax")
					strValue = "003";
				else if (strValue == "di")
					strValue = "004";
				else if (strValue == "dc")
					strValue = "005";
				else if (strValue == "jc")
					strValue = "007";
				else if (strValue == "sw")
					strValue = "024";
				document.getElementById("card_cardType" + iNo).value = strValue;
			} else if (field.Name == "Card Security Code")
				document.getElementById("card_cvNumber" + iNo).value = field.Value;
			else if (field.Name == "Expiration Month")
				document.getElementById("ExpiryMonth" + iNo).value = field.Value;
			else if (field.Name == "Expiration Year")
				document.getElementById("ExpiryYear" + iNo).value = pad(
						field.Value, 4);
		}
		// Submit the SOP form
		document.getElementById("cmdSubmit").click();
	}
</script>
<!-- END: XiIntercept Credit Card Integration -->