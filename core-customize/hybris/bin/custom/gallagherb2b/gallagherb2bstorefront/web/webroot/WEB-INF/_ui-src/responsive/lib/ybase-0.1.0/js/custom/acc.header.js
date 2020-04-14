ACC.header = {

	_autoload: [
	    "clickOnNav"
	],

	clickOnNav: function(){
			$(document).ready(function () {

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

        });
	}
};