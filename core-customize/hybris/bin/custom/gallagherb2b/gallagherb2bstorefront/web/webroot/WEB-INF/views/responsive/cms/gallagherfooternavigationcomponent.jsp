<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="footer"
	tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${component.visible}">
	<div class="footer-out">
		<div class="footer-top-section">
			<div class="container">
				<div class="row">
					<div class="col-lg-offset-2 col-md-offset-3  col-sm-offset-1 col-lg-8 col-md-7 col-sm-10 row">
						<c:forEach items="${component.navigationNode.children}"
							var="childLevel1">
							<c:forEach items="${childLevel1.children}"
								step="${component.wrapAfter}" varStatus="i">
								<c:choose>
									<c:when test = "${childLevel1.title ne  'Support'}">
										<div class="col-xs-6 col-sm-4 col-lg-3">
											<div class="footer-links-out">
												<div class="footer-links-title">${fn:escapeXml(childLevel1.title)}</div>
												<c:forEach items="${childLevel1.children}" var="childLevel2"
													begin="${i.index}" end="${i.index + component.wrapAfter - 1}">
													<c:forEach items="${childLevel2.entries}" var="childlink">
														<ul>
															<li>
																<cms:component component="${childlink.item}"
																	evaluateRestriction="true"/>
															</li>
														</ul>
													</c:forEach>
												</c:forEach>												
											</div>
										</div>
									</c:when>
						 			<c:otherwise>
							      	<div class="col-xs-6 col-sm-4 col-lg-3 support-col">
											<div class="footer-links-out">
												<div class="footer-links-title">${fn:escapeXml(childLevel1.title)}</div>
												<c:forEach items="${childLevel1.children}" var="childLevel2"
													begin="${i.index}" end="${i.index + component.wrapAfter - 1}">
													<c:forEach items="${childLevel2.entries}" var="childlink">
														<ul>
															<li>
																<cms:component component="${childlink.item}"
																	evaluateRestriction="true"/>
															</li>
														</ul>
													</c:forEach>
												</c:forEach>
											</div>
										</div>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
		<%-- <div class="col-md-4 col-lg-3">
			<div class="social-icons">
				<a href="https://www.facebook.com/" title="Facebook" target="_blank">
					<img src="${commonResourcePath}/images/facebook.png" alt="Facebook">
				</a> <a href="https://www.youtube.com/" title="Youtuebe" target="_blank">
					<img src="${commonResourcePath}/images/youtube.png" alt="Youtube">
				</a> <a href="https://www.linkedin.com/" title="Linkedin"
					target="_blank"> <img
					src="${commonResourcePath}/images/linkedin.png" alt="Linkedin">
				</a> <a href="https://www.twitter.com/" title="Twitter" target="_blank">
					<img src="${commonResourcePath}/images/twitter.png" alt="Twitter">
				</a> <a href="https://www.instagram.com/" title="Instagram"
					target="_blank"> <img
					src="${commonResourcePath}/images/instagram.png" alt="Instagram">
				</a>
			</div>
		</div> --%>

		<div class="footer-bottom-section">
			<div class="container">
				<div class="row">
				
					<div class="col-xs-12 col-sm-12 col-md-3 col-lg-2 footer-bottom-left-section">
						<div class="dropup division-dropdown">
							<c:forEach
								items="${gallagherDivisionsLinkNagivationNode.children}"
								var="othersDivisionsDropdown" varStatus="i">
								<c:forEach items="${othersDivisionsDropdown.entries}"
									var="dropdownValue">
									<c:if test="${i.index == 0}">
										<button class="btn dropdown-toggle" type="button" id="dropdownMenu2"
										data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
											${dropdownValue.item.linkName} 
											<span class="down-arrow-icon">
												<svg>
													<use xlink:href="${siteRootUrl}/theme-securityB2B/images/svg/gallagher-icons.svg#arrow-down" /></use>
												</svg>
											</span>
										</button>
									</c:if>
								</c:forEach>
							</c:forEach>
							<ul class="dropdown-menu" aria-labelledby="dropdownMenu2">
								<c:forEach
									items="${gallagherDivisionsLinkNagivationNode.children}"
									var="othersDivisionsDropdown1" varStatus="i">
									<c:forEach items="${othersDivisionsDropdown1.entries}"
										var="dropdownValue1">
										<c:if test="${i.index != 0}">
											<li>
												<a href=${dropdownValue1.item.url}>${dropdownValue1.item.linkName}</a>
	                               </li>
										</c:if>
									</c:forEach>
								</c:forEach>
							</ul>
						</div>
					</div>
					
					<div class="col-xs-12 col-sm-12 col-md-9 col-lg-10 footer-bottom-middle-section">
						<div class="footer-middle-section-inner">
							<div class="left-social-links-col">
								<ul>
									<li class="field-link">
										<c:forEach items="${footerLinkNagivationNode.children}"
											var="childLevel1">
											<c:forEach items="${childLevel1.entries}" var="entry">
												<cms:component component="${entry.item}"
													evaluateRestriction="true" />
											</c:forEach>
										</c:forEach>
									</li>
									<c:forEach items="${socialLinkNagivationNode.links}" var="childLevel2">
										<cms:component component="${childLevel2}"	evaluateRestriction="true"/>
									</c:forEach>
								</ul>
							</div>
									
							<div class="copyright-col">
								<div class="copyright">
									<div class="field-copyright"> ${noticeHeader}</div>
								</div>
							</div>
							
							<div class="country-col">
								<div class="dropup country-dropdown">
									<button class="btn dropdown-toggle" type="button" id="dropdownMenuCountry"
										data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
										<c:forEach items="${countryDropdownLinkNagivationNode.children}"
											var="countryDropdown" varStatus="i">
											<c:forEach items="${countryDropdown.entries}"
												var="dropdownValue">
												<c:if test="${dropdownValue.item.uid == regionCode.code.concat('B2BLink')}">
													${dropdownValue.item.linkName}
												</c:if>
											</c:forEach>
										</c:forEach>
										<span class="down-arrow-icon">
											<svg>
												<use xlink:href="${siteRootUrl}/theme-securityB2B/images/svg/gallagher-icons.svg#arrow-down"></use>
											</svg>
										</span>
									</button>
									<ul class="dropdown-menu" aria-labelledby="dropdownMenuCountry">
										<c:forEach items="${countryDropdownLinkNagivationNode.children}"
											var="countryDropdown" varStatus="i">
											<c:forEach items="${countryDropdown.entries}"
												var="dropdownValue">
												<c:if test="${dropdownValue.item.uid != regionCode.code.concat('B2BLink')}">
													<li>
														<cms:component component="${dropdownValue.item}"/>
													</li>
												</c:if>
											</c:forEach>
										</c:forEach>											
									</ul>
								</div>
							</div>
						
						</div>
					</div>
					
				</div>
			</div>
		</div>
	</div>
</c:if>