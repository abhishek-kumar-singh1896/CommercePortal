<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>

<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<spring:theme code="text.addToCart" var="addToCartText" />
<spring:theme code="text.popupCartTitle" var="popupCartTitleText" />
<spring:theme code="cart.page.continue" var="viewCart" />
<spring:theme code="popup.cart.total" var="total" />
<c:url value="/cart" var="cartUrl" />
<c:url value="/cart/checkout" var="checkoutUrl" />


<c:choose>
	<c:when test="${not empty cartData.quoteData}">
		<c:set var="miniCartProceed" value="quote.view" />
	</c:when>
	<c:otherwise>
		<c:set var="miniCartProceed" value="checkout.checkout" />
	</c:otherwise>
</c:choose>

<c:choose>

	<c:when test="${numberShowing > 0 }">


		<div class="your-cart-title">Your Cart</div>
		<div class="mini-cart-item-out">
			<ul>
				<c:forEach items="${entries}" var="entry" end="${numberShowing - 1}">
					<c:url value="${entry.product.url}" var="entryProductUrl" />
					<li>
						<div class="row">
							<div class="col-3 pr-0">
								<div class="mini-cart-product-img">
									<a href="${entryProductUrl}">
									<product:productIconImage product="${entry.product}" format="thumbnail"/>
<%-- 										<product:productPrimaryImage	product="${entry.product}" format="cartIcon" /> --%>
									</a>
								</div>
							</div>
							<div class="col-9">
								<div class="mini-cart-title">
									<a href="${entryProductUrl}">${fn:escapeXml(entry.quantity)}
										x ${fn:escapeXml(entry.product.name)} </a>
								</div>

								<div class="row mt-2">
									<div class="col-5">
										<div class="mini-cart-id">${entry.product.code}</div>
									</div>
									<div class="col-7 text-right">
										<div class="mini-cart-price">
											<format:price priceData="${entry.basePrice}" />
										</div>
									</div>
								</div>

							</div>

						</div>
					</li>
				</c:forEach>
			</ul>

			<div class="mini-cart-total-out">
				<div class="row align-items-center">
					<div class="col-5 pr-0">

						<c:if test="${numberItemsInCart > numberShowing}">
							<a href="${fn:escapeXml(cartUrl)}" class="showing-link"> <spring:theme
									code="popup.cart.showing"
									arguments="${numberShowing},${numberItemsInCart}" />
							</a>
						</c:if>

					</div>
					<div class="col-7">
						<div class="mini-cart-total">
							<span class="total-text">${total}</span> <span
								class="total-value"> <format:price
									priceData="${cartData.totalPrice}" />
							</span>
						</div>
					</div>
				</div>
			</div>

		</div>

		<button onclick="window.location.href = '${fn:escapeXml(cartUrl)}'"
			type="button" class="btn btn-view-cart">${viewCart}</button>
	</c:when>


	<c:otherwise>

		<div class="blank-cart-message-out">
			<div class="row align-items-center">
				<div class="col-12">
					<div class="blank-cart-message">There is no product in cart.
					</div>
				</div>
			</div>
		</div>
	</c:otherwise>
</c:choose>
