package com.gallagher.sap.sapcpiorderexchange.service.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PaymentCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SalesConditionCsvColumns;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiCreditCardPayment;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderAddress;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderPriceComponent;
import de.hybris.platform.sap.sapcpiorderexchange.service.impl.SapCpiOmmOrderConversionService;

import java.math.BigInteger;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gallagher.constants.GallagherPartnerCsvColumns;
import com.gallagher.constants.GallagherPaymentCsvColumns;
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

	@Override
	protected List<SapCpiCreditCardPayment> mapCreditCards(final OrderModel orderModel)
	{
		final List<SapCpiCreditCardPayment> sapCpiCreditCardPayments = new ArrayList<>();
		try
		{
			getSapPaymentContributor().createRows(orderModel).forEach(row -> {

				final SapCpiCreditCardPayment sapCpiCreditCardPayment = new SapCpiCreditCardPayment();
				sapCpiCreditCardPayment.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
				sapCpiCreditCardPayment.setCcOwner(mapAttribute(PaymentCsvColumns.CC_OWNER, row));
				sapCpiCreditCardPayment.setPaymentProvider(mapAttribute(PaymentCsvColumns.PAYMENT_PROVIDER, row));
				sapCpiCreditCardPayment.setSubscriptionId(mapAttribute(PaymentCsvColumns.SUBSCRIPTION_ID, row));

				String requestId = mapAttribute(PaymentCsvColumns.REQUEST_ID, row);
				requestId = Pattern.compile("^\\d+$").matcher(requestId).matches()
						? BigInteger.valueOf(Long.parseLong(requestId)).toString(32).toUpperCase()
						: requestId;
				sapCpiCreditCardPayment.setRequestId(requestId);

				final String month = mapAttribute(PaymentCsvColumns.VALID_TO_MONTH, row);
				sapCpiCreditCardPayment.setValidToMonth(month);

				final String year = mapAttribute(PaymentCsvColumns.VALID_TO_YEAR, row);

				if (year != null && month != null)
				{
					final YearMonth yearMonth = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
					sapCpiCreditCardPayment.setValidToYear(yearMonth.atEndOfMonth().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
				}
				else
				{
					sapCpiCreditCardPayment.setValidToYear(null);
				}
				sapCpiCreditCardPayment.setAmount(mapAttribute(GallagherPaymentCsvColumns.AMOUNT, row));
				sapCpiCreditCardPayment.setAuthorizationTime(mapDateAttribute(GallagherPaymentCsvColumns.AUTHORIZATION_TIME, row));
				sapCpiCreditCardPayment.setAuthorizationNumber(mapAttribute(GallagherPaymentCsvColumns.AUTHORIZATION_NUMBER, row));
				sapCpiCreditCardPayment.setResultText(mapAttribute(GallagherPaymentCsvColumns.RESULT_TEXT, row));
				sapCpiCreditCardPayment.setMerchantID(mapAttribute(GallagherPaymentCsvColumns.MERCHANT_ID, row));
				sapCpiCreditCardPayment.setAvsCode(mapAttribute(GallagherPaymentCsvColumns.AVS_CODE, row));

				sapCpiCreditCardPayments.add(sapCpiCreditCardPayment);
			});
		}
		catch (final RuntimeException ex)
		{
			final String msg = String.format("Error occurs while setting the payment information for the order [%s]!",
					orderModel.getCode());
			LOG.error(msg.concat(ex.getMessage()));
			throw ex;
		}
		return sapCpiCreditCardPayments;
	}
}
