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

		 

		    $(".country-select .dropdown-menu a").click(function () {
		        var selText = $(this).text();
		        $(this).parents().find('.dropdown-toggle .country-text').html(selText);
		        // $(this).parents('.btn-group').find('.dropdown-toggle').html(selText+' <span class="caret"></span>');
		    });
		    
		});
	}
};