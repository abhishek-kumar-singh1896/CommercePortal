/**
 *
 */
package com.enterprisewide.b2badvance.core.cache.impl;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import org.apache.log4j.Logger;


/**
 * @author Enterprise Wide
 *
 */
public class DefaultB2badvanceManualCacheRegion extends InvalidationBasedB2badvanceManualCacheRegion
{
	private static final Logger LOG = Logger.getLogger(DefaultB2badvanceManualCacheRegion.class);
	private static final int INVALIDATIONTYPE_MODIFIED = 1;

	/**
	 * EHCache Manager
	 */
	private CacheManager cacheManager;
	/**
	 * Cache instance
	 */
	private Cache cacheMap;

	/**
	 * @param cacheName
	 */
	public DefaultB2badvanceManualCacheRegion(final String cacheName)
	{
		super(cacheName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see au.com.b2bAdvanceXyz.core.cache.impl.AbstractB2bAdvanceXyzManualCacheRegion#initCacheProvider()
	 */
	@Override
	protected void initCacheProvider()
	{
		this.cacheManager = CacheManager.getInstance();
		if (!this.cacheManager.cacheExists(this.cacheName))
		{
			final Cache cacheMap = new Cache(this.createCacheConfiguration());
			this.cacheManager.addCache(cacheMap);
			this.cacheMap = this.cacheManager.getCache(this.cacheName);
		}
	}

	/**
	 * Creates EHCache configuration
	 */
	private CacheConfiguration createCacheConfiguration()
	{
		final CacheConfiguration config = new CacheConfiguration();
		config.setStatistics(false);
		LOG.info("B2badvanceManualCacheRegion " + this.cacheName + ", eviction policy " + this.evictionPolicy + ", size "
				+ this.maxEntries);
		config.setMemoryStoreEvictionPolicy(this.evictionPolicy.toString());
		config.setMaxElementsOnDisk(1);
		config.setMaxEntriesLocalHeap(this.maxEntries.longValue());
		config.setName(this.cacheName);
		config.overflowToDisk(false);
		if (this.ttlSeconds == null)
		{
			config.setEternal(true);
		}
		else
		{
			config.setEternal(false);
			config.setTimeToLiveSeconds(this.ttlSeconds.longValue());
		}

		return config;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see au.com.b2bAdvanceXyz.core.cache.B2bAdvanceXyzManualCacheRegion#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void put(final Object key, final Object value)
	{
		this.cacheMap.put(new Element(key, value));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see au.com.b2bAdvanceXyz.core.cache.B2bAdvanceXyzManualCacheRegion#get(java.lang.Object)
	 */
	@Override
	public Object get(final Object key)
	{
		final Element element = this.cacheMap.get(key);
		Object returnedValue = null;
		if (element != null)
		{
			returnedValue = element.getObjectValue();
		}
		return returnedValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see au.com.b2bAdvanceXyz.core.cache.B2bAdvanceXyzManualCacheRegion#invalidate(java.lang.Object)
	 */
	@Override
	public void invalidate(final Object key)
	{
		this.invalidate(key, INVALIDATIONTYPE_MODIFIED);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see au.com.b2bAdvanceXyz.core.cache.B2bAdvanceXyzManualCacheRegion#clear()
	 */
	@Override
	public void clear()
	{
		for (final Object key : this.cacheMap.getKeys())
		{
			this.invalidate(key);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.b2bAdvanceXyz.core.cache.impl.AbstractB2bAdvanceXyzManualCacheRegion#removeCacheElement(java.lang.
	 * Object)
	 */
	@Override
	protected void removeCacheElement(final Object key)
	{
		this.cacheMap.remove(key);
	}

}
