/**
 *
 */
package com.enterprisewide.b2badvance.facades.populators;

import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 *
 */
public class B2badvanceOrderEntryPopulator extends OrderEntryPopulator {
	@Autowired
	private UnitService unitService;

	@Autowired
	private PriceDataFactory priceDataFactory;

	@Autowired
	private CommonI18NService commonI18NService;

	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		target.setDeliveredQuantity(source.getDeliveredQuantity());
		target.setStatus(source.getStatus());
		super.populate(source, target);
	}

	@Override
	protected void addTotals(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry) {
		if (orderEntry != null && orderEntry.getTotalPrice() != null && orderEntry.getQuantity() != null
				&& orderEntry.getBasePrice() != null && orderEntry.getQuantity() != 0) {
			entry.setSellingPrice(buildPrice(orderEntry.getTotalPrice() / orderEntry.getQuantity()));
		}
		super.addTotals(orderEntry, entry);
	}

	protected PriceData buildPrice(final double amount) {
		return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(amount), commonI18NService
				.getCurrency(JaloSession.getCurrentSession().getSessionContext().getCurrency().getIsocode()));
	}

}
