/**
 *
 */
package com.gallagher.facades.order.impl;

import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.Collection;
import java.util.Iterator;


/**
 * @author FrankZ
 *
 */
public class GallagherCartFacade extends DefaultCartFacade
{

	@Override
	public CartModificationData addToCart(final String code, final long quantity) throws CommerceCartModificationException
	{
		final AddToCartParams params = new AddToCartParams();
		params.setProductCode(code);
		params.setQuantity(quantity);

		return addToCart(params);
	}

	@Override
	public CartModificationData addToCart(final AddToCartParams addToCartParams) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = getCommerceCartParameterConverter().convert(addToCartParams);
		final ProductModel product = parameter.getProduct();
		final Collection<PriceRowModel> models = product.getEurope1Prices();
		if (null != models && models.size() > 0)
		{
			final Iterator<PriceRowModel> iter = models.iterator();
			final PriceRowModel model = iter.next();
			parameter.setUnit(model.getUnit());
		}
		System.out.println("DefaultCartFacade addToCart " + parameter.getUnit());
		final CommerceCartModification modification = getCommerceCartService().addToCart(parameter);
		return getCartModificationConverter().convert(modification);
	}

}
