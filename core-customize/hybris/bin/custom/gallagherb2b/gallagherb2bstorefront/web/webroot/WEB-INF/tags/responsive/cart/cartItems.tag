<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

 <c:set var="errorStatus" value="<%= de.hybris.platform.catalog.enums.ProductInfoStatus.valueOf(\"ERROR\") %>" />

 <ul class="item__list item__list__cart">
    <li class="hidden-xs hidden-sm">
        <ul class="item__list--header">
            <li class="item__toggle"></li>
            <li class="item__sno"><spring:theme code="basket.page.serialNumber"/></li>
            <li class="item__image"></li>
            <li class="item__info"><spring:theme code="basket.page.item"/></li>
            <li class="item__price"><spring:theme code="basket.page.price"/></li>
            <li class="item__quantity"><spring:theme code="basket.page.qty"/></li>
            <li class="item__delivery"><spring:theme code="basket.page.delivery"/></li>
            <li class="item__total--column"><spring:theme code="basket.page.total"/></li>
            <li class="item__remove"></li>
        </ul>
    </li>

    <c:forEach items="${cartData.entries}" var="entry" varStatus="loop">

        <c:if test="${not empty entry.statusSummaryMap}" >
            <c:set var="errorCount" value="${entry.statusSummaryMap.get(errorStatus)}"/>
            <c:if test="${not empty errorCount && errorCount > 0}" >
                <div class="notification has-error">
                    <spring:theme code="basket.error.invalid.configuration" arguments="${errorCount}"/>
                    <a href="<c:url value="/cart/${entry.entryNumber}/configuration/${entry.configurationInfos[0].configuratorType}" />" >
                        <spring:theme code="basket.error.invalid.configuration.edit"/>
                    </a>
                </div>
            </c:if>
        </c:if>
        <c:set var="showEditableGridClass" value=""/>
        <c:url value="${entry.product.url}" var="productUrl"/>
    
        <li class="item__list--item">
            <%-- chevron for multi-d products --%>
            <div class="hidden-xs hidden-sm item__toggle">
                <c:if test="${entry.product.multidimensional}" >
                    <div class="js-show-editable-grid" data-index="${loop.index}" data-read-only-multid-grid="${not entry.updateable}">
                        <ycommerce:testId code="cart_product_updateQuantity">
                            <span class="glyphicon glyphicon-chevron-down"></span>
                        </ycommerce:testId>
                    </div>
                </c:if>
            </div>
			
			<%-- serial number --%>
            <div class="item__sno hidden-sm">
			 	<span>${loop.count}</span>
			</div>
			
            <%-- product image --%>
            <div class="item__image">
