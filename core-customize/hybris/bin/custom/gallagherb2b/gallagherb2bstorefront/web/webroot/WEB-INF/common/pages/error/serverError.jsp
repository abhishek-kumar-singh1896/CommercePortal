<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html>
<head>
<c:choose>
	<%-- if empty webroot, skip originalContextPath, simply use favIconPath --%>
	<c:when test="${fn:length(originalContextPath) eq 1}">
		<link rel="shortcut icon" type="image/x-icon" media="all"
			href="/_ui/responsive/theme-amB2C/images/favicon.ico" />
	</c:when>
	<c:otherwise>
		<link rel="shortcut icon" type="image/x-icon" media="all"
			href="${originalContextPath}/_ui/responsive/theme-amB2C/images/favicon.ico" />
	</c:otherwise>
</c:choose>
<title>Server Error</title>
</head>
<body>
	<p>Server Error</p>
</body>
</html>
