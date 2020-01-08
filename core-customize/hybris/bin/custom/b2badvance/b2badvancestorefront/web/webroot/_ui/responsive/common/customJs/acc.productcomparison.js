function pcUpdateComparableState(code, url, cb, compareBtnLabel) {
	//console.log(cb.checked);
    if (code != '') {
    	var selectUrl = url + (cb.checked ? '/add' : '/cbremove');
    	$.postJSON(selectUrl, {
            code: code
        }, function(data) {
        	pcUpdateDisableAttribuite(compareBtnLabel, data);
    	});
    }
}

function pcUpdateDisableAttribuite(compareBtnLabel, data) {
	$('.compareBtn').each(function () {
		this.textContent = compareBtnLabel + ' (' + data + ')';
		if (data < 2) {
			$(this).attr('disabled', 'disabled');
		} else {
			$(this).removeAttr("disabled");
		}
	})
}

function pcShowComparePage(url, openPopup, closePopupAfterAddToCart) {
	if (openPopup) {
		//$.colorbox.close();
		var close = closePopupAfterAddToCart ? '&close=true' : '';
		var heading = $("#compareHeading").val();
		$.get(url + 'compare/list?popup=true' + close).done(function(data) {
			$.colorbox({
				html: data,
				opacity:"0.7", 
				width: "auto",
				maxWidth:"100%",
				close:'<span class="glyphicon glyphicon-remove"></span>',
				scrolling: "false",
				title: '<div class="headline"><span class="headline-text">' + heading + '</span></div>',
				onComplete: function ()
				{
					pcUpdateDiffRows();
					pcUpdateAddCartButton();
					$(this).colorbox.resize();
				}
			});
		});
		
	} else {
		location.href = url + 'comparison';
	}
}

function pcRemoveProduct(code, url, openPopup) {
	var selectUrl = url + 'compare/cbremove';
	$.postJSON(selectUrl, {
		code : code
	}, function(data) {
		if (openPopup) {
			var compareBtnLabel = document.getElementById("compareBtnLabel").value;
			pcUpdateDisableAttribuite(compareBtnLabel, data)
		}
	});
	pcShowComparePage(url, openPopup, false);
}

function cpAddToCart(pcPopupClose, url, productCode) {
	var qty = $("#qty_" + productCode).val();
	$.postJSON(url, {
		productCodePost: productCode,
		qty: qty 
    }, function(data) {
    	$('#rollover_cart_popup').html(data.cartPopupHtml);
		if (pcPopupClose) {
			$.colorbox.close();
		}

		ACC.cartpopup.refreshMiniCart();
		$('#rollover_cart_popup').fadeIn();
	});	
}

function pcUpdateDiffRows() {
	$('.product_compare .middle table').each(function () {
		$(this).find('tr').each(function () {
			var diffFound = false;
			var tmp = null;
			$(this).find('td').each(function () {
				var tdText = $(this).text();
				if (tdText == null || tdText == '') {
					$(this).html('-');
				}
				if (tmp == null) {
					tmp = $(this);
				} else {
					if (tmp.text() != $(this).text()) {
						diffFound = true;
					}
				}
			});
			if (diffFound) {
				$(this).addClass( 'diff' );
				/*$(this).find('th').first().addClass( 'thDiff' );*/
			}
		});
	});
	
}

function pcClearList(url, openPopup) {
	$('#pcClearBtnId').attr("disabled", "disabled");		
	var clearListUrl = url + 'compare/clear';
	$.postJSON(clearListUrl, {}, null).always(function() {
		if (openPopup) {
			$.colorbox.close();
			var compareBtnLabel = document.getElementById("compareBtnLabel").value;
			pcUpdateDisableAttribuite(compareBtnLabel, '0')
		} else {
			location.href = url + 'comparison';
		}
	});
}

function pcUpdateAddCartButton() {
	$('.js-enable-btn').each(function () {
        if (!($(this).hasClass('outOfStock') || $(this).hasClass('out-of-stock'))) {
            $(this).removeAttr("disabled");
        }
    });
	 var addToCartForm = $('.add_to_cart_form');
     addToCartForm.ajaxForm({success: ACC.product.displayAddToCartPopup});
};


