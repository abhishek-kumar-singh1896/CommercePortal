<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>



<div class="resizeableColorbox hidden-xs">
	<input type="hidden" id="cmsSiteChannel" value="${cmsSite.channel}" />
	<div class="product_compare">
		<div class="heading">
			<h1>
				<spring:theme code="productcomparison.header.compare_items"
					text="Compare Items" />
				${component.openPopup}
			</h1>
		</div>
		<c:if test="${empty productList}">
			<div>
				<p class="empty">
					<spring:theme code="productcomparison.label.empty_list"
						text="Please include at least 2 products to view comparison." />
				</p>
			</div>
		</c:if>
		<c:if test="${not empty productList}">
			<div class="clear-items">
				<button type="button" class="neutral" id="pcClearBtnId"
					onclick="pcClearList('<c:url value="/"/>', '${pcPopup}')">
					<spring:theme code="productcomparison.button.clear_items"
						text="CLEAR LIST" />
				</button>
			</div>
			<div class="top">
				<table>
					<tbody>
						<tr class="desc">
							<th id="product"></th>
							<c:forEach items="${productList}" var="product" varStatus="row">
								<td class="title"><span> <c:choose>
											<c:when test="${pcPopup != null}">
												<a href='#'
													onclick="pcRemoveProduct('${product.code}','<c:url value="/"/>', '${pcPopup}')"></a>
											</c:when>
											<c:otherwise>
												<a
													href='<c:url value="/compare/remove?code=${product.code}"/>'></a>
											</c:otherwise>
										</c:choose>
								</span>
									<h2>${product.name}</h2></td>
							</c:forEach>
							<tr>
						
							<tr class="pic">
								<th id="brand"></th>
								<c:forEach items="${productList}" var="product" varStatus="row">
									<c:set value="${ycommerce:productImage(product, 'thumbnail')}"
										var="primaryImage" />
									<td headers="brand"><a
										href="<c:url value="${product.url}"/>" title="#" alt=""><c:choose>
												<c:when test="${not empty primaryImage}">
													<c:choose>
														<c:when test="${not empty primaryImage.altText}">
															<img src="${primaryImage.url}"
																alt="${fn:escapeXml(primaryImage.altText)}"
																title="${fn:escapeXml(primaryImage.altText)}" />
														</c:when>
														<c:otherwise>
															<img src="${primaryImage.url}"
																alt="${fn:escapeXml(product.name)}"
																title="${fn:escapeXml(product.name)}" />
														</c:otherwise>
													</c:choose></a> </c:when> <c:otherwise>
											<theme:image code="img.missingProductImage.${format}"
												alt="${fn:escapeXml(product.name)}"
												title="${fn:escapeXml(product.name)}" />
										</c:otherwise> </c:choose>
									</td>
								</c:forEach>
							</tr>
						<tr>
							<th id="reference"><spring:theme
									code="productcomparison.label.product_code"
									text="Product Code" /></th>
							<c:forEach items="${productList}" var="product" varStatus="row">
								<td headers="reference">${product.code}</td>
							</c:forEach>
						</tr>
						<tr class="costs">
							<th id="price"><spring:theme
									code="productcomparison.label.price" text="Price" /></th>
							<c:forEach items="${productList}" var="product" varStatus="row">
								<td headers="price">
									<c:choose>
											<c:when test="${not empty product.totalDiscounts and not empty product.totalDiscounts[0]}">
												  <span class="price strike-through"><format:fromPrice priceData="${product.price}"/></span>
												  <span class="discounted bold">${product.totalDiscounts[0].value}</span>
											</c:when>
											<c:otherwise>
												<span class="price">
													<format:fromPrice priceData="${product.price}"/>
												</span>
											</c:otherwise>
									</c:choose>
								</td>
							</c:forEach>
						</tr>
						<tr>
							<th id="image"><spring:theme
									code="productcomparison.label.brand" text="Brand" /></th>
							<c:forEach items="${productList}" var="product" varStatus="row">
								<td headers="image">${product.manufacturer}<!-- <img src="http://placehold.it/162x75"> -->
								</td>
							</c:forEach>
						</tr>
						<%-- ratings excluded --%>
						<%-- <tr>
							<th id="rating">Rating</th>
							<c:forEach items="${productList}" var="product" varStatus="row">
								<td headers="rating"><c:if
										test="${not empty product.averageRating}">
										<div class="prod_review">
											<span class="stars large" style="display: inherit;"> <span
												style="width: <fmt:formatNumber maxFractionDigits="0" value="${product.averageRating * 24}" />px;"></span>
											</span>
											<p class="average-rating">
												<fmt:formatNumber maxFractionDigits="1"
													value="${product.averageRating}" />
												/5
											</p>
										</div>
									</c:if></td>
							</c:forEach>
						</tr> --%>
						<tr>
							<th id="availability">Availability</th>
							<c:forEach items="${productList}" var="product" varStatus="row">
								<c:if test="${not product.multidimensional}" >
									<td headers="availability"><spring:theme code="productcomparison.label.${product.stock.stockLevelStatus.code}"/></td>
								</c:if>
								<c:if test="${product.multidimensional}" >
									<td headers="availability"></td>
								</c:if>
							</c:forEach>
						</tr>
					</tbody>
				</table>
			</div>
			<!--/top-->
			<!--middle-->
			<c:set
				value="${(not empty variantMap ? fn:length(variantMap) : 0) + fn:length(productList[0].classifications) }"
				var="countOfRows" />
			<c:if test="${countOfRows > 0}">
				<c:set value="${countOfRows < 7 ? 'half' : ''}" var="middleStyle" />
				
				
					<c:if test="${not empty variantMap}">
						<div class="headline">
							<spring:theme code="productcomparison.label.options" text="Options" />
						</div>
						<table class="table">
							<tbody>
								<c:forEach items="${variantMap}" var="variant"
									varStatus="variantStatus">
									<tr>
										<th>${variant.name}</th>
										<c:forEach items="${productList}" var="product"
											varStatus="row">

											<td headers="detail02 detail03">
												${variant.productAttrValueMap[product.code]}</td>
										</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>

					<c:forEach items="${productList[0].classifications}"
						var="classification" varStatus="classStatus">
						<div class="classification-headline">${classification.name}</div>
						<table class="table">
							<tbody>
								<c:forEach items="${classification.features}" var="feature"
									varStatus="featureStatus">
									<tr>
										<th>${feature.name}</th>
										<c:forEach items="${productList}" var="product"
											varStatus="row">

											<td headers="detail02 detail03"><c:forEach
													items="${product.classifications[classStatus.index].features[featureStatus.index].featureValues}"
													var="featureValue" varStatus="fvStatus">
													<c:set
														value="${product.classifications[classStatus.index].features[featureStatus.index]}"
														var="specFeature" />
	  								${featureValue.value}
	  								<c:choose>
														<c:when test="${specFeature.range}">
											${not fvStatus.last ? '-' : specFeature.featureUnit.symbol}
										</c:when>
														<c:otherwise>
											${specFeature.featureUnit.symbol}
											${not fvStatus.last ? '<br/>' : ''}
										</c:otherwise>
													</c:choose>
												</c:forEach>
							</td>
							</c:forEach>
									</tr>
						</c:forEach>
							
						</tbody>
						</table>
					</c:forEach>
				
				
			</c:if>
			<!--/middle-->
			<div class="bottom">
				<table>
					<tr>
						<th id="add">&nbsp;</th>
						<c:forEach items="${productList}" var="product" varStatus="row">
							<td headers="add">
								<div class="prod_add_to_cart">
									<div id="addToCartTitle" style="display: none">
										<spring:theme code="basket.added.to.basket" />
									</div>
									<product:listorderformaction product="${product}"/>
									<product:listaddtocartaction product="${product}"/>
										
									<!-- pickup in store -->
									<c:if test="${cmsSite.channel == 'B2C'}">
										<c:url var="pickUpInStoreFormAction"
											value="/store-pickup/${product.code}/pointOfServices" />

										<div class="collect_from_store clear_fix">
											<c:choose>
												<c:when test="${cartPage}">
													<a href="javascript:void(0)"
														class="click_pickupInStore_Button"
														id="product_${product.code}${entryNumber}"
														data-productcart='<p><strong>${product.price.formattedValue}</strong></p><c:if test="${not empty product.baseOptions[0].selected.variantOptionQualifiers}"><c:forEach var="variant" items="${product.baseOptions[0].selected.variantOptionQualifiers}"><c:if test="${not empty variant.value}"><p><spring:theme code="basket.pickup.product.variant" arguments="${variant.name},${variant.value}" /></p></c:if></c:forEach></c:if>'
														data-img='<product:productPrimaryImage product="${product}" format="thumbnail"/>'
														data-productname="${product.name}"
														data-cartpage="${cartPage}"
														data-entryNumber="${entryNumber}"
														data-actionurl="${pickUpInStoreFormAction}"
														data-value="${quantity}"> <c:choose>
															<c:when test="${not empty deliveryPointOfService}">
																<spring:theme code="basket.page.shipping.change.store" />
															</c:when>
															<c:otherwise>
																<spring:theme code="basket.page.shipping.find.store"
																	text="Find A Store" />
															</c:otherwise>
														</c:choose>
													</a>
												</c:when>
												<c:when test="${searchResultsPage}">
													<button class="neutral large click_pickupInStore_Button"
														id="product_${product.code}${entryNumber}" type="submit"
														data-productcart='<p><strong>${product.price.formattedValue}</strong></p><c:if test="${not empty product.baseOptions[0].selected.variantOptionQualifiers}"><c:forEach var="variant" items="${product.baseOptions[0].selected.variantOptionQualifiers}"><c:if test="${not empty variant.value}"><p><spring:theme code="basket.pickup.product.variant" arguments="${variant.name},${variant.value}" /></p></c:if></c:forEach></c:if>'
														data-img='<product:productPrimaryImage product="${product}" format="thumbnail"/>'
														data-productname="${product.name}" data-cartpage="false"
														data-entryNumber="0"
														data-actionurl="${pickUpInStoreFormAction}" data-value="1">
														<spring:theme code="pickup.in.store" />
													</button>
												</c:when>
												<c:otherwise>

													<button class="neutral large click_pickupInStore_Button"
														id="product_${product.code}${entryNumber}" type="submit"
														data-productcart='<p><strong>${product.price.formattedValue}</strong></p><c:if test="${not empty product.baseOptions[0].selected.variantOptionQualifiers}"><c:forEach var="variant" items="${product.baseOptions[0].selected.variantOptionQualifiers}"><c:if test="${not empty variant.value}"><p><spring:theme code="basket.pickup.product.variant" arguments="${variant.name},${variant.value}" /></p></c:if></c:forEach></c:if>'
														data-img='<product:productPrimaryImage product="${product}" format="thumbnail"/>'
														data-productname="${product.name}" data-cartpage="false"
														data-entryNumber="0"
														data-actionurl="${pickUpInStoreFormAction}" data-value="1">
														<spring:theme code="pickup.in.store" />
													</button>
												</c:otherwise>
											</c:choose>
										</div>

										<div id="popup_store_pickup_form"
											class="pickup_store_search clearfix" style="display: none">
											<div class="item_container_holder clearfix">
												<div class="title_holder">
													<h2>
														<spring:theme code="pickup.product.availability" />
													</h2>
												</div>
											</div>

											<div class="prod_grid span-4">
												<span class="thumb"> </span> <strong class="details"></strong>
												<div class="cart">
													<p>
														<strong></strong>
													</p>
													<c:if
														test="${not empty product.baseOptions[0].selected.variantOptionQualifiers}">
														<c:forEach var="variant"
															items="${product.baseOptions[0].selected.variantOptionQualifiers}">
															<c:if test="${not empty variant.value}">
																<p>
																	<spring:theme code="basket.pickup.product.variant"
																		arguments="${variant.name},${variant.value}" />
																</p>

															</c:if>
														</c:forEach>
													</c:if>
												</div>
												<div class="quantity pickup_store_search-quantity">
													<label data-for="pickupQty"><spring:theme
															code="basket.page.quantity" /></label> <input type="text"
														size="1" maxlength="3" data-id="pickupQty" name="qty"
														class="qty" />
												</div>
											</div>

											<div class="span-17 last">
												<div class="pickup_store_search-form">
													<form name="pickupInStoreForm" action="#" method="post"
														class="form_field-input">
														<label for="locationForSearch" class="nostyle"><spring:theme
																code="pickup.search.message" /></label> <input type="text"
															name="locationQuery" data-id="locationForSearch" /> <input
															type="hidden" name="cartPage" data-id="atCartPage"
															value="${cartPage}" /> <input type="hidden"
															name="entryNumber" value="${entryNumber}"
															class="entryNumber" />
														<button type="submit" class="form"
															data-id="pickupstore_search_button">
															<spring:theme code="pickup.search.button" />
														</button>
														<button type="submit" class="form"
															data-id="find_pickupStoresNearMe_button">
															<spring:theme code="storeFinder.findStoresNearMe" />
														</button>
													</form>
												</div>
												<div data-id="pickup_store_results"
													class="pickup_store_results"></div>
											</div>
										</div>
									</c:if>
								</div>
							</td>
						</c:forEach>
					</tr>
				</table>
			</div>
			<!--/bottom-->
		</c:if>
	</div>
	<!--/product_compare-->
</div>
<div class="product_compare visible-xs">
	<h3>
		<spring:theme code="productcomparison.compareNotAvailableOnMobile"
			text="Sorry, Product Comparison is not available on mobile." />
	</h3>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		pcUpdateDiffRows();
	});
</script>
