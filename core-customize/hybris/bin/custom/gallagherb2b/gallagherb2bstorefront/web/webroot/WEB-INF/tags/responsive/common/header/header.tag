<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="hideHeaderLinks" required="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<cms:pageSlot position="TopHeaderSlot" var="component" element="div" >
	<cms:component component="${component}" />
</cms:pageSlot>
<input type="hidden" name="showB2BUnitsPopup" id="showB2BUnitsPopup" value="${showB2BUnitsPopup}"/>
<div class="modal-updated responsive-mega-menu-modal hidden-lg" id="responsiveMegaMenu" tabindex="-1"
			role="dialog" aria-labelledby="responsiveMegaMenuTitle" aria-hidden="true">
	<div class="modal-dialog modal-dialog-scrollable" role="document">
	
		<!-- Main menu/L1 Content -->
		<div class="modal-content xs-main-menu-l1 xs-mega-menu">
	
			<div class="xs-search-out hidden-lg">
				<div class="search-result-out">
					<div class="search-text-box-out">
						<cms:pageSlot position="SearchBox" var="component">
							<cms:component component="${component}" />
						</cms:pageSlot>
					</div>
				</div>
			</div>
	
			<div class="modal-body">
				<div class="xs-main-menu-l1-links menu-listing">
					<ul>
						<sec:authorize access="hasAnyRole('ROLE_ANONYMOUS')" >
							<li>
								<cms:pageSlot position="ggB2BLogin" var="component">
									<cms:component component="${component}" />
								</cms:pageSlot>
							</li>
						</sec:authorize>
						<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
							<li>
								<cms:pageSlot position="ggB2BLogout" var="component">
									<cms:component component="${component}" />
	                 				</cms:pageSlot>
							</li>
						</sec:authorize>
						<cms:pageSlot position="NavigationBar" var="component">
							<c:choose>
							<c:when test="${component.visible}">
								<c:set var="checkSubMenu" value="true"/>
							</c:when>
							<c:otherwise>
								<c:set var="checkSubMenu" value="false"/>
							</c:otherwise>
							</c:choose>
						</cms:pageSlot>
						<cms:pageSlot position="ggB2BNavBar" var="feature">
							<c:if test="${feature.visible}">
								<c:if test="${not empty feature.navigationNode && feature.navigationNode ne null}">
									<c:forEach items="${feature.navigationNode.children}" var="l1" varStatus="status">
										<c:forEach items="${l1.entries}" var="dropdownValue">
											<li>
												<c:url var="L1link" value="${dropdownValue.item.url}"></c:url>
												<c:choose>
													<c:when test="${dropdownValue.item.linkName eq 'PRODUCTS'}">
														<c:choose>
															<c:when test="${checkSubMenu}">
																<c:set var="checkNavigation" value="mainNavLink${status.index+1}Container"/>
																<a href="javascript:void(0)" class="with-sublevel-text l1-anchor with-sublevel" id="xsLevel1Link${status.index+1}">
																	${dropdownValue.item.linkName}
																	<span class="right-arrow-icon">
																		<svg>
																			<use xlink:href="${siteRootUrl}/theme-securityB2B/images/svg/gallagher-icons.svg#arrow-right" />
																		</svg>
																	</span>
																</a>
															</c:when>
															<c:otherwise>
																<a href="${L1link}" class="with-sublevel-text check-product">
																    ${dropdownValue.item.linkName}
																</a>
															</c:otherwise>
														</c:choose>
														
													</c:when>
													<c:otherwise>
														<a href="${L1link}" class="with-sublevel-text">
														    ${dropdownValue.item.linkName}
														</a>
													</c:otherwise>
												</c:choose>
											</li>
										</c:forEach>
									</c:forEach>
								</c:if>
							</c:if>
						</cms:pageSlot>
						<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">								
							<cms:pageSlot position="B2BMyAccount" var="feature">
								<li>
									<a href="javascript:void(0)" class="with-sublevel-text l1-anchor with-sublevel" id="xsmyacclink">
										${feature.navigationNode.name}
										<span class="right-arrow-icon">
											<svg>
												<use xlink:href="${siteRootUrl}/theme-securityB2B/images/svg/gallagher-icons.svg#arrow-right" />
											</svg>
										</span>
									</a>
								</li>
							</cms:pageSlot>
							
							<cms:pageSlot position="B2BMyCompany" var="feature">
								<li>
									<a href="javascript:void(0)" class="with-sublevel-text l1-anchor with-sublevel" id="xsmycompanylink">
										${feature.navigationNode.name}
										<span class="right-arrow-icon">
											<svg>
												<use xlink:href="${siteRootUrl}/theme-securityB2B/images/svg/gallagher-icons.svg#arrow-right" />
											</svg>
										</span>
									</a>
								</li>												
							</cms:pageSlot>
						</sec:authorize>
					</ul>
				</div>
			</div>
		</div>
		
		<nav:megaMenu />

		<!-- My Account menu content -->
		<div class="modal-content xs-main-menu-l2 xs-mega-menu d-none" id="xsmyacclinkContainer">
			<div class="modal-header">
				<div class="left-title-out">
					<a href="javascript:void(0)" class="back-to-l1">
						<span>
							<svg>
								<use xlink:href="${siteRootUrl}/theme-securityB2B/images/svg/gallagher-icons.svg#arrow-left" />
							</svg>
						</span>

						<span class="back-text"></span>
					</a>
				</div>
			</div>
			<div class="modal-body">
				<div class="xs-main-menu-l2-links menu-listing">
					<ul>
						<cms:pageSlot position="B2BMyAccount" var="feature">											
        					<c:forEach items="${feature.navigationNode.children}"
								var="childLevel1">													
								<c:forEach items="${childLevel1.entries}" var="entry">
									<cms:component component="${entry.item}" element="li" 
 								 		evaluateRestriction="true" />
								</c:forEach>
							</c:forEach>
						</cms:pageSlot>
					</ul>
				</div>
			</div>
		</div>

		<!-- My company menu content -->
		<div class="modal-content xs-main-menu-l2 xs-mega-menu d-none" id="xsmycompanylinkContainer">
			<div class="modal-header">
				<div class="left-title-out">
					<a href="javascript:void(0)" class="back-to-l1">
						<span>
							<svg>
								<use xlink:href="${siteRootUrl}/theme-securityB2B/images/svg/gallagher-icons.svg#arrow-left" />
							</svg>
						</span>
						<span class="back-text"></span>
					</a>
				</div>
			</div>
			<div class="modal-body">
				<div class="xs-main-menu-l2-links menu-listing">
					<ul>
						<cms:pageSlot position="B2BMyCompany" var="feature">											
        					<c:forEach items="${feature.navigationNode.children}"
								var="childLevel1">													
								<c:forEach items="${childLevel1.entries}" var="entry">
									<cms:component component="${entry.item}" element="li" 
 								 		evaluateRestriction="true" />
								</c:forEach>
							</c:forEach>
						</cms:pageSlot>
					</ul>
				</div>
			</div>
		</div>
		
	</div>
