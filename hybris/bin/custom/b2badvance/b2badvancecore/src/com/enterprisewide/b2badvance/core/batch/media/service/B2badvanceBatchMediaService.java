/**
 *
 */
package com.enterprisewide.b2badvance.core.batch.media.service;

import com.enterprisewide.b2badvance.core.batch.product.media.attribute.service.B2badvanceProductMediaAttributeService;
import com.enterprisewide.b2badvance.core.batch.util.B2badvanceImportUtil;
import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.HeaderTask;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.util.CSVReader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * The class performs the media conversion for the medias added through the hot
 * media folder immediately after adding them. It gets the source CSV file from
 * the Batch header and reads it line by line and then performs the conversion
 * job for each product.
 * 
 * @author Enterprise Wide
 */
public class B2badvanceBatchMediaService implements HeaderTask {

	private static final Logger LOG = Logger.getLogger(B2badvanceBatchMediaService.class);

	@Autowired
	private MediaConversionService mediaConversionService;

	@Autowired
	private B2badvanceProductMediaAttributeService productMediaAttributeService;

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
				// reader the csv line by line for each product
				while (reader.readNextLine()) {
					// read the line
					final Map<Integer, String> row = reader.getLine();
					// get the product code from the first column
					final String productCode = row.get(0);
					ProductModel product = importUtil.getProductForCode(productCode);

					if (product != null) {
						// get all the media containers for the product obtained
						final List<MediaContainerModel> mediaContainerModels = product.getGalleryImages();
						// Convert the product medias for each container one by
						// one
						for (final MediaContainerModel mediaContainer : mediaContainerModels) {
							LOG.info("Starting Media Convertion for media container: " + mediaContainer.getQualifier()
									+ "[" + mediaContainer.getPk() + "]");
							mediaConversionService.convertMedias(mediaContainer);
						}
						// Call the product media attribute service to set the
						// media attributes with required format.
						productMediaAttributeService.setProductMediaAttributes(product);
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
