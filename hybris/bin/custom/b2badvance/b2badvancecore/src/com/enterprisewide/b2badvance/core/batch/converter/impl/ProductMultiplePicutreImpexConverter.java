package com.enterprisewide.b2badvance.core.batch.converter.impl;

import com.enterprisewide.b2badvance.core.batch.util.B2badvanceImportUtil;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.ImpexRowFilter;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.impl.DefaultImpexConverter;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.impl.NullImpexRowFilter;
import de.hybris.platform.core.model.product.ProductModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;
import java.util.Set;


/**
 * Product Image Importer
 *
 * @author Enterprise Wide
 *
 */
public class ProductMultiplePicutreImpexConverter extends DefaultImpexConverter
{

	private static final String VALUE_DELIMITER = ",";

	private static final Logger LOG = LoggerFactory.getLogger(ProductMultiplePicutreImpexConverter.class);

	private final String PACK_IMAGE_SUFFIX = "pack";

	private B2badvanceImportUtil importUtil;

	private String header;
	private String impexRow;
	private String type;
	private int startDynamicValueIndex;

	/**
	 * @return the startDynamicValueIndex
	 */
	public int getStartDynamicValueIndex()
	{
		return startDynamicValueIndex;
	}

	/**
	 * @param startDynamicValueIndex
	 *           the startDynamicValueIndex to set
	 */
	public void setStartDynamicValueIndex(final int startDynamicValueIndex)
	{
		this.startDynamicValueIndex = startDynamicValueIndex;
	}

	private ImpexRowFilter rowFilter = new NullImpexRowFilter();

	@Override
	public String convert(final Map<Integer, String> row, final Long sequenceId)
	{
		if (row.size() < 2)
		{

			LOG.error("Can not handle row without fields");
			throw new IllegalArgumentException("Can not handle row without fields");
		}

		ProductModel product = importUtil.getProductForCode(row.get(0));

		if (product != null)
		{
			final String productCode = row.get(0);
			final String productPic = row.get(1);

			final Set<Integer> keys = row.keySet();

			String packagingImage = "";
			String galleryPics = "";

			for (int i = 2; i < keys.size(); i++)
			{

				final String rowValue = row.get(i);

				if (StringUtils.isNotEmpty(rowValue))
				{

					final String mediaContainerQualifier = getImportUtil().getMediaContainerQualifier(rowValue);
					if (StringUtils.isNotEmpty(mediaContainerQualifier))
					{
						if (StringUtils.startsWith(getImageSuffix(rowValue), PACK_IMAGE_SUFFIX))
						{
							packagingImage += mediaContainerQualifier + VALUE_DELIMITER;
							continue;
						}
						else
						{
							galleryPics += getImportUtil().getMediaContainerQualifier(rowValue) + VALUE_DELIMITER;
						}
					}
				}
			}

			// Adding product main image to gallery images as well
			if (StringUtils.isNotEmpty(productPic))
			{
				galleryPics = getImportUtil().getMediaContainerQualifier(productPic) + VALUE_DELIMITER + galleryPics;
			}

			packagingImage = StringUtils.removeEnd(packagingImage, VALUE_DELIMITER);
			packagingImage = StringUtils.removeStart(packagingImage, VALUE_DELIMITER);
			galleryPics = StringUtils.removeEnd(galleryPics, VALUE_DELIMITER);
			galleryPics = StringUtils.removeStart(galleryPics, VALUE_DELIMITER);

			String impexLine = impexRow.replace("{productCode}", productCode);
			impexLine = impexLine.replace("{productPicture}", productPic);
			impexLine = impexLine.replace("{packagingImage}", packagingImage);
			impexLine = impexLine.replace("{galleryImages}", galleryPics);

			return impexLine;

		}
		return "";
	}

	private String getImageSuffix(final String rowValue)
	{
		final String separater = "_";
		final String[] split = rowValue.split(separater);
		if (split.length >= 2)
		{
			final String imageSuffix = split[split.length - 1];
			return imageSuffix;
		}

		return null;
	}

	@Override
	public String getHeader()
	{
		return header;
	}

	@Override
	public void setHeader(final String header)
	{
		this.header = header;
	}

	public String getImpexRow()
	{
		return impexRow;
	}

	@Override
	public void setImpexRow(final String impexRow)
	{
		this.impexRow = impexRow;
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Override
	public void setType(final String type)
	{
		this.type = type;
	}

	public ImpexRowFilter getRowFilter()
	{
		return rowFilter;
	}

	@Override
	public void setRowFilter(final ImpexRowFilter rowFilter)
	{
		this.rowFilter = rowFilter;
	}

	public B2badvanceImportUtil getImportUtil()
	{
		return importUtil;
	}

	@Required
	public void setImportUtil(final B2badvanceImportUtil b2badvanceImportUtil)
	{
		this.importUtil = b2badvanceImportUtil;
	}

}
