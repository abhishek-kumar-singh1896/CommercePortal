<%@ page trimDirectiveWhitespaces="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="product-details-container-out">
	<div class="product-detail-tab-container">
		<div class="product-detail-tab">
			<div class="container">
				<nav id="productDetailTab" class="navbar" data-spy="affix">
					<ul class="nav nav-pills nav-fill">
						<c:forEach var="component" items="${components}"
							varStatus="children">
							<c:set var="title" value="${fn:replace(component.title,' ', '')}" />
							<c:if test="${fn:contains(title, '&')}">
								<c:set var="title" value="${fn:replace(title,'&', '')}" />
							</c:if>
							<c:if test="${component.visible eq true}">

								<c:choose>
									<c:when test="${not empty component.actions}">
										<cms:component
												component="${component}" evaluateRestriction="true" />
									</c:when>
									<c:otherwise>
										<li class="nav-item"><c:if test="${children.index eq 0}">
												<a class="nav-link d-flex align-content-center flex-wrap"
													href="#${title}"> <span>${component.title}</span>
												</a>
											</c:if> <c:if test="${children.index ne 0}">
												<a class="nav-link d-flex align-content-center flex-wrap"
													href="#${title}"> <span>${component.title}</span>
												</a>
											</c:if></li>
									</c:otherwise>
								</c:choose>

							</c:if>
						</c:forEach>
					</ul>
				</nav>
			</div>
		</div>

		<div data-target="#productDetailTab" data-offset="0">
			<c:forEach var="component" items="${components}">
				<c:if test="${empty component.actions}">
					<cms:component component="${component}" />
				</c:if>
			</c:forEach>
		</div>
	</div>
</div>