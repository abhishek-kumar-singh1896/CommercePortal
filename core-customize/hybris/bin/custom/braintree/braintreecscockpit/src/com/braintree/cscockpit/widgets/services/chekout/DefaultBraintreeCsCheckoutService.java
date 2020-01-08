/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.braintree.cscockpit.widgets.services.chekout;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ResourceMessage;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.services.checkout.impl.DefaultCsCheckoutService;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.braintree.command.result.BrainTreeCreatePaymentMethodResult;
import com.braintree.method.BrainTreePaymentService;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.payment.dto.BraintreeInfo;
import com.braintree.servicelayer.i18n.BraintreeRegionI18NService;
import com.braintree.transaction.service.BrainTreeTransactionService;


public class DefaultBraintreeCsCheckoutService extends DefaultCsCheckoutService implements BraintreeCsCheckoutService
{

	private BrainTreeTransactionService brainTreeTransactionService;
	private BrainTreePaymentService brainTreePaymentService;
	private ModelService modelService;
	private CommonI18NService commonI18NService;
	private BraintreeRegionI18NService regionI18NService;

	@Override
	public PaymentTransactionEntryModel authorisePayment(final CartModel paramCartModel,
			final BrainTreePaymentInfoModel paymentInfo, final BigDecimal amount) throws PaymentException, ValidationException
	{
		throw new UnsupportedOperationException("Is not implemented yet");
	}

	@Override
	public void createPaymentInfo(final CartModel cart, final CardInfo cardInfo, final double amount,
			final BraintreeInfo braintreeInfo) throws PaymentException, ValidationException
	{

		getCsCardPaymentService().validate(cart, cardInfo, amount);

		final CustomerModel customer = (CustomerModel) cart.getUser();
		final AddressModel billingAddress = convertBillingInfo(cardInfo.getBillingInfo());

		final BrainTreeCreatePaymentMethodResult paymentMethodResult = getBrainTreePaymentService()
				.createPaymentMethodForCustomer(customer, billingAddress, braintreeInfo);

		checkBraintreeResult(paymentMethodResult);

		addAdditionalPaymentMethodFields(braintreeInfo, paymentMethodResult);

		final BrainTreePaymentInfoModel paymentInfo = getBrainTreeTransactionService().createSubscription(billingAddress, customer,
				braintreeInfo);

		paymentInfo.setPaymentMethodToken(paymentMethodResult.getPaymentMethodToken());
		cart.setPaymentInfo(paymentInfo);

		getModelService().saveAll(cart, billingAddress, customer, paymentInfo);
		getModelService().refresh(cart);
	}

	private void checkBraintreeResult(final BrainTreeCreatePaymentMethodResult paymentMethodResult) throws PaymentException
	{
		if (!paymentMethodResult.isSuccess())
		{
			throw new PaymentException(paymentMethodResult.getErrorMessage());
		}
	}

	private void addAdditionalPaymentMethodFields(final BraintreeInfo braintreeInfo,
			final BrainTreeCreatePaymentMethodResult createPaymentMethodResult)
	{
		if (createPaymentMethodResult != null)
		{
			braintreeInfo.setPaymentMethodToken(createPaymentMethodResult.getPaymentMethodToken());
			braintreeInfo.setExpirationMonth(createPaymentMethodResult.getExpirationMonth());
			braintreeInfo.setExpirationYear(createPaymentMethodResult.getExpirationYear());
			braintreeInfo.setImageSource(createPaymentMethodResult.getImageSource());
			braintreeInfo.setCardNumber(createPaymentMethodResult.getCardNumber());
			braintreeInfo.setCardholderName(createPaymentMethodResult.getCardholderName());
			braintreeInfo.setCardType(createPaymentMethodResult.getCardType());
			braintreeInfo.setEmail(createPaymentMethodResult.getEmail());
		}
	}

	//TODO: IP - Move to populator
	private AddressModel convertBillingInfo(final BillingInfo billingInfo) throws ValidationException
	{
		final AddressModel address = getModelService().create(AddressModel.class);
		address.setFirstname(billingInfo.getFirstName());
		address.setLastname(billingInfo.getLastName());
		address.setStreetname(billingInfo.getStreet1());
		address.setStreetnumber(billingInfo.getStreet2());
		address.setTown(billingInfo.getCity());
		address.setPostalcode(billingInfo.getPostalCode());
		address.setPhone1(billingInfo.getPhoneNumber());
		address.setEmail(billingInfo.getEmail());


		if (StringUtils.isNotEmpty(billingInfo.getCountry()))
		{
			final String isocode = billingInfo.getCountry();
			try
			{
				final CountryModel countryModel = getCommonI18NService().getCountry(isocode);
				address.setCountry(countryModel);
				findRegion(billingInfo, address, countryModel);
			}
			catch (final UnknownIdentifierException e)
			{
				throw new ConversionException("No country with the code " + isocode + " found.", e);
			}
			catch (final AmbiguousIdentifierException e)
			{
				throw new ConversionException("More than one country with the code " + isocode + " found.", e);
			}
		}

		return address;
	}

	private void findRegion(final BillingInfo billingInfo, final AddressModel address, final CountryModel countryModel)
			throws ValidationException
	{
		if (StringUtils.isNotEmpty(billingInfo.getState()))
		{
			final String regionSign = billingInfo.getState();
			if (CollectionUtils.isNotEmpty(countryModel.getRegions()))
			{
				final RegionModel regionModel = getRegionI18NService().findRegion(countryModel, regionSign);
				validateRegion(regionModel);
				address.setRegion(regionModel);
			}
			else
			{
				address.setZone(regionSign);
			}
		}
	}

	private void validateRegion(final RegionModel regionModel) throws ValidationException
	{
		if (regionModel == null)
		{
			throw new ValidationException(
					Arrays.<ResourceMessage> asList(new ResourceMessage("paymentOption.billingInfo.validation.noRegion")));
		}
	}

	/**
	 * @return the brainTreeTransactionService
	 */
	public BrainTreeTransactionService getBrainTreeTransactionService()
	{
		return brainTreeTransactionService;
	}

	/**
	 * @param brainTreeTransactionService
	 *           the brainTreeTransactionService to set
	 */
	public void setBrainTreeTransactionService(final BrainTreeTransactionService brainTreeTransactionService)
	{
		this.brainTreeTransactionService = brainTreeTransactionService;
	}

	/**
	 * @return the modelService
	 */
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the brainTreePaymentService
	 */
	public BrainTreePaymentService getBrainTreePaymentService()
	{
		return brainTreePaymentService;
	}

	/**
	 * @param brainTreePaymentService
	 *           the brainTreePaymentService to set
	 */
	public void setBrainTreePaymentService(final BrainTreePaymentService brainTreePaymentService)
	{
		this.brainTreePaymentService = brainTreePaymentService;
	}

	@Override
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Override
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public BraintreeRegionI18NService getRegionI18NService()
	{
		return regionI18NService;
	}

	public void setRegionI18NService(final BraintreeRegionI18NService regionI18NService)
	{
		this.regionI18NService = regionI18NService;
	}
}
