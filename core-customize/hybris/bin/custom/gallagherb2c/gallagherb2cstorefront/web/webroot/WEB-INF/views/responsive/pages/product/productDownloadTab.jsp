<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<section id="${fn:replace(title,' ', '')}" class="common-sub-tab-section">
    <div class="container">
        <div class="how-to-download-section">
            <h4 class="small-section-sub-title">Related documents</h4>

            <ul class="row align-items-stretch">
                <li class="col-md-3 col-6">
                    <div class="download-column-out">
                        <div class="download-column-title">
                            <a href="javascript:void(0)">
                                Animal Management Weighing and EID Brochure
                            </a>
                        </div>
                        <div class="download-column-bottom-section">
                            <span class="document-icon">
                                <svg>
                                    <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#pdf-download" />
                                </svg>
                            </span>
                            <span>
                                NZ AU, 0.7 MB
                            </span>
                        </div>
                    </div>
                </li>
                <li class="col-md-3 col-6">
                    <div class="download-column-out">
                        <div class="download-column-title">
                            <a href="javascript:void(0)">
                                3E4402 TWR-5 Quick Start Guide
                            </a>
                        </div>
                        <div class="download-column-bottom-section">
                            <span class="document-icon">
                                <svg>
                                    <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#pdf-download" />
                                </svg>
                            </span>
                            <span>
                                NZ AU, 0.7 MB
                            </span>
                        </div>
                    </div>
                </li>
                <li class="col-md-3 col-6">
                    <div class="download-column-out">
                        <div class="download-column-title">
                            <a href="javascript:void(0)">
                                TWR & TW DL brochure 2019
                            </a>
                        </div>
                        <div class="download-column-bottom-section">
                            <span class="document-icon">
                                <svg>
                                    <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#pdf-download" />
                                </svg>
                            </span>
                            <span>
                                NZ AU, 0.7 MB
                            </span>
                        </div>
                    </div>
                </li>
                <li class="col-md-3 col-6">
                    <div class="download-column-out">
                        <div class="download-column-title">
                            <a href="javascript:void(0)">
                                Animal Management Weighing and EID Brochure
                            </a>
                        </div>
                        <div class="download-column-bottom-section">
                            <span class="document-icon">
                                <svg>
                                    <use xlink:href="${commonResourcePath}/images/gallagher-icons.svg#pdf-download" />
                                </svg>
                            </span>
                            <span>
                                NZ AU, 0.7 MB
                            </span>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>

    <div class="register-account-out">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-12">
                    <h2 class="register-account-title"><spring:theme code="text.Register.Account.Title"/></h2>
                    <button type="button" class="btn btn-primary"><spring:theme code="text.Register.Account"/></button>
                </div>
            </div>
        </div>
    </div>
    <!-- <div class="container">
        <div class="two-column-section">
            <div class="row">
                <div class="col-md-6">
                    <div class="column-full-img">
                        <img src="img/product-two-column-img.png" alt="Product Image">
                    </div>
                </div>

                <div class="col-md-6">
                    <h1 class="section-title">
                        Award winning innovation
                    </h1>
                    <div class="section-decription">
                        <p>The TW Weigh Scales has won the following awards:</p>
                        <div class="content-list-out">
                            <ul>
                                <li>2018 Good Design Award Australia</li>
                                <li>2017 Good Design Award Chicago</li>
                                <li>2018 Red dot Award - Product Design</li>
                                <li>2017 Best Design Awards NZ - 'Gold pin' best in Category, 'Purple
                                    pin' best in Discipline </li>
                            </ul>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div> -->
</section>