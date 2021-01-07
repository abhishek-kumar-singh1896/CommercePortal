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
import com.gallagher.core.services.GallagherCustomerGroupService;


/**
 * @author ankituniyal
 *
 */
public class GallagherCustomerGroupQualifierProvider implements GallagherQualifierProvider
{
	private final Set<Class<?>> supportedTypes = Collections.singleton(PointOfServiceModel.class);
	private GallagherCustomerGroupService customerGroupService;
	private SessionService sessionService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Qualifier> getAvailableQualifiers(final FacetSearchConfig facetSearchConfig, final IndexedType indexedType)
	{
		final ArrayList qualifiers = new ArrayList();
		final Set<String> customerGroupSet = customerGroupService.getAllCustomerGroups();

		for (final String customerGroup : customerGroupSet)
		{
			final GallagherCustomerGroupQualifierProvider.GallagherCustomerGroupQualifier qualifier = new GallagherCustomerGroupQualifierProvider.GallagherCustomerGroupQualifier(
					customerGroup);
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
		return indexedProperty.isCustomerGroup();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyQualifier(final Qualifier qualifier)
	{
		Objects.requireNonNull(qualifier, "qualifier is null");
		if (!(qualifier instanceof GallagherCustomerGroupQualifierProvider.GallagherCustomerGroupQualifier))
		{
			throw new IllegalArgumentException("provided qualifier is not of expected type");
		}
		else
		{
			customerGroupService.setCurrentCustomerGroupInSession(
					((GallagherCustomerGroupQualifierProvider.GallagherCustomerGroupQualifier) qualifier).getCustomerGroup());
		}
	}

	@Override
	public void removeQualifier()
	{
		getSessionService().removeAttribute("currentSessionCustomerGroup");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Qualifier getCurrentQualifier()
	{
		final String customerGroup = customerGroupService.getCurrentCustomerGroup();
		return StringUtils.isBlank(customerGroup) ? null
				: new GallagherCustomerGroupQualifierProvider.GallagherCustomerGroupQualifier(customerGroup);
	}

	/**
	 * The static inner class FSStoreQualifer. Holds the current store.
	 */
	protected static class GallagherCustomerGroupQualifier implements Qualifier
	{
		private final String customerGroup;

		public GallagherCustomerGroupQualifier(final String customerGroup)
		{
			Objects.requireNonNull(customerGroup, "customerGroup is null");
			this.customerGroup = customerGroup;
		}

		public String getCustomerGroup()
		{
			return this.customerGroup;
		}

		@Override
		public <U> U getValueForType(final Class<U> type)
		{
			Objects.requireNonNull(type, "type is null");
			if (type.equals(PointOfServiceModel.class))
			{
				return type.cast(this.customerGroup);
			}
			else
			{
				throw new IllegalArgumentException("type not supported");
			}
		}

		@Override
		public String toFieldQualifier()
		{
			return this.customerGroup;
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			else if (!(obj instanceof GallagherCustomerGroupQualifierProvider.GallagherCustomerGroupQualifier))
			{
				return false;
			}
			else
			{
				final GallagherCustomerGroupQualifierProvider.GallagherCustomerGroupQualifier otherQualifier = (GallagherCustomerGroupQualifierProvider.GallagherCustomerGroupQualifier) obj;
				return this.customerGroup.equals(otherQualifier.customerGroup);
			}
		}

		@Override
		public int hashCode()
		{
			return this.customerGroup.hashCode();
		}
	}

	@Override
	public Set<Class<?>> getSupportedTypes()
	{
		return this.supportedTypes;
	}

	/**
	 * @return the customerGroupService
	 */
	public GallagherCustomerGroupService getCustomerGroupService()
	{
		return customerGroupService;
	}

	/**
	 * @param customerGroupService
	 *           the customerGroupService to set
	 */
	public void setCustomerGroupService(final GallagherCustomerGroupService customerGroupService)
	{
		this.customerGroupService = customerGroupService;
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
