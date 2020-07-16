<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="tabhead" id="productVideos">
	<a href="">${fn:escapeXml(title)}</a> <span class="glyphicon"></span>
</div>

<div class="tabbody" style="display: block;">
	<div>
	<c:choose>
		<c:when test="${not empty product.videos}">
		<div class="row">
			<div class="col-md-6"> 
				<div class="carousel-with-video">
					 <div id="productDetailVideoSlider"
		                 class="flexslider slider-big-image with-video"> 
		                 <ul class="slides">
			                  <c:forEach var="entry" items="${product.videos}">
										<li class="slide">
											<div data-bynder-widget="video-item" data-media-id="${entry.id}" data-width="" data-autoplay="false"></div>
											<div class="overlayText">
												<p id="topText">${entry.description}</p>
											</div>
										</li>
									</c:forEach>
		  				</ul>
		              </div>
		                        
		                <div id="productDetailVideoCarousel"
		                   class="flexslider flex-carousel video-carousel">
		                   <ul class="slides">
		                   	<c:forEach var="entry" items="${product.videos}">
		                   	<li>
		                   		<img src="${entry.url}" class="carousel-thumb-image" />
		                   	</li>
		                   	</c:forEach>
		                   </ul>
		               </div>  
		        </div>
	        </div>
        </div>
		</c:when>
		<c:otherwise>
			<strong><spring:theme code="video.notavailable"/></strong>
		</c:otherwise>
		</c:choose>
	</div>
</div>