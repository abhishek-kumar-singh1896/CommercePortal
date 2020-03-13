<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<section id="${fn:escapeXml(title)}" class="common-sub-tab-section py-0">
<div class="common-sub-tab-section gray-back">
   <div class="container">
       <div class="row">
           <div class="col-lg-6 col-padding-0">
               <div class="common-product-with-title">
                   <div class="section-title">
                       Products for compliance
                   </div>
                   <div class="section-description">
                       If you have a small farm or garden, these work for you
                   </div>

                   <div class="product-list">
                       <div class="product-listing-out clearfix">
                           <ul>
                           <c:forEach items="${sparepart}" var="reference">
								<product:productReferenceList product="${reference}" />
							</c:forEach>
							
                           </ul>
                       </div>
                       <div class="mt-3">
                           <a href="/am/us/en/Animal-Management/Electric-Fencing/c/electric-fencing">See all</a>
                       </div>
                   </div>

               </div>
           </div>
           <div class="col-lg-6 col-padding-0">
           <div class="common-product-with-title">
                   <div class="section-title">
                       Products for cattle
                   </div>
                   <div class="section-description">
                       Lorem ipsum dolor sit amet et cetera
                   </div>

                   <div class="product-list">
                       <div class="product-listing-out clearfix">
                           <ul>
                           	<c:forEach items="${others}" var="reference1">
							<product:productReferenceList product="${reference1}" />
							</c:forEach>
                            </ul>
                        </div>
                        <div class="mt-3">
                            <a href="/am/us/en/Animal-Management/Electric-Fencing/c/electric-fencing">See all</a>
                        </div>
                    </div>

                </div>
				<%-- <product:productReferenceCattle product="${product}"/> --%>

            </div>
        </div>
    </div>
</div>




    <div class="find-dealer-out">
        <div class="container">
            <div class="row">
                <div class="col-md-6 col-padding-0">
                    <div class="gray-shade"></div>
                    <div class="find-dealer-img"
                        style="background: url('${commonResourcePath}/images/find-dealer-img.png') center center no-repeat; background-size: cover;">
                        &nbsp;
                    </div>
                    <div class="find-dealer-out-middle">
                        <div class="find-dealer-title">Find a dealer near you</div>
                        <div class="find-dealer-search-out">
                            <input type="text" class="form-control input-dealer-search"
                                placeholder="Enter post code">
                            <button type="submit"
                                class="btn btn-highlight btn-dealer-search">Search</button>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-padding-0">
                    <div class="primary-shade"></div>
                    <div class="find-dealer-img"
                        style="background: url('${commonResourcePath}/images/getexpert-advice-img.png') center center no-repeat; background-size: cover;">
                        &nbsp;
                    </div>
                    <div class="find-dealer-out-middle">
                        <div class="expert-advice-title">Get expert advice</div>
                        <div class="expert-advice-number">0800 731 500</div>
                        <div class="expert-advice-common-text">Or Find Your Nearest Territory Manager
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>