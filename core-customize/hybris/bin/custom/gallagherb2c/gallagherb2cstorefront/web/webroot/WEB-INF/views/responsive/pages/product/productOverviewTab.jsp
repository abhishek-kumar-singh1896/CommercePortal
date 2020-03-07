<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section id="${fn:escapeXml(title)}" class="common-sub-tab-section">
    <div class="container">
    	<c:if test="${not empty product.summary}">
        <div class="over-view-out">
            <h1 class="overview-title">${fn:escapeXml(title)}</h1>
            <div class="row">
                <div class="col-md-6">
                    <div class="overview-left-section">
                        ${ycommerce:sanitizeHTML(product.summary)}
                    </div>
                </div>
                <div class="col-md-6">
                    <product:productDetailsClassifications product="${product}"/>
                </div>
            </div>
        </div>
        </c:if>
        <c:if test="${not empty product.videoDescription}">
        <div class="two-column-section">
           <div class="row">
               <div class="col-md-6">
                   ${ycommerce:sanitizeHTML(product.videoDescription)}
               </div> 
               <div class="col-md-6">
                   Owl carousel
               </div>
           </div>
        </div>
        </c:if>
    </div>
</section>