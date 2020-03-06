<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="reviewTitle" value="${fn:replace(title,' ', '')}"/>
<c:if test="${fn:contains(reviewTitle, '&')}">
  <c:set var="reviewTitle" value="${fn:replace(reviewTitle,'&', '')}"/>
</c:if>
<section id="${reviewTitle}" class="common-sub-tab-section">
    <div class="container">
        <h4 class="small-section-title">
            Reviews
            <c:if test="${not empty product.reviews}">
				<span class="small-text">(${fn:length(product.reviews)})</span>
			</c:if>
        </h4>

        <div class="see-all-review-out">
            <a href="javascript:void(0)" class="all-reviews-btn">See all reviews</a>
        </div>

        <div class="row mt-3">
            <c:forEach items="${product.reviews}" var="l1" varStatus="status">
              <div class="col-md-6 mb-3">
                  <div>
                      <div class="rating-out with-rating-value">
                          <span class="star-rating">
                              <span style="width: ${fn:escapeXml(l1.rating)*20}%;"></span>
                          </span>

                          <span class="rating-value">
                          (${l1.rating})
                          </span>

                          <span class="reviews-text">
                          	<c:set var="reviewDate" value="${l1.date}" />
                              ${l1.principal.name} / <span class="date"> (<fmt:formatDate value="${reviewDate}" pattern="dd/MM/yyyy" />)</span>
                          </span>
                      </div>

                      <div class="rating-description">
                      	${l1.comment}
                      </div>
                  </div>
              </div>
            </c:forEach>
        </div>

        <product:productPageReviewsTab product="${product}" />
    </div>
</section>