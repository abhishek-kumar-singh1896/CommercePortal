<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<template:page pageTitle="${pageTitle}">

<div class="row">
		<cms:pageSlot position="Section1" var="feature" element="div"
			class="product-list-section1-slot">
			<cms:component component="${feature}" element="div"
				class="col-xs-12 yComponentWrapper product-list-section1-component" />
		</cms:pageSlot>
	</div>
	<div class="product-list">
		<!-- 		<div class="row"> -->
		<!-- 		<div class="col-xs-3"> -->
		<div class="product-list-container-out">
			<div class="container">
				<div class="row">
					<div class="col-lg-3 d-none d-lg-block">
						<cms:pageSlot position="ProductLeftRefinements" var="feature"
							element="div" class="product-list-left-refinements-slot">
							<cms:component component="${feature}" element="div"
								class="yComponentWrapper product-list-left-refinements-component" />
						</cms:pageSlot>
					</div>


					<!-- 		<div class="col-sm-12 col-md-9"> -->
					<div class="col-lg-9 plr-xs-0">
						<cms:pageSlot position="ProductListSlot" var="feature"
							element="div" class="product-list-right-slot">
							<cms:component component="${feature}" element="div"
								class="product__list--wrapper yComponentWrapper product-list-right-component" />
						</cms:pageSlot>
					</div>
				</div>
			</div>
		</div>
	</div>


    
    <div class="product-list">

        <div class="banner-out"
            style="background: url('img/promotion-img-1.png') left top no-repeat; background-size: cover;">
            <div class="orange-shade"></div>
            <div class="container">
                <div class="banner-inner">
                    <div class="breadcrumb-out">
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="#">Home</a></li>
                                <li class="breadcrumb-item"><a href="#">Electric fencing</a></li>
                                <li class="breadcrumb-item active" aria-current="page">Power</li>
                            </ol>
                        </nav>
                    </div>

                    <div class="banner-text-container">
                        <h1>Fence energizers</h1>
                        <p>The Energizer is the heart of your fencing system, chosen and installed correctly it will
                            control all types of animals</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="sub-header-out">
            <div class="container">
                <div class="row">
                    <div class="col-md-8 col-lg-9">
                        <div class="left-section">
                            <h1 class="sub-header-title">What you need to consider</h1>
                            <!-- <ul>
                                <li>Accordion question lorem ipsum?</li>
                                <li>Second accordion item</li>
                                <li>Third accordion item that has a longer copy text that can go on multiple lines</li>
                            </ul> -->

                            <div class="accordion" id="accordionSubHeader">
                                <div class="accordion-card">
                                    <div class="card-header" id="headingOne">
                                        <a href="javascript:void(0)" data-toggle="collapse" data-target="#collapseOne"
                                            aria-expanded="false" aria-controls="collapseOne">
                                            <span>Accordion question lorem ipsum?</span>
                                            <span class="arrow left-align">
                                                <span></span>
                                                <span></span>
                                            </span>
                                        </a>
                                    </div>

                                    <div id="collapseOne" class="collapse" aria-labelledby="headingOne"
                                        data-parent="#accordionSubHeader">
                                        <div class="card-body">
                                            Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry
                                            richardson ad squid.
                                            3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck
                                            quinoa nesciunt
                                            laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it
                                            squid single-origin
                                            coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft
                                            beer labore wes
                                            anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice
                                            lomo. Leggings
                                            occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you
                                            probably haven't heard
                                            of them accusamus labore sustainable VHS.
                                        </div>
                                    </div>
                                </div>
                                <div class="accordion-card">
                                    <div class="card-header" id="headingTwo">
                                        <a href="javascript:void(0)" data-toggle="collapse" data-target="#collapseTwo"
                                            aria-expanded="false" aria-controls="collapseTwo">
                                            <span>Second accordion item</span>
                                            <span class="arrow left-align">
                                                <span></span>
                                                <span></span>
                                            </span>
                                        </a>
                                    </div>
                                    <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo"
                                        data-parent="#accordionSubHeader">
                                        <div class="card-body">
                                            Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry
                                            richardson ad squid.
                                            3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck
                                            quinoa nesciunt
                                            laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it
                                            squid single-origin
                                            coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft
                                            beer labore wes
                                            anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice
                                            lomo. Leggings
                                            occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you
                                            probably haven't heard
                                            of them accusamus labore sustainable VHS.
                                        </div>
                                    </div>
                                </div>
                                <div class="accordion-card">
                                    <div class="card-header" id="headingThree">
                                        <a href="javascript:void(0)" data-toggle="collapse" data-target="#collapseThree"
                                            aria-expanded="false" aria-controls="collapseThree">
                                            <span>Third accordion item that has a longer copy text that can go on
                                                multiple lines</span>
                                            <span class="arrow left-align">
                                                <span></span>
                                                <span></span>
                                            </span>
                                        </a>
                                    </div>
                                    <div id="collapseThree" class="collapse" aria-labelledby="headingThree"
                                        data-parent="#accordionSubHeader">
                                        <div class="card-body">
                                            Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry
                                            richardson ad squid.
                                            3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck
                                            quinoa nesciunt
                                            laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it
                                            squid single-origin
                                            coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft
                                            beer labore wes
                                            anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice
                                            lomo. Leggings
                                            occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you
                                            probably haven't heard
                                            of them accusamus labore sustainable VHS.
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 col-lg-3">
                        <div class="right-section">
                            <div class="right-section-content">
                                Need some help figuring out the right energizer for your needs?
                            </div>

                            <button type="button" class="btn btn-highlight btn-block">Use our energizer
                                selector</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="product-list-container-out">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 d-none d-lg-block">
                        <div class="product-list-sidebar-out">
                            <div class="sidebar-filter-section">


                            </div>

                            <div class="sidebar-section">
                                <h4 class="sidebar-section-header">Category</h4>
                                <div class="sidebar-section-container">
                                    <ul>
                                        <li class="active">
                                            <a href="javascript:void(0)">
                                                All
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Mains fence
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Multi-powered fence
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Solar
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Battery
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>

                            <!-- Filter by animal type -->
                            <div class="sidebar-section">
                                <h4 class="sidebar-section-header with-link">
                                    <a data-toggle="collapse" href="#animalType" role="button" aria-expanded="false"
                                        aria-controls="animalType">
                                        Filter by animal type

                                        <span class="down-arrow-icon">
                                            <svg>
                                                <use xlink:href="img/gallagher-icons.svg#arrow-down" />
                                            </svg>
                                        </span>
                                    </a>
                                </h4>
                                <div class="sidebar-section-container collapse" id="animalType">
                                    <ul>
                                        <li>
                                            <a href="javascript:void(0)">
                                                All
                                            </a>
                                        </li>
                                        <li class="active">
                                            <a href="javascript:void(0)">
                                                Cattle
                                            </a>
                                        </li>
                                        <li class="active">
                                            <a href="javascript:void(0)">
                                                Sheep
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Horse
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Deer
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Goat
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Other
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>

                            <!-- Price -->
                            <div class="sidebar-section">
                                <h4 class="sidebar-section-header with-link">
                                    <a data-toggle="collapse" href="#price" role="button" aria-expanded="false"
                                        aria-controls="price">
                                        Price

                                        <span class="down-arrow-icon">
                                            <svg>
                                                <use xlink:href="img/gallagher-icons.svg#arrow-down" />
                                            </svg>
                                        </span>
                                    </a>
                                </h4>
                                <div class="sidebar-section-container collapse" id="price">
                                    <ul>
                                        <li>
                                            <a href="javascript:void(0)">
                                                All
                                            </a>
                                        </li>
                                        <li class="active">
                                            <a href="javascript:void(0)">
                                                Mains fence
                                            </a>
                                        </li>
                                        <li class="active">
                                            <a href="javascript:void(0)">
                                                Multi-powered fence
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Solar
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Battery
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>

                            <!-- Filter by area -->
                            <div class="sidebar-section">
                                <h4 class="sidebar-section-header with-link">
                                    <a data-toggle="collapse" href="#area" role="button" aria-expanded="false"
                                        aria-controls="area">
                                        Filter by area

                                        <span class="down-arrow-icon">
                                            <svg>
                                                <use xlink:href="img/gallagher-icons.svg#arrow-down" />
                                            </svg>
                                        </span>
                                    </a>
                                </h4>
                                <div class="sidebar-section-container collapse" id="area">
                                    <ul>
                                        <li>
                                            <a href="javascript:void(0)">
                                                All
                                            </a>
                                        </li>
                                        <li class="active">
                                            <a href="javascript:void(0)">
                                                Mains fence
                                            </a>
                                        </li>
                                        <li class="active">
                                            <a href="javascript:void(0)">
                                                Multi-powered fence
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Solar
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Battery
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>

                            <!-- Filter by fence length -->
                            <div class="sidebar-section">
                                <h4 class="sidebar-section-header with-link">
                                    <a data-toggle="collapse" href="#fenceLength" role="button" aria-expanded="false"
                                        aria-controls="fenceLength">
                                        Filter by fence length

                                        <span class="down-arrow-icon">
                                            <svg>
                                                <use xlink:href="img/gallagher-icons.svg#arrow-down" />
                                            </svg>
                                        </span>
                                    </a>
                                </h4>
                                <div class="sidebar-section-container collapse" id="fenceLength">
                                    <ul>
                                        <li>
                                            <a href="javascript:void(0)">
                                                All
                                            </a>
                                        </li>
                                        <li class="active">
                                            <a href="javascript:void(0)">
                                                Mains fence
                                            </a>
                                        </li>
                                        <li class="active">
                                            <a href="javascript:void(0)">
                                                Multi-powered fence
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Solar
                                            </a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0)">
                                                Battery
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>

                        </div>
                    </div>
                    <div class="col-lg-9 plr-xs-0">
                        <div class="sorting-section-out d-none d-lg-block">
                            <div class="row align-items-end">
                                <div class="col-6">
                                    <div class="result-found-text">
                                        Found 21 products
                                    </div>
                                </div>
                                <div class="col-6 text-right">
                                    <div class="sort-by-out">
                                        <select class="product-list-sort-by js-example-basic-single" name="sortby">
                                            <option value="0">Sort by name</option>
                                            <option value="1">Option 2</option>
                                            <option value="2">Option 3</option>
                                            <option value="3">Option 4</option>
                                            <option value="4">Option 5</option>
                                            <option value="5">Option 6</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="product-listing-out">
                            <ul>
                                <li class="product-tile">
                                    <div class="row">
                                        <div class="col-4 col-md-12 pr-xs-0">
                                            <div class="product-status">
                                                NEW
                                            </div>
                                            <div class="product-img-box">
                                                <a href="javascript:void(0)">
                                                    <img class="product-image" src="img/product-list-img.png">
                                                </a>
                                            </div>
                                        </div>
                                        <div class="col-8 col-md-12">
                                            <div class="product-name-desc-out">
                                                <div class="product-name">Product name 1234
                                                </div>
                                                <div class="product-id">123456</div>
                                                <div class="product-description">
                                                    <ul>
                                                        <li>
                                                            1.0 Joules stored energy
                                                        </li>
                                                        <li>Farms up to 10 acres</li>
                                                        <li>Unordered list item</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="product-list-footer">
                                        <div class="row">
                                            <div class="col-5">
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/male-icon.png" alt="Male">
                                                </span>
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/female-icon.png" alt="Male">
                                                </span>
                                            </div>
                                            <div class="col-7 text-right">
                                                <div class="currency-text">RRP</div>
                                                <div class="currency-value">$123.00</div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li class="product-tile">
                                    <div class="row">
                                        <div class="col-4 col-md-12 pr-xs-0">
                                            <div class="product-status">
                                                NEW
                                            </div>
                                            <div class="product-img-box">
                                                <a href="javascript:void(0)">
                                                    <img class="product-image" src="img/product-list-img.png">
                                                </a>
                                            </div>
                                        </div>
                                        <div class="col-8 col-md-12">
                                            <div class="product-name-desc-out">
                                                <div class="product-name">Product name Product nameProduct nameProduct
                                                    name 1234
                                                </div>
                                                <div class="product-id">123456</div>
                                                <div class="product-description">
                                                    <ul>
                                                        <li>
                                                            1.0 Joules stored energy
                                                        </li>
                                                        <li>Farms up to 10 acres</li>
                                                        <li>Unordered list item</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="product-list-footer">
                                        <div class="row">
                                            <div class="col-5">
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/male-icon.png" alt="Male">
                                                </span>
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/female-icon.png" alt="Male">
                                                </span>
                                            </div>
                                            <div class="col-7 text-right">
                                                <div class="currency-text">RRP</div>
                                                <div class="currency-value">$123.00</div>
                                            </div>
                                        </div>
                                    </div>
                                </li>

                                <li class="product-tile">
                                    <div class="row">
                                        <div class="col-4 col-md-12 pr-xs-0">
                                            <div class="product-status">
                                                NEW
                                            </div>
                                            <div class="product-img-box">
                                                <a href="javascript:void(0)">
                                                    <img class="product-image" src="img/product-list-img.png">
                                                </a>
                                            </div>
                                        </div>
                                        <div class="col-8 col-md-12">
                                            <div class="product-name-desc-out">
                                                <div class="product-name">Product name Product nameProduct nameProduct
                                                    name 1234
                                                </div>
                                                <div class="product-id">123456</div>
                                                <div class="product-description">
                                                    <ul>
                                                        <li>
                                                            1.0 Joules stored energy
                                                        </li>
                                                        <li>Farms up to 10 acres</li>
                                                        <li>Unordered list item</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="product-list-footer">
                                        <div class="row">
                                            <div class="col-5">
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/male-icon.png" alt="Male">
                                                </span>
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/female-icon.png" alt="Male">
                                                </span>
                                            </div>
                                            <div class="col-7 text-right">
                                                <div class="currency-text">RRP</div>
                                                <div class="currency-value">$123.00</div>
                                            </div>
                                        </div>
                                    </div>
                                </li>

                                <li class="product-tile">
                                    <div class="row">
                                        <div class="col-4 col-md-12 pr-xs-0">
                                            <div class="product-status">
                                                NEW
                                            </div>
                                            <div class="product-img-box">
                                                <a href="javascript:void(0)">
                                                    <img class="product-image" src="img/product-list-img.png">
                                                </a>
                                            </div>
                                        </div>
                                        <div class="col-8 col-md-12">
                                            <div class="product-name-desc-out">
                                                <div class="product-name">Product name Product nameProduct nameProduct
                                                    name 1234
                                                </div>
                                                <div class="product-id">123456</div>
                                                <div class="product-description">
                                                    <ul>
                                                        <li>
                                                            1.0 Joules stored energy
                                                        </li>
                                                        <li>Farms up to 10 acres</li>
                                                        <li>Unordered list item</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="product-list-footer">
                                        <div class="row">
                                            <div class="col-5">
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/male-icon.png" alt="Male">
                                                </span>
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/female-icon.png" alt="Male">
                                                </span>
                                            </div>
                                            <div class="col-7 text-right">
                                                <div class="currency-text">RRP</div>
                                                <div class="currency-value">$123.00</div>
                                            </div>
                                        </div>
                                    </div>
                                </li>

                                <li class="product-tile">
                                    <div class="row">
                                        <div class="col-4 col-md-12 pr-xs-0">
                                            <div class="product-status">
                                                NEW
                                            </div>
                                            <div class="product-img-box">
                                                <a href="javascript:void(0)">
                                                    <img class="product-image" src="img/product-list-img.png">
                                                </a>
                                            </div>
                                        </div>
                                        <div class="col-8 col-md-12">
                                            <div class="product-name-desc-out">
                                                <div class="product-name">Product name Product nameProduct nameProduct
                                                    name 1234
                                                </div>
                                                <div class="product-id">123456</div>
                                                <div class="product-description">
                                                    <ul>
                                                        <li>
                                                            1.0 Joules stored energy
                                                        </li>
                                                        <li>Farms up to 10 acres</li>
                                                        <li>Unordered list item</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="product-list-footer">
                                        <div class="row">
                                            <div class="col-5">
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/male-icon.png" alt="Male">
                                                </span>
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/female-icon.png" alt="Male">
                                                </span>
                                            </div>
                                            <div class="col-7 text-right">
                                                <div class="currency-text">RRP</div>
                                                <div class="currency-value">$123.00</div>
                                            </div>
                                        </div>
                                    </div>
                                </li>

                                <li class="product-tile">
                                    <div class="row">
                                        <div class="col-4 col-md-12 pr-xs-0">
                                            <div class="product-status">
                                                NEW
                                            </div>
                                            <div class="product-img-box">
                                                <a href="javascript:void(0)">
                                                    <img class="product-image" src="img/product-list-img.png">
                                                </a>
                                            </div>
                                        </div>
                                        <div class="col-8 col-md-12">
                                            <div class="product-name-desc-out">
                                                <div class="product-name">Product name Product nameProduct nameProduct
                                                    name 1234
                                                </div>
                                                <div class="product-id">123456</div>
                                                <div class="product-description">
                                                    <ul>
                                                        <li>
                                                            1.0 Joules stored energy
                                                        </li>
                                                        <li>Farms up to 10 acres</li>
                                                        <li>Unordered list item</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="product-list-footer">
                                        <div class="row">
                                            <div class="col-5">
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/male-icon.png" alt="Male">
                                                </span>
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/female-icon.png" alt="Male">
                                                </span>
                                            </div>
                                            <div class="col-7 text-right">
                                                <div class="currency-text">RRP</div>
                                                <div class="currency-value">$123.00</div>
                                            </div>
                                        </div>
                                    </div>
                                </li>

                                <li class="product-tile">
                                    <div class="row">
                                        <div class="col-4 col-md-12 pr-xs-0">
                                            <div class="product-status">
                                                NEW
                                            </div>
                                            <div class="product-img-box">
                                                <a href="javascript:void(0)">
                                                    <img class="product-image" src="img/product-list-img.png">
                                                </a>
                                            </div>
                                        </div>
                                        <div class="col-8 col-md-12">
                                            <div class="product-name-desc-out">
                                                <div class="product-name">Product name Product nameProduct nameProduct
                                                    name 1234
                                                </div>
                                                <div class="product-id">123456</div>
                                                <div class="product-description">
                                                    <ul>
                                                        <li>
                                                            1.0 Joules stored energy
                                                        </li>
                                                        <li>Farms up to 10 acres</li>
                                                        <li>Unordered list item</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="product-list-footer">
                                        <div class="row">
                                            <div class="col-5">
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/male-icon.png" alt="Male">
                                                </span>
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/female-icon.png" alt="Male">
                                                </span>
                                            </div>
                                            <div class="col-7 text-right">
                                                <div class="currency-text">RRP</div>
                                                <div class="currency-value">$123.00</div>
                                            </div>
                                        </div>
                                    </div>
                                </li>

                                <li class="product-tile">
                                    <div class="row">
                                        <div class="col-4 col-md-12 pr-xs-0">
                                            <div class="product-status">
                                                NEW
                                            </div>
                                            <div class="product-img-box">
                                                <a href="javascript:void(0)">
                                                    <img class="product-image" src="img/product-list-img.png">
                                                </a>
                                            </div>
                                        </div>
                                        <div class="col-8 col-md-12">
                                            <div class="product-name-desc-out">
                                                <div class="product-name">Product name Product nameProduct nameProduct
                                                    name 1234
                                                </div>
                                                <div class="product-id">123456</div>
                                                <div class="product-description">
                                                    <ul>
                                                        <li>
                                                            1.0 Joules stored energy
                                                        </li>
                                                        <li>Farms up to 10 acres</li>
                                                        <li>Unordered list item</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="product-list-footer">
                                        <div class="row">
                                            <div class="col-5">
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/male-icon.png" alt="Male">
                                                </span>
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/female-icon.png" alt="Male">
                                                </span>
                                            </div>
                                            <div class="col-7 text-right">
                                                <div class="currency-text">RRP</div>
                                                <div class="currency-value">$123.00</div>
                                            </div>
                                        </div>
                                    </div>
                                </li>

                                <li class="product-tile">
                                    <div class="row">
                                        <div class="col-4 col-md-12 pr-xs-0">
                                            <div class="product-status">
                                                NEW
                                            </div>
                                            <div class="product-img-box">
                                                <a href="javascript:void(0)">
                                                    <img class="product-image" src="img/product-list-img.png">
                                                </a>
                                            </div>
                                        </div>
                                        <div class="col-8 col-md-12">
                                            <div class="product-name-desc-out">
                                                <div class="product-name">Product name Product nameProduct nameProduct
                                                    name 1234
                                                </div>
                                                <div class="product-id">123456</div>
                                                <div class="product-description">
                                                    <ul>
                                                        <li>
                                                            1.0 Joules stored energy
                                                        </li>
                                                        <li>Farms up to 10 acres</li>
                                                        <li>Unordered list item</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="product-list-footer">
                                        <div class="row">
                                            <div class="col-5">
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/male-icon.png" alt="Male">
                                                </span>
                                                <span class="product-list-footer-left-icon">
                                                    <img src="img/female-icon.png" alt="Male">
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
                    </div>
                </div>
            </div>
        </div>
    </div>

</template:page>