<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="searchUrl"
	value="/my-account/billing?sort=${ycommerce:encodeUrl(searchPageData.pagination.sort)}" />
<c:set var="messageKey" value="text.account.billingHistory.page" />


<div class="account-section-header">
	<spring:theme code="text.account.billingHistory" text="Billing History" />
</div>


<c:if test="${empty searchPageData.results}">
	<div
		class="account-section-content	col-md-6 col-md-push-3 content-empty">
		<ycommerce:testId code="orderHistory_noOrders_label">
			<spring:theme code="text.account.billingHistory.noOrders" />
		</ycommerce:testId>
	</div>
</c:if>
<c:if test="${not empty searchPageData.results}">
	<div class="account-orderhistory-pagination">
		<nav:pagination top="true" msgKey="${messageKey}"
			showCurrentPageInfo="true" hideRefineButton="true"
			supportShowPaged="${isShowPageAllowed}"
			supportShowAll="${isShowAllAllowed}"
			searchPageData="${searchPageData}" searchUrl="${searchUrl}"
			numberPagesShown="${numberPagesShown}" />
	</div>

	<div class="responsive-table">
		<table id="order_history" class="responsive-table-item">
			<thead>
				<tr class="responsive-table-head hidden-xs">
					<th id="header1"><spring:theme
							code="text.account.billingHistory.orderNumber"
							text="Order Number" /></th>
					<th id="header2">Billed For</th>
					<th id="header2">Billing Address</th>
					<th id="header3">Delivery Instructions</th>
					<th id="header4"><spring:theme
							code="text.account.billingHistory.datePlaced" text="Date Placed" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${searchPageData.results}" var="order">

					<c:url value="/my-account/order/${order.code}?page=billinghistory"
						var="myAccountOrderDetailsUrl" />

					<tr>
						<td class="hidden-sm hidden-md hidden-lg"><spring:theme
								code="text.account.billingHistory.orderNumber" /></td>
						<td headers="header1" class="responsive-table-cell"><ycommerce:testId
								code="orderHistory_orderNumber_link">
								<a href="${myAccountOrderDetailsUrl}">${order.code}</a>
							</ycommerce:testId></td>
						<td class="hidden-sm hidden-md hidden-lg"><spring:theme
								code="text.account.billingHistory.billedFor" /></td>
						<td headers="header2" class="responsive-table-cell"><ycommerce:testId
								code="orderHistory_orderStatus_label">
								<p>
									${fn:escapeXml(order.deliveryAddress.title)}&nbsp;${fn:escapeXml(order.deliveryAddress.firstName)}&nbsp;${fn:escapeXml(order.deliveryAddress.lastName)}
								</p>
							</ycommerce:testId></td>
						<td class="hidden-sm hidden-md hidden-lg"><spring:theme
								code="text.account.billingHistory.billingAddress" /></td>
						<td headers="header3" class="responsive-table-cell">
							<p>
								<c:if test="${ not empty order.deliveryAddress.line1}">${fn:escapeXml(order.deliveryAddress.line1)}<br>
								</c:if>
								<c:if test="${ not empty order.deliveryAddress.line2}">${fn:escapeXml(order.deliveryAddress.line2)}<br>
								</c:if>
								<c:if test="${ not empty order.deliveryAddress.town}">${fn:escapeXml(order.deliveryAddress.town)}<br>
								</c:if>
								<c:if test="${ not empty order.deliveryAddress.region.name}">${fn:escapeXml(order.deliveryAddress.region.name)}<br>
								</c:if>
								<c:if test="${ not empty order.deliveryAddress.postalCode}">${fn:escapeXml(order.deliveryAddress.postalCode)}<br>
								</c:if>
								<c:if test="${ not empty order.deliveryAddress.country.name}">${fn:escapeXml(order.deliveryAddress.country.name)}<br>
								</c:if>
							</p>

						</td>
						<td class="hidden-sm hidden-md hidden-lg"><spring:theme
								code="text.account.delivaryHistory.deliveryInstructions" /></td>
						<td headers="header4" class="responsive-table-cell">${fn:escapeXml(order.deliveryInstructions)}</td>
						<td class="hidden-sm hidden-md hidden-lg"><spring:theme
								code="text.account.billingHistory.datePlaced" /></td>
						<td headers="header5"
							class="responsive-table-cell responsive-table-cell-bold">
							<p>
								<fmt:formatDate value="${order.created}" dateStyle="long"
									timeStyle="short" type="both" />
							</p>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="account-orderhistory-pagination">
		<nav:pagination top="false" msgKey="${messageKey}"
			showCurrentPageInfo="true" hideRefineButton="true"
			supportShowPaged="${isShowPageAllowed}"
			supportShowAll="${isShowAllAllowed}"
			searchPageData="${searchPageData}" searchUrl="${searchUrl}"
			numberPagesShown="${numberPagesShown}" />
	</div>
</c:if>