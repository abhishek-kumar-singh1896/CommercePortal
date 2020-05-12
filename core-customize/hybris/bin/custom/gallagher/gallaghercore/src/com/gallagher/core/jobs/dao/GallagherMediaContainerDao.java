/**
 *
 */
package com.gallagher.core.jobs.dao;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.media.dao.MediaContainerDao;

import java.util.List;


/**
 *
 */
public interface GallagherMediaContainerDao extends MediaContainerDao
{

	public List<MediaContainerModel> getMediaContainer(final String containerName, final PK containerid);

	public List<ProductModel> getProductModeList(List<String> property_part_numbers, PK pk);

}
