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

<cms:pageSlot position="TopHeaderSlot" var="component" element="div"
	limit="1">
	<cms:component component="${component}" />
</cms:pageSlot>

<sec:authorize access="hasAnyRole('ROLE_ANONYMOUS')">
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
						<c:if
							test="${not empty feature.navigationNode && feature.navigationNode ne null}">
							<div class="header-middle-section">
								<div class="main-nav-out">
									<ul>
										<c:forEach items="${feature.navigationNode.children}" var="l1"
											varStatus="status">
											<c:forEach items="${l1.entries}" var="dropdownValue">
												<li class="first-level with-dropdown"><c:choose>
														<c:when
															test="${not empty l1.children && l1.children ne null}">
															<a href="javascript:void(0)"
																id="mainNavLink${status.index+1}">
																${dropdownValue.item.linkName} <span
																class="arrow-down-icon"> <svg>
					                                            <use
																			xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-down" />
					                                        </svg>
															</span>
															</a>
														</c:when>
														<c:otherwise>
															<cms:component component="${dropdownValue.item}"
																evaluateRestriction="true" />
														</c:otherwise>
													</c:choose></li>
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
									aria-labelledby="btnGroupDrop1"></div>
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
									aria-labelledby="btnGroupDrop1"></div>
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
							aria-labelledby="btnGroupDrop1"></div>
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
				<c:if
					test="${not empty feature.navigationNode && feature.navigationNode ne null}">
					<c:forEach items="${feature.navigationNode.children}" var="l1"
						varStatus="children">
						<c:if
							test="${not empty l1.children && l1.children ne null && l1.uid ne 'ContactNavNode'}">
							<div class="megamenu-out second-level-menu shadow-sm d-none"
								id="mainNavContainer${children.index+1}">
								<div class="container">
									<div class="third-level-menu">
										<div class="row align-items-stretch">
											<div class="col-lg-3 left-menu-container">
												<div class="left-menu-inner">
													<ul class="nav nav-tabs" id="solutionTab" role="tablist">
														<%-- <c:forEach items="${feature.navigationNode.children}" var="l11" varStatus="status"> --%>
														<c:forEach items="${l1.children}" var="topLevelChild"
															varStatus="l3link">
															<c:forEach items="${topLevelChild.entries}"
																var="dropdownValue">
																<li class="nav-item"><c:choose>
																		<c:when
																			test="${not empty dropdownValue.item && dropdownValue.item ne null && (empty topLevelChild.children || topLevelChild.children eq null)}">
																			<cms:component component="${dropdownValue.item}"
																				evaluateRestriction="true" />
																			<%-- <a class="nav-link gray-link" href="${dropdownValue.item.url}" title="${dropdownValue.item.linkName}">${dropdownValue.item.linkName}</a> --%>
																		</c:when>
																		<c:otherwise>
																			<c:if test="${l3link.index eq 0}">
																				<a class="nav-link left-tab active"
																					id="l3link${l3link.index}-tab" data-toggle="tab"
																					href="#l3link${l3link.index}" role="tab"
																					aria-controls="l3link${l3link.index}"
																					aria-selected="true"> ${topLevelChild.title} <span
																					class="arrow-right-icon"> <svg>
	                                                        <use
																								xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
	                                                    </svg>
																				</span>
																				</a>
																			</c:if>
																			<c:if test="${l3link.index ne 0}">
																				<a class="nav-link left-tab"
																					id="l3link${l3link.index}-tab" data-toggle="tab"
																					href="#l3link${l3link.index}" role="tab"
																					aria-controls="l3link${l3link.index}"
																					aria-selected="true"> ${topLevelChild.title} <span
																					class="arrow-right-icon"> <svg>
	                                                        <use
																								xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
	                                                    </svg>
																				</span>
																				</a>
																			</c:if>
																		</c:otherwise>
																	</c:choose></li>
															</c:forEach>
															<%-- </c:forEach> --%>
														</c:forEach>
													</ul>
												</div>
											</div>
											<div class="col-lg-9">
												<div class="right-menu-container">
													<div class="tab-content">
														<%-- <c:forEach items="${feature.components}" var="l1" varStatus="status"> --%>
														<c:forEach items="${l1.children}" var="topLevelChild"
															varStatus="l3link">
															<c:if test="${l3link.index eq 0}">
																<div class="tab-pane active" id="l3link${l3link.index}"
																	role="tabpanel"
																	aria-labelledby="l3link${l3link.index}-tab">
																	<c:if
																		test="${not empty topLevelChild.entries && topLevelChild.entries ne null && not empty topLevelChild.children && topLevelChild.children ne null}">
																		<%-- <c:forEach items="${topLevelChild.links}" var="topLink1"> --%>
																		<div class="menu-container-title">
																			<c:forEach items="${topLevelChild.entries}"
																				var="dropdownValue">
																				<cms:component component="${dropdownValue.item}"
																					evaluateRestriction="true" />
																			</c:forEach>
																			<span class="arrow-right-icon"> <svg>
                                                        <use
																						xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
                                                    </svg>
																			</span>
																		</div>
																		<%-- </c:forEach> --%>
																	</c:if>
																	<div class="row">
																		<c:forEach items="${topLevelChild.children}"
																			var="entry2">
																			<c:forEach items="${entry2.children}" var="entry">
																				<div class="col-4">
																					<div class="container-col-title">
																						${entry.title}</div>
																					<div class="container-col-links">
																						<ul>
																							<c:forEach items="${entry.entries}"
																								var="topLevelLink1">
																								<li><cms:component
																										component="${topLevelLink1.item}"
																										evaluateRestriction="true" /></li>
																							</c:forEach>
																						</ul>
																					</div>
																				</div>
																			</c:forEach>
																		</c:forEach>
																	</div>
																</div>
															</c:if>
															<c:if test="${l3link.index ne 0}">
																<div class="tab-pane" id="l3link${l3link.index}"
																	role="tabpanel"
																	aria-labelledby="l3link${l3link.index}-tab">
																	<c:if
																		test="${not empty topLevelChild.entries && topLevelChild.entries ne null && not empty topLevelChild.children && topLevelChild.children ne null}">
																		<%-- <c:forEach items="${topLevelChild.links}" var="topLink1"> --%>
																		<div class="menu-container-title">
																			<c:forEach items="${topLevelChild.entries}"
																				var="dropdownValue">
																				<cms:component component="${dropdownValue.item}"
																					evaluateRestriction="true" />
																			</c:forEach>
																			<span class="arrow-right-icon"> <svg>
                                                        <use
																						xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
                                                    </svg>
																			</span>
																		</div>
																		<%-- </c:forEach> --%>
																	</c:if>
																	<div class="row">
																		<c:forEach items="${topLevelChild.children}"
																			var="entry2">
																			<c:forEach items="${entry2.children}" var="entry">
																				<div class="col-4">
																					<div class="container-col-title">
																						${entry.title}</div>
																					<div class="container-col-links">
																						<ul>
																							<c:forEach items="${entry.entries}"
																								var="topLevelLink1">
																								<li><cms:component
																										component="${topLevelLink1.item}"
																										evaluateRestriction="true" /></li>
																							</c:forEach>
																						</ul>
																					</div>
																				</div>
																			</c:forEach>
																		</c:forEach>
																	</div>
																</div>
															</c:if>
														</c:forEach>
													</div>

												</div>
											</div>
										</div>

									</div>
								</div>
							</div>
						</c:if>
						<c:if test="${l1.uid eq 'ContactNavNode'}">
							<div class="megamenu-out second-level-menu shadow-sm d-none"
								id="mainNavContainer${children.index+1}">
								<div class="container">
									<div class="contact-us-container">
										<div class="row">
											<c:forEach items="${l1.children}" var="topLevelChild"
												varStatus="l3link">
												<c:if test="${l3link.index eq 0}">
													<div class="col-md-3">
														<c:forEach items="${topLevelChild.entries}"
															var="topLevelLink1">
                               	${topLevelLink1.item.linkName}
                                       <%-- <cms:component component="${topLevelLink1.item}" evaluateRestriction="true" /> --%>
														</c:forEach>
														<div class="contact-us-description">
															${topLevelChild.title}</div>
													</div>
												</c:if>
												<c:if test="${l3link.index ne 0}">
													<div class="col-md-3">
														<div class="menu-container-title less-margin">
															<c:forEach items="${topLevelChild.entries}"
																var="topLevelLink1">
																<cms:component component="${topLevelLink1.item}"
																	evaluateRestriction="true" />
																<span class="arrow-right-icon"> <svg>
                                            <use
																			xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
                                        </svg>
																</span>
															</c:forEach>
														</div>
														<div class="contact-us-description">
															${topLevelChild.title}</div>
													</div>
												</c:if>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
						</c:if>
					</c:forEach>
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

				<cms:pageSlot position="ggB2CNavBar" var="feature">
					<c:if test="${feature.visible}">
						<c:if
							test="${not empty feature.navigationNode && feature.navigationNode ne null}">
							<div class="header-middle-section">
								<div class="main-nav-out">
									<ul>
										<c:forEach items="${feature.navigationNode.children}" var="l1"
											varStatus="status">
											<c:forEach items="${l1.entries}" var="dropdownValue">
												<li class="first-level with-dropdown"><c:choose>
														<c:when
															test="${not empty l1.children && l1.children ne null}">
															<a href="javascript:void(0)"
																id="mainNavLink${status.index+1}">
																${dropdownValue.item.linkName} <span
																class="arrow-down-icon"> <svg>
					                                            <use
																			xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-down" />
					                                        </svg>
															</span>
															</a>
														</c:when>
														<c:otherwise>
															<cms:component component="${dropdownValue.item}"
																evaluateRestriction="true" />
														</c:otherwise>
													</c:choose></li>
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

											<div class="row">
												<div class="col-12 text-truncate user-name">

													<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
														<c:set var="maxNumberChars" value="25" />
														<c:if
															test="${fn:length(user.firstName) gt maxNumberChars}">
															<c:set target="${user}" property="firstName"
																value="${fn:substring(user.firstName, 0, maxNumberChars)}..." />
														</c:if>

														<li class="logged_in js-logged_in"><ycommerce:testId
																code="header_LoggedUser">
										${user.firstName} &nbsp ${user.lastName}
									</ycommerce:testId></li>
													</sec:authorize>
												</div>
											</div>

											<li><a href="javascript:void(0)"> Register a Product
											</a></li>
											<li><a href="javascript:void(0)"> Edit Profile </a></li>
											<li><c:url value="/logout" var="logoutUrl" /> <a
												href="logoutUrl"> <spring:theme
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
									aria-labelledby="btnGroupDrop1"></div>
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
									aria-labelledby="btnGroupDrop1"></div>
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
							aria-labelledby="btnGroupDrop1"></div>
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
				<c:if
					test="${not empty feature.navigationNode && feature.navigationNode ne null}">
					<c:forEach items="${feature.navigationNode.children}" var="l1"
						varStatus="children">
						<c:if
							test="${not empty l1.children && l1.children ne null && l1.uid ne 'ContactNavNode'}">
							<div class="megamenu-out second-level-menu shadow-sm d-none"
								id="mainNavContainer${children.index+1}">
								<div class="container">
									<div class="third-level-menu">
										<div class="row align-items-stretch">
											<div class="col-lg-3 left-menu-container">
												<div class="left-menu-inner">
													<ul class="nav nav-tabs" id="solutionTab" role="tablist">
														<%-- <c:forEach items="${feature.navigationNode.children}" var="l11" varStatus="status"> --%>
														<c:forEach items="${l1.children}" var="topLevelChild"
															varStatus="l3link">
															<c:forEach items="${topLevelChild.entries}"
																var="dropdownValue">
																<li class="nav-item"><c:choose>
																		<c:when
																			test="${not empty dropdownValue.item && dropdownValue.item ne null && (empty topLevelChild.children || topLevelChild.children eq null)}">
																			<cms:component component="${dropdownValue.item}"
																				evaluateRestriction="true" />
																			<%-- <a class="nav-link gray-link" href="${dropdownValue.item.url}" title="${dropdownValue.item.linkName}">${dropdownValue.item.linkName}</a> --%>
																		</c:when>
																		<c:otherwise>
																			<c:if test="${l3link.index eq 0}">
																				<a class="nav-link left-tab active"
																					id="l3link${l3link.index}-tab" data-toggle="tab"
																					href="#l3link${l3link.index}" role="tab"
																					aria-controls="l3link${l3link.index}"
																					aria-selected="true"> ${topLevelChild.title} <span
																					class="arrow-right-icon"> <svg>
	                                                        <use
																								xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
	                                                    </svg>
																				</span>
																				</a>
																			</c:if>
																			<c:if test="${l3link.index ne 0}">
																				<a class="nav-link left-tab"
																					id="l3link${l3link.index}-tab" data-toggle="tab"
																					href="#l3link${l3link.index}" role="tab"
																					aria-controls="l3link${l3link.index}"
																					aria-selected="true"> ${topLevelChild.title} <span
																					class="arrow-right-icon"> <svg>
	                                                        <use
																								xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
	                                                    </svg>
																				</span>
																				</a>
																			</c:if>
																		</c:otherwise>
																	</c:choose></li>
															</c:forEach>
															<%-- </c:forEach> --%>
														</c:forEach>
													</ul>
												</div>
											</div>
											<div class="col-lg-9">
												<div class="right-menu-container">
													<div class="tab-content">
														<%-- <c:forEach items="${feature.components}" var="l1" varStatus="status"> --%>
														<c:forEach items="${l1.children}" var="topLevelChild"
															varStatus="l3link">
															<c:if test="${l3link.index eq 0}">
																<div class="tab-pane active" id="l3link${l3link.index}"
																	role="tabpanel"
																	aria-labelledby="l3link${l3link.index}-tab">
																	<c:if
																		test="${not empty topLevelChild.entries && topLevelChild.entries ne null && not empty topLevelChild.children && topLevelChild.children ne null}">
																		<%-- <c:forEach items="${topLevelChild.links}" var="topLink1"> --%>
																		<div class="menu-container-title">
																			<c:forEach items="${topLevelChild.entries}"
																				var="dropdownValue">
																				<cms:component component="${dropdownValue.item}"
																					evaluateRestriction="true" />
																			</c:forEach>
																			<span class="arrow-right-icon"> <svg>
                                                        <use
																						xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
                                                    </svg>
																			</span>
																		</div>
																		<%-- </c:forEach> --%>
																	</c:if>
																	<div class="row">
																		<c:forEach items="${topLevelChild.children}"
																			var="entry2">
																			<c:forEach items="${entry2.children}" var="entry">
																				<div class="col-4">
																					<div class="container-col-title">
																						${entry.title}</div>
																					<div class="container-col-links">
																						<ul>
																							<c:forEach items="${entry.entries}"
																								var="topLevelLink1">
																								<li><cms:component
																										component="${topLevelLink1.item}"
																										evaluateRestriction="true" /></li>
																							</c:forEach>
																						</ul>
																					</div>
																				</div>
																			</c:forEach>
																		</c:forEach>
																	</div>
																</div>
															</c:if>
															<c:if test="${l3link.index ne 0}">
																<div class="tab-pane" id="l3link${l3link.index}"
																	role="tabpanel"
																	aria-labelledby="l3link${l3link.index}-tab">
																	<c:if
																		test="${not empty topLevelChild.entries && topLevelChild.entries ne null && not empty topLevelChild.children && topLevelChild.children ne null}">
																		<%-- <c:forEach items="${topLevelChild.links}" var="topLink1"> --%>
																		<div class="menu-container-title">
																			<c:forEach items="${topLevelChild.entries}"
																				var="dropdownValue">
																				<cms:component component="${dropdownValue.item}"
																					evaluateRestriction="true" />
																			</c:forEach>
																			<span class="arrow-right-icon"> <svg>
                                                        <use
																						xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
                                                    </svg>
																			</span>
																		</div>
																		<%-- </c:forEach> --%>
																	</c:if>
																	<div class="row">
																		<c:forEach items="${topLevelChild.children}"
																			var="entry2">
																			<c:forEach items="${entry2.children}" var="entry">
																				<div class="col-4">
																					<div class="container-col-title">
																						${entry.title}</div>
																					<div class="container-col-links">
																						<ul>
																							<c:forEach items="${entry.entries}"
																								var="topLevelLink1">
																								<li><cms:component
																										component="${topLevelLink1.item}"
																										evaluateRestriction="true" /></li>
																							</c:forEach>
																						</ul>
																					</div>
																				</div>
																			</c:forEach>
																		</c:forEach>
																	</div>
																</div>
															</c:if>
														</c:forEach>
													</div>

												</div>
											</div>
										</div>

									</div>
								</div>
							</div>
						</c:if>
						<c:if test="${l1.uid eq 'ContactNavNode'}">
							<div class="megamenu-out second-level-menu shadow-sm d-none"
								id="mainNavContainer${children.index+1}">
								<div class="container">
									<div class="contact-us-container">
										<div class="row">
											<c:forEach items="${l1.children}" var="topLevelChild"
												varStatus="l3link">
												<c:if test="${l3link.index eq 0}">
													<div class="col-md-3">
														<c:forEach items="${topLevelChild.entries}"
															var="topLevelLink1">
                               	${topLevelLink1.item.linkName}
                                       <%-- <cms:component component="${topLevelLink1.item}" evaluateRestriction="true" /> --%>
														</c:forEach>
														<div class="contact-us-description">
															${topLevelChild.title}</div>
													</div>
												</c:if>
												<c:if test="${l3link.index ne 0}">
													<div class="col-md-3">
														<div class="menu-container-title less-margin">
															<c:forEach items="${topLevelChild.entries}"
																var="topLevelLink1">
																<cms:component component="${topLevelLink1.item}"
																	evaluateRestriction="true" />
																<span class="arrow-right-icon"> <svg>
                                            <use
																			xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
                                        </svg>
																</span>
															</c:forEach>
														</div>
														<div class="contact-us-description">
															${topLevelChild.title}</div>
													</div>
												</c:if>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
						</c:if>
					</c:forEach>
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

<cms:pageSlot position="BottomHeaderSlot" var="component" element="div"
	class="container-fluid">
	<cms:component component="${component}" />
</cms:pageSlot>
