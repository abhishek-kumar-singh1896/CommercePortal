/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sap.orderexchange.outbound.impl;

import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerRoles;
import de.hybris.platform.sap.orderexchange.outbound.B2CCustomerHelper;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultPartnerContributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.gallagher.constants.GallagherPartnerCsvColumns;


/**
 * Custom contributor to populate the Partner info like addresses etc.
 *
 * @author Vikram BishnoiF
 */
public class GallagherPartnerContributor extends DefaultPartnerContributor
{
	@Resource
	private CustomerEmailResolutionService customerEmailResolutionService;

	private B2CCustomerHelper b2CCustomerHelper;

	@Override
	protected Map<String, Object> mapAddressData(final OrderModel order, final AddressModel address)
	{
		final Map<String, Object> row = super.mapAddressData(order, address);
		row.put(GallagherPartnerCsvColumns.TAX_JURISDICTION_CODE, address.getGeoCode());
		row.put(GallagherPartnerCsvColumns.GALLAGHER_STREET_NUMBER, address.getGallagherStreetNumber());
		if (address.getEmail() == null)
		{
			final String email = customerEmailResolutionService.getEmailForCustomer(((CustomerModel) order.getUser()));
			row.put(PartnerCsvColumns.EMAIL, email);
		}
		return row;
	}

	@Override
	public List<Map<String, Object>> createRows(final OrderModel order)
	{
		final List<Map<String, Object>> result = new ArrayList<>(4);
		final AddressModel paymentAddress = order.getPaymentAddress();
		AddressModel deliveryAddress = order.getDeliveryAddress();
		if (deliveryAddress == null)
		{
			deliveryAddress = paymentAddress;
		}

		String sapcommon_Customer = null;
		if (null != order.getUnit())
		{
			sapcommon_Customer = order.getUnit().getUid();
		}
		else
		{

			final String b2cCustomer = b2CCustomerHelper.determineB2CCustomer(order);
			sapcommon_Customer = b2cCustomer != null ? b2cCustomer
					: order.getStore().getSAPConfiguration().getSapcommon_referenceCustomer();
		}

		final String deliveryAddressNumber = getShipToAddressNumber();
		Map<String, Object> row = mapAddressData(order, deliveryAddress);
		row.put(PartnerCsvColumns.PARTNER_ROLE_CODE, PartnerRoles.SHIP_TO.getCode());
		row.put(PartnerCsvColumns.PARTNER_CODE, sapcommon_Customer);
		row.put(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, deliveryAddressNumber);
		getBatchIdAttributes().forEach(row::putIfAbsent);
		row.put("dh_batchId", order.getCode());
		result.add(row);

		String soldToAddressNumber;
		if (paymentAddress != null)
		{
			final String paymentAddressNumber = getSoldToAddressNumber();
			row = mapAddressData(order, paymentAddress);
			row.put(PartnerCsvColumns.PARTNER_ROLE_CODE, PartnerRoles.BILL_TO.getCode());
			row.put(PartnerCsvColumns.PARTNER_CODE, sapcommon_Customer);
			row.put(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, paymentAddressNumber);
			getBatchIdAttributes().forEach(row::putIfAbsent);
			row.put("dh_batchId", order.getCode());
			result.add(row);
			soldToAddressNumber = paymentAddressNumber;
			row = mapAddressData(order, paymentAddress);
		}
		else
		{
			soldToAddressNumber = deliveryAddressNumber;
			row = mapAddressData(order, deliveryAddress);
		}

		row.put(PartnerCsvColumns.PARTNER_ROLE_CODE, PartnerRoles.SOLD_TO.getCode());
		row.put(PartnerCsvColumns.PARTNER_CODE, sapcommon_Customer);
		row.put(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, soldToAddressNumber);
		getBatchIdAttributes().forEach(row::putIfAbsent);
		row.put("dh_batchId", order.getCode());
		result.add(row);

		/*
		 * Add partner function for sale rep ID and partner function (VE) for Assisted Service Mode (ASM)
		 */
		final UserModel salesRep = order.getPlacedBy();
		if (salesRep != null)
		{
			row = new HashMap<String, Object>();
			row.put(OrderCsvColumns.ORDER_ID, order.getCode());
			row.put(PartnerCsvColumns.LANGUAGE_ISO_CODE, order.getLanguage().getIsocode());
			row.put(PartnerCsvColumns.PARTNER_ROLE_CODE, PartnerRoles.PLACED_BY.getCode());
			if (null != order.getUnit())
			{
				row.put(PartnerCsvColumns.PARTNER_CODE, sapcommon_Customer);
			}
			else
			{
				row.put(PartnerCsvColumns.PARTNER_CODE, salesRep.getUid());
			}
			row.put(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, "");
			getBatchIdAttributes().forEach(row::putIfAbsent);
			row.put("dh_batchId", order.getCode());
			result.add(row);
		}

		return result;
	}

	@Override
	@SuppressWarnings("javadoc")
	public B2CCustomerHelper getB2CCustomerHelper()
	{
		return b2CCustomerHelper;
	}

	@Override
	@SuppressWarnings("javadoc")
	public void setB2CCustomerHelper(final B2CCustomerHelper b2cCustomerHelper)
	{
		b2CCustomerHelper = b2cCustomerHelper;
	}

}
