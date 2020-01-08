<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="quickorder" tagdir="/WEB-INF/tags/responsive/quickorder"%>

<div class="quick-order-table-header">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="thead">
					<div class="th item">
						<spring:theme code="text.quickOrder.page.product" />
					</div>
					<div class="th item-feature"></div>
					<div class="th price">
						<spring:theme code="text.quickOrder.page.price" />
					</div>
					<div class="th qty">
						<spring:theme code="text.quickOrder.page.qty" />
					</div>
					<div class="th item-total-price">
						<spring:theme code="text.quickOrder.page.total" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="quick-order-table-body">
	<div class="container">
		<div class="row">
			<div class="tbody quick-order-table-body-div">
				<c:if test="${not empty quickOrderEntries}">
					<c:forEach items="${quickOrderEntries}" var="entry" varStatus="status">
						<quickorder:quickOrderLineItem productData="${entry.product}" quantity="${entry.quantity}" entryNumber="${entry.entryNumber}"/>
					</c:forEach>
				</c:if>
			</div>
		</div>
	</div>
</div>
