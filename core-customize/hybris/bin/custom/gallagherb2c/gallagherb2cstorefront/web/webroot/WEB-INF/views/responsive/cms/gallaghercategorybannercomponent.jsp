<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="banner-out"
	style="background: url(${media.url}) left top no-repeat; background-size: cover;">
	<div class="orange-shade"></div>
	<div class="container">
		<div class="banner-inner">
			<div class="breadcrumb-out">
				<c:if test="${fn:length(breadcrumbs) > 0}">
					<nav aria-label="breadcrumb">
						<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
					</nav>
				</c:if>

			</div>

			<div class="banner-text-container">
				<h1>${categoryName}</h1>
				${searchPageData.description}
				<p></p>
			</div>
		</div>
	</div>
</div>