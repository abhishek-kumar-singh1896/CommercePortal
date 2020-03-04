ACC.productDetails = {

	_autoload: [
	    "clickOnTab"
	],

	clickOnTab: function(){
			$(document).ready(function () {
				$('.write-review-link').click(function(){
					$('.tab-review .write-review').toggle();
				});
				
				//navigation menu js start
				var subTabOut = $('.product-detail-tab').offset();

	            $(window).bind('scroll', function () {
	                if ($(window).scrollTop() > subTabOut.top) {
	                    $('.product-detail-tab').addClass('sticky-top');
	                }
	                else {
	                    $('.product-detail-tab').removeClass('sticky-top');
	                }
	            });


	            $("#productDetailTab ul li a[href^='#']").on('click', function (e) {

	                // prevent default anchor click behavior
	                e.preventDefault();

	                // store hash
	                var hash = this.hash;

	                // animate
	                $('html, body').animate({
	                    scrollTop: $(hash).offset().top
	                }, 500, function () {

	                    // when done, add hash to url
	                    // (default click behaviour)
	                    window.location.hash = hash;
	                });

	            });
	            

        });
	}
};