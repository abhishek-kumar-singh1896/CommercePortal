/**
 *
 */
package com.gallagher.core.util;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

		request.setRsltLvl(sovosConfiguration.getResultLevel());
		request.setTrnId(abstractOrder.getCode());
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
			lineItem.setOrgCd(sovosConfiguration.getOrganizationCode()); // Need to check
			lineItem.setDropShipInd(sovosConfiguration.getDropShipInd());

			lineItem.setsFCountry("US"); // Need to check
			lineItem.setsFCity("Portland"); // Need to check
			lineItem.setsFStateProv("OR"); // Need to check
			lineItem.setsFPstlCd("97203"); // Need to check

			final AddressModel deliveryAddress = abstractOrder.getDeliveryAddress();

			lineItem.setsTCountry(deliveryAddress.getCountry().getIsocode());
			lineItem.setsTCity(deliveryAddress.getTown());
			lineItem.setsTStateProv(deliveryAddress.getDistrict()); // Need to check
			lineItem.setsTPstlCd(deliveryAddress.getPostalcode());
			lineItem.setsTStNameNum(deliveryAddress.getStreetnumber().concat(" ").concat(deliveryAddress.getStreetname()));

			lineItems.add(lineItem);
		}

		request.setLines(lineItems);
	}

}
