/**
 *
 */
package com.gallagher.core.pdt.interceptor;

import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.product.impl.PDTRowPrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

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
		if (model instanceof PDTRowModel)
		{
			final PDTRowModel pdtModel = (PDTRowModel) model;
			updateSalesAreaMatchQualifier(pdtModel, ctx);
		}
		super.onPrepare(model, ctx);
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
				prModel.setSalesAreaMatchQualifier(String.valueOf(0));
			}
		}
	}
}
