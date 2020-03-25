ACC.productDetails = {

	_autoload: [
	    "clickOnTab"
	],

	clickOnTab: function(){
			$(document).ready(function () {
				/*$('.write-review-link').click(function(){
					$('.tab-review .write-review').toggle();
				});*/
				
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
	            
	            // Product details sub tab fixed and scroll functionality
	            var subTabOut = $('.product-detail-tab').offset();
	            // console.log(subTabOut.top);

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
	            
	         // Product variant selection
	            $('.product-variant-section ul li a').click(function () {
	                $(this).closest('ul').find('.variant-checkbox-icon svg').addClass('d-none');
	                $(this).closest('ul').find('li').removeClass('active');
	                $(this).parent().addClass('active');
	                $(this).find('.variant-checkbox-icon svg').removeClass('d-none');
	            });
	            
	            $('.write-a-review-link').click(function(){
	                $(this).toggleClass('expanded');
	                $('.write-a-review-container').toggleClass('d-none fadeInUp');

	            });
	            
	         // The video slider
	            $('#productDetailVideoCarousel').flexslider({
	                animation: "slide",
	                controlNav: false,
	                animationLoop: false,
	                slideshow: false,
	                itemWidth: 171,
	                itemMargin: 10,
	                directionNav: false,
	                asNavFor: '#productDetailVideoSlider'
	            });

	            $('#productDetailVideoSlider').flexslider({
	                animation: "slide",
	                controlNav: false,
	                animationLoop: false,
	                slideshow: false,
	                touch: true,
	                sync: "#productDetailVideoCarousel"
	            });
	            
	            $('#awardSlider').flexslider({
	                animation: "slide",
	                controlNav: false,
	                animationLoop: false,
	                slideshow: false,
	                touch: true,
	                // itemWidth: 171,
	                // itemMargin: 12,
	                directionNav: true,
	            });

        });
			
			 $(window).on("load", function () {
		            // The slider being synced must be initialized first
		            $('#productDetailMainCarousel').flexslider({
		                animation: "slide",
		                controlNav: false,
		                animationLoop: false,
		                slideshow: false,
		                itemWidth: 171,
		                itemMargin: 8,
		                directionNav: false,
		                asNavFor: '#productDetailMainSlider'
		            });

		            $('#productDetailMainSlider').flexslider({
		                animation: "slide",
		                controlNav: false,
		                animationLoop: false,
		                slideshow: false,
		                touch: true,
		                sync: "#productDetailMainCarousel"
		            });
		        });
			 
			 $('#productTileOut1').flexslider({
	                animation: "slide",
	                controlNav: false,
	                animationLoop: false,
	                slideshow: false,
	                itemWidth: 340,
	                itemMargin: 15,
	                minItem: 2,
	                touch: true,
	                directionNav: true,
	            });

	            $('#productTileOut2').flexslider({
	                animation: "slide",
	                controlNav: false,
	                animationLoop: false,
	                slideshow: false,
	                itemWidth: 340,
	                itemMargin: 15,
	                minItem: 2,
	                touch: true,
	                directionNav: true,
	            });
	            
	            $('.see-review-link').click(function () {
	                $(this).toggleClass('show-less');

	                let toalReviewCount = $('.review-list-out ul li').length;

	                if ($(this).hasClass('show-less')) {
	                    $(this).text('Show less review');
	                    $('.review-list-out ul li').removeClass('d-none');
	                    if (toalReviewCount > 10) {
	                        $('.review-list-out').addClass('scrollable');
	                    }
	                } else {
	                    $(this).text('See all review');
	                    $('.review-list-out').removeClass('scrollable');
	                    $('.review-list-out ul li').addClass('d-none');
	                    $('.review-list-out ul li').eq(1).removeClass('d-none');
	                    $('.review-list-out ul li').eq(2).removeClass('d-none');
	                }
	            });
	}
};