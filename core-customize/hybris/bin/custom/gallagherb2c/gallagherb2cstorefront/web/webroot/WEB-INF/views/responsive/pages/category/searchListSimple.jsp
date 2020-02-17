<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<cms:pageSlot position="ProductGridSlot" var="feature" element="div"
	class="product-grid-right-result-slot">
	<cms:component component="${feature}" element="div"
		class="product__list--wrapper yComponentWrapper product-grid-right-result-component" />
</cms:pageSlot>