</div>

<header class="main-header-out shadow-sm js-mainHeader">
	<div class="container header-container-xs">
		<div class="header-out-row">
		
			<div class="header-left-section mr-auto">
				<div class="logo-out gallagher-logo">
					<cms:pageSlot position="SiteLogo" var="logo" limit="1">
						<cms:component component="${logo}" element="div" />
					</cms:pageSlot>
				</div>
			</div>

			<cms:pageSlot position="ggB2BNavBar" var="feature">
				<c:if test="${feature.visible}">
					<c:if test="${not empty feature.navigationNode && feature.navigationNode ne null}">
						<div class="header-middle-section">
							<div class="main-nav-out">
								<ul>
									<c:forEach items="${feature.navigationNode.children}" var="l1" varStatus="status">
										<c:forEach items="${l1.entries}" var="dropdownValue">
											<c:url var="L1link" value="${dropdownValue.item.url}"></c:url>
											<li class="first-level with-dropdown">
												<a href="${L1link}" id="mainNavLink${status.index+1}" class="${dropdownValue.item.styleAttributes}">
												    ${dropdownValue.item.linkName} 
												</a>
												<c:if test="${dropdownValue.item.uid eq 'B2BProductsNavLink'}">
													<div class="megamenu-out second-level-menu shadow-sm d-none" id="${checkNavigation}">
		                                            <nav:topNavigation />
	                                              </div>
	                                              </c:if>
											</li>
										</c:forEach>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
				</c:if>
			</cms:pageSlot>

			<div class="header-right-section">
				<div class="main-nav-out">
					<ul>
						<li>
							<div class="search-out">
								<a href="javascript:void(0)" class="search-link" id="searchLink1">
									<span class="nav-icon-out">
									<svg class="search-icon">
										<use xlink:href="${siteRootUrl}/theme-securityB2B/images/svg/gallagher-icons.svg#search" />
									</svg> 
									</span>
								</a> 
							</div>
						</li>
						<sec:authorize access="hasAnyRole('ROLE_ANONYMOUS')" >
							<li>
								<cms:pageSlot position="ggB2BLogin" var="component">
									<cms:component component="${component}" />
								</cms:pageSlot>
							</li>
						</sec:authorize>
						<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
							<li>
								<div class="cart-out ">
									<cms:pageSlot position="MiniCart" var="cart">
										<cms:component component="${cart}" />
									</cms:pageSlot>
								</div>
							</li>
