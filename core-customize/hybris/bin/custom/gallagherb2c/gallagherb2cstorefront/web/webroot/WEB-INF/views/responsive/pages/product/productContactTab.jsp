<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<div class="common-sub-tab-section gray-back">
   <div class="container">
       <div class="row">
           <div class="col-lg-6">
               <div class="common-product-with-title">
                   <div class="section-title">
                       Products for compliance
                   </div>
                   <div class="section-description">
                       If you have a small farm or garden, these work for you
                   </div>

                   <div class="product-list">
                       <div class="product-listing-out clearfix flexslider" id="productTileOut1">
                           <ul class="slides">
	                           <c:forEach items="${sparepart}" var="reference">
											<product:productReferenceList product="${reference}" />
										</c:forEach>							
                           </ul>
                       </div>
                   </div>

               </div>
           </div>
           <div class="col-lg-6">
           <div class="common-product-with-title">
                   <div class="section-title">
                       Products for cattle
                   </div>
                   <div class="section-description">
                       Lorem ipsum dolor sit amet et cetera
                   </div>

                   <div class="product-list">
                       <div class="product-listing-out clearfix flexslider" id="productTileOut2">
                           <ul class="slides">
                           	<c:forEach items="${others}" var="reference1">
											<product:productReferenceList product="${reference1}" />
										</c:forEach>
                            </ul>
                        </div>
                    </div>

                </div>
				<%-- <product:productReferenceCattle product="${product}"/> --%>

            </div>
        </div>
    </div>
</div>

<section id="${fn:escapeXml(title)}" class="common-sub-tab-section py-0">
	<cms:pageSlot position="TMDealer" var="comp">
		<cms:component component="${comp}"/>
	</cms:pageSlot>
</section>