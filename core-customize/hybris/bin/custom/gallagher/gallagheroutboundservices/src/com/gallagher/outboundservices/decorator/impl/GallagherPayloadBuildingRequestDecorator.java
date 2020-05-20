package com.gallagher.outboundservices.decorator.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.outboundservices.decorator.DecoratorContext;
import de.hybris.platform.outboundservices.decorator.DecoratorExecution;
import de.hybris.platform.outboundservices.decorator.impl.DefaultPayloadBuildingRequestDecorator;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;


public class GallagherPayloadBuildingRequestDecorator extends DefaultPayloadBuildingRequestDecorator
{


	@Override
	public HttpEntity<Map<String, Object>> decorate(final HttpHeaders httpHeaders, final Map<String, Object> payload,
			final DecoratorContext context, final DecoratorExecution execution)
	{
		final Map<String, Object> map = getIntegrationObjectConversionService().convert(context.getItemModel(),
				context.getIntegrationObjectCode());

		if (context.getItemModel() instanceof CustomerModel)
		{
			final boolean value = map.containsKey("newsLetters");
			if (map.get("newsLetters") == null)
			{
				map.put("newsLetters", false);
			}
			if (map.get("productPromo") == null)
			{
				map.put("productPromo", false);
			}
			if (map.get("events") == null)
			{
				map.put("events", false);
			}
			if (map.get("productRelease") == null)
			{
				map.put("productRelease", false);
			}
			if (map.get("productUpdate") == null)
			{
				map.put("productUpdate", false);
			}
		}
		payload.putAll(map);

		return execution.createHttpEntity(httpHeaders, payload, context);
	}


}