<%-- 							<cms:pageSlot position="MiniCart" var="cart" element="div" class="miniCartSlot componentContainer mobile__nav__row--table hidden-sm hidden-md hidden-lg"> --%>
<%-- 								<cms:component component="${cart}" element="div" class="mobile__nav__row--table-cell" /> --%>
<%-- 							</cms:pageSlot> --%>
							<li>
								<c:set var="maxNumberChars" value="25" /> 
 								<c:if test="${fn:length(user.firstName) gt maxNumberChars}"> 
 									<c:set target="${user}" property="firstName"
 										value="${fn:substring(user.firstName, 0, maxNumberChars)}..." /> 
 								</c:if> 
 								
 								<cms:pageSlot position="B2BAccount" var="accountFeature">
 								<c:url value="${accountFeature.url}" var="myAccountURL"/>
									<a href="${myAccountURL}" class="btn user-btn dropdown-toggle">
											<spring:theme code="header.welcome" arguments="${user.firstName},${user.lastName}" />
									</a>
									</cms:pageSlot>
								<%-- <div class="dropdown user-dropdown">
									<button class="btn user-btn dropdown-toggle" type="button" id="dropdownMenuUser"
										data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
										<span class="user-name"><spring:theme code="header.welcome" arguments="${user.firstName},${user.lastName}" /></span>
									</button>
									<ul class="dropdown-menu dropdown-menu-right"
										aria-labelledby="dropdownMenuUser">

										<cms:pageSlot position="B2BMyAccount" var="feature">
											<li>
												<div class="menu-title">
													${feature.navigationNode.name}
												</div>
											</li>												
                      					<c:forEach items="${feature.navigationNode.children}"
												var="childLevel1">
												<c:forEach items="${childLevel1.entries}" var="entry">
													<cms:component component="${entry.item}" element="li" 
												 	evaluateRestriction="true" />
												</c:forEach>
											</c:forEach>
										</cms:pageSlot>
										
										<cms:pageSlot position="B2BMyCompany" var="feature">
											<li>
												<div class="menu-title">
													${feature.navigationNode.name}
												</div>
											</li>												
                      					<c:forEach items="${feature.navigationNode.children}"
												var="childLevel1">													
												<c:forEach items="${childLevel1.entries}" var="entry">
													<cms:component component="${entry.item}" element="li" 
												 		evaluateRestriction="true" />
												</c:forEach>
											</c:forEach>
										</cms:pageSlot>
										
										<li>
											<ycommerce:testId code="header_signOut">
												<cms:pageSlot position="ggB2BLogout" var="component">
													<cms:component component="${component}" />
                            				</cms:pageSlot>
											</ycommerce:testId>
										</li>
										
									</ul>
								</div> --%>
							</li>
						</sec:authorize>
							<%-- <sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
 								<c:set var="maxNumberChars" value="25" /> 
 								<c:if test="${fn:length(user.firstName) gt maxNumberChars}"> 
 									<c:set target="${user}" property="firstName"
 										value="${fn:substring(user.firstName, 0, maxNumberChars)}..." /> 
 								</c:if> 

								<!-- <li class="logged_in js-logged_in"> -->
									<ycommerce:testId code="header_LoggedUser">
										<spring:theme code="header.welcome" arguments="${user.firstName},${user.lastName}" />
									</ycommerce:testId>
								<!-- </li> -->
						</sec:authorize> --%>
						<!-- </li> -->
					</ul>
				</div>
			</div>
			
			<div class="xs-header-right-section">

				<div class="xs-menu-out">
					<a href="javascript:void(0)" class="hamburger-icon">
						<span class="line1"></span>
						<span class="line2"></span>
						<span class="line3"></span>
					</a>
				</div>
				<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
					<div class="xs-cart-out ">
						<cms:pageSlot position="MiniCart" var="cart">
										<cms:component component="${cart}" />
						</cms:pageSlot>
					</div>
				</sec:authorize>
			</div>
		</div>
							
	</div>
	
