<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="facetData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.FacetData" %>
<%@ attribute name="collapseflag" required="false" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:if test="${not empty facetData.values}">
<ycommerce:testId code="facetNav_title_${facetData.name}">
	<div class="sidebar-section">
		<c:set var="facetData.name" value="${facetData.name}"/> 
		<h4 class="sidebar-section-header with-link">	
		 <a data-toggle="collapse" href="#${fn:replace(facetData.name,' ','_')}" role="button" aria-expanded="false" aria-controls="${facetData.name}">	
			<spring:theme code="search.nav.facetTitle" arguments="${facetData.name}"/>
			<span class="down-arrow-icon">
                                    <svg>
                                        <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-down" />
                                    </svg>
                                </span>
			</a>
			
			<%-- <span class="down-arrow-icon">
          		<svg>
             		  <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#arrow-down" />
               </svg>
           </span> --%>
		</h4>	
		<div class="js-facet-form sidebar-section-container collapse ${collapseflag}" id="${fn:replace(facetData.name,' ','_')}">
			<div class="sidebar-section-container-inner">
			<c:if test="${not empty facetData.topValues}">
				<ul>
					<c:forEach items="${facetData.topValues}" var="facetValue">
						<li>
							<c:if test="${facetData.multiSelect}">
								<form action="#" method="get">
								<!-- facetValue.query.query.value and searchPageData.freeTextSearch are html output encoded in the backend -->
									<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
									<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
									<label>
										<input class="facet__list__checkbox" type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''} class="facet-checkbox" />
										<span class="facet__list__label">
												${fn:escapeXml(facetValue.name)}
												
										</span>
									</label>
								</form>
							</c:if>
							<c:if test="${not facetData.multiSelect}">
								<c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>
									<a href="${fn:escapeXml(facetValueQueryUrl)}&amp;text=${searchPageData.freeTextSearch}">${fn:escapeXml(facetValue.name)}&nbsp;
									<ycommerce:testId code="facetNav_count">
										<spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/>
									</ycommerce:testId>
									</a>
							</c:if>
						</li>
					</c:forEach>
				</ul>
			</c:if>
			<ul>
				<c:forEach items="${facetData.values}" var="facetValue">
					<li class="${facetData.multiSelect ?'multi-select':'single-select'}">
						<c:if test="${facetData.multiSelect}">
							<ycommerce:testId code="facetNav_selectForm">
							<form action="#" method="get">
							<!-- facetValue.query.query.value and searchPageData.freeTextSearch are html output encoded in the backend -->
								<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
								<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
								<label>
									<input type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''} class="facet__list__checkbox js-facet-checkbox" />
<!-- 											 class="facet__list__checkbox js-facet-checkbox sr-only"											 -->
											${fn:escapeXml(facetValue.name)}
											<%-- <ycommerce:testId code="facetNav_count">

												<spring:theme code="search.nav.facetValueCount" arguments="${facetValue.count}"/>

											</ycommerce:testId> --%>

								</label>
							</form>
							</ycommerce:testId>
						</c:if>
						<c:if test="${not facetData.multiSelect}">
							<c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>
								 <a href="${fn:escapeXml(facetValueQueryUrl)}"> 
								${fn:escapeXml(facetValue.name)}  
								</a>
						</c:if>
					</li>
				</c:forEach>
			</ul>

			<c:if test="${not empty facetData.topValues}">
				<span class="facet__values__more js-more-facet-values">
					<a href="#" class="js-more-facet-values-link" ><spring:theme code="search.nav.facetShowMore_${facetData.code}" /></a>
				</span>
				<span class="facet__values__less js-less-facet-values">
					<a href="#" class="js-less-facet-values-link"><spring:theme code="search.nav.facetShowLess_${facetData.code}" /></a>
				</span>
			</c:if>
			</div>
		</div>
	</div>
</ycommerce:testId>
</c:if>
