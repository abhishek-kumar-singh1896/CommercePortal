/**
 *
 */
package com.gallagher.core.services;

import de.hybris.platform.b2b.model.B2BCustomerModel;

import java.io.IOException;

import org.dom4j.DocumentException;


/**
 * @author ankituniyal
 *
 */
public interface GallagherMindTouchService
{

	void pushCustomerToMindTouch(final B2BCustomerModel customer) throws IOException, DocumentException;

	/**
	 * @param customer
	 * @throws IOException
	 * @throws DocumentException
	 */
	void updateCustomerInMindTouch(B2BCustomerModel customer) throws IOException, DocumentException;
}
