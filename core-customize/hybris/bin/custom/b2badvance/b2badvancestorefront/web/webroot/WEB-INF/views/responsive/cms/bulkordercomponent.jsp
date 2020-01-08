
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<head>

<script type="text/javascript" src="${commonResourcePath}/js/jquery-3.2.1.min.js"></script>


<script type="text/javascript">
	$(function() {
		$('#dropZone')
				.filedrop(
						{
							url : $("#qoUrl").val() + '/upload',
							paramname : 'files',
							data: {'CSRFToken' : ACC.config.CSRFToken},
							withCredentials: true,
							maxFiles : 5,
							allowedfileextensions : [ '.xlsx' ],
							dragOver : function() {
								$('#dropZone').css('background', 'blue');
							},
							dragLeave : function() {
								$('#dropZone').css('background', 'gray');
							},
							drop : function() {
								$('#dropZone').css('background', 'gray');
							},
							afterAll : function(data, status) {
								//populateSucessData(data);
								$('#dropZone')
										.html(
												'The file(s) have been uploaded successfully!');
							},

							error : function() {
								var msg = document
										.getElementById("errWrongExt");
								qoShowErrors(msg.value);
							},

							uploadFinished : function(i, file, response, time) {
								populateSucessData(response);
							}
						});
	});
</script>

</head>
<c:choose>
	<c:when test='${component.uid eq "HomePageBulkOrderComponent"}'>
		<div class="hidden-xs hidden-sm">
			<nav:desktopBulkOrderForm />
		</div>
	</c:when>
	<c:when test='${component.uid eq "BulkOrderFormPageComponent"}'>

		<div class="hidden-lg hidden-md">
			<nav:mobileBulkOrderForm />
		</div>
	</c:when>
</c:choose>


<script type="text/javascript">
	$(document).ready(function() {
		configureAutocomplete();
	})
</script>