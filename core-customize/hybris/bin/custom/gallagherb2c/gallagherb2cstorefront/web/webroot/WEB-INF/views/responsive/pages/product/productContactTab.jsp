<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<c:if test="${fn:length(sparepart) gt 0 or fn:length(sparepart) gt 0}"> 
<div class="common-sub-tab-section gray-back">
   <div class="container">
       <div class="row">
	       <c:if test="${fn:length(sparepart) gt 0}"> 
	           <div class="col-lg-6">
	               <div class="common-product-with-title">
	                   <div class="section-title">
	                       ${product.sparePartsReferenceHeading}
	                   </div>
	                   <div class="section-description">
	                      ${product.sparePartsReferenceSubHeading}
	                   </div>
	
	                   <div class="product-list">
	                       <div class="product-listing-out clearfix flexslider" id="productTileOut1">
	                           <ul class="slides">
		                           <c:forEach items="${sparepart}" var="reference">
												<product:productReferenceList referenceProduct="${reference}" />
											</c:forEach>							
	                           </ul>
	                       </div>
	                   </div>
	
	               </div>
	           </div>
           </c:if>
           <c:if test="${fn:length(others) gt 0}">
	           <div class="col-lg-6">
	               <div class="common-product-with-title">
	                   <div class="section-title">
	                       ${product.othersReferenceHeading}
	                   </div>
	                   <div class="section-description">
	                       ${product.othersReferenceSubHeading}
	                   </div>
	
	                   <div class="product-list">
	                       <div class="product-listing-out clearfix flexslider" id="productTileOut2">
	                           <ul class="slides">
		                           <c:forEach items="${others}" var="reference1">
												<product:productReferenceList referenceProduct="${reference1}" />
											</c:forEach>							
	                           </ul>
	                       </div>
	                   </div>
	
	               </div>
	           </div>
           </c:if>       
       </div>
    </div>
</div>
</c:if>

<section id="${fn:escapeXml(title)}" class="common-sub-tab-section py-0">
	<cms:pageSlot position="TMDealer" var="comp">
		<cms:component component="${comp}"/>
	</cms:pageSlot>
</section>