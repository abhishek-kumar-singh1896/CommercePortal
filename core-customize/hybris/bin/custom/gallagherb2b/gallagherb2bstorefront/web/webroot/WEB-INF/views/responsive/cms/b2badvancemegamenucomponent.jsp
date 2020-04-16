<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<c:if test="${component.visible}">
	<div class="container">
	<div class="second-level-nav">
		<ul>
		<c:forEach items="${menuItems}" var="menuItem">
		<c:set var="menuData" value="${menuMap[menuItem.categoryCode]}" />
			<li class="<c:if test="${not empty menuData.subCategories[menuItem.categoryCode]}">nav__links--primary-has__sub js-enquire-has-sub</c:if>">
				<cms:component component="${menuItem.link}" evaluateRestriction="true" />
				<div class="dropdown-menu-out d-none">
					<c:set var="totalSubNavigationColumns" value="${0}" /> 
					<c:set var="hasSubChild" value="false" />
					<c:if test="${not empty menuData.subCategories[menuItem.categoryCode]}">
					<ul>
					<c:forEach items="${menuData.subCategories[menuItem.categoryCode]}" var="allProductsChilds">
						<c:if test="${empty menuData.subCategories[allProductsChilds.code]}">
								<li class="verticalMenuOption groupOfLinks">
								<c:url value="${allProductsChilds.url}" var="childUrl" /> 
									<a href="${childUrl}" title="${allProductsChilds.name}">${allProductsChilds.name}</a>
								</li>
						</c:if>
					</c:forEach>
					</ul>
					</c:if>
				</div>

			</li>
		</c:forEach>
			
		</ul>
	</div>
</div>
</c:if>