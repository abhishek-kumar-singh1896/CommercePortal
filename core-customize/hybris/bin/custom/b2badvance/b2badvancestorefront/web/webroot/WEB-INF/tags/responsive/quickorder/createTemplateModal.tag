<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="titleKey" required="true" type="java.lang.String"%>
<%@ attribute name="messageKey" required="false" type="java.lang.String"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<c:set var="loc_val">
    <spring:message code="create.template.max.chars" />
</c:set>
<input type="hidden" id="localized_val" name="localized_val" value="${loc_val}"/>
<div class="hidden">
	<div id="createTemplate" data-create-template-title="<spring:theme code="${titleKey}"/>">
		<form:form action="#" id="createTemplateForm" modelAttribute="createTemplateForm" autocomplete="off">
			<div class="form-group">
				<c:if test="${not empty messageKey}">
					<div class="legend"><spring:theme code="${messageKey}"/></div>
				</c:if>
				
				<label class="control-label" for="templateName">
					<spring:theme code="create.template.name" />
				</label>
                <form:input cssClass="form-control" id="templateName" path="templateName" maxlength="255" />
                 <div class="help-block right-cartName" id="remain">
                </div>
            </div>
            <div class="form-group">
				<label class="control-label" for="templateDesc">
					<spring:theme code="create.template.description" />
				</label>
                <form:textarea cssClass="form-control" id="templateDesc" path="templateDesc" maxlength="255" />             
                <div class="help-block" id="remainTextArea">
                </div>
			</div>
			<div class="form-actions">
	            <div class="modal-actions">
                    <div class="row">
                        <div class="col-sm-12">
                            <button type="button" class="btn btn-primary btn-block " id="createTemplateButton" disabled="disabled">
                                <spring:theme code="create.template.action.create"/>
                            </button>

                            <button type="button" class="btn btn-default btn-block" id="cancelCreateTemplateButton">
                                <spring:theme code="create.template.action.cancel"/>
                            </button>
                        </div>
	                </div>
	            </div>
	        </div>
		</form:form>
	</div>
</div>

