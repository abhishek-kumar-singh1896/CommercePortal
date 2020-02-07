ACC.header = {

	_autoload: [
	    "clickOnNav"
	],

	clickOnNav: function(){
		$(document).ready(function () {
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