<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="tabhead">
	<a href="">${fn:escapeXml(title)}</a> <span class="glyphicon"></span>
</div>

<div class="tabbody" style="display: block;">
	<div>
	<c:choose>
		<c:when test="${not empty product.dataSheet}">
			<div class="document-download-out">

				<ul class="row align-items-stretch">

					<c:forEach var="media" items="${product.dataSheet}">
						<li class="col-md-3 col-sm-6 col-xs-6">
							<div class="download-column-out">
								<div class="download-column-title">
									<a target="_blank" href="${media.url}">
										${media.description} </a>
								</div>
								<div class="download-column-bottom-section">
									<a target="_blank" href="${media.url}"> <span
										class="document-icon"> <svg>
                                    <%-- <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#pdf-download" /> --%>
                                    <use
													xlink:href="${siteRootUrl}/theme-securityB2B/images/svg/gallagher-icons.svg#pdf-download" />
                                </svg>
									</span>
									</a> <span> ${media.altText} </span>
								</div>
							</div>
						</li>
					</c:forEach>

				</ul>

			</div>
		</c:when>
		<c:otherwise>
		<strong><spring:theme code="document.notavailable"/></strong>
		</c:otherwise>
		</c:choose>
	</div>
</div>



<%-- <div class="container-lg">
		<div class="row">
			<div class="col-md-6 col-lg-4">
			<c:if test="${not empty product.dataSheet}">
				<div class="tab-container">
				     <div class="how-to-download-section">
            <h4 class="small-section-sub-title">Related documents</h4>

            <ul class="row align-items-stretch">
            	<c:forEach var="media" items="${product.dataSheet}">
                <li class="col-md-3 col-6">
                    <div class="download-column-out">
                        <div class="download-column-title">
                            <a target="_blank" href="${media.url}">
                                ${media.description}
                            </a>
                        </div>
                        <div class="download-column-bottom-section">
                        	<!-- <a target="_blank" href="http://raymor.co.nz/wp-content/uploads/2016/10/567903_Raymor_WallBasins_Quartz-WEB.pdf"> -->
                        	<a target="_blank" href="${media.url}">
                            <span class="document-icon">
                                <svg>
                                    <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#pdf-download" />
                                    <use xlink:href="${siteRootUrl}/theme-securityB2B/images/svg/gallagher-icons.svg#arrow-right" />
                                </svg>
                            </span>
                            </a>
                            <span>
                                ${media.altText}
                            </span>
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
	

 --%>