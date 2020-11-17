<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

<div class="row">
    <div class="col-sm-6">
        <div class="checkout-headline">
            <span class="glyphicon glyphicon-lock"></span>
            <spring:theme code="checkout.multi.secure.checkout" />
        </div>
		<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
			<jsp:body>
				<ycommerce:testId code="checkoutStepTwo">
					<div class="checkout-shipping">
						<multi-checkout:shipmentItems cartData="${cartData}" showDeliveryAddress="true" />
						<div class="checkout-indent">
							<%-- <div class="headline"><spring:theme code="checkout.summary.deliveryMode.selectDeliveryMethodForOrder" text="Shipping Method"></spring:theme></div> --%>
							<form:form id="selectDeliveryMethodForm" action="${request.contextPath}/checkout/multi/delivery-method/select" modelAttribute="deliveryMethod">
								<%-- <div class="form-group">
									<multi-checkout:deliveryMethodSelector deliveryMethods="${deliveryMethods}" selectedDeliveryMethodId="${cartData.deliveryMode.code}"/>
								</div> --%>
								<p><spring:theme code="checkout.multi.deliveryMethod.message" text="Items will ship as soon as they are available. <br> See Order Summary for more information. <br><b> *No taxes or freight costs are included in the total"></b></spring:theme></p>
								 <%-- <div class="deliveryInstructions">
                                  	<label>Delivery Instructions:</label>
                                 	 <textarea name="deliveryInstructions" id="deliveryInstructions">${cartData.deliveryInstructions}
                                                       </textarea>
                          		</div> --%>
								<button id="deliveryMethodSubmit" type="submit" class="btn btn-primary btn-block checkout-next"><spring:theme code="checkout.multi.deliveryMethod.continue" text="Next"/></button>
							</form:form>
						</div>
					</div>
				</ycommerce:testId>
			</jsp:body>
		</multi-checkout:checkoutSteps>
    </div>

    <div class="col-sm-6 hidden-xs">
		<multi-checkout:checkoutOrderDetails cartData="${cartData}" showDeliveryAddress="true" showPaymentInfo="false" showTaxEstimate="false" showTax="true" />
    </div>

    <div class="col-sm-12 col-lg-12">
        <cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help">
            <cms:component component="${feature}"/>
        </cms:pageSlot>
    </div>
</div>

</template:page>