package com.enterprisewide.b2badvance.core.sftp;

import java.util.List;


/**
 * This class contains SFTP files response.
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceSFTPFilesResponse
{
	private List<String> processedFiles;
	private List<String> successFiles;
	private List<String> errorFiles;
	private boolean hasError;

	public List<String> getProcessedFiles()
	{
		return processedFiles;
	}

	public void setProcessedFiles(final List<String> processedFiles)
	{
		this.processedFiles = processedFiles;
	}

	public List<String> getSuccessFiles()
	{
		return successFiles;
	}

	public void setSuccessFiles(final List<String> successFiles)
	{
		this.successFiles = successFiles;
	}

	public List<String> getErrorFiles()
	{
		return errorFiles;
	}

	public void setErrorFiles(final List<String> errorFiles)
	{
		this.errorFiles = errorFiles;
	}

	public boolean getHasError()
	{
		return hasError;
	}

	public void setHasError(final boolean hasError)
	{
		this.hasError = hasError;
	}
}
