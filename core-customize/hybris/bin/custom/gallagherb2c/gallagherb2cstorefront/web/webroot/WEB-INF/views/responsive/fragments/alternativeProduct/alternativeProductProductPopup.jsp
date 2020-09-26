<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>

  <c:if test="${not empty maximumProducts}">
<c:choose>
<c:when test="${maximumProducts gt 4}">
<c:set var="maxProductValue" value="4"/>
</c:when>
<c:otherwise>
<c:set var="maxProductValue" value="${maximumProducts}"/>
</c:otherwise>
</c:choose>
<div class="">
 <div id="myProductModal" class="modaldata">
 <div items="4" class="modal-dialog product-popup">
<div class="modal-content">
<div class="modal-header">
 <h4 class="modal-title">Alternative Products</h4>
 <button type="button" class="close" data-dismiss="modal">&times;</button>
 </div>
<div class="modal-body">
 <h3 class="sub-title">The product you are looking is out of stock.Here are some alternatives:</h3>
  <div class="flexslider carousel">
 <ul class="slides">
<!-- <div class="prod-row"> 
 --><c:forEach items="${alternativeProducts}" var="reference">
<!-- <div class="col"> -->
<li>
        <div class="card product-card">
        <c:url value="${reference.url}" var="productUrl" />
        <c:forEach items="${reference.images}" var="medias">
      
				<c:if test="${medias.format eq 'product'}">
					<c:set var="tempicon" value="1" />
					<a href="${fn:escapeXml(productUrl)}"
						title="${fn:escapeXml(product.name)}"> <img class="card-img-top"
						src="${medias.url}" alt="${medias.altText}">
					</a>
			</c:if>
		</c:forEach> 

      <div class="card-body">

       <h4 class="card-title product-name"><a href="${productUrl}">${reference.name}</a><span class="sku">${fn:escapeXml(reference.code)}</span>
       </h4>
        <p class="card-text product-desc">${reference.plpProductDescription}</p>
         <p class="product-price"><span class="rrp">RRP</span>${reference.price.formattedValue}</p>
         </div>
         </div>
         </li>
         <!-- </div> -->
</c:forEach>
</ul>
</div> 
</div>
 <div class="modal-footer">
<!--         <button type="button" class="btn btn-primary close" data-dismiss="modal">Close</button>
 -->      </div> 
    </div>
    </div>
    </div>
    </div> 
    </c:if> 
 