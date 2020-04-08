<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="reviewTitle" value="${fn:replace(title,' ', '')}"/>
<c:if test="${fn:contains(reviewTitle, '&')}">
  <c:set var="reviewTitle" value="${fn:replace(reviewTitle,'&', '')}"/>
</c:if>

<section id="${reviewTitle}" class="common-sub-tab-section">
	<div class="container">
		<h4 class="small-section-title">
		    <spring:theme code="product.produt.details.reviews.reviews" />
		    <c:if test="${not empty product.reviews}">
				<span class="small-text">(${fn:length(product.reviews)})</span>
				</c:if>
		</h4>

		<c:if test="${not empty product.reviews}">
			<div class="review-list-out">
				<ul class="row">
					<c:forEach items="${product.reviews}" var="l1" varStatus="status">
	               <c:choose>
		               <c:when test="${status.index gt 1}">
			               <li class="col-md-6 mb-3 d-none">
			                  <product:productReview review="${l1}"/>
			               </li>
		               </c:when>
		               <c:otherwise>
			               <li class="col-md-6 mb-3">
			               	<product:productReview review="${l1}"/>
			               </li>
		               </c:otherwise>
	               </c:choose>
	               
	            </c:forEach>
            </ul>
			</div>
		</c:if>

		<c:if test="${fn:length(product.reviews) gt 2}">
		  <div class="see-all-review-out">
            <a href="javascript:void(0)" class="see-review-link">
            	<spring:theme code="product.produt.details.reviews.seeAll" />
            </a>
        </div> 
		</c:if>
		
		<product:productPageReviewsTab product="${product}" />
		
	</div>
</section>