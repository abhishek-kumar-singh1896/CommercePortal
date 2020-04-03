/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.conversion.DefaultMediaConversionService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 */
public class GallagherMediaConversionServiceImpl extends DefaultMediaConversionService
{
	private static final Logger LOGGER = Logger.getLogger(GallagherMediaConversionServiceImpl.class);

	@Autowired
	private ModelService modelService;

	@Override
	public MediaModel getOrConvert(final MediaContainerModel container, final MediaFormatModel format)
	{


		LOGGER.info("container getbasestoreesss" + container.getBaseStores());
		final MediaModel mediaModel = super.getOrConvert(container, format);
		mediaModel.setBaseStores(container.getBaseStores());
		LOGGER.info("MediaModel getbasestoreesss" + mediaModel.getBaseStores());
		LOGGER.info("converted media model returned" + mediaModel.getCode());
		modelService.save(mediaModel);
		return mediaModel;

	}


}
