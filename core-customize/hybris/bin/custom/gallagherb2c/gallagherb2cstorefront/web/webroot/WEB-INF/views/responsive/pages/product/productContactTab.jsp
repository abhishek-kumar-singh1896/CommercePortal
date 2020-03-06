<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<section id="${fn:escapeXml(title)}" class="common-sub-tab-section">
<div class="common-sub-tab-section gray-back">
   <div class="container">
       <div class="row">
           <div class="col-lg-6">
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
                           <c:forEach items="${product.productReferences}" var="reference">
							<c:if test="${reference.referenceType eq 'FOLLOWUP'}">
							<product:productListerGridItem product="${reference.target}" />
							</c:if>
							</c:forEach>
                               <!-- <li class="product-tile"
                                   onclick="window.location.href='product-details.html'">
                                   <div class="row">
                                       <div class="col-md-12 pr-xs-0">
                                           <div class="product-status">
                                               <img src="img/new.svg">
                                           </div>
                                           <div class="product-img-box">
                                               <a href="javascript:void(0)">
                                                   <img class="product-image"
                                                       src="img/product-list-img.png">
                                               </a>
                                           </div>
                                       </div>
                                       <div class="col-md-12">
                                           <div class="product-name-desc-out">
                                               <div class="product-name">Product name 1234
                                               </div>
                                               <div class="product-id">123456</div>
                                               <div class="product-description">
                                                   <p>A description of what this product is used for et
                                                       cetera. Lorem ipsum dolor sit amet och so vidare
                                                       et all.</p>
                                               </div>
                                           </div>
                                       </div>
                                   </div>

                                   <div class="product-list-footer">
                                       <div class="row">
                                           <div class="col-5">
                                               <span class="product-list-footer-left-icon">
                                                   <img src="img/bird.svg" alt="Male">
                                               </span>
                                               <span class="product-list-footer-left-icon">
                                                   <img src="img/bull.svg" alt="Male">
                                               </span>
                                           </div>
                                           <div class="col-7 text-right">
                                               <div class="currency-text">RRP</div>
                                               <div class="currency-value">$123.00</div>
                                           </div>
                                       </div>
                                   </div>
                               </li>
                               <li class="product-tile"
                                   onclick="window.location.href='product-details.html'">
                                   <div class="row">
                                       <div class="col-md-12 pr-xs-0">
                                           <div class="product-status">
                                               <img src="img/new.svg">
                                           </div>
                                           <div class="product-img-box">
                                               <a href="javascript:void(0)">
                                                   <img class="product-image"
                                                       src="img/product-list-img.png">
                                               </a>
                                           </div>
                                       </div>
                                       <div class="col-md-12">
                                           <div class="product-name-desc-out">
                                               <div class="product-name">Product name
                                                   Product
                                                   nameProduct nameProduct
                                                   name 1234
                                               </div>
                                               <div class="product-id">123456</div>
                                               <div class="product-description">
                                                   <p>A description of what this product is used for et
                                                       cetera. Lorem ipsum dolor sit amet och so vidare
                                                       et all.</p>
                                               </div>
                                           </div>
                                       </div>
                                   </div>

                                   <div class="product-list-footer">
                                       <div class="row">
                                           <div class="col-5">
                                               <span class="product-list-footer-left-icon">
                                                   <img src="img/bird.svg" alt="Male">
                                               </span>
                                               <span class="product-list-footer-left-icon">
                                                   <img src="img/bull.svg" alt="Male">
                                               </span>
                                           </div>
                                           <div class="col-7 text-right">
                                               <div class="currency-text">RRP</div>
                                               <div class="currency-value">$123.00</div>
                                           </div>
                                       </div>
                                   </div>
                               </li> -->
                           </ul>
                       </div>
                       <div class="mt-3">
                           <a href="javascript:void(0)">See all</a>
                       </div>
                   </div>

               </div>
           </div>
           <div class="col-lg-6">
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
                               <li class="product-tile"
                                   onclick="window.location.href='product-details.html'">
                                   <div class="row">
                                       <div class="col-md-12 pr-xs-0">
                                           <div class="product-status">
                                               <img src="img/new.svg">
                                           </div>
                                           <div class="product-img-box">
                                               <a href="javascript:void(0)">
                                                   <img class="product-image"
                                                       src="img/product-list-img.png">
                                               </a>
                                           </div>
                                       </div>
                                       <div class="col-md-12">
                                           <div class="product-name-desc-out">
                                               <div class="product-name">Product name 1234
                                               </div>
                                               <div class="product-id">123456</div>
                                               <div class="product-description">
                                                   <p>A description of what this product is used for et
                                                       cetera. Lorem ipsum dolor sit amet och so vidare
                                                       et all.</p>
                                               </div>
                                           </div>
                                       </div>
                                   </div>

                                   <div class="product-list-footer">
                                       <div class="row">
                                           <div class="col-5">
                                               <span class="product-list-footer-left-icon">
                                                   <img src="img/bird.svg" alt="Male">
                                               </span>
                                               <span class="product-list-footer-left-icon">
                                                   <img src="img/bull.svg" alt="Male">
                                               </span>
                                           </div>
                                           <div class="col-7 text-right">
                                               <div class="currency-text">RRP</div>
                                               <div class="currency-value">$123.00</div>
                                           </div>
                                       </div>
                                   </div>
                               </li>
                               <li class="product-tile"
                                   onclick="window.location.href='product-details.html'">
                                   <div class="row">
                                       <div class="col-md-12 pr-xs-0">
                                           <!-- <div class="product-status">
                                               <img src="img/new.svg">
                                           </div> -->
                                            <div class="product-img-box">
                                                <a href="javascript:void(0)">
                                                    <img class="product-image"
                                                        src="img/product-list-img.png">
                                                </a>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="product-name-desc-out">
                                                <div class="product-name">Product name
                                                    Product
                                                    nameProduct nameProduct
                                                    name 1234
                                                </div>
                                                <div class="product-id">123456</div>
                                                <div class="product-description">
                                                    <p>A description of what this product is used for et
                                                        cetera. Lorem ipsum dolor sit amet och so vidare
                                                        et all.</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="product-list-footer">
                                        <div class="row">
                                            <div class="col-5">
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/bird.svg" alt="Male">
                                                </span>
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/bull.svg" alt="Male">
                                                </span>
                                            </div>
                                            <div class="col-7 text-right">
                                                <div class="currency-text">RRP</div>
                                                <div class="currency-value">$123.00</div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <div class="mt-3">
                            <a href="javascript:void(0)">See all</a>
                        </div>
                    </div>

                </div>
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
                        style="background: url('img/find-dealer-img.png') center center no-repeat; background-size: cover;">
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
                        style="background: url('img/getexpert-advice-img.png') center center no-repeat; background-size: cover;">
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