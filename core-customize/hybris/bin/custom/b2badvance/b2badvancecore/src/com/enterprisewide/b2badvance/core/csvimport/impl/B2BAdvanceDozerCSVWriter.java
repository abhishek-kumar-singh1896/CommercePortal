package com.enterprisewide.b2badvance.core.csvimport.impl;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;
import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.io.dozer.ICsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;


/**
 * This class is used to write csv files.
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceDozerCSVWriter
{
	private static final Logger LOG = Logger.getLogger(B2BAdvanceDozerCSVWriter.class);

	private ICsvDozerBeanWriter beanWriter;

	/**
	 * This method is used to create write object
	 *
	 * @param file
	 * @param preferences
	 * @return
	 */
	public CsvDozerBeanWriter createWriter(final File file)
	{
		try
		{
			beanWriter = new CsvDozerBeanWriter(new FileWriterWithEncoding(file, B2badvanceCoreConstants.DEFAULT_CSV_ENCODING),
					CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
		}
		catch (final IOException exception)
		{
			LOG.error("Exception occured creating csv writer : ", exception);
		}
		return (CsvDozerBeanWriter) beanWriter;
	}

	/**
	 * This method is used to close writer.
	 *
	 * @param file
	 */
	@PreDestroy
	public void closeWriter()
	{
		if (beanWriter != null)
		{
			try
			{
				beanWriter.close();
			}
			catch (final IOException ioException)
			{
				LOG.error("Exception occured while closing csv writer : ", ioException);
			}
			beanWriter = null;
		}
	}
}
