ACC.header = {

	_autoload: [
	    "clickOnNav"
	],

	clickOnNav: function(){
			$(document).ready(function () {
				//navigation menu js start
		            /*$('.global-search').keyup(function () {
		                var globalSearchVal = $('.global-search').val();
		                if (globalSearchVal !== '') {
		                    $('.search-dropdown-out').removeClass('d-none').addClass('fadeInUp');
		                } else {
		                    $('.search-dropdown-out').addClass('d-none').removeClass('fadeInUp');
		                }
		            });*/

		            $(document).click(function (e) {
		                // For desktop mega menu code
		                const $li = $(e.target).closest('li');
		                const clickedEvent = $li.hasClass('with-dropdown');
		                const $searchLink = $(e.target).closest('.search-link');

		                if (clickedEvent) {
		                    let linkId = $li.find('a').attr('id');
		                    $li.toggleClass('active').siblings().removeClass('active');
		                    $('.megamenu-out').not('#' + linkId + 'Container').addClass('d-none').removeClass('fadeInUp');
		                    $('#' + linkId + 'Container').toggleClass('d-none').toggleClass('fadeInUp');
		                    $('.search-out .search-link').removeClass('active');
		                } else if ($searchLink.length) {
		                    $searchLink.toggleClass('active');
		                    $('.main-nav-out li.first-level').removeClass('active');
		                    $('.megamenu-out').not('.search-link-container').addClass('d-none').removeClass('fadeInUp');
		                    $('.search-link-container').toggleClass('d-none').toggleClass('fadeInUp');
		                } else if (!$(e.target).closest('.megamenu-out').length) {
		                    $('.main-nav-out li.first-level, .search-out .search-link').removeClass('active');
		                    $('.megamenu-out').addClass('d-none').removeClass('fadeInUp');
		                }
		            });

		            // For mobile mega menu

		            // Level 1 Click
		            $('.l1-anchor.with-sublevel').click(function () {
		                let l1anchorId = $(this).attr('id');
		                let l1anchorText = $(this).text();
		                $('.xs-mega-menu').not('#' + l1anchorId + 'Container').addClass('d-none').removeClass('fadeInRightXs');
		                $('#' + l1anchorId + 'Container').removeClass('d-none').addClass('fadeInRightXs');
		                $('#' + l1anchorId + 'Container').find('.back-text').text(l1anchorText);

		                let link1containerid = $('#' + l1anchorId + 'Container');
		                link1containerid.addClass('l2-container-active');

		            });

		            $('.back-to-l1').click(function () {
		                $('.xs-main-menu-l2').addClass('d-none').removeClass("fadeInRightXs, l2-container-active");
		                $('.xs-main-menu-l1').removeClass('d-none').addClass('fadeInRightXs');
		            });


		            // Level 2 Click
		            $('.l2-anchor.with-sublevel').click(function () {
		                let l2anchorId = $(this).attr('id');
		                let l2anchorText = $(this).text();
		                $('.xs-mega-menu').not('#' + l2anchorId + 'Container').addClass('d-none').removeClass('fadeInRightXs');

		                $('#' + l2anchorId + 'Container').removeClass('d-none').addClass('fadeInRightXs');
		                $('#' + l2anchorId + 'Container').find('.back-text').text(l2anchorText);
		            });

		            $('.back-to-l2').click(function () {
		                $('.xs-main-menu-l3, .xs-main-menu-l2').addClass('d-none').removeClass('fadeInRightXs');
		                $('.l2-container-active').removeClass('d-none').addClass('fadeInRightXs');

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
		            
		            $('.hamburger-icon').click(function(){
		                $('#responsiveMegaMenu').show();
		            });
		 
		            $('#responsiveMegaMenu .modal-header .close').click(function(){
		                $('#responsiveMegaMenu').hide();
		            });

				
				//navigation menu js end
				
	            /*$('.search-link').click(function () {
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
	            });*/
	
	            $(".country-select .dropdown-menu a").click(function () {
	                var selText = $(this).text();
	                $(this).parents().find('.dropdown-toggle .country-text').html(selText);
	            });
        });
	}
};