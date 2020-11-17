<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="searchUrl" required="true"%>
<%@ attribute name="searchPageData" required="true"
	type="de.hybris.platform.commerceservices.search.pagedata.SearchPageData"%>
<%@ attribute name="top" required="true" type="java.lang.Boolean"%>
<%@ attribute name="navigationBarFlag" required="false"
	type="java.lang.Boolean"%>
<%@ attribute name="showTopTotals" required="false"
	type="java.lang.Boolean"%>
<%@ attribute name="supportShowAll" required="true"
	type="java.lang.Boolean"%>
<%@ attribute name="supportShowPaged" required="true"
	type="java.lang.Boolean"%>
<%@ attribute name="additionalParams" required="false"
	type="java.util.HashMap"%>
<%@ attribute name="msgKey" required="false"%>
<%@ attribute name="showCurrentPageInfo" required="false"
	type="java.lang.Boolean"%>
<%@ attribute name="hideRefineButton" required="false"
	type="java.lang.Boolean"%>
<%@ attribute name="numberPagesShown" required="false"
	type="java.lang.Integer"%>
<%@ taglib prefix="pagination"
	tagdir="/WEB-INF/tags/responsive/nav/pagination"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="themeMsgKey"
	value="${not empty msgKey ? msgKey : 'search.page'}" />
<c:set var="showCurrPage"
	value="${not empty showCurrentPageInfo ? showCurrentPageInfo : false}" />
<c:set var="hideRefBtn" value="${hideRefineButton ? true : false}" />
<c:set var="showTotals"
	value="${empty showTopTotals ? true : showTopTotals}" />

<c:if
	test="${searchPageData.pagination.totalNumberOfResults == 0 && top && showTotals}">
	<div class="paginationBar top clearfix">
		<ycommerce:testId code="searchResults_productsFound_label">
			<div class="totalResults">
				<spring:theme code="${themeMsgKey}.totalResults"
					arguments="${searchPageData.pagination.totalNumberOfResults}" />
			</div>
		</ycommerce:testId>
	</div>
</c:if>

<c:if test="${searchPageData.pagination.totalNumberOfResults > 0}">
	<c:if test="${top}">
		<div class="sorting-section-out mb-3 ${(top)?'top':'bottom'}">
			<div class="row align-items-center">

				<c:if test="${top && showTotals}">
					<!--             <div class="row"> -->
					<div class="col-6 col-md-8 col-lg-9">
						<div class="result-found-text">
							<ycommerce:testId code="searchResults_productsFound_label">
								<c:choose>
									<c:when test="${showCurrPage}">
										<c:choose>
											<c:when
												test="${searchPageData.pagination.totalNumberOfResults == 1}">
												<spring:theme code="${themeMsgKey}.totalResultsSingleOrder" />
											</c:when>
											<c:when
												test="${searchPageData.pagination.numberOfPages <= 1}">
												<spring:theme code="${themeMsgKey}.totalResultsSinglePag"
													arguments="${searchPageData.pagination.totalNumberOfResults}" />
											</c:when>
											<c:otherwise>
												<c:set var="currentPageItems"
													value="${(searchPageData.pagination.currentPage + 1) * searchPageData.pagination.pageSize}" />
												<c:set var="upTo"
													value="${(currentPageItems > searchPageData.pagination.totalNumberOfResults ? searchPageData.pagination.totalNumberOfResults : currentPageItems)}" />
												<c:set var="currentPage"
													value="${searchPageData.pagination.currentPage * searchPageData.pagination.pageSize + 1} - ${upTo}" />
												<spring:theme code="${themeMsgKey}.totalResultsCurrPage"
													arguments="${currentPage},${searchPageData.pagination.totalNumberOfResults}" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<spring:theme code="${themeMsgKey}.totalResults"
											arguments="${searchPageData.pagination.totalNumberOfResults}" />
									</c:otherwise>
								</c:choose>
							</ycommerce:testId>
						</div>
					</div>
					<!--             </div> -->
				</c:if>



				<c:if test="${not empty searchPageData.sorts}">
					<div class="col-6 col-md-4 col-lg-3">
						<form id="sortForm1"
							name="sortForm1" method="get" action="#">
							<div class="sort-by-out">
								<select id="sortOptions1" name="sort"
									class="form-control js-example-basic-single product-list-sort-by">
									<option disabled><spring:theme
											code="${themeMsgKey}.sortTitle" /></option>
									<c:forEach items="${searchPageData.sorts}" var="sort">
										<option value="${fn:escapeXml(sort.code)}"
											${sort.selected? 'selected="selected"' : ''}>
											<c:choose>
												<c:when test="${not empty sort.name}">
                                                        ${fn:escapeXml(sort.name)}
                                                    </c:when>
												<c:otherwise>
													<spring:theme code="${themeMsgKey}.sort.${sort.code}" />
												</c:otherwise>
											</c:choose>
										</option>
									</c:forEach>
								</select>
								<c:catch var="errorException">
									<spring:eval expression="searchPageData.currentQuery.query"
										var="dummyVar" />
									<%-- This will throw an exception is it is not supported --%>
									<!-- searchPageData.currentQuery.query.value is html output encoded in the backend -->
									<input type="hidden" name="q"
										value="${searchPageData.currentQuery.query.value}" />
								</c:catch>

								<c:if test="${supportShowAll}">
									<ycommerce:testId code="searchResults_showAll_link">
										<input type="hidden" name="show" value="Page" />
									</ycommerce:testId>
								</c:if>
								<c:if test="${supportShowPaged}">
									<ycommerce:testId code="searchResults_showPage_link">
										<input type="hidden" name="show" value="All" />
									</ycommerce:testId>
								</c:if>
								<c:if test="${not empty additionalParams}">
									<c:forEach items="${additionalParams}" var="entry">
										<input type="hidden" name="${fn:escapeXml(entry.key)}"
											value="${fn:escapeXml(entry.value)}" />
									</c:forEach>
								</c:if>
							</div>
						</form>

					</div>
				</c:if>
			</div>
		</div>
	</c:if>

	<c:if test="${!top}">
		<div class="pagination-out">
			<nav aria-label="Page navigation">
				<pagination:pageSelectionPagination searchUrl="${searchUrl}"
					searchPageData="${searchPageData}"
					numberPagesShown="${numberPagesShown}" themeMsgKey="${themeMsgKey}" />
			</nav>
		</div>
	</c:if>
</c:if>