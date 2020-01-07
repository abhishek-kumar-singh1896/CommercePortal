/**
 *
 */
package com.enterprisewide.b2badvance.core.util;

import de.hybris.platform.util.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Core Util for B2B Advance
 *
 * @author Enterprise Wide
 */
public class B2badvanceCoreUtil
{
	private static final Logger LOG = Logger.getLogger(B2badvanceCoreUtil.class);

	private static final String UNDERSCORE = "_";

	/**
	 * The method creates a new CSVReader for the input file.
	 *
	 * @param file
	 *           the source CSV file
	 * @param linesToSkip
	 *           the lines to skip
	 * @param fieldSeparator
	 *           field separator
	 * @return CSVReader
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public CSVReader createCsvReader(final File file, final int linesToSkip, final char fieldSeparator) throws IOException
	{
		LOG.debug("Creating CSVReader Instance");
		final CSVReader csvReader = new CSVReader(file, "UTF-8");
		csvReader.setLinesToSkip(linesToSkip);
		csvReader.setFieldSeparator(new char[]
		{ fieldSeparator });
		return csvReader;
	}

	/**
	 * Method to close the CSV Reader.
	 *
	 * @param csvReader
	 */
	public void closeCsvReader(final CSVReader csvReader)
	{
		LOG.debug("Closing CSVReader " + csvReader);
		if (csvReader != null)
		{
			try
			{
				csvReader.close();
			}
			catch (final IOException e)
			{
				LOG.warn("Could not close csvReader" + e);
			}
		}
	}

	/**
	 * Cleans a directory without deleting it.
	 *
	 * @param directory
	 *           directory to clean
	 * @throws IOException
	 *            in case cleaning is unsuccessful
	 */
	public static void cleanDirectory(final File directory) throws IOException
	{
		if (!directory.exists())
		{
			final String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory())
		{
			final String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		final File[] files = directory.listFiles();
		if (files == null)
		{
			// null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		for (final File file : files)
		{
			try
			{
				deleteFile(file);
			}
			catch (final IOException exception)
			{
				LOG.error("Exception occured while deleting file : " + exception);
			}
		}

	}

	private static void deleteFile(final File file) throws IOException
	{
		if (!file.isDirectory())
		{
			final boolean filePresent = file.exists();
			if (!file.delete())
			{
				if (!filePresent)
				{
					throw new FileNotFoundException("File does not exist: " + file);
				}
				final String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}

	/**
	 * Format Date into required pattern
	 *
	 * @param date
	 * @return String
	 */
	public static String formatDate(final Date date)
	{
		String formattedDate = StringUtils.EMPTY;
		if (date != null)
		{
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E dd MMM yyyy");
			formattedDate = simpleDateFormat.format(date);
		}
		return formattedDate;
	}
}
