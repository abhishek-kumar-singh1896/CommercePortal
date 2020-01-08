<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<spring:url value="/my-account/invoices/" var="invoicesLink" htmlEscape="false"/>
<c:set var="searchUrl" value="/my-account/invoices?sort=${ycommerce:encodeUrl(searchPageData.pagination.sortCode)}"/>

<div class="account-section-header">
    <spring:theme code="text.account.invoices"/>
</div>
<c:if test="${empty searchPageData.results}">
    <div class="account-section-content content-empty">
        <ycommerce:testId code="savedCarts_noOrders_label">
            <spring:theme code="text.account.invoices.noInvoices"/>
        </ycommerce:testId>
    </div>
</c:if>

<c:if test="${not empty searchPageData.results}">
    <div class="account-section-content">
        <div class="account-orderhistory-pagination">
            <nav:advancepagination top="true" msgKey="text.account.invoices.page" showCurrentPageInfo="true"
                            hideRefineButton="true"
                            supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}"
                            searchPageData="${searchPageData}" searchUrl="${searchUrl}"
                            numberPagesShown="${numberPagesShown}"/>
        </div>

        <div class="account-overview-table saved__carts__overview--table">
			<c:set var="cartIdRowMapping" value=''/>
            <table class="responsive-table">
                <thead>
                    <tr class="responsive-table-head hidden-xs">
                        <th><spring:theme code="text.account.invoices.invoiceNumber" /></th>
                        <th><spring:theme code="text.account.invoices.erpOrderNumber" /></th>
                        <th><spring:theme code="text.account.invoices.orderNumber" /></th>
                        <th><spring:theme code="text.account.invoices.status" /></th>
                        <th><spring:theme code="text.account.invoices.dueDate"/></th>
                        <th><spring:theme code="text.account.invoices.totalAmount"/></th>
                    </tr>
                </thead>
                <c:forEach items="${searchPageData.results}" var="invoice" varStatus="loop">
                    <tr id="row-${loop.index}" class="responsive-table-item"> 		
          				<td class="hidden-sm hidden-md hidden-lg">
          					<spring:theme code='text.account.invoices.invoiceNo'/>
          				</td>
                  		<td class="responsive-table-cell saved-cart-name">
                          		<a href="${fn:escapeXml(invoice.pdfUrl)}"
									class="responsive-table-link js-saved-cart-name" target="_blank">
                          			${fn:escapeXml(invoice.invoiceNumber)}
                          		</a>
                  		</td>
                  		<td class="hidden-sm hidden-md hidden-lg">
                 			<spring:theme code='text.account.invoices.erpOrderNo'/>
                 		</td>
						<td class="responsive-table-cell">
								${fn:escapeXml(invoice.erpOrderNumber)}
						</td>
						<td class="hidden-sm hidden-md hidden-lg">
                 			<spring:theme code='text.account.invoices.orderNo'/>
                 		</td>
						<td class="responsive-table-cell">
								${fn:escapeXml(invoice.hybrisOrderNumber)}
						</td>
                 		<td class="hidden-sm hidden-md hidden-lg">
                 			<spring:theme code='text.account.invoices.status'/>
                 		</td>
						<td class="responsive-table-cell">
								${fn:escapeXml(invoice.status)}
						</td>
                   		<td class="hidden-sm hidden-md hidden-lg">
                   			<spring:theme code='text.account.invoices.dueDate'/>
                   		</td>
						<td class="responsive-table-cell">
							<fmt:formatDate value="${invoice.dueDate}" dateStyle="medium" timeStyle="short" type="both" />
						</td>
						<td class="hidden-sm hidden-md hidden-lg">
							<spring:theme code='text.account.invoices.totalAmount'/>
						</td>
						<td class="responsive-table-cell saved-cart-description">
												${fn:escapeXml(invoice.totalAmount)}
						</td>      
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div class="account-orderhistory-pagination">
            <nav:advancepagination top="false" msgKey="text.account.invoices.page" showCurrentPageInfo="true"
                            hideRefineButton="true"
                            supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}"
                            searchPageData="${searchPageData}" searchUrl="${searchUrl}"
                            numberPagesShown="${numberPagesShown}"/>
        </div>
    </div>
</c:if>
