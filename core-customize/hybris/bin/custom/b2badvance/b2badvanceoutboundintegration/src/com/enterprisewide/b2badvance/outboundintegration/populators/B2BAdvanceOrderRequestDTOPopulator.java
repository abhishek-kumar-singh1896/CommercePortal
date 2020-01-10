/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.enterprisewide.b2badvance.outboundintegration.populators;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceAddressDTO;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceDeliveryDetailsDTO;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceOrderContactDTO;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceOrderEntryDTO;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceOrderRequestDTO;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceOrganizationDTO;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvancePaymentDetailsDTO;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvancePaymentTransactionDTO;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvancePaymentTransactionEntryDTO;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceTaxDTO;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.InvoicePaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.util.TaxValue;

/**
 * Populator to convert OrderModel to B2BAdvanceOrderRequestDTO
 *
 * @author Vikram Bishnoi
 */
public class B2BAdvanceOrderRequestDTOPopulator implements Populator<OrderModel, B2BAdvanceOrderRequestDTO> {
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceOrderRequestDTOPopulator.class);

	private static final String UTC = "UTC";
	private static final String PICKUP = "pickup";

	private static final String POSTAL_ADDRESS = "P";

	private static final String RESIDENTIAL_ADDRESS = "R";

	private static final String HOME_DELIVERY = "HOME-DELIVERY";
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void populate(final OrderModel source, final B2BAdvanceOrderRequestDTO target) throws ConversionException {
		Assert.notNull(source, "Parameter OrderModel cannot be null.");
		Assert.notNull(target, "Parameter FSOrderRequestDTO cannot be null.");

		target.setHybrisOrderNumber(source.getCode());

		target.setDate(formatDate(source.getDate(), DATE_TIME_FORMAT, UTC));
		if (null != source.getCurrency()) {
			target.setCurrency(source.getCurrency().getIsocode());
		}
		final CustomerModel customer = (CustomerModel) source.getUser();
		if (null != customer) {
			populateContactInformation(target, customer, source);
		}

		populateEntries(source, target);
		populateTaxes(source, target);
		target.setDeliveryCost(source.getDeliveryCost());
		target.setTotalTax(source.getTotalTax());
		target.setTotal(source.getTotalPrice());
		populatePaymentDetails(source, target);
		populateDeliveryDetails(source, target);
	}

	/**
	 * Populates Customer details in order information
	 *
	 * @param target   order data
	 * @param customer to be populated in target
	 * @param source
	 */
	private void populateContactInformation(final B2BAdvanceOrderRequestDTO target, final CustomerModel customer,
			OrderModel source) {
		final B2BAdvanceOrderContactDTO contactDTO = new B2BAdvanceOrderContactDTO();
		contactDTO.setContactCustomerID(customer.getCustomerID());
		contactDTO.setContactEmailAddress(customer.getContactEmail());
		contactDTO.setContactName(customer.getName());
		if (source.getUnit() != null) {
			B2BAdvanceOrganizationDTO organization = new B2BAdvanceOrganizationDTO();
			organization.setBusinessUnitID(source.getUnit().getId());
			organization.setBusinessUnitID(source.getUnit().getDisplayName());
			if (CollectionUtils.isNotEmpty(source.getUnit().getGroups())) {

				B2BUnitModel parentUnit = (B2BUnitModel) (source.getUnit().getGroups().iterator().next());
				organization.setParentBusinessUnitID(parentUnit.getId());
				organization.setParentBusinessUnitName(parentUnit.getDisplayName());
			}
		}
		target.setContact(contactDTO);
	}

	/**
	 * Parses the date.
	 *
	 * @param date   the date to be formatted
	 * @param format to be format in
	 * @return the date
	 */
	private String formatDate(final Date date, final String format, final String timeZone) {
		String formattedTime = null;
		ZoneId zone = null;
		if (StringUtils.isEmpty(timeZone)) {
			zone = ZoneId.systemDefault();
		} else {
			zone = ZoneId.of(timeZone);
		}
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		try {
			formattedTime = formatter.format(ZonedDateTime.ofInstant(date.toInstant(), zone));
		} catch (final DateTimeException dTimeEx) {
			LOG.error("Error Occurred during parsing the date in desired format", dTimeEx);
		}
		return formattedTime;
	}

	/**
	 * Populates payment details
	 *
	 * @param target order request DTO
	 * @param source hybris order
	 *
	 */
	private void populatePaymentDetails(final OrderModel source, final B2BAdvanceOrderRequestDTO target) {
		if (CollectionUtils.isNotEmpty(source.getPaymentTransactions())) {
			List<B2BAdvancePaymentTransactionDTO> paymentTransactions = new ArrayList<>();
			source.getPaymentTransactions().forEach(transaction -> {
				B2BAdvancePaymentTransactionDTO transactionDTO = new B2BAdvancePaymentTransactionDTO();
				transactionDTO.setCode(transaction.getCode());
				transactionDTO.setPaymentProvider(transaction.getPaymentProvider());
				transactionDTO.setPlannedAmount(transaction.getPlannedAmount().toString());
				transactionDTO.setRequestID(transaction.getRequestId());
				transactionDTO.setRequestToken(transaction.getRequestToken());
				if (CollectionUtils.isNotEmpty(transaction.getEntries())) {
					List<B2BAdvancePaymentTransactionEntryDTO> entryDTOs = new ArrayList<>();
					transaction.getEntries().forEach(entry -> {
						B2BAdvancePaymentTransactionEntryDTO transactionEntryDTO = new B2BAdvancePaymentTransactionEntryDTO();
						entryDTOs.add(transactionEntryDTO);

						transactionEntryDTO.setCode(entry.getCode());
						transactionEntryDTO.setTime(formatDate(entry.getTime(), DATE_TIME_FORMAT, UTC));

						transactionEntryDTO.setCurrency(entry.getCurrency().getIsocode());
						transactionEntryDTO.setRequestID(entry.getRequestId());
						transactionEntryDTO.setRequestToken(entry.getRequestToken());
						transactionEntryDTO.setTransactionStatus(entry.getTransactionStatus());
						transactionEntryDTO.setType(entry.getType().getCode());
						transactionEntryDTO.setTransactionStatusDetails(entry.getTransactionStatusDetails());
					});
				}
				if (transaction.getInfo() != null) {
					B2BAdvancePaymentDetailsDTO paymentDetails = new B2BAdvancePaymentDetailsDTO();
					if (transaction.getInfo() instanceof InvoicePaymentInfoModel) {
						InvoicePaymentInfoModel invoicePaymentInfo = ((InvoicePaymentInfoModel) transaction.getInfo());
						paymentDetails.setPaymentType("INVOICE");
						paymentDetails.setHybrisPaymentIdentifier(invoicePaymentInfo.getCode());
					}
					populateBillingAddress(transaction.getInfo().getBillingAddress(), paymentDetails);

				}
				paymentTransactions.add(transactionDTO);
			});

		}
	}

	private void populateBillingAddress(final AddressModel billingAddress, final B2BAdvancePaymentDetailsDTO target) {
		final B2BAdvanceAddressDTO addressDTO = new B2BAdvanceAddressDTO();
		populateAddress(billingAddress, false, addressDTO);
		target.setBillingAddress(addressDTO);
	}

	/**
	 * Populates order entries and applied promotions
	 *
	 * @param source hybris order
	 * @param target order request DTO
	 */
	private void populateEntries(final OrderModel source, final B2BAdvanceOrderRequestDTO target) {
		if (null != source.getEntries()) {
			final List<B2BAdvanceOrderEntryDTO> orderEntryDTOList = new ArrayList<>();
			for (final AbstractOrderEntryModel entry : source.getEntries()) {

				final B2BAdvanceOrderEntryDTO orderEntry = new B2BAdvanceOrderEntryDTO();
				orderEntry.setEntryID(entry.getEntryNumber().toString());
				final ProductModel product = entry.getProduct();
				if (null != product) {
					orderEntry.setProductCode(product.getCode());
					if (product.getPicture() != null) {
						orderEntry.setImage(product.getPicture().getDownloadURL());
					}
				}
				orderEntry.setOriginalTotalPrice(entry.getTotalPrice());
				orderEntry.setBasePrice(entry.getBasePrice());
				orderEntry.setQuantity(BigDecimal.valueOf(entry.getQuantity().doubleValue()));
				if (CollectionUtils.isNotEmpty(entry.getTaxValues())) {
					orderEntry.setTaxValue(Double.valueOf(entry.getTaxValues().iterator().next().getAppliedValue()));
				}
				orderEntry.setTotalPrice(entry.getTotalPrice());
				orderEntryDTOList.add(orderEntry);
			}
			if (CollectionUtils.isNotEmpty(orderEntryDTOList)) {
				target.setEntries(orderEntryDTOList);
			}
		}

	}

	/**
	 * Populates taxes from hybris order
	 *
	 * @param source hybris order
	 * @param target to be populated with taxes
	 */
	private void populateTaxes(final OrderModel source, final B2BAdvanceOrderRequestDTO target) {
		final List<B2BAdvanceTaxDTO> taxDtoList = new ArrayList<>();
		if (null != source.getTotalTaxValues() && !source.getTotalTaxValues().isEmpty()) {

			for (final TaxValue tax : source.getTotalTaxValues()) {
				final B2BAdvanceTaxDTO taxDto = new B2BAdvanceTaxDTO();
				taxDto.setId(tax.getCode());
				taxDto.setPercentage(Double.valueOf(tax.getValue()));
				taxDto.setTotal(Double.valueOf(tax.getAppliedValue()));
				taxDtoList.add(taxDto);
			}
		}
		if (CollectionUtils.isNotEmpty(taxDtoList)) {
			target.setTaxes(taxDtoList);
		}
	}

	/**
	 * Populates Delivery Details
	 *
	 *
	 * @param source hybris order
	 * @param target to be populated with delivery details
	 */
	private void populateDeliveryDetails(final OrderModel source, final B2BAdvanceOrderRequestDTO target) {

		final B2BAdvanceDeliveryDetailsDTO deliveryDetails = new B2BAdvanceDeliveryDetailsDTO();

		if (null != source.getDeliveryMode() && StringUtils.isNotEmpty(source.getDeliveryMode().getCode())) {
			if (PICKUP.equals(source.getDeliveryMode().getCode())) {
				deliveryDetails.setDeliveryMethod(source.getDeliveryMode().getCode().toUpperCase());
			} else {
				deliveryDetails.setDeliveryMethod(HOME_DELIVERY);
			}
		}

		AddressModel address = null;
		boolean isHomeDelivery = false;
		if (source.getDeliveryMode() instanceof PickUpDeliveryModeModel) {
			// populate address as per implementation
		} else {
			address = source.getDeliveryAddress();
			isHomeDelivery = true;
		}
		if (null != address) {
			final B2BAdvanceAddressDTO deliveryAddressDTO = new B2BAdvanceAddressDTO();
			populateAddress(address, isHomeDelivery, deliveryAddressDTO);
			// Set Delivery Address
			deliveryDetails.setDeliveryAddress(deliveryAddressDTO);
		}
		deliveryDetails.setDeliveryInstructions(source.getDeliveryInstructions());
		target.setDeliveryDetails(deliveryDetails);

	}

	private void populateAddress(final AddressModel address, final boolean isHomeDelivery,
			final B2BAdvanceAddressDTO addressDTO) {
		if (isHomeDelivery) {
			addressDTO.setAddressKind(RESIDENTIAL_ADDRESS);// R
		} else {
			addressDTO.setAddressKind(POSTAL_ADDRESS);// P
		}
		addressDTO.setCity(address.getTown());
		addressDTO.setPostcode(address.getPostalcode());
		addressDTO.setStreet(address.getStreetname());
		addressDTO.setStreetNumber(address.getStreetnumber());
		addressDTO.setHouseNumber(address.getAppartment());
		// Add following fields if address is fetched from third party
		// addressDTO.setAddressID(address.getAddressID());
		// addressDTO.setLatitude(address.getLatitude());
		// addressDTO.setLongitude(address.getLongitude());
		// addressDTO.setRuralDelivery(address.getRuralDelivery());
	}
}
