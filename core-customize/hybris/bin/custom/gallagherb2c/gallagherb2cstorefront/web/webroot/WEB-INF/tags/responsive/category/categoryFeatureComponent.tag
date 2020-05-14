<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ attribute name="categoryData" required="true"
	type="de.hybris.platform.commercefacades.product.data.CategoryData"%>
<%@ taglib prefix="category" tagdir="/WEB-INF/tags/responsive/category"%>

<c:url value="${categoryData.url}" var="url"/>
<div class="col-12 col-sm-12 col-md-3 mb-4">
  <div class="image-section">
    <a href="${url}">
         <c:choose>
			<c:when test="${not empty categoryData.image.url}">
		      	<img title="${categoryData.name}" alt="${categoryData.image.altText}" src="${categoryData.image.url}">
		    </c:when>
	      	<c:otherwise>
	      		<theme:image code="img.missingProductImage.responsive.thumbnail"
				alt="${categoryData.name}" title="${categoryData.name}" />
			</c:otherwise>
	  </c:choose>
    </a>
  </div>
  <div class="title-section">
    <a href="${url}">${categoryData.name}</a>
  </div>
  <div class="description-section">
    ${categoryData.description}
  </div>
</div>