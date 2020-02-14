<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>



                            <div class="sidebar-filter-section d-flex flex-column justify-content-center">
                                <div>
                                    <a href="javascript:void(0)">
                                        <span class="cross-icon">
                                            <svg>
                                                <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#cross" />
                                            </svg>
                                        </span>
                                        Clear filters
                                    </a>
                                </div>

                            </div>
                
                
						<c:forEach items="${pageData.facets}" var="facet" varStatus="i">
							<c:choose>
								<c:when test="${facet.code eq 'availableInStores'}">
									<%-- <nav:facetNavRefinementStoresFacet facetData="${facet}" userLocation="${userLocation}"/> --%>
								</c:when>
								<c:when test="${facet.code eq 'price'}">
									<%-- <nav:facetNavRefinementStoresFacet facetData="${facet}" userLocation="${userLocation}"/> --%>
									<!-- this part is for price slider   -->
									<div id="slider-range"></div>
									<div class="countarea">
										<input id="my_min" name="" type="text" readonly="readonly" />
											<strong>to</strong>
										<input id="my_max" name="" type="text" readonly="readonly" />
									</div> 
								</c:when>
								<c:otherwise>
								
								        <c:set var="flag" value="" />
						            <c:if test="${i.first}"> 
						            this is 
						           <c:set var="flag" value="show" />
						            </c:if>
									<nav:facetNavRefinementFacet facetData="${facet}" collapseflag="${flag}"/>
								</c:otherwise>
							</c:choose>
						</c:forEach>

					
	
