/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.enterprisewide.b2badvance.core.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.acceleratorservices.cartfileupload.data.SavedCartFileUploadReportData;
import de.hybris.platform.acceleratorservices.enums.ImportStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceSaveCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.order.CartFactory;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.dto.OrderTemplateImportDTO;
import com.enterprisewide.b2badvance.core.dto.OrderTemplateImportEntryDTO;
import com.enterprisewide.b2badvance.core.order.B2BAdvanceSaveCartService;
import com.enterprisewide.b2badvance.core.order.dao.B2BAdvanceSaveCartDao;


/**
 * B2B implementation of the interface {@link B2BAdvanceSaveCartService}
 */
public class B2BAdvanceSaveCartServiceImpl extends DefaultCommerceSaveCartService implements B2BAdvanceSaveCartService
{
	private static final Logger LOG = Logger.getLogger(B2BAdvanceSaveCartServiceImpl.class);

	private TimeService timeService;
	private CartFactory cartFactory;
	private ModelService modelService;
	private ProductService productService;
	private KeyGenerator guidKeyGenerator;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private CommonI18NService commonI18NService;
	private CommerceCartService commerceCartService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SearchPageData<CartModel> getOrderTemplatesForSiteAndUser(final PaginationData pagination, final BaseSiteModel baseSite,
			final UserModel user)
	{
		return ((B2BAdvanceSaveCartDao) getSaveCartDao()).getOrderTemplatesForSiteAndUser(pagination, baseSite, user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getOrderTemplatesCountForSiteAndUser(final BaseSiteModel baseSite, final UserModel user)
	{
		validateParameterNotNull(user, "user cannot be null");
		return (getUserService().isAnonymousUser(user)) ? Integer.valueOf(0)
				: ((B2BAdvanceSaveCartDao) getSaveCartDao()).getOrderTemplatesCountForSiteAndUser(baseSite, user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createOrderTemplate(final OrderTemplateImportDTO template)
	{
		final CartModel cartModel = getCartFactory().createCart();
		cartModel.setSaveTime(getTimeService().getCurrentTime());
		cartModel.setName(template.getName());
		cartModel.setDescription(template.getDescription());
		cartModel.setUser(getUserService().getCurrentUser());
		cartModel.setCurrency(getCommonI18NService().getCurrentCurrency());
		cartModel.setDate(getTimeService().getCurrentTime());
		cartModel.setSite(getBaseSiteService().getCurrentBaseSite());
		cartModel.setSavedBy(getUserService().getCurrentUser());
		cartModel.setImportStatus(ImportStatus.COMPLETED);
		cartModel.setOrderTemplate(true);
		cartModel.setStore(getBaseStoreService().getCurrentBaseStore());
		cartModel.setGuid(getGuidKeyGenerator().generate().toString());
		getModelService().save(cartModel);
		getModelService().refresh(cartModel);


		final List<CommerceCartModification> errorModifications = new LinkedList<>();
		final SavedCartFileUploadReportData savedCartFileUploadReportData = new SavedCartFileUploadReportData();
		final AtomicInteger successCounter = new AtomicInteger(0);
		final AtomicInteger partialImportCounter = new AtomicInteger(0);
		final AtomicInteger failureCounter = new AtomicInteger(0);
		for (final OrderTemplateImportEntryDTO entry : template.getEntries())
		{

			if (StringUtils.isNotEmpty(entry.getProductCode()) && entry.getQuantity() != null)
			{
				try
				{
					final CommerceCartModification commerceCartModification = addLinesToCart(entry, cartModel);
					if (!CommerceCartModificationStatus.SUCCESS.equals(commerceCartModification.getStatusCode()))
					{
						errorModifications.add(commerceCartModification);
						if (commerceCartModification.getQuantityAdded() > 0)
						{
							partialImportCounter.incrementAndGet();
						}
						else
						{
							failureCounter.incrementAndGet();
						}
					}
					else
					{
						successCounter.incrementAndGet();
					}
				}
				catch (final CommerceCartModificationException | UnknownIdentifierException e)
				{
					LOG.debug("Import of line for cart:" + cartModel.getCode() + " failed due to" + e.getMessage());
					errorModifications.add(handleExceptionForImport(e));
					failureCounter.incrementAndGet();
				}
			}
		}
		savedCartFileUploadReportData.setErrorModificationList(errorModifications);
		savedCartFileUploadReportData.setSuccessCount(Integer.valueOf(successCounter.get()));
		savedCartFileUploadReportData.setFailureCount(Integer.valueOf(failureCounter.get()));
		savedCartFileUploadReportData.setPartialImportCount(Integer.valueOf(partialImportCounter.get()));
		//return savedCartFileUploadReportData;
	}

	protected CommerceCartModification handleExceptionForImport(final Exception ex)
	{
		final CommerceCartModification commerceCartModification = new CommerceCartModification();
		commerceCartModification.setStatusCode(ex.getMessage());
		commerceCartModification.setQuantityAdded(0);

		return commerceCartModification;
	}

	protected CommerceCartModification addLinesToCart(final OrderTemplateImportEntryDTO entry, final CartModel cartModel)
			throws CommerceCartModificationException
	{
		final CommerceCartParameter commerceCartParameter = createCommerceCartParam(entry.getProductCode(), entry.getQuantity(),
				cartModel);
		return getCommerceCartService().addToCart(commerceCartParameter);
	}


	protected CommerceCartParameter createCommerceCartParam(final String code, final long quantity, final CartModel cartModel)
			throws CommerceCartModificationException
	{
		final ProductModel product = getProductService().getProductForCode(code);
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setQuantity(quantity);
		parameter.setProduct(product);
		parameter.setCreateNewEntry(false);
		return parameter;
	}


	protected TimeService getTimeService()
	{
		return timeService;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	protected KeyGenerator getGuidKeyGenerator()
	{
		return guidKeyGenerator;
	}

	@Required
	public void setGuidKeyGenerator(final KeyGenerator guidKeyGenerator)
	{
		this.guidKeyGenerator = guidKeyGenerator;
	}

	protected CartFactory getCartFactory()
	{
		return cartFactory;
	}

	@Required
	public void setCartFactory(final CartFactory cartFactory)
	{
		this.cartFactory = cartFactory;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	@Required
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}
}
