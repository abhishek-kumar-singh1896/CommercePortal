<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="product-classifications">
	<c:if test="${not empty product.classifications}">
		<c:forEach items="${product.classifications}" var="classification">
			
				<div class="row">
				<c:forEach items="${classification.features}" var="feature" varStatus="status">
				    <div class="col-md-6 mb-3">
				        <label for="warranty" class="overview-label">${fn:escapeXml(feature.name)}</label>
				        <div class="overview-value" id="warranty"><!-- 1 year -->
				        <c:forEach items="${feature.featureValues}" var="value" varStatus="status">
							${fn:escapeXml(value.value)}
							<c:choose>
								<c:when test="${feature.range}">
									${not status.last ? '-' : fn:escapeXml(feature.featureUnit.symbol)}
								</c:when>
								<c:otherwise>
									${fn:escapeXml(feature.featureUnit.symbol)}
									${not status.last ? '<br/>' : ''}
								</c:otherwise>
							</c:choose>
						</c:forEach>
				        </div>
				    </div>
				    </c:forEach>
				</div>
			
		</c:forEach>
		<c:if test="${not empty product.productReferences}">
		<c:forEach items="${product.productReferences}" var="reference">
		<c:if test="${reference.referenceType eq 'FOLLOWUP'}">
		<c:url value="${reference.target.url}" var="productUrl" />
		<div class="row mb-3">
            <div class="col-md-6">
                <label for="warranty" class="overview-label">Compatible with</label>
                <div class="overview-value" id="warranty">
                    <a href="${fn:escapeXml(productUrl)}">${reference.target.name}
                        <!-- Animal Data Transfer App
                        NAIT Exchange Software -->
                    </a>
                </div>
            </div>
        </div>
        </c:if>
        </c:forEach>
        </c:if>
	</c:if>
</div>