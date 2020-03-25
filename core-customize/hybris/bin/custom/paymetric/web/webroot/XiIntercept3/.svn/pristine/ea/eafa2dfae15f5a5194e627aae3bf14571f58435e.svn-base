

function render3dsPopup(e){
	
	
	 if (!e) {
         return;
     }
     
     if (!e.HasPassed) {
         return;
     }

     if ($("#post_frame").length > 0) {
         return;
     } 
     
     var myiframe = document.createElement("iframe");
     myiframe.setAttribute("id","post_frame");
     myiframe.setAttribute("style","width:450px;height:480px;");
     myiframe.setAttribute("scrolling","yes");
     myiframe.setAttribute("frameborder","0");
     
     document.body.appendChild(myiframe);
     
     var framebody = $('#post_frame').contents().find('body');
     
     var context = e.UrlsAndPayloadsFor3Ds;
     
     var myform = document.createElement("form");
     myform.setAttribute("method", "post");
     myform.setAttribute("action", context.StepUpUrl);
     myform.setAttribute("target", "view");
     
     var hiddenField1 = document.createElement("input"); 
     hiddenField1.setAttribute("type", "hidden");
     hiddenField1.setAttribute("name", "JWT");
     hiddenField1.setAttribute("value", context.JWT);
     myform.appendChild(hiddenField1);
     
     var hiddenField2 = document.createElement("input"); 
     hiddenField2.setAttribute("type", "hidden");
     hiddenField2.setAttribute("name", "MD");
     hiddenField2.setAttribute("value", context.PaymetricPayload);
     myform.appendChild(hiddenField2);
     
     framebody.append(myform);
     
     var left = (screen.width/2)-(450/2);
     var top = (screen.height/2)-(480/2);

     window.open('', 'view',"width=450,height=480,top="+top+",left="+left+",toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,copyhistory=no");

     myform.submit();
	
	
}

function create3ds2Elements(CardinalResp){
	
	 var cavv = document.createElement("input");
	 cavv.setAttribute("type", "hidden");
	 cavv.setAttribute("name", "card_cavv");
	 cavv.setAttribute("value",CardinalResp.Cavv);
	 document.getElementById("silentOrderPostForm").appendChild(cavv);
	   
	 	
	 var DSTranID = document.createElement("input");
	 DSTranID.setAttribute("type", "hidden");
	 DSTranID.setAttribute("name", "card_dstranid");
	 
	 if(CardinalResp.DSTransactionId != null){
		 DSTranID.setAttribute("value",CardinalResp.DSTransactionId);
	 }
	 else{
		 DSTranID.setAttribute("value",CardinalResp.Xid);
	 }	 
	 document.getElementById("silentOrderPostForm").appendChild(DSTranID);
	
	 var parEsStatus = document.createElement("input");
	 parEsStatus.setAttribute("type", "hidden");
	 parEsStatus.setAttribute("name", "card_parEsStatus");
	 parEsStatus.setAttribute("value",CardinalResp.PAResStatus);
	 document.getElementById("silentOrderPostForm").appendChild(parEsStatus);
	 
	 var eciFlag = document.createElement("input");
	 eciFlag.setAttribute("type", "hidden");
	 eciFlag.setAttribute("name", "card_eciFlag");
	 eciFlag.setAttribute("value",CardinalResp.EciFlag);
	 document.getElementById("silentOrderPostForm").appendChild(eciFlag);
	 
	 
}