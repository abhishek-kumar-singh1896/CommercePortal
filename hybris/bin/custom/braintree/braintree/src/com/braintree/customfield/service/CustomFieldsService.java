package com.braintree.customfield.service;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


public interface CustomFieldsService
{
	Map<String, String> getDefaultCustomFieldsMap();
}
