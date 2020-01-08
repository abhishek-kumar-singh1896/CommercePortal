/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.braintree.cscockpit.widgets.renderers.impl.customer;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemAppender;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.braintree.command.result.BrainTreeAddressResult;
import com.braintree.converters.BraintreeCustomerDetailsConverter;
import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.renderers.utils.DefaultLocaleUtils;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.model.BraintreeCustomerDetailsModel;
import com.braintreegateway.Customer;


public abstract class AbstractCountryRegionSelectWidgetRenderer<T extends Widget> extends AbstractCsWidgetRenderer<T>
{
	private CommonI18NService commonI18NService;
	private PopupWidgetHelper popupWidgetHelper;
	private CsBrainTreeFacade csBrainTreeFacade;
	private ItemAppender<TypedObject> itemAppender;
	private BraintreeCustomerDetailsConverter customerDetailsPopulator;

	protected InternalControls createCountryAndRegionControls(final T widget, final Div content, final String selectedCountryIso,
			final String stateProvinceRegion)
	{
		final List<CountryModel> countries = getCommonI18NService().getAllCountries();

		final Div customRegionBox = new Div();
		customRegionBox.setParent(content);
		final Listbox countrySelectField = UIElementUtils.createCountriesListBox(widget, content, "country", countries,
				selectedCountryIso);

		final Div regionHolder = createDiv(content);
		final Label fieldLabel = new Label(LabelUtils.getLabel(widget, "stateProvinceRegion", new Object[0]));
		fieldLabel.setClass("btStateRegionLabel");
		regionHolder.appendChild(fieldLabel);
		final Textbox stateProvinceRegionField = new Textbox();
		stateProvinceRegionField.setClass("btProvinceRegionListBox");
		regionHolder.appendChild(stateProvinceRegionField);
		final Listbox regionSelectField = new Listbox();
		regionSelectField.setClass("btStateRegionListBox");
		regionHolder.appendChild(regionSelectField);

		final CountryModel selectedCountryModel = getSelectedCountryModel(countries, selectedCountryIso);
		Collection<RegionModel> regions = Collections.EMPTY_LIST;
		if (null != selectedCountryModel)
		{
			regions = selectedCountryModel.getRegions();
		}
		initRegionsBox(widget, stateProvinceRegionField, regionSelectField, regions, stateProvinceRegion);
		countrySelectField.addEventListener(
				Events.ON_SELECT,
				createCountrySelectEventListener(widget, countrySelectField, regionSelectField, stateProvinceRegionField,
						stateProvinceRegion));


		return new InternalControls.Builder().stateProvinceRegionField(stateProvinceRegionField)
				.countrySelectField(countrySelectField).regionSelectField(regionSelectField).bulid();
	}

