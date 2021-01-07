/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sapcustomerb2b.outbound;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.user.UserService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gallagher.sap.sapcpicustomerexchangeb2b.outbound.services.GallagherSCPIB2BCustomerOutboundService;
import com.sap.hybris.sapcustomerb2b.outbound.DefaultB2BCustomerInterceptor;


public class GallagherB2BCustomerInterceptor extends DefaultB2BCustomerInterceptor
{

	private static final Logger LOGGER = Logger.getLogger(GallagherB2BCustomerInterceptor.class.getName());
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZone(ZoneId.of("UTC"));

	@Resource(name = "userService")
	private UserService userService;

	@Override
	public void onValidate(final B2BCustomerModel customerModel, final InterceptorContext ctx) throws InterceptorException
	{

		if (!getB2bCustomerExportService().isB2BCustomerReplicationEnabled())
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("'Replicate B2B Customers' flag in 'SAP Base Store Configuration' is set to 'false'.");
				LOGGER.debug("B2B Customer with customer ID " + customerModel.getCustomerID() + " was not sent to Data Hub.");
			}
			return;
		}

		/*
		 * ********** Obsolete Comment **************** This interceptor is used to replicate changes to existing B2B
		 * customers. New B2B customers are handled and replicated to Data Hub by B2BCustomerRegistrationEventListener.
		 * Therefore check ctx.isNew(customerModel).
		 *
		 * This interceptor is also called when B2B customers are received from Data Hub and Data Hub always sets the
		 * sapIsReplicated flag when sending B2B customers. But sapIsReplicated can't be set from the storefront.
		 * Therefore, to avoid replicating B2B customers that have just been received from Data Hub back to Data Hub,
		 * check ctx.isModified(customerModel, "sapIsReplicated"). ********** Obsolete Comment
		 ****************/

		/*
		 * The previous comment is obsolete and left here for reference. This interceptor has been updated to handle new
		 * B2B customers.
		 */

		if (isB2BCustomerReplicatedFromSapBackend(customerModel, ctx))
		{
			LOGGER.debug("The B2B customer has been replicated form SAP backend and will not be sent again to SAP backend!");
			return;
		}

		if (isNewB2BCustomer(customerModel, ctx))
		{
			LOGGER.debug("The new B2B customer will be sent to SAP backend!");
			sendB2BCustomerToSapBackend(customerModel);
			return;
		}

		if (getCustomerAddressReplicationUtilityService().isCustomerReplicationRequired(customerModel, getMonitoredAttributes(),
				ctx))
		{
			LOGGER.info("Interceptor called!");
			//getting the groups from the old version
			final ItemModelContextImpl context = (ItemModelContextImpl) customerModel.getItemModelContext();
			final ModelValueHistory history = context.getValueHistory();
			final boolean isChanged = !Objects.equals(customerModel.getProperty(B2BCustomerModel.UID),
					history.getOriginalValue(B2BCustomerModel.UID));
			final String customerUid = isChanged ? (String) history.getOriginalValue(B2BCustomerModel.UID) : customerModel.getUid();
			if (isChanged)
			{
				customerModel.setEmailID(customerModel.getUid().substring(4, customerModel.getUid().length()));
			}
			final B2BCustomerModel userDBCopy = userService.getUserForUID(customerUid, B2BCustomerModel.class);


			//getting the groups from the new version

			final Collection oldGroups = new HashSet(userDBCopy.getGroups());
			final Collection deletedGroups = new HashSet(userDBCopy.getGroups());
			final Collection newGroups = new HashSet(customerModel.getGroups());

			deletedGroups.removeAll(newGroups);
			newGroups.removeAll(oldGroups);

			final List<String> addedUnits = getUnits(newGroups);
			final List<String> deletedUnits = getUnits(deletedGroups);

			LOGGER.info("Interceptor finished!");
			final String sessionLanguage = getStoreSessionFacade().getCurrentLanguage() != null
					? getStoreSessionFacade().getCurrentLanguage().getIsocode()
					: "en";

			LOGGER.debug("The modified B2B customer will be sent to SAP backend!");
			((GallagherSCPIB2BCustomerOutboundService) getB2bCustomerExportService()).prepareAndSend(customerModel, sessionLanguage,
					addedUnits, deletedUnits);
		}

	}

	/**
	 *
	 */
	@Override
	protected void sendB2BCustomerToSapBackend(final B2BCustomerModel b2bCustomer)
	{

		if (b2bCustomer.getCustomerID() == null || b2bCustomer.getCustomerID().isEmpty())
		{
			b2bCustomer.setSapReplicationInfo(String.format("Sent to Data Hub at %s", ZonedDateTime.now().format(DTF)));
			b2bCustomer.setCustomerID((String) getSapContactIdGenerator().generate());
			b2bCustomer.setLoginDisabled(false);
			final String sessionLanguage = getStoreSessionFacade().getCurrentLanguage() != null
					? getStoreSessionFacade().getCurrentLanguage().getIsocode()
					: "en";
			LOGGER.debug("The new B2B customer will be sent to SAP backend!");
			((GallagherSCPIB2BCustomerOutboundService) getB2bCustomerExportService()).prepareAndSend(b2bCustomer, sessionLanguage,
					getUnits(b2bCustomer.getGroups()), null);
		}

	}

	/**
	 * Returns the uids of all B2B Units
	 *
	 * @return b2bunit Ids
	 */
	private List<String> getUnits(final Collection<PrincipalGroupModel> groups)
	{
		final List<String> units = new ArrayList<String>();
		groups.forEach(group -> {
			if (group instanceof B2BUnitModel)
			{
				units.add(group.getUid());
			}
		});
		return units;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}