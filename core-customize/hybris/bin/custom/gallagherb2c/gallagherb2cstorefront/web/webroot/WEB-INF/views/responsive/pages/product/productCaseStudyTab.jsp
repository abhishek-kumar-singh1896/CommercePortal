<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%-- <div class="tabhead">
	<a href="">${fn:escapeXml(title)}</a> <span class="glyphicon"></span>
</div> --%>
<section id="${fn:replace(title,' ', '')}" class="common-sub-tab-section ptb-xs-0">
	<c:if test="${not empty product.caseStudies}">
	<div class="container">
		<h4 class="small-section-sub-title">Case Study</h4>
		<div class="row">
			<div class="component search-results col-12">
				<ul class="search-result-list">
				<c:forEach var="casestudy" items="${product.caseStudies}">
					<li>
						<div class="col-12 col-sm-6 col-md-4 col-lg-3 case-study-block">
							<div class="case-study-block-content">
								<div class="case-study-block-image" 
									style="background: url(${casestudy.picture});">								
								</div>
								<div class="case-study-text">
									<h5 class="field-title">${casestudy.desc}</h5>
								</div>
								<div class="link">
									<a title="${casestudy.desc}" 
										href="${casestudy.link}">Read more &gt;</a>
								</div>
							</div>
						</div>
					</li>
				</c:forEach>
				</ul>
			</div>
		</div>
	</div>	
	</c:if>
</section>

