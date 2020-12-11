/**
 *
 */
package com.gallagher.core.services;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.user.CustomerModel;

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
	 * @param currentUser
	 * @throws IOException
	 * @throws DocumentException
	 */
	void updateCustomerInMindTouch(CustomerModel currentUser) throws IOException, DocumentException;
}
