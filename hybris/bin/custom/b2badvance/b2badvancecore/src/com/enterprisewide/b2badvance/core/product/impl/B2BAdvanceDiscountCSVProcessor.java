/**
 *
 */
package com.enterprisewide.b2badvance.core.product.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.supercsv.io.dozer.CsvDozerBeanData;
import org.supercsv.io.dozer.CsvDozerBeanWriter;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;
import com.enterprisewide.b2badvance.core.dto.B2BAdvanceDiscountCsvDTO;
import com.enterprisewide.b2badvance.core.util.B2badvanceCoreUtil;


/**
 * Base Product Proccess Class for processing the discount rows
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceDiscountCSVProcessor extends B2BAdvanceCSVProcessor
{

	private static final Logger LOG = Logger.getLogger(B2BAdvanceDiscountCSVProcessor.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean processCSVFiles(final List<File> csvFiles, final Map<String, File> dirMap, final String catalogId,
			final String importType)
	{
		boolean processError = false;
		final File discountDir = createDiscountFileIfNotExists(dirMap, importType);
		if (discountDir.exists())
		{
			for (final File csvFile : csvFiles)
			{
				LOG.info("Processing base product file " + csvFile);
				final boolean rawProcessError = processRawCSV(csvFile, dirMap, catalogId, importType, discountDir);
				final boolean discountImportError = getB2bAdvanceImpexProcessor().processCSV(discountDir, importType, catalogId);
				final boolean importError = getB2bAdvanceImpexProcessor().processCSV(dirMap.get(importType), importType, catalogId);
				if (rawProcessError || importError || discountImportError)
				{
					processError = true;
				}
				cleanProcessedFiles(dirMap, discountDir, importType);
			}
		}
		else
		{
			processError = true;
		}
		return processError;
	}

	/**
	 * This method creates a directory if that doesn't exist.
	 *
	 * @param processError
	 *           to get the parent dir
	 * @param importType
	 *           to get the parent dir
	 * @return the created file
	 */
	public File createDiscountFileIfNotExists(final Map<String, File> dirMap, final String importType)
	{
		LOG.info("Creating Discount Dir");
		final File dir = new File(dirMap.get(importType).getParent(), "discount");
		if (!dir.exists() && !dir.mkdirs())
		{
			LOG.error("Error occured while creating directory : " + dir.getName());
		}
		return dir;
	}

	/**
	 * Processes raw CSV file for discounts
	 *
	 * @param rawCSVFile
	 *           to be processed
	 * @param dirMap
	 *           contains all the file locations
	 * @param catalogId
	 *           for the import
	 * @param importType
	 *           discount
	 * @param discountDir
	 *           to create the discount codes
	 * @return true if processing is successful
	 */
	private boolean processRawCSV(final File rawCSVFile, final Map<String, File> dirMap, final String catalogId,
			final String importType, final File discountDir)
	{
		boolean hasError = false;
		final File baseFile = new File(dirMap.get(importType), importType + B2badvanceCoreConstants.CSV_EXTENSION);
		final Map<String, String> discountMap = new HashMap<String, String>();
		getCsvReader().createReader(rawCSVFile);
		final CsvDozerBeanWriter baseProductWriter = getCsvWriter().createWriter(baseFile);
		B2BAdvanceDiscountCsvDTO dto = null;
		final StringBuilder errorBuilder = new StringBuilder();
		while ((dto = getCsvReader().readNext(errorBuilder)) != null)
		{
			dto = (B2BAdvanceDiscountCsvDTO) getDataValidator().validateCSVData(dto);
			dto.setCatalogId(catalogId);
			final CsvDozerBeanData baseProductData = (CsvDozerBeanData) getCsvReader().convertObject(dto, CsvDozerBeanData.class,
					importType);
			if (StringUtils.isNotEmpty(dto.getDiscountCode()))
			{
				discountMap.put(dto.getDiscountCode(), dto.getCurrency());
			}
			try
			{
				baseProductWriter.write(baseProductData);
			}
			catch (final IOException ioException)
			{
				hasError = true;
				LOG.error("Exception occured while writing csv: ", ioException);
			}
		}

		if (errorBuilder.length() > 0)
		{
			hasError = true;
		}
		getCsvReader().closeReader();
		createCSVForDiscount(discountMap, discountDir);
		try
		{
			baseProductWriter.close();
		}
		catch (final IOException ioException)
		{
			hasError = true;
			LOG.error("Exception occured while writing csv writer : ", ioException);
		}
		return hasError;
	}

	/**
	 * This method creates csv for all discount codes in map.
	 *
	 * @param discountMap
	 *           to hold all discounts code to be created
	 * @param discountDir
	 *           to hold the new csv file
	 * @return boolean hasError, code sets its value to true in case of any exception/data issue.
	 */
	private boolean createCSVForDiscount(final Map<String, String> discountMap, final File discountDir)
	{
		boolean hasError = false;
		final File discountFile = new File(discountDir,
				"discountType" + System.currentTimeMillis() + B2badvanceCoreConstants.CSV_EXTENSION);
		try (CsvDozerBeanWriter discountType = getCsvWriter().createWriter(discountFile);)
		{
			for (final Map.Entry<String, String> discountEntry : discountMap.entrySet())
			{

				final String discountCode = discountEntry.getKey();
				final String currency = discountEntry.getValue();
				final CsvDozerBeanData enumBean = new CsvDozerBeanData();
				final List<Object> enumDataList = new ArrayList<>();
				enumDataList.add(discountCode);
				enumDataList.add(currency);
				enumBean.setColumns(enumDataList);
				discountType.write(enumBean);
			}
		}
		catch (final IOException ioException)
		{
			hasError = true;
			LOG.error("Exception occured while writing discount csvs : " + discountFile, ioException);
		}
		return hasError;
	}

	/**
	 * This method removes all the processed files associated with a base product file from the directory.
	 *
	 * @param dirMap
	 *           for discount rows
	 * @param discountDir
	 *           for discount codes
	 */
	private void cleanProcessedFiles(final Map<String, File> dirMap, final File discountDir, final String importType)
	{
		try
		{
			B2badvanceCoreUtil.cleanDirectory(dirMap.get(importType));
			B2badvanceCoreUtil.cleanDirectory(discountDir);
		}
		catch (final IOException ioException)
		{
			LOG.error("Unable to clean directories", ioException);
		}

	}
}
