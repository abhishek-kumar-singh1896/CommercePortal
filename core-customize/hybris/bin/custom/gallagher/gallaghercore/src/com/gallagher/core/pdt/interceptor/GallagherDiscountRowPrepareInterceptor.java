/**
 *
 */
package com.gallagher.core.pdt.interceptor;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.apache.commons.lang.StringUtils;


/**
 * @author ankituniyal
 *
 */
public class GallagherDiscountRowPrepareInterceptor extends GallagherPDTRowPrepareInterceptor
{

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof DiscountRowModel)
		{
			final DiscountRowModel dModel = (DiscountRowModel) model;
			super.onPrepare(dModel, ctx);

			if (ctx.isNew(model) || ctx.isModified(model, DiscountRowModel.PRODUCT) || ctx.isModified(model, DiscountRowModel.PG)
					|| ctx.isModified(model, DiscountRowModel.USER) || ctx.isModified(model, DiscountRowModel.UG)
					|| ctx.isModified(model, DiscountRowModel.PRODUCTID) || ctx.isModified(model, DiscountRowModel.SALESAREA))
			{
				updateMatchValue(dModel);
			}
		}
	}

	/**
	 * @param dModel
	 */
	private void updateMatchValue(final DiscountRowModel prModel)
	{
		prModel.setMatchValue(Integer.valueOf(calculateMatchValue(prModel)));
	}

	/**
	 * Calculates the match value as per the priority level
	 */
	protected int calculateMatchValue(final DiscountRowModel discount)
	{
		final boolean _product = discount.getProduct() != null || discount.getProductId() != null;
		final boolean _productGroup = discount.getPg() != null;
		final boolean _user = discount.getUser() != null;
		final boolean _userGroup = discount.getUg() != null;
		final boolean _salesArea = StringUtils.isNotBlank(discount.getSalesArea())
				&& !discount.getSalesArea().equalsIgnoreCase(String.valueOf(0));

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


	@Override
	protected void updateCatalogVersion(final PDTRowModel pdtModel)
	{
		CatalogVersionModel catver = ((DiscountRowModel) pdtModel).getCatalogVersion();
		if (catver == null)
		{
			final ProductModel prod = pdtModel.getProduct();
			if (prod != null)
			{
				catver = getCatalogTypeService().getCatalogVersionForCatalogVersionAwareModel(prod);
				//
				if (catver != null)
				{
					((DiscountRowModel) pdtModel).setCatalogVersion(catver);
				}
			}
		}
	}
}
