/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

/**
 *
 */
public class GallagherBynderMediaResponse
{

	private String s3_file;

	public String getS3_file()
	{
		return s3_file;
	}

	public void setS3_file(final String s3_file)
	{
		this.s3_file = s3_file;
	}

	@Override
	public String toString()
	{
		return "GallagherBynderMediaResponse [s3_file=" + s3_file + "]";
	}

}
