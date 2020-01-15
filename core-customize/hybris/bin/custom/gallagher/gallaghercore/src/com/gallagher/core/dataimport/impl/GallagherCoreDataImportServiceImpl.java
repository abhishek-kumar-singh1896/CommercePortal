/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.core.dataimport.impl;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;


/**
 * Extension of {@link CoreDataImportService} that defines services related to core data.
 *
 * @author Vikram Bishnoi
 */
public class GallagherCoreDataImportServiceImpl extends CoreDataImportService
{

	private static final String SOLR_OVERRIDE_CONFIG_NAME = "%s.solr.override.config.name";

	@Override
	protected void importSolrIndex(final String extensionName, final String storeName)
	{
		/* Get the overriden config name for the store */
		String configuredConfigName = Config.getParameter(String.format(SOLR_OVERRIDE_CONFIG_NAME, storeName));

		if (StringUtils.isBlank(configuredConfigName))
		{
			configuredConfigName = String.format("%sIndex", storeName);
		}

		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/stores/%s/solr.impex", extensionName, storeName),
				false);

		getSetupSolrIndexerService().createSolrIndexerCronJobs(configuredConfigName);

		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/coredata/stores/%s/solrtrigger.impex", extensionName, storeName), false);
	}

	/**
	 * Runs solr indexer jobs
	 *
	 * @param extensionName
	 * @param storeName
	 */
	@Override
	public void runSolrIndex(final String extensionName, final String storeName)
	{
		String configuredConfigName = Config.getParameter(String.format(SOLR_OVERRIDE_CONFIG_NAME, storeName));

		if (StringUtils.isBlank(configuredConfigName))
		{
			configuredConfigName = String.format("%sIndex", storeName);
		}
		getSetupSolrIndexerService().executeSolrIndexerCronJob(configuredConfigName, true);
		getSetupSolrIndexerService().activateSolrIndexerCronJobs(configuredConfigName);
	}
}
