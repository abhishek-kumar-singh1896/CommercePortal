<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<ul class="nav-menu">
    <cms:pageSlot position="B2BMyAccount" var="feature">
    	
		<li>
			<div class="menu-title">
				${feature.navigationNode.name}
			</div>
		</li>												
		<c:forEach items="${feature.navigationNode.children}"
			var="childLevel1">
			<c:forEach items="${childLevel1.entries}" var="entry">
				<cms:component component="${entry.item}" element="li" 
			 	evaluateRestriction="true" />
			</c:forEach>
		</c:forEach>
	</cms:pageSlot>
	<cms:pageSlot position="B2BMyCompany" var="feature">
		<li>
			<div class="menu-title">
				${feature.navigationNode.name}
			</div>
		</li>												
		<c:forEach items="${feature.navigationNode.children}"
			var="childLevel1">													
			<c:forEach items="${childLevel1.entries}" var="entry">
				<cms:component component="${entry.item}" element="li" 
			 		evaluateRestriction="true" />
			</c:forEach>
		</c:forEach>
	</cms:pageSlot>
	
	<li>
		<ycommerce:testId code="header_signOut">
			<cms:pageSlot position="ggB2BLogout" var="component">
				<cms:component component="${component}" />
                   				</cms:pageSlot>
		</ycommerce:testId>
	</li>
</ul>