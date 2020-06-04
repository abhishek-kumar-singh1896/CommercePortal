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
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>


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
				<button type="button" class="btn btn-small btn-primary" id="pcClearBtnId"
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
								<c:choose>
									<c:when test="${not empty product.images}">
									 <c:set var="count" value="1"/>
									 			<td headers="brand">
									 			<c:url value="${product.url}" var="productUrl" />
		                                       	<c:forEach items="${product.images}" var="medias">
			                                        <c:if test="${medias.format eq 'product'}">
			                                        <c:if test="${count lt 2}">
			                                        <a href="${fn:escapeXml(productUrl)}" title="${fn:escapeXml(product.name)}">
			                                        	<img src="${medias.url}" alt="${medias.altText}">
			                                        	<c:set var="count" value="${count+1}"/>
			                                        </a>
		                                        	</c:if>
			                                        </c:if>
		                                        </c:forEach></td>
	                                        </c:when>
	                                        <c:otherwise>
	                                        <td headers="brand">
	                                        <c:url value="${product.url}" var="productUrl" />
	                                        <a href="${fn:escapeXml(productUrl)}" title="${fn:escapeXml(product.name)}">
											<theme:image code="img.missingProductImage.responsive.thumbnail"
												alt="${fn:escapeXml(product.name)}"
												title="${fn:escapeXml(product.name)}" />
												</a>
												</td>
										</c:otherwise>
											</c:choose>
											
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
											&nbsp; ${not fvStatus.last ? '-' : specFeature.featureUnit.name}
										</c:when>
														<c:otherwise>
											&nbsp; ${specFeature.featureUnit.name}
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
