<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<spring:theme code="bulkorder.default.display.overridetitle"
	var="bulkorder_default_displaytitle" />
	<div id="addToCartTitle" class="display-none">
	<spring:theme code="basket.added.to.basket"/>
</div>
<div class="container bulkOrderDiv">
	<div
		class="vertical product-add-container hidden-lg hidden-md mobileBulkOrder">
		<div style="clear: both"></div>
		<c:choose>
			<c:when test="${component.maximumNumberRows > 0}">
				<div class="prod_add_to_cart ">
					<input type="hidden" value="${fn:length(qoList)}"
						id="qoTableSizeId" /> <input type="hidden"
						value="${component.resultListSize}" id="qoResultSize" /> <input
						type="hidden" value="${component.minimumCharactersForSearch}"
						id="qoMinimumCharactersForSearch" />
					<c:url value="/bulkorder" var="bulkorderUrl" />
					<input type="hidden" value="${bulkorderUrl}" id="qoUrl" />
					<spring:theme code="bulkorder.wrong_extension_msg"
						var="errWrongExt" />
					<spring:theme code="bulkorder.wrong_product_code"
						var="wrongProductCode" />
					<input type="hidden" value="${wrongProductCode}"
						id="wrongProductCode" /> <input type="hidden"
						value="${errWrongExt}" id="errWrongExt" />
					<spring:theme code="bulkorder.unknown_error" var="unknownError" />
					<input type="hidden" value="${unknownError}" id="unknownError" />
					<spring:theme code="bulkorder.not_imported_rows_error"
						var="notImportedRowsError" />
					<input type="hidden" value="${notImportedRowsError}"
						id="notImportedRowsError" />
					<div style="display: none" id="msgBox">
						<br />
					</div>
					<div id="msgBoxForRows" class="qoRowsWithErrors"></div>
					<input type="hidden" value="${isValidProduct}" id="isValidProduct" />
					<div class="site-search">
						<div class="ui-front">
							<div class="col-xs-10 table-responsive">
								<table
									class="multi-add-product table" id="qoTable">
									<tbody class="scroll-items-header">
										<tr>
											<th class="bulk_order_label qty"><spring:theme
													code="bulkorder.qty" /></th>
											<th class="bulk_order_label product"><spring:theme
													code="bulkorder.product" /></th>
											<th></th>
										</tr>
									</tbody>


									<tbody class="scroll-items">
										<c:forEach items="${qoList}" var="qoData">
											<tr>
												<c:set var="pr" value="${qoData.productData}" />
												<td class="col-1-left"><input type="text"
													onchange="qoUpdateQty(${qoData.orderNumber})"
													class="text qtt"
													value="${pr != null ? qoData.quantity : ''}"
													id="qty_${qoData.orderNumber}" /></td>
												<c:choose>
													<c:when test="${pr == null}">
														<td class="col-2-center" id="td_${qoData.orderNumber}"><input type="text"
													class="text product js-bulk-search-input" value=""
													id="qo_${qoData.orderNumber}"
													data-options='{"autocompleteUrl" : "${bulkorderUrl}","minCharactersBeforeRequest" : "${qoMinimumCharactersForSearch}","waitTimeBeforeRequest" : "500","displayProductImages" : true}' />
												</td>
														<td class="col-3-right"><a class="cross-button"
															onclick="qoRemove(${qoData.orderNumber})"
															disabled="disabled" id="qoRemove_${qoData.orderNumber}" ></a></td>
													</c:when>
													<c:otherwise>
														<td class="col-2-center" id="td_${qoData.orderNumber}">
															<div style="clear: both; width: 150px;">
																<div class="pr-img">
																	<c:forEach items="${pr.images}" var="image">
																		<c:if test="${image.format == 'cartIcon'}">
																			<c:set var="imageCartUrl" value="${image.url}" />

																		</c:if>

																	</c:forEach>
																	<img src="${imageCartUrl}" alt="" />
																</div>
																<div class="pr-attr">
																	<div class="pr-code">${pr.code}</div>
																	<div class="pr-manufacturer">${pr.manufacturer}</div>
																	<div class="pr-name">${pr.name}</div>
																</div>

															</div>
														</td>
														<td class="col-3-right"><a class="cross-button"
															onclick="qoRemove(${qoData.orderNumber})"
															id="qoRemove_${qoData.orderNumber}" ></a></td>
													</c:otherwise>
												</c:choose>
											</tr>
												<input type="hidden"
													id="code_${qoData.orderNumber}"
													value="${pr != null ? pr.code : ''}" />
										</c:forEach>

										<tr>
											<td><button
													class="positive btn btn-primary glyphicon glyphicon-shopping-cart "
													id="qoAddToCart" onclick="qoAddToCart('${bulkorderUrl}')">
												</button></td>
											<td></td>
											<td class="col-3-right"><a id="qoAddRow" onclick="addNewQoRow()"></a></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="col-xs-1"></div>
					<%-- <div class="row col-xs-12">
						
						
						<div class="row col-xs-1">
						</div>
						<div class="row col-xs-10">
						<button class="positive btn btn-primary glyphicon glyphicon-shopping-cart " id="qoAddToCart"
							onclick="qoAddToCart('${bulkorderUrl}')">
						</button>
						</div>
						<div class="row col-xs-1">
						<a  id="qoAddRow" onclick="addNewQoRow()">
						</a>
						</div>
					</div> --%>
				</div>
			</c:when>
			<c:otherwise>
				<component:emptyComponent />
			</c:otherwise>
		</c:choose>
	</div>

</div>