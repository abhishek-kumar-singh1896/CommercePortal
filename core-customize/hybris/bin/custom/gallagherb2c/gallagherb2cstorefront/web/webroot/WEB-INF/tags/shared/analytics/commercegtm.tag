<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>


<c:if test="${not empty pageType && not empty commerceGTMId}">
	<c:choose>

		<%-- Tracking - Enhanced Ecommerce Transaction --%>
		<c:when test="${pageType == 'ORDERCONFIRMATION' && newOrder}">
			<c:set var="transactionId" value="${orderData.code}" />
			<script type="text/javascript">
				dataLayer.push({
					
					    	   'transactionAffiliation': '${orderData.site}',
								'transactionId':'${orderData.code}',									
								'transactionTotal':${orderData.totalPrice.value},                     	
							 	'transactionTax':${orderData.totalTax.value},                           	
								'transactionShipping':${orderData.deliveryCost.value},   	
								

							'transactionProducts': [
								<c:set var="noOfOrders" value="${fn:length(orderData.entries)}"/>
								<c:forEach items="${orderData.entries}" var="entry" varStatus="entryCounter">
									
									
									<c:if test="${not empty entry.product}">
										{
											'name':'${entry.product.name}',                                         	
											'sku':'${entry.product.code}',
											'price':${entry.totalPrice.value},         			                       	
											'quantity':${entry.quantity}
										}
								
										<c:if test= "${noOfOrders != entryCounter.index + 1}">
											,
										</c:if>
									</c:if>
								</c:forEach>
							]
						});
			</script>
		</c:when>
	</c:choose>
</c:if>