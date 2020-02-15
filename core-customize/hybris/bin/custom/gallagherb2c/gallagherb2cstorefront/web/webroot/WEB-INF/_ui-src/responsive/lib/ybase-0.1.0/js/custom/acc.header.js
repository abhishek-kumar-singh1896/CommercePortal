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
	                // $('.search-result-out').addClass('d-none').removeClass('fadeInUp');
	                // if ($('.search-link').hasClass('active')) {
	                //     $('.search-link').removeClass('active');
	                // }

	                hideMegaMenu();
	            });

	            $(".country-select .dropdown-menu a").click(function () {
	                var selText = $(this).text();
	                $(this).parents().find('.dropdown-toggle .country-text').html(selText);
	            });


	            $('#solutionTab a').on('click', function (e) {
	                e.preventDefault()
	                $(this).tab('show')
	            });

	            $('#productsTab a').on('click', function (e) {
	                e.preventDefault()
	                $(this).tab('show')
	        });
            $('.search-link').click(function () {
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