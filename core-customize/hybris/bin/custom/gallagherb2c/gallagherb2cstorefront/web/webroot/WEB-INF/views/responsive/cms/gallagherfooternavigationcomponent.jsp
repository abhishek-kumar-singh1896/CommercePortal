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
					<div class="col-md-12 col-lg-3 mb-md-4 footer-logo-col">
						<div class="gallagher-logo">
							<cms:component component="${siteLogo }" />
						</div>
					</div>
					
						<c:forEach items="${component.navigationNode.children}"
							var="childLevel1">
							<c:forEach items="${childLevel1.children}"
								step="${component.wrapAfter}" varStatus="i">
								<!-- 								<div class=""> -->
								<%-- 									<c:if test="${component.wrapAfter > i.index}"> --%>
								<%-- 										<div class="title">${fn:escapeXml(childLevel1.title)}</div> --%>
								<%-- 									</c:if> --%>
								<div class="col-md-4 col-lg-3">
									<div class="footer-links-out">
										<c:forEach items="${childLevel1.children}" var="childLevel2"
											begin="${i.index}" end="${i.index + component.wrapAfter - 1}">
											<c:forEach items="${childLevel2.entries}" var="childlink">
												<cms:component component="${childlink.item}"
													evaluateRestriction="true" />
											</c:forEach>
	
										</c:forEach>
									</div>
								</div>
							</c:forEach>
						</c:forEach>
					
					<div class="col-md-4 col-lg-3">
						<div class="social-icons">
							<a href="https://www.facebook.com/" title="Facebook" target="_blank">
								<img src="_ui/responsive/common/images/facebook.png"
									alt="Facebook">
							</a>
							<a href="https://www.youtube.com/" title="Youtuebe" target="_blank">
								<img src="_ui/responsive/common/images/youtube.png"
									alt="Youtube">
							</a>
							<a href="https://www.linkedin.com/" title="Linkedin" target="_blank">
								<img src="_ui/responsive/common/images/linkedin.png"
									alt="Linkedin">
							</a>
							<a href="https://www.linkedin.com/" title="Twitter" target="_blank">
								<img src="_ui/responsive/common/images/twitter.png"
									alt="Twitter">
							</a>
							<a href="https://www.instagram.com/" title="Instagram" target="_blank">
								<img src="_ui/responsive/common/images/instagram.png"
									alt="Instagram">
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="footer-bottom-section">
			<div class="container">
				<div class="row">
					<div class="col-md-6">
						${noticeHeader} 
						<span class="down-arrow-icon">
							<svg>
								<use xlink:href="_ui/responsive/common/images/gallagher-icons.svg#arrow-down" />
	                        </svg>
						</span>
					</div>
					<div class="col-md-6 footer-text-right footer-text-order">
						<c:forEach items="${footerLinkNagivationNode.children}"
							var="childLevel1">
							<c:forEach items="${childLevel1.entries}" var="entry">
								<cms:component component="${entry.item}"
									evaluateRestriction="true" />
							</c:forEach>
						</c:forEach>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<span class="copyright-text"> ${notice} </span>
					</div>
				</div>
			</div>
		</div>
	</div>

</c:if>