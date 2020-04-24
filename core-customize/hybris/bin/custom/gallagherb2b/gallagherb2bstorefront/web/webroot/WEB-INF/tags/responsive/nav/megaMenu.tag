<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<cms:pageSlot position="NavigationBar" var="component">
	<c:if test="${component.visible}">
		<div class="modal-content xs-main-menu-l2 xs-mega-menu d-none" id="xsLevel1Link3Container">
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
						<c:forEach items="${component.menuItems}" var="menuItem" varStatus="status">
							<c:set var="menuData" value="${menuMap[menuItem.categoryCode]}" />
								<li class="<c:if test="${not empty menuData.subCategories[menuItem.categoryCode]}">nav__links--primary-has__sub js-enquire-has-sub</c:if>">
									<c:url var="L2link" value="${menuItem.link.url}"></c:url>
										<a href="${L2link}" class="with-sublevel-text">
										    ${menuItem.link.linkName}
										</a>
									<c:if test="${not empty menuData.subCategories[menuItem.categoryCode]}">
										<a href="javascript:void(0)" class="l2-anchor with-sublevel" id="xsLevel2Link${status.index+1}-1">
											<span class="right-arrow-icon">
												<svg>
													<use xlink:href="${siteRootUrl}/theme-securityB2B/images/svg/gallagher-icons.svg#arrow-right" />
												</svg>
											</span>
										</a>
									</c:if>
								</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		<c:forEach items="${component.menuItems}" var="menuItem1" varStatus="index">
		<c:set var="menuData" value="${menuMap[menuItem1.categoryCode]}" />
		<c:if test="${not empty menuData.subCategories[menuItem1.categoryCode]}">
			<div class="modal-content xs-main-menu-l3 xs-mega-menu d-none" id="xsLevel2Link${index.index+1}-1Container">
				<div class="modal-header">
					<div class="left-title-out">
						<a href="javascript:void(0)" class="back-to-l2">
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
							<c:forEach items="${menuData.subCategories[menuItem1.categoryCode]}" var="allProductsChilds">
								<c:if test="${empty menuData.subCategories[allProductsChilds.code]}">
									<li>
										<c:url value="${allProductsChilds.url}" var="childUrl" /> 
										<a href="${childUrl}" title="${allProductsChilds.name}">${allProductsChilds.name}</a>
									</li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</c:if>
		</c:forEach>
	
	
	</c:if>
</cms:pageSlot>