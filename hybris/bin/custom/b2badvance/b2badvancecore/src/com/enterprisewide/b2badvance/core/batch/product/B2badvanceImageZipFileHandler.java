/**
 *
 */
package com.enterprisewide.b2badvance.core.batch.product;

import de.hybris.platform.util.Config;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * @author Enterprise Wide
 *
 *         Zip Import handler
 */
public class B2badvanceImageZipFileHandler
{
	private String imagePath;

	private String csvPath;

	private String zipFilePath;

	private String errorFilePath;

	private String allowedExtensions;

	private boolean temporaryZipCreated;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final static String VARIANT_IMAGE_SUFFIX = "var";
	private final static String SEPARATOR = "_";
	private final static String ERROR_FOLDER = Config.getParameter("media.import.error.zip.location");
	private static final String FILE_NAMESPACE = "/";
	private static final String ERROR_PREFIX = "error_";
	private static final String PROCESSING_FOLDER = "/processing/";

	/**
	 * Unzips the file and create CSV rows from the extracted images
	 *
	 * @param file
	 */
	public File createUnzipAndCreateCSV(final File file)
	{
		final PrintWriter errorWriter = null;
		List<String> unzipFileNames = null;

		try
		{

			unzipFileNames = unzip(file, new File(imagePath));
			final Comparator<String> cmp = Collections.reverseOrder();

			// sort the list
			Collections.sort(unzipFileNames);
			final Map<String, CSVLine> csvLines = createCSVLines(unzipFileNames, file);
			if (!temporaryZipCreated || file.exists())
			{
				Files.delete(file.toPath());
			}

			return createCSVFile(csvLines);

		}
		catch (final IOException e)
		{
			log.error("Error reading zip file", e);
			errorFolder(file);
		}
		finally
		{
			IOUtils.closeQuietly(errorWriter);
		}
		return null;
	}

