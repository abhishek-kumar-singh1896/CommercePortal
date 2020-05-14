/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;


/**
 *
 */
public class GallagherBynderResponse
{

	private String copyright;
	private String type;
	private String idHash;
	private String id;
	private float height;
	private float archive;
	private String datePublished;
	private float fileSize;
	private String brandId;
	private String name;
	private String description;
	private String userCreated;
	private String dateCreated;
	private float isPublic;
	private String orientation;
	private String dateModified;
	private float width;
	private float watermarked;
	private float limited;
	private String property_Website;
	private Thumbnails thumbnails;
	List<Object> tags = new ArrayList<Object>();
	List<Object> extension = new ArrayList<Object>();
	List<Object> propertyOptions = new ArrayList<Object>();
	List<String> property_part_numbers = new ArrayList<String>();
	List<String> property_product_subtype = new ArrayList<String>();
	List<String> property_assettype = new ArrayList<String>();
	List<String> property_region = new ArrayList<String>();
	List<String> property_product_type = new ArrayList<String>();
	List<String> property_skus = new ArrayList<String>();
	List<String> property_business_unit = new ArrayList<String>();
	List<String> property_product_name = new ArrayList<String>();
	List<String> property_asset_subtype = new ArrayList<String>();

	public List<Object> getTags()
	{
		return tags;
	}

	public void setTags(final List<Object> tags)
	{
		this.tags = tags;
	}

	public String getProperty_Website()
	{
		return property_Website;
	}

	public void setProperty_Website(final String property_Website)
	{
		this.property_Website = property_Website;
	}

	public List<Object> getExtension()
	{
		return extension;
	}

	public void setExtension(final List<Object> extension)
	{
		this.extension = extension;
	}

	public List<Object> getPropertyOptions()
	{
		return propertyOptions;
	}

	public void setPropertyOptions(final List<Object> propertyOptions)
	{
		this.propertyOptions = propertyOptions;
	}

	public List<String> getProperty_part_numbers()
	{
		return property_part_numbers;
	}

	public void setProperty_part_numbers(final List<String> property_part_numbers)
	{
		this.property_part_numbers = property_part_numbers;
	}

	public List<String> getProperty_skus()
	{
		return property_skus;
	}

	public void setProperty_skus(final List<String> property_skus)
	{
		this.property_skus = property_skus;
	}



	public List<String> getProperty_product_subtype()
	{
		return property_product_subtype;
	}

	public void setProperty_product_subtype(final List<String> property_product_subtype)
	{
		this.property_product_subtype = property_product_subtype;
	}

	public List<String> getProperty_assettype()
	{
		return property_assettype;
	}

	public void setProperty_assettype(final List<String> property_assettype)
	{
		this.property_assettype = property_assettype;
	}

	public List<String> getProperty_region()
	{
		return property_region;
	}

	public void setProperty_region(final List<String> property_region)
	{
		this.property_region = property_region;
	}

	public List<String> getProperty_product_type()
	{
		return property_product_type;
	}

	public void setProperty_product_type(final List<String> property_product_type)
	{
		this.property_product_type = property_product_type;
	}

	public List<String> getProperty_business_unit()
	{
		return property_business_unit;
	}

	public void setProperty_business_unit(final List<String> property_business_unit)
	{
		this.property_business_unit = property_business_unit;
	}

	public List<String> getProperty_product_name()
	{
		return property_product_name;
	}

	public void setProperty_product_name(final List<String> property_product_name)
	{
		this.property_product_name = property_product_name;
	}

	public List<String> getProperty_asset_subtype()
	{
		return property_asset_subtype;
	}

	public void setProperty_asset_subtype(final List<String> property_asset_subtype)
	{
		this.property_asset_subtype = property_asset_subtype;
	}

	public String getCopyright()
	{
		return copyright;
	}

	public String getType()
	{
		return type;
	}

	public String getIdHash()
	{
		return idHash;
	}

	public String getId()
	{
		return id;
	}

	public float getHeight()
	{
		return height;
	}

	public float getArchive()
	{
		return archive;
	}

