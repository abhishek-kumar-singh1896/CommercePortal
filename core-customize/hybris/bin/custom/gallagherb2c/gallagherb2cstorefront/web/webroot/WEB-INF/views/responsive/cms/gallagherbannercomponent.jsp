<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<div class="xs-main-menu-l2-footer" style="background: url(${media.url}) left top no-repeat; background-size: cover;">
    <div class="${component.styleClass}"></div>
    <div class="menu-l2-footer-container">
        <div class="menu-l2-footer-container-inner">
            <div class="promotion-title">${component.heading}</div>
            <div class="promotion-link">
            	<cms:component component="${component.link}" evaluateRestriction="true" />
            </div>
        </div>
    </div>
</div>