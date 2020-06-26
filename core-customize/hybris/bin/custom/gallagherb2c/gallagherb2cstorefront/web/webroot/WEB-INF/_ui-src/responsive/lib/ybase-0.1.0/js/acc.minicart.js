ACC.minicart = {
	
	_autoload: [
		"bindMiniCart"
	],

	bindMiniCart: function(){

		$(document).on("click",".js-mini-cart-link", function(e){
			e.preventDefault();
			e.stopPropagation();
			var cartUrl = $(this).data("miniCartUrl");
			var cartName = ($(this).find(".js-mini-cart-count").html() != 0) ? $(this).data("miniCartName"):$(this).data("miniCartEmptyName");
			var _this = $(this);
			var minicart = {
				url :cartUrl,
				type:'GET',
				success:function(data){
					$('.mini-cart').html(data);

					_this.parent().toggleClass('show');
					_this.siblings('.mini-cart').toggleClass('show');
				}
			};
			$.ajax(minicart);
			
		});
		$(document).on("click",".main-header-out .header-right-btn-group .dropdown-menu", function(e){
			e.stopPropagation();
		});
		$(document).on("click","body", function(e){
			$('.main-header-out .dropdown-menu').removeClass('show');
			$('.main-header-out .btn-group').removeClass('show');
		});

		$(document).on("click",".js-mini-cart-close-button", function(e){
			e.preventDefault();
			ACC.colorbox.close();
		});
	},

	updateMiniCartDisplay: function(){
		var cartItems = $(".js-mini-cart-link").data("miniCartItemsText");
		var miniCartRefreshUrl = $(".js-mini-cart-link").data("miniCartRefreshUrl");
		$.ajax({
			url: miniCartRefreshUrl,
			cache: false,
			type: 'GET',
			dataType: 'json',
			success: function(jsonData){
				var $cartItems = $("<span>").addClass("items-desktop hidden-xs hidden-sm").text(" " + cartItems);
				var $numberItem = $("<span>").addClass("info-text").text(jsonData.miniCartCount).append($cartItems);
				$(".js-mini-cart-link .js-mini-cart-price").empty();
                $(".js-mini-cart-link .js-mini-cart-count").append($numberItem);
                $(".js-mini-cart-link .js-mini-cart-price").text(jsonData.miniCartPrice);    
				$(".info-number").empty();
				if(jsonData.miniCartCount > 9 ){
					$(".info-number").append("9+");
					$(".info-number").removeAttr('hidden');
					}else{
						$(".info-number").append(jsonData.miniCartCount);
						$(".info-number").removeAttr('hidden');
					}
			}
		});
	}

};
