/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.enterprisewide.b2badvance.core.constants;

/**
 * Global class for all B2badvanceCore constants. You can add global constants for your extension into this class.
 */
public final class B2badvanceCoreConstants extends GeneratedB2badvanceCoreConstants
{
	public static final String EXTENSIONNAME = "b2badvancecore";


	private B2badvanceCoreConstants()
	{
		//empty
	}

	// implement here constants used by this extension
	public static final String QUOTE_BUYER_PROCESS = "quote-buyer-process";
	public static final String QUOTE_SALES_REP_PROCESS = "quote-salesrep-process";
	public static final String QUOTE_USER_TYPE = "QUOTE_USER_TYPE";
	public static final String QUOTE_SELLER_APPROVER_PROCESS = "quote-seller-approval-process";
	public static final String QUOTE_TO_EXPIRE_SOON_EMAIL_PROCESS = "quote-to-expire-soon-email-process";
	public static final String QUOTE_EXPIRED_EMAIL_PROCESS = "quote-expired-email-process";
	public static final String QUOTE_POST_CANCELLATION_PROCESS = "quote-post-cancellation-process";


	// SFTP Server Separator
	public static final String SFTP_SERVER_SEPARATOR = "/";
	public static final String DEFAULT_CSV_ENCODING = "UTF-8";
	public static final String CSV_EXTENSION = ".csv";
	public static final String UNDERSCORE = "_";
	public static final String SPACE = " ";
	public static final String COMMA = ",";

	public static final char SFTP_CSV_FILE_SEPARATOR = ',';
	public static final String SFTP_FILES_LOCATION = "sftp";
	public static final String CSV_FILES_LOCATION = "csv";
	public static final String INVOICE_FILES_LOCATION = "invoice";
	public static final String BASE_PRODUCT_FILES_LOCATION = "baseProduct";
	public static final String VARIANT_PRODUCT_FILES_LOCATION = "variantProduct";
	public static final String INVOICE_FILES = "invoice";
	public static final String PRODUCT_FILES = "product";
	public static final String BASE_PRODUCT_FILES = "baseProduct";
	public static final String VARIANT_PRODUCT_FILES = "variantProduct";
	public static final String FILE_STARTER = "b2badvance";
	public static final String SFTP_FILE_MATCHING_SUFFIX_REGEX = "[0-9]{16}.csv";
	public static final String SFTP_DATE_REGEX = "yyyyMMddHHmmssSSS";

	// CSV File Import Prefixes
	public static final String B2BADVANCE_INVOICE_FILE_PREFIX = "b2bAdvanceInvoiceImport";
	public static final String B2BADVANCE_BASE_PRODUCT_FILE_PREFIX = "b2bAdvanceBaseProductImport";
	public static final String B2BADVANCE_VARIANT_PRODUCT_FILE_PREFIX = "b2bAdvanceVariantProductImport";


	// SFTP Cred Keys
	public static final String SFTP_HOST_KEY = "sftp.host";
	public static final String SFTP_PORT_KEY = "sftp.port";
	public static final String SFTP_USERNAME_KEY = "sftp.username";
	public static final String SFTP_PASSWORD_KEY = "sftp.password";
	public static final String SFTP_DIRECTORY_KEY = "sftp.directory";
	public static final String SFTP_FILE_TYPE_REGEX_INVOICE = "invoice_";
	public static final String SFTP_FILE_TYPE_REGEX_PRODUCT = "(sku|base)_";
	public static final char SFTP_CSV_UNDERSCORE = '_';
	public static final String FAILURE = "failure";

	//Change control operations
	public static final String ENABLE = "ENABLE";
	public static final String DELETE = "DELETE";
	public static final String APPROVED = "approved";
	public static final String UNAPPROVED = "unapproved";
	public static final String IGNORE = "<ignore>";


	public static final String B2BTECNICIANGROUP = "b2btechniciangroup";


}
