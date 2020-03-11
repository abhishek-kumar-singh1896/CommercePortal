ACC.productList = {

	_autoload : [ "productListClick" ],

	productListClick : function() {
		$(document).ready(function() {
			$('.js-example-basic-single').select2({
				minimumResultsForSearch : -1,
				width: '100%',
				placeholder : "---Select---"
			}).on('select2:opening', function() {
				$('.select2-dropdown').slideDown(1000);
				$(this).on("select2:open", function() {
					$('.select2-dropdown').slideDown(1000);

				});
			});
			
			$('#refineFilterLink').click(function(){
                $('#refineFilter').show();
            });
 
            $('#refineFilter .modal-header .close').click(function(){
                $('#refineFilter').hide();
            });
            
            $('#refineFilterSaveBtn').click(function(){
                $('#refineFilter').hide();
            });
            
            $('.refine-btn').click(function(){
                $('#refineFilter').show();
            });

		});
	}
};