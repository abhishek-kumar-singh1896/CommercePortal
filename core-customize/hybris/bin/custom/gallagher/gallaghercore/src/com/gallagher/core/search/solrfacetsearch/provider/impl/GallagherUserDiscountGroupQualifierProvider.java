package com.gallagher.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.gallagher.core.dao.GallagherB2BUnitDao;
import com.gallagher.core.search.solrfacetsearch.provider.GallagherQualifierProvider;


/**
 * Qualifier provider for the user discount group.
 *
 * @author ankituniyal
 */
public class GallagherUserDiscountGroupQualifierProvider implements GallagherQualifierProvider
{
	private SessionService sessionService;
	private final Set<Class<?>> supportedTypes = Collections.singleton(PointOfServiceModel.class);
	@Autowired
	private GallagherB2BUnitDao b2bUnitDao;
	private EnumerationService enumerationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Qualifier> getAvailableQualifiers(final FacetSearchConfig facetSearchConfig, final IndexedType indexedType)
	{
		Objects.requireNonNull(facetSearchConfig, "facetSearchConfig is null");
		Objects.requireNonNull(indexedType, "indexedType is null");
		final ArrayList qualifiers = new ArrayList();
		final Set<UserDiscountGroup> userDiscountGroups = getUserDiscountGroups();

		for (final UserDiscountGroup udg : userDiscountGroups)
		{
			final GallagherUserDiscountGroupQualifierProvider.GallagherUDGQualifier qualifier = new GallagherUserDiscountGroupQualifierProvider.GallagherUDGQualifier(
					udg);
			qualifiers.add(qualifier);
		}

		return Collections.unmodifiableList(qualifiers);
	}

	/**
	 * @return
	 */
	private Set<UserDiscountGroup> getUserDiscountGroups()
	{

		final List<B2BUnitModel> b2bUnits = getB2bUnitDao().getAllB2BUnits();
		final Set<UserDiscountGroup> userDiscountGroups = new HashSet<>();
		if (CollectionUtils.isNotEmpty(b2bUnits))
		{
			b2bUnits.stream().forEach(unit -> {
				if (null != unit.getUserDiscountGroup())
				{
					final UserDiscountGroup discountGroup = unit.getUserDiscountGroup();
					userDiscountGroups.add(discountGroup);
				}

				if (null != unit.getUserPriceGroup())
				{
					final UserPriceGroup priceGroup = unit.getUserPriceGroup();
					final Optional<UserDiscountGroup> udgOptional = getEnumerationService()
							.getEnumerationValues(
							UserDiscountGroup.class)
							.stream().filter(udg -> udg.getCode().equals(priceGroup.getCode())).findFirst();
					if (udgOptional.isPresent())
					{
						userDiscountGroups.add(udgOptional.get());
					}
				}
			});
		}
		return userDiscountGroups;

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canApply(final IndexedProperty indexedProperty)
	{
		Objects.requireNonNull(indexedProperty, "indexedProperty is null");
		return indexedProperty.isUserPriceGroup();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyQualifier(final Qualifier qualifier)
	{
		Objects.requireNonNull(qualifier, "qualifier is null");
		if (!(qualifier instanceof GallagherUserDiscountGroupQualifierProvider.GallagherUDGQualifier))
		{
			throw new IllegalArgumentException("provided qualifier is not of expected type");
		}
		else
		{
			sessionService.setAttribute(Europe1Constants.PARAMS.UDG,
					((GallagherUserDiscountGroupQualifierProvider.GallagherUDGQualifier) qualifier).getUdg());
		}
	}

	@Override
	public void removeQualifier()
	{
		sessionService.setAttribute(Europe1Constants.PARAMS.UDG, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Qualifier getCurrentQualifier()
	{
		final UserDiscountGroup udg = sessionService.getAttribute(Europe1Constants.PARAMS.UDG);
		return udg == null ? null : new GallagherUserDiscountGroupQualifierProvider.GallagherUDGQualifier(udg);
	}

	/**
	 * The static inner class GallagherUPGQualifier. Holds the current UPG.
	 */
	protected static class GallagherUDGQualifier implements Qualifier
	{
		private final UserDiscountGroup udg;

		public GallagherUDGQualifier(final UserDiscountGroup udg)
		{
			Objects.requireNonNull(udg, "User price group is null");
			this.udg = udg;
		}

		public UserDiscountGroup getUdg()
		{
			return this.udg;
		}

		@Override
		public <U> U getValueForType(final Class<U> type)
		{
			Objects.requireNonNull(type, "type is null");
			if (type.equals(UserDiscountGroup.class))
			{
				return type.cast(this.udg);
			}
			else
			{
				throw new IllegalArgumentException("type not supported");
			}
		}

		@Override
		public String toFieldQualifier()
		{
			return this.udg.getCode();
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			else if (!(obj instanceof GallagherUserDiscountGroupQualifierProvider.GallagherUDGQualifier))
			{
				return false;
			}
			else
			{
				final GallagherUserDiscountGroupQualifierProvider.GallagherUDGQualifier otherQualifier = (GallagherUserDiscountGroupQualifierProvider.GallagherUDGQualifier) obj;
				return this.udg.equals(otherQualifier.udg);
			}
		}

		@Override
		public int hashCode()
		{
			return this.udg.hashCode();
		}
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the b2bUnitDao
	 */
	public GallagherB2BUnitDao getB2bUnitDao()
	{
		return b2bUnitDao;
	}

	/**
	 * @param b2bUnitDao
	 *           the b2bUnitDao to set
	 */
	public void setB2bUnitDao(final GallagherB2BUnitDao b2bUnitDao)
	{
		this.b2bUnitDao = b2bUnitDao;
	}

	@Override
	public Set<Class<?>> getSupportedTypes()
	{
		return this.supportedTypes;

	}
	/**
	 * @return the enumerationService
	 */
	public EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	/**
	 * @param enumerationService
	 *           the enumerationService to set
	 */
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
