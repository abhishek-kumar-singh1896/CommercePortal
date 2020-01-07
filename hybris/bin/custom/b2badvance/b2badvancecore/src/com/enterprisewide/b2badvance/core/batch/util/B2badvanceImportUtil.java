/**
 *
 */
package com.enterprisewide.b2badvance.core.batch.util;

import com.enterprisewide.b2badvance.core.batch.media.service.B2badvanceMediaContainerRemovalService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.CSVReader;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * @author Enterprise Wide
 *
 */
public class B2badvanceImportUtil
{
	private static final Logger LOG = Logger.getLogger(B2badvanceMediaContainerRemovalService.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	private SearchRestrictionService searchRestrictionService;

	private String catalog;
	private static final String CATALOG_VERSION = "Staged";
	
	/**
	 * Substring real file name (removing .jpg/.jpeg) and appending '-C'
	 * 
	 * @param mediaCode
	 */
	public String getMediaContainerQualifier(final String mediaCode)
	{
		String mediaName = "";
		if (StringUtils.isNotEmpty(mediaCode) && mediaCode.lastIndexOf(".") != -1)
		{
			mediaName = mediaCode.substring(0, mediaCode.lastIndexOf("."));
			return mediaName + "-C";
		}
		return mediaName;
	}
	
	public ProductModel getProductForCode(String code) {
		// create a new session
		Session session = sessionService.createNewSession();
		// disable the search restrictions
		searchRestrictionService.disableSearchRestrictions();
		// get the required catalog version
		final CatalogVersionModel catalogVersion = getCatalogVersion();
		ProductModel product = null;
		try {
			product = productService.getProductForCode(catalogVersion, code);
		} catch (final UnknownIdentifierException e) {
			LOG.error("No product found for image identifier " + code);
			throw new IllegalArgumentException("No product found for image identifier " + code);
		} catch (final AmbiguousIdentifierException e) {

			LOG.error("More than one products found for image identifier " + code);
			throw new IllegalArgumentException("More than one products found for image identifier " + code);
		} finally {
			// enable the search restrictions back
			searchRestrictionService.enableSearchRestrictions();
			sessionService.closeSession(session);
		}
		return product;
	}
	
	/**
	 * The method returns the catalog version
	 *
	 * @return CatalogVersionModel the catalogVersion required
	 */
	protected CatalogVersionModel getCatalogVersion()
	{
		final Object result = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				userService.setCurrentUser(userService.getAdminUser());
				final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalog, CATALOG_VERSION);
				return catalogVersion;
			}
		});
		return (CatalogVersionModel) result;
	}
	
	/**
	 * The method creates a new CSVReader for the input file.
	 *
	 * @param file
	 *            the source CSV file
	 * @return CSVReader
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public CSVReader createCsvReader(final File file) throws UnsupportedEncodingException, FileNotFoundException {
		final CSVReader csvReader = new CSVReader(file, "UTF-8");
		csvReader.setLinesToSkip(0);
		csvReader.setFieldSeparator(new char[] { ',' });
		return csvReader;
	}
	
	/**
	 * @param csvReader
	 */
	public void closeQuietly(final CSVReader csvReader) {
		if (csvReader != null) {
			try {
				csvReader.close();
			} catch (final IOException e) {
				LOG.warn("Could not close csvReader" + e);
			}
		}
	}

	/**
	 * @param catalog
	 *            the catalog to set
	 */
	@Required
	public void setCatalog(final String catalog) {
		this.catalog = catalog;
	}
}
