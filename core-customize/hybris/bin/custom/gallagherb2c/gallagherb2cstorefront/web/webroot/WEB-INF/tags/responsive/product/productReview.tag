<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ attribute name="review" required="true"
	type="de.hybris.platform.commercefacades.product.data.ReviewData"%>

<div>
	<div class="rating-out with-rating-value">
		<span class="star-rating"> <span
			style="width: ${fn:escapeXml(review.rating)*20}%;"></span>
		</span> <span class="rating-value"> (${review.rating}) </span>
		<span class="reviews-text">
			 ${review.principal.name} /
			<span class="date">
				(${review.formattedReviewDate})
			</span>
		</span>
	</div>

	<div class="rating-description">
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${fn:length(review.comment) gt 250}"> --%>
<%--      			${fn:substring(review.comment, 0, 250)} --%>
<!--      			<a href="javascript:void(0)">Read more</a> -->
<%-- 			</c:when> --%>
<%-- 			<c:otherwise> --%>
     			${review.comment}
<%--      		</c:otherwise> --%>
<%-- 		</c:choose> --%>
	</div>
</div>
