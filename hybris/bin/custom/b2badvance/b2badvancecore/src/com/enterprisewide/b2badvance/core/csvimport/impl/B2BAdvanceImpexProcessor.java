package com.enterprisewide.b2badvance.core.csvimport.impl;


import de.hybris.platform.acceleratorservices.dataimport.batch.BatchException;
import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.HeaderSetupTask;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.ImpexTransformerTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;
import com.enterprisewide.b2badvance.core.integration.task.B2BAdvanceAbstractImpexRunnerTask;


/**
 * This class includes common import invoice methods.
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceImpexProcessor
{
	private static final Logger LOG = Logger.getLogger(B2BAdvanceImpexProcessor.class);
	private ImpexTransformerTask b2bAdvanceImportTransformerTask;
	private B2BAdvanceAbstractImpexRunnerTask b2bAdvanceImportImpexRunnerTask;
	private HeaderSetupTask b2bAdvanceImportHeaderSetupTask;

	/**
	 * Process the CSV Files to import data into the system.
	 *
	 * @param dir
	 * @param fileNamePrefix
	 * @return boolean hasError, code sets its value to true in case of any exception/data issue.
	 */
	public boolean processCSV(final File dir, final String fileNamePrefix, final String catalogId)
	{
		boolean hasError = false;
		final File[] csvFiles = dir.listFiles();

		if (csvFiles == null)
		{
			LOG.info("No processed csv file exist in dir to import data : " + dir);
		}
		else
		{
			for (final File fileEntry : csvFiles)
			{
				try
				{
					importFromCSV(fileEntry, catalogId, fileNamePrefix);
				}
				catch (UnsupportedEncodingException | FileNotFoundException | BatchException exception)
				{
					hasError = true;
					LOG.error("Error while importing csv file : " + exception.getMessage(), exception);
				}
			}
		}
		return hasError;

	}

	/**
	 * This method imports data from csv.
	 *
	 * @param file
	 * @param fileNamePrefix
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws BatchException
	 */
	private void importFromCSV(final File file, final String catalogId, final String fileNamePrefix)
			throws UnsupportedEncodingException, FileNotFoundException, BatchException
	{
		if (file != null && file.isFile() && file.getName().endsWith(B2badvanceCoreConstants.CSV_EXTENSION)
				&& file.getName().startsWith(fileNamePrefix))
		{
			LOG.info("Transforming and running file : " + file.getName());
			b2bAdvanceImportHeaderSetupTask.setCatalog(catalogId);
			BatchHeader header = b2bAdvanceImportHeaderSetupTask.execute(file);
			header = b2bAdvanceImportTransformerTask.execute(header);
			header = b2bAdvanceImportImpexRunnerTask.execute(header);
			LOG.info("Impex file import is completed :" + file.getName() + " : " + header);
		}
	}

	@Required
	public void setB2bAdvanceImportHeaderSetupTask(final HeaderSetupTask b2bAdvanceImportHeaderSetupTask)
	{
		this.b2bAdvanceImportHeaderSetupTask = b2bAdvanceImportHeaderSetupTask;
	}

	@Required
	public void setB2bAdvanceImportTransformerTask(final ImpexTransformerTask b2bAdvanceImportTransformerTask)
	{
		this.b2bAdvanceImportTransformerTask = b2bAdvanceImportTransformerTask;
	}

	@Required
	public void setB2bAdvanceImportImpexRunnerTask(final B2BAdvanceAbstractImpexRunnerTask b2bAdvanceImportImpexRunnerTask)
	{
		this.b2bAdvanceImportImpexRunnerTask = b2bAdvanceImportImpexRunnerTask;
	}
}
