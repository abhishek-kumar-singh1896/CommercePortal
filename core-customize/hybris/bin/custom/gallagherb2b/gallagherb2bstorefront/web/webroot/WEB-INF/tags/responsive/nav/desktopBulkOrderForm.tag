<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
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
<div
	class="scroller bulkOrderDiv vertical product-add-container">
	<div style="clear: both"></div>
	<c:choose>
		<c:when test="${component.maximumNumberRows > 0}">
			<div class="prod_add_to_cart">
				<input type="hidden" value="${fn:length(qoList)}" id="qoTableSizeId" />
				<input type="hidden" value="${component.resultListSize}"
					id="qoResultSize" /> <input type="hidden"
					value="${component.minimumCharactersForSearch}"
					id="qoMinimumCharactersForSearch" />
				<c:url value="/bulkorder" var="bulkorderUrl" />
				<input type="hidden" value="${bulkorderUrl}" id="qoUrl" />
				<spring:theme code="bulkorder.wrong_extension_msg" var="errWrongExt" />
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
				<spring:theme code="bulkorder.rows_imported_success_msg"
					var="rowsImportedSuccessMsg" />
				<input type="hidden" value="${rowsImportedSuccessMsg}"
					id="rowsImportedSuccessMsg" />
				<div style="display: none" id="msgBox">
					<br />
				</div>
				<div id="msgBoxForRows" class="qoRowsWithErrorsBlock">
					<ul id="msgBoxForRowsList"></ul>
				</div>
				<input type="hidden" value="${isValidProduct}" id="isValidProduct" />
				<div class="site-search">
					<div class="ui-front"> 
					  <div class="bulk-order-information">
					  	<h2><b>Upload Order</b></h2>
					  	 
						  <h3>The text file should list the product quantities and SKUs in the following format:</h3>
						  <ul>
							  <li>Quantity, SKU</li>
							  <li>Select a file to upload. The file must be a spreadsheet with extension .xlsx</li>
							  <li>Maximum file size: 10.00KB</li>
							  <li><a href="${bulkOrderTemplateDownloadURL}">Download template example</a></li>
						  </ul>  
						  <button class="positive btn btn-primary" onclick="qoClickBrowse()"
							id="dummyBrowseBtn">
							<spring:theme code="bulkorder.upload_file" />
						</button>
						<button class="positive btn btn-primary" id="qoAddToCart"
							onclick="qoAddToCart('${bulkorderUrl}')">
							IMPORT
							<%-- <spring:theme code="bulkorder.add_to_cart" /> --%>
						</button>
						<div class="dropFiles">
						<div class="dropFilesOr">OR</div>
						</div>
						<form id="item_form" enctype="multipart/form-data">
							<input type="file" accept=".xlsx" name="files" id="fileBrowseBtn"
								style="display: none;" onchange="qoUploadFile()" />
							<div id="dropZone">Drop your files here</div>

						</form>
					  </div>
					  <div class="bulk-order-table-container">
					  <div class="bulk-order-table">
						<table class="multi-add-product" id="qoTable">
						<tbody class="scroll-items-header">
							<tr>
								<th class="qty-header text-center"><spring:theme code="bulkorder.qty" /></th>
								<th class="product-header text-center"><spring:theme code="bulkorder.product" /></th>
								<th class="cross-header"></th>
							</tr>
							</tbody>
							<tbody class="scroll-items">
								<c:forEach items="${qoList}" var="qoData">
									<tr>
										<c:set var="pr" value="${qoData.productData}" />
										<td class="col-1-left"><input type="text"
											onchange="qoUpdateQty(${qoData.orderNumber})"
											class="text qtt" value="${pr != null ? qoData.quantity : ''}"
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
													disabled="disabled" id="qoRemove_${qoData.orderNumber}" ></a>
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
													id="qoRemove_${qoData.orderNumber}" ></a>
											</c:otherwise>
										</c:choose>
										<input type="hidden" id="code_${qoData.orderNumber}"
											value="${pr != null ? pr.code : ''}" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					  </div>
					 
					  <button class="positive btn btn-primary" onclick="addNewQoRow()">
					  ADD ROW
						</button>
</div>
						
					</div>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<component:emptyComponent />
		</c:otherwise>
	</c:choose>
</div>