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
	<div class="content">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1>Something went wrong! </h1>
                    <p class="lead">
                        </p><h2>Please try refreshing the page or try returning to the homepage.</h2><br>
                        <a href="/" class="btn orange">Return to our homepage</a>
                    <p></p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>