	public String getDatePublished()
	{
		return datePublished;
	}

	public float getFileSize()
	{
		return fileSize;
	}

	public String getBrandId()
	{
		return brandId;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public String getUserCreated()
	{
		return userCreated;
	}

	public String getDateCreated()
	{
		return dateCreated;
	}

	public float getIsPublic()
	{
		return isPublic;
	}

	public String getOrientation()
	{
		return orientation;
	}

	public String getDateModified()
	{
		return dateModified;
	}

	public float getWidth()
	{
		return width;
	}

	public float getWatermarked()
	{
		return watermarked;
	}

	public float getLimited()
	{
		return limited;
	}

	public Thumbnails getThumbnails()
	{
		return thumbnails;
	}

	// Setter Methods

	public void setCopyright(final String copyright)
	{
		this.copyright = copyright;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public void setIdHash(final String idHash)
	{
		this.idHash = idHash;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public void setHeight(final float height)
	{
		this.height = height;
	}

	public void setArchive(final float archive)
	{
		this.archive = archive;
	}

	public void setDatePublished(final String datePublished)
	{
		this.datePublished = datePublished;
	}

	public void setFileSize(final float fileSize)
	{
		this.fileSize = fileSize;
	}

	public void setBrandId(final String brandId)
	{
		this.brandId = brandId;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public void setUserCreated(final String userCreated)
	{
		this.userCreated = userCreated;
	}

	public void setDateCreated(final String dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	public void setIsPublic(final float isPublic)
	{
		this.isPublic = isPublic;
	}

	public void setOrientation(final String orientation)
	{
		this.orientation = orientation;
	}

	public void setDateModified(final String dateModified)
	{
		this.dateModified = dateModified;
	}

	public void setWidth(final float width)
	{
		this.width = width;
	}

	public void setWatermarked(final float watermarked)
	{
		this.watermarked = watermarked;
	}

	public void setLimited(final float limited)
	{
		this.limited = limited;
	}

	public void setThumbnails(final Thumbnails thumbnails)
	{
		this.thumbnails = thumbnails;
	}

	public class Thumbnails
	{
		private String mini;
		private String webimage;
		private String thul;
		@SerializedName(value = "General Purpose")
		private String generalPurpose;


		public String getGeneralPurpose()
		{
			return generalPurpose;
		}

		public void setGeneralPurpose(final String generalPurpose)
		{
			this.generalPurpose = generalPurpose;
		}

		// Getter Methods
		public String getMini()
		{
			return mini;
		}

		public String getWebimage()
		{
			return webimage;
		}

		public String getThul()
		{
			return thul;
		}

		// Setter Methods

		public void setMini(final String mini)
		{
			this.mini = mini;
		}

		public void setWebimage(final String webimage)
		{
			this.webimage = webimage;
		}

		public void setThul(final String thul)
		{
			this.thul = thul;
		}
	}

	@Override
	public String toString()
	{
		return "GallagherBynderResponse [copyright=" + copyright + ", type=" + type + ", idHash=" + idHash + ", id=" + id
				+ ", height=" + height + ", archive=" + archive + ", tags=" + tags + ", datePublished=" + datePublished
				+ ", fileSize=" + fileSize + ", brandId=" + brandId + ", name=" + name + ", extension=" + extension + ", description="
				+ description + ", userCreated=" + userCreated + ", dateCreated=" + dateCreated + ", isPublic=" + isPublic
				+ ", propertyOptions=" + propertyOptions + ", orientation=" + orientation + ", dateModified=" + dateModified
				+ ", width=" + width + ", watermarked=" + watermarked + ", limited=" + limited + ", thumbnails=" + thumbnails
				+ ", property_part_numbers=" + property_part_numbers + ", property_product_subtype=" + property_product_subtype
				+ ", property_assettype=" + property_assettype + ", property_region=" + property_region + ", property_product_type="
				+ property_product_type + ", property_skus=" + property_skus + ", property_business_unit=" + property_business_unit
				+ ", property_product_name=" + property_product_name + ", property_asset_subtype=" + property_asset_subtype + "]";
	}

}
