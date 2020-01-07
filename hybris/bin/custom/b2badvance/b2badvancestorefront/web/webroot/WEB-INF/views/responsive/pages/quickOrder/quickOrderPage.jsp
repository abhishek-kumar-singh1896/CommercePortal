<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="quickorder" tagdir="/WEB-INF/tags/responsive/quickorder" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<spring:htmlEscape defaultHtmlEscape="true" />
<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<c:if test="${not empty accConfMsgs}">
			<c:forEach items="${accConfMsgs}" var="msg">
				<div class="alert alert-info alert-dismissable getAccAlert">
					<button class="close closeAccAlert" aria-hidden="true" data-dismiss="alert" type="button">&times;</button>
					<spring:theme code="${msg.code}" arguments="${msg.attributes}" htmlEscape="false" var="informationMessages"/>
					${ycommerce:sanitizeHTML(informationMessages)}
				</div>
			</c:forEach>
		</c:if>
	</div>
	<input type="hidden" value="${locale}" class="locale">
	<div id="quickOrder" class="account-section quick-order-container" data-grid-confirm-message="${gridConfirmMessage}">
        <div class="account-section-content ">
            <div class="quick-order-section-header account-section-header">
            	<div class="row">
            	<div class="col-lg-8 col-md-8 col-xs-6">
                <spring:theme code="text.quickOrder.header" />
       		      </div>
       		      <div class="col-lg-4 quick-order-links">
                 	<cms:pageSlot position="QuickOrderLinks" var="link">
						<cms:component component="${link}" element="div" />
				 	</cms:pageSlot>
				 </div>
				 </div>
            </div>
            <quickorder:quickorderLineItemTable />
            <quickorder:quickOrderSearchBox />
            <quickorder:quickorderErrorMessages />
            <div class="final-product-price">
					<div class="container">
						<div class="row">
							<div class="col-12">
								<div class="total-text">
									<spring:theme code="text.quickOrder.page.total" />
								</div>
								<div class="final-price"></div>
								<div class="gst">								
										<spring:theme code="text.quickOrder.page.gst.info" />									
								</div>
								
								<c:url var="cartPageUrl" value="/cart" />
								<c:url var="templatePageUrl" value="/my-account/quick-order-templates" />
								<input type="hidden" id="cartPageUrl" value="${cartPageUrl}">
								<input type="hidden" id="templatePageUrl" value="${templatePageUrl}">
								<input type="hidden" id="maxLimit" value="${quickOrderMaxRows}">
								<button id="js-add-to-cart-quick-order-btn-bottom" class="btn btn-primary">
									<spring:theme code="text.quickOrder.page.add.to.cart" />
								</button>
								
								<button id="js-create-template-quick-order-btn-bottom" class="btn btn-primary js-create-template-link">
									<spring:theme code="text.quickOrder.page.add.to.template" />
								</button>
							</div>
						</div>
					</div>
				</div>
	</div>
</div>
<quickorder:createTemplateModal titleKey="text.create.template.title" messageKey="text.create.template.info.msg"/>
</template:page>