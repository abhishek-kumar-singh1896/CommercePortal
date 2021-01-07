<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:htmlEscape defaultHtmlEscape="true" />



<div id="recommendations-overlay" class="modaldata">
	<div class="modal-dialog recommendations-popup">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Recommended Products or Categories</h4>
			</div>
			<div class="modal-body">
				<div class="modal-body caraousel-body">
					<div class="flexslider carousel">
						<ul class="slides productsPopUp">
							<c:forEach items="${recommendedProducts}" var="recommendedProduct">
								<c:url value="${recommendedProduct.url}" var="recommendationUrl" />
								<li onclick="window.location='${recommendationUrl}'">
									<div class="card product-card">
										<c:set var="tempicon" value="0" />
										<c:choose>
											<c:when test="${not empty recommendedProduct.images}">
												<c:forEach items="${recommendedProduct.images}" var="medias">
													<c:if test="${tempicon == 0 }">
														<c:if test="${medias.format eq 'product'}">
															<c:set var="tempicon" value="1" />
															<div class="imageProduct">
																<img src="${medias.url}" alt="${medias.altText}">
															</div>
														</c:if>
													</c:if>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<c:if test="${tempicon == 0 }">
													<c:set value="${fn:escapeXml(recommendedProduct.name)}"
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
												<a href="${productUrl}">${recommendedProduct.name}</a><span
													class="sku">${fn:escapeXml(recommendedProduct.code)}</span>
											</h4>
											<div class="card-text product-desc">${recommendedProduct.description}</div>
										</div>
									</div>
								</li>
							</c:forEach>
							<c:forEach items="${recommendedCategories}" var="recommendedCategory">
								<c:url value="${recommendedCategory.url}" var="recommendationUrl" />
								<li onclick="window.location='${recommendationUrl}'">
									<div class="card product-card">
										<c:set var="tempicon" value="0" />
										<c:choose>
											<c:when test="${not empty recommendedCategory.image}">
												<c:set var="medias" value="${recommendedCategory.image}"/>
													<c:if test="${tempicon == 0 }">
														<c:set var="tempicon" value="1" />
															<div class="imageProduct">
																<img src="${medias.url}" alt="${medias.altText}">
															</div>
													</c:if>
											</c:when>
											<c:otherwise>
												<c:if test="${tempicon == 0 }">
													<c:set value="${fn:escapeXml(recommendedCategory.name)}"
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
												<a href="${productUrl}">${recommendedCategory.name}</a><span
													class="sku">${fn:escapeXml(recommendedCategory.code)}</span>
											</h4>
											<div class="card-text product-desc">${recommendedCategory.description}</div>
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
</div>