<%--                 <a href="${productUrl}"><product:productPrimaryImage product="${entry.product}" format="thumbnail"/></a> --%>
     				 <a href="${productUrl}"><product:productIconImage product="${entry.product}" format="thumbnail"/></a>
            </div>

            <%-- product name, code, promotions --%>
            <div class="item__info">
                <ycommerce:testId code="cart_product_name">
                    <a href="${productUrl}"><span class="item__name">${entry.product.name}</span></a>
                </ycommerce:testId>
                
                <%-- short description --%>
				<div class="item__shortDescription">${entry.product.shortDescription}</div>
                
                <%-- <div class="item__code">${entry.product.code}</div> --%>
                
                <div class="item__partNumber">${entry.product.partNumber}</div>

                <%-- availability --%>
                <div class="item__stock">
                    <c:set var="entryStock" value="${entry.product.stock.stockLevelStatus.code}"/>
                    <c:forEach items="${entry.product.baseOptions}" var="option">
                        <c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
                            <c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
                                <div>
                                    <strong>${selectedOption.name}:</strong>
                                    <span>${selectedOption.value}</span>
                                </div>
                                <c:set var="entryStock" value="${option.selected.stock.stockLevelStatus.code}"/>
                            </c:forEach>
                        </c:if>
                    </c:forEach>

                    <div>
                        <c:choose>
                            <c:when test="${not empty entryStock and entryStock ne 'outOfStock' or entry.product.multidimensional}">
                                <span class="stock"><spring:theme code="product.variants.in.stock"/></span>
                            </c:when>
                            <c:otherwise>
                                <span class="out-of-stock"><spring:theme code="product.variants.out.of.stock"/></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <c:if test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
                    <c:forEach items="${cartData.potentialProductPromotions}" var="promotion">
                        <c:set var="displayed" value="false"/>
                        <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
                            <c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber && not empty promotion.description}">
                                <c:set var="displayed" value="true"/>

                                    <div class="promo">
                                         <ycommerce:testId code="cart_potentialPromotion_label">
                                             ${promotion.description}
                                         </ycommerce:testId>
                                    </div>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </c:if>
                <c:if test="${ycommerce:doesAppliedPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
                    <c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
                        <c:set var="displayed" value="false"/>
                        <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
                            <c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
                                <c:set var="displayed" value="true"/>
                                <div class="promo">
                                    <ycommerce:testId code="cart_appliedPromotion_label">
                                        ${promotion.description}
                                    </ycommerce:testId>
                                </div>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </c:if>

                <c:if test="${entry.product.configurable}">
                    <div class="hidden-xs hidden-sm">
                        <c:url value="/cart/${entry.entryNumber}/configuration/${entry.configurationInfos[0].configuratorType}" var="entryConfigUrl"/>
                        <div class="item__configurations">
                            <c:forEach var="config" items="${entry.configurationInfos}">
                                <c:set var="style" value=""/>
                                <c:if test="${config.status eq errorStatus}">
                                    <c:set var="style" value="color:red"/>
                                </c:if>
                                <div class="item__configuration--entry" style="${style}">
                                    <div class="row">
                                        <div class="item__configuration--name col-sm-4">
                                                ${config.configurationLabel}
                                                <c:if test="${not empty config.configurationLabel}">:</c:if>

                                        </div>
                                        <div class="item__configuration--value col-sm-8">
                                                ${config.configurationValue}
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <c:if test="${not empty entry.configurationInfos}">
                            <div class="item__configurations--edit">
                                <a class="btn" href="${entryConfigUrl}"><spring:theme code="basket.page.change.configuration"/></a>
                            </div>
                        </c:if>
                    </div>
                </c:if>
            </div>

            <%-- price --%>
            <div class="item__price">
                <span class="visible-xs visible-sm"><spring:theme code="basket.page.itemPrice"/>: </span>
                <c:choose>
					<c:when test="${entry.basePrice.value ne entry.sellingPrice.value}">
											<span class="font-size12 actual bold strike-through"> <format:price
													priceData="${entry.basePrice}" displayFreeForZero="true" /></span>
											<span class="discounted bold"> <format:price
													priceData="${entry.sellingPrice}" displayFreeForZero="true" /></span>
										</c:when>
										<c:otherwise>
											<span class="actual bold"> <format:price
													priceData="${entry.basePrice}" displayFreeForZero="true" /></span>
										</c:otherwise>
									</c:choose>
            </div>

            <%-- quantity --%>
            <div class="item__quantity hidden-xs hidden-sm">
                <c:choose>
                    <c:when test="${not entry.product.multidimensional}" >
                        <c:url value="/cart/update" var="cartUpdateFormAction" />
                        <form:form id="updateCartForm${loop.index}" action="${cartUpdateFormAction}" method="post" modelAttribute="updateQuantityForm${entry.entryNumber}"
                                    class="js-qty-form${loop.index}"
                                    data-cart='{"cartCode" : "${cartData.code}","productPostPrice":"${entry.basePrice.value}","productName":"${entry.product.name}"}'>
                            <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
                            <input type="hidden" name="productCode" value="${entry.product.code}"/>
                            <input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
                            <ycommerce:testId code="cart_product_quantity">
                                <form:label cssClass="visible-xs visible-sm" path="quantity" for="quantity${entry.entryNumber}"></form:label>
                                <form:input cssClass="form-control js-update-entry-quantity-input" disabled="${not entry.updateable}" type="text" size="1" id="quantity_${loop.index}" path="quantity" />
                            </ycommerce:testId>
                        </form:form>
                    </c:when>
                    <c:otherwise>
                        <c:url value="/cart/updateMultiD" var="cartUpdateMultiDFormAction" />
                        <form:form id="updateCartForm${loop.index}" action="${cartUpdateMultiDFormAction}" method="post" class="js-qty-form${loop.index}" modelAttribute="updateQuantityForm${loop.index}">
                            <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
                            <input type="hidden" name="productCode" value="${entry.product.code}"/>
                            <input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
                            <label class="visible-xs visible-sm"><spring:theme code="basket.page.qty"/>:</label>
                            <span class="qtyValue"><c:out value="${entry.quantity}" /></span>
                            <%--<input type="text" value="${entry.quantity}" class="form-control qtyValue" name="quantity" readonly>--%>
                            <input type="hidden" name="quantity" value="0"/>
                            <ycommerce:testId code="cart_product_updateQuantity">
                                <div id="QuantityProduct${loop.index}" class="updateQuantityProduct"></div>
                            </ycommerce:testId>
                        </form:form>
                    </c:otherwise>
                </c:choose>
            </div>

            <%-- delivery --%>
            <div class="item__delivery">
                <c:if test="${entry.product.purchasable}">
                    <c:if test="${not empty entryStock and entryStock ne 'outOfStock'}">
                        <c:if test="${entry.deliveryPointOfService eq null or not entry.product.availableForPickup}">
                            <span class="item__delivery--label"><spring:theme code="basket.page.shipping.ship"/></span>
                        </c:if>
                    </c:if>
                    <c:if test="${not empty entry.deliveryPointOfService.name}">
                        <span class="item__delivery--label"><spring:theme code="basket.page.shipping.pickup"/></span>
                    </c:if>

                    <c:if test="${entry.product.availableForPickup and not empty entry.deliveryPointOfService.name}">
                        <div class="item__delivery--store">${entry.deliveryPointOfService.name}</div>
                    </c:if>
                </c:if>
            </div>

            <%-- total --%>
            <ycommerce:testId code="cart_totalProductPrice_label">
                <div class="item__total js-item-total hidden-xs hidden-sm">
                    <format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/>
                </div>
            </ycommerce:testId>

            <%-- remove icon --%>
            <div class="item__remove">
                <c:if test="${entry.updateable}" >
                    <ycommerce:testId code="cart_product_removeProduct">
                    	<button class="btn js-submit-remove-product-multi-d" data-index="${loop.index}" id="removeEntry_${loop.index}">
                        	<span class="glyphicon glyphicon-remove"></span>
                        </button>
                    </ycommerce:testId>
                </c:if>
            </div>

            <div class="item__quantity__total visible-xs visible-sm">
                <c:if test="${entry.product.multidimensional}" >
                    <ycommerce:testId code="cart_product_updateQuantity">
                        <c:set var="showEditableGridClass" value="js-show-editable-grid"/>
                    </ycommerce:testId>
                </c:if>
                <div class="details ${showEditableGridClass}" data-index="${loop.index}" data-read-only-multid-grid="${not entry.updateable}">
                    <div class="qty">
                        <c:choose>
                            <c:when test="${not entry.product.multidimensional}" >
                                <c:url value="/cart/update" var="cartUpdateFormAction" />
                                <form:form id="updateCartForm${loop.index}" action="${cartUpdateFormAction}" method="post" modelAttribute="updateQuantityForm${entry.entryNumber}"
                                            class="js-qty-form${loop.index}"
                                            data-cart='{"cartCode" : "${cartData.code}","productPostPrice":"${entry.basePrice.value}","productName":"${entry.product.name}"}'>
                                    <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
                                    <input type="hidden" name="productCode" value="${entry.product.code}"/>
                                    <input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
                                    <ycommerce:testId code="cart_product_quantity">
                                        <form:label cssClass="" path="quantity" for="quantity${entry.entryNumber}"><spring:theme code="basket.page.qty"/>:</form:label>
                                        <form:input cssClass="form-control js-update-entry-quantity-input" disabled="${not entry.updateable}" type="text" size="1" id="quantity_${loop.index}" path="quantity" />
                                    </ycommerce:testId>
                                </form:form>
                            </c:when>
                            <c:otherwise>
                                <c:url value="/cart/updateMultiD" var="cartUpdateMultiDFormAction" />
                                <form:form id="updateCartForm${loop.index}" action="${cartUpdateMultiDFormAction}" method="post" class="js-qty-form${loop.index}" modelAttribute="updateQuantityForm${loop.index}">
                                    <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
                                    <input type="hidden" name="productCode" value="${entry.product.code}"/>
                                    <input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
                                    <label><spring:theme code="basket.page.qty"/>:</label>
                                    <span class="qtyValue"><c:out value="${entry.quantity}" /></span>
                                    <%--<input type="text" value="${entry.quantity}" class="form-control qtyValue" name="quantity" readonly>--%>
                                    <input type="hidden" name="quantity" value="0"/>
                                    <ycommerce:testId code="cart_product_updateQuantity">
                                        <div id="QuantityProduct${loop.index}" class="updateQuantityProduct"></div>
                                    </ycommerce:testId>
                                </form:form>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${entry.product.multidimensional}" >
                            <ycommerce:testId code="cart_product_updateQuantity">
                                <span class="glyphicon glyphicon-chevron-right"></span>
                            </ycommerce:testId>
                        </c:if>
                        <ycommerce:testId code="cart_totalProductPrice_label">
                            <div class="item__total js-item-total">
                                <format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/>
                            </div>
                        </ycommerce:testId>
                    </div>
                </div>

                <c:if test="${entry.product.configurable}">
                    <div class="hidden-md hidden-lg">
                        <c:url value="/cart/${entry.entryNumber}/configuration/${entry.configurationInfos[0].configuratorType}" var="entryConfigUrl"/>
                        <div class="item__configurations">
                            <c:forEach var="config" items="${entry.configurationInfos}">
                                <c:set var="style" value=""/>
                                <c:if test="${config.status eq errorStatus}">
                                    <c:set var="style" value="color:red"/>
                                </c:if>
                                <div class="item__configuration--entry" style="${style}">
                                    <div class="row">
                                        <div class="item__configuration--name col-sm-4">
                                                ${config.configurationLabel}
                                                <c:if test="${not empty config.configurationLabel}">:</c:if>
                                        </div>
                                        <div class="item__configuration--value col-sm-8">
                                                ${config.configurationValue}
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <c:if test="${not empty entry.configurationInfos}">
                            <div class="item__configurations--edit">
                                <a class="btn" href="${entryConfigUrl}"><spring:theme code="basket.page.change.configuration"/></a>
                            </div>
                        </c:if>
                    </div>
                </c:if>
            </div>
        </li>
        
        <li>
        	<spring:url value="/cart/getProductVariantMatrix" var="targetUrl"/>
			<grid:gridWrapper entry="${entry}" index="${loop.index}" styleClass="add-to-cart-order-form-wrap display-none" 
				targetUrl="${targetUrl}"/>
		</li>
    </c:forEach>
</ul>

<product:productOrderFormJQueryTemplates />
<storepickup:pickupStorePopup />