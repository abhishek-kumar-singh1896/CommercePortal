<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<c:if test="${not empty maximumProducts}">
	<c:choose>
		<c:when test="${maximumProducts gt 4}">
			<c:set var="maxProductValue" value="4" />
		</c:when>
		<c:otherwise>
			<c:set var="maxProductValue" value="${maximumProducts}" />
		</c:otherwise>
	</c:choose>
	<div id="myProductModal" class="modaldata">
		<div items="4" class="modal-dialog product-popup">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">Alternative Products</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				<div class="modal-body">
					<h3 class="sub-title popUpTitle"><spring:theme code="text.alternativeProduct.heading"/></h3>
					<div class="flexslider carousel">
						<ul class="slides productsPopUp">
							<c:forEach items="${alternativeProducts}" var="reference" end="3">
								<li>
									<div class="card product-card">
										<c:url value="${reference.url}" var="productUrl" />
										<c:set var="tempicon" value="0" />
										<c:choose>
											<c:when test="${not empty reference.images}">
												<c:forEach items="${reference.images}" var="medias">
													<c:if test="${tempicon == 0 }">
														<c:if test="${medias.format eq 'product'}">
															<c:set var="tempicon" value="1" />
															<div class="imageProduct">
															<a href="${fn:escapeXml(productUrl)}"
																title="${fn:escapeXml(reference.name)}"> 
																<img src="${medias.url}" alt="${medias.altText}">
															</a>
															</div>
														</c:if>
													</c:if>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<c:if test="${tempicon == 0 }">
													<c:set value="${fn:escapeXml(reference.name)}"
														var="productNameHtml" />
														<div class="imageProduct">
													<theme:image
														code="img.missingProductImage.responsive.product"
														alt="${productNameHtml}" title="${productNameHtml}" />
														</div>
												</c:if>
											</c:otherwise>
										</c:choose>
										<div class="card-body">
											<h4 class="card-title product-name">
												<a href="${productUrl}">${reference.name}</a><span
													class="sku">${fn:escapeXml(reference.code)}</span>
											</h4>
											<div class="card-text product-desc">${reference.plpProductDescription}</div>
											<p class="product-price">
												<span class="rrp">RRP</span><label><sup class="currency-sign">${reference.price.currencyIso}</sup>${reference.price.formattedValue}</label></p>
										</div>
									</div>
								</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</c:if>
