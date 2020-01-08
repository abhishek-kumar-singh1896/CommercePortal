/**
 *
 */
package com.enterprisewide.b2badvance.facades.product.populators;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.facades.product.data.B2BAdvanceProductData;


/**
 * Populates B2BAdvanceProductData into product Model
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceProductReversePopulator<SOURCE extends B2BAdvanceProductData, TARGET extends ProductModel>
		implements Populator<SOURCE, TARGET>
{

	private static final String CATALOG_VERSION = "Staged";
	private static final String DELETE = "DELETE";
	private static final String ENABLE = "ENABLE";
	private static final String CHANGE = "CHANGE";

	private String catalog;
	private UnitService unitService;
	private CategoryService categoryService;
	private CatalogVersionService catalogVersionService;
	private BaseSiteService baseSiteService;
	private CatalogVersionModel catalogVersion = null;

	/**
	 * {@inheritDoc}
	 */
	public void populate(final B2BAdvanceProductData source, final ProductModel target) throws ConversionException
	{
		target.setCode(source.getArticleNumber());
		target.setEan(source.getErpArticleNumber());
		target.setApprovalStatus(getApprovalStatus(source.getAction(), target));
		target.setUnit(getUnit(source.getSalesUnit()));
		target.setSummary(source.getProductSummary());
		target.setName(source.getProductName());
		target.setOnlineDate(source.getOnlineFrom());
		target.setOfflineDate(source.getOnlineTo());
		target.setSupercategories(getCategories(source.getCategories()));
		target.setCatalogVersion(getCatalogVersion());
	}

	/**
	 * Returns the list of product categories
	 *
	 * @param categories
	 *           list of category codes
	 * @return collection of category models
	 */
	protected Collection<CategoryModel> getCategories(final List<String> categories)
	{
		final Collection<CategoryModel> superCategories = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(categories))
		{
			categories.forEach(categoryCode -> {
				try
				{
					final CategoryModel category = categoryService.getCategoryForCode(getCatalogVersion(), categoryCode);
					superCategories.add(category);
				}
				catch (UnknownIdentifierException | AmbiguousIdentifierException unIdEx)
				{
					throw new ConversionException(String.format("Invalid Category %s", categoryCode), unIdEx);
				}
			});
		}
		return superCategories;
	}

	/**
	 * Returns the sales unit
	 *
	 * @param salesUnit
	 *           code
	 * @return sales unit
	 */
	private UnitModel getUnit(final String salesUnit)
	{
		UnitModel unit = null;
		try
		{
			unit = getUnitService().getUnitForCode(salesUnit);
		}
		catch (UnknownIdentifierException | AmbiguousIdentifierException unIdEx)
		{
			throw new ConversionException("Invalid Sales Unit", unIdEx);
		}
		return unit;
	}

	/**
	 * Returns approval status based on action. If product already exists and the action is CHANGE then approval status
	 * will be kept as is
	 *
	 * @param action
	 *           to decide the approval status
	 * @param target
	 *           product model to get the existing status
	 * @return approval status
	 */
	private ArticleApprovalStatus getApprovalStatus(final String action, final ProductModel target)
	{
		ArticleApprovalStatus approvalStatus = null;
		switch (action)
		{
			case DELETE:
				approvalStatus = ArticleApprovalStatus.UNAPPROVED;
				break;
			case ENABLE:
				approvalStatus = ArticleApprovalStatus.APPROVED;
				break;
			case CHANGE:
				if (target.getApprovalStatus() == null)
				{
					approvalStatus = ArticleApprovalStatus.CHECK;
				}
				else
				{
					approvalStatus = target.getApprovalStatus();
				}
				break;
			default:
				approvalStatus = ArticleApprovalStatus.CHECK;
				break;
		}
		return approvalStatus;
	}

	/**
	 * Returns the catalog version
	 *
	 * @return CatalogVersionModel the catalogVersion required
	 */
	protected CatalogVersionModel getCatalogVersion()
	{
		if (catalogVersion == null)
		{
			if (baseSiteService.getCurrentBaseSite() == null)
			{
				catalogVersion = catalogVersionService.getCatalogVersion(catalog, CATALOG_VERSION);
			}
			else
			{
				catalogVersion = ((CMSSiteModel) baseSiteService.getCurrentBaseSite()).getDefaultCatalog().getCatalogVersions()
						.stream().filter(catalogVersion -> !catalogVersion.getActive()).findFirst().get();
			}
		}
		return catalogVersion;
	}

	protected String getCatalog()
	{
		return catalog;
	}

	@Required
	public void setCatalog(final String catalog)
	{
		this.catalog = catalog;
	}

	protected UnitService getUnitService()
	{
		return unitService;
	}

	@Required
	public void setUnitService(final UnitService unitService)
	{
		this.unitService = unitService;
	}

	protected CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}
}
