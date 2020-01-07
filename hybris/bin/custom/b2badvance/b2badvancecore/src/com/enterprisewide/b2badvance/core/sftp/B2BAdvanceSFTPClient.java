/**
 *
 */
package com.enterprisewide.b2badvance.core.sftp;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


/**
 * This class handles SFTP requests from hybris system to setup sftp and download files.
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceSFTPClient
{
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceSFTPClient.class);
	private static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
	private static final String SFTP = "sftp";
	private Session session;
	private B2BAdvanceJSchLogger b2bAdvanceJSchLogger;

	/**
	 * This method download files from SFTP Server and save it to Hybris location
	 *
	 * @param sftpDetails
	 * @param lastJobStartTime
	 * @return B2BAdvanceSFTPFilesResponse
	 */
	public B2BAdvanceSFTPFilesResponse downloadFileFromSFTPServer(final B2BAdvanceSFTPDetails sftpDetails,
			final Date lastJobStartTime)
	{
		boolean hasError = false;
		final B2BAdvanceSFTPFilesResponse sftpFilesResponse = new B2BAdvanceSFTPFilesResponse();
		final List<String> errorFiles = new ArrayList<>();
		final List<String> processedFiles = new ArrayList<>();
		final List<String> successFiles = new ArrayList<>();
		try
		{
			final Channel channel = setupSFTPChannel(sftpDetails);
			final ChannelSftp sftpChannel = (ChannelSftp) channel;
			final List<LsEntry> lsEntries = sftpChannel.ls(sftpDetails.getSourcePath());
			if (lsEntries.isEmpty())
			{
				LOG.info("No file exist in the specified sftp folder location.");
			}
			else
			{
				hasError = getSuccessFiles(sftpDetails, errorFiles, processedFiles, successFiles, sftpChannel, lsEntries,
						lastJobStartTime);
			}
			sftpFilesResponse.setProcessedFiles(processedFiles);
			sftpFilesResponse.setSuccessFiles(successFiles);
			sftpFilesResponse.setErrorFiles(errorFiles);
			sftpChannel.exit();
			session.disconnect();
		}
		catch (JSchException | SftpException exception)
		{
			LOG.error("Error Getting Files from SFTP", exception);
			hasError = true;
		}
		sftpFilesResponse.setHasError(hasError);
		return sftpFilesResponse;
	}

	/**
	 * Returns the sftp files.
	 *
	 * @param sftpDetails
	 * @param errorFiles
	 * @param processedFiles
	 * @param successFiles
	 * @param sftpChannel
	 * @param lsEntries
	 * @param lastJobStartTime
	 * @return true if all files are successful
	 */
	private boolean getSuccessFiles(final B2BAdvanceSFTPDetails sftpDetails, final List<String> errorFiles,
			final List<String> processedFiles, final List<String> successFiles, final ChannelSftp sftpChannel,
			final List<LsEntry> lsEntries, final Date lastJobStartTime)
	{
		boolean hasError = false;
		final Pattern filePattern = Pattern.compile(sftpDetails.getFileRegex());
		for (final LsEntry entry : lsEntries)
		{
			if (validFile(entry, filePattern, lastJobStartTime))
			{
				processedFiles.add(entry.getFilename());
				try
				{
					sftpChannel.get(
							new StringBuilder().append(sftpDetails.getSourcePath()).append(B2badvanceCoreConstants.SFTP_SERVER_SEPARATOR)
									.append(entry.getFilename()).toString(),
							new StringBuilder(sftpDetails.getDestPath()).append(File.separator).append(entry.getFilename()).toString());
					LOG.info("Downloaded file : " + entry.getFilename());
					successFiles.add(entry.getFilename());
				}
				catch (final SftpException sftpException)
				{
					hasError = true;
					errorFiles.add(entry.getFilename());
					LOG.error("Failed to download the file " + entry.getFilename(), sftpException);
				}
			}
		}
		return hasError;
	}

	/**
	 * This method sets sftp channel
	 *
	 * @param sftpdto
	 *           the sftp
	 * @param sftpdto
	 * @return sftp channel
	 * @throws JSchException
	 *            if there is no SFTP details available or there is any issue while opening SFTP session
	 */
	private Channel setupSFTPChannel(final B2BAdvanceSFTPDetails sftpdto) throws JSchException
	{
		Channel channel = null;
		JSch.setLogger(b2bAdvanceJSchLogger);
		if (StringUtils.isNotEmpty(sftpdto.getUsername()) && StringUtils.isNotEmpty(sftpdto.getHostname())
				&& StringUtils.isNotEmpty(sftpdto.getPort()))
		{
			final JSch jsch = new JSch();
			session = jsch.getSession(sftpdto.getUsername(), sftpdto.getHostname(), Integer.parseInt(sftpdto.getPort()));
			session.setConfig(STRICT_HOST_KEY_CHECKING, "no");
			session.setPassword(sftpdto.getPassword());
			session.connect();
			channel = session.openChannel(SFTP);
			channel.connect();
		}
		else
		{
			throw new JSchException("No SFTP Detail Available");
		}
		return channel;
	}

	/**
	 * This method is used file by its name.
	 *
	 * @param entry
	 * @param filePattern
	 * @param lastJobStartTime
	 * @return true if file is valid
	 */
	private boolean validFile(final LsEntry entry, final Pattern filePattern, final Date lastJobStartTime)
	{
		boolean fileIsValid = false;
		final String fileName = entry.getFilename();
		if (filePattern.matcher(fileName).matches())
		{
			if (lastJobStartTime == null)
			{
				fileIsValid = true;
			}
			else
			{
				fileIsValid = compareWithLastJobTime(fileName, lastJobStartTime);
			}
		}
		return fileIsValid;
	}

	/**
	 * This method compares Last cronjob time with date mentioned in file name.
	 *
	 * @param fileName
	 * @param lastJobStartTime
	 * @return boolean true if file is valid
	 */
	private boolean compareWithLastJobTime(final String fileName, final Date lastJobStartTime)
	{
		boolean fileIsValid = false;
		final SimpleDateFormat df = new SimpleDateFormat(B2badvanceCoreConstants.SFTP_DATE_REGEX);
		try
		{
			//changed to include milliseconds in the date
			final Date csvDate = df.parse(String.valueOf(fileName.substring(fileName.length() - 20, fileName.length() - 4)));
			if (csvDate.after(lastJobStartTime))
			{
				fileIsValid = true;
			}
		}
		catch (final ParseException parseException)
		{
			LOG.error("Error occured while validating invalid file: " + fileName, parseException);
		}
		return fileIsValid;
	}

	@Required
	public void setB2bAdvanceJSchLogger(final B2BAdvanceJSchLogger b2bAdvanceJSchLogger)
	{
		this.b2bAdvanceJSchLogger = b2bAdvanceJSchLogger;
	}
}