	/**
	 * Create CSV lines from the extracted image file names
	 *
	 * @param unzipFileNames
	 * @param file
	 */
	private Map<String, CSVLine> createCSVLines(final List<String> unzipFileNames, final File file)
	{

		final Map<String, CSVLine> lines = new HashMap<>();

		for (final String fileName : unzipFileNames)
		{

			final String imageSuffix = getImageNameSuffix(fileName, file);
			if (imageSuffix != null && imageSuffix.substring(0, imageSuffix.lastIndexOf(".")).equalsIgnoreCase(VARIANT_IMAGE_SUFFIX))
			{

				final String productId = getProductIdForVariant(fileName, file);
				if (imageSuffix.substring(0, imageSuffix.lastIndexOf(".")).equalsIgnoreCase(VARIANT_IMAGE_SUFFIX))
				{
					// 'pack' suffix -> Packaging image
					final CSVLine csvLine = createOrGetCSVLine(lines, productId);
					csvLine.setMainPicture(fileName);
				}

				else
				{
					log.info("Invalid filename " + fileName);
				}
			}
			else
			{
				final String productId = getProductId(fileName, file);


				if (StringUtils.isEmpty(imageSuffix))
				{
					// No suffix -> Main image
					final CSVLine csvLine = createOrGetCSVLine(lines, productId.substring(0, productId.lastIndexOf(".")));
					csvLine.setMainPicture(fileName);
				}
				else if (imageSuffix.substring(0, imageSuffix.lastIndexOf(".")).equalsIgnoreCase(VARIANT_IMAGE_SUFFIX))
				{
					// 'pack' suffix -> Packaging image
					final CSVLine csvLine = createOrGetCSVLine(lines, productId.substring(0, productId.lastIndexOf(".")));
					csvLine.setMainPicture(fileName);
				}
				else if (NumberUtils.isDigits(imageSuffix.substring(0, imageSuffix.lastIndexOf("."))))
				{
					// Assuming the imageSuffix is a number for gallery images here
					final CSVLine csvLine = createOrGetCSVLine(lines, productId);
					csvLine.getGalleryPictures().add(fileName);
				}
				else
				{
					log.info("Invalid filename " + fileName);
				}
			}


		}

		return lines;
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
			final String encoding, final String fileName) throws UnsupportedEncodingException, FileNotFoundException
	{
		PrintWriter result = errorWriter;
		if (result == null)
		{
			result = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getErrorFile(file, fileName)),
					encoding)));
		}
		result.print("File Name is :" + file.getName() + "Error Cause is:-->Extension is not valid.");
		return result;
	}

	/**
	 * Returns the error file
	 *
	 * @param file
	 * @return the error file
	 */
	protected File getErrorFile(final File file, final String fileName)
	{
		final File errorFolderPath = new File(errorFilePath);
		if (!errorFolderPath.exists())
		{
			errorFolderPath.mkdir();
		}
		final String tempFileName = errorFilePath + FILE_NAMESPACE + ERROR_PREFIX + fileName;
		final File errorFile = new File(tempFileName);
		return errorFile;
	}

	private String getProductId(final String fileName, final File file)
	{

		final String productId = fileName.contains(SEPARATOR) ? fileName.substring(0, fileName.lastIndexOf(SEPARATOR))
				: fileName;
		return productId;
	}


	private String getProductIdForVariant(final String fileName, final File file)
	{
		final String tempFileName = fileName.substring(0, fileName.lastIndexOf("."));
		return tempFileName.substring(0, tempFileName.lastIndexOf(VARIANT_IMAGE_SUFFIX) - 1);
	}

	private String getImageNameSuffix(final String fileName, final File file)
	{
		final String[] split = fileName.split(SEPARATOR);
		if (split.length > 1)
		{
			return split[split.length - 1];
		}
		return null;
	}

	private CSVLine createOrGetCSVLine(final Map<String, CSVLine> lines, final String productId)
	{
		CSVLine csvLine = lines.get(productId);

		if (csvLine == null)
		{
			csvLine = new CSVLine();
			lines.put(productId, csvLine);
		}
		return csvLine;
	}

	/**
	 * Moves the zip folder to error folder
	 *
	 * @param file
	 */
	public void errorFolder(final File file)
	{
		try
		{
			final File errorFile = new File(ERROR_FOLDER);
			if (!errorFile.exists())
			{
				errorFile.mkdirs();
			}
			final Path errorPath = Paths.get(ERROR_FOLDER + "/" + file.getName());
			Files.copy(file.toPath(), errorPath, StandardCopyOption.REPLACE_EXISTING);
			Files.deleteIfExists(file.toPath());
		}
		catch (final IOException e)
		{

			log.error("Error reading zip file", e);
		}

	}

	/**
	 * Create CSV file containing multiple CSV rows for impex generation
	 *
	 * @param csvLines
	 * @throws IOException
	 */
	private File createCSVFile(final Map<String, CSVLine> csvLines) throws IOException
	{
		final List<String> outPutLines = new ArrayList<>();

		for (final Entry<String, CSVLine> entry : csvLines.entrySet())
		{
			String line = entry.getKey();
			final String delimiter = ",";
			line += delimiter;

			final CSVLine value = entry.getValue();
			line += value.getMainPicture() + delimiter;
			line += value.getPackagingImage() + delimiter;
			for (final String galleryPic : value.getGalleryPictures())
			{
				line += galleryPic + delimiter;
			}

			line = StringUtils.removeEnd(line, delimiter);
			outPutLines.add(line);

		}
		Files.write(Paths.get(csvPath), outPutLines, Charset.defaultCharset());
		return new File(csvPath);
	}

	/**
	 * Unzips all files in the zipFile into the targetDir and returns the unzipped files.
	 */
	public List<String> unzip(final File zipFile, final File targetDir) throws IOException
	{
		final List<File> files = new ArrayList<File>();
		File tempZipFile = zipFile;
		final String tempZipPath = zipFilePath + PROCESSING_FOLDER + zipFile.getName();
		if (!zipFile.exists())
		{
			temporaryZipCreated = true;
			tempZipFile = new File(tempZipPath);
		}
		final ZipFile zip = new ZipFile(tempZipFile);
		final List<String> fileNames = new ArrayList<>();
		try
		{
			for (final ZipEntry entry : Collections.list(zip.entries()))
			{
				if (!entry.isDirectory())
				{
					final InputStream input = zip.getInputStream(entry);
					try
					{
						if (!targetDir.exists())
						{
							targetDir.mkdirs();
						}
						final File target = new File(targetDir, entry.getName().substring(entry.getName().indexOf("/")));
						if (!checkAndCreateErrorFile(target))
						{
							FileUtils.copyInputStreamToFile(input, target);
							fileNames.add(target.getName());
							files.add(target);
						}

					}
					finally
					{
						IOUtils.closeQuietly(input);
					}
				}
			}
			//			Files.delete(tempZipFile.toPath());
			return fileNames;
		}
		finally
		{
			zip.close();
		}
	}

	/**
	 * This method is to check the extensions of input file and return variable.
	 *
	 * @param file
	 * @return
	 */
	private boolean checkAndCreateErrorFile(final File file)
	{
		PrintWriter errorWriter = null;
		boolean extVar = false;
		final String fileName = file.getName();
		final String[] extArray = allowedExtensions.split(",");
		final List<String> extList = Arrays.asList(extArray);
		final String inputMediaExtension = fileName.substring(fileName.indexOf(".") + 1);


		if (!extList.contains(inputMediaExtension))
		{
			extVar = true;
			try
			{
				errorWriter = writeErrorLine(file, errorWriter, "", "UTF-8", fileName);
			}
			catch (final UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			catch (final FileNotFoundException e)
			{
				e.printStackTrace();
			}
			finally
			{
				IOUtils.closeQuietly(errorWriter);
			}
		}
		return extVar;
	}

	/**
	 *
	 * Class containing fields to represent a single CSV line
	 *
	 */
	private class CSVLine
	{

		private String mainPicture = "";
		private String packagingImage = "";
		private List<String> galleryPictures = new ArrayList<>();

		public String getMainPicture()
		{
			return mainPicture;
		}

		public void setMainPicture(final String mainPicture)
		{
			this.mainPicture = mainPicture;
		}

		public List<String> getGalleryPictures()
		{
			return galleryPictures;
		}

		@SuppressWarnings("unused")
		public void setGalleryPictures(final List<String> galleryPictures)
		{
			this.galleryPictures = galleryPictures;
		}

		public String getPackagingImage()
		{
			return packagingImage;
		}

		public void setPackagingImage(final String packagingImage)
		{
			this.packagingImage = packagingImage;
		}

	}

	public String getImagePath()
	{
		return imagePath;
	}

	public void setImagePath(final String imagePath)
	{
		this.imagePath = imagePath;
	}

	public String getCsvPath()
	{
		return csvPath;
	}

	public void setCsvPath(final String csvPath)
	{
		this.csvPath = csvPath;
	}

	public String getAllowedExtensions()
	{
		return allowedExtensions;
	}

	public void setAllowedExtensions(final String allowedExtensions)
	{
		this.allowedExtensions = allowedExtensions;
	}


	public String getErrorFilePath()
	{
		return errorFilePath;
	}


	public void setErrorFilePath(final String errorFilePath)
	{
		this.errorFilePath = errorFilePath;
	}

	/**
	 * @return the zipFilePath
	 */
	public String getZipFilePath()
	{
		return zipFilePath;
	}

	/**
	 * @param zipFilePath
	 *           the zipFilePath to set
	 */
	public void setZipFilePath(final String zipFilePath)
	{
		this.zipFilePath = zipFilePath;
	}

	/**
	 * @return the temporaryZipCreated
	 */
	public boolean isTemporaryZipCreated()
	{
		return temporaryZipCreated;
	}

	/**
	 * @param temporaryZipCreated
	 *           the temporaryZipCreated to set
	 */
	public void setTemporaryZipCreated(final boolean temporaryZipCreated)
	{
		this.temporaryZipCreated = temporaryZipCreated;
	}

}
