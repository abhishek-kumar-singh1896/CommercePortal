/**
 *
 */
package com.gallagher.facades.checkout;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.acceleratorfacades.order.impl.DefaultAcceleratorCheckoutFacade;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * @author shilpiverma
 *
 */
public class GallagherDefaultCheckoutFacadeImpl extends DefaultAcceleratorCheckoutFacade implements AcceleratorCheckoutFacade
{
	@Override
	public boolean setDeliveryAddressIfAvailable()
	{
		final CartModel cartModel = getCart();

		if (cartModel == null || cartModel.getDeliveryAddress() != null)
		{
			return false;
		}

		final UserModel currentUser = getCurrentUserForCheckout();
		if (cartModel.getUser().equals(currentUser))
		{
			final AddressModel currentUserDefaultShipmentAddress = currentUser.getDefaultShipmentAddress();
			if (currentUserDefaultShipmentAddress != null)
			{
				final Collection<CountryModel> countries = getCommonI18NService().getAllCountries();
				if (CollectionUtils.isNotEmpty(countries) && countries.contains(currentUserDefaultShipmentAddress.getCountry()))
				{
					final AddressModel supportedDeliveryAddress = getDeliveryAddressModelForCode(
							currentUserDefaultShipmentAddress.getPk().toString());
					if (supportedDeliveryAddress != null)
					{
						return setDeliveryAddress(cartModel, supportedDeliveryAddress);
					}
				}
			}
		}

		// Could not use default address, try any address
		final List<AddressModel> supportedDeliveryAddresses = getDeliveryService().getSupportedDeliveryAddressesForOrder(cartModel,
				true);
		if (supportedDeliveryAddresses != null && !supportedDeliveryAddresses.isEmpty())
		{
			final AddressModel supportedDeliveryAddress = supportedDeliveryAddresses.get(0);
			return setDeliveryAddress(cartModel, supportedDeliveryAddress);
		}
		return false;
	}

	/**
	 * Returns true if the delivery address is set
	 *
	 * @param cartModel
	 * @param supportedDeliveryAddress
	 * @return true if the delivery address is set
	 */
	private boolean setDeliveryAddress(final CartModel cartModel, final AddressModel supportedDeliveryAddress)
	{
		final CommerceCheckoutParameter parameter = createCommerceCheckoutParameter(cartModel, true);
		parameter.setAddress(supportedDeliveryAddress);
		parameter.setIsDeliveryAddress(false);
		return getCommerceCheckoutService().setDeliveryAddress(parameter);
	}

}