	private EventListener createCountrySelectEventListener(final Widget widget, final Listbox countrySelectField,
			final Listbox regionSelectField, final Textbox stateProvinceRegionField, final String selected)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				handleCountrySelectEvent(widget, countrySelectField, regionSelectField, stateProvinceRegionField, selected);
			}
		};
	}

	private void handleCountrySelectEvent(final Widget widget, final Listbox countrySelectField, final Listbox regionSelectField,
			final Textbox stateProvinceRegionField, final String selected)
	{
		String selectedCountryIdentifier = (String) countrySelectField.getSelectedItem().getValue();
		if (StringUtils.isNotBlank(selectedCountryIdentifier))
		{
			final CountryModel selectedCountry = getCommonI18NService().getCountry(selectedCountryIdentifier);
			if (null != selectedCountry)
			{
				final Collection<RegionModel> regions = selectedCountry.getRegions();
				updateRegionsListBox(widget, regionSelectField, regions, selected);
				stateProvinceRegionField.setVisible(!regionSelectField.isVisible());
			}
		}
	}

	private boolean initRegionsBox(final Widget widget, final Textbox stateProvinceRegionField, final Listbox listbox,
			final Collection<RegionModel> regions, final String selected)
	{
		final Locale userLocale = JaloSession.getCurrentSession().getSessionContext().getLocale();

		listbox.setMold("select");
		if (null != regions && !regions.isEmpty())
		{
			final List<RegionModel> sortedRegions = new ArrayList(regions);
			Collections.sort(sortedRegions, new LocaleRegionComparator(userLocale));

			listbox.appendItem(LabelUtils.getLabel(widget, "empty.region", new Object[0]), " ").setSelected(true);
			initSelectControl(listbox, selected, userLocale, sortedRegions);
			stateProvinceRegionField.setVisible(false);
			listbox.setVisible(true);
		}
		else
		{
			stateProvinceRegionField.setValue(selected);
			listbox.setVisible(false);
			stateProvinceRegionField.setVisible(true);
		}
		return true;
	}

	private void initSelectControl(final Listbox listbox, final String selected, final Locale userLocale,
			final List<RegionModel> sortedRegions)
	{
		for (final RegionModel region : sortedRegions)
		{
			final String regionName = DefaultLocaleUtils.getRegionName(region, userLocale);
			final String regionIso = region.getIsocodeShort();
			final Listitem item = listbox.appendItem(regionName, regionIso);
			if (regionIso.equals(selected) || (null != regionName && regionName.equals(selected)))
			{
				item.setSelected(true);
			}
		}
	}

	private boolean updateRegionsListBox(final Widget widget, final Listbox listbox, final Collection<RegionModel> regions,
			final String selected)
	{
		final Locale userLocale = JaloSession.getCurrentSession().getSessionContext().getLocale();
		clearListboxItems(listbox);

		if (null != regions)
		{
			final List<RegionModel> sortedRegions = new ArrayList(regions);
			Collections.sort(sortedRegions, new LocaleRegionComparator(userLocale));

			listbox.appendItem(LabelUtils.getLabel(widget, "empty.region", new Object[0]), " ").setSelected(true);
			initSelectControl(listbox, selected, userLocale, sortedRegions);
		}
		listbox.setVisible((null != regions && !regions.isEmpty()));
		return true;
	}

	private void clearListboxItems(final Listbox listbox)
	{
		listbox.getItems().clear();
	}

	private CountryModel getSelectedCountryModel(final Collection<CountryModel> countries, final String selectedCountryIso)
	{
		for (final CountryModel country : countries)
		{
			if (country.getIsocode().equals(selectedCountryIso))
			{
				return country;
			}
		}
		return null;
	}

	protected Div createDiv(final Div content)
	{
		final Div cloneFieldsBox = new Div();
		cloneFieldsBox.setParent(content);
		cloneFieldsBox.setClass("btRegionDiv");
		return cloneFieldsBox;
	}

	protected Textbox createTextField(final Widget widget, final Div parent, final String label, final String value)
	{
		final Label fieldLabel = new Label(LabelUtils.getLabel(widget, label, new Object[0]));
		fieldLabel.setClass("btRegionTextFieldLabel");
		final Textbox fieldTextBox = new Textbox(value);
		parent.appendChild(fieldLabel);
		parent.appendChild(fieldTextBox);
		fieldTextBox.setClass("btRegionTextFieldBox");
		return fieldTextBox;
	}

	protected void processCustomerRendering(final BrainTreeAddressResult result)
	{
		getPopupWidgetHelper().dismissCurrentPopup();
		final Customer customerItem = getCsBrainTreeFacade().findCustomer(result.getAddress().getCustomerId());
		//		final Customer customerItem = result.getCustomer();
		final BraintreeCustomerDetailsModel customer = convertCustomer(customerItem);
		final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(customer);
		getItemAppender().add(itemTypedObject, 1l);
	}

	private BraintreeCustomerDetailsModel convertCustomer(final Customer customerItem)
	{
		if (customerItem != null)
		{
			return getCustomerDetailsPopulator().convert(customerItem);
		}
		return null;
	}

	public BraintreeCustomerDetailsConverter getCustomerDetailsPopulator()
	{
		return customerDetailsPopulator;
	}


	public void setCustomerDetailsPopulator(final BraintreeCustomerDetailsConverter customerDetailsPopulator)
	{
		this.customerDetailsPopulator = customerDetailsPopulator;
	}

	public PopupWidgetHelper getPopupWidgetHelper()
	{
		return popupWidgetHelper;
	}

	public void setPopupWidgetHelper(final PopupWidgetHelper popupWidgetHelper)
	{
		this.popupWidgetHelper = popupWidgetHelper;
	}

	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

	public void setCsBrainTreeFacade(final CsBrainTreeFacade csBrainTreeFacade)
	{
		this.csBrainTreeFacade = csBrainTreeFacade;
	}

	public ItemAppender<TypedObject> getItemAppender()
	{
		return itemAppender;
	}

	public void setItemAppender(final ItemAppender<TypedObject> itemAppender)
	{
		this.itemAppender = itemAppender;
	}


	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	private static final class LocaleRegionComparator implements Comparator<RegionModel>
	{
		private final Locale locale;

		LocaleRegionComparator(final Locale locale)
		{
			this.locale = locale;
		}

		@Override
		public int compare(final RegionModel o1, final RegionModel o2)
		{
			return UIElementUtils.compareStrings(o1.getName(locale), o2.getName(locale));
		}
	}

	protected static class InternalControls
	{
		private final Textbox stateProvinceRegionField;
		private final Listbox countrySelectField;
		private final Listbox regionSelectField;

		private InternalControls(final Builder builder)
		{
			this.stateProvinceRegionField = builder.stateProvinceRegionField;
			this.regionSelectField = builder.regionSelectField;
			this.countrySelectField = builder.countrySelectField;
		}

		protected final Textbox getStateProvinceRegionField()
		{
			return stateProvinceRegionField;
		}

		protected final Listbox getCountrySelectField()
		{
			return countrySelectField;
		}

		protected final Listbox getRegionSelectField()
		{
			return regionSelectField;
		}

		public static class Builder
		{
			private Textbox stateProvinceRegionField;
			private Listbox countrySelectField;
			private Listbox regionSelectField;

			private Builder stateProvinceRegionField(final Textbox stateProvinceRegionField)
			{
				this.stateProvinceRegionField = stateProvinceRegionField;
				return this;
			}

			private Builder countrySelectField(final Listbox countrySelectField)
			{
				this.countrySelectField = countrySelectField;
				return this;
			}

			private Builder regionSelectField(final Listbox regionSelectField)
			{
				this.regionSelectField = regionSelectField;
				return this;
			}

			private InternalControls bulid()
			{
				return new InternalControls(this);
			}
		}
	}
}
