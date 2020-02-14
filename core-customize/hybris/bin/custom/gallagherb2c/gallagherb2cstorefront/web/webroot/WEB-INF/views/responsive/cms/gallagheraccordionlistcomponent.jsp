<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<c:set var="inedx" value="1"/>
<div class="sub-header-out">
   <div class="container">
       <div class="row">
			<div class="col-md-8 col-lg-9">
			    <div class="left-section">
			        <h1 class="sub-header-title">${component.accordionHeading}</h1>
			       		<div class="accordion" id="accordionSubHeader">
					       	<c:forEach items="${component.accordions}" var="accordion" varStatus="status">
					       		<div class="accordion-card">
								    <div class="card-header" id="heading${status.index}">
								        <a href="javascript:void(0)" data-toggle="collapse" data-target="#collapse${status.index}"
								            aria-expanded="false" aria-controls="collapse${status.index}">
								            <span>${accordion.heading}</span>
								            <span class="arrow left-align">
								                <span></span>
								                <span></span>
								            </span>
								        </a>
								    </div>
								
								    <div id="collapse${status.index}" class="collapse" aria-labelledby="heading${status.index}"
								        data-parent="#accordionSubHeader">
								        <div class="card-body">
								            ${accordion.description}
								        </div>
								    </div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
				<div class="col-md-4 col-lg-3">
    				<div class="right-section">
						<cms:component component="${component.buttonInfo}" />
					</div>
				</div>
		</div>
	</div>
</div>