<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<spring:url var="restoreTemplatePostUrl"
       value="/my-account/quick-order-templates/{/templateId}/restore" htmlEscape="false">
    <spring:param name="templateId" value="${commerceSaveCartResultData.savedCartData.code}"/>
</spring:url>

<div id="popup_confirm_savedcart_restore" class="savedcart_restore_confirm_modal js-savedcart_restore_confirm_modal">
	<form:form action="${restoreTemplatePostUrl}" id="restoreTemplateForm" modelAttribute="restoreTemplateForm" autocomplete="off">
    	<p><spring:theme code="text.account.quickOrderTemplates.to.activeQuickOrder"/></p>

    <div class="modal-details row">
        <span class="col-xs-6"><spring:theme code="text.account.ordertemplate.template.name"/>:</span>
        <span class="col-xs-6"><b>${fn:escapeXml(commerceSaveCartResultData.savedCartData.name)}</b></span>
        <span class="col-xs-6"><spring:theme code="text.account.ordertemplate.template.id"/>:</span>
        <span class="col-xs-6">${fn:escapeXml(commerceSaveCartResultData.savedCartData.code)}</span>
        <span class="col-xs-6"><spring:theme code="text.account.ordertemplate.numberofproducts"/>:</span>
        <span class="col-xs-6"><b>${commerceSaveCartResultData.savedCartData.totalUnitCount}</b></span>
    </div>
    <br/>
    <input type="hidden" value="${templateId}" class="templateId">
    <div class="modal-actions">
        <div class="row">
            <div class="col-xs-12 col-sm-6 col-sm-push-6">
                <button type="submit" class="js-template-restore-btn btn btn-primary btn-block"
                        data-restore-url="${restoreTemplatePostUrl}">
                    <spring:theme code="text.account.savedcart.restore.btn"/>
                </button>
            </div>
            <div class="col-xs-12 col-sm-6 col-sm-pull-6">
                <button type="button" class="js-cancel-restore-btn btn btn-default btn-block">
                    <spring:theme code="text.button.cancel"/>
                </button>
            </div>
        </div>
    </div>
    </form:form>
</div>