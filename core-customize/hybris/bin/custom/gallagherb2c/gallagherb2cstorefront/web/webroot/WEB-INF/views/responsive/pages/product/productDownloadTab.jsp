<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<section id="${fn:replace(title,' ', '')}" class="common-sub-tab-section ptb-xs-0">
	<c:if test="${not empty product.dataSheet}">
    <div class="container">
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
    </c:if>

	<cms:pageSlot position="RegisterProduct" var="comp" >
		<cms:component component="${comp}"/>
	</cms:pageSlot>
    <c:if test="${not empty product.others}">                
    <div class="container">
      <div class="two-column-section">
          <div class="row">
              <div class="col-md-6">
                  <div class="column-full-img clearfix">
                      <div class="flexslider slider-big-image award-slider" id="awardSlider">
                          <ul class="slides">
                              <li>
                                  <img src="${commonResourcePath}/images/product-two-column-img.png" alt="Product Image">
                              </li>
                              <li>
                                  <img src="${commonResourcePath}/images/product-two-column-img.png" alt="Product Image">
                              </li>
                              <li>
                                  <img src="${commonResourcePath}/images/product-two-column-img.png" alt="Product Image">
                              </li>
                              <li>
                                  <img src="${commonResourcePath}/images/product-two-column-img.png" alt="Product Image">
                              </li>
                          </ul>
                      </div>
                  </div>
              </div>

              <div class="col-md-6">
              		${product.imageDescription}
              </div>

          </div>
      </div>
  </div>
  </c:if>
</section>