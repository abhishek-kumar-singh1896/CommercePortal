<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<cms:pageSlot position="ggB2CNavBar" var="feature">
<c:if test="${feature.visible && not empty feature.navigationNode && feature.navigationNode ne null}">
<div class="modal-updated responsive-mega-menu-modal hidden-lg hidden-md" id="responsiveMegaMenu" tabindex="-1"
        role="dialog" aria-labelledby="responsiveMegaMenuTitle" aria-hidden="true">
        <div class="modal-dialog modal-dialog-scrollable" role="document">
            <div class="modal-content xs-main-menu-l1 xs-mega-menu">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">
                            Close
                            <span class="cross-icon">
                                &times;
                            </span>
                        </span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="xs-main-menu-l1-links menu-listing">
                   		<ul>
                    		<c:forEach items="${feature.navigationNode.children}" var="l1" varStatus="status">
							<c:forEach items="${l1.entries}" var="dropdownValue">
							<c:choose>
								<c:when test="${not empty l1.children && l1.children ne null}">
									<li>
		                              <a href="javascript:void(0)" class="l1-anchor with-sublevel" id="xsLevel1Link${status.index+1}">
		                                  ${dropdownValue.item.linkName}
		                                  <span class="right-arrow-icon">
		                                   <svg>
		                                       <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
		                                   </svg>
		                       			   </span>
		                              </a>
	                              	</li>
	                            </c:when>
					            <c:otherwise>
					            	<li>
					            		<cms:component component="${dropdownValue.item}" evaluateRestriction="true" />
					            	</li>
					            </c:otherwise>
				            </c:choose>
                         	</c:forEach>
							</c:forEach>
                  		</ul>
                    </div>
                </div>
                <sec:authorize access="hasAnyRole('ROLE_ANONYMOUS')">
                <div class="modal-footer">
                    <div class="xs-main-menu-l1-footer">
                        <div class="btn-group btn-block mb-3" role="group" aria-label="Button group login register">
                        	<ycommerce:testId code="header_Register_link">
								<c:url value="/register" var="registerUrl" />
								<button type="button" class="btn btn-secondary left-btn"
									onclick="window.location.href = '${fn:escapeXml(registerUrl)}'">
									<spring:theme code="header.link.register" />
								</button>
							</ycommerce:testId>
							<ycommerce:testId code="header_Login_link">
								<c:url value="/login" var="loginUrl" />
								<button type="button" class="btn btn-secondary"
									onclick="window.location.href = '${fn:escapeXml(loginUrl)}'">
									<spring:theme code="header.link.login" />
								</button>
							</ycommerce:testId>
                        </div>

                        <div class="l1-footer-bottom-links menu-listing">
                            <ul>
                                <li><a href="javascript:void(0)">Find a dealer</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                </sec:authorize>
                <sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
                <div class="modal-footer">
                    <div class="xs-main-menu-l1-footer with-logged-in-user">
                        <div class="btn-group btn-block mb-3" role="group" aria-label="Button group login register">
                            <button type="button" class="btn btn-highlight left-btn">My App</button>

                            <div class="btn-group user-profile-btn-group" role="group">
                                <button type="button" class="btn btn-highlight dropdown-toggle user-profile-btn"
                                    id="userProfileDropdown" data-toggle="dropdown" aria-haspopup="true"
                                    aria-expanded="false">
                                    <span class="user-icon">
                                        <svg>
                                            <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#user" />
                                        </svg>
                                    </span>
                                </button>
                                <div class="dropdown-menu user-profile-dropdown" aria-labelledby="userProfileDropdown">

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
                                            <li><a href="javascript:void(0)"> <spring:theme code="text.Register.Product" />
                                            </a></li>
                                            <li><a href="javascript:void(0)"> <spring:theme code="text.Registered.Products" />
                                            </a></li>
                                            <li><a href="javascript:void(0)"> <spring:theme code="text.Account.Management" /> </a></li>
                                            <li><c:url value="/logout" var="logoutUrl" /> <a
                                                href="logoutUrl"> <spring:theme
                                                        code="header.link.logout" />
                                            </a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </sec:authorize>
            </div>
			<c:forEach items="${feature.navigationNode.children}" var="l1" varStatus="children">
			<c:if test="${not empty l1.children && l1.children ne null && l1.uid ne 'ContactNavNode'}">
            <div class="modal-content xs-main-menu-l2 xs-mega-menu d-none" id="xsLevel1Link${children.index+1}Container">
                <div class="modal-header">
                    <div class="left-title-out">
                        <a href="javascript:void(0)" class="back-to-l1">
                            <span>
                                <svg>
                                    <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-left" />
                                </svg>
                            </span>

                            <span class="back-text"></span>
                        </a>
                    </div>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">
                            Close
                            <span class="cross-icon">
                                &times;
                            </span>
                        </span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="xs-main-menu-l2-links menu-listing">
                        <ul>
                        <c:forEach items="${l1.children}" var="topLevelChild" varStatus="l3link">
                        <c:forEach items="${topLevelChild.entries}" var="dropdownValue">
                        <c:choose>
                		<c:when test="${not empty dropdownValue.item && dropdownValue.item ne null && (empty topLevelChild.children || topLevelChild.children eq null)}">
	                        <li>
	                        	<cms:component component="${dropdownValue.item}" evaluateRestriction="true"/>
                        	</li>
			             </c:when>
			             <c:otherwise>
				             <li>
	                               <a href="javascript:void(0)" class="l2-anchor with-sublevel" id="xsLevel2Link${children.index+1}-${l3link.index+1}">
	                                    ${topLevelChild.title}
	                                   <span class="right-arrow-icon">
	                                       <svg>
	                                           <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
	                                       </svg>
	                                   </span>
	                               </a>
	                         </li>
			             </c:otherwise>
			             </c:choose>
                         </c:forEach>
                         </c:forEach>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                	<cms:component component="${feature.promotionBanner}" evaluateRestriction="true"/>
                </div>
            </div>
            
            <c:forEach items="${l1.children}" var="topLevelChild" varStatus="l3link">
            <div class="modal-content xs-main-menu-l3 xs-mega-menu d-none" id="xsLevel2Link${children.index+1}-${l3link.index+1}Container">
                <div class="modal-header">
                    <div class="left-title-out">
                        <a href="javascript:void(0)" class="back-to-l2">
                            <span>
                                <svg>
                                    <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-left" />
                                </svg>
                            </span>

                            <span class="back-text"></span>
                        </a>
                    </div>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">
                            Close
                            <span class="cross-icon">
                                &times;
                            </span>
                        </span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="xs-main-menu-l3-links menu-listing">
						<c:if test="${not empty topLevelChild.entries && topLevelChild.entries ne null && not empty topLevelChild.children && topLevelChild.children ne null}">
                        <div class="menu-container-title">
                            <c:forEach items="${topLevelChild.entries}" var="dropdownValue">
                              	<cms:component component="${dropdownValue.item}" evaluateRestriction="true" />
                           	</c:forEach>
                            <span class="arrow-right-icon">
                                <svg>
                                    <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right"></use>
                                </svg>
                            </span>
                        </div>
                        </c:if>
                        <c:forEach items="${topLevelChild.children}" var="entry2">
						<c:forEach items="${entry2.children}" var="entry" varStatus="count">
						<c:if test="${count.index eq 0}">
                        <div class="mb-4">
                            <div class="container-col-title">
                                ${entry.title}
                            </div>
                            <ul>
                            	<c:forEach items="${entry.entries}" var="topLevelLink1">
                                <li>
                                    <cms:component component="${topLevelLink1.item}" evaluateRestriction="true" />
                                </li>
                                </c:forEach>
                            </ul>
                        </div>
                        </c:if>
                        <c:if test="${count.index ne 0}">
                        <div>
                            <div class="container-col-title">
                                ${entry.title}
                            </div>
                            <ul>
                            	<c:forEach items="${entry.entries}" var="topLevelLink1">
                                <li>
                                    <cms:component component="${topLevelLink1.item}" evaluateRestriction="true" />
                                </li>
                                </c:forEach>
                            </ul>
                        </div>
                        </c:if>
                        </c:forEach>
                        </c:forEach>
                    </div>
                    </div>
	                <div class="modal-footer">
	                	<cms:component component="${feature.promotionBanner}" evaluateRestriction="true"/>
	                </div>
	                </div>
	                </c:forEach>
	                </c:if>
	                
	       <c:if test="${not empty l1.children && l1.children ne null && l1.uid eq 'ContactNavNode'}">

           <div class="modal-content xs-main-menu-l2 xs-mega-menu d-none" id="xsLevel1Link${children.index+1}Container">

                <div class="modal-header">
                    <div class="left-title-out">
                        <a href="javascript:void(0)" class="back-to-l1">
                            <span>
                                <svg>
                                    <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-left" />
                                </svg>
                            </span>

                            <span class="back-text"></span>
                        </a>
                    </div>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">
                            Close
                            <span class="cross-icon">
                                &times;
                            </span>
                        </span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="xs-main-menu-l2-links menu-listing contact-us">
                        <div class="contact-us-container-xs">
                        <c:forEach items="${l1.children}" var="topLevelChild" varStatus="l3link">
                            <c:if test="${l3link.index eq 0}">
                            <div class="row mb-4">
                                <div class="col-12">
                                <c:forEach items="${topLevelChild.entries}" var="topLevelLink1">
                               	${topLevelLink1.item.linkName}
                                </c:forEach>
								<div class="contact-us-description">
                                    ${topLevelChild.title}
                                </div>
                                </div>
                            </div>
                            </c:if>
                            <c:if test="${l3link.index ne 0}">
                            <div class="row mb-4">
                                <div class="col-12">
                                    <div class="menu-container-title less-margin">
                                    <c:forEach items="${topLevelChild.entries}" var="topLevelLink1">
                                        <cms:component component="${topLevelLink1.item}" evaluateRestriction="true" />
                                        <span class="arrow-right-icon">
                                        <svg>
                                            <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-right" />
                                        </svg>
                                    </span>
                                    </c:forEach>
                                    </div>
                                    <div class="contact-us-description">
                                        ${topLevelChild.title}
                                    </div>
                                </div>
                            </div>
                            </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                	<cms:component component="${feature.promotionBanner}" evaluateRestriction="true"/>
                </div>
            </div>
            </c:if>
            </c:forEach>
	</div>
</div>
</c:if>
</cms:pageSlot>