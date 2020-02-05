<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="hideHeaderLinks" required="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>


<spring:htmlEscape defaultHtmlEscape="true" />

<cms:pageSlot position="TopHeaderSlot" var="component" element="div">
	<cms:component component="${component}" />
</cms:pageSlot>

<header class="main-header-out">
	<div class="container">
		<div>
			<div class="header-left-section">
				<div class="logo-out gallagher-logo">
					<cms:pageSlot position="SiteLogo" var="logo" limit="1">
						<cms:component component="${logo}" element="div" />
					</cms:pageSlot>
				</div>
			</div>

			<div class="header-middle-section">
				<div class="main-nav-out">
					<cms:pageSlot position="ggB2CNavBar" var="feature">
						<ul>
							<c:forEach items="${feature.navigationNode.children}"
								var="childLevel1">
								<c:forEach items="${childLevel1.entries}" var="entry">
									<cms:component component="${entry.item}"
										evaluateRestriction="true" element="li" />
								</c:forEach>
							</c:forEach>
						</ul>
					</cms:pageSlot>
				</div>
			</div>

			<div class="header-right-section text-right">

				<div class="search-out">
					<a href="javascript:void(0)" class="search-link"> <svg
							class="search-icon">
                                <use
								xlink:href="_ui/responsive/common/images/gallagher-icons.svg#search" />
                            </svg> 
	                            <span class="search-text"> Search </span> 
	                            <span class="arrow-up"> <svg class="arrow-up-icon">
	                                    <use xlink:href="_ui/responsive/common/images/gallagher-icons.svg#arrow-up" />
                            </svg>
					</span>
					</a>
				</div>
				<!--  Add there button and cart-->

				
			</div>
			<div class="xs-header-right-section">
			<!-- mini cart for responsive -->

				<div class="search-out">
					<a href="javascript:void(0)" class="search-link"> <svg
							class="search-icon">
                                <use
								xlink:href="_ui/responsive/common/images/gallagher-icons.svg#search" />
                            </svg> <span class="arrow-up"> <svg
								class="arrow-up-icon">
                                    <use
									xlink:href="_ui/responsive/common/images/gallagher-icons.svg#arrow-up" />
                                </svg>
					</span>
					</a>
				</div>

				<div class="xs-menu-out">
					<a href="javascript:void(0)" class="hamburger-icon"> <span
						class="line1"></span> <span class="line2"></span> <span
						class="line3"></span>
					</a>
				</div>
			</div>
		</div>

		<div class="search-result-out d-none">
			<div class="container">
				<div class="search-text-box-out">
					<cms:pageSlot position="SearchBox" var="component">
						<cms:component component="${component}" element="div" class="" />
					</cms:pageSlot>
				</div>


			</div>

		</div>
	</div>
</header>

<cms:pageSlot position="BottomHeaderSlot" var="component" element="div"
	class="container-fluid">
	<cms:component component="${component}" />
</cms:pageSlot>
