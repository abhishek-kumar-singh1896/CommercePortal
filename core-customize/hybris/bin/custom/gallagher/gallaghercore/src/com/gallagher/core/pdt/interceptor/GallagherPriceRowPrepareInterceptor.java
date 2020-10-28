/**
 *
 */
package com.gallagher.core.pdt.interceptor;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.apache.commons.lang.StringUtils;


/**
 * The GallagherPriceRowPrepareInterceptor class. Handles the Sales Area based Pricing functionalities.
 *
 * @author Nagarro-Dev
 *
 */
public class GallagherPriceRowPrepareInterceptor extends GallagherPDTRowPrepareInterceptor
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof PriceRowModel)
		{
			final PriceRowModel prModel = (PriceRowModel) model;

			if (prModel.getUnit() == null)
			{
				final UnitModel fallbackUnit = (prModel.getProduct() == null) ? null : prModel.getProduct().getUnit();
				if (fallbackUnit == null)
				{
					throw new InterceptorException("missing unit for price row ");
				}
				else
				{
					prModel.setUnit(fallbackUnit);
				}
			}

			super.onPrepare(prModel, ctx);

			if (ctx.isNew(model) || ctx.isModified(model, PriceRowModel.PRODUCT) || ctx.isModified(model, PriceRowModel.PG)
					|| ctx.isModified(model, PriceRowModel.USER) || ctx.isModified(model, PriceRowModel.UG)
					|| ctx.isModified(model, PriceRowModel.PRODUCTID) || ctx.isModified(model, PriceRowModel.SALESAREA))
			{
				updateMatchValue(prModel);
			}
		}
	}

	/**
	 * Calculates the match value as per the priority level
	 */
	protected int calculateMatchValue(final PriceRowModel price)
	{
		final boolean _product = price.getProduct() != null || price.getProductId() != null;
		final boolean _productGroup = price.getPg() != null;
		final boolean _user = price.getUser() != null;
		final boolean _userGroup = price.getUg() != null;
		final boolean _salesArea = StringUtils.isNotBlank(price.getSalesArea())
				&& !price.getSalesArea().equalsIgnoreCase(String.valueOf(0));

		int value = 0;
		if (_product)
		{
			if (_user)
			{
				if (_salesArea)
				{
					value = 18;
				}
				else
				{
					value = 14;
				}
			}
			else if (_userGroup)
			{
				if (_salesArea)
				{
					value = 16;
				}
				else
				{
					value = 12;
				}
			}
			else
			{
				if (_salesArea)
				{
					value = 10;
				}
				else
				{
					value = 8;
				}
			}
		}
		else if (_productGroup)
		{
			if (_user)
			{
				if (_salesArea)
				{
					value = 17;
				}
				else
				{
					value = 13;
				}
			}
			else if (_userGroup)
			{
				if (_salesArea)
				{
					value = 15;
				}
				else
				{
					value = 11;
				}
			}
			else
			{
				if (_salesArea)
				{
					value = 9;
				}
				else
				{
					value = 7;
				}
			}
		}
		else
		{
			if (_user)
			{
				if (_salesArea)
				{
					value = 6;
				}
				else
				{
					value = 4;
				}
			}
			else if (_userGroup)
			{
				if (_salesArea)
				{
					value = 5;
				}
				else
				{
					value = 3;
				}
			}
			else
			{
				if (_salesArea)
				{
					value = 2;
				}
				else
				{
					value = 1;
				}
			}
		}
		return value;
	}


	/**
	 * Method to update the match value
	 *
	 * @param prModel
	 *           the price row model
	 */
	private void updateMatchValue(final PriceRowModel prModel)
	{
		prModel.setMatchValue(Integer.valueOf(calculateMatchValue(prModel)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateCatalogVersion(final PDTRowModel pdtModel)
	{
		CatalogVersionModel catver = ((PriceRowModel) pdtModel).getCatalogVersion();
		if (catver == null)
		{
			final ProductModel prod = pdtModel.getProduct();
			if (prod != null)
			{
				catver = getCatalogTypeService().getCatalogVersionForCatalogVersionAwareModel(prod);

				if (catver != null)
				{
					((PriceRowModel) pdtModel).setCatalogVersion(catver);
				}
			}
		}

	}
}
