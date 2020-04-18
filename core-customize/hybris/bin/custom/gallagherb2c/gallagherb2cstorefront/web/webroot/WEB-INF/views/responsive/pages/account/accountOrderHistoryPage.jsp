<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="searchUrl" value="/my-account/orders?sort=${ycommerce:encodeUrl(searchPageData.pagination.sort)}"/>

<div class="container">
	<h1 class="primary-title">
		<spring:theme code="text.account.orderHistory" />
	</h1>

	<c:if test="${empty searchPageData.results}">
		<div class="account-section-content content-empty">
			<ycommerce:testId code="orderHistory_noOrders_label">
				<div class="col-6 col-md-8 col-lg-9">
					<div class="result-found-text">
						<spring:theme code="text.account.orderHistory.noOrders" />
					</div>
				</div>
			</ycommerce:testId>
		</div>
	</c:if>
	
	<c:if test="${not empty searchPageData.results}">
		<div class="account-section-content	">
			<div class="account-orderhistory">
				<div class="account-orderhistory-pagination">
					<nav:paginationAccountOrderHistory top="true" msgKey="text.account.orderHistory.page" showCurrentPageInfo="true" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
				</div>
				<div class="account-overview-table">
					<table class="orderhistory-list-table responsive-table">
						<tbody>
							<tr class="account-orderhistory-table-head responsive-table-head d-none d-md-table-row">
								<th><spring:theme code="text.account.orderHistory.orderNumber" /></th>
								<th><spring:theme code="text.account.orderHistory.orderStatus"/></th>
								<th><spring:theme code="text.account.orderHistory.datePlaced"/></th>
								<th><spring:theme code="text.account.orderHistory.total"/></th>
							</tr>
							<c:forEach items="${searchPageData.results}" var="order">
								<tr class="responsive-table-item">
									<ycommerce:testId code="orderHistoryItem_orderDetails_link">
										<td class="table-cell d-md-none"><spring:theme code="text.account.orderHistory.orderNumber" /></td>
										<td class="responsive-table-cell">
											<spring:url value="/my-account/order/{/orderCode}" var="orderDetailsUrl" htmlEscape="false">
												<spring:param name="orderCode" value="${order.code}"/>
											</spring:url>
											<a href="${fn:escapeXml(orderDetailsUrl)}" class="responsive-table-link">
												${fn:escapeXml(order.code)}
											</a>
										</td>
										<td class="table-cell d-md-none"><spring:theme code="text.account.orderHistory.orderStatus"/></td>																
										<td class="status responsive-table-cell">
											<spring:theme code="text.account.order.status.display.${order.statusDisplay}"/>
										</td>
										<td class="table-cell d-md-none"><spring:theme code="text.account.orderHistory.datePlaced"/></td>
										<td class="responsive-table-cell">
											<fmt:formatDate value="${order.placed}" dateStyle="medium" timeStyle="short" type="both"/>
										</td>
										<td class="table-cell d-md-none"><spring:theme code="text.account.orderHistory.total"/></td>
										<td class="responsive-table-cell responsive-table-cell-bold">
											${fn:escapeXml(order.total.formattedValue)}
										</td>
									</ycommerce:testId>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div class="account-orderhistory-pagination mb-4">
				<nav:pagination top="false" msgKey="text.account.orderHistory.page" showCurrentPageInfo="true" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
			</div>
		</div>
	</c:if>
</div>