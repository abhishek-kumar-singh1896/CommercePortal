/**
 *
 */
package com.enterprisewide.b2badvance.core.batch.converter.impl;

import com.enterprisewide.b2badvance.core.batch.util.B2badvanceImportUtil;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.ImpexRowFilter;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.impl.DefaultImpexConverter;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.impl.NullImpexRowFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * @author Enterprise Wide
 *
 *         Customized converter to cover the cases when one CSV row will produce multiple impex rows. This class still
 *         comes with a number of limitations though.
 *         <p>
 *         It is assumed that there may be a number of static values (values that will remain the same over each impex
 *         row). It is then assumed that starting on a specific index of the CSV, defined by startDynamicValueIndex, the
 *         remaining values will be treated as the dynamic value, and for each CSV column after this index, one new
 *         impex row will be created. In the impex header, the dynamic value must be stated using the special syntax
 *         {dynamic}.
 *         <p>
 *         EXAMPLE: We have an impex placeholder that looks like this:<br>
 *         {0};{1};{dynamic}<br>
 *
 *         We have a CSV row that looks like the following:<br>
 *         PROD-CODE,img1.jpg,img2.jpg,img3.jpg,img4.jpg<br>
 *         We configure the startDynamicValueIndex to be 2.<br>
 *         We will then get the following impex rows:<br>
 *         PROD-CODE;img1.jpg;img2.jpg<br>
 *         PROD-CODE;img1.jpg;img3.jpg<br>
 *         PROD-CODE;img1.jpg;img4.jpg<br>
 *         <p>
 *         NOTE: The definition of the static values {0}, {1} etc must be strictly matched! I.e: it is NOT allowed to
 *         use the {+0} syntax. It has to be a number surrounded by curly brackets.
 *         <p>
 *         NOTE 2: If no dynamic values are found at all (i.e. not enough columns in the CSV), this converter will not
 *         blow up, but rather return empty string.
 *         <p>
 *         This class extends the {@link DefaultImpexConverter}, but it uses nothing of the converter-specific logic of
 *         that class.
 *
 *
 */
public class B2badvanceMultipleRowsImpexConverter extends DefaultImpexConverter
{
	public static final String DYNAMIC_VALUE_PLACEHOLDER = "{dynamic}";
	public static final String QUALIFIER_VALUE_PLACEHOLDER = "{qualifier}";

	private static final char BRACKET_END = '}';
	private static final char BRACKET_START = '{';

	private final Logger LOG = Logger.getLogger(getClass());

	private String header;
	private String impexRow;
	private String type;
	private ImpexRowFilter rowFilter = new NullImpexRowFilter();
	private int startDynamicValueIndex;

	private B2badvanceImportUtil importUtil;



	@Override
	public String convert(final Map<Integer, String> row, final Long sequenceId)
	{

		if (row.size() < 1)
		{
			/*
			 * contiigoLogService.logEvent(LogEventType.MEDIA_IMPORT, LogEventStatus.FAILED,
			 * "Empty Zip file or files with improper naming conventions", Severity.WARN);
			 */
			throw new IllegalArgumentException("Empty Zip file or files with improper naming conventions");
		}
		final Set<Integer> keys = row.keySet();

		final StringBuilder allRows = new StringBuilder();

		for (int i = startDynamicValueIndex; i < keys.size(); i++)
		{
			String oneImpexRow = impexRow;
			final int staticValuesIndex = Math.min(keys.size(), startDynamicValueIndex);
			for (int j = 0; j < staticValuesIndex; j++)
			{
				final String onePlaceholder = createNumericPlaceholderRegexpPattern(j);
				oneImpexRow = oneImpexRow.replaceAll(onePlaceholder, row.get(j));
			}
			if (!row.get(i).isEmpty())
			{
				if (oneImpexRow.contains(QUALIFIER_VALUE_PLACEHOLDER))
				{
					final String qualifier = getImportUtil().getMediaContainerQualifier(row.get(i));
					oneImpexRow = oneImpexRow.replaceAll(Pattern.quote(QUALIFIER_VALUE_PLACEHOLDER), qualifier);
				}
				oneImpexRow = oneImpexRow.replaceAll(Pattern.quote(DYNAMIC_VALUE_PLACEHOLDER), row.get(i));
				allRows.append(oneImpexRow);
				allRows.append("\n");
			}
		}
		final String returnString = StringUtils.removeEnd(allRows.toString(), "\n");
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Returning impex rows: " + returnString);
		}
		return returnString;
	}

	/**
	 * Creates a placeholder for a value given a number. For example: given 3 the returned pattern String will match {3},
	 * if given as input to a regexp parser.
	 *
	 * @param i
	 *           Number
	 * @return The resulting regexp pattern.
	 */
	private String createNumericPlaceholderRegexpPattern(final Integer i)
	{
		return Pattern.quote(BRACKET_START + i.toString() + BRACKET_END);
	}

	/**
	 * @param startDynamicValueIndex
	 *           the startDynamicValueIndex to set
	 */
	public void setStartDynamicValueIndex(final int startDynamicValueIndex)
	{
		this.startDynamicValueIndex = startDynamicValueIndex;
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

	public void setImportUtil(final B2badvanceImportUtil b2badvanceImportUtil)
	{
		this.importUtil = b2badvanceImportUtil;
	}

}
