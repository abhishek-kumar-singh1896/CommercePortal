package com.braintree.configuration.service;

import com.braintreegateway.util.BraintreeCrypto;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.braintreegateway.Environment;

import javax.annotation.Resource;

import static com.braintree.constants.BraintreeConstants.*;


public class BrainTreeConfigService
{
	private final static Logger LOG = Logger.getLogger(BrainTreeConfigService.class);

	private BaseSiteService baseSiteService;
	private CommonI18NService commonI18NService;
	private ConfigurationService configurationService;
	private UserService userService;


    @Resource(name = "sessionService")
    private SessionService sessionService;

	public Environment getEnvironmentType()
	{
		if (ENV_TYPE_MAP.containsKey(getConfiguration().getString(BRAINTREE_ENVIRONMENT)))
		{
			return ENV_TYPE_MAP.get(getConfiguration().getString(BRAINTREE_ENVIRONMENT));
		}
		LOG.error("Configuration environment property name is incorrect! Use environment: " + Environment.DEVELOPMENT);
		return Environment.DEVELOPMENT;
	}

	/**
	 * @return Configuration
	 */
	private Configuration getConfiguration()
	{
		return getConfigurationService().getConfiguration();
	}

	public Boolean getSettlementConfigParameter()
	{
		return Boolean.valueOf(getConfiguration().getBoolean(BRAINTREE_SUBMIT_FOR_SETTLEMENT, false));
	}

	public String getEnvironmentTypeName()
	{
		return getEnvironmentType().getEnvironmentName();
	}

	public Boolean get3dSecureConfiguration()
	{
		return Boolean.valueOf(getConfiguration().getBoolean(BRAINTREE_3D_SECURE, false));
	}

	public Boolean getAdvancedFraudTools()
	{
		return Boolean.valueOf(getConfiguration().getBoolean(BRAINTREE_ADVANCED_FRAUD_TOOLS_ENABLED, true));
	}

	public Boolean getSingleUse()
	{
		return Boolean.valueOf(getConfiguration().getBoolean(SINGLE_USE_PARAMETER, true));
	}

	private final Map<String, Environment> ENV_TYPE_MAP = new HashMap()
	{
		{
			put(ENVIRONMENT_SANDBOX, Environment.SANDBOX);
			put(ENVIRONMENT_PRODUCTION, Environment.PRODUCTION);
		}
	};

	public Boolean getHostedFieldEnabled()
	{
		return Boolean.valueOf(getConfiguration().getBoolean(HOSTED_FIELDS_ENABLED, true));
	}

	public Boolean getPayPalExpressEnabled()
	{
		return Boolean.valueOf(getConfiguration().getBoolean(PAYPAL_EXPRESS_ENABLED, true));
	}

	public Boolean getPayPalStandardEnabled()
	{
		return Boolean.valueOf(getConfiguration().getBoolean(PAYPAL_STANDARD_ENABLED, true));
	}

	public Boolean getIsSkip3dSecureLiabilityResult()
	{
		return Boolean.valueOf(getConfiguration().getBoolean(IS_SKIP_3D_SECURE_LIABILITY_RESULT, false));
	}

	public String getCreditCardStatementName()
	{
		return getConfiguration().getString(BRAINTREE_CREDIT_CARD_STATEMENT_NAME);
	}

	public String getBrainTreeChannel()
	{
		return BraintreeCrypto.decrypt(getConfiguration().getString(BRAINTREE_KEY), getConfiguration().getString(BRAINTREE_CHANNEL_NAME));
	}

	public String getMerchantAccountIdForCurrentSiteAndCurrency()
	{
	    return getMerchantAccountIdByCurrentSiteNameAndCurrency(getBaseSiteService().getCurrentBaseSite().getUid(),
				getCommonI18NService().getCurrentCurrency().getIsocode().toLowerCase());
	}

