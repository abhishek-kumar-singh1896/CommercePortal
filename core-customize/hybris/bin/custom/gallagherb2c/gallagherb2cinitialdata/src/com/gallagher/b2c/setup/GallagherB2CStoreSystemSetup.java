/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.setup;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.gallagher.b2c.constants.Gallagherb2cinitialdataConstants;


@SystemSetup(extension = Gallagherb2cinitialdataConstants.EXTENSIONNAME)
public class GallagherB2CStoreSystemSetup extends AbstractSystemSetup
{
	public static final String AM_B2C = "amB2C";
	public static final String AM_B2C_CA = "amB2CCA";
	public static final String AM_B2C_US = "amB2CUS";
	public static final String AM_B2C_AU = "amB2CAU";
	public static final String AM_B2C_NZ = "amB2CNZ";
	public static final String AM_B2C_GLOBAL = "amB2CGlobal";
	public static final String AM_B2C_LATAM = "amB2CLatAm";
	public static final String AM_B2C_MASTER = "amB2CMaster";

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";

	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;

	@SystemSetupParameterMethod
	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));

		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<ImportData>();

		final ImportData amB2CMasterImportData = new ImportData();
		amB2CMasterImportData.setProductCatalogName(AM_B2C_MASTER);
		amB2CMasterImportData.setContentCatalogNames(Arrays.asList(AM_B2C));
		amB2CMasterImportData.setStoreNames(Collections.EMPTY_LIST);
		importData.add(amB2CMasterImportData);

		final ImportData amB2CUSImportData = new ImportData();
		amB2CUSImportData.setProductCatalogName(AM_B2C_US);
		amB2CUSImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		amB2CUSImportData.setStoreNames(Arrays.asList(AM_B2C_US));
		importData.add(amB2CUSImportData);

		final ImportData amB2CCAImportData = new ImportData();
		amB2CCAImportData.setProductCatalogName(AM_B2C_CA);
		amB2CCAImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		amB2CCAImportData.setStoreNames(Arrays.asList(AM_B2C_CA));
		importData.add(amB2CCAImportData);

		final ImportData amB2CAUImportData = new ImportData();
		amB2CAUImportData.setProductCatalogName(AM_B2C_AU);
		amB2CAUImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		amB2CAUImportData.setStoreNames(Arrays.asList(AM_B2C_AU));
		importData.add(amB2CAUImportData);

		final ImportData amB2CNZImportData = new ImportData();
		amB2CNZImportData.setProductCatalogName(AM_B2C_NZ);
		amB2CNZImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		amB2CNZImportData.setStoreNames(Arrays.asList(AM_B2C_NZ));
		importData.add(amB2CNZImportData);

		final ImportData amB2CGlobalImportData = new ImportData();
		amB2CGlobalImportData.setProductCatalogName(AM_B2C_GLOBAL);
		amB2CGlobalImportData.setContentCatalogNames(Collections.EMPTY_LIST);
		amB2CGlobalImportData.setStoreNames(Arrays.asList(AM_B2C_GLOBAL));
		importData.add(amB2CGlobalImportData);

		final ImportData amB2CLatAmImportData = new ImportData();
		amB2CLatAmImportData.setProductCatalogName(AM_B2C_LATAM);
		amB2CLatAmImportData.setContentCatalogNames(Collections.emptyList());
		amB2CLatAmImportData.setStoreNames(Arrays.asList(AM_B2C_LATAM));
		importData.add(amB2CLatAmImportData);

		getCoreDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new CoreDataImportedEvent(context, importData));
		importImpexFile(context, "/gallagherb2cinitialdata/import/coredata/common/am-sync.impex");
		importImpexFile(context, "/gallagherb2cinitialdata/import/coredata/common/cronjobs.impex");
		getSampleDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));
	}

	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Required
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Required
	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}
}