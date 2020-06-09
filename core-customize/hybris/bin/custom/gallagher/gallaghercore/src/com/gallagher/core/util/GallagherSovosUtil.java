/**
 *
 */
package com.gallagher.core.util;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.gallagher.core.model.GallagherSovosConfigurartionModel;
import com.gallagher.outboundservices.request.dto.GallagherSovosCalculateTaxLineItem;
import com.gallagher.outboundservices.request.dto.GallagherSovosCalculateTaxRequest;


/**
 * @author shishirkant
 */
public class GallagherSovosUtil
{

	/**
	 * Convert AbstractOrderModel into GallagherSovosCalculateTaxRequest.
	 *
	 * @param abstractOrder
	 * @param request
	 */
	public static void convert(final AbstractOrderModel abstractOrder, final GallagherSovosCalculateTaxRequest request)
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		final GallagherSovosConfigurartionModel sovosConfiguration = abstractOrder.getStore().getSovosConfiguration();
		final long timeStamp = System.currentTimeMillis();

		request.setRsltLvl(sovosConfiguration.getResultLevel());
		request.setTrnId(abstractOrder.getCode() + "_" + timeStamp);
		request.setCurrn(abstractOrder.getCurrency() == null ? "USD" : abstractOrder.getCurrency().getIsocode());
		request.setDocDt(dateFormat.format(new Date()));
		request.setTxCalcTp(sovosConfiguration.getTaxCalculationType());
		request.setTrnDocNum(abstractOrder.getCode());
		request.setTrnSrc(abstractOrder.getCode());
		request.setDlvrAmt(abstractOrder.getDeliveryCost());

		final List<GallagherSovosCalculateTaxLineItem> lineItems = new ArrayList<>();

		for (final AbstractOrderEntryModel orderEntry : abstractOrder.getEntries())
		{
			final GallagherSovosCalculateTaxLineItem lineItem = new GallagherSovosCalculateTaxLineItem();

			lineItem.setDebCredIndr(sovosConfiguration.getDebitCreditInd());
			lineItem.setGoodSrvCd(orderEntry.getProduct().getCode());
			lineItem.setGoodSrvDesc(orderEntry.getProduct().getName());
			lineItem.setGrossAmt(orderEntry.getTotalPrice()); // Need to check
			lineItem.setLnItmId(orderEntry.getEntryNumber());
			lineItem.setQnty(orderEntry.getQuantity());
			lineItem.setTrnTp(sovosConfiguration.getTransactionType());
			lineItem.setOrgCd(sovosConfiguration.getOrganizationCode());
			lineItem.setDropShipInd(sovosConfiguration.getDropShipInd());

			final List<WarehouseModel> warehouses = abstractOrder.getStore().getWarehouses();
			if (CollectionUtils.isNotEmpty(warehouses))
			{
				final Collection<PointOfServiceModel> pointOfServices = warehouses.get(0).getPointsOfService();

				if (CollectionUtils.isNotEmpty(pointOfServices))
				{
					final AddressModel address = pointOfServices.iterator().next().getAddress();

					if (null != address)
					{
						final Integer geoCd = Integer.valueOf(address.getGeoCode());
						lineItem.setsFGeoCd(geoCd);
						lineItem.setiOAGeoCd(geoCd);
						lineItem.setiORGeoCd(geoCd);
					}
				}

			}

			final AddressModel deliveryAddress = abstractOrder.getDeliveryAddress();
			final Integer geoCd = Integer.valueOf(deliveryAddress.getGeoCode());
			lineItem.setsTGeoCd(geoCd);
			lineItem.setiUGeoCd(geoCd);
			lineItem.setiSPGeoCd(geoCd);
			lineItems.add(lineItem);
		}

		request.setLines(lineItems);
	}

}
