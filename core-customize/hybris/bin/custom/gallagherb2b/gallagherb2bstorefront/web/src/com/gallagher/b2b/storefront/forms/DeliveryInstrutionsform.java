/**
 *
 */
package com.gallagher.b2b.storefront.forms;

/**
 * @author abhinavgupta03
 *
 */
public class DeliveryInstrutionsform
{
	private String deliveryInstruction;

	private Integer entryNumber;

	private String commentPlaceHolder;

	/**
	 * @return the commentPlaceHolder
	 */
	public String getCommentPlaceHolder()
	{
		return commentPlaceHolder;
	}

	/**
	 * @param commentPlaceHolder
	 *           the commentPlaceHolder to set
	 */
	public void setCommentPlaceHolder(final String commentPlaceHolder)
	{
		this.commentPlaceHolder = commentPlaceHolder;
	}

	/**
	 * @return the deliveryInstruction
	 */
	public String getDeliveryInstruction()
	{
		return deliveryInstruction;
	}

	/**
	 * @param deliveryInstruction
	 *           the deliveryInstruction to set
	 */
	public void setDeliveryInstruction(final String deliveryInstruction)
	{
		this.deliveryInstruction = deliveryInstruction;
	}

	/**
	 * @return the entryNumber
	 */
	public Integer getEntryNumber()
	{
		return entryNumber;
	}

	/**
	 * @param entryNumber
	 *           the entryNumber to set
	 */
	public void setEntryNumber(final Integer entryNumber)
	{
		this.entryNumber = entryNumber;
	}

}
