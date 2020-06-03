<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<c:set var="req" value="${pageContext.request}" />

<c:if test="${not empty accessories}">
<section id="${fn:replace(title,' ', '')}" class="common-sub-tab-section">
    <div class="use-with-section-out">
        <div class="container">
            <h4 class="small-section-title">${fn:escapeXml(title)}</h4>
            <div class="use-with-listing">
                <ul class="clearfix">
				<c:forEach items="${product.productReferences}" var="reference">
					<c:if test="${reference.referenceType eq 'ACCESSORIES'}">
                    <li>
                    	<c:url value="${reference.target.url}" var="productUrl" />
                        <div class="common-product-small-tile" onclick="${fn:escapeXml(productUrl)}">
                            <div class="row">
                                <div class="col-lg-4 col-md-5">
                                    <div class="tile-image">
                                    	<product:productIconImage product="${reference.target}" format="thumbnail"/>
                                    </div>
                                </div>
                                <div class="col-lg-8 col-md-7">
                                    <div class="tile-title"><a href="${fn:escapeXml(productUrl)}">${reference.target.name}</a></div>
                                    <div>
                                    <c:if test="${not empty reference.target.price}">
		                                    <span class="currency-text">RRP</span>
		                                </c:if>
                                        
										<product:productListerItemPrice product="${reference.target}" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                    </c:if>
                  </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</section>
</c:if>
<c:if test="${not empty product.simulator.simulatorDescription || not empty product.simulator.simulatorURL}">
  <c:url value="${product.simulator.simulatorURL}" var="simulatorUrl"/>
<div class="common-sub-tab-section pt-0">
    <section id="scale-indicator">

 

        <div class="scale-indicator-out">
            <div class="container">
                <div class="scale-indicator">
                ${product.simulator.simulatorDescription}
                    <!-- 1:1 aspect ratio -->
                 <div class="embed-responsive embed-responsive-1by1">
                      <iframe 
                            src="${fn:escapeXml(contextPath)}${product.simulator.simulatorURL}"
                            allow="autoplay; encrypted-media" allowfullscreen="allowfullscreen"
                            border="0" height="585" width="767"></iframe>
                        <!-- <iframe class="embed-responsive-item"
                                            src="https://am.gallagher.com/simulator-tw3-web/story_html5.html"
                                            allow="autoplay; encrypted-media" allowfullscreen="allowfullscreen"
                                            border="0" height="585" width="765"></iframe> -->
                    </div>

                </div>
            </div>
        </div>
    </section>
</div>
</c:if>