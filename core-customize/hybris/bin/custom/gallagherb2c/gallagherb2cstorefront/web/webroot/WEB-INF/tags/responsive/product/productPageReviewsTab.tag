<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:url value="${product.url}/reviewhtml/3" var="getPageOfReviewsUrl"/>
<c:url value="${product.url}/reviewhtml/all" var="getAllReviewsUrl"/>
<c:url value="${product.url}/review" var="productReviewActionUrl"/>

<div class="tab-review">
	<div class="review-pagination-bar">
		<%-- <button class="btn btn-default js-review-write-toggle "><spring:theme code="review.write.title"/></button> --%>
		<div class="write-review-out">
                            <a href="javascript:void(0)" class="js-review-write-toggle write-a-review-link"><spring:theme code="review.write.title"/>
                            <span class="down-arrow-icon">
                                    <svg>
                                        <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-down" />
                                    </svg>
                                </span>
                            </a>
                        </div>

		<%-- <div class="right">
			<button class="btn btn-default all-reviews-btn"><spring:theme code="review.show.all" /></button>
			<button class="btn btn-default less-reviews-btn"><spring:theme code="review.show.less" /></button>
		</div> --%>
	</div>

	<div class="js-review-write write-a-review-container d-none">
		<form:form method="post" action="${productReviewActionUrl}" modelAttribute="reviewForm">
			<div class="form-group">
				<formElement:formInputBox idKey="review.headline" labelKey="review.headline" path="headline" inputCSS="common-input" mandatory="true" labelCSS="common-form-label"/>
			</div>
			<div class="form-group">
				<formElement:formTextArea idKey="review.comment" labelKey="review.comment" path="comment" areaCSS="form-control common-input" mandatory="true" labelCSS="common-form-label"/>
			</div>
			
			<div class="form-group">
			
				<label><spring:theme code="review.rating"/></label>
				
				
				<div class="star-rating-input clearfix">
                            <div class="rating">
                            	<c:forEach  begin="1" end="5" varStatus="loop">
                            	<c:if test="${loop.index eq 1}">
                            	<c:set var="starValue" value="5"/>
                            	</c:if>
                            	<c:if test="${loop.index eq 2}">
                            	<c:set var="starValue" value="4"/>
                            	</c:if>
                            	<c:if test="${loop.index eq 3}">
                            	<c:set var="starValue" value="3"/>
                            	</c:if>
                            	<c:if test="${loop.index eq 4}">
                            	<c:set var="starValue" value="2"/>
                            	</c:if>
                            	<c:if test="${loop.index eq 5}">
                            	<c:set var="starValue" value="1"/>
                            	</c:if>
                                <input type="radio" id="inputStar${loop.index}" name="rating" value="${starValue}" />
                                <label for="inputStar${loop.index}">
                                    <svg class="icon icon-star">
                                        <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#star" />
                                    </svg>
                                </label>
                                </c:forEach>
                            </div>
                        </div>

				<%-- <div class="rating rating-set js-ratingCalcSet">
					<div class="rating-stars js-writeReviewStars">
                        <c:forEach  begin="1" end="10" varStatus="loop">
                            <span class="js-ratingIcon glyphicon glyphicon-star ${loop.index % 2 == 0 ? 'lh' : 'fh'}"></span>
                        </c:forEach>
					</div>
				</div> --%>

				<formElement:formInputBox idKey="review.rating" labelKey="review.rating" path="rating" inputCSS="sr-only js-ratingSetInput common-input" labelCSS="sr-only common-form-label" mandatory="true"/>
	
				<formElement:formInputBox idKey="alias" labelKey="review.alias" path="alias" inputCSS="form-control common-input" mandatory="false" labelCSS="common-form-label"/>
			</div>

			<button type="submit" class="btn btn-primary" value="<spring:theme code="review.submit"/>"><spring:theme code="review.submit"/></button>
		</form:form>

	</div>

	<ul id="reviews" class="review-list" data-reviews="${fn:escapeXml(getPageOfReviewsUrl)}"  data-allreviews="${fn:escapeXml(getAllReviewsUrl)}"></ul>

	<%-- <div class="review-pagination-bar">

		<div class="right">
			<button class="btn btn-default all-reviews-btn"><spring:theme code="review.show.all" /></button>
			<button class="btn btn-default less-reviews-btn"><spring:theme code="review.show.less" /></button>
		</div>
	</div> --%>
</div>
