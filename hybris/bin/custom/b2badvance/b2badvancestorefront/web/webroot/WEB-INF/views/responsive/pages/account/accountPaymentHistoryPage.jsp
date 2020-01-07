<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>


<spring:htmlEscape defaultHtmlEscape="true" />
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<c:set var="searchUrl"
	value="/my-account/paymentHistory?sort=${ycommerce:encodeUrl(searchPageData.pagination.sort)}" />
<c:set var="messageKey" value="text.account.paymentHistory.page" />

<div class="account-section-header">
	<spring:theme code="text.account.paymentHistory" text="Payment History" />
</div>

<c:if test="${empty searchPageData.results}">
	<div
		class="account-section-content	col-md-6 col-md-push-3 content-empty">
		<ycommerce:testId code="orderHistory_noOrders_label">
			<spring:theme code="text.account.paymentHistory.noOrders" />
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
			numberPagesShown="${numberPagesShown}" showDateRange="true" />

	</div>

	<div class="title_holder negative_margin">
		<div class="title">
			<div class="title-top">
				<span></span>
			</div>
		</div>
		<h2>Card Payments</h2>
		<a target="_blank" href="https://localhost:9002//medias/AccountStatement.pdf?context=bWFzdGVyfGRvY3VtZW50c3w4OTY4NnxhcHBsaWNhdGlvbi9wZGZ8ZG9jdW1lbnRzL2hhZi9oNmUvODc5Njg2MDIxOTQyMi5wZGZ8Yjc4ZjU1ZDgwNzllZGE5N2NkZTUzMDQzMzU1ODY3MDFiNTZiMDIwYTA1YjA4YmViZjhjMTQ3ZGM1ZGM1M2E0Zg">
		<button id="btn-print-statement" class="btn-primary" style="float: right" type="button">PRINT STATEMENT</button><br><br>
		</a>

	</div>

	<div>
		<c:choose>
			<c:when test="${cardListEmpty}">
				<p>No Card Payments yet</p>
			</c:when>
			<c:otherwise>
				<div class="responsive-table">
					<table id="order_history" class="responsive-table-item">
						<thead>
							<tr class="responsive-table-head hidden-xs">
								<th id="header1"><spring:theme
										code="text.account.paymentHistory.orderNumber"
										text="Order Number" /></th>
								<th id="header2">Payment Type</th>
								<th id="header3">Card Type</th>
								<th id="header4">Card Number</th>
								<th id="header5">Amount</th>
								<th id="header6">Order Status</th>
								<th id="header7"><spring:theme
										code="text.account.paymentHistory.datePlaced"
										text="Date Placed" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${cards}" var="order">

								<c:url value="/my-account/order/${order.code}?page=paymenthistory"
									var="myAccountOrderDetailsUrl" />

								<tr>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.paymentHistory.orderNumber" />
									<td headers="header1"><ycommerce:testId
											code="orderHistory_orderNumber_link">
											<a href="${myAccountOrderDetailsUrl}">${order.code}</a>
										</ycommerce:testId></td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.paymentHistory.paymentType" />
									<td headers="header2"><ycommerce:testId
											code="orderHistory_orderStatus_label">
											<p>${order.paymentType.code}</p>
										</ycommerce:testId></td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.paymentHistory.cardType" />
									<td headers="header3">
										<p>${fn:escapeXml(order.paymentInfo.cardTypeData.code)}</p>
									</td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.paymentHistory.paymentInfo1" />
									<td headers="header4">
										<p>
											<c:if test="${order.paymentType.code eq 'CARD'}">
															${fn:escapeXml(order.paymentInfo.cardNumber)}
														</c:if>
											<c:if test="${order.paymentType.code eq 'ACCOUNT'}">
															${order.purchaseOrderNumber}
														</c:if>
										</p>
									</td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.paymentHistory.price" />
									<td headers="header5">
										<p>
											<format:price priceData="${order.totalPriceWithTax}" />
										</p>
									</td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.paymentHistory.orderStatus" />
									<td headers="header6">
										<p>${fn:escapeXml(order.status.code)}</p>
									</td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.paymentHistory.datePlaced" />
									<td headers="header7"><ycommerce:testId
											code="orderHistory_orderDate_label">
											<p>
												<fmt:formatDate value="${order.created}" dateStyle="long"
													timeStyle="short" type="both" />
											</p>
										</ycommerce:testId></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div class="spacing"></div>

	<div class="title_holder negative_margin">
		<div class="title">
			<div class="title-top">
				<span></span>
			</div>
		</div>
		<h2>Account Payments</h2>
		<a target="_blank" href="https://localhost:9002//medias/AccountStatement.pdf?context=bWFzdGVyfGRvY3VtZW50c3w4OTY4NnxhcHBsaWNhdGlvbi9wZGZ8ZG9jdW1lbnRzL2hhZi9oNmUvODc5Njg2MDIxOTQyMi5wZGZ8Yjc4ZjU1ZDgwNzllZGE5N2NkZTUzMDQzMzU1ODY3MDFiNTZiMDIwYTA1YjA4YmViZjhjMTQ3ZGM1ZGM1M2E0Zg">
		<button id="btn-print-statement" class="btn-primary" style="float: right" type="button">PRINT STATEMENT</button><br><br>
		</a>
		
	</div>

	<div>
		<c:choose>
			<c:when test="${accountListEmpty}">
				<p>No Account Payments yet</p>
			</c:when>
			<c:otherwise>
				<div class="responsive-table">
					<table id="order_history" class="responsive-table-item">
						<thead>
							<tr class="responsive-table-head hidden-xs">
								<th id="header1"><spring:theme
										code="text.account.paymentHistory.orderNumber"
										text="Order Number" /></th>
								<th id="header2">Payment Type</th>
								<th id="header3">Cost Centre</th>
								<th id="header4">Purchase Order Number</th>
								<th id="header5">Amount</th>
								<th id="header6">Due Date</th>
								<th id="header7"><spring:theme code="text.account.paymentHistory.datePlaced" text="Date Placed" /></th>

							</tr>
						</thead>
						<tbody>
							<c:forEach items="${accounts}" var="order">

								<c:url value="/my-account/order/${order.code}?page=paymenthistory"
									var="myAccountOrderDetailsUrl" />

								<tr>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.paymentHistory.orderNumber" />
									<td headers="header1"><ycommerce:testId
											code="orderHistory_orderNumber_link">
											<a href="${myAccountOrderDetailsUrl}">${order.code}</a>
										</ycommerce:testId></td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.paymentHistory.paymentType" />
									<td headers="header2"><ycommerce:testId
											code="orderHistory_orderStatus_label">
											<p>${order.paymentType.code}</p>
										</ycommerce:testId></td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.paymentHistory.costCenter" />
									<td headers="header3">
										<p>${order.costCenter.code}</p>
									</td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.paymentHistory.paymentInfo2" />
									<td headers="header4">
										<p>
											<c:if test="${order.paymentType.code eq 'CARD'}">
																${fn:escapeXml(order.paymentInfo.cardNumber)}
														</c:if>
											<c:if test="${order.paymentType.code eq 'ACCOUNT'}">
																${order.purchaseOrderNumber}
														</c:if>
										</p>
									</td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.paymentHistory.price" />
									<td headers="header5">
										<p>
											<format:price priceData="${order.totalPriceWithTax}" />
										</p>
									</td>
									<td class="hidden-sm hidden-md hidden-lg">Due Date
									<td headers="header6">
										<p id="due${order.code}">
											<c:choose>
												<c:when test="${order.code == '00010013' or order.code == '00010010'}">
													loading ...
												</c:when>
											<c:otherwise>
											PAID
											</c:otherwise>
											</c:choose>
										</p>
										
									</td>
									
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.paymentHistory.datePlaced" />
									<td headers="header7">
										<ycommerce:testId
											code="orderHistory_orderDate_label">
											<p>
												<fmt:formatDate value="${order.created}" dateStyle="long"
													timeStyle="short" type="both" />
											</p>
										</ycommerce:testId>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div class="account-orderhistory-pagination">
		<nav:pagination top="false" msgKey="${messageKey}"
			showCurrentPageInfo="true" hideRefineButton="true"
			supportShowPaged="${isShowPageAllowed}"
			supportShowAll="${isShowAllAllowed}"
			searchPageData="${searchPageData}" searchUrl="${searchUrl}"
			numberPagesShown="${numberPagesShown}" />
	</div>
	<div id="paymentModal" class="paymentModal">
  <span class="payment-modal-close cursor" onclick="closeModal()">&times;</span>
  <div class="payment-modal-content">
	<div class="headline">Card Payment</div>
		
		<form class="form-horizontal" role="form">
                  <input type="hidden" id="hidden-orderId" />
                  <div class="form-group">
				<label class="control-label " for="card_cardType">
				Card type<span class="mandatory">
						</span>
				<span class="skip">
						</span>
			</label>
			<div class="control">
				<select id="card_cardType" name="card_cardType" class="form-control" tabindex="1"><option value="" disabled="disabled" selected="selected">
							Please select a card type</option>
					<option value="003">American Express</option><option value="024">Maestro</option><option value="001">Visa</option><option value="002">Mastercard</option><option value="005">Diner's Club</option></select></div>
	</div>
                  <div class="form-group">
				<label class="control-label " for="card_nameOnCard">
			Name on card(optional)</label>
			
		<input id="card_nameOnCard" name="card_nameOnCard" class="form-control form-control" tabindex="2" type="text" value=""></div>
                  <div class="form-group">
				<label class="control-label " for="card_accountNumber">
			Card number</label>
			
		<input id="card_accountNumber" name="card_accountNumber" class="form-control form-control" tabindex="3" type="text" value="" autocomplete="off"></div>
                  <fieldset id="cardDate">
										<label for="" class="control-label">Expiry date*</label>
										<div class="row">
											<div class="col-xs-6">
												<div class="form-group">
				<label class="control-label " for="ExpiryMonth">
				Month<span class="mandatory">
						</span>
				<span class="skip">
						</span>
			</label>
			<div class="control">
				<select id="ExpiryMonth" name="card_expirationMonth" class="form-control" tabindex="6"><option value="" disabled="disabled" selected="selected">
							Month</option>
					<option value="1">01</option><option value="2">02</option><option value="3">03</option><option value="4">04</option><option value="5">05</option><option value="6">06</option><option value="7">07</option><option value="8">08</option><option value="9">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option></select></div>
	</div>
		</div>
											<div class="col-xs-6">
												<div class="form-group">
				<label class="control-label " for="ExpiryYear">
				Year<span class="mandatory">
						</span>
				<span class="skip">
						</span>
			</label>
			<div class="control">
				<select id="ExpiryYear" name="card_expirationYear" class="form-control" tabindex="7"><option value="" disabled="disabled" selected="selected">
							Year</option>
					<option value="2017">2017</option><option value="2018">2018</option><option value="2019">2019</option><option value="2020">2020</option><option value="2021">2021</option><option value="2022">2022</option><option value="2023">2023</option><option value="2024">2024</option><option value="2025">2025</option><option value="2026">2026</option><option value="2027">2027</option></select></div>
	</div>
		</div>
										</div>
									</fieldset>
				<div class="form-group">
				<label class="control-label " for="card_cvNumber">
			Card Verification Number</label>
			
		<input id="card_cvNumber" name="card_cvNumber" class="form-control form-control" tabindex="8" type="text" value=""></div>
                  <div class="form-group">
				<div class="checkbox">
	   		
	   		<label class="control-label " for="savePaymentMethod">
	   			<input id="savePaymentMethod" name="savePaymentInfo" tabindex="10" type="checkbox" value="true"><input type="hidden" name="_savePaymentInfo" value="on">Save Payment Info<span class="skip"></span>
	   		</label>
	   	</div>

