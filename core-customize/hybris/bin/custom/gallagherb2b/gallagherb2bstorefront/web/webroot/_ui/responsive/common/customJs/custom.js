$(document).ready(function ()
{  		
	 $("#salesForm").change(function() {
	        $(this).submit();
	    });
	 
	enquire.register("screen and (max-width:"+screenSmMax+")", {
        match : function() {

            $(document).on("click",".js-enquire-offcanvas-navigation .js-enquire-has-sub .hasNextLevel .js_nav__link--drill__down",function(e){
            	e.preventDefault();
                $(".js-userAccount-Links").hide();
                $(".js-enquire-offcanvas-navigation ul.js-offcanvas-links").addClass("next-level-active");
                $(".js-enquire-offcanvas-navigation .js-enquire-has-sub .hasNextLevel").removeClass("active");
                $(this).parents(".js-enquire-has-sub").addClass("active");
                $(this).parent(".js-enquire-has-sub .hasNextLevel").addClass("active");
            });


            $(document).on("click",".js-enquire-offcanvas-navigation .js-enquire-sub-close-second-level",function(e){
                e.preventDefault();
               /* $(".js-userAccount-Links").show();*/
                $(".js-enquire-offcanvas-navigation ul.js-offcanvas-links").removeClass("next-level-active");
                $(".js-enquire-offcanvas-navigation .js-enquire-has-sub .hasNextLevel").removeClass("active");
            });
            
            divRemoved = $(".toBeRemovedOnMobile").html();
            $(".toBeRemovedOnMobile").remove();
            configureAutocomplete();


        },

        unmatch : function() {

            $(".js-enquire-offcanvas-navigation > ul.js-offcanvas-links").removeClass("next-level-active");
            $(this).parents(".js-enquire-has-sub").removeClass("active");
            $(".js-enquire-offcanvas-navigation .js-enquire-has-sub .hasNextLevel").removeClass("active");

            $(document).off("click",".js-enquire-offcanvas-navigation .js-enquire-has-sub .hasNextLevel .js_nav__link--drill__down");
            $(document).off("click",".js-enquire-offcanvas-navigation .js-enquire-sub-close-second-level");


        }

    });

});

$('.bulkOrderNavLink').click(function() {
	var $this = $(this).parent("li");
	
	if(!$('.bulkOrderNavDiv').is(':visible')) {
		itemWidth = $this.width();
		var $subNav = $this.find('.bulkOrderNavDiv'),
		subNavWidth = $subNav.outerWidth();
		var $mainNav = $('.js_navigation--bottom'),
		mainNavWidth = $mainNav.width();

		// get the left position for sub-navigation to be centered under each <li>
		var leftPos = $this.position().left + itemWidth / 2 - subNavWidth / 2;
		// get the top position for sub-navigation. this is usually the height of the <li> unless there is more than one row of <li>
		var topPos = $this.position().top + $this.height();

		if (leftPos > 0 && leftPos + subNavWidth < mainNavWidth) {
			// .sub-navigation is within bounds of the .main-navigation
			$subNav.css({
				"left": leftPos,
				"top": topPos,
				"right": "auto"
			});
		} else if (leftPos < 0) {
			// .suv-navigation can't be centered under the <li> because it would exceed the .main-navigation on the left side
			$subNav.css({
				"left": 0,
				"top": topPos,
				"right": "auto"
			});
		} else if (leftPos + subNavWidth > mainNavWidth) {
			// .suv-navigation can't be centered under the <li> because it would exceed the .main-navigation on the right side
			$subNav.css({
				"right": 0,
				"top": topPos,
				"left": "auto"
			});
		}
		$this.addClass("show-sub");
	}
	else {
		$this.removeClass("show-sub");
	}
	
    $('.bulkOrderNavDiv').toggle();
     
});

