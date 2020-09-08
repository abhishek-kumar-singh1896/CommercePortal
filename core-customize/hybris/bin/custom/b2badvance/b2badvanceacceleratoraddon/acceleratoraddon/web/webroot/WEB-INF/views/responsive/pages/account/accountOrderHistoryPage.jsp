<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="b2b-order" tagdir="/WEB-INF/tags/addons/b2badvanceacceleratoraddon/responsive/order" %>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/responsive/account"%>
<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="searchUrl" value="/my-account/orders?sort=${ycommerce:encodeUrl(searchPageData.pagination.sort)}"/>
<div class="row">
	<div class="col-md-2 col-md-offset-1 hidden-xs">
		<account:accountLeftNavigation />
	</div>
	<div class="col-md-8">
		<div class="account-section-header">
			<spring:theme code="text.account.orderHistory" />
		</div>
		<b2b-order:orderListing searchUrl="${searchUrl}" messageKey="text.account.orderHistory.page"></b2b-order:orderListing>
	</div>
</div>