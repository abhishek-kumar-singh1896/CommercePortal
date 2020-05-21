package com.gallagher.outboundservices.decorator.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.model.impl.DefaultIntegrationObjectDescriptor;
import de.hybris.platform.integrationservices.service.IntegrationObjectNotFoundException;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.outboundservices.decorator.DecoratorContext;
import de.hybris.platform.outboundservices.decorator.DecoratorExecution;
import de.hybris.platform.outboundservices.decorator.impl.DefaultPayloadBuildingRequestDecorator;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;


public class GallagherPayloadBuildingRequestDecorator extends DefaultPayloadBuildingRequestDecorator
{

	private IntegrationObjectService integrationObjectService;

	@Override
	public HttpEntity<Map<String, Object>> decorate(final HttpHeaders httpHeaders, final Map<String, Object> payload,
			final DecoratorContext context, final DecoratorExecution execution)
	{
		final Map<String, Object> map = getIntegrationObjectConversionService().convert(context.getItemModel(),
				context.getIntegrationObjectCode());


		if (context.getItemModel() instanceof CustomerModel)
		{

			final TypeDescriptor descriptor = findIntegrationObjectDescriptor(context.getIntegrationObjectCode())
					.getItemTypeDescriptor(context.getItemModel()).get();

			if (map.get("newsLetters") == null && descriptor.getAttribute("newsLetters").isPresent())
			{
				map.put("newsLetters", false);
			}
			if (map.get("productPromo") == null && descriptor.getAttribute("productPromo").isPresent())
			{
				map.put("productPromo", false);
			}
			if (map.get("events") == null && descriptor.getAttribute("events").isPresent())
			{
				map.put("events", false);
			}
			if (map.get("productRelease") == null && descriptor.getAttribute("productRelease").isPresent())
			{
				map.put("productRelease", false);
			}
			if (map.get("productUpdate") == null && descriptor.getAttribute("productUpdate").isPresent())
			{
				map.put("productUpdate", false);
			}
		}
		payload.putAll(map);

		return execution.createHttpEntity(httpHeaders, payload, context);
	}

	protected IntegrationObjectDescriptor findIntegrationObjectDescriptor(final String integrationObjectCode)
	{
		try
		{
			final IntegrationObjectModel model = integrationObjectService.findIntegrationObject(integrationObjectCode);
			return DefaultIntegrationObjectDescriptor.create(model);
		}
		catch (final ModelNotFoundException e)
		{
			throw new IntegrationObjectNotFoundException(integrationObjectCode, e);
		}
	}

	protected IntegrationObjectService getIntegrationObjectService()
	{
		return integrationObjectService;
	}

	public void setIntegrationObjectService(final IntegrationObjectService integrationObjectService)
	{
		this.integrationObjectService = integrationObjectService;
	}
}
