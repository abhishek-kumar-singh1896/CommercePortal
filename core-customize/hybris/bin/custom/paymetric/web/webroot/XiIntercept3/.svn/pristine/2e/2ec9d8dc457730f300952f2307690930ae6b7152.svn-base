<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.paymetric.sdk.AccessTokenUtility"%>
<%@page import="com.paymetric.sdk.models.AccessTokenResponse.AccessTokenResponsePacket"%>
<%@page import="com.paymetric.sdk.models.MerchantHtmlPacket.IFrameMerchantHtmlPacket"%>
<%@page import="com.paymetric.sdk.XIConfig"%>
<%@page import="java.math.BigDecimal"%>


<%
try
{
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Forward-Declare global variables
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	String strLocation = null;
	String strStore = null;
	XIConfig xiConfig = null;
	IFrameMerchantHtmlPacket packetRQ = null;
	AccessTokenResponsePacket packetRS = null;
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Load the XiIntercept configuration file
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	xiConfig = new XIConfig();
	xiConfig.Config(request, "XiIntercept.properties");

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Initialize the IFRAME Template or Custom XML to provide to XiIntercept
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	String strSite = xiConfig.getSiteURL(request);
	packetRQ = xiConfig.getIFramePacket(request);

	if(XIConfig.is3DS.get())
	{
		String strRS = xiConfig.getResponse(request);
		String strOrderNo = xiConfig.getOrderNo(request);
		BigDecimal dAmount = xiConfig.getAmount(request);
		Integer iCurrency = xiConfig.getCurrency(request);
	
		if(strRS.length() == 0)
			packetRQ.SetCardinal3DSProperties(dAmount, iCurrency, strSite + "XiInterceptIFrameRS.jsp", strOrderNo);
		else packetRQ.SetCardinal3DSProperties(dAmount, iCurrency, strSite + "XiInterceptIFrameRS.jsp?RS=" + strRS, strOrderNo);
	}

	packetRS = AccessTokenUtility.GetIFrameAccessToken(XIConfig.CC_GUID.get(), XIConfig.CC_PSK.get(), XIConfig.URL.get(), packetRQ);
	if (!(packetRS.getRequestError() + "").equals("null")) 
	{
		String strError = packetRS.getRequestError().getStatusCode() + "," + packetRS.getRequestError().getMessage();
	    request.setAttribute("errormessage", strError);
	    request.setAttribute("errorstyle", "class='errorMsg'");
	} 
	else
	{
	    String accessToken = packetRS.getResponsePacket().getAccessToken();
	    request.setAttribute("XIeCommUrl", XIConfig.URL.get());
	    request.setAttribute("AutoSizeHeight", XIConfig.AutoSizeHeight.get());
	    request.setAttribute("AutoSizeWidth", XIConfig.AutoSizeWidth.get());
	    request.setAttribute("accessToken", accessToken);
	    request.setAttribute("signature", packetRS.getResponsePacket().getSignature());
	    request.setAttribute("iFrameUrl", XIConfig.URL.get() + "/view/iframe/" + XIConfig.CC_GUID.get() + "/" + accessToken + "/true");
	    request.setAttribute("site", strSite);
	    request.setAttribute("secureresponse", strSite + "XiInterceptIFrameRS.jsp");
	}
}
catch(Exception ex)
{
    request.setAttribute("errormessage", ex.getMessage());
    request.setAttribute("errorstyle", "class='errorMsg'");
}

%>

