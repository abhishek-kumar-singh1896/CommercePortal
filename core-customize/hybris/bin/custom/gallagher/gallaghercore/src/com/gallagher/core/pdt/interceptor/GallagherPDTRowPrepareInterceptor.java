/**
 *
 */
package com.gallagher.core.pdt.interceptor;

import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.product.impl.PDTRowPrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 *
 * The GallagherPDTRowPrepareInterceptor class. Handles the Sales Area based Pricing functionalities.
 *
 * @author Nagarro-Dev
 *
 */
public abstract class GallagherPDTRowPrepareInterceptor extends PDTRowPrepareInterceptor
{
	private static final Logger LOGGER = Logger.getLogger(GallagherPDTRowPrepareInterceptor.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		super.onPrepare(model, ctx);
		if (model instanceof PDTRowModel)
		{
			final PDTRowModel pdtModel = (PDTRowModel) model;
			updateSalesAreaMatchQualifier(pdtModel, ctx);
			updateCustomerGorup(pdtModel, ctx);
		}
	}

	/**
	 * @param pdtModel
	 * @param ctx
	 */
	private void updateCustomerGorup(final PDTRowModel pdtModel, final InterceptorContext ctx) throws InterceptorException
	{

		if (ctx.isNew(pdtModel) || ctx.isModified(pdtModel, PDTRowModel.CUSTOMERGROUP))
		{

			final String customerGroup = pdtModel.getCustomerGroup();
			if (StringUtils.isNotBlank(customerGroup))
			{
				pdtModel.setCustomerGroupMatchQualifier(customerGroup);
			}
			else
			{
				pdtModel.setCustomerGroup(String.valueOf(0));
				pdtModel.setCustomerGroupMatchQualifier(String.valueOf(0));
			}
		}
	}

	/**
	 * Method to update the Sales Area match qualifier as per Sales Area value.
	 *
	 * @param prModel
	 *           the pdt row model
	 * @param ctx
	 *           the context
	 * @throws InterceptorException
	 *            the interceptor exception
	 */
	protected void updateSalesAreaMatchQualifier(final PDTRowModel prModel, final InterceptorContext ctx)
			throws InterceptorException
	{
		if (ctx.isNew(prModel) || ctx.isModified(prModel, PDTRowModel.SALESAREA))
		{
			final String salesArea = prModel.getSalesArea();
			if (null != salesArea)
			{
				prModel.setSalesAreaMatchQualifier(salesArea);
			}
			else
			{
				prModel.setSalesArea(String.valueOf(0));
				prModel.setSalesAreaMatchQualifier(String.valueOf(0));
			}
		}
	}
}
