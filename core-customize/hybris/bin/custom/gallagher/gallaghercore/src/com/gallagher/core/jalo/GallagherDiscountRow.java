package com.gallagher.core.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.europe1.jalo.DiscountRow;
import de.hybris.platform.europe1.jalo.PDTRow;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.persistence.GenericItemEJBImpl;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JaloPropertyContainer;
import de.hybris.platform.util.StandardDateRange;

import java.util.Date;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;


/**
 * The custom jalo class for Price Row. Handles the Sales Area specific pricing.
 *
 * @author Nagarro-Dev
 */
@SuppressWarnings("deprecation")
public class GallagherDiscountRow extends DiscountRow
{

	public static final String SALES_AREA = "salesArea";
	public static final String SALES_AREA_MATCHQUALIFIER = "salesAreaMatchQualifier";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		allAttributes.put(SALES_AREA_MATCHQUALIFIER, getInitialSalesAreaMatchField(allAttributes));

		allAttributes.setAttributeMode(SALES_AREA, AttributeMode.INITIAL);
		allAttributes.setAttributeMode(SALES_AREA_MATCHQUALIFIER, AttributeMode.INITIAL);


		final GallagherDiscountRow ret1 = (GallagherDiscountRow) createDiscountRowItem(ctx, type, allAttributes);
		ret1.markProductModified();
		return ret1;
	}

	/**
	 * Method to create the Price Row Item. Created from the createItem method of PriceRow.class to change the
	 * calculateMatchValue method signature.
	 *
	 * @param ctx
	 *           the session context
	 * @param type
	 *           the composed type
	 * @param allAttributes
	 *           the item attribute map
	 * @return the price row item
	 * @throws JaloBusinessException
	 *            the jalo business exception
	 */
	protected Item createDiscountRowItem(final SessionContext ctx, final ComposedType type,
			final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		final HashSet missing = new HashSet();

		checkMandatoryAttribute("currency", allAttributes, missing);

		if (!missing.isEmpty())
		{

			throw new JaloInvalidParameterException("missing price row attributes " + missing, 0);

		}
		else
		{
			if (allAttributes.get("minqtd") == null)
			{

				allAttributes.put("minqtd", Long.valueOf(1L));
			}
			if (allAttributes.get("net") == null)
			{

				allAttributes.put("net", Boolean.FALSE);
			}
			if (allAttributes.get("unitFactor") == null)
			{

				allAttributes.put("unitFactor", Integer.valueOf(1));
			}
			if (allAttributes.get("unit") == null)
			{

				final Product product = (Product) allAttributes.get("product");
				final Unit fallbackUnit = product != null ? product.getUnit() : null;

				if (fallbackUnit == null)
				{
					throw new JaloInvalidParameterException("missing unit for price row ", 0);
				}
				else
				{
					allAttributes.put("unit", fallbackUnit);
				}
			}

			allAttributes.setAttributeMode("minqtd", AttributeMode.INITIAL);
			allAttributes.setAttributeMode("currency", AttributeMode.INITIAL);
			allAttributes.setAttributeMode("net", AttributeMode.INITIAL);
			allAttributes.setAttributeMode("price", AttributeMode.INITIAL);
			allAttributes.setAttributeMode("unit", AttributeMode.INITIAL);
			allAttributes.setAttributeMode("unitFactor", AttributeMode.INITIAL);
			allAttributes.setAttributeMode("giveAwayPrice", AttributeMode.INITIAL);

			allAttributes.put("matchValue",
					Integer
							.valueOf(calculateMatchValue((Product) allAttributes.get("product"), (String) allAttributes.get("productId"),
									(EnumerationValue) allAttributes.get("pg"), (User) allAttributes.get("user"),
									(EnumerationValue) allAttributes.get("ug"), (String) allAttributes.get(SALES_AREA))));


			allAttributes.setAttributeMode("matchValue", AttributeMode.INITIAL);

			PRODUCTHANDLER.newInstance(ctx, allAttributes);

			final DiscountRow ret1 = (DiscountRow) createPDTItem(ctx, type, allAttributes);
			return ret1;
		}
	}

	/**
	 * Method to create the PDT item. Created from createItem method of PDT.class
	 *
	 * @param ctx
	 *           the session context
	 * @param type
	 *           the composed type
	 * @param allAttributes
	 *           the item attribute map
	 * @return the pdt row item
	 * @throws JaloBusinessException
	 *            the jalo business exception
	 */
	protected Item createPDTItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		if (moreThanOneReferenceAssigned(allAttributes))
		{

			throw new JaloPriceFactoryException("You can only set only one of the following: PRODUCT, PG, PRODUCTID", 0);

		}
		else if (allAttributes.get("user") != null && allAttributes.get("ug") != null)
		{

			throw new JaloPriceFactoryException("cannot set both USER and UG - set just one of them", 0);

		}
		else
		{
			allAttributes.put("productMatchQualifier", getInitialProductMatchField(allAttributes));
			allAttributes.put("userMatchQualifier", getInitialUserMatchField(allAttributes));

			allAttributes.setAttributeMode("product", AttributeMode.INITIAL);
			allAttributes.setAttributeMode("pg", AttributeMode.INITIAL);
			allAttributes.setAttributeMode("productMatchQualifier", AttributeMode.INITIAL);

			allAttributes.setAttributeMode("user", AttributeMode.INITIAL);
			allAttributes.setAttributeMode("ug", AttributeMode.INITIAL);
			allAttributes.setAttributeMode("userMatchQualifier", AttributeMode.INITIAL);

			final StandardDateRange dateRange = (StandardDateRange) allAttributes.get("dateRange");
			final Date startTime = (Date) allAttributes.get("startTime");
			final Date endTime = (Date) allAttributes.get("endTime");
			if (dateRange != null)
			{

				if (startTime != null && !startTime.equals(dateRange.getStart()))
				{

					throw new JaloInvalidParameterException(
							"cannot specify both dateRange=" + dateRange + " and " + "startTime" + "=" + startTime + " attributes", -1);

				}

				allAttributes.put("startTime", dateRange.getStart());
				if (endTime != null && !endTime.equals(dateRange.getEnd()))
				{

					throw new JaloInvalidParameterException(
							"cannot specify both dateRange=" + dateRange + " and " + "endTime" + "=" + startTime + " attributes", -1);
				}

				allAttributes.put("endTime", dateRange.getEnd());
				allAttributes.remove("dateRange");


			}
			else
			{
				allAttributes.put("startTime", startTime);
				allAttributes.put("endTime", endTime);
			}
			allAttributes.setAttributeMode("startTime", AttributeMode.INITIAL);
			allAttributes.setAttributeMode("endTime", AttributeMode.INITIAL);

			final PDTRow result = (PDTRow) createGenericItem(ctx, type, allAttributes);
			return result;
		}
	}

	/**
	 * Method to create the generic item. Created from the createItem method of GenericItem.class
	 *
	 * @param ctx
	 *           the session context
	 * @param type
	 *           the composed type
	 * @param allAttributes
	 *           the item attribute map
	 * @return the generic item
	 * @throws JaloBusinessException
	 *            the jalo business exception
	 */
	protected Item createGenericItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		final JaloSession session = type.getSession();

		GenericItem genericItem;
		try
		{
			if (ctx != null)
			{
				session.createLocalSessionContext(ctx);
			}
			else
			{
				session.createLocalSessionContext().setLanguage((Language) null);
			}

			JaloPropertyContainer props = getInitialProperties(session, allAttributes);

			final Date creationTime = (Date) allAttributes.get(CREATION_TIME);
			final Item owner = (Item) allAttributes.get(OWNER);
			if (creationTime != null || owner != null)
			{
				if (props == null)
				{
					props = session.createPropertyContainer();
				}

				if (creationTime != null)
				{
					props.setProperty(CREATION_TIME, creationTime);
				}
				if (owner != null)
				{
					props.setProperty(OWNER, owner);
				}
			}

			allAttributes.checkConsistency();

			genericItem = GenericItemEJBImpl.createGenericItem(getTenant(), (PK) allAttributes.get(Item.PK), type, props);
		}
		finally
		{
			session.removeLocalSessionContext();
		}

		return genericItem;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void markProductModified()
	{
		final boolean markProductModifiedSession = Boolean.TRUE
				.equals(getSession().getSessionContext().getAttribute("pdtrow.mark.product.modified"));

		final boolean markProductModified = Config.getBoolean("pdtrow.mark.product.modified", false);
		if ((markProductModifiedSession || markProductModified) && isAlive())
		{
			markProductModified(getProduct());
		}

	}

	/**
	 * Method to mark the product as modified.
	 *
	 * @param product
	 *           the product
	 */
	private void markProductModified(final Product product)
	{
		if (product != null && !isCurrentlyRemoving(product))
		{

			product.setModificationTime(new Date());
		}
	}



	/**
	 * Method to check if more than one reference for product assigned.
	 *
	 * @param allAttributes
	 *           the item attribute map
	 * @return referenceCounter if more than one reference assigned
	 */
	private boolean moreThanOneReferenceAssigned(final ItemAttributeMap allAttributes)
	{
		int referenceCounter = 0;
		if (allAttributes.get("product") != null)
		{
			++referenceCounter;
		}
		if (allAttributes.get("pg") != null)
		{
			++referenceCounter;
		}
		if (allAttributes.get("productId") != null)
		{
			++referenceCounter;
		}
		return referenceCounter > 1;
	}

	/**
	 * Method to set the initial salesArea match field.
	 *
	 * @param allAttributes
	 *           the item attribute map
	 * @return the salesArea match field
	 */
	protected String getInitialSalesAreaMatchField(final ItemAttributeMap allAttributes)
	{
		final String salesArea = (String) allAttributes.get(SALES_AREA);

		return salesArea != null ? salesArea : StringUtils.EMPTY;
	}


	/**
	 * Method to calculate the match value
	 *
	 * @param product
	 *           the product
	 * @param productCode
	 *           the product code
	 * @param productGroup
	 *           the product group
	 * @param user
	 *           the user
	 * @param userGroup
	 *           the user group
	 * @param salesArea
	 *           the Sales Area
	 * @return the match value
	 */
	protected int calculateMatchValue(final Product product, final String productCode, final EnumerationValue productGroup,
			final User user, final EnumerationValue userGroup, final String salesArea)
	{
		final boolean _product = product != null || productCode != null;
		final boolean _productGroup = productGroup != null;
		final boolean _user = user != null;
		final boolean _userGroup = userGroup != null;
		final boolean _salesArea = salesArea != null;

		int value = 0;
		if (_product)
		{
			if (_user)
			{
				if (_salesArea)
				{
					value = 18;
				}
				else
				{
					value = 14;
				}
			}
			else if (_userGroup)
			{
				if (_salesArea)
				{
					value = 16;
				}
				else
				{
					value = 12;
				}
			}
			else
			{
				if (_salesArea)
				{
					value = 10;
				}
				else
				{
					value = 8;
				}
			}
		}
		else if (_productGroup)
		{
			if (_user)
			{
				if (_salesArea)
				{
					value = 17;
				}
				else
				{
					value = 13;
				}
			}
			else if (_userGroup)
			{
				if (_salesArea)
				{
					value = 15;
				}
				else
				{
					value = 11;
				}
			}
			else
			{
				if (_salesArea)
				{
					value = 9;
				}
				else
				{
					value = 7;
				}
			}
		}
		else
		{
			if (_user)
			{
				if (_salesArea)
				{
					value = 6;
				}
				else
				{
					value = 4;
				}
			}
			else if (_userGroup)
			{
				if (_salesArea)
				{
					value = 5;
				}
				else
				{
					value = 3;
				}
			}
			else
			{
				if (_salesArea)
				{
					value = 2;
				}
				else
				{
					value = 1;
				}
			}
		}

		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	protected int calculateMatchValue(final Product product, final String productCode, final EnumerationValue productGroup,
			final User user, final EnumerationValue userGroup)
	{
		final SessionContext sessionContext = getSession().getSessionContext();
		return calculateMatchValue(product, productCode, productGroup, user, userGroup, getSalesArea(sessionContext));
	}

	/**
	 * Method to get the salesArea from the session context
	 *
	 * @return the salesArea
	 */
	public String getSalesArea()
	{
		return this.getSalesArea(this.getSession().getSessionContext());
	}

	/**
	 * Method to get the salesArea from the session context
	 *
	 * @param sessionContext
	 *           the session context
	 * @return the salesArea
	 */
	protected String getSalesArea(final SessionContext sessionContext)
	{
		return (String) getProperty(sessionContext, SALES_AREA);
	}

	@Override
	protected void setPg(final SessionContext ctx, final EnumerationValue value)
	{
		super.setPg(ctx, value);
		this.updateMatchValue();
	}

	protected void updateMatchValue()
	{
		this.setMatchValue(
				this.calculateMatchValue(this.getProduct(), this.getProductId(), this.getPg(), this.getUser(), this.getUg()));
	}

	public void setMatchValue(final SessionContext ctx, final Integer value)
	{
		this.setProperty(ctx, "matchValue", value);
	}

	public void setMatchValue(final Integer value)
	{
		this.setMatchValue(this.getSession().getSessionContext(), value);
	}

	public Integer getMatchValue(final SessionContext ctx)
	{
		return (Integer) this.getProperty(ctx, "matchValue");
	}

	public Integer getMatchValue()
	{
		return this.getMatchValue(this.getSession().getSessionContext());
	}
}
