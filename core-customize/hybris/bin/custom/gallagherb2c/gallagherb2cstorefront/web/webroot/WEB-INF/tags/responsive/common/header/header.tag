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

<sec:authorize access="hasAnyRole('ROLE_ANONYMOUS')">
	<header class="main-header-out shadow-sm fixed-top">
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
					<c:if test="${not empty feature.components && feature.components ne null}">
						<div class="header-middle-section">
							<div class="main-nav-out">
								<ul>
									<c:forEach items="${feature.components}" var="l1" varStatus="status">
									<li class="first-level with-dropdown">
		                                <a href="javascript:void(0)" id="mainNavLink${status.index+1}">
		                                    ${l1.navigationNode.title}
		                                    <span class="arrow-down-icon">
		                                        <svg>
		                                            <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-down" />
		                                        </svg>
		                                    </span>
		                                </a>
		                            </li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
				</cms:pageSlot>

				<div class="header-right-section text-right">

					<div class="search-out">
						<a href="javascript:void(0)" class="search-link"> <svg
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
								<c:url value="/login" var="registerUrl" />
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
								<c:url value="/login" var="registerUrl" />
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
						<a href="javascript:void(0)" class="hamburger-icon"> <span
							class="line1"> </span> <span class="line2"> </span> <span
							class="line3"> </span>
						</a>
					</div>
				</div>
			</div>
			<cms:pageSlot position="ggB2CNavBar" var="feature">
			<c:if test="${not empty feature.components && feature.components ne null}">
			<div class="second-level-menu shadow-sm d-none" id="mainNavContainer1">
                <div class="container">
                    <div class="third-level-menu">
                        <div class="row align-items-stretch">
                            <div class="col-lg-3 left-menu-container">
                                <div class="left-menu-inner">
                                    <ul class="nav nav-tabs" id="solutionTab" role="tablist">
                                    <c:forEach items="${feature.components}" var="l1" varStatus="status">
                                    
                                    <c:forEach items="${l1.navigationNode.children}" var="topLevelChild">
					                	<li class="nav-item">
					                	<c:choose>
					                		<c:when test="${not empty topLevelChild.links && topLevelChild.links ne null}">
                                                <c:forEach items="${topLevelChild.links}" var="topLevelChild1">
                                                <cms:component component="${topLevelChild1}" evaluateRestriction="true" styleClass="test" />
								                </c:forEach>
								             </c:when>
								             <c:otherwise>
                                            <a class="nav-link" id="electircFencing-tab" data-toggle="tab"
                                                href="#electircFencing" role="tab" aria-controls="electircFencing"
                                                aria-selected="true">
                                                ${topLevelChild.title}
                                                <span class="arrow-right-icon">
                                                    <svg>
                                                        <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
                                                    </svg>
                                                </span>
                                            </a>
                                            </c:otherwise>
                                           </c:choose>
                                        </li>
					                </c:forEach>
					            	</c:forEach>
                                    </ul>
                                </div>
                            </div>
                            <div class="col-lg-9">
                                <div class="right-menu-container">

                                    <div class="tab-content">
                                    	<c:forEach items="${feature.components}" var="l1" varStatus="status">
                                        <div class="tab-pane active" id="electircFencing" role="tabpanel"
                                            aria-labelledby="electircFencing-tab">

                                            <div class="menu-container-title">
                                                <%-- <a href="javascript:void(0)">
                                                    <!-- All Electric fencing -->
                                                    <span class="arrow-right-icon">
                                                        <svg>
                                                            <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
                                                        </svg>
                                                    </span>
                                                </a> --%>
                                            </div>

                                            <div class="row">
                                            
	                                    		<c:forEach items="${l1.navigationNode.children}" var="topLevelChild">
						                			<c:forEach items="${topLevelChild.children}" var="entry">
		                                                <div class="col-4">
		                                                    <div class="container-col-title">
		                                                        ${entry.title}
		                                                    </div>
		                                                    <div class="container-col-links">
		                                                        <ul>
		                                                        <c:forEach items="${entry.links}" var="topLevelLink1">
		                                                            <li>
		                                                                <%-- <a href="javascript:void(0)">
		                                                                    ${topLevelLink1.linkName}
		                                                                </a> --%>
		                                                                <cms:component component="${topLevelLink1}" evaluateRestriction="true" />
		                                                            </li>
		                                                         </c:forEach>
		                                                        </ul>
		                                                    </div>
		                                                </div>
	                                                </c:forEach>
	                                             </c:forEach>
                                              </div>
                                        </div>
                                        </c:forEach>
                                        <div class="tab-pane" id="weighingEID" role="tabpanel"
                                            aria-labelledby="weighingEID-tab">2</div>
                                        <div class="tab-pane" id="wirelessWaterMonitoring" role="tabpanel"
                                            aria-labelledby="wirelessWaterMonitoring-tab">3</div>
                                        <div class="tab-pane" id="steelGatesHardware" role="tabpanel"
                                            aria-labelledby="steelGatesHardware-tab">4</div>
                                        <div class="tab-pane" id="farmInformationTools" role="tabpanel"
                                            aria-labelledby="farmInformationTools-tab">5</div>


                                    </div>

                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            </c:if>
			</cms:pageSlot>
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
</sec:authorize>

<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
	<header class="main-header-out shadow-sm with-logged-in-user fixed-top">
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
											evaluateRestriction="true" element="li">
											<span class="avtive-arrow d-none"> <svg
													class="arrow-up-icon">
                                            <use
														xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-up" />
                                        	</svg>
											</span>

											<span class="hover-arrow d-none"> <svg
													class="arrow-down-icon">
                                            <use
														xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-down" />
                                        </svg>
											</span>
										</cms:component>
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
							<button type="button" class="btn">
								<span class="user-icon"> <svg>
                                        <use
											xlink:href="${commonResourcePath}/images/gallagher-icons.svg#user" />
                                    </svg>
								</span>
							</button>
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
							<button type="button" class="btn">
								<span class="user-icon"> <svg>
                        	    	<use
											xlink:href="${commonResourcePath}/images/gallagher-icons.svg#user" />
                                </svg>
								</span>
							</button>
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
						<a href="javascript:void(0)" class="hamburger-icon"> <span
							class="line1"> </span> <span class="line2"> </span> <span
							class="line3"> </span>
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
</sec:authorize>

<cms:pageSlot position="BottomHeaderSlot" var="component" element="div"
	class="container-fluid">
	<cms:component component="${component}" />
</cms:pageSlot>
