/**
 *
 */
package com.gallagher.facades.search.solrfacetsearch.populators;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.converters.populator.DefaultIndexedPropertyPopulator;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;


/**
 * @author ankituniyal
 *
 */
public class GallagherIndexedPropertyPopulator extends DefaultIndexedPropertyPopulator
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void populate(final SolrIndexedPropertyModel source, final IndexedProperty target) throws ConversionException
	{
		super.populate(source, target);
		target.setSalesArea(source.isSalesArea());
		target.setCustomerGroup(source.isCustomerGroup());
		target.setUserPriceGroup(source.isUserPriceGroup());
	}
}
