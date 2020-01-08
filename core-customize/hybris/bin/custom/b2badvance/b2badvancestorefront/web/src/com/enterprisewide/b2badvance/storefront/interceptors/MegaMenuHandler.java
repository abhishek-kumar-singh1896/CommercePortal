/**
 *
 */
package com.enterprisewide.b2badvance.storefront.interceptors;

import de.hybris.platform.acceleratorcms.component.slot.CMSPageSlotComponentService;
import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.enterprisewide.b2badvance.cms.model.B2badvanceMegaMenuComponentModel;
import com.enterprisewide.b2badvance.cms.model.B2badvanceMegaMenuItemComponentModel;
import com.enterprisewide.b2badvance.core.cache.B2badvanceManualCacheRegion;
import com.enterprisewide.b2badvance.storefront.data.MegaMenuData;


/**
 * @author Enterprise Wide
 *
 */
public class MegaMenuHandler implements BeforeViewHandler
{
	private static final Logger LOG = Logger.getLogger(MegaMenuHandler.class);

	public static final String SLOTS_MODEL = "slots";
	public static final String MEGAMENU_ID = "B2badvanceMegaMenu";

	@Resource(name = "defaultCMSPageSlotComponentService")
	CMSPageSlotComponentService cmsPageSlotComponentService;

	@Resource(name = "categoryCacheRegion")
	private B2badvanceManualCacheRegion cacheRegion;


	@Autowired
	private CommerceCategoryService commerceCategoryService;

	@Autowired
	private Converter<CategoryModel, CategoryData> categoryConverter;


	MegaMenuData megaMenuData;

	private MegaMenuData getMegaMenuDataInstance()
	{
		if (megaMenuData == null)
		{
			return new MegaMenuData();
		}
		return megaMenuData;
	}

	/*
	 * @description this method is intended to populate mega menu data before view on the basis of the category model..
	 *
	 * @see au.com.b2badvance.storefront.interceptors.BeforeViewHandler#beforeView(javax.servlet.http.HttpServletRequest ,
	 * javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{

		final B2badvanceMegaMenuComponentModel megaMenuComponentModel = (B2badvanceMegaMenuComponentModel) cmsPageSlotComponentService
				.getComponentForId(MEGAMENU_ID);

		if (megaMenuComponentModel != null)
		{
			final Map<String, MegaMenuData> menuMap = new HashMap<String, MegaMenuData>();
			for (final AbstractCMSComponentModel menuItem : megaMenuComponentModel.getMenuItems())
			{
				if (menuItem instanceof B2badvanceMegaMenuItemComponentModel)
				{
					final String categoryCode = ((B2badvanceMegaMenuItemComponentModel) menuItem).getCategoryCode();

					try
					{
						if (cacheRegion.get(categoryCode) != null)
						{
							menuMap.put(categoryCode, (MegaMenuData) cacheRegion.get(categoryCode));
						}
						else
						{
							final MegaMenuData megaMenuData = getMegaMenuData(menuItem, categoryCode);
							menuMap.put(categoryCode, megaMenuData);
							cacheRegion.put(categoryCode, megaMenuData);
						}

					}
					catch (final Exception e)
					{
						LOG.error("Category for Code " + categoryCode + " not found !!", e);
					}
				}
			}
			modelAndView.addObject("menuMap", menuMap);
		}
		modelAndView.getModel();
	}

	/**
	 * @param menuItem
	 * @param categoryCode
	 * @return megaMenuData
	 */
	private MegaMenuData getMegaMenuData(final AbstractCMSComponentModel menuItem, final String categoryCode)
	{
		final MegaMenuData megaMenuData = getCachedCategoryData(menuItem, categoryCode);
		return megaMenuData;
	}

	/**
	 * `
	 *
	 * @param menuItem
	 * @param categoryCode
	 * @return megamenu data
	 *
	 */
	private MegaMenuData getCachedCategoryData(final AbstractCMSComponentModel menuItem, final String categoryCode)
	{
		final MegaMenuData megaMenuData = getMegaMenuDataInstance();
		fillSubCategories(menuItem, megaMenuData);
		return megaMenuData;
	}

