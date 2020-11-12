<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<div id="b2bUnitOverlay" class="modal fade" role="dialog">
  <div class="modal-dialog">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title"><spring:theme code="Please.select.Business.Unit"/></h4>
      </div>
      <form:form id="selectB2BUnitForm" modelAttribute="b2bUnitsForm" action="submitSelectedUnit" method="post" enctype="multipart/form-data">
      <div class="modal-body">
        <ul class="listing">
        	<c:forEach items="${b2bunits}" var="unit" varStatus="status">
                <li><form:radiobutton path="selectedUnit" id="B2BTypeSelection_${unit.uid}" value="${unit.uid}" label="${unit.displayName}" /></li>
            </c:forEach>
        </ul>
      </div>
      <div class="modal-footer">
        <button id="chooseB2BType_continue_button" type="submit" class="btn btn-primary b2bunit-submit" data-dismiss="modal">
			<spring:theme code="Submit.selected.Business.Unit"/>
		</button>
      </div>
      </form:form>
    </div>
  </div>
</div>