/**
 *
 */
package com.gallagher.facades.search.converters.poulator;

import de.hybris.platform.acceleratorfacades.order.data.PriceRangeData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.product.data.DiscountData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * @author gauravkamboj
 *
 */
public class GallagherSearchResultProductPopulator extends SearchResultVariantProductPopulator
{

	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private UserService userService;

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		super.populate(source, target);
		final String marketingDescription = this.<String> getValue(source, "marketingDescription");
		if (!StringUtils.isEmpty(marketingDescription))
		{
			target.setName(marketingDescription);
		}
		if (!StringUtils.isEmpty(this.<String> getValue(source, "promoSticker")))
		{
			target.setPromoSticker((this.<String> getValue(source, "promoSticker")).toLowerCase());
		}

		if (CollectionUtils.isNotEmpty(this.<List<String>> getValue(source, "animalCompatibility")))
		{
			final List<String> animalList = this.<List<String>> getValue(source, "animalCompatibility");
			animalList.replaceAll(String::toLowerCase);
			target.setAnimalCompatibility(animalList);
		}
		if (!StringUtils.isEmpty(this.<String> getValue(source, "plpProductDescription")))
		{
			target.setPlpProductDescription(this.<String> getValue(source, "plpProductDescription"));
		}
		target.setPartNumber(this.<String> getValue(source, "partNumber"));
		target.setCategoryPaths(this.<List<String>> getValue(source, "categoryPath"));
		target.setModifiedTime(this.<Date> getValue(source, "modifiedtime"));

		if (CollectionUtils.isNotEmpty(source.getVariants()))
		{
			setPriceRange(source, target);
		}

		populateDiscount(source, target);
	}

	/**
	 * @param source
	 * @param target
	 */
	private void populateDiscount(final SearchResultValueData source, final ProductData target)
	{

		final String totalDiscountsString = this.<String> getValue(source, "totalDiscounts");
		final BaseSiteModel cmssite = getBaseSiteService().getCurrentBaseSite();
		final UserModel user = userService.getCurrentUser();
		if (cmssite != null && cmssite.getUid().contains("B2B") && userService.isAnonymousUser(user)
				|| BooleanUtils.isFalse(isB2BUnitTansactional()))
		{
			target.setTotalDiscounts(null);
		}
		else
		{
			if (StringUtils.isNotBlank(totalDiscountsString))
			{
				target.setTotalDiscounts(Collections.singletonList(createTotalDiscounts(totalDiscountsString)));
			}
		}
	}

	private boolean isB2BUnitTansactional()
	{

		final BaseSiteModel cmssite = getBaseSiteService().getCurrentBaseSite();
		boolean b2bUnitTransactional = true;

		if (cmssite != null && cmssite.getUid().contains("B2B"))
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			if (currentCustomer instanceof B2BCustomerModel && null != ((B2BCustomerModel) currentCustomer).getDefaultB2BUnit())
			{
				b2bUnitTransactional = Boolean.TRUE
						.equals(((B2BCustomerModel) currentCustomer).getDefaultB2BUnit().getTransactional());

			}
			else
			{
				return false;
			}
		}
		return b2bUnitTransactional;
	}

	/**
	 * @param totalDiscountsString
	 * @return
	 */
	protected DiscountData createTotalDiscounts(final String totalDiscountsString)
	{
		final DiscountData discountData = createDiscountData();
		discountData.setDiscountString(totalDiscountsString);
		discountData.setValue(totalDiscountsString);
		return discountData;
	}

	protected DiscountData createDiscountData()
	{
		return new DiscountData();
	}

	@Override
	protected void populatePrices(final SearchResultValueData source, final ProductData target)
	{
		final BaseSiteModel cmssite = getBaseSiteService().getCurrentBaseSite();
		final UserModel user = userService.getCurrentUser();
		if (cmssite != null && cmssite.getUid().contains("B2B") && userService.isAnonymousUser(user)
				|| BooleanUtils.isFalse(isB2BUnitTansactional()))
		{
			target.setVolumePricesFlag(null);
			target.setPrice(null);
		}
		else
		{
			super.populatePrices(source, target);
		}

	}

	@Override
	protected void setPriceRange(final SearchResultValueData source, final ProductData target)
	{
		final BaseSiteModel cmssite = getBaseSiteService().getCurrentBaseSite();
		if (cmssite != null && cmssite.getUid().contains("B2B"))
		{
			final UserModel user = userService.getCurrentUser();
			if (userService.isAnonymousUser(user) || BooleanUtils.isFalse(isB2BUnitTansactional()))
			{
				target.setPriceRange(null);
			}
			else
			{
				final PriceRangeData priceRange = new PriceRangeData();
				final String priceRangeValue = (String) source.getValues().get(PRICE_RANGE);

				if (StringUtils.isNotEmpty(priceRangeValue))
				{
					final CurrencyModel currency = getCommonI18NService().getCurrentCurrency();
					final PriceData priceData = getPriceDataFactory().create(PriceDataType.FROM,
							BigDecimal.valueOf(Double.valueOf(priceRangeValue)), currency);

					priceRange.setMaxPrice(priceData);
					priceRange.setMinPrice(priceData);

					target.setPriceRange(priceRange);
				}
			}
		}
		else
		{
			super.setPriceRange(source, target);
		}
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

}
