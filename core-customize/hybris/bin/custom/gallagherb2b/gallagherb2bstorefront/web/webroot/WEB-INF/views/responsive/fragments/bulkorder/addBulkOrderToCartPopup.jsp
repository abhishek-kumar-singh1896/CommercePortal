<%@ page trimDirectiveWhitespaces="true" contentType="application/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

{	"cartData": {
		"total": 	"${cartData.totalPrice.value}",
		"products": [
		<c:forEach items="${cartData.entries}" var="cartEntry" varStatus="status">
			{
				"sku":		"${cartEntry.product.code}",
				"name": 	"${cartEntry.product.name}",
				"qty": 		"${cartEntry.quantity}",
				"price": 	"${cartEntry.basePrice.value}",
				"categories": [
				<c:forEach items="${cartEntry.product.categories}" var="category" varStatus="categoryStatus">
					"${category.name}"<c:if test="${not categoryStatus.last}">,</c:if>
				</c:forEach>
				]
			}<c:if test="${not status.last}">,</c:if>
		</c:forEach>
		]
	},
	"cartPopupHtml":	"<spring:escapeBody javaScriptEscape="true">
							<spring:theme code="text.addToCart" var="addToCartText"/>
							<c:url value="/cart" var="cartUrl"/>
							<c:url value="/cart/checkout" var="checkoutUrl"/>

							<div class="title">
								<a href="#" class="close" id="add_to_cart_close"></a>
							</div>
						<div class="add-to-cart">
							<div class="add-to-cart-item">
								<c:if test="${errorMsg}">
									<div class="cart_popup_error_msg">
										<spring:theme code="${errorMsg}" />
									</div>
								</c:if>
							
									<c:forEach items="${entries}" var="entry">
										<c:url value="${entry.product.url}" var="entryProductUrl"/>
											<div class="thumb">
												<a href="${entryProductUrl}">
													<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
												</a>
											</div>
											
											<c:choose>
												<c:when test="${not empty entry.errorMsg}">
													<div class="prod_info">
														<p>${entry.product.name}</p>
														<div class="cart_popup_error_msg">
															<spring:theme code="${entry.errorMsg}" />
														</div>
													</div>
												</c:when>
												<c:otherwise>
													<div class="details">
														<a class="name" href="${entryProductUrl}">${entry.product.name}</a>
														<div class="qty"><span><spring:theme code="popup.cart.quantity.added"/></span>&nbsp;${entry.quantity}</div>
														<c:forEach items="${entry.product.baseOptions}" var="baseOptions">
															<c:forEach items="${baseOptions.selected.variantOptionQualifiers}" var="baseOptionQualifier">
																<c:if test="${baseOptionQualifier.qualifier eq 'style' and not empty baseOptionQualifier.image.url}">
																	<div class="itemColor">
																		<span class="label"><spring:theme code="product.variants.colour"/></span>
																		<img src="${baseOptionQualifier.image.url}"  alt="${baseOptionQualifier.value}" title="${baseOptionQualifier.value}"/>
																	</div>
																</c:if>
																<c:if test="${baseOptionQualifier.qualifier eq 'size'}">
																	<div class="itemSize"> 
																		<span class="label"><spring:theme code="product.variants.size"/></span>
																			${baseOptionQualifier.value}
																	</div>
																</c:if>
															</c:forEach>
														</c:forEach>
														<div class="prod_price">
														 <span class="price"> 	<format:price priceData="${entry.product.price}"/></span>
														</div>
													</div>
												</c:otherwise>
											</c:choose>
											</br>
									</c:forEach>
									</div>
							</div>

							<div class="links">
								<a href="${cartUrl}" class="btn btn-primary btn-block add-to-cart-button">
				                    <spring:theme code="checkout.checkout" />
				                </a>
				                 <a href="" class="btn btn-default btn-block js-mini-cart-close-button">
                <spring:theme code="cart.page.continue"/>
            </a>
							</div>
						</spring:escapeBody>"
}