<!DOCTYPE>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="cache-control" content="max-age=0" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta charset="ISO-8859-1" />
		<title>XiIntercept v3 - HOSTED-IFRAME Request</title>
	    <script src="${XIeCommUrl}/scripts/XIFrame/XIFrame-1.1.0.js" type="text/javascript"></script>
	    <script type="text/javascript">
	    	var strError = "${errormessage}";
	    	
	    	if(strError.length != 0)
	    	{
            	if(typeof window.parent.DisplayMessage === "function")
            	{
                	window.parent.DisplayMessage("XiInterceptIFrameRQ","ERROR", strError);
            	}
	    	}
	    	
	        function submitform()
	        {
	            var iframe = document.getElementsByName('xieCommFrame');
	            
	            if (iframe) 
	            {
	                $XIFrame.submit({
	                    iFrameId: 'xieCommFrame',
	                    targetUrl: iframe[0].getAttribute("src"),
	                    onSuccess: function (msg)
	                    {
	                    	var message = JSON.parse(msg);
	                        if (message && message.data.HasPassed)
	                        {
	                            var accessToken = document.getElementById('AccessToken');
	                            var signedToken = document.getElementById('SignedToken');
	                            window.location = "${secureresponse}?id=" + accessToken.value + "&s=" + signedToken.value;
	                        }
	                        else
	                        {
	                        	if(typeof window.parent.DisplayMessage === "function")
	                        	{
		                        	window.parent.DisplayMessage("XiInterceptIFrameRQ","ERROR",message.data.Message);
	                        	}
	                        }
	                    },
	                    onError: function (msg)
	                    {
                        	if(typeof window.parent.DisplayMessage === "function")
                        	{
    	                    	window.parent.DisplayMessage("XiInterceptIFrameRQ","ERROR",msg);
                        	}
	                    }
	                });
	            }
	        }

	        function IFrame_OnLoad()
	        {
                var xiTokenize = document.getElementById('xiTokenize');
                var xiStatus = document.getElementById('Status');
                if (xiTokenize)
                {
                	if(typeof window.parent.InitForTokenization == 'undefined')
                	{
                		xiTokenize.style.display = 'block';
                		xiStatus.style.display = 'block';
                	}
                	else
                	{
                		xiTokenize.style.display = 'none';
                		xiStatus.style.display = 'none';
                	}
		        }
                
                var iframe = document.getElementsByName('xieCommFrame');
                if (iframe)
                {
                    $XIFrame.onload({
                        iFrameId: 'xieCommFrame',
                        targetUrl: iframe[0].getAttribute("src"),
                        autosizeheight: ${AutoSizeHeight},
                        autosizewidth: ${AutoSizeWidth},
                        onSuccess: function (msg) 
                        {
                        	if(typeof window.parent.DisplayMessage === "undefined")
                        	{
                        		xiStatus.innerText = "A form for the merchant guid and access token combination is loading in the iFrame successfully.";
                        	}
                        },
                        onValidate: function (e)
                        {
                        	if(typeof window.parent.DisplayMessage === "function")
                        	{
    	                    	window.parent.DisplayMessage("XiInterceptIFrameRQ.onValidate()","INFO",e);
                        	}
                        	else
                        	{
                        		xiStatus.innerText = "XiInterceptIFrameRQ.onValidate()" + e;
                        	}
                        },
                        onInvalidHandler: function (e)
                        {
                        	if(typeof window.parent.DisplayMessage === "function")
                        	{
    	                    	window.parent.DisplayMessage("XiInterceptIFrameRQ.onInvalidHandler()","ERROR",e);
                        	}
                        	else
                        	{
                        		xiStatus.innerText = "XiInterceptIFrameRQ.onInvalidHandler()" + e;
                        	}
                        },
						onError: function (msg)
                        {
                        	if(typeof window.parent.DisplayMessage === "function")
                        	{
    	                    	window.parent.DisplayMessage("XiInterceptIFrameRQ","ERROR",msg);
                        	}
                        	else
                        	{
                        		xiStatus.innerText = "XiInterceptIFrameRQ.onError()" + msg;
                        	}
                        }
                    });
                }
	        }
	    </script>
	</head>
	<body>
	    <div class="sampleBody">
	        <div class="payment-content">
                <div class="billing-info">
                    <div id="iframewrapper">
                        <iframe id="xieCommFrame" name="xieCommFrame" style="border:none;" src="${iFrameUrl}" onload="IFrame_OnLoad();return false;"></iframe>
                    </div>
					<input id="xiTokenize" type="button" value="Tokenize..." onclick="submitform(); return false;" style="display: none" />
	            	<input type="hidden" id="AccessToken" value="${accessToken}" />
	            	<input type="hidden" id="SignedToken" value="${signature}" />
	        	</div>
	        </div>
	        <div id="Status" ${errorstyle}>${errormessage}</div>
	    </div>
	</body>
</html>