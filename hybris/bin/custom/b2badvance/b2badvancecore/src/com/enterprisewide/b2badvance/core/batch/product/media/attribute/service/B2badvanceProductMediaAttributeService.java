package com.enterprisewide.b2badvance.core.batch.product.media.attribute.service;

import de.hybris.platform.core.model.product.ProductModel;

/**
 *  
 * The B2badvanceProductMediaAttributeService interface. Set all the product
 * media attributes from the main/primary image of the product.
 * 
 * @author Enterprise Wide
 *
 */
public interface B2badvanceProductMediaAttributeService {

	public void setProductMediaAttributes(final ProductModel product);

}
