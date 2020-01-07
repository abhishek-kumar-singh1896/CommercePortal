package com.enterprisewide.b2badvance.core.sftp;

import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;
import com.enterprisewide.b2badvance.core.util.B2badvanceCoreUtil;


/**
 * This class contains all the sftp and file management related methods.
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceSFTPFileHandler
{
	private static final Logger LOG = Logger.getLogger(B2BAdvanceSFTPFileHandler.class);
	private String sftpDataBaseDirectory;
	private B2BAdvanceSFTPClient b2bAdvanceSFTPClient;

	/**
	 * Create initial directories required to save and process the sftp product csvs
	 *
	 * @param dirMap
	 * @param importType
	 * @return boolean hasError, code sets its value to true in case of any exception.
	 */
	public boolean createDirectories(final Map<String, File> dirMap, final String importType)
	{
		boolean hasError = false;
		try
		{
			dirMap.put(B2badvanceCoreConstants.SFTP_FILES_LOCATION,
					createDirIfNotExists(B2badvanceCoreConstants.SFTP_FILES_LOCATION + "/" + importType));

			final String csvString = new StringBuilder().append(B2badvanceCoreConstants.CSV_FILES_LOCATION).append(File.separator)
					.toString();
			dirMap.put(importType, createDirIfNotExists(new StringBuilder().append(csvString).append(importType).toString()));
		}
		catch (final FileNotFoundException exception)
		{
			hasError = true;
			LOG.error("Error occured while creating diectory for import csv functionality", exception);
		}
		return hasError;
	}

	/**
	 * This method creates a directory if that doesn't exist.
	 *
	 * @param loc
	 * @return File, on sucessfully creation in given location.
	 * @throws FileNotFoundException
	 */
	public File createDirIfNotExists(final String loc) throws FileNotFoundException
	{
		final String dirName = new StringBuilder().append(sftpDataBaseDirectory).append(File.separator).append(File.separator)
				.append(loc).toString();
		LOG.info("Creating Dir : " + dirName);

		final File dir = new File(dirName);
		if (!dir.exists() && !dir.mkdirs())
		{
			LOG.error("Error occured while creating directory : " + dirName);
			throw new FileNotFoundException("Error occured while creating directory : " + dirName);
		}
		return dir;
	}

	/**
	 * Download Files from SFTP Server to Hybris temp folder.
	 *
	 * @param sftpFilesDir
	 * @param sftpFileTypeRegex
	 * @param lastJobStartTime
	 * @return boolean hasError, code sets its value to true in case of any exception.
	 */
	public boolean downloadFilesFromServer(final File sftpFilesDir, final String sftpFileTypeRegex, final Date lastJobStartTime)
	{
		final String fileMatchingRegex = getFileMatchingRegex(sftpFileTypeRegex);
		final B2BAdvanceSFTPFilesResponse sftpFilesResponse = b2bAdvanceSFTPClient
				.downloadFileFromSFTPServer(getSFTPDetails(fileMatchingRegex, sftpFilesDir.getPath()), lastJobStartTime);
		if (CollectionUtils.isNotEmpty(sftpFilesResponse.getErrorFiles()))
		{
			//TODO Trigger Email
		}
		return sftpFilesResponse.getHasError();
	}

	/**
	 * Returns the regex to match files to be downloaded from sftp server
	 *
	 * @param sftpFileTypeRegex
	 *
	 * @return fileMatchingRegex
	 */
	private String getFileMatchingRegex(final String sftpFileTypeRegex)
	{
		final String fileMatchingRegex = new StringBuilder().append(sftpFileTypeRegex)
				.append(B2badvanceCoreConstants.SFTP_FILE_MATCHING_SUFFIX_REGEX).toString();
		return fileMatchingRegex;
	}

	/**
	 * This method sets and returns SFTP DTO.
	 *
	 * @param fileRegex
	 * @param sftpFolderPath
	 * @return B2badvanceSFTPDetails, containing all the SFTP details to establish connection.
	 */
	private B2BAdvanceSFTPDetails getSFTPDetails(final String fileRegex, final String sftpFolderPath)
	{
		final B2BAdvanceSFTPDetails sftpDetails = new B2BAdvanceSFTPDetails();
		sftpDetails.setHostname(Config.getParameter(B2badvanceCoreConstants.SFTP_HOST_KEY));
		sftpDetails.setPort(Config.getParameter(B2badvanceCoreConstants.SFTP_PORT_KEY));
		sftpDetails.setUsername(Config.getParameter(B2badvanceCoreConstants.SFTP_USERNAME_KEY));
		sftpDetails.setPassword(Config.getParameter(B2badvanceCoreConstants.SFTP_PASSWORD_KEY));
		sftpDetails.setSourcePath(Config.getParameter(B2badvanceCoreConstants.SFTP_DIRECTORY_KEY));
		sftpDetails.setDestPath(sftpFolderPath);
		sftpDetails.setFileRegex(fileRegex);
		return sftpDetails;
	}

	/**
	 * This method download all the required csv files from the sftp server and returns list of invoice files in a map
	 *
	 * @param sftpFilesDir
	 * @param string
	 * @param sftpFileTypeRegexProduct
	 * @param lastJobStartTime
	 * @param csvMap
	 * @return boolean hasError, code sets its value to true in case of any exception.
	 */
	public boolean populateCsvFiles(final File sftpFilesDir, final String importType, final String sftpFileTypeRegex,
			final Date lastJobStartTime, final Map<String, List<File>> csvMap)
	{
		final boolean hasError = downloadFilesFromServer(sftpFilesDir, sftpFileTypeRegex, lastJobStartTime);
		final File[] sftpFiles = sftpFilesDir.listFiles();
		if (ArrayUtils.isEmpty(sftpFiles))
		{
			LOG.info("No file found in SFTP folder to import data");
		}
		else
		{
			sortByFileNumber(sftpFiles);
			csvMap.put(importType, new ArrayList<>());
			for (final File fileEntry : sftpFiles)
			{
				if (fileEntry != null && fileEntry.isFile())
				{
					if (fileEntry.getName().contains(importType))
					{
						csvMap.get(importType).add(fileEntry);
					}
				}
			}
		}
		return hasError;
	}

	/**
	 * This method cleans all the files in SFTP directory.
	 *
	 * @param dirMap
	 */
	public void cleanSFTPLocalDirectories(final File sftpDir)
	{
		try
		{
			B2badvanceCoreUtil.cleanDirectory(sftpDir);
		}
		catch (final IOException ioException)
		{
			LOG.error("Unable to clean directories", ioException);
		}
	}

	/**
	 * This method is used to sort file by date mentioned in filename.
	 *
	 * @param sftpFiles
	 */
	private void sortByFileNumber(final File[] sftpFiles)
	{
		Arrays.sort(sftpFiles, new Comparator<File>()
		{
			@Override
			public int compare(final File o1, final File o2)
			{
				final long n1 = extractNumber(o1.getName());
				final long n2 = extractNumber(o2.getName());
				return (int) (n1 - n2);
			}

			private long extractNumber(final String fileName)
			{
				long fileNo = 0;
				try
				{
					fileNo = Long.parseLong(fileName.substring(fileName.length() - 18, fileName.length() - 4));
				}
				catch (final IndexOutOfBoundsException exception)
				{
					// if filename does not match the format return default 0
					LOG.error("Error occured while validating invalid file: " + fileName, exception);
				}
				return fileNo;
			}
		});
	}

	@Required
	public void setSftpDataBaseDirectory(final String sftpDataBaseDirectory)
	{
		this.sftpDataBaseDirectory = sftpDataBaseDirectory;
	}

	@Required
	public void setB2bAdvanceSFTPClient(final B2BAdvanceSFTPClient b2badvanceSFTPClient)
	{
		this.b2bAdvanceSFTPClient = b2badvanceSFTPClient;
	}
}
