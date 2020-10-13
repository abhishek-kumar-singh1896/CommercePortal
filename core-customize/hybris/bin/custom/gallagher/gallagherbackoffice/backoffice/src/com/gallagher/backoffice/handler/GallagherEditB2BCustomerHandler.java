package com.gallagher.backoffice.handler;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gallagher.c4c.outboundservices.facade.GallagherC4COutboundServiceFacade;
import com.gallagher.core.events.GallagherB2BCustomerUpdateEvent;
import com.gallagher.core.services.GallagherB2BUnitService;
import com.gallagher.keycloak.outboundservices.service.GallagherKeycloakService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaLogicHandler;


/**
 * @author ankituniyal
 *
 *         This handler pushes the customer to keycloak, mindtouch and C4C when the user info is updated from backoffice
 *
 */
public class GallagherEditB2BCustomerHandler extends DefaultEditorAreaLogicHandler
{
	private static final Logger LOG = LoggerFactory.getLogger(GallagherEditB2BCustomerHandler.class);

	private static final String EMAIL_ALREADY_PRESENT = "duplicateHybrisCustomer";
	private static final String INPUT_OBJECT = "$_inputObject";
	private static final String EMAIL_REGEX = "email.regex";

	private GallagherC4COutboundServiceFacade gallagherC4COutboundServiceFacade;
	private Converter<UserModel, CustomerData> customerConverter;
	private GallagherKeycloakService gallagherKeycloakService;
	private NotificationService notificationService;
	private StoreSessionFacade storeSessionFacade;
	private BaseSiteService baseSiteService;
	private SessionService sessionService;
	private EventService eventService;

	@Resource(name = "gallagherB2BUnitService")
	protected GallagherB2BUnitService b2bUnitService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public Object performSave(final WidgetInstanceManager widgetInstanceManager, final Object currentObject)
			throws ObjectSavingException
	{
		if (currentObject instanceof B2BCustomerModel)
		{
			final B2BCustomerModel currentCustomerModel = (B2BCustomerModel) currentObject;
			final CustomerData b2bCustomer = getCustomerConverter().convert(currentCustomerModel);
			b2bCustomer.setKeycloakGUID(currentCustomerModel.getKeycloakGUID());
				final String email = b2bCustomer.getUid();

				LOG.debug("Checking if the customer is already present in keyCloak and C4C");
				final Boolean isEmailValid = isCustomerEmailValid(widgetInstanceManager, email);

				if (BooleanUtils.isFalse(isEmailValid))
				{
					getNotificationService().notifyUser(StringUtils.EMPTY, EMAIL_ALREADY_PRESENT, Level.FAILURE, null);
					throw new ObjectSavingException(EMAIL_ALREADY_PRESENT, new Throwable(EMAIL_ALREADY_PRESENT));
				}

				final Map<String, String> modifiedAttributesMap = getModifiedAttributesMap(currentCustomerModel);
				if (MapUtils.isNotEmpty(modifiedAttributesMap))
				{
					LOG.debug("Updating user profile in keycloak");
					getGallagherKeycloakService().updateKeyCloakUserProfile(b2bCustomer);
				}

				final Context ctx = new DefaultContext();
				ctx.addAttribute(ObjectFacade.CTX_PARAM_SUPPRESS_EVENT, Boolean.TRUE);

				final Map<String, String> modifiedMap = addModifiedUnitsAndAccess(currentCustomerModel, modifiedAttributesMap);

				LOG.debug("Updating user profile in C4C");
				final B2BCustomerModel updatedCustomerModel = pushToC4C(currentCustomerModel, ctx);

				if (MapUtils.isNotEmpty(modifiedMap))
				{
					getEventService().publishEvent(
							initializeEvent(new GallagherB2BCustomerUpdateEvent(), updatedCustomerModel,
									modifiedMap));
				}

				LOG.debug("Updating widget instance manager with updated B2B customer");
				widgetInstanceManager.getModel().setValue(INPUT_OBJECT, currentCustomerModel);

				return updatedCustomerModel;
		}

		LOG.debug("Customer is not B2B, executing OOTB flow ");
		return super.performSave(widgetInstanceManager, currentObject);

	}

