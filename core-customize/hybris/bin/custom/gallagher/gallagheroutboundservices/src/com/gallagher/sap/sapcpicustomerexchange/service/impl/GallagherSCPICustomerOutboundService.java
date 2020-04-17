package com.gallagher.sap.sapcpicustomerexchange.service.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static de.hybris.platform.sap.sapcpiadapter.service.SapCpiOutboundService.RESPONSE_MESSAGE;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.sap.sapcpicustomerexchange.service.impl.SapCpiCustomerOutboundService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.gallagher.outboundservices.constants.GallagheroutboundservicesConstants;
import com.gallagher.sap.sapcpiadapter.service.GallagherSCPIOutboundService;


/**
 * Service to provide SCPI operations for customer
 *
 * @author Vikram Bishnoi
 */
public class GallagherSCPICustomerOutboundService extends SapCpiCustomerOutboundService
{

	private static final Logger LOG = LoggerFactory.getLogger(GallagherSCPICustomerOutboundService.class);

	@Resource(name = "modelService")
	private ModelService modelService;


	@Override
	public void sendCustomerData(final CustomerModel customerModel, final String baseStoreUid, final String sessionLanguage,
			final AddressModel addressModel)
	{

		((GallagherSCPIOutboundService) getSapCpiOutboundService()).sendCustomer(customerModel).subscribe(

				// onNext
				responseEntityMap -> {

					if (isSentSuccessfully(responseEntityMap))
					{
						customerModel
								.setSapContactID(getPropertyValue(responseEntityMap, GallagheroutboundservicesConstants.SAP_CONTACT_ID));
						customerModel.setObjectID(getPropertyValue(responseEntityMap, GallagheroutboundservicesConstants.OBJECT_ID));
						customerModel.setSapIsReplicated(true);

						modelService.save(customerModel);

						LOG.info(String.format("The customer [%s] has been sent to the SAP backend through SCPI! %n%s",
								customerModel.getCustomerID(), getPropertyValue(responseEntityMap, RESPONSE_MESSAGE)));

					}
					else
					{

						LOG.error(String.format("The customer [%s] has not been sent to the SAP backend! %n%s",
								customerModel.getCustomerID(), getPropertyValue(responseEntityMap, RESPONSE_MESSAGE)));

					}

				}

				// onError
				, error -> LOG.error(String.format("The customer [%s] has not been sent to the SAP backend through SCPI! %n%s",
						customerModel.getCustomerID(), error.getMessage()), error)

		);

	}

	/**
	 * isSentSuccessfully
	 *
	 * @param responseEntityMap
	 *           ResponseEntity<Map>
	 * @return boolean
	 */
	static boolean isSentSuccessfully(final ResponseEntity<Map> responseEntityMap)
	{
		return responseEntityMap.getStatusCode().is2xxSuccessful();
	}

	/**
	 * getPropertyValue
	 *
	 * @param responseEntityMap
	 *           ResponseEntity<Map>
	 * @param property
	 *           String
	 * @return String
	 */
	static String getPropertyValue(final ResponseEntity<Map> responseEntityMap, final String property)
	{

		final Object next = responseEntityMap.getBody().keySet().iterator().next();
		checkArgument(next != null, String.format("SCPI response entity key set cannot be null for property [%s]!", property));

		final String responseKey = next.toString();
		checkArgument(responseKey != null && !responseKey.isEmpty(),
				String.format("SCPI response property can neither be null nor empty for property [%s]!", property));

		final Object propertyValue = ((HashMap) responseEntityMap.getBody().get(responseKey)).get(property);
		checkArgument(propertyValue != null, String.format("SCPI response property [%s] value cannot be null!", property));

		return propertyValue.toString();

	}

}
