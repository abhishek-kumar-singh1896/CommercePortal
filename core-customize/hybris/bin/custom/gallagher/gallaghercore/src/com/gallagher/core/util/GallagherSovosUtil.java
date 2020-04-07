/**
 *
 */
package com.gallagher.core.util;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.ArrayList;
import java.util.List;

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
		request.setTrnId("");
		request.setTrnSrc("01");
		request.setRsltLvl("3");
		request.setCurrn(abstractOrder.getCurrency() == null ? "USD" : abstractOrder.getCurrency().getIsocode());
		request.setDocDt("2020-03-21");
		request.setTxCalcTp(1);
		request.setTrnDocNum("4910374");

		final List<GallagherSovosCalculateTaxLineItem> lineItems = new ArrayList<>();

		for (final AbstractOrderEntryModel orderEntry : abstractOrder.getEntries())
		{
			final GallagherSovosCalculateTaxLineItem lineItem = new GallagherSovosCalculateTaxLineItem();

			lineItem.setLnItmNum(orderEntry.getEntryNumber());
			lineItem.setLnItmId("1");
			lineItem.setGrossAmt(orderEntry.getTotalPrice());
			lineItem.setQnty(orderEntry.getQuantity().intValue());
			lineItem.setTrnTp(1);
			lineItem.setDropShipInd(0);
			lineItem.setOrigTrnDt("2019-08-14");
			lineItem.setGoodSrvCd("2038356");
			lineItem.setOrgCd("QASGUS");
			lineItem.setDebCredIndr(1);
			lineItem.setQntyUMCd("LE");
			lineItem.setSTStNameNum("3817 Millenia Blvd");
			lineItem.setSTCity("ORLANDO");
			lineItem.setSTStateProv("FL");
			lineItem.setSTPstlCd("32839");
			lineItem.setSTCountry("US");
			lineItem.setSFCity("Portland");
			lineItem.setSFStateProv("OR");
			lineItem.setSFPstlCd("97203");
			lineItem.setSFCountry("US");
			lineItem.setLOACity("Portland");
			lineItem.setLOAStateProv("OR");
			lineItem.setLOAPstlCd("97229");
			lineItem.setLOACountry("US");
			lineItem.setLORCity("Portland");
			lineItem.setLORStateProv("OR");
			lineItem.setLORPstlCd("97229");
			lineItem.setLORCountry("US");

			lineItems.add(lineItem);
		}

		request.setLines(lineItems);
	}

}
