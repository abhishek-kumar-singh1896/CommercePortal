<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<c:set var="regionCodeUpper" scope="page"
	value="${fn:toUpperCase(regionCode)}" />
<c:set var="languageUpper" scope="page"
	value="${fn:toUpperCase(language)}" />


<cms:pageSlot position="ggB2CNavBar" var="feature">
	<c:if
		test="${not empty feature.navigationNode && feature.navigationNode ne null}">
		<c:forEach items="${feature.navigationNode.children}" var="l1"
			varStatus="children">
			<c:if
				test="${not empty l1.children && l1.children ne null && l1.uid ne 'ContactNavNode' && l1.uid ne 'SupportNavNode'}">
				<div class="megamenu-out second-level-menu shadow-sm d-none"
					id="mainNavLink${children.index+1}Container">
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
															</c:when>
															<c:otherwise>
																<c:if test="${l3link.index eq 0}">
																	<a class="nav-link left-tab active"
																		id="l3link${children.index+1}${l3link.index}-tab"
																		data-toggle="tab"
																		href="#l3link${children.index+1}${l3link.index}"
																		role="tab"
																		aria-controls="l3link${children.index+1}${l3link.index}"
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
																		id="l3link${children.index+1}${l3link.index}-tab"
																		data-toggle="tab"
																		href="#l3link${children.index+1}${l3link.index}"
																		role="tab"
																		aria-controls="l3link${children.index+1}${l3link.index}"
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
													<div class="tab-pane active"
														id="l3link${children.index+1}${l3link.index}"
														role="tabpanel"
														aria-labelledby="l3link${children.index+1}${l3link.index}-tab">
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
															<c:forEach items="${topLevelChild.children}" var="entry2">
																<c:forEach items="${entry2.children}" var="entry">
																	<div class="col-4">
																		<div class="container-col-title">${entry.title}
																		</div>
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
													<div class="tab-pane"
														id="l3link${children.index+1}${l3link.index}"
														role="tabpanel"
														aria-labelledby="l3link${children.index+1}${l3link.index}-tab">
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
															<c:forEach items="${topLevelChild.children}" var="entry2">
																<c:forEach items="${entry2.children}" var="entry">
																	<div class="col-4">
																		<div class="container-col-title">${entry.title}
																		</div>
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
			<c:if
				test="${l1.uid eq 'ContactNavNode' || l1.uid eq 'SupportNavNode'}">
				<div class="megamenu-out second-level-menu shadow-sm d-none"
					id="mainNavLink${children.index+1}Container">
					<div class="container">
						<div class="contact-us-container">
							<div class="row">
								<c:forEach items="${l1.children}" var="topLevelChild"
									varStatus="l3link">
									<c:set var="isCallNode" value="${fn:contains(topLevelChild.uid, 'CallUs')}"/>
									<c:if test="${isCallNode}">
										<div class="col-md-3 ${l1.uid}">
											<c:set var="showLink" scope="page" value="false" />
											<c:forEach items="${topLevelChild.entries}"
												var="topLevelLink1">
												<span class="d-none"> <cms:component
														component="${topLevelLink1.item}"
														evaluateRestriction="true" />
												</span>
												${topLevelLink1.item.linkName} 
											</c:forEach>
											<div class="contact-us-description">
												${topLevelChild.title}</div>
										</div>
									</c:if>
									<c:if test="${isCallNode eq false}">
										<div class="col-md-3">
											<div class="menu-container-title less-margin">

												<c:forEach items="${topLevelChild.entries}"
													var="topLevelLink1">
<%-- 													<c:if --%>
<%-- 														test="${topLevelLink1.item.uid == (regionCodeUpper).concat(languageUpper).concat('SupportMaterialLink')}"> --%>

<%-- 														<a href="${topLevelLink1.item.url}" target="_blank">${topLevelLink1.item.linkName}</a>  --%>
<%-- 														<cms:component component="${topLevelLink1.item}" --%>
<%-- 															evaluateRestriction="false" /> --%>
<%-- 													</c:if> --%>
<%-- 													<c:if --%>
<%-- 														test="${topLevelLink1.item.uid == (regionCodeUpper).concat(languageUpper).concat('TechnicalSupportLink')}"> --%>

<%-- 														<cms:component component="${topLevelLink1.item}" --%>
<%-- 															evaluateRestriction="false" /> --%>
<%-- 													</c:if> --%>
<%-- 													<c:if --%>
<%-- 														test="${topLevelLink1.item.uid == (regionCodeUpper).concat(languageUpper).concat('SoftwareDownloadsLink')}"> --%>

<%-- 														<cms:component component="${topLevelLink1.item}" --%>
<%-- 															evaluateRestriction="false" /> --%>
<%-- 													</c:if> --%>
<%-- 													<c:if --%>
<%-- 														test="${topLevelLink1.item.uid == (regionCodeUpper).concat(languageUpper).concat('FindStoreLink')}"> --%>

<%-- 														<cms:component component="${topLevelLink1.item}" --%>
<%-- 															evaluateRestriction="false" /> --%>
<%-- 													</c:if> --%>
<%-- 													<c:if --%>
<%-- 														test="${topLevelLink1.item.uid == (regionCodeUpper).concat(languageUpper).concat('FindManagerLink')}"> --%>

<%-- 														<cms:component component="${topLevelLink1.item}" --%>
<%-- 															evaluateRestriction="false" /> --%>
<%-- 													</c:if> --%>
<%-- 													<c:if --%>
<%-- 														test="${topLevelLink1.item.uid == (regionCodeUpper).concat(languageUpper).concat('SendMessageLink')}"> --%>

<%-- 														<cms:component component="${topLevelLink1.item}" --%>
<%-- 															evaluateRestriction="false" /> --%>
<%-- 													</c:if> --%>

													 <cms:component component="${topLevelLink1.item}" evaluateRestriction="true" />
													<span class="arrow-right-icon"> <svg>
                                    <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
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