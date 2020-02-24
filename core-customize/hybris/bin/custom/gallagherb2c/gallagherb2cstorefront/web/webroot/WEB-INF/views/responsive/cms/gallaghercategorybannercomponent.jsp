<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="banner-out"
            style="background: url(${media.url}) left top no-repeat; background-size: cover;">
            <div class="orange-shade"></div>
            <div class="container">
                <div class="banner-inner">
                    <div class="breadcrumb-out">
                    	<c:if test="${fn:length(searchPageData.breadcrumbs) > 0}">
							<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
						</c:if>
                        <!-- <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="#">Home</a></li>
                                <li class="breadcrumb-item"><a href="#">Electric fencing</a></li>
                                <li class="breadcrumb-item active" aria-current="page">Power</li>
                            </ol>
                        </nav> -->
                    </div>

                    <div class="banner-text-container">
                        <h1>${searchPageData.categoryCode}</h1>
                        ${searchPageData.description}
                        <p></p>
                    </div>
                </div>
            </div>
        </div>