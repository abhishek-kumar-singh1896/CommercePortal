/**
 *
 */
package com.enterprisewide.b2badvance.core.csvimport.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Required;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.dozer.CsvDozerBeanReader;
import org.supercsv.io.dozer.ICsvDozerBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;


/**
 *
 */
public class B2BAdvanceDozerCSVReader
{
	private static final Logger LOG = Logger.getLogger(B2BAdvanceDozerCSVReader.class);

	private ICsvDozerBeanReader beanReader;
	private CellProcessor[] cellProcessors;
	private Class<?> dtoType;
	private String[] fieldMappers;
	private DozerBeanMapper dozerBeanMapper;
	private boolean hasCsvHeader;

	/**
	 * This method is used to create reader's object.
	 *
	 * @param file
	 * @return CsvDozerBeanReader , reader
	 */
	public CsvDozerBeanReader createReader(final File file)
	{
		try
		{
			beanReader = new CsvDozerBeanReader(
					new InputStreamReader(new FileInputStream(file), B2badvanceCoreConstants.DEFAULT_CSV_ENCODING),
					CsvPreference.STANDARD_PREFERENCE);
			beanReader.configureBeanMapping(dtoType, fieldMappers);
			beanReader.getHeader(hasCsvHeader);
		}
		catch (final IOException | SuperCsvException exception)
		{
			LOG.error("Exception occured while creating reader for csv : ", exception);
			if (exception instanceof SuperCsvException)
			{
				LOG.error("Error occured at :: " + ((SuperCsvException) exception).getCsvContext());
			}
		}
		return (CsvDozerBeanReader) beanReader;
	}

	/**
	 * This method is used to fetch the rows of csv one by one
	 *
	 * @param errorBuilder
	 * @return dto, column read
	 */
	public <T> T readNext(final StringBuilder errorBuilder)
	{
		T dto = null;
		try
		{
			dto = (T) beanReader.read(dtoType, cellProcessors);
		}
		catch (final IOException | SuperCsvException exception)
		{
			LOG.error("Exception occured while reading csv : ", exception);
			if (exception instanceof SuperCsvException)
			{
				LOG.error("Error occured at :: " + ((SuperCsvException) exception).getCsvContext());
			}
			errorBuilder.append(B2badvanceCoreConstants.FAILURE);
			dto = readNext(errorBuilder);

		}
		return dto;
	}

	/**
	 * This method is used to call mapper to map fields of one object to another object.
	 *
	 * @param source
	 * @param destination
	 * @param mapId
	 * @return Object of destination
	 */
	public Object convertObject(final Object source, final Class<?> destination, final String mapId)
	{
		return dozerBeanMapper.map(source, destination, mapId);
	}

	/**
	 * This method is used to close reader's object.
	 */
	@PreDestroy
	public void closeReader()
	{
		if (beanReader != null)
		{
			try
			{
				beanReader.close();
				beanReader = null;
			}
			catch (final IOException ioException)
			{
				LOG.error("Exception occured while closing csv reader : ", ioException);
			}
		}
	}

	@Required
	public void setCellProcessors(final CellProcessor[] cellProcessors)
	{
		this.cellProcessors = cellProcessors.clone();
	}

	@Required
	public void setDtoType(final Class<?> dtoType)
	{
		this.dtoType = dtoType;
	}

	@Required
	public void setFieldMappers(final String[] fieldMappers)
	{
		this.fieldMappers = fieldMappers.clone();
	}

	@Required
	public void setDozerBeanMapper(final DozerBeanMapper dozerBeanMapper)
	{
		this.dozerBeanMapper = dozerBeanMapper;
	}

	@Required
	public void setHasCsvHeader(final boolean hasCsvHeader)
	{
		this.hasCsvHeader = hasCsvHeader;
	}

}