	public String getMerchantAccountIdForCurrentSiteAndCurrencyIsoCode(final String currencyIsoCode)
	{
		return getMerchantAccountIdByCurrentSiteNameAndCurrency(getBaseSiteService().getCurrentBaseSite().getUid(),
				currencyIsoCode.toLowerCase());
	}

	public String getMerchantAccountIdForSiteAndCurrencyIsoCode(final String baseSiteUid, final String currencyIsoCode)
	{
		return getMerchantAccountIdByCurrentSiteNameAndCurrency(baseSiteUid, currencyIsoCode.toLowerCase());
	}

	public String getAcceptedPaymentMethods()
	{
		return getConfiguration().getString(BRAINTREE_ACCEPTED_PAYMENT_METHODS);
	}

	public String getStoreInVault()
	{
		return getConfiguration().getString(STORE_IN_VAULT);
	}
	
	public String getStoreInVaultForCurrentUser()
	{
		if (userService.isAnonymousUser(userService.getCurrentUser()))
		{
			return Boolean.FALSE.toString();
		}
		return getStoreInVault();
	}

	public Map<String, String> getAcceptedPaymentMethodImages()
	{
		final Map<String, String> acceptedPaymentMethodImages = new HashMap<>();
		final String acceptedPaymentMethods = getAcceptedPaymentMethods();
		if (StringUtils.isNotBlank(acceptedPaymentMethods))
		{
			final String paymentMethods = StringUtils.deleteWhitespace(acceptedPaymentMethods);

			final List<String> paymentMethodList = Arrays
					.asList(StringUtils.split(paymentMethods, BRAINTREE_ACCEPTED_PAYMENT_METHODS_DELIMETER));

			for (final String paymentMethod : paymentMethodList)
			{
				final String imageLink = getConfiguration().getString(BRAINTREE_IMAGES_PREFIX + paymentMethod);
				if (StringUtils.isNotBlank(imageLink))
				{
					acceptedPaymentMethodImages.put(paymentMethod, imageLink);
				}
			}
		}

		return acceptedPaymentMethodImages;
	}

	public String getMerchantAccountIdByCurrentSiteNameAndCurrency(final String currentSiteName, final String currency)
	{
		final String merchantAccountId = getConfiguration()
				.getString(BRAINTREE_MERCHANT_ACCOUNT_PREFIX + currentSiteName + CONFIGURATION_PROPERTY_DELIMETER + currency);
		return merchantAccountId;
	}

	public boolean getVerifyCard()
	{
		return getConfiguration().getBoolean(VERIFY_CARD);
	}

	public boolean getVerifyCardOnVaulting()
	{
		return getConfiguration().getBoolean(VERIFY_CARD_ON_VAULTING);
	}

	public boolean getMultiCaptureEnabled()
	{
		return getConfiguration().getBoolean(MULTICAPTURE_ENABLED,true);
	}

	public boolean getB2CFlowEnabled()
	{
		return getConfiguration().getBoolean(B2C_FLOW,true);
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
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
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

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	public String getIntent(){
		return getConfiguration().getString(BRAINTREE_PAYPAL_INTENT);
	}

	public String getBrainTreeLocale()
	{
		return getConfiguration().getString(BRAINTREE_LOCALE);
	}

	public Integer getHybrisBuildApiVersion()
	{
		String apiVersion = getConfiguration().getString(HYBRIS_BUILD_API_VERSION);
        if (StringUtils.isNotEmpty(apiVersion))
		{
			String[] version = apiVersion.split("\\.");
			if (version.length > 0 && StringUtils.isNumeric(version[0]))
			{
				return Integer.valueOf(version[0]);
			}
		}
		return null;
	}

	public boolean getApplePayEnabled()
	{
		return getConfiguration().getBoolean(BRAINTREE_APPLE_PAY_ENABLE, false);
	}

	public Object getBillingAgreementDescription() {
		return getConfiguration().getString(BILLING_AGREEMENT_DESCRIPTION_KEY, "");
	}
}
