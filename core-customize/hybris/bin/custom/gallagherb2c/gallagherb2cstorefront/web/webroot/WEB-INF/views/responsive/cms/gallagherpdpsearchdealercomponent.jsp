<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${component.visible}">
	<c:set var="regionCodeUpper" scope="page" value="${fn:toUpperCase(regionCode)}"/>
	<c:set var="languageUpper" scope="page" value="${fn:toUpperCase(language)}"/>
	
    <div class="find-dealer-out">
        <div class="container">
            <div class="row">
                <div class="col-md-6 col-padding-0">
                    <div class="gray-shade"></div>
                    <div class="find-dealer-img"  style="background: url('${mapBackground.media.url}') center center no-repeat; background-size: cover;">
                        &nbsp;
                    </div>
                    <div class="find-dealer-out-middle">
                        <div class="find-dealer-title">
                        	<cms:component component="${findDealerText}"
												evaluateRestriction="true" />
                        </div>
                        <div class="find-dealer-search-out">
                           <c:forEach items="${searchDealer.children}"
										var="sdNode" varStatus="i">
										<c:forEach items="${sdNode.entries}" var="sd">												
											<%-- <c:if test="${sd.item.uid == (regionCodeUpper).concat(languageUpper).concat('SearchComponent')}"> --%>
		                       
												<cms:component component="${sd.item}"
															evaluateRestriction="true" />
									<%-- 		</c:if> --%>
										</c:forEach>
									</c:forEach>
                                
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-padding-0">
                    <div class="primary-shade"></div>
                    <div class="find-dealer-img" style="background: url('${background.media.url}') center center no-repeat; background-size: cover;">
                        &nbsp;
                    </div>
                    <div class="find-dealer-out-middle">
                        <div class="expert-advice-title">
                        	<cms:component component="${expertAdviceText}"
												evaluateRestriction="true" />
                        </div>
                        <div class="expert-advice-number">
                        	<cms:component component="${contactNoText}"
												evaluateRestriction="true" />
                        </div>
                        <div class="expert-advice-common-text">
                        	<c:forEach items="${territoryManager.children}"
									var="tmNode" varStatus="i">
										<c:forEach items="${tmNode.entries}" var="tm">
<%-- 											<c:if test="${tm.item.uid == (regionCodeUpper).concat('TMLinkComp')}"> --%>
<%-- 												<a href="${tm.item.url}" target="_blank">${tm.item.linkName}</a> --%>
<%-- 											</c:if> --%>
												<cms:component component="${tm.item}"
															evaluateRestriction="true" />
										</c:forEach>
									</c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>
