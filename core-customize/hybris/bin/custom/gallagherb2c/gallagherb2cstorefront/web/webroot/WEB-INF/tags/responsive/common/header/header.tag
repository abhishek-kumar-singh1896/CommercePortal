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
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/responsive/common/header"%>


<spring:htmlEscape defaultHtmlEscape="true" />

<cms:pageSlot position="TopHeaderSlot" var="component" element="div">
	<cms:component component="${component}" />
</cms:pageSlot>

<!-- Modal responsive mega menu -->
<sec:authorize access="hasAnyRole('ROLE_ANONYMOUS')">
	<header:megaMenu />
	<header class="main-header-out shadow-sm">
		<div class="container">
			<div>
				<div class="header-left-section">
					<div class="logo-out gallagher-logo">
						<cms:pageSlot position="SiteLogo" var="logo" limit="1">
							<cms:component component="${logo}" element="div" />
						</cms:pageSlot>
					</div>
				</div>

				<cms:pageSlot position="ggB2CNavBar" var="feature">
					<c:if test="${feature.visible}">
						<c:if test="${not empty feature.navigationNode && feature.navigationNode ne null}">
							<div class="header-middle-section">
								<div class="main-nav-out">
									<ul>
										<c:forEach items="${feature.navigationNode.children}" var="l1" varStatus="status">
											<c:forEach items="${l1.entries}" var="dropdownValue">
												<li class="first-level with-dropdown">
													<c:choose>
													<c:when test="${not empty l1.children && l1.children ne null}">
					                                <a href="javascript:void(0)" id="mainNavLink${status.index+1}">
					                                    ${dropdownValue.item.linkName}
					                                    <span class="arrow-down-icon">
					                                        <svg>
					                                            <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-down" />
					                                        </svg>
					                                    </span>
					                                </a>
					                                </c:when>
										            <c:otherwise>
										            	<cms:component component="${dropdownValue.item}" evaluateRestriction="true" />
										            </c:otherwise>
										            </c:choose>
					                            </li>
				                            </c:forEach>
										</c:forEach>
									</ul>
								</div>
							</div>
						</c:if>
					</c:if>
				</cms:pageSlot>

				<div class="header-right-section text-right">

					<div class="search-out">
						<a href="javascript:void(0)" class="search-link" id="searchLink1"> <svg
								class="search-icon">
							<use
									xlink:href="${commonResourcePath}/images/gallagher-icons.svg#search" />
                        </svg> <span class="search-text"> <spring:theme
									code="search.placeholder" />
						</span> <span class="arrow-up"> <svg class="arrow-up-icon">
		                         <use
										xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-up" />
	                        </svg>
						</span>
						</a>
					</div>
					<!--  Add button and cart here-->
					<div class="header-right-btn-group">
						<div class="btn-group" role="group"
							aria-label="Button group with nested dropdown">
							<ycommerce:testId code="header_Register_link">
								<c:url value="/register" var="registerUrl" />
								<button type="button" class="btn left-btn"
									onclick="window.location.href = '${fn:escapeXml(registerUrl)}'">
									<spring:theme code="header.link.register" />
								</button>
							</ycommerce:testId>
							<ycommerce:testId code="header_Login_link">
								<c:url value="/login" var="loginUrl" />
								<button type="button" class="btn"
									onclick="window.location.href = '${fn:escapeXml(loginUrl)}'">
									<spring:theme code="header.link.login" />
								</button>
							</ycommerce:testId>


							<div class="btn-group" role="group">
								<button id="btnGroupDrop1" type="button"
									class="btn right-btn dropdown-toggle" data-toggle="dropdown"
									aria-haspopup="true" aria-expanded="false">
									<cms:pageSlot position="MiniCart" var="cart">
										<cms:component component="${cart}" />
									</cms:pageSlot>
								</button>
							
							<div class="dropdown-menu dropdown-menu-right mini-cart"
								aria-labelledby="btnGroupDrop1">
								
							</div>
							</div>
						</div>
					</div>
				</div>

				<!-- Ipad and Mobile Header Right Section-->
				<div class="xs-header-right-section">

					<!-- For Ipad Cart Section-->
					<div class="header-right-btn-group for-ipad-view">
						<div class="btn-group" role="group"
							aria-label="Button group with nested dropdown">
							<ycommerce:testId code="header_Register_link">
								<c:url value="/register" var="registerUrl" />
								<button type="button" class="btn left-btn"
									onclick="window.location.href = '${fn:escapeXml(registerUrl)}'">
									<spring:theme code="header.link.register" />
								</button>
							</ycommerce:testId>
							<ycommerce:testId code="header_Login_link">
								<c:url value="/login" var="loginUrl" />
								<button type="button" class="btn"
									onclick="window.location.href = '${fn:escapeXml(loginUrl)}'">
									<spring:theme code="header.link.login" />
								</button>
							</ycommerce:testId>

							<div class="btn-group" role="group">
								<button id="btnGroupDrop1" type="button"
									class="btn right-btn dropdown-toggle" data-toggle="dropdown"
									aria-haspopup="true" aria-expanded="false">
									<cms:pageSlot position="MiniCart" var="cart">
										<cms:component component="${cart}" />
									</cms:pageSlot>
								</button>
							

							<div class="dropdown-menu dropdown-menu-right mini-cart"
								aria-labelledby="btnGroupDrop1">
								
							</div>
							</div>
						</div>
					</div>

					<!-- For Mobile Cart Section-->
					<div class="header-right-btn-group for-mobile-view">
						<button id="btnGroupDrop1" type="button"
							class="btn right-btn dropdown-toggle" data-toggle="dropdown"
							aria-haspopup="true" aria-expanded="false">
							<cms:pageSlot position="MiniCart" var="cart">
								<cms:component component="${cart}" />
							</cms:pageSlot>
						</button>

						<div class="dropdown-menu dropdown-menu-right mini-cart"
							aria-labelledby="btnGroupDrop1">
							
						</div>
					</div>
					<div class="search-out">
						<a href="javascript:void(0)" class="search-link"> <svg
								class="search-icon">
						<use
									xlink:href="${commonResourcePath}/images/gallagher-icons.svg#search" />
                    </svg> <span class="arrow-up"> <svg
									class="arrow-up-icon">
                    		<use
										xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-up" />
                         </svg>
						</span>
						</a>
					</div>

					<div class="xs-menu-out">
                        <a href="javascript:void(0)" class="hamburger-icon">
                            <span class="line1"></span>
                            <span class="line2"></span>
                            <span class="line3"></span>
                        </a>
                    </div>
				</div>
			</div>
			<header:navigationNodes/>
			<div class="megamenu-out search-result-out search-link-container d-none" id="searchLink1Container">
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
</sec:authorize>

