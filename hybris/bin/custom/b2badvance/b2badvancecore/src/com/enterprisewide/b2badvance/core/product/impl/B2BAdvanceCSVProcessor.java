/**
 *
 */
package com.enterprisewide.b2badvance.core.product.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.supercsv.io.dozer.CsvDozerBeanData;
import org.supercsv.io.dozer.CsvDozerBeanWriter;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;
import com.enterprisewide.b2badvance.core.csvimport.impl.B2BAdvanceDozerCSVReader;
import com.enterprisewide.b2badvance.core.csvimport.impl.B2BAdvanceDozerCSVWriter;
import com.enterprisewide.b2badvance.core.csvimport.impl.B2BAdvanceImpexProcessor;
import com.enterprisewide.b2badvance.core.dto.B2BAdvanceCsvDTO;
import com.enterprisewide.b2badvance.core.util.B2badvanceCoreUtil;


/**
 * Base Product Proccess Class for processing the base product files
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceCSVProcessor
{

	private static final Logger LOG = Logger.getLogger(B2BAdvanceCSVProcessor.class);
	private B2BAdvanceDozerCSVReader csvReader;
	private B2BAdvanceDozerCSVWriter csvWriter;
	private B2BAdvanceCSVDataValidator dataValidator;
	private B2BAdvanceImpexProcessor b2bAdvanceImpexProcessor;

	/**
	 * Processes the CSV files for specific type
	 *
	 * @param csvFiles
	 *           list of csv files
	 * @param dirMap
	 *           holds all the files
	 * @param catalogId
	 *           for this import
	 * @param importType
	 *           the type going to be imported
	 * @return true if CSVs have been processed successfully
	 */
	public boolean processCSVFiles(final List<File> csvFiles, final Map<String, File> dirMap, final String catalogId,
			final String importType)
	{
		boolean processError = false;
		for (final File csvFile : csvFiles)
		{
			LOG.info("Processing base product file " + csvFile);
			final boolean rawProcessError = processRawCSV(csvFile, dirMap, catalogId, importType);
			final boolean importError = b2bAdvanceImpexProcessor.processCSV(dirMap.get(importType), importType, catalogId);
			if (rawProcessError || importError)
			{
				processError = true;
			}
			cleanProcessedFiles(dirMap, importType);
		}
		return processError;
	}

	/**
	 * Processes a single CSV file
	 *
	 * @param rawCSVFile
	 *           the file to be processed
	 * @param catalogId
	 *           for this import
	 * @param importType
	 *           the type going to be imported
	 * @return true if CSVs have been processed successfully
	 */
	private boolean processRawCSV(final File rawCSVFile, final Map<String, File> dirMap, final String catalogId,
			final String importType)
	{
		boolean hasError = false;
		final File baseFile = new File(dirMap.get(importType), importType + B2badvanceCoreConstants.CSV_EXTENSION);

		csvReader.createReader(rawCSVFile);
		final CsvDozerBeanWriter baseProductWriter = csvWriter.createWriter(baseFile);
		B2BAdvanceCsvDTO dto = null;
		final StringBuilder errorBuilder = new StringBuilder();
		while ((dto = csvReader.readNext(errorBuilder)) != null)
		{
			dto = dataValidator.validateCSVData(dto);
			dto.setCatalogId(catalogId);
			final CsvDozerBeanData baseProductData = (CsvDozerBeanData) csvReader.convertObject(dto, CsvDozerBeanData.class,
					importType);

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
		csvReader.closeReader();
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
	 * This method removes all the processed files associated with a base product file from the directory.
	 *
	 * @param dirMap
	 */
	private void cleanProcessedFiles(final Map<String, File> dirMap, final String importType)
	{
		try
		{
			B2badvanceCoreUtil.cleanDirectory(dirMap.get(importType));
		}
		catch (final IOException ioException)
		{
			LOG.error("Unable to clean directories", ioException);
		}

	}

	public B2BAdvanceDozerCSVReader getCsvReader()
	{
		return csvReader;
	}

	public void setCsvReader(final B2BAdvanceDozerCSVReader baseProductCsvReader)
	{
		this.csvReader = baseProductCsvReader;
	}

	public B2BAdvanceDozerCSVWriter getCsvWriter()
	{
		return csvWriter;
	}

	public void setCsvWriter(final B2BAdvanceDozerCSVWriter csvWriter)
	{
		this.csvWriter = csvWriter;
	}

	public B2BAdvanceCSVDataValidator getDataValidator()
	{
		return dataValidator;
	}

	public void setDataValidator(final B2BAdvanceCSVDataValidator dataValidator)
	{
		this.dataValidator = dataValidator;
	}

	public B2BAdvanceImpexProcessor getB2bAdvanceImpexProcessor()
	{
		return b2bAdvanceImpexProcessor;
	}

	public void setB2bAdvanceImpexProcessor(final B2BAdvanceImpexProcessor b2bAdvanceImpexProcessor)
	{
		this.b2bAdvanceImpexProcessor = b2bAdvanceImpexProcessor;
	}
}
