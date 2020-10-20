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
package com.gallagher.b2b.initialdata.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gallagher.b2b.initialdata.constants.GallagherB2BInitialDataConstants;
import com.gallagher.b2b.initialdata.setup.services.dataimport.impl.GallagherB2BinitialdataCoreDataImportService;
import com.gallagher.b2b.initialdata.setup.services.dataimport.impl.GallagherB2BinitialdataSampleDataImportService;


/**
 * This class provides hooks into the system's initialization and update processes.
 */
@SystemSetup(extension = GallagherB2BInitialDataConstants.EXTENSIONNAME)
public class GallagherB2BInitialDataSystemSetup extends AbstractSystemSetup
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(GallagherB2BInitialDataSystemSetup.class);

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";

	private GallagherB2BinitialdataCoreDataImportService coreDataImportService;
	private GallagherB2BinitialdataSampleDataImportService sampleDataImportService;

	public static final String SECURITY_B2B = "securityB2B";
	public static final String SECURITY_B2B_MASTER = "securityB2BMaster";
	public static final String SECURITY_B2B_NZ = "securityB2BNZ";
	public static final String SECURITY_B2B_AU = "securityB2BAU";
	public static final String SECURITY_B2B_GLOBAL = "securityB2BGlobal";

	public static final String SECURITY_B2B_STHAM = "securityB2BSthAm";
	public static final String SECURITY_B2B_US = "securityB2BUS";
	public static final String SECURITY_B2B_CA = "securityB2BCA";
	public static final String SECURITY_B2B_UK = "securityB2BUK";
	public static final String SECURITY_B2B_ASIA = "securityB2BAsia";

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));
		params.add(createBooleanSystemSetupParameter("NEW OPTION", "New Option", true));
		// Add more Parameters here as you require

		return params;
	}

	/**
	 * Implement this method to create initial objects. This method will be called by system creator during
	 * initialization and system update. Be sure that this method can be called repeatedly.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		// Add Essential Data here as you require
	}

	/**
	 * Implement this method to create data that is used in your project. This method will be called during the system
	 * initialization. <br>
	 * Add import data for each site you have configured
	 *
	 * <pre>
	 * final List<ImportData> importData = new ArrayList<ImportData>();
	 *
	 * final ImportData sampleImportData = new ImportData();
	 * sampleImportData.setProductCatalogName(SAMPLE_PRODUCT_CATALOG_NAME);
	 * sampleImportData.setContentCatalogNames(Arrays.asList(SAMPLE_CONTENT_CATALOG_NAME));
	 * sampleImportData.setStoreNames(Arrays.asList(SAMPLE_STORE_NAME));
	 * importData.add(sampleImportData);
	 *
	 * getCoreDataImportService().execute(this, context, importData);
	 * getEventService().publishEvent(new CoreDataImportedEvent(context, importData));
	 *
	 * getSampleDataImportService().execute(this, context, importData);
	 * getEventService().publishEvent(new SampleDataImportedEvent(context, importData));
	 * </pre>
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<ImportData>();

		// import NZ Catalog data
		final ImportData securityMasterImportData = new ImportData();
		securityMasterImportData.setProductCatalogName(SECURITY_B2B_MASTER);
		securityMasterImportData.setContentCatalogNames(Arrays.asList(SECURITY_B2B));
		securityMasterImportData.setStoreNames(Collections.EMPTY_LIST);
		importData.add(securityMasterImportData);

		// import NZ Catalog data
		final ImportData securityNZImportData = new ImportData();
		securityNZImportData.setProductCatalogName(SECURITY_B2B_NZ);
		securityNZImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		securityNZImportData.setStoreNames(Arrays.asList(SECURITY_B2B_NZ));
		importData.add(securityNZImportData);

		//import AU catalog data
		final ImportData securityAUImportData = new ImportData();
		securityAUImportData.setProductCatalogName(SECURITY_B2B_AU);
		securityAUImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		securityAUImportData.setStoreNames(Arrays.asList(SECURITY_B2B_AU));
		importData.add(securityAUImportData);

		//import GLOBAL catalog data
		final ImportData securityGlobalImportData = new ImportData();
		securityGlobalImportData.setProductCatalogName(SECURITY_B2B_GLOBAL);
		securityGlobalImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		securityGlobalImportData.setStoreNames(Arrays.asList(SECURITY_B2B_GLOBAL));
		importData.add(securityGlobalImportData);

		//import US catalog data
		final ImportData securityUSImportData = new ImportData();
		securityUSImportData.setProductCatalogName(SECURITY_B2B_US);
		securityUSImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		securityUSImportData.setStoreNames(Arrays.asList(SECURITY_B2B_US));
		importData.add(securityUSImportData);

		//import UK catalog data
		final ImportData securityUKImportData = new ImportData();
		securityUKImportData.setProductCatalogName(SECURITY_B2B_UK);
		securityUKImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		securityUKImportData.setStoreNames(Arrays.asList(SECURITY_B2B_UK));
		importData.add(securityUKImportData);

		//import CA catalog data
		final ImportData securityCAImportData = new ImportData();
		securityCAImportData.setProductCatalogName(SECURITY_B2B_CA);
		securityCAImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		securityCAImportData.setStoreNames(Arrays.asList(SECURITY_B2B_CA));
		importData.add(securityCAImportData);

		//import ASIA catalog data
		final ImportData securityAsiaImportData = new ImportData();
		securityAsiaImportData.setProductCatalogName(SECURITY_B2B_ASIA);
		securityAsiaImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		securityAsiaImportData.setStoreNames(Arrays.asList(SECURITY_B2B_ASIA));
		importData.add(securityAsiaImportData);

		//import South America catalog data
		final ImportData securitySthAmImportData = new ImportData();
		securitySthAmImportData.setProductCatalogName(SECURITY_B2B_STHAM);
		securitySthAmImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		securitySthAmImportData.setStoreNames(Arrays.asList(SECURITY_B2B_STHAM));
		importData.add(securitySthAmImportData);

		getCoreDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new CoreDataImportedEvent(context, importData));
		importImpexFile(context, "/gallagherb2binitialdata/import/coredata/common/security-sync.impex");
		importImpexFile(context, "/gallagherb2binitialdata/import/coredata/common/cronjobs.impex");
		getSampleDataImportService().execute(this, context, importData);
		getSampleDataImportService().importCommerceOrgData(context);
		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));

	}

	public GallagherB2BinitialdataCoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Required
	public void setCoreDataImportService(final GallagherB2BinitialdataCoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public GallagherB2BinitialdataSampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Required
	public void setSampleDataImportService(final GallagherB2BinitialdataSampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}
}
