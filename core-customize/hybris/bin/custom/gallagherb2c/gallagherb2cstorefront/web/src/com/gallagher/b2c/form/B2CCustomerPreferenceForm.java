/**
 *
 */
package com.gallagher.b2c.form;

/**
 * Pojo to collect customers preferences
 *
 * @author abhishek
 *
 */
public class B2CCustomerPreferenceForm
{
	private boolean newsLetters;
	private boolean events;
	private boolean productRelease;
	private boolean productUpdate;
	private boolean productPromo;

	public boolean isNewsLetters()
	{
		return newsLetters;
	}

	public void setNewsLetters(final boolean newsLetters)
	{
		this.newsLetters = newsLetters;
	}

	public boolean isEvents()
	{
		return events;
	}

	public void setEvents(final boolean events)
	{
		this.events = events;
	}

	public boolean isProductRelease()
	{
		return productRelease;
	}

	public void setProductRelease(final boolean productRelease)
	{
		this.productRelease = productRelease;
	}

	public boolean isProductUpdate()
	{
		return productUpdate;
	}

	public void setProductUpdate(final boolean productUpdate)
	{
		this.productUpdate = productUpdate;
	}

	public boolean isProductPromo()
	{
		return productPromo;
	}

	public void setProductPromo(final boolean productPromo)
	{
		this.productPromo = productPromo;
	}

}
