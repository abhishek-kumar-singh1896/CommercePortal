/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.core.constants;

/**
 * Global class for all GallagherCore constants. You can add global constants for your extension into this class.
 */
public final class GallagherCoreConstants extends GeneratedGallagherCoreConstants
{
	public static final String EXTENSIONNAME = "gallaghercore";


	private GallagherCoreConstants()
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
	public static final String GGL_TIMEZONE = "gallagherTimeZone";
	public static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
	public static final String LOCALHOST_IPV4 = "127.0.0.1";

	public interface Bynder
	{
		public static final String IMAGE = "image";
		public static final String IMAGE_VECTOR = "image_vector";
		public static final String DOCUMENTS = "documents";
		public static final String VIDEOS = "videos";
		public static final String IMAGES = "images";
		public static final String B2BADVANCE_BF = "B2BADVANCE-BF";
		public static final String DATE_MODIFIED = "dateModified";
		public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		public static final String CONSUMER_KEY = "bynder.access.consumerKey";
		public static final String CONSUMER_SECRET = "bynder.access.consumerSecret";
		public static final String ACCESS_TOKEN = "bynder.access.token";
		public static final String TOKEN_SECRET = "bynder.access.tokenSecret";
		public static final String BASE_URL = "bynder.base.url";
		public static final String BYNDER_QUERY = "bynder.query.";


	}

}
