<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- <i class="${component.styleClass}"></i> --%>

<c:if test="${component.styleClass eq 'icon--facebook'}">
<img class="svg-icon" src="${siteRootUrl}/theme-securityB2B/images/svg/facebook.svg">
</c:if>
<c:if test="${component.styleClass eq 'icon--linkedin'}">
<img class="svg-icon" src="${siteRootUrl}/theme-securityB2B/images/svg/linkedin.svg">
</c:if>
<c:if test="${component.styleClass eq 'icon--youtube'}">
<img class="svg-icon" src="${siteRootUrl}/theme-securityB2B/images/svg/youtube.svg">
</c:if>
<c:if test="${component.styleClass eq 'icon--twitter'}">
<img class="svg-icon" src="${siteRootUrl}/theme-securityB2B/images/svg/twitter.svg">
</c:if>