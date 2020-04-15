<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.owasp.encoder.Encode"%>
<%@page import="com.paymetric.sdk.AccessTokenUtility"%>
<%@page import="com.paymetric.sdk.XIConfig"%>
<%@page import="java.io.StringReader"%>
<%@page import="java.io.StringWriter"%>
<%@page import="javax.xml.transform.OutputKeys"%>
<%@page import="javax.xml.transform.Source"%>
<%@page import="javax.xml.transform.Transformer"%>
<%@page import="javax.xml.transform.TransformerFactory"%>
<%@page import="javax.xml.transform.stream.StreamResult"%>
<%@page import="javax.xml.transform.stream.StreamSource"%>
<%@page import="java.util.Base64.Encoder" %>
<%@page import="java.util.Base64.Decoder" %>


<%
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Forward-Declare global variables
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
String strLocation = null;
String strResponseURL = null;
String status = null;
String accessToken = null;
String type = null;
String[] splitTokens = null;
String outXML = null;
String xmlRS = null;
XIConfig xiConfig = null;
Decoder decoder = null;
Encoder encoder = null;
	
try
{
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Load the XiIntercept configuration file
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	xiConfig = new XIConfig();
	xiConfig.Config(request, "XiIntercept.properties");


	if(!(strResponseURL = XIConfig.getParameter(request, "RS")).isEmpty())
	{
		strResponseURL = new String(decoder.decode(strResponseURL));
	}
	
	status = XIConfig.getParameter(request, "status");
    if(!status.toLowerCase().contains("cancel")) 
    {
        accessToken = XIConfig.getParameter(request, "id");
        
        type = XIConfig.getParameter(request, "type");
        splitTokens=accessToken.split(",");
        outXML="";
        for(String value:splitTokens)
        {
        	xmlRS = AccessTokenUtility.GetResponsePacket(XIConfig.CC_GUID.get(), XIConfig.CC_PSK.get(), XIConfig.URL.get(), value);
            Source xmlInput = new StreamSource(new StringReader(xmlRS));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
            transformer.transform(xmlInput, xmlOutput);
            outXML += xmlOutput.getWriter().toString();
        }
        
        if(!strResponseURL.isEmpty())
        {
        	response.sendRedirect(strResponseURL + "?Response=" + encoder.encode(outXML.getBytes()));
        }
        request.setAttribute("responsePacketHtml", Encode.forHtml(outXML));
        request.setAttribute("XiInterceptResponse", Encode.forHtml(xmlRS));
    }
    else
    {
        request.setAttribute("responsePacketHtml", "The user has cancelled using the payment page."); 
    }
}
catch (Exception ex)
{
    request.setAttribute("errormessage", ex.getMessage());
    request.setAttribute("errorstyle", "class='errorMsg'");
}
%>


<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="cache-control" content="max-age=0" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta charset="ISO-8859-1" />
	<title>XiIntercept v3 - HOSTED-IFRAME Response</title>
    <script type="text/javascript" >
    function sendXml()
    {
		var objXml = document.getElementById("DebugPacket");
		var strXml = null;
		
		if(window.parent.document != window.document)
        {             
             window.parent.XiInterceptResponse(objXml.textContent, window.frameElement.id);
             return;
        }
		else if(window.opener != window.document)
        {
           window.opener.XiInterceptResponse(objXml.textContent,'"xiFrameHosted"');
           window.close();
           return;
        } 
	        
		objXml.style.display = 'inline';
    }
    </script>
</head>
<body onload="sendXml();">
	<div class="sampleBody">
	    <pre id="DebugPacket" style="display: none"><code class="xml hljs" id="ResponseSpan">${responsePacketHtml}</code></pre>
        <div id="Status" ${errorstyle}>${errormessage}</div>
	</div>
</body>
</html>