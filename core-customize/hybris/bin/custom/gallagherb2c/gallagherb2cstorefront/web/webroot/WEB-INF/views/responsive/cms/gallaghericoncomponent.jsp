<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- <i class="${component.styleClass}"></i> --%>

<c:if test="${component.styleClass eq 'icon--facebook'}">
<img src="${commonResourcePath}/images/facebook.png" alt="Facebook">
</c:if>
<c:if test="${component.styleClass eq 'icon--linkedin'}">
<img src="${commonResourcePath}/images/linkedin.png" alt="Linkedin">
</c:if>
<c:if test="${component.styleClass eq 'icon--youtube'}">
<img src="${commonResourcePath}/images/youtube.png" alt="Youtube">
</c:if>
<c:if test="${component.styleClass eq 'icon--twitter'}">
<img src="${commonResourcePath}/images/twitter.png" alt="Twitter">
</c:if>
<c:if test="${component.styleClass eq 'icon--instagram'}">
<img src="${commonResourcePath}/images/instagram.png" alt="Instagram">
</c:if>