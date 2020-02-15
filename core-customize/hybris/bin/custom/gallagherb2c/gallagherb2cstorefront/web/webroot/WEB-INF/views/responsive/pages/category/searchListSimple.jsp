<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


	
<cms:pageSlot position="ProductListSlot" var="feature" element="div"
	class="product-list-right-slot">
	<cms:component component="${feature}" element="div"
		class="product__list--wrapper yComponentWrapper product-list-right-component" />
</cms:pageSlot>