<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
	<header:megaMenu/>
	<header class="main-header-out shadow-sm with-logged-in-user">
		<div class="container">
			<div>
				<div class="header-left-section">
					<div class="logo-out gallagher-logo">
						<cms:pageSlot position="SiteLogo" var="logo" limit="1">
							<cms:component component="${logo}" element="div" />
						</cms:pageSlot>
					</div>
				</div>
				
				<cms:pageSlot position="ggB2CNavBar" var="feature">
					<c:if test="${feature.visible}">
						<c:if test="${not empty feature.navigationNode && feature.navigationNode ne null}">
							<div class="header-middle-section">
								<div class="main-nav-out">
									<ul>
										<c:forEach items="${feature.navigationNode.children}" var="l1" varStatus="status">
											<c:forEach items="${l1.entries}" var="dropdownValue">
												<li class="first-level with-dropdown">
													<c:choose>
													<c:when test="${not empty l1.children && l1.children ne null}">
					                                <a href="javascript:void(0)" id="mainNavLink${status.index+1}">
					                                    ${dropdownValue.item.linkName}
					                                    <span class="arrow-down-icon">
					                                        <svg>
					                                            <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-down" />
					                                        </svg>
					                                    </span>
					                                </a>
					                                </c:when>
										            <c:otherwise>
										            	<cms:component component="${dropdownValue.item}" evaluateRestriction="true" />
										            </c:otherwise>
										            </c:choose>
					                            </li>
				                            </c:forEach>
										</c:forEach>
									</ul>
								</div>
							</div>
						</c:if>
					</c:if>
				</cms:pageSlot>

				<div class="header-right-section text-right">

					<div class="search-out">
						<a href="javascript:void(0)" class="search-link" id="searchLink1"> <svg
								class="search-icon">
							<use
									xlink:href="${commonResourcePath}/images/gallagher-icons.svg#search" />
                        </svg> <span class="search-text"> <spring:theme
									code="search.placeholder" />
						</span> <span class="arrow-up"> <svg class="arrow-up-icon">
		                         <use
										xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-up" />
	                        </svg>
						</span>
						</a>
					</div>
					<!--  Add button and cart here-->
					<div class="header-right-btn-group">
						<div class="btn-group" role="group"
							aria-label="Button group with nested dropdown">
							<ycommerce:testId code="header_MyApp_link">
								<c:url value="/" var="myApp" />
								<button type="button" class="btn left-btn"
									onclick="window.location.href = '${fn:escapeXml(myApp)}'">
									<spring:theme code="header.link.app" />
								</button>
							</ycommerce:testId>
							<%-- <button type="button" class="btn">
								<span class="user-icon"> <svg>
                                        <use
											xlink:href="${commonResourcePath}/images/gallagher-icons.svg#user" />
                                    </svg>
								</span>
							</button> --%>
							<div class="btn-group" role="group">
                                <button type="button"
                                    class="btn dropdown-toggle user-profile-btn"
                                    id="userProfileDropdown" aria-haspopup="true"
                                    data-toggle="dropdown" aria-expanded="false">
                                    <span class="user-icon"> <svg>
                                        <use
                                                xlink:href="${commonResourcePath}/images/gallagher-icons.svg#user" />
                                    </svg>
                                    </span>
                                </button>
                                <div class="dropdown-menu user-profile-dropdown"
                                    aria-labelledby="userProfileDropdown">
                                    <div class="user-profile-dropdown-inner">
                                        <ul>
                                        	<li>
                                            <div class="row mb-1">
                                                <div class="col-12 text-truncate user-name">
                                                    <sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
                                                        <c:set var="maxNumberChars" value="25" />
                                                        <c:if test="${fn:length(user.firstName) gt maxNumberChars}">
                                                            <c:set target="${user}" property="firstName"
                                                                value="${fn:substring(user.firstName, 0, maxNumberChars)}..." />
                                                        </c:if>
	                                                        <ycommerce:testId code="header_LoggedUser">
				                                              Hi,&nbsp${user.firstName}&nbsp${user.lastName}.
				                                    		</ycommerce:testId>
                                                    </sec:authorize>
                                                </div>
                                            </div>
                                            </li>
                                            <li><c:url value="/register-product" var="registerProductUrl" /><a href="${registerProductUrl}"><spring:theme code="text.Register.Product" />
                                            </a></li>
                                            <li><a href="javascript:void(0)"> <spring:theme code="text.Registered.Products" />
                                            </a></li>
                                            <li><a href="javascript:void(0)"> <spring:theme code="text.Account.Management" /> </a></li>
                                            <li>
                                        <c:url value="/logout" var="logoutUrl"/>
                                        <a href="${(logoutUrl)}">
                                            <spring:theme code="header.link.logout" />
                                        </a>
                                    </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
							<div class="btn-group" role="group">
								<button id="btnGroupDrop1" type="button"
									class="btn right-btn dropdown-toggle" data-toggle="dropdown"
									aria-haspopup="true" aria-expanded="false">
									<cms:pageSlot position="MiniCart" var="cart">
										<cms:component component="${cart}" />
									</cms:pageSlot>
								</button>
							
							<div class="dropdown-menu dropdown-menu-right mini-cart"
								aria-labelledby="btnGroupDrop1">
								
							</div>
							</div>
						</div>
					</div>
				</div>

				<!-- Ipad and Mobile Header Right Section-->
				<div class="xs-header-right-section">

					<!-- For Ipad Cart Section-->
					<div class="header-right-btn-group for-ipad-view">
						<div class="btn-group" role="group"
							aria-label="Button group with nested dropdown">

							<c:url value="/" var="myAppUrl" />
							<ycommerce:testId code="header_MyApp_link">
								<c:url value="/" var="myApp" />
								<button type="button" class="btn left-btn"
									onclick="window.location.href = '${fn:escapeXml(myApp)}'">
									<spring:theme code="header.link.app" />
								</button>
							</ycommerce:testId>
					<!-- <button type="button" class="btn"> -->	
					<div class="btn-group" role="group">	
                             <button type="button" class="btn btn-highlight dropdown-toggle user-profile-btn"
                                    id="userProfileDropdown" data-toggle="dropdown" aria-haspopup="true"
                                    aria-expanded="false">
								<span class="user-icon"> <svg>
                        	    	<use
											xlink:href="${commonResourcePath}/images/gallagher-icons.svg#user" />
                                </svg>
								</span>
							</button>
							<div class="dropdown-menu user-profile-dropdown" aria-labelledby="userProfileDropdown">

                                    <div class="user-profile-dropdown-inner">
                                        <ul>
                                        	<li>
                                            <div class="row">
                                                <div class="col-12 text-truncate user-name">
                                                    <sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
                                                        <c:set var="maxNumberChars" value="25" />
                                                        <c:if test="${fn:length(user.firstName) gt maxNumberChars}">
                                                            <c:set target="${user}" property="firstName"
                                                                value="${fn:substring(user.firstName, 0, maxNumberChars)}..." />
                                                        </c:if>
	                                                        <ycommerce:testId code="header_LoggedUser">
				                                              Hi,&nbsp${user.firstName}&nbsp${user.lastName}.
				                                    		</ycommerce:testId>
                                                    </sec:authorize>
                                                </div>
                                            </div>
                                            </li>
                                            <li><c:url value="/register-product" var="registerProductUrl" /><a href="${registerProductUrl}"> <spring:theme code="text.Register.Product" />
                                            </a></li>
                                            <li><a href="javascript:void(0)"> <spring:theme code="text.Registered.Products" />
                                            </a></li>
                                            <li><a href="javascript:void(0)"> <spring:theme code="text.Account.Management" /> </a></li>
                                            <li><c:url value="/logout" var="logoutUrl" /> <a
                                                href="${logoutUrl}"> <spring:theme
                                                        code="header.link.logout" />
                                            </a></li>
                                        </ul>
                                    </div>
                                </div>
                                </div>
                                <div class="btn-group" role="group">
								<button id="btnGroupDrop1" type="button"
									class="btn right-btn dropdown-toggle" data-toggle="dropdown"
									aria-haspopup="true" aria-expanded="false">
									<cms:pageSlot position="MiniCart" var="cart">
										<cms:component component="${cart}" />
									</cms:pageSlot>
								</button>
							

							<div class="dropdown-menu dropdown-menu-right mini-cart"
								aria-labelledby="btnGroupDrop1">
								
							</div>
							</div>
						</div>
					</div>

					<!-- For Mobile Cart Section-->
					<div class="header-right-btn-group for-mobile-view">
						<button id="btnGroupDrop1" type="button"
							class="btn right-btn dropdown-toggle" data-toggle="dropdown"
							aria-haspopup="true" aria-expanded="false">
							<cms:pageSlot position="MiniCart" var="cart">
								<cms:component component="${cart}" />
							</cms:pageSlot>
						</button>

						<div class="dropdown-menu dropdown-menu-right mini-cart"
							aria-labelledby="btnGroupDrop1">
							
						</div>
					</div>
					<div class="search-out">
						<a href="javascript:void(0)" class="search-link"> <svg
								class="search-icon">
						<use
									xlink:href="${commonResourcePath}/images/gallagher-icons.svg#search" />
                    </svg> <span class="arrow-up"> <svg
									class="arrow-up-icon">
                    		<use
										xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-up" />
                         </svg>
						</span>
						</a>
					</div>

					<div class="xs-menu-out">
                        <a href="javascript:void(0)" class="hamburger-icon">
                            <span class="line1"></span>
                            <span class="line2"></span>
                            <span class="line3"></span>
                        </a>
                    </div>
				</div>
			</div>
			<header:navigationNodes/>
			
			<div class="megamenu-out search-result-out search-link-container d-none" id="searchLink1Container">
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
</sec:authorize>

<cms:pageSlot position="BottomHeaderSlot" var="component" element="div"
	class="container-fluid">
	<cms:component component="${component}" />
</cms:pageSlot>
