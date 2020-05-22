package com.gallagher.sap.sapcpiorderexchange.service.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SalesConditionCsvColumns;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderAddress;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderPriceComponent;
import de.hybris.platform.sap.sapcpiorderexchange.service.impl.SapCpiOmmOrderConversionService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gallagher.constants.GallagherPartnerCsvColumns;
import com.gallagher.constants.GallagherSalesConditionCsvColumns;


/**
 * Custom SapCpiOmmOrderConversionService
 *
 * @author Vikram Bishnoi
 */
public class GallagherSCPIOmmOrderConversionService extends SapCpiOmmOrderConversionService
{

	private static final Logger LOG = LoggerFactory.getLogger(GallagherSCPIOmmOrderConversionService.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<SapCpiOrderPriceComponent> mapOrderPrices(final OrderModel orderModel)
	{

		final List<SapCpiOrderPriceComponent> sapCpiOrderPriceComponents = new ArrayList<>();

		getSapSalesConditionsContributor().createRows(orderModel).forEach(row -> {

			final SapCpiOrderPriceComponent sapCpiOrderPriceComponent = new SapCpiOrderPriceComponent();

			sapCpiOrderPriceComponent.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
			sapCpiOrderPriceComponent.setEntryNumber(mapAttribute(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, row));
			sapCpiOrderPriceComponent.setConditionCode(mapAttribute(SalesConditionCsvColumns.CONDITION_CODE, row));
			sapCpiOrderPriceComponent.setTotalAmt(mapAttribute(GallagherSalesConditionCsvColumns.CONDITION_TOTAL_AMOUNT, row));
			sapCpiOrderPriceComponent.setConditionCounter(mapAttribute(SalesConditionCsvColumns.CONDITION_COUNTER, row));
			sapCpiOrderPriceComponent.setCurrencyIsoCode(mapAttribute(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, row));
			sapCpiOrderPriceComponent.setPriceQuantity(mapAttribute(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, row));
			sapCpiOrderPriceComponent.setUnit(mapAttribute(SalesConditionCsvColumns.CONDITION_UNIT_CODE, row));
			sapCpiOrderPriceComponent.setValue(mapAttribute(SalesConditionCsvColumns.CONDITION_VALUE, row));
			sapCpiOrderPriceComponent.setAbsolute(mapAttribute(SalesConditionCsvColumns.ABSOLUTE, row));

			sapCpiOrderPriceComponents.add(sapCpiOrderPriceComponent);

		});

		return sapCpiOrderPriceComponents;
	}

	@Override
	protected List<SapCpiOrderAddress> mapOrderAddresses(final OrderModel orderModel)
	{

		final List<SapCpiOrderAddress> sapCpiOrderAddresses = new ArrayList<>();

		getSapPartnerContributor().createRows(orderModel).forEach(row -> {

			final SapCpiOrderAddress sapCpiOrderAddress = new SapCpiOrderAddress();

			sapCpiOrderAddress.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
			sapCpiOrderAddress.setApartment(mapAttribute(PartnerCsvColumns.APPARTMENT, row));
			sapCpiOrderAddress.setBuilding(mapAttribute(PartnerCsvColumns.BUILDING, row));
			sapCpiOrderAddress.setCity(mapAttribute(PartnerCsvColumns.CITY, row));
			sapCpiOrderAddress.setCountryIsoCode(mapAttribute(PartnerCsvColumns.COUNTRY_ISO_CODE, row));
			sapCpiOrderAddress.setDistrict(mapAttribute(PartnerCsvColumns.DISTRICT, row));
			sapCpiOrderAddress.setDocumentAddressId(mapAttribute(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, row));
			sapCpiOrderAddress.setEmail(mapAttribute(PartnerCsvColumns.EMAIL, row));
			sapCpiOrderAddress.setFaxNumber(mapAttribute(PartnerCsvColumns.FAX, row));
			sapCpiOrderAddress.setFirstName(mapAttribute(PartnerCsvColumns.FIRST_NAME, row));
			sapCpiOrderAddress.setHouseNumber(mapAttribute(PartnerCsvColumns.HOUSE_NUMBER, row));
			sapCpiOrderAddress.setLanguageIsoCode(mapAttribute(PartnerCsvColumns.LANGUAGE_ISO_CODE, row));
			sapCpiOrderAddress.setLastName(mapAttribute(PartnerCsvColumns.LAST_NAME, row));
			sapCpiOrderAddress.setMiddleName(mapAttribute(PartnerCsvColumns.MIDDLE_NAME, row));
			sapCpiOrderAddress.setMiddleName2(mapAttribute(PartnerCsvColumns.MIDDLE_NAME2, row));
			sapCpiOrderAddress.setPobox(mapAttribute(PartnerCsvColumns.POBOX, row));
			sapCpiOrderAddress.setPostalCode(mapAttribute(PartnerCsvColumns.POSTAL_CODE, row));
			sapCpiOrderAddress.setRegionIsoCode(mapAttribute(PartnerCsvColumns.REGION_ISO_CODE, row));
			sapCpiOrderAddress.setStreet(mapAttribute(PartnerCsvColumns.STREET, row));
			sapCpiOrderAddress.setTelNumber(mapAttribute(PartnerCsvColumns.TEL_NUMBER, row));
			sapCpiOrderAddress.setTitleCode(mapAttribute(PartnerCsvColumns.TITLE, row));
			sapCpiOrderAddress.setTaxJurCode(mapAttribute(GallagherPartnerCsvColumns.TAX_JURISDICTION_CODE, row));

			if (sapCpiOrderAddress.getDocumentAddressId() != null && !sapCpiOrderAddress.getDocumentAddressId().isEmpty())
			{
				sapCpiOrderAddresses.add(sapCpiOrderAddress);
			}
		});

		return sapCpiOrderAddresses;
	}
}
