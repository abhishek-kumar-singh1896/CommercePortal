package com.gallagher.core.search.solrfacetsearch.translator;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider.FieldType;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;
import de.hybris.platform.solrfacetsearch.provider.QualifierProviderAware;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFieldNameTranslator;

import org.apache.commons.lang3.StringUtils;

import com.gallagher.core.search.solrfacetsearch.provider.impl.GallagherProductDiscountValueResolver;
import com.gallagher.core.search.solrfacetsearch.provider.impl.GallagherProductPriceRangeValueResolver;
import com.gallagher.core.search.solrfacetsearch.provider.impl.GallagherProductPricesValueResolver;
import com.gallagher.core.services.GallagherSalesAreaService;


/**
 * The GallagherFieldNameTranslator class. Handles the sales Area in the indexed properties.
 *
 * @author ankituniyal
 */
public class GallagherFieldNameTranslator extends DefaultFieldNameTranslator
{
	public static final String FIELDNAME_SEPARATOR = "_";

	private GallagherSalesAreaService salesAreaService;
	private UserService userService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String translateFromProperty(final SearchQuery searchQuery, final IndexedProperty indexedProperty,
			final FieldType fieldType)
	{
		final IndexedType indexedType = searchQuery.getIndexedType();
		String fieldQualifier = null;
		final String valueProviderId = getValueProviderSelectionStrategy().resolveValueProvider(indexedType, indexedProperty);

		final Object valueProvider = getValueProviderSelectionStrategy().getValueProvider(valueProviderId);

		final QualifierProvider qualifierProvider = valueProvider instanceof QualifierProviderAware
				? ((QualifierProviderAware) valueProvider).getQualifierProvider() : null;

		QualifierProvider salesAreaQualifierProvider = valueProvider instanceof GallagherProductPricesValueResolver
				? ((GallagherProductPricesValueResolver) valueProvider).getSalesAreaQualifierProvider()
				: null;

		if (salesAreaQualifierProvider == null)
		{
			salesAreaQualifierProvider = valueProvider instanceof GallagherProductPriceRangeValueResolver
					? ((GallagherProductPriceRangeValueResolver) valueProvider).getSalesAreaQualifierProvider()
					: null;
		}

		if (salesAreaQualifierProvider == null)
		{
			salesAreaQualifierProvider = valueProvider instanceof GallagherProductDiscountValueResolver
					? ((GallagherProductDiscountValueResolver) valueProvider).getSalesAreaQualifierProvider()
					: null;
		}

		QualifierProvider userPriceGroupQualifierProvider = valueProvider instanceof GallagherProductPricesValueResolver
				? ((GallagherProductPricesValueResolver) valueProvider).getUserPriceGroupQualifierProvider()
				: null;

		if (userPriceGroupQualifierProvider == null)
		{
			userPriceGroupQualifierProvider = valueProvider instanceof GallagherProductPriceRangeValueResolver
					? ((GallagherProductPriceRangeValueResolver) valueProvider).getUserPriceGroupQualifierProvider()
					: null;
		}

		if (userPriceGroupQualifierProvider == null)
		{
			userPriceGroupQualifierProvider = valueProvider instanceof GallagherProductDiscountValueResolver
					? ((GallagherProductDiscountValueResolver) valueProvider).getUserDiscountGroupQualifierProvider()
					: null;
		}

		if (qualifierProvider != null && qualifierProvider.canApply(indexedProperty))
		{
			final Qualifier qualifier = qualifierProvider.getCurrentQualifier();
			fieldQualifier = qualifier != null ? qualifier.toFieldQualifier() : null;
			if ((salesAreaQualifierProvider != null && salesAreaQualifierProvider.canApply(
					indexedProperty))
					|| (userPriceGroupQualifierProvider != null && userPriceGroupQualifierProvider.canApply(indexedProperty)))
			{
				final UserModel user = userService.getCurrentUser();
				if (user instanceof B2BCustomerModel)
				{

					final B2BCustomerModel currentUser = (B2BCustomerModel) user;
					if (salesAreaQualifierProvider != null && salesAreaQualifierProvider.canApply(indexedProperty))
					{
						final Qualifier salesAreaQualifier = salesAreaQualifierProvider.getCurrentQualifier();
						fieldQualifier = salesAreaQualifier != null ? fieldQualifier + FIELDNAME_SEPARATOR + salesAreaQualifier
								.toFieldQualifier()
								: (currentUser != null ? fieldQualifier + FIELDNAME_SEPARATOR + currentUser.getEurope1PriceFactory_UPG()
										: fieldQualifier);

					}
					if (userPriceGroupQualifierProvider != null && userPriceGroupQualifierProvider.canApply(indexedProperty))
					{
						final Qualifier upgQualifier = userPriceGroupQualifierProvider.getCurrentQualifier();
						fieldQualifier = upgQualifier != null ? fieldQualifier + FIELDNAME_SEPARATOR + upgQualifier.toFieldQualifier()
								: (currentUser.getEurope1PriceFactory_UPG() == null ? fieldQualifier
										: fieldQualifier + FIELDNAME_SEPARATOR + currentUser.getEurope1PriceFactory_UPG());

					}
				}
			}
		}
		else if (indexedProperty.isLocalized())
		{
			fieldQualifier = searchQuery.getLanguage();
		}
		else if (indexedProperty.isCurrency())
		{
			fieldQualifier = searchQuery.getCurrency();
			final String currentSalesArea = salesAreaService.getCurrentSalesArea();
			final UserModel currentUser = userService.getCurrentUser();
			if (indexedProperty.isSalesArea() && StringUtils.isNotBlank(currentSalesArea))
			{
				fieldQualifier = fieldQualifier + FIELDNAME_SEPARATOR + currentSalesArea;
			}

			if (indexedProperty.isUserPriceGroup() && currentUser != null && currentUser.getEurope1PriceFactory_UPG() != null)
			{
				fieldQualifier = fieldQualifier + FIELDNAME_SEPARATOR + currentUser.getEurope1PriceFactory_UPG();
			}
		}
		return getFieldNameProvider().getFieldName(indexedProperty, fieldQualifier, fieldType);
	}

	/**
	 * @return the salesAreaService
	 */
	public GallagherSalesAreaService getSalesAreaService()
	{
		return salesAreaService;
	}

	/**
	 * @param salesAreaService
	 *           the salesAreaService to set
	 */
	public void setSalesAreaService(final GallagherSalesAreaService salesAreaService)
	{
		this.salesAreaService = salesAreaService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

}
