package com.gallagher.sap.sapcpiorderexchange.service.impl;

import de.hybris.platform.sap.sapcpiadapter.data.SapCpiCreditCardPayment;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderAddress;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderPriceComponent;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundAddressModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundCardPaymentModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundPriceComponentModel;
import de.hybris.platform.sap.sapcpiorderexchange.service.impl.SapCpiOmmOrderMapperService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Custom SAP CPI OMM Order Mapper Service
 *
 * @author Vikram Bishnoi
 */
public class GallagherSCPIOmmOrderMapperService extends SapCpiOmmOrderMapperService
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<SAPCpiOutboundPriceComponentModel> mapOrderPrices(
			final List<SapCpiOrderPriceComponent> sapCpiOrderPriceComponents)
	{

		final List<SAPCpiOutboundPriceComponentModel> sapCpiOutboundPriceComponents = new ArrayList<>();

		sapCpiOrderPriceComponents.forEach(price -> {

			final SAPCpiOutboundPriceComponentModel sapCpiOutboundPriceComponent = new SAPCpiOutboundPriceComponentModel();
			sapCpiOutboundPriceComponent.setOrderId(price.getOrderId());
			sapCpiOutboundPriceComponent.setEntryNumber(price.getEntryNumber());
			sapCpiOutboundPriceComponent.setValue(price.getValue());
			sapCpiOutboundPriceComponent.setUnit(price.getUnit());
			sapCpiOutboundPriceComponent.setAbsolute(price.getAbsolute());
			sapCpiOutboundPriceComponent.setConditionCode(price.getConditionCode());
			sapCpiOutboundPriceComponent.setTotalAmt(price.getTotalAmt());
			sapCpiOutboundPriceComponent.setConditionCounter(price.getConditionCounter());
			sapCpiOutboundPriceComponent.setCurrencyIsoCode(price.getCurrencyIsoCode());
			sapCpiOutboundPriceComponent.setPriceQuantity(price.getPriceQuantity());

			sapCpiOutboundPriceComponents.add(sapCpiOutboundPriceComponent);

		});

		return new HashSet<>(sapCpiOutboundPriceComponents);

	}

	@Override
	protected Set<SAPCpiOutboundAddressModel> mapOrderAddresses(final List<SapCpiOrderAddress> sapCpiOrderAddresses)
	{

		final List<SAPCpiOutboundAddressModel> sapCpiOutboundAddresses = new ArrayList<>();

		sapCpiOrderAddresses.forEach(address -> {

			final SAPCpiOutboundAddressModel sapCpiOutboundAddress = new SAPCpiOutboundAddressModel();
			sapCpiOutboundAddress.setOrderId(address.getOrderId());
			sapCpiOutboundAddress.setDocumentAddressId(address.getDocumentAddressId());
			sapCpiOutboundAddress.setFirstName(address.getFirstName());
			sapCpiOutboundAddress.setLastName(address.getLastName());
			sapCpiOutboundAddress.setMiddleName(address.getMiddleName());
			sapCpiOutboundAddress.setMiddleName2(address.getMiddleName2());
			sapCpiOutboundAddress.setStreet(address.getStreet());
			sapCpiOutboundAddress.setCity(address.getCity());
			sapCpiOutboundAddress.setDistrict(address.getDistrict());
			sapCpiOutboundAddress.setBuilding(address.getBuilding());
			sapCpiOutboundAddress.setApartment(address.getApartment());
			sapCpiOutboundAddress.setPobox(address.getPobox());
			sapCpiOutboundAddress.setFaxNumber(address.getFaxNumber());
			sapCpiOutboundAddress.setTitleCode(address.getTitleCode());
			sapCpiOutboundAddress.setTelNumber(address.getTelNumber());
			sapCpiOutboundAddress.setHouseNumber(address.getHouseNumber());
			sapCpiOutboundAddress.setPostalCode(address.getPostalCode());
			sapCpiOutboundAddress.setRegionIsoCode(address.getRegionIsoCode());
			sapCpiOutboundAddress.setCountryIsoCode(address.getCountryIsoCode());
			sapCpiOutboundAddress.setEmail(address.getEmail());
			sapCpiOutboundAddress.setLanguageIsoCode(address.getLanguageIsoCode());
			sapCpiOutboundAddress.setTaxJurCode(address.getTaxJurCode());

			sapCpiOutboundAddresses.add(sapCpiOutboundAddress);

		});

		return new HashSet<>(sapCpiOutboundAddresses);

	}

	@Override
	protected Set<SAPCpiOutboundCardPaymentModel> mapCreditCards(final List<SapCpiCreditCardPayment> sapCpiCreditCardPayments)
	{

		final List<SAPCpiOutboundCardPaymentModel> sapCpiOutboundCardPayments = new ArrayList<>();

		sapCpiCreditCardPayments.forEach(payment -> {

			final SAPCpiOutboundCardPaymentModel sapCpiOutboundCardPayment = new SAPCpiOutboundCardPaymentModel();
			sapCpiOutboundCardPayment.setOrderId(payment.getOrderId());
			sapCpiOutboundCardPayment.setRequestId(payment.getRequestId());
			sapCpiOutboundCardPayment.setCcOwner(payment.getCcOwner());
			sapCpiOutboundCardPayment.setValidToMonth(payment.getValidToMonth());
			sapCpiOutboundCardPayment.setValidToYear(payment.getValidToYear());
			sapCpiOutboundCardPayment.setSubscriptionId(payment.getSubscriptionId());
			sapCpiOutboundCardPayment.setPaymentProvider(payment.getPaymentProvider());

			sapCpiOutboundCardPayment.setAmount(payment.getAmount());
			sapCpiOutboundCardPayment.setAuthorizationTime(payment.getAuthorizationTime());
			sapCpiOutboundCardPayment.setAuthorizationNumber(payment.getAuthorizationNumber());
			sapCpiOutboundCardPayment.setResultText(payment.getResultText());
			sapCpiOutboundCardPayment.setMerchantID(payment.getMerchantID());
			sapCpiOutboundCardPayment.setAvsCode(payment.getAvsCode());

			sapCpiOutboundCardPayments.add(sapCpiOutboundCardPayment);

		});

		return new HashSet<>(sapCpiOutboundCardPayments);

	}

}
