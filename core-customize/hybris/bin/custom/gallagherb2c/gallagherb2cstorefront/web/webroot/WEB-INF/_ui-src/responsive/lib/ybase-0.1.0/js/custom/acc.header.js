ACC.header = {

	_autoload: [
	    "clickOnNav"
	],

	clickOnNav: function(){
			$(document).ready(function () {
				//navigation menu js start
				
				$(document).ready(function () {
		            $('.global-search').keyup(function () {
		                var globalSearchVal = $('.global-search').val();
		                if (globalSearchVal !== '') {
		                    $('.search-dropdown-out').removeClass('d-none').addClass('fadeInUp');
		                } else {
		                    $('.search-dropdown-out').addClass('d-none').removeClass('fadeInUp');
		                }
		            });

		            $(document).click(function (e) {
		                const $li = $(e.target).closest('li');
		                const clickedEvent = $li.hasClass('with-dropdown');
		                const $searchLink = $(e.target).closest('.search-link');
		                console.log($searchLink);

		                if (clickedEvent) {
		                    let linkId = $li.find('a').attr('id');
		                    $li.toggleClass('active').siblings().removeClass('active');
		                    $('.megamenu-out').not('#' + linkId + 'Container').addClass('d-none').removeClass('fadeInUp');
		                    $('#' + linkId + 'Container').toggleClass('d-none').toggleClass('fadeInUp');
		                    $('.search-out .search-link').removeClass('active');
		                } else if ($searchLink.length) {
		                    let linkId = $searchLink.attr('id');
		                    console.log(linkId);
		                    $searchLink.toggleClass('active');
		                    $('.main-nav-out li.first-level').removeClass('active');
		                    $('.megamenu-out').not('#' + linkId + 'Container').addClass('d-none').removeClass('fadeInUp');
		                    $('#' + linkId + 'Container').toggleClass('d-none').toggleClass('fadeInUp');
		                } else if (!$(e.target).closest('.megamenu-out').length) {
		                    $('.main-nav-out li.first-level, .search-out .search-link').removeClass('active');
		                    $('.megamenu-out').addClass('d-none').removeClass('fadeInUp');
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

		        });
				
				//navigation menu js end
				
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