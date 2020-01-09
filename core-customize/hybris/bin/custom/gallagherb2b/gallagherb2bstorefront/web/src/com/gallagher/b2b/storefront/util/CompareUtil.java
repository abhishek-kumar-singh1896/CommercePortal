package com.gallagher.b2b.storefront.util;

import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import org.springframework.ui.Model;

import java.util.List;

public class CompareUtil {
	
	 public static void checkComparedProducts(List<ProductData> productComparisonList, Model model) {
	    	Object searchPageData = model.asMap().get("searchPageData");
			if(searchPageData != null){
				ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> productCategorySearchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) searchPageData;
				populateSelectedForCompare(productCategorySearchPageData, productComparisonList);
			}
			
		}
	    
	    /**
		 * @param searchPageData
		 * @param comparedProducts
		 */
		private static  void populateSelectedForCompare(
				final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData,
				final Object comparedProducts)
		{
			if (searchPageData == null || searchPageData.getResults() == null || comparedProducts == null
					|| !(comparedProducts instanceof List<?>))
			{
				return;
			}

			final List<ProductData> sessionCompareList = (List<ProductData>) comparedProducts;
			for (final ProductData productData : searchPageData.getResults())
			{
				productData.setSelectedForCompare(productIsSelectedForCompare(productData.getCode(), sessionCompareList));
			}
		}


		/**
		 * @param code
		 *           current product code to check
		 * @param source
		 *           Products that have been selected for compare and added to the session.
		 * @return true if product code to check is part of the session
		 */
		private static boolean productIsSelectedForCompare(final String codeToCheck, final List<ProductData> source)
		{
			for (final ProductData productData : source)
			{
				if (productData.getCode().equals(codeToCheck))
				{
					return true;
				}
			}
			return false;
		}
}