	/**
	 * This method returns if any of email, name or b2bUnit is modified
	 *
	 * @param currentCustomerModel
	 * @return
	 */
	private Map<String, String> getModifiedAttributesMap(final B2BCustomerModel currentCustomerModel)
	{
		final Set<String> monitoredAttributes = getMonitoredAttributes();
		final Set<String> attributeSet = currentCustomerModel.getItemModelContext().getDirtyAttributes();

		final Map<String, String> modifiedAttributesMap = new HashMap<>();

		for (final String attribute : attributeSet)
		{
			if (monitoredAttributes.contains(attribute))
			{
				modifiedAttributeMap(currentCustomerModel, attribute, modifiedAttributesMap);
			}
		}
		return modifiedAttributesMap;

	}

	/**
	 * @param modifiedAttributesMap
	 * @param originalCustomerModel
	 * @param attribute
	 * @return
	 */
	private Map<String, String> modifiedAttributeMap(final B2BCustomerModel itemModel,
			final String itemProperty,
			final Map<String, String> modifiedAttributesMap)
	{

		final ItemModelContextImpl context = (ItemModelContextImpl) itemModel.getItemModelContext();
		final ModelValueHistory history = context.getValueHistory();
		final boolean isChanged = !Objects.equals(itemModel.getProperty(itemProperty), history.getOriginalValue(itemProperty));

		if (isChanged)
		{
			LOG.debug("The [{}] attribute [{}], has been updated from [{}] to [{}]!", itemModel.getItemtype(), itemProperty,
					history.getOriginalValue(itemProperty), itemModel.getProperty(itemProperty));
			modifiedAttributesMap.put(itemProperty, itemModel.getProperty(itemProperty));
		}
		return modifiedAttributesMap;

	}


	/**
	 * @return
	 */
	private Set<String> getMonitoredAttributes()
	{

		final Set<String> monitoredAttributes = new HashSet<>();
		monitoredAttributes.add(B2BCustomerModel.UID);
		monitoredAttributes.add(B2BCustomerModel.NAME);
		return monitoredAttributes;

	}


	/**
	 * This method checks, if the email is changed from commerce and already present in keycloak and C4C
	 *
	 * @param widgetInstanceManager
	 * @param uid
	 * @return
	 * @throws ObjectSavingException
	 */
	private Boolean isCustomerEmailValid(final WidgetInstanceManager widgetInstanceManager, final String email)
			throws ObjectSavingException
	{
		Boolean isCustomerEmailValid = Boolean.TRUE;

		if (StringUtils.isEmpty(email) || StringUtils.length(email) > 255 || !validateEmailAddress(email))
		{
			throw new ObjectSavingException(EMAIL_ALREADY_PRESENT, new Throwable(EMAIL_ALREADY_PRESENT));
		}

		else if (BooleanUtils.isFalse(widgetInstanceManager.getTitle().contains(email)))
		{
			final String existingKeyCloakId = getGallagherKeycloakService().getKeycloakUserFromEmail(email);

			if (StringUtils.isNotBlank(existingKeyCloakId))
			{
				LOG.debug("Customer with same email [{}] already exists in keycloak::", email);
				isCustomerEmailValid = Boolean.FALSE;
			}

			else if (CollectionUtils
					.isNotEmpty(getGallagherC4COutboundServiceFacade().getCustomerInfoFromC4C(email, StringUtils.EMPTY)))
			{
				LOG.debug("Customer with same email [{}] already exists in C4C::", email);
				isCustomerEmailValid = Boolean.FALSE;
			}
		}

		return isCustomerEmailValid;
	}

	protected boolean validateEmailAddress(final String email)
	{
		final Matcher matcher = Pattern.compile(configurationService.getConfiguration().getString(EMAIL_REGEX)).matcher(email);
		return matcher.matches();
	}

