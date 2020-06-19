package com.gallagher.core.sitemap.generator.impl;

import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GallagherProductPageSiteMapGenerator extends GallagherAbstractSiteMapGenerator<ProductModel>
{

	@Override
	public List<SiteMapUrlData> getSiteMapUrlData(final List<ProductModel> models)
	{
		return Converters.convertAll(models, getSiteMapUrlDataConverter());
	}

	@Override
	protected List<ProductModel> getDataInternal(final CMSSiteModel siteModel)
	{
		final String query = "SELECT {p.pk} FROM {Product AS p JOIN CatalogVersion AS cv ON {p.catalogVersion}={cv.pk} "
				+ " JOIN Catalog AS cat ON {cv.pk}={cat.activeCatalogVersion} "
				+ " JOIN CMSSite AS site ON {cat.pk}={site.defaultCatalog}}  WHERE {site.pk} = ?site"
				+ " AND {p.approvalStatus} = ?approvalStatus";

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("site", siteModel);
		params.put("approvalStatus", ArticleApprovalStatus.APPROVED);
		return doSearch(query, params, ProductModel.class);
	}
}
