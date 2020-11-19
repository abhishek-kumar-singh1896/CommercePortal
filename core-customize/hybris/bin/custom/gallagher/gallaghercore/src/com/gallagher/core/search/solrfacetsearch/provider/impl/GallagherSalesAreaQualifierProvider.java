/**
 *
 */
package com.gallagher.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.gallagher.core.search.solrfacetsearch.provider.GallagherQualifierProvider;
import com.gallagher.core.services.GallagherSalesAreaService;


/**
 * @author ankituniyal
 *
 */
public class GallagherSalesAreaQualifierProvider implements GallagherQualifierProvider
{
	private final Set<Class<?>> supportedTypes = Collections.singleton(PointOfServiceModel.class);
	private GallagherSalesAreaService salesAreaService;
	private SessionService sessionService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Qualifier> getAvailableQualifiers(final FacetSearchConfig facetSearchConfig, final IndexedType indexedType)
	{
		final ArrayList qualifiers = new ArrayList();
		final Set<String> salesAreaSet = salesAreaService.getAllSalesAreas();

		for (final String salesArea : salesAreaSet)
		{
			final GallagherSalesAreaQualifierProvider.GallagherSalesAreaQualifier qualifier = new GallagherSalesAreaQualifierProvider.GallagherSalesAreaQualifier(
					salesArea);
			qualifiers.add(qualifier);
		}

		return Collections.unmodifiableList(qualifiers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canApply(final IndexedProperty indexedProperty)
	{
		Objects.requireNonNull(indexedProperty, "indexedProperty is null");
		return indexedProperty.isSalesArea();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyQualifier(final Qualifier qualifier)
	{
		Objects.requireNonNull(qualifier, "qualifier is null");
		if (!(qualifier instanceof GallagherSalesAreaQualifierProvider.GallagherSalesAreaQualifier))
		{
			throw new IllegalArgumentException("provided qualifier is not of expected type");
		}
		else
		{
			salesAreaService.setCurrentSalesAreaInSession(
					((GallagherSalesAreaQualifierProvider.GallagherSalesAreaQualifier) qualifier).getSalesArea());
		}
	}

	@Override
	public void removeQualifier()
	{
		getSessionService().removeAttribute("currentSessionSalesArea");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Qualifier getCurrentQualifier()
	{
		final String salesArea = salesAreaService.getCurrentSalesArea();
		return StringUtils.isBlank(salesArea) ? null
				: new GallagherSalesAreaQualifierProvider.GallagherSalesAreaQualifier(salesArea);
	}

	/**
	 * The static inner class FSStoreQualifer. Holds the current store.
	 */
	protected static class GallagherSalesAreaQualifier implements Qualifier
	{
		private final String salesArea;

		public GallagherSalesAreaQualifier(final String salesArea)
		{
			Objects.requireNonNull(salesArea, "salesArea is null");
			this.salesArea = salesArea;
		}

		public String getSalesArea()
		{
			return this.salesArea;
		}

		@Override
		public <U> U getValueForType(final Class<U> type)
		{
			Objects.requireNonNull(type, "type is null");
			if (type.equals(PointOfServiceModel.class))
			{
				return type.cast(this.salesArea);
			}
			else
			{
				throw new IllegalArgumentException("type not supported");
			}
		}

		@Override
		public String toFieldQualifier()
		{
			return this.salesArea;
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			else if (!(obj instanceof GallagherSalesAreaQualifierProvider.GallagherSalesAreaQualifier))
			{
				return false;
			}
			else
			{
				final GallagherSalesAreaQualifierProvider.GallagherSalesAreaQualifier otherQualifier = (GallagherSalesAreaQualifierProvider.GallagherSalesAreaQualifier) obj;
				return this.salesArea.equals(otherQualifier.salesArea);
			}
		}

		@Override
		public int hashCode()
		{
			return this.salesArea.hashCode();
		}
	}

	@Override
	public Set<Class<?>> getSupportedTypes()
	{
		return this.supportedTypes;
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
	@Required
	public void setSalesAreaService(final GallagherSalesAreaService salesAreaService)
	{
		this.salesAreaService = salesAreaService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