<%-- 	<div class="megamenu-out second-level-menu shadow-sm d-none" id="${checkNavigation}">
		<nav:topNavigation />
	</div>  above--%>
	
	<div class="megamenu-out search-result-out search-link-container d-none" id="searchLink1Container">
       <div class="container">
			<div class="search-text-box-out">
				<cms:pageSlot position="SearchBox" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot>
			</div>
		</div>
	</div>
					
<!-- 		ootb code from here -->
		<%-- <nav class="navigation navigation--top hidden-xs hidden-sm">	
			<div class="col-sm-12 col-md-8">
				<div class="nav__right">
					<ul class="nav__links nav__links--account">
						<c:if test="${empty hideHeaderLinks}">
							<c:if test="${uiExperienceOverride}">
<!-- 								<li class="backToMobileLink"> -->
									<c:url value="/_s/ui-experience?level=" var="backToMobileStoreUrl" />
									<a href="${fn:escapeXml(backToMobileStoreUrl)}">
										<spring:theme code="text.backToMobileStore" />
<!-- 									</a> -->
<!-- 								</li> -->
							</c:if>

 							<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
 								<c:set var="maxNumberChars" value="25" /> 
 								<c:if test="${fn:length(user.firstName) gt maxNumberChars}"> 
 									<c:set target="${user}" property="firstName"
 										value="${fn:substring(user.firstName, 0, maxNumberChars)}..." /> 
 								</c:if> 

								<li class="logged_in js-logged_in">
									<ycommerce:testId code="header_LoggedUser">
										<spring:theme code="header.welcome" arguments="${user.firstName},${user.lastName}" />
									</ycommerce:testId>
								</li>
							</sec:authorize>

							 <cms:pageSlot position="HeaderLinks" var="link">
								 <cms:component component="${link}" element="li" />
							 </cms:pageSlot>

							<sec:authorize access="hasAnyRole('ROLE_ANONYMOUS')" >
<!-- 								<li class="liOffcanvas"> -->
									<ycommerce:testId code="header_Login_link">
										<c:url value="/login" var="loginUrl" />
										<a href="${fn:escapeXml(loginUrl)}">
											<spring:theme code="header.link.login" />
<!-- 										</a> -->
									</ycommerce:testId>
<!-- 								</li> -->
							</sec:authorize>

							<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')" >
								<li class="liOffcanvas">
									<ycommerce:testId code="header_signOut">
										<c:url value="/logout" var="logoutUrl"/>
										<a href="${fn:escapeXml(logoutUrl)}">
											<spring:theme code="header.link.logout" />
										</a>
									</ycommerce:testId>
								</li>
							</sec:authorize>

						</c:if>
					</ul>
				</div>
			</div>
