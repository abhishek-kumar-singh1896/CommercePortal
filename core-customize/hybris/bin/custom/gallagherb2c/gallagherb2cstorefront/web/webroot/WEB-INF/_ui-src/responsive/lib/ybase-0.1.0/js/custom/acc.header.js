ACC.header = {

	_autoload: [
	    "clickOnNav"
	],

	clickOnNav: function(){
			$(document).ready(function () {
				function hideMegaMenu() {
	                $('#mainNavContainer1, #mainNavContainer2, #mainNavContainer4, .search-result-out').addClass('d-none').removeClass('fadeInUp');
	                $('.main-nav-out ul li, .search-link').removeClass('active');
	            }

	            hideMegaMenu();

	            $("#mainNavLink1").click(function () {
	                hideMegaMenu();
	                $(this).closest('li').toggleClass('active');
	                $('#mainNavContainer1').toggleClass('d-none').toggleClass('fadeInUp');
	            });

	            $("#mainNavLink2").click(function () {
	                hideMegaMenu();
	                $(this).closest('li').toggleClass('active');
	                $('#mainNavContainer2').toggleClass('d-none').toggleClass('fadeInUp');
	            });

	            $("#mainNavLink4").click(function () {
	                hideMegaMenu();
	                $(this).closest('li').toggleClass('active');
	                $('#mainNavContainer4').toggleClass('d-none').toggleClass('fadeInUp');
	            });

	            $('.search-link').click(function () {
	                hideMegaMenu();
	                $(this).toggleClass('active');
	                $('.search-result-out').toggleClass('d-none').toggleClass('fadeInUp');
	                $('.global-search').val('');
	                $('.search-dropdown-out').addClass('d-none').removeClass('fadeInUp');
	            });

	            $('.global-search').keyup(function () {
	                var globalSearchVal = $('.global-search').val();
	                if (globalSearchVal !== '') {
	                    $('.search-dropdown-out').removeClass('d-none').addClass('fadeInUp');
	                } else {
	                    $('.search-dropdown-out').addClass('d-none').removeClass('fadeInUp');
	                }
	            });

	            $('.search-result-out, .search-link, #mainNavContainer1, #mainNavContainer2, #mainNavContainer4, #mainNavLink1, #mainNavLink2, #mainNavLink4').click(function (e) {
	                e.stopPropagation();
	            });

	            $(document).click(function () {
	                hideMegaMenu();
	                var $trigger = $(".megamenu-out");
	                if ($trigger !== event.target && !$trigger.has(event.target).length) {
	                    $(".megamenu-out").addClass('d-none').removeClass('fadeInUp');
	                }
	            });

	            $(".country-select .dropdown-menu a").click(function () {
	                var selText = $(this).text();
	                $(this).parents().find('.dropdown-toggle .country-text').html(selText);
	            });


	            $('#solutionTab a.left-tab').on('click', function (e) {
	                e.preventDefault()
	                $(this).tab('show')
	            });

	            $('#productsTab a.left-tab').on('click', function (e) {
	                e.preventDefault()
	                $(this).tab('show')
	            });


	            function hideXsMenu() {
	                $('.responsive-mega-menu-modal .modal-content').addClass('d-none').removeClass('fadeInRightXs');
	            }
	            $('.xs-main-menu-l1-links ul li.with-sublevel a').click(function () {
	                hideXsMenu();
	                // $('.xs-main-menu-l1').addClass('d-none').removeClass('fadeInRightXs');
	                $('.xs-main-menu-l2').removeClass('d-none').addClass('fadeInRightXs');
	                var currentL1LinkVal = $(this).text();
	                $(this).parents().find('.xs-main-menu-l2 .left-title-out a .back-to-l1').text(currentL1LinkVal);
	            });

	            $('.xs-main-menu-l2 .left-title-out a').click(function () {
	                hideXsMenu();
	                $('.xs-main-menu-l1').removeClass('d-none').addClass('fadeInRightXs');
	                // $('.xs-main-menu-l2').addClass('d-none').removeClass('fadeInRightXs');
	                var currentL1LinkVal = $(this).text();
	                $(this).parents().find('.xs-main-menu-l2 .left-title-out a .back-to-l1').text(currentL1LinkVal);
	            });

	            $('.xs-main-menu-l2-links ul li.with-sublevel a').click(function () {
	                hideXsMenu();
	                // $('.xs-main-menu-l1').addClass('d-none').removeClass('fadeInRightXs');
	                $('.xs-main-menu-l3').removeClass('d-none').addClass('fadeInRightXs');
	                var currentL2LinkVal = $(this).text();
	                $(this).parents().find('.xs-main-menu-l3 .left-title-out a .back-to-l2').text(currentL2LinkVal);
	            });

	            $('.xs-main-menu-l3 .left-title-out a').click(function () {
	                hideXsMenu();
	                $('.xs-main-menu-l2').removeClass('d-none').addClass('fadeInRightXs');
	                var currentL3LinkVal = $(this).text();
	                $(this).parents().find('.xs-main-menu-l3 .left-title-out a .back-to-l2').text(currentL3LinkVal);
	            });
            $('.search-link').click(function () {
            	hideMegaMenu();
                $(this).toggleClass('active');
                $('.search-result-out').toggleClass('d-none').toggleClass('fadeInUp');
                $('.global-search').val('');
                $('.search-dropdown-out').addClass('d-none').removeClass('fadeInUp');
            });


            $('.global-search').keyup(function () {
                var globalSearchVal = $('.global-search').val();
                if (globalSearchVal !== '') {
                    $('.search-dropdown-out').removeClass('d-none').addClass('fadeInUp');
                } else {
                    $('.search-dropdown-out').addClass('d-none').removeClass('fadeInUp');
                }
            });

            $('.search-result-out, .search-link').click(function (e) {
                e.stopPropagation();
            });

            $(document).click(function () {
                $('.search-result-out').addClass('d-none').removeClass('fadeInUp');
                if ($('.search-link').hasClass('active')) {
                    $('.search-link').removeClass('active');
                }
            });

            $(".country-select .dropdown-menu a").click(function () {
                var selText = $(this).text();
                $(this).parents().find('.dropdown-toggle .country-text').html(selText);
            });
        });
	}
};