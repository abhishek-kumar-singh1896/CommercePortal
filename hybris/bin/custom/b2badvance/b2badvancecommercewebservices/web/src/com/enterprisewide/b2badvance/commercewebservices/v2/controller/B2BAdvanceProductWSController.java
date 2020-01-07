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
package com.enterprisewide.b2badvance.commercewebservices.v2.controller;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.enterprisewide.b2badvance.commercewebservices.dto.WSRequestHeaderDTO;
import com.enterprisewide.b2badvance.commercewebservices.dto.WSResponseHeaderDTO;
import com.enterprisewide.b2badvance.commercewebservices.product.dto.BaseProductWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.product.dto.ProductErrorWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.product.dto.VariantProductWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.product.request.BaseProductReqestWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.product.request.VariantProductReqestWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.product.response.ProductResponseWsDTO;
import com.enterprisewide.b2badvance.facades.product.B2BAdvanceProductFacade;
import com.enterprisewide.b2badvance.facades.product.data.B2BAdvanceBaseProductData;
import com.enterprisewide.b2badvance.facades.product.data.B2BAdvanceVariantProductData;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Controller for product related requests such as creating updating products (base/variants)
 *
 * @author Enterprise Wide
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/products")
public class B2BAdvanceProductWSController extends B2BAdvanceBaseCommerceController
{
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceProductWSController.class);
	private static final String ERROR_RESPONSE_HEADER_CODE = "500";
	private static final String SUCCESS_RESPONSE_HEADER_CODE = "200";
	private static final String REJECTED_RESPONSE_HEADER_CODE = "406";

	@Resource(name = "baseProductDTOValidator")
	private Validator baseProductDTOValidator;

	@Resource(name = "variantProductDTOValidator")
	private Validator variantProductDTOValidator;

	@Resource(name = "productVariantFacade")
	private B2BAdvanceProductFacade productFacade;

	/**
	 * Handles base products create/update requests. Accepts a list of products and validates each product. This API
	 * saves all the valid base products into the system and sends error response for all other invalid products
	 *
	 * @param productRequest
	 *           contains the list of products
	 * @return response with detailed error messages for base products
	 */
	@Secured("IS_AUTHENTICATED_FULLY")
	@RequestMapping(value = "/createUpdateBaseProducts", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@ResponseBody
	@ApiOperation(nickname = "createUpdateBaseProducts", value = " Creates/Updates Base Products")
	@ApiBaseSiteIdParam
	public ProductResponseWsDTO createUpdateBaseProducts(@ApiParam(value = "Object containing base product list.", required = true)
	@RequestBody
	final BaseProductReqestWsDTO productRequest)
	{
		final WSRequestHeaderDTO requestHeader = productRequest.getHeader();
		/* Logging */
		LOG.info("Base Product update request has been generated on the system [{}] by the user [{}] at [{}]",
				requestHeader.getSystemID(), requestHeader.getUserID(), requestHeader.getTimestamp());
		final String transactionId = RandomStringUtils.random(16, true, true);
		String infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" REQUEST ")
				.append("Base Product update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(productRequest, infoLogString, "JSON processing error occurred for request");
		final ProductResponseWsDTO productResponse = new ProductResponseWsDTO();
		productResponse.setHeader(new WSResponseHeaderDTO());
		final List<ProductErrorWsDTO> errorEntries = new ArrayList<>();
		if (productRequest == null || CollectionUtils.isEmpty(productRequest.getProducts()))
		{
			productResponse.getHeader().setResponseCode(REJECTED_RESPONSE_HEADER_CODE);
			addError(null, errorEntries, Collections.singletonList("No product available in request"));
		}
		else
		{
			productResponse.getHeader().setResponseCode(SUCCESS_RESPONSE_HEADER_CODE);
			final List<B2BAdvanceBaseProductData> productDataList = new ArrayList<>(productRequest.getProducts().size());
			/* Validate and convert products for facade layer */
			productRequest.getProducts().forEach(product -> processBaseProducts(product, productDataList, errorEntries));
			/* Saves products and updates error list with all the errors occurred during save */
			saveBaseProducts(errorEntries, productDataList);
		}
		if (CollectionUtils.isNotEmpty(errorEntries))
		{
			productResponse.setErrorEntries(errorEntries);
		}
		/* Logging */
		infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" RESPONSE ")
				.append("Base Product update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(productResponse, infoLogString, "JSON processing error occurred for response");
		return productResponse;
	}

	/**
	 * Validate and convert base product for facade layer
	 *
	 * @param product
	 *           to be validated
	 * @param productDataList
	 *           to hold the productData converted from WS DTO
	 * @param errorEntries
	 *           to hold all the validation errors
	 */
	private void processBaseProducts(final BaseProductWsDTO product, final List<B2BAdvanceBaseProductData> productDataList,
			final List<ProductErrorWsDTO> errorEntries)
	{
		try
		{

			validate(product, "product", baseProductDTOValidator);
			productDataList.add(getDataMapper().map(product, B2BAdvanceBaseProductData.class));
		}
		catch (final WebserviceValidationException wsVEx)
		{
			addError(product.getArticleNumber(), errorEntries, ((Errors) wsVEx.getValidationObject()));
		}
	}

	/**
	 * Saves base products
	 *
	 * @param errorEntries
	 *           to hold all the processing errors
	 * @param baseProducts
	 *           to be saved
	 */
	private void saveBaseProducts(final List<ProductErrorWsDTO> errorEntries, final List<B2BAdvanceBaseProductData> baseProducts)
	{
		if (CollectionUtils.isNotEmpty(baseProducts))
		{
			final Map<String, String> errorMap = productFacade.saveBaseProducts(baseProducts);
			if (MapUtils.isNotEmpty(errorMap))
			{
				errorMap.forEach((k, v) -> addError(k, errorEntries, Collections.singletonList(v)));
			}
		}
	}

	/**
	 * Handles variant products create/update requests. Accepts a list of products and validates each product. This API
	 * saves all the valid variant products into the system and sends error response for all other invalid variant
	 * products
	 *
	 * @param productRequest
	 *           contains the list of products
	 * @return response with detailed error messages for variant products
	 */
	@Secured("IS_AUTHENTICATED_FULLY")
	@RequestMapping(value = "/createUpdateVariantProducts", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@ResponseBody
	@ApiOperation(nickname = "createUpdateVariantProducts", value = " Creates/Updates Variant Products")
	@ApiBaseSiteIdParam
	public ProductResponseWsDTO createUpdateVariantProducts(
			@ApiParam(value = "Object containing variant product list.", required = true)
			@RequestBody
			final VariantProductReqestWsDTO productRequest)
	{
		final WSRequestHeaderDTO requestHeader = productRequest.getHeader();
		/* Logging */
		LOG.info("Variant Product update request has been generated on the system [{}] by the user [{}] at [{}]",
				requestHeader.getSystemID(), requestHeader.getUserID(), requestHeader.getTimestamp());
		final String transactionId = RandomStringUtils.random(16, true, true);
		String infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" REQUEST ")
				.append("Variant Product update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(productRequest, infoLogString, "JSON processing error occurred for request");
		final ProductResponseWsDTO productResponse = new ProductResponseWsDTO();
		productResponse.setHeader(new WSResponseHeaderDTO());
		final List<ProductErrorWsDTO> errorEntries = new ArrayList<>();
		if (productRequest == null || CollectionUtils.isEmpty(productRequest.getProducts()))
		{
			productResponse.getHeader().setResponseCode(REJECTED_RESPONSE_HEADER_CODE);
			addError(null, errorEntries, Collections.singletonList("No variant product available in request"));
		}
		else
		{
			productResponse.getHeader().setResponseCode(SUCCESS_RESPONSE_HEADER_CODE);
			final List<B2BAdvanceVariantProductData> productDataList = new ArrayList<>(productRequest.getProducts().size());
			/* Validate and convert products for facade layer */
			productRequest.getProducts().forEach(product -> processvariantProducts(product, productDataList, errorEntries));
			/* Saves products and updates error list with all the errors occurred during save */
			saveVariantProducts(errorEntries, productDataList);
		}
		if (CollectionUtils.isNotEmpty(errorEntries))
		{
			productResponse.setErrorEntries(errorEntries);
		}
		/* Logging */
		infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" RESPONSE ")
				.append("Base Product update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(productResponse, infoLogString, "JSON processing error occurred for response");
		return productResponse;
	}

	/**
	 * Validate and convert base product for facade layer
	 *
	 * @param product
	 *           to be validated
	 * @param productDataList
	 *           to hold the productData converted from WS DTO
	 * @param errorEntries
	 *           to hold all the validation errors
	 */
	private void processvariantProducts(final VariantProductWsDTO product,
			final List<B2BAdvanceVariantProductData> productDataList, final List<ProductErrorWsDTO> errorEntries)
	{
		try
		{

			validate(product, "product", variantProductDTOValidator);
			productDataList.add(getDataMapper().map(product, B2BAdvanceVariantProductData.class));
		}
		catch (final WebserviceValidationException wsVEx)
		{
			addError(product.getArticleNumber(), errorEntries, ((Errors) wsVEx.getValidationObject()));
		}
	}

	/**
	 * Saves variant products
	 *
	 * @param errorEntries
	 *           to hold all the processing errors
	 * @param variantProducts
	 *           to be saved
	 */
	private void saveVariantProducts(final List<ProductErrorWsDTO> errorEntries,
			final List<B2BAdvanceVariantProductData> variantProducts)
	{
		if (CollectionUtils.isNotEmpty(variantProducts))
		{
			final Map<String, String> errorMap = productFacade.saveVariantProducts(variantProducts);
			if (MapUtils.isNotEmpty(errorMap))
			{
				errorMap.forEach((k, v) -> addError(k, errorEntries, Collections.singletonList(v)));
			}
		}
	}
}
