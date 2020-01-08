<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<!--<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>-->



<div class="tabs js-tabs tabs-responsive">

<!--
<sec:authorize access="hasAnyRole('ROLE_DISTRIBUTOR')">
	<div class="tabhead">
		<a href=""><spring:theme code="product.product.details" /></a> <span
			class="glyphicon"></span>
	</div>
	<div class="tabbody">
		<div class="container-lg">
			<div class="row">
				<div class="col-md-6 col-lg-4">
					<div class="tab-container">
						<product:productDetailsTab product="${product}" />
					</div>
				</div>
			</div>
		</div>
	</div>
</sec:authorize>

-->


<div id="details" class="tabhead">
		<a href="">Product details</a> <span
			class="glyphicon"></span>
	</div>
	<div class="tabbody">
		<div class="container-lg">
            <a target="_blank" href="http://raymor.co.nz/wp-content/uploads/2016/10/567903_Raymor_WallBasins_Quartz-WEB.pdf">
                         <button id="btn-print-statement" class="btn-primary" style="float: left" type="button">CLICK TO DOWNLOAD PRODUCT DETAILS</button>
                        </a>
                        <br>
		</div>
		</div>

	<div class="tabhead">
		<a href="">Technical Spec</a> <span
			class="glyphicon"></span>
	</div>

	<div class="tabbody">
		<div class="container-lg">
			<div class="row">
				<div class="col-md-6 col-lg-4">
					<div class="tab-container">
						<product:productDetailsClassifications product="${product}" />
					</div>
				</div>
			</div>
		</div>
	</div>



<div id="installation" class="tabhead">
		<a href="">Installation manual</a> <span
			class="glyphicon"></span>
	</div>
	<div class="tabbody">
		<div class="container-lg">
            <a target="_blank" href="http://localhost:9001//medias/bathroom-shower.pdf?context=bWFzdGVyfGltYWdlc3wzMDkwMzV8YXBwbGljYXRpb24vcGRmfGltYWdlcy9oZTIvaGVmLzg3OTc3MzM5NzgxNDIucGRmfGY0ODljMTcyN2Y4ZjUwMmNjZDk5YTcxNWFkNzI4ODQ4NTdmMWY0NGUyZDZhMmFiNTFmYTIyNWFlZDZhMTYzMmU">
                         <button id="btn-print-statement" class="btn-primary" style="float: left" type="button">CLICK TO DOWNLOAD INSTALLATION MANUAL</button>
                        </a>
                        <br>
		</div>
		</div>


<div id="video" class="tabhead">
		<a href="">Video</a> <span
			class="glyphicon"></span>
	</div>
	<div class="tabbody">
		<div class="container-lg">

<iframe width="560" height="315" src="https://www.youtube.com/embed/kQIkXlpJ2Vg" frameborder="0" allowfullscreen></iframe>
		</div>
	</div>



	<div id="tabreview" class="tabhead">
		<a href="">Reviews</a> <span
			class="glyphicon"></span>
	</div>
	<div class="tabbody">
		<div class="container-lg">
			<div class="row">
				<div class=" col-md-6 col-lg-4">
					<div class="tab-container">
<product:productPageReviewsTab product="${product}" />
					</div>
				</div>
			</div>
		</div>
	</div>


<!--<sec:authorize access="hasAnyRole('ROLE_BUILDER')">
<div id="tabreview" class="tabhead">
		<a href="">Product Warranty</a> <span
			class="glyphicon"></span>
	</div>
	<div class="tabbody">
		<div class="container-lg">
<div class="container-lg">
            <a target="_blank" href="http://accel.com.au/base_tl/Scyon_Stria_Warranty.pdf">
                         <button id="btn-print-statement" class="btn-primary" style="float: left" type="button">CLICK TO DOWNLOAD WARRANTY INFO</button>
                        </a>
                        <br>
		</div>
		</div>
</div>
</sec:authorize>-->










	<cms:pageSlot position="Tabs" var="tabs">
		<cms:component component="${tabs}" />
	</cms:pageSlot>

</div>