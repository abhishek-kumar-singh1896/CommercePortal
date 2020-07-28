ACC.header = {

	_autoload: [
	    "clickOnNav"
	],

	clickOnNav: function(){
		$(document).ready(function () {

			$('.second-level-nav ul li').hover(function () {
				$(this).find('.second-level-link-desktop').addClass('active');
				$(this).find('.dropdown-menu-out').removeClass('d-none').addClass('fadeInUp');
			}, function () {
				$(this).find('.second-level-link-desktop').removeClass('active');
				$(this).find('.dropdown-menu-out').addClass('d-none').removeClass('fadeInUp');
			});

			function removeClasses(className) {
			    var elements = document.getElementsByClassName(className);
			    while(elements.length > 0){
			        elements[0].classList.remove(className);
			    }
				}
				    
				$(document).click(function (e) {
					// For desktop mega menu code
					const $li = $(e.target).closest('li');
					const clickedEvent = $li.hasClass('with-dropdown');
					const $searchLink = $(e.target).closest('.search-link');
					const menu = document.getElementById('product-mega-menu');
					const searchButton = document.getElementById('searchLink1');
					const searchLinkContainer = document.getElementById('searchLink1Container');
					if(null != document.getElementById('dropdownMenuUser'))
					{
						const accountButton = document.getElementById('dropdownMenuUser');
						accountButton.onclick =function() {
							$('.main-nav-out li.first-level').removeClass('active');
							if(menu) {
								menu.classList.remove('menu-button-active');
								removeClasses('mega-menu-active');
							}
					        searchButton.classList.remove('active');
					        searchLinkContainer.classList.add('d-none');
					        $('.megamenu-out').not('.search-link-container').addClass('d-none').removeClass('fadeInDown');
					        $('.search-link-container').toggleClass('fadeInDown');
						}
					}
					if (clickedEvent) {
						let linkId = $li.find('a').attr('id');
						$li.toggleClass('active').siblings().removeClass('active');
						$('.megamenu-out').not('#' + linkId + 'Container').addClass('d-none').removeClass('fadeInDown');
						$('#' + linkId + 'Container').toggleClass('d-none').toggleClass('fadeInDown');
						$('.search-out .search-link').removeClass('active');
						const menuButton = document.getElementById(linkId).parentElement;
						if(menu) {
							menu.classList.toggle('menu-button-active');
							menuButton.classList.toggle('mega-menu-active');
						}
					} else if ($searchLink.length) {
						if(menu) {
							menu.classList.remove('menu-button-active');
							removeClasses('mega-menu-active');
						}
						$searchLink.toggleClass('active');
						$('.main-nav-out li.first-level').removeClass('active');
						$('.megamenu-out').not('.search-link-container').addClass('d-none').removeClass('fadeInDown');
						$('.search-link-container').toggleClass('d-none').toggleClass('fadeInDown');
					} else if (!$(e.target).closest('.megamenu-out').length) {
						$('.main-nav-out li.first-level, .search-out .search-link').removeClass('active');
						$('.megamenu-out').addClass('d-none').removeClass('fadeInDown');
						if(menu) {
							menu.classList.remove('menu-button-active');
							removeClasses('mega-menu-active');
						}
					}
				});

			// For mobile mega menu

			// Level 1 Click
			$('.l1-anchor.with-sublevel').click(function () {
				let l1anchorId = $(this).attr('id');
				let l1anchorText = $(this).parent().find('.with-sublevel-text').text();
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
				let l2anchorText = $(this).parent().find('.with-sublevel-text').text();
				$('.xs-mega-menu').not('#' + l2anchorId + 'Container').addClass('d-none').removeClass('fadeInRightXs');

				$('#' + l2anchorId + 'Container').removeClass('d-none').addClass('fadeInRightXs');
				$('#' + l2anchorId + 'Container').find('.back-text').text(l2anchorText);
			});

			$('.back-to-l2').click(function () {
				$('.xs-main-menu-l3, .xs-main-menu-l2').addClass('d-none').removeClass('fadeInRightXs');
				$('.l2-container-active').removeClass('d-none').addClass('fadeInRightXs');

			});

			// $(".country-select .dropdown-menu a").click(function () {
			// 	var selText = $(this).text();
			// 	$(this).parents().find('.dropdown-toggle .country-text').html(selText);
			// });


			$('#solutionTab a.left-tab').on('click', function (e) {
				e.preventDefault()
				$(this).tab('show')
			});

			$('#productsTab a.left-tab').on('click', function (e) {
				e.preventDefault()
				$(this).tab('show')
			});

			$('.check-product').click(function () {
				$('body').toggleClass('freeze');
				$('.hamburger-icon').toggleClass('active');
				$('.xs-main-menu-l1').removeClass('fadeInRightXs');
				$('#responsiveMegaMenu').toggle().toggleClass('fadeInUp');

			});
			
			$('.hamburger-icon').click(function () {
				$('body').toggleClass('freeze');
				$(this).toggleClass('active');
				$('.xs-main-menu-l1').removeClass('fadeInRightXs');
				$('#responsiveMegaMenu').toggle().toggleClass('fadeInDown');// Prashant
			});

			$('#responsiveMegaMenu .modal-header .close').click(function () {
				$('#responsiveMegaMenu').hide();
			});

			// if($(window).width() >= 1200) {
			// 	$('body').togleClass('freeze');
			// }
			
		});
	}
};