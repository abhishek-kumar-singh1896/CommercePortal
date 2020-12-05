import com.gallagher.core.model.GallagherRegionRestrictionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

def catalog = catalogService.getCatalogForId("amB2CLatAmProductCatalog");
catalog.setId("amB2CCLProductCatalog");
modelService.save(catalog);


def job =cronJobService.getJob("sync amB2CLatAmProductCatalog:Staged->Online");
job.setCode("sync amB2CCLProductCatalog:Staged->Online");
modelService.save(job);
 job =cronJobService.getJob("sync amB2CMasterProductCatalog:Staged->amB2CLatAmProductCatalog:Staged");
job.setCode("sync amB2CMasterProductCatalog:Staged->amB2CCLProductCatalog:Staged");
modelService.save(job);


def store = baseStoreService.getBaseStoreForUid("amB2CLatAm");
store.setUid("amB2CCL");
modelService.save(store);

def site = baseSiteService.getBaseSiteForUID("amB2CLatAm");
site.setUid("amB2CCL");
modelService.save(site);


GallagherRegionRestrictionModel restriction = new GallagherRegionRestrictionModel();
restriction.setUid("GallagherLATAMRegionRestriction");


def restrictions = flexibleSearchService.getModelsByExample(restriction);

restrictions.each{
it.setUid("GallagherCLRegionRestriction");
modelService.save(it);
}
								
setupSolrIndexerService.createSolrIndexerCronJobs("amB2CCLIndex");


FlexibleSearchQuery searchQuery = new FlexibleSearchQuery("select {pk} from {Product} where {eligibleForLatAm} = 1");
searchQuery.setCount(2000);
def search = flexibleSearchService.search(searchQuery);

search.result.each {
it.setEligibleForChile(true);
modelService.save(it);
}