<!-- 		</div> -->
	</nav>  --%>
	<%-- a hook for the my account links in desktop/wide desktop--%>
	<div class="hidden-xs hidden-sm js-secondaryNavAccount collapse" id="accNavComponentDesktopOne">
		<ul class="nav__links">

		</ul>
	</div>
	<div class="hidden-xs hidden-sm js-secondaryNavCompany collapse" id="accNavComponentDesktopTwo">
		<ul class="nav__links js-nav__links">

		</ul>
	</div>
	<%-- <nav class="navigation navigation--middle js-navigation--middle">
		<div class="container-fluid">
			<div class="row">
				<div class="mobile__nav__row mobile__nav__row--table">
					<div class="mobile__nav__row--table-group">
						<div class="mobile__nav__row--table-row">
							<div class="mobile__nav__row--table-cell visible-xs hidden-sm">
								<button class="mobile__nav__row--btn btn mobile__nav__row--btn-menu js-toggle-sm-navigation"
										type="button">
									<span class="glyphicon glyphicon-align-justify"></span>
								</button>
							</div>

							<div class="mobile__nav__row--table-cell visible-xs mobile__nav__row--seperator">
								<ycommerce:testId code="header_search_activation_button">
									<button	class="mobile__nav__row--btn btn mobile__nav__row--btn-search js-toggle-xs-search hidden-sm hidden-md hidden-lg" type="button">
										<span class="glyphicon glyphicon-search"></span>
									</button>
								</ycommerce:testId>
							</div>

							<c:if test="${empty hideHeaderLinks}">
								<ycommerce:testId code="header_StoreFinder_link">
									<div class="mobile__nav__row--table-cell hidden-sm hidden-md hidden-lg mobile__nav__row--seperator">
										<c:url value="/store-finder" var="storeFinderUrl"/>
										<a href="${fn:escapeXml(storeFinderUrl)}" class="mobile__nav__row--btn mobile__nav__row--btn-location btn">
											<span class="glyphicon glyphicon-map-marker"></span>
										</a>
									</div>
								</ycommerce:testId>
							</c:if>

							<cms:pageSlot position="MiniCart" var="cart" element="div" class="miniCartSlot componentContainer mobile__nav__row--table hidden-sm hidden-md hidden-lg">
								<cms:component component="${cart}" element="div" class="mobile__nav__row--table-cell" />
							</cms:pageSlot>

						</div>
					</div>
				</div>
			</div>
			<div class="row desktop__nav">
				<div class="nav__left col-xs-12 col-sm-6">
					<div class="row">
						<div class="col-sm-2 hidden-xs visible-sm mobile-menu">
							<button class="btn js-toggle-sm-navigation" type="button">
								<span class="glyphicon glyphicon-align-justify"></span>
							</button>
						</div>
<!-- 						<div class="col-sm-10"> -->
<!-- 							<div class="site-search"> -->
								<cms:pageSlot position="SearchBox" var="component">
									<cms:component component="${component}" element="div"/>
								</cms:pageSlot>
<!-- 							</div> -->
<!-- 						</div> -->
					</div>
				</div>
				<div class="nav__right col-xs-6 col-xs-6 hidden-xs">
					<ul class="nav__links nav__links--shop_info">
						<li>
							<c:if test="${empty hideHeaderLinks}">
								<ycommerce:testId code="header_StoreFinder_link">
									<div class="nav-location hidden-xs">
										<c:url value="/store-finder" var="storeFinderUrl"/>
										<a href="${fn:escapeXml(storeFinderUrl)}" class="btn">
											<span class="glyphicon glyphicon-map-marker"></span>
										</a>
									</div>
								</ycommerce:testId>
							</c:if>
						</li>
						<li>
							<cms:pageSlot position="MiniCart" var="cart" element="div" class="componentContainer">
								<cms:component component="${cart}" element="div"/>
							</cms:pageSlot>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</nav> --%>
	<a id="skiptonavigation"></a>
	<%-- <nav:topNavigation /> --%>
</header>

<cms:pageSlot position="BottomHeaderSlot" var="component">
	<cms:component component="${component}" />
</cms:pageSlot>

<div id="b2bUnitsModal"></div>