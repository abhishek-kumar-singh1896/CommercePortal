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
 * Qualifier provider for the user price group.
 *
 * @author ankituniyal
 */
public class GallagherUserPriceGroupQualifierProvider implements GallagherQualifierProvider
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
		final Set<UserPriceGroup> userPriceGroups = getUserPriceGroups();

		for (final UserPriceGroup upg : userPriceGroups)
		{
			final GallagherUserPriceGroupQualifierProvider.GallagherUPGQualifier qualifier = new GallagherUserPriceGroupQualifierProvider.GallagherUPGQualifier(
					upg);
			qualifiers.add(qualifier);
		}

		return Collections.unmodifiableList(qualifiers);
	}

	/**
	 * @return
	 */
	private Set<UserPriceGroup> getUserPriceGroups()
	{

		final List<B2BUnitModel> b2bUnits = getB2bUnitDao().getAllB2BUnits();
		final Set<UserPriceGroup> userPriceGroups = new HashSet<>();
		if (CollectionUtils.isNotEmpty(b2bUnits))
		{
			b2bUnits.stream().forEach(unit -> {
				if (null != unit.getUserPriceGroup())
				{
					userPriceGroups.add(unit.getUserPriceGroup());
				}

				if (null != unit.getUserDiscountGroup())
				{
					final UserDiscountGroup discountGroup = unit.getUserDiscountGroup();
					final Optional<UserPriceGroup> upgOptional = getEnumerationService().getEnumerationValues(UserPriceGroup.class)
							.stream()
							.filter(upg -> upg.getCode().equals(discountGroup.getCode())).findFirst();

					if (upgOptional.isPresent())
					{
						userPriceGroups.add(upgOptional.get());
					}
				}
			});
		}
		return userPriceGroups;

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
		if (!(qualifier instanceof GallagherUserPriceGroupQualifierProvider.GallagherUPGQualifier))
		{
			throw new IllegalArgumentException("provided qualifier is not of expected type");
		}
		else
		{
			sessionService.setAttribute(Europe1Constants.PARAMS.UPG,
					((GallagherUserPriceGroupQualifierProvider.GallagherUPGQualifier) qualifier).getUpg());
		}
	}

	@Override
	public void removeQualifier()
	{
		sessionService.setAttribute(Europe1Constants.PARAMS.UPG, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Qualifier getCurrentQualifier()
	{
		final UserPriceGroup upg = sessionService.getAttribute(Europe1Constants.PARAMS.UPG);
		return upg == null ? null : new GallagherUserPriceGroupQualifierProvider.GallagherUPGQualifier(upg);
	}

	/**
	 * The static inner class GallagherUPGQualifier. Holds the current UPG.
	 */
	protected static class GallagherUPGQualifier implements Qualifier
	{
		private final UserPriceGroup upg;

		public GallagherUPGQualifier(final UserPriceGroup upg)
		{
			Objects.requireNonNull(upg, "User price group is null");
			this.upg = upg;
		}

		public UserPriceGroup getUpg()
		{
			return this.upg;
		}

		@Override
		public <U> U getValueForType(final Class<U> type)
		{
			Objects.requireNonNull(type, "type is null");
			if (type.equals(UserPriceGroup.class))
			{
				return type.cast(this.upg);
			}
			else
			{
				throw new IllegalArgumentException("type not supported");
			}
		}

		@Override
		public String toFieldQualifier()
		{
			return this.upg.getCode();
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			else if (!(obj instanceof GallagherUserPriceGroupQualifierProvider.GallagherUPGQualifier))
			{
				return false;
			}
			else
			{
				final GallagherUserPriceGroupQualifierProvider.GallagherUPGQualifier otherQualifier = (GallagherUserPriceGroupQualifierProvider.GallagherUPGQualifier) obj;
				return this.upg.equals(otherQualifier.upg);
			}
		}

		@Override
		public int hashCode()
		{
			return this.upg.hashCode();
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
	 * @param b2bUnitDa
	 *            the b2bUnitDao to set
	 */
	public void setB2bUnitDaofinal (final GallagherB2BUnitDao b2bUnitDao)
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
