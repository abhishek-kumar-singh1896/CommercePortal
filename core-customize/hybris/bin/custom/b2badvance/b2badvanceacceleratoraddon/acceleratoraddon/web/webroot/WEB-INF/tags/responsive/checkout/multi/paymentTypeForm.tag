<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form:form id="selectPaymentTypeForm" modelAttribute="paymentTypeForm" action="${request.contextPath}/checkout/multi/payment-type/choose" method="post">
    <div class="step-body-form">
        <div class="radiobuttons_paymentselection">
        <c:forEach items="${paymentTypes}" var="paymentType">
              <c:if test="${paymentType.displayName ne 'Card Payment'}">
                <form:radiobutton path="paymentType" id="PaymentTypeSelection_${paymentType.code}" value="${paymentType.code}" label="${paymentType.displayName}" 
                checked="${paymentType.displayName == 'Account' ? 'checked' : '' }" />
                <br>
             </c:if>
        </c:forEach>
     </div>
     
        <formElement:formInputBox idKey="PurchaseOrderNumber" labelKey="checkout.multi.purchaseOrderNumber.label" path="purchaseOrderNumber" inputCSS="text" />

        <div id="costCenter">
            <formElement:formSelectBox idKey="costCenterSelect" labelKey="checkout.multi.costCenter.label" path="b2bUnit" skipBlank="false" 
            skipBlankMessageKey="checkout.multi.costCenter.title.pleaseSelect" itemValue="code" itemLabel="name" items="${b2bUnits}" mandatory="true" selectCSSClass="form-control"/>
        </div>
        
       <div>
       <label class="control-label " for="DeliveryDate"><spring:theme code="checkout.multi.requiredDate.label"/></label>
      <form:input idKey="DeliveryDate" path="requiredDeliveryDate" id="datePurchased" class="form-control" />
     </div>
            
     <br> 
      
      <label class="control-label " for="DeliveryDate"><spring:theme code="checkout.multi.comments.label"/></label>             	
      <div>
       <form:textarea  rows="14"  idKey="Comments" path="deliveryInstructions" name = "deliveryInstructions"  label = "Comments" class="form-control" /> 
     </div>
        
        
        
    </div>

	<button id="choosePaymentType_continue_button" type="submit" class="btn btn-primary btn-block checkout-next">
		<spring:theme code="checkout.multi.paymentType.continue"/>
	</button>
		
</form:form>