</div>
                  <div class="form-group">
                    <div >
                      <button type="button" onclick="submitPayment()" data-toggle="modal" data-target="#payment-confirmation" class="btn btn-default">Submit</button>
                      <button type="button" onclick="closeModal()" class="btn btn-default">Cancel</button>
                    </div>
                  </div>
                </form>
        
          
                                
  </div>
</div>
<div id="payment-confirmation" class="modal fade" role="dialog">
	<div class="modal-dialog">
      <div class="modal-content">
	<div class="modal-body">
	<p>Your payment was successful</p>
	</div>
	<div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
	</div>
	</div>
</div>
	
</c:if>

<script type="text/javascript">
function openPaymentModal(id) {
	document.getElementById('paymentModal').style.display = "block";
	document.getElementById("hidden-orderId").value = id;
}

function closeModal() {
	document.getElementById('paymentModal').style.display = "none";
}
function submitPayment(id) {
	document.getElementById('paymentModal').style.display = "none";	
	var id = document.getElementById("hidden-orderId").value;
	changeText('due'+id, 'PAID', '','');
}

function changeText(id, text, text2, text3){
	document.getElementById(id).innerHTML = text + text2 + text3;
}

window.onload = function(){
	changeText("due00010013", "December 28, 2017 <br/> <button onclick=", "openPaymentModal('00010013') ", "id='btn-print-statement' class='btn-primary' type='button'>PAY INVOICE</button>");
	changeText("due00010010", "December 28, 2017 <br/> <button onclick=", "openPaymentModal('00010010') ", "id='btn-print-statement' class='btn-primary' type='button'>PAY INVOICE</button>");
}
</script>