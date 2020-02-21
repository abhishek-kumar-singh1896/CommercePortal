<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="searchUrl" required="true" %>
<%@ attribute name="searchPageData" required="true" type="de.hybris.platform.commerceservices.search.pagedata.SearchPageData" %>
<%@ attribute name="numberPagesShown" required="true" type="java.lang.Integer" %>
<%@ attribute name="themeMsgKey" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:set var="hasPreviousPage" value="${searchPageData.pagination.currentPage > 0}"/>
<c:set var="hasNextPage" value="${(searchPageData.pagination.currentPage + 1) < searchPageData.pagination.numberOfPages}"/>

<c:if test="${(searchPageData.pagination.numberOfPages > 1)}">
    <ul class="pagination justify-content-center">
        <c:if test="${hasPreviousPage}">
            <li class="page-item">
                <spring:url value="${searchUrl}" var="previousPageUrl" htmlEscape="true">
                    <spring:param name="page" value="${searchPageData.pagination.currentPage - 1}"/>
                </spring:url>
                <ycommerce:testId code="searchResults_previousPage_link">
                    <a href="${previousPageUrl}" class="page-link" aria-label="Previous">
	                    <span class="arrow-left-icon">
	                           <svg>
	                           	<use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-left" />
	                            </svg>
	                    </span>
                    </a>
                </ycommerce:testId>
            </li>
        </c:if>

        <c:if test="${!hasPreviousPage}">
            <li class="page-item disabled">
            	<a class="page-link" href="#" aria-label="Previous">
                  <span class="arrow-left-icon">
                        <svg>
                           <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-left" />
                        </svg>
                   </span>
                </a>
             </li>
        </c:if>

        <c:set var="limit" value="${numberPagesShown}"/>
        <c:set var="halfLimit"><fmt:formatNumber value="${limit/2}" maxFractionDigits="0"/></c:set>
        <c:set var="beginPage">
            <c:choose>
                <%-- Limit is higher than number of pages --%>
                <c:when test="${limit gt searchPageData.pagination.numberOfPages}">1</c:when>
                <%-- Start shifting page numbers once currentPage reaches halfway point--%>
                <c:when test="${searchPageData.pagination.currentPage + halfLimit ge limit}">
                    <c:choose>
                        <c:when test="${searchPageData.pagination.currentPage + halfLimit lt searchPageData.pagination.numberOfPages}">
                            <%-- Avoid rounding issue--%>
                            <c:choose>
		                        <c:when test="${searchPageData.pagination.currentPage + 1 - halfLimit gt 0}">
		                            ${searchPageData.pagination.currentPage + 1 - halfLimit}
		                        </c:when>
		                        <c:otherwise>1</c:otherwise>
		                    </c:choose>
                        </c:when>
                        <c:otherwise>${searchPageData.pagination.numberOfPages + 1 - limit}</c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>1</c:otherwise>
            </c:choose>
        </c:set>
        <c:set var="endPage">
            <c:choose>
                <c:when test="${limit gt searchPageData.pagination.numberOfPages}">
                    ${searchPageData.pagination.numberOfPages}
                </c:when>
                <c:when test="${hasNextPage}">
                    ${beginPage + limit - 1}
                </c:when>
                <c:otherwise>
                    ${searchPageData.pagination.numberOfPages}
                </c:otherwise>
            </c:choose>
        </c:set>
        <c:forEach begin="${beginPage}" end="${endPage}" var="pageNumber">
            <c:set var="linkClass" value=""/>
            <c:choose>
                <c:when test="${searchPageData.pagination.currentPage + 1 ne pageNumber}">
                <%-- ${searchPageData.pagination } ${ pageNumber} --%>
                    <spring:url value="${searchUrl}" var="pageNumberUrl" htmlEscape="true">
                        <spring:param name="page" value="${pageNumber - 1}"/>
                    </spring:url>

                    <c:choose>
                        <c:when test="${ (searchPageData.pagination.currentPage + 1 eq beginPage) or (searchPageData.pagination.currentPage + 1 eq endPage) }">
                            <c:if test="${searchPageData.pagination.currentPage + 1 eq beginPage}">
                                <c:if test="${pageNumber gt searchPageData.pagination.currentPage + 3}">
                                    <c:set var="linkClass" value="hidden-xs"/>
                                </c:if>
                            </c:if>

                            <c:if test="${searchPageData.pagination.currentPage + 1 eq endPage}">
                                <c:if test="${pageNumber lt searchPageData.pagination.currentPage - 1}">
                                    <c:set var="linkClass" value="hidden-xs"/>
                                </c:if>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${pageNumber lt searchPageData.pagination.currentPage}">
                                <c:set var="linkClass" value="hidden-xs"/>
                            </c:if>

                            <c:if test="${pageNumber gt searchPageData.pagination.currentPage + 2}">
                                <c:set var="linkClass" value="hidden-xs"/>
                            </c:if>
                        </c:otherwise>
                    </c:choose>

                    <ycommerce:testId code="pageNumber_link">
                        <li class="page-item"><a class="page-link" href="${pageNumberUrl}">${pageNumber}</a></li>
                    </ycommerce:testId>
                </c:when>
                <c:otherwise>
                    <li class="page-item active">
                    			<a class="page-link" href="#">${pageNumber}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${hasNextPage}">
            <li class="page-item">
                <spring:url value="${searchUrl}" var="nextPageUrl" htmlEscape="true">
                    <spring:param name="page" value="${searchPageData.pagination.currentPage + 1}"/>
                </spring:url>
                <ycommerce:testId code="searchResults_nextPage_link">
                    <a href="${nextPageUrl}" class="page-link" aria-label="Next">
	                    <span class="arrow-right-icon">
	                           <svg>
	                           	<use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
	                            </svg>
	                    </span>
                    </a>
                </ycommerce:testId>
            </li>
        </c:if>

        <c:if test="${!hasNextPage}">
            <li class="page-item disabled">
            		<a class="page-link" href="#" aria-label="Next">
                     	<span class="arrow-right-icon">
                              <svg>
                                    <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
                              </svg>
                        </span>
                  </a>
            </li>
        </c:if>
    </ul>
</c:if>