	/**
	 * @param currentCustomerModel
	 * @param ctx
	 * @return
	 */
	private B2BCustomerModel pushToC4C(final B2BCustomerModel b2bCustomer, final Context ctx)
	{
		final B2BUnitModel defaultB2BUnit = b2bCustomer.getDefaultB2BUnit();
		final Pair<BaseSiteModel, BaseStoreModel> baseSiteAndStore = getB2bUnitService().getBaseSiteAndStoreForUnit(defaultB2BUnit);

		final LanguageModel language = getLanguage(baseSiteAndStore.getRight(), b2bCustomer.getSessionLanguage());
		final CurrencyModel currency = getCurrency(baseSiteAndStore.getRight(), b2bCustomer.getSessionCurrency());

		b2bCustomer.setSessionCurrency(currency);
		b2bCustomer.setSessionLanguage(language);

		getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				getBaseSiteService().setCurrentBaseSite(baseSiteAndStore.getLeft(), Boolean.FALSE);
				getStoreSessionFacade().setCurrentLanguage(language.getIsocode());
				getStoreSessionFacade().setCurrentCurrency(currency.getIsocode());
				try
				{
					getObjectFacade().save(b2bCustomer, ctx);
				}
				catch (final ObjectSavingException e)
				{
					LOG.error("Error while saving B2B customer :: [{}] ", e.getMessage());
				}
			}
		});

		return b2bCustomer;
	}

	/**
	 * Returns the currency for this customer. If user selected currency is not in allowed list of currencies then use
	 * the default currency.
	 *
	 * @param baseStore
	 *           base store
	 * @param sessionCurrency
	 *           session currency selected by user
	 * @return currency to be set
	 */
	private CurrencyModel getCurrency(final BaseStoreModel baseStore, final CurrencyModel sessionCurrency)
	{
		CurrencyModel currency;
		if (sessionCurrency == null)
		{
			currency = baseStore.getDefaultCurrency();
		}
		else
		{
			currency = baseStore.getCurrencies().contains(sessionCurrency) ? sessionCurrency : baseStore.getDefaultCurrency();
		}
		return currency;
	}

	/**
	 * Returns the language for this customer. If user selected language is not in allowed list of languages then use the
	 * default language.
	 *
	 * @param baseStore
	 *           base store
	 * @param sessionLanguage
	 *           session language selected by user
	 * @return language to be set
	 */
	private LanguageModel getLanguage(final BaseStoreModel baseStore, final LanguageModel sessionLanguage)
	{
		LanguageModel language;
		if (sessionLanguage == null)
		{
			language = baseStore.getDefaultLanguage();
		}
		else
		{
			language = baseStore.getLanguages().contains(sessionLanguage) ? sessionLanguage : baseStore.getDefaultLanguage();
		}
		return language;
	}

	/**
	 * initializes an {@link GallagherB2BCustomerUpdateEvent}
	 *
	 * @param event
	 * @param updatedCustomerModel
	 * @param modifiedAttributesMap
	 * @param modifiedAttributesMap
	 * @param customerModel
	 * @return the event
	 */
	private GallagherB2BCustomerUpdateEvent initializeEvent(final GallagherB2BCustomerUpdateEvent event,
			final B2BCustomerModel originalCustomerModel,
			final Map<String, String> modifiedAttributesMap)
	{
		final B2BUnitModel defaultB2BUnit = originalCustomerModel.getDefaultB2BUnit();
		final Pair<BaseSiteModel, BaseStoreModel> baseSite = getB2bUnitService().getBaseSiteAndStoreForUnit((defaultB2BUnit));

		event.setBaseStore(baseSite.getRight());
		event.setSite(baseSite.getLeft());
		event.setCustomer(originalCustomerModel);
		event.setLanguage(originalCustomerModel.getSessionLanguage());
		event.setCurrency(originalCustomerModel.getSessionCurrency());
		event.setModifiedAttributesMap(modifiedAttributesMap);

		return event;
	}



	/**
	 * @param originalCustomerModel
	 * @param updatedCustomerModel
	 * @param modifiedAttributesMap
	 * @return
	 */
	private Map<String, String> addModifiedUnitsAndAccess(final B2BCustomerModel updatedCustomerModel,
			final Map<String, String> modifiedAttributesMap)
	{

		final ItemModelContextImpl context = (ItemModelContextImpl) updatedCustomerModel.getItemModelContext();
		final ModelValueHistory history = context.getValueHistory();

		final Collection origialGroups = (Collection) history.getOriginalValue(B2BCustomerModel.GROUPS);

		final Collection oldGroups = new HashSet(origialGroups);
		final Collection deletedGroups = new HashSet(origialGroups);
		final Collection newGroups = new HashSet(updatedCustomerModel.getGroups());

		deletedGroups.removeAll(newGroups);
		newGroups.removeAll(oldGroups);

		final List<String> addedUnits = getUnits(newGroups);
		final List<String> deletedUnits = getUnits(deletedGroups);

		if (CollectionUtils.isNotEmpty(deletedUnits))
		{
			modifiedAttributesMap.put("b2bUnitRemoved", String.join(",", deletedUnits));
		}
		if (CollectionUtils.isNotEmpty(addedUnits))
		{
			modifiedAttributesMap.put("b2bUnitAdded", String.join(",", addedUnits));
		}

		final boolean isChanged = !Objects.equals(updatedCustomerModel.getProperty(B2BCustomerModel.LOGINDISABLED),
				history.getOriginalValue(B2BCustomerModel.LOGINDISABLED));

		if (isChanged)
		{
			final String access = updatedCustomerModel.isLoginDisabled() ? "Disabled" : "Enabled";
			modifiedAttributesMap.put(B2BCustomerModel.LOGINDISABLED, access);
		}

		return modifiedAttributesMap;
	}
	/**
	 * Returns the uids of all B2B Units
	 *
	 * @return b2bunit Ids
	 */
	private List<String> getUnits(final Collection<PrincipalGroupModel> groups)
	{
		final List<String> units = new ArrayList<>();
		groups.forEach(group -> {
			if (group instanceof B2BUnitModel)
			{
				units.add(group.getLocName());
			}
		});
		return units;
	}

	/**
	 * @return the gallagherKeycloakService
	 */
	public GallagherKeycloakService getGallagherKeycloakService()
	{
		return gallagherKeycloakService;
	}

	/**
	 * @param gallagherKeycloakService
	 *           the gallagherKeycloakService to set
	 */
	public void setGallagherKeycloakService(final GallagherKeycloakService gallagherKeycloakService)
	{
		this.gallagherKeycloakService = gallagherKeycloakService;
	}

	/**
	 * @return the customerConverter
	 */
	public Converter<UserModel, CustomerData> getCustomerConverter()
	{
		return customerConverter;
	}

	/**
	 * @param customerConverter
	 *           the customerConverter to set
	 */
	public void setCustomerConverter(final Converter<UserModel, CustomerData> customerConverter)
	{
		this.customerConverter = customerConverter;
	}

	/**
	 * @return the notificationService
	 */
	public NotificationService getNotificationService()
	{
		return notificationService;
	}

	/**
	 * @param notificationService
	 *           the notificationService to set
	 */
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the storeSessionFacade
	 */
	public StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	/**
	 * @param storeSessionFacade
	 *           the storeSessionFacade to set
	 */
	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

	/**
	 * @return the gallagherC4COutboundServiceFacade
	 */
	public GallagherC4COutboundServiceFacade getGallagherC4COutboundServiceFacade()
	{
		return gallagherC4COutboundServiceFacade;
	}

	/**
	 * @param gallagherC4COutboundServiceFacade
	 *           the gallagherC4COutboundServiceFacade to set
	 */
	public void setGallagherC4COutboundServiceFacade(final GallagherC4COutboundServiceFacade gallagherC4COutboundServiceFacade)
	{
		this.gallagherC4COutboundServiceFacade = gallagherC4COutboundServiceFacade;
	}

	/**
	 * @return the eventService
	 */
	public EventService getEventService()
	{
		return eventService;
	}

	/**
	 * @param eventService
	 *           the eventService to set
	 */
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	/**
	 * @return the b2bUnitService
	 */
	public GallagherB2BUnitService getB2bUnitService()
	{
		return b2bUnitService;
	}

	/**
	 * @param b2bUnitService
	 *           the b2bUnitService to set
	 */
	public void setB2bUnitService(final GallagherB2BUnitService b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
