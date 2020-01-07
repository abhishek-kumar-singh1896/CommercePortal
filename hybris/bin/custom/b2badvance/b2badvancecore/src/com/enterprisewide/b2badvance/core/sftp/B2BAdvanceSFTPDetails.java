/**
 *
 */
package com.enterprisewide.b2badvance.core.sftp;

/**
 * This class contains data required for setting up SFTP server
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceSFTPDetails
{
	private String hostname;
	private String port;
	private String username;
	private String password;
	private String sourcePath;
	private String destPath;
	private String fileRegex;

	public String getHostname()
	{
		return hostname;
	}

	public void setHostname(final String hostname)
	{
		this.hostname = hostname;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(final String port)
	{
		this.port = port;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(final String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	public String getSourcePath()
	{
		return sourcePath;
	}

	public void setSourcePath(final String sourcePath)
	{
		this.sourcePath = sourcePath;
	}

	public String getDestPath()
	{
		return destPath;
	}

	public void setDestPath(final String destPath)
	{
		this.destPath = destPath;
	}

	public String getFileRegex()
	{
		return fileRegex;
	}

	public void setFileRegex(final String fileRegex)
	{
		this.fileRegex = fileRegex;
	}
}