	/**
	 * @param menuItem
	 * @param megaMenuData
	 */
	private void fillSubCategories(final AbstractCMSComponentModel menuItem, final MegaMenuData megaMenuData)
	{
		final Map<String, List<CategoryData>> subCategoriesMap = new HashMap<>();

		List<CategoryData> l1SubCategoriesData = null;
		List<CategoryData> l2SubCategoriesData = null;
		List<CategoryData> l3SubCategoriesData = null;
		final String l1CategoryCode = ((B2badvanceMegaMenuItemComponentModel) menuItem).getCategoryCode();

		final CategoryModel categoryModel = getCommerceCategoryService().getCategoryForCode(l1CategoryCode);
		if (categoryModel != null)
		{
			final Collection<CategoryModel> subCategoryModels = categoryModel.getCategories();
			if (CollectionUtils.isNotEmpty(subCategoryModels))
			{
				l1SubCategoriesData = Converters.convertAll(subCategoryModels, getCategoryConverter());
				LOG.debug("Sub-categories not found in cache for category code: " + l1CategoryCode);
			}
		}
		// fill L2 Categories in Map
		if (CollectionUtils.isNotEmpty(l1SubCategoriesData))
		{
			subCategoriesMap.put(l1CategoryCode, l1SubCategoriesData);
			for (final CategoryData subCategory : l1SubCategoriesData)
			{
				final CategoryModel subCategoryModel = getCommerceCategoryService().getCategoryForCode(subCategory.getCode());
				if (subCategoryModel != null)
				{
					final Collection<CategoryModel> l2CategoryModels = subCategoryModel.getCategories();
					if (CollectionUtils.isNotEmpty(l2CategoryModels))
					{
						l2SubCategoriesData = Converters.convertAll(l2CategoryModels, getCategoryConverter());
						LOG.debug("Sub-categories not found in cache for category code: " + l1CategoryCode);
					}
					else
					{
						subCategoriesMap.put(subCategory.getCode(), null);
						l2SubCategoriesData = null;
					}
				}

				if (CollectionUtils.isNotEmpty(l2SubCategoriesData))
				{
					subCategoriesMap.put(subCategory.getCode(), l2SubCategoriesData);
					for (final CategoryData l2Category : l2SubCategoriesData)
					{
						final CategoryModel l2CategoryModel = getCommerceCategoryService().getCategoryForCode(l2Category.getCode());
						if (l2CategoryModel != null)
						{
							final Collection<CategoryModel> l3CategoryModels = l2CategoryModel.getCategories();
							if (CollectionUtils.isNotEmpty(l3CategoryModels))
							{
								l3SubCategoriesData = Converters.convertAll(l3CategoryModels, getCategoryConverter());
								LOG.debug("Sub-categories not found in cache for category code: " + l1CategoryCode);
							}
							else
							{
								subCategoriesMap.put(l2Category.getCode(), null);
								l3SubCategoriesData = null;
							}
							if (CollectionUtils.isNotEmpty(l3SubCategoriesData))
							{
								final CategoryData l2CategoryData = l2Category;
								l2CategoryData.setThirdLevelNode(Boolean.TRUE);
								for (int listSize = 0; listSize < l1SubCategoriesData.size(); listSize++)
								{
									final CategoryData categoryData = l1SubCategoriesData.get(listSize);
									if (categoryData.getCode().equalsIgnoreCase(subCategory.getCode()))
									{
										categoryData.setThirdLevelNode(Boolean.TRUE);
									}

								}
								megaMenuData.setIsThreeLevelHierarchy(Boolean.TRUE);
								subCategoriesMap.put(l2Category.getCode(), l3SubCategoriesData);
							}
							else
							{
								megaMenuData.setIsThreeLevelHierarchy(Boolean.FALSE);
							}

						}
					}

				}

			}

		}
		megaMenuData.setSubCategories(subCategoriesMap);
	}


	/**
	 * @return the commerceCategoryService
	 */
	public CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

	/**
	 * @param commerceCategoryService
	 *           the commerceCategoryService to set
	 */
	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}

	/**
	 * @return the categoryConverter
	 */
	public Converter<CategoryModel, CategoryData> getCategoryConverter()
	{
		return categoryConverter;
	}

	/**
	 * @param categoryConverter
	 *           the categoryConverter to set
	 */
	public void setCategoryConverter(final Converter<CategoryModel, CategoryData> categoryConverter)
	{
		this.categoryConverter = categoryConverter;
	}
}
