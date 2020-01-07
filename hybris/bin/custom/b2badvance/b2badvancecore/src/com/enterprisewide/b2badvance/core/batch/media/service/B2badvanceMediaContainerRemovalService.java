/**
 *
 */
package com.enterprisewide.b2badvance.core.batch.media.service;

import com.enterprisewide.b2badvance.core.batch.util.B2badvanceImportUtil;
import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.HeaderTask;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.CSVReader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Enterprise Wide
 *
 *         
 */
public class B2badvanceMediaContainerRemovalService implements HeaderTask {
	private static final Logger LOG = Logger.getLogger(B2badvanceMediaContainerRemovalService.class);
	
	@Autowired
	private MediaConversionService mediaConversionService;

	@Autowired
	private ModelService modelService;
	
	@Autowired
	private B2badvanceImportUtil importUtil;


	/**
	 * The method executes the media conversion for the medias put in the hot
	 * folder immediately after the addition of medias.
	 *
	 * @param header
	 *            The batch header
	 * @return header The batch header
	 */
	@Override
	public BatchHeader execute(final BatchHeader header) {
		if (header == null) {
			LOG.error("Error reading the header: Header is null.");
		} else if (header.getFile() == null) {
			LOG.error("Error reading the file: File is null.");
		} else {
			CSVReader reader = null;
			try {
				// Get the file from batch header
				final File file = header.getFile();
				// create a csv reader for file
				reader = importUtil.createCsvReader(file);
				
				List<MediaContainerModel> mediaContainerModels = null;
				
				// reader the csv line by line for each product
				while (reader.readNextLine()) {
					//read the line
					final Map<Integer, String> row = reader.getLine();
					//get the product code from the first column
					final String productCode = row.get(0);
					
					ProductModel product = importUtil.getProductForCode(productCode);
					
					if (product != null) {
						LOG.info("Starting removal of media containers for product code:" + productCode);
						// get all the media containers for the product obtained
						mediaContainerModels = product.getGalleryImages();

						final List<ItemModel> itemsToRemove = new ArrayList<>();

						// remove medias for media container one by one
						for (final MediaContainerModel mediaContainer : mediaContainerModels) {
							LOG.info("Removing medias for media container: " + mediaContainer.getQualifier() + "["
									+ mediaContainer.getPk() + "]");
							itemsToRemove.addAll(mediaContainer.getMedias());
							mediaConversionService.deleteConvertedMedias(mediaContainer);
						}
						LOG.info("Removed all media containers for product code:" + productCode);
						itemsToRemove.addAll(mediaContainerModels);
						modelService.removeAll(itemsToRemove);
					}
				}
			} catch (final UnsupportedEncodingException unsupportedEncodingException) {
				LOG.error("Error reading the file: Unsupported file Encoding ", unsupportedEncodingException);
			} catch (final FileNotFoundException fileNotFoundException) {
				LOG.error("Error reading the file: File not found ", fileNotFoundException);
			} catch (final Exception exception) {
				LOG.error("Error reading the file: ", exception);
			} finally {
				// close the CSV reader
				importUtil.closeQuietly(reader);
			}
		}
		return header;
	}

}
