/**
 *
 */
package com.gallagher.core.services;

import de.hybris.platform.core.model.user.CustomerModel;

import java.io.IOException;

import org.dom4j.DocumentException;


/**
 * @author ankituniyal
 *
 */
public interface GallagherMindTouchService
{

	void pushCustomerToMindTouch(final CustomerModel customer) throws IOException, DocumentException;
}
