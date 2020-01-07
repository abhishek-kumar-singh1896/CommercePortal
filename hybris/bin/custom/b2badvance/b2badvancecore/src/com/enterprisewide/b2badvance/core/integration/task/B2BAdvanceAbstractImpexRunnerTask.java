/**
 *
 */
package com.enterprisewide.b2badvance.core.integration.task;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.AbstractImpexRunnerTask;
import de.hybris.platform.acceleratorservices.dataimport.batch.util.BatchDirectoryUtils;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * @author robingarg
 *
 */
public class B2BAdvanceAbstractImpexRunnerTask extends AbstractImpexRunnerTask
{
	private static final Logger LOG = Logger.getLogger(B2BAdvanceAbstractImpexRunnerTask.class);

	private SessionService sessionService;
	private ImportService importService;
	private static final String ERROR_FILE_PREFIX = "error_";

	@Override
	public BatchHeader execute(final BatchHeader header) throws FileNotFoundException
	{
		Assert.notNull(header);
		Assert.notNull(header.getEncoding());
		if (CollectionUtils.isNotEmpty(header.getTransformedFiles()))
		{
			final Session localSession = getSessionService().createNewSession();
			try
			{
				for (final File file : header.getTransformedFiles())
				{
					processFile(file, header.getEncoding());
				}
			}
			finally
			{
				getSessionService().closeSession(localSession);
			}
		}
		return header;
	}

	/**
	 * Process an impex file using the given encoding
	 *
	 * @param file
	 * @param encoding
	 * @throws FileNotFoundException
	 */
	@Override
	protected void processFile(final File file, final String encoding) throws FileNotFoundException
	{
		FileInputStream fis = null;
		PrintWriter errorWriter = null;
		try
		{
			fis = new FileInputStream(file);
			final ImportConfig config = getImportConfig();
			if (config == null)
			{
				LOG.error(
						String.format("Import config not found. The file %s won't be imported.", file == null ? null : file.getName()));
				return;
			}
			final ImpExResource resource = new StreamBasedImpExResource(fis, encoding);
			config.setScript(resource);
			final ImportResult importResult = getImportService().importData(config);
			if (importResult.isError() && importResult.hasUnresolvedLines())
			{
				try
				{
					errorWriter = writeErrorLine(file, errorWriter, importResult.getUnresolvedLines().getPreview(), encoding);
				}
				catch (final UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
				LOG.error(importResult.getUnresolvedLines().getPreview());
			}
		}
		finally
		{
			IOUtils.closeQuietly(errorWriter);
			IOUtils.closeQuietly(fis);
		}
	}

	/**
	 *
	 * @param file
	 * @param errorWriter
	 * @param errorMessage
	 * @param encoding
	 * @return result object of PrintWiter class
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	protected PrintWriter writeErrorLine(final File file, final PrintWriter errorWriter, final String errorMessage,
			final String encoding) throws UnsupportedEncodingException, FileNotFoundException
	{
		PrintWriter result = errorWriter;
		if (result == null)
		{
			result = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getErrorFile(file)), encoding)));
		}
		result.print(errorMessage);
		return result;
	}

	/**
	 * Returns the error file
	 *
	 * @param file
	 * @return the error file
	 */
	protected File getErrorFile(final File file)
	{
		return new File(BatchDirectoryUtils.getRelativeErrorDirectory(file), ERROR_FILE_PREFIX + file.getName());
	}

	@Override
	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Override
	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	@Override
	public ImportService getImportService()
	{
		return importService;
	}

	@Override
	@Required
	public void setImportService(final ImportService importService)
	{
		this.importService = importService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.acceleratorservices.dataimport.batch.task.AbstractImpexRunnerTask#getImportConfig()
	 */
	@Override
	public ImportConfig getImportConfig()
	{
		// YTODO Auto-generated method stub
		return null;
	}

}
