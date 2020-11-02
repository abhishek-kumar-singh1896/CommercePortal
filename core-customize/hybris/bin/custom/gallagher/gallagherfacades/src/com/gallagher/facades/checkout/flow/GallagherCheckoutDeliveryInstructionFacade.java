/**
 *
 */
package com.gallagher.facades.checkout.flow;

import de.hybris.platform.commercefacades.order.data.DeliveryInstructionData;

/**
 * @author abhinavgupta03
 *
 */
public interface GallagherCheckoutDeliveryInstructionFacade
{

	void setDeliveryInstructions(final String deliveryInstructions, final String productSpecificDetailsHeading,
			final Integer entryNumber);

	void populateDeliveryInstruction(final DeliveryInstructionData deliveryInstructionData);

}
