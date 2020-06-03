/**
 *
 */
package com.enterprisewide.b2badvance.facades.product.comparison.impl;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.enterprisewide.b2badvance.facades.product.comparison.B2badvanceProductComparisonFacade;
import com.enterprisewide.b2badvance.facades.product.comparison.constants.B2badvanceProductcomparisonConstants;
import com.enterprisewide.b2badvance.facades.product.comparison.data.B2badvanceWrapperQueueProductComparison;
import com.enterprisewide.b2badvance.facades.product.comparison.data.WrapperMapVariantAttributes;


/**
 * @author Enterprise Wide
 *
 */
public class B2badvanceProductComparisonFacadeImpl implements B2badvanceProductComparisonFacade
{

	private final int MAX_QUEUE_SIZE = 3;

	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
			ProductOption.CLASSIFICATION, ProductOption.STOCK, ProductOption.VARIANT_FULL, ProductOption.URL, ProductOption.VARIANT_MATRIX);

	private final Logger LOG = Logger.getLogger(B2badvanceProductComparisonFacadeImpl.class);

	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ProductData> getProductComparisonList()
	{
		final List recentlyViewedProductCodes = Arrays.asList(retrieveQueueFromSession());
		Collections.reverse(recentlyViewedProductCodes);

		//@TODO .The below code can be refactored
		final List<ProductData> productComparisonList = new ArrayList<ProductData>();

		//Contruct a product list - logic to retrieve a productdata against a product code, and then append to a product list.
		for (final Object productCode : (List) recentlyViewedProductCodes.get(0))
		{
			//System.out.println("******REtrieving TOP products - Product Code " + (String) productCode);
			try
			{
				//String productCode;
				final ProductData prodData = productFacade.getProductForCodeAndOptions((String) productCode, PRODUCT_OPTIONS);
				if (prodData != null)
				{
					//then add to a list of products that need to be visible.
					productComparisonList.add(prodData);
				}
			}
			catch (final Exception e)
			{
				remove((String) productCode);
				LOG.error("Product detail for product article number  " + productCode + " is not available.", e);
			}
		}

		//merge classification attributes
		final Map<String, Set<String>> classificationIds = findCommonClassificationAttributes(productComparisonList);
		updateProductClassifications(productComparisonList, classificationIds);
		return productComparisonList;
	}

	private void updateProductClassifications(final List<ProductData> productComparisonList,
			final Map<String, Set<String>> classificationIds)
	{
		if (productComparisonList != null)
		{
			for (final ProductData product : productComparisonList)
			{
				final List<ClassificationData> resultList = new ArrayList<ClassificationData>();
				if (product.getClassifications() != null)
				{
					for (final ClassificationData classData : product.getClassifications())
					{
						if (classificationIds.containsKey(classData.getCode()))
						{
							final List<FeatureData> features = new ArrayList<FeatureData>();
							//final Set<String> featureNames = classificationIds.get(classData.getCode());
							final Map<String, FeatureData> fMap = new HashMap<String, FeatureData>();
							for (final FeatureData fd : classData.getFeatures())
							{
								if (fd.isComparable())
								{
									fMap.put(fd.getName(), fd);
								}
							}
							final Set<String> classificationSet = classificationIds.get(classData.getCode());
							for (final String featureName : classificationSet)
							{
								if (!fMap.containsKey(featureName))
								{
									final FeatureData featureData = new FeatureData();
									featureData.setName(featureName);
									final FeatureValueData valueData = new FeatureValueData();
									valueData.setValue("-");
									final Collection<FeatureValueData> featureCollection = new ArrayList<FeatureValueData>();
									featureCollection.add(valueData);
									featureData.setFeatureValues(featureCollection);
									fMap.put(featureName, featureData);
								}
							}
							//							for (final String fData : featureNames)
							//							{
							//								if (!fMap.containsKey(fData))
							//								{
							//									final FeatureData fd = new FeatureData();
							//									fd.setName(fData);
							//									fd.setComparable(true);
							//									fMap.put(fData, fd);
							//								}
							//							}
							features.addAll(fMap.values());

							if (!fMap.isEmpty())
							{
								Collections.sort(features, new ProductComparisonClassificationFeatureComparator());
								classData.setFeatures(features);
								resultList.add(classData);
							}
						}
					}
				}
				//sort result list
				Collections.sort(resultList, new ProductComparisonClassificationComparator());

				product.setClassifications(resultList);
			}
		}
	}

	private Map<String, Set<String>> findCommonClassificationAttributes(final List<ProductData> productComparisonList)
	{
		final Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		if (!productComparisonList.isEmpty())
		{
			final ProductData firstProduct = productComparisonList.get(0);
			if (firstProduct.getClassifications() != null)
			{
				for (final ClassificationData classData : firstProduct.getClassifications())
				{
					final String classCode = classData.getCode();
					final Set<String> classFeatureCodes = new HashSet<String>();
					boolean addToCommonList = true;
					//search through products
					for (int i = 0; i < productComparisonList.size(); i++)
					{
						boolean found = false;
						final ProductData product = productComparisonList.get(i);
						if (product.getClassifications() != null)
						{
							//search through class attr
							for (final ClassificationData classInnerData : product.getClassifications())
							{
								if (classInnerData.getCode().equals(classCode))
								{
									//found
									found = true;
									for (final FeatureData fd : classInnerData.getFeatures())
									{
										if (fd.isComparable())
										{
											classFeatureCodes.add(fd.getName());
										}
									}
									break;
								}
							}
						}
						if (!found)
						{
							//not found classification attr for some product -> dont add to list
							addToCommonList = false;
							break;
						}
					}
					//check addToCommonList
					if (addToCommonList)
					{
						result.put(classCode, classFeatureCodes);
					}
				}
			}
		}
		return result;
	}

	@Override
	public List<String> getProductComparisonCodes()
	{
		final List recentlyViewedProductCodes = Arrays.asList(retrieveQueueFromSession());
		return (List) recentlyViewedProductCodes.get(0);
	}

	@Override
	public int size()
	{
		return retrieveQueueFromSession().size();
	}

	@Override
	public boolean isEmpty()
	{
		return retrieveQueueFromSession().isEmpty();
	}

	@Override
	public boolean contains(final String productCode)
	{
		if (productCode == null)
		{
			return false;
		}
		return retrieveQueueFromSession().contains(productCode);
	}

	@Override
	public boolean add(final String code)
	{
		if (code == null)
		{
			return false;
		}

		//RETRIEVE QUEUE
		final Queue queue = retrieveQueueFromSession();

		//if the variable size hits the max, then remove an entry.
		if ((queue.size() == this.MAX_QUEUE_SIZE))
		{
			/*
			 * whilst removing, if the queue does not contain the new code then accomodate by removing the head, else find
			 * and remove the older instance.
			 */
			if (!queue.contains(code))
			{
				queue.remove();
			}
		}
		//if at any time there is an existing code, then first remove it from current position and then add this one to the tail.
		if (queue.contains(code))
		{
			queue.remove(code);
		}

		//UPDATE QUEUE TO SESSION
		if (this.MAX_QUEUE_SIZE > 0)
		{
			queue.add(code);

		}
		updateQueueToSession(queue);

		return true;
	}

	@Override
	public String remove()
	{
		//RETRIEVE CURRENT QUEUE
		final Queue queue = retrieveQueueFromSession();

		if (queue.size() <= 0)
		{
			return null;
		}

		final String removed = (String) queue.remove();

		//UPDATE CURRENT QUEUE
		updateQueueToSession(queue);
		return removed;
	}

	@Override
	public boolean remove(final String productCode)
	{
		//RETRIEVE QUEUE
		final Queue queue = retrieveQueueFromSession();
		if (productCode == null || productCode.isEmpty() || queue.size() < 0)
		{
			return false;
		}
		final boolean removed = queue.remove(productCode);

		//UPDATE QUEUE
		updateQueueToSession(queue);
		return removed;
	}

	@Override
	public void clear()
	{
		final Queue queue = retrieveQueueFromSession();
		queue.clear();
		updateQueueToSession(queue);
	}

	private Queue retrieveQueueFromSession()
	{
		if (sessionService.getAttribute(B2badvanceProductcomparisonConstants.SESSION_ATTR_PRODUCTCOMPARISON) == null)
		{
			sessionService.setAttribute(B2badvanceProductcomparisonConstants.SESSION_ATTR_PRODUCTCOMPARISON,
					new B2badvanceWrapperQueueProductComparison());
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Retrieveing Session id = " + sessionService.getCurrentSession().getSessionId());
		}
		return ((B2badvanceWrapperQueueProductComparison) sessionService
				.getAttribute(B2badvanceProductcomparisonConstants.SESSION_ATTR_PRODUCTCOMPARISON)).getQueue();
	}

	private void updateQueueToSession(final Queue queue)
	{
		final B2badvanceWrapperQueueProductComparison wrapperQueue = (B2badvanceWrapperQueueProductComparison) sessionService
				.getAttribute(B2badvanceProductcomparisonConstants.SESSION_ATTR_PRODUCTCOMPARISON);
		wrapperQueue.setQueue(queue);
		sessionService.setAttribute(B2badvanceProductcomparisonConstants.SESSION_ATTR_PRODUCTCOMPARISON, wrapperQueue);
	}

	@Override
	public List<WrapperMapVariantAttributes> getProductVariantAttributes(final List<ProductData> productList)
	{
		final List<WrapperMapVariantAttributes> result = new ArrayList<WrapperMapVariantAttributes>();
		if (productList != null && productList.size() > 0)
		{
			//Map<[VariantOptionQualifier.name], Map<[productCode],[VariantOptionQualifier.value]>>
			//final Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

			final ProductData firstProduct = productList.get(0);
			if (firstProduct.getBaseOptions() != null)
			{
				for (final BaseOptionData fBaseOption : firstProduct.getBaseOptions())
				{
					if (fBaseOption.getSelected() != null && fBaseOption.getSelected().getCode().equals(firstProduct.getCode()))
					{
						if (fBaseOption.getSelected().getVariantOptionQualifiers() != null)
						{
							boolean addToCommonList = true;
							for (final VariantOptionQualifierData voqd : fBaseOption.getSelected().getVariantOptionQualifiers())
							{
								final String name = voqd.getName();
								final String qualifier = voqd.getQualifier();
								final Map<String, String> valueMap = new HashMap<String, String>();
								valueMap.put(firstProduct.getCode(), voqd.getValue());
								boolean found = false;
								for (final ProductData product : productList)
								{
									if (product.getBaseOptions() != null && product.getBaseOptions().size() > 0)
									{
										for (final BaseOptionData baseOption : product.getBaseOptions())
										{
											if (baseOption.getSelected() != null
													&& baseOption.getSelected().getCode().equals(product.getCode()))
											{
												if (baseOption.getSelected().getVariantOptionQualifiers() != null)
												{
													for (final VariantOptionQualifierData vo : baseOption.getSelected()
															.getVariantOptionQualifiers())
													{
														if (voqd.getQualifier().equals(vo.getQualifier()))
														{
															found = true;
															valueMap.put(product.getCode(), vo.getValue());
															break;
														}
													}
												}
											}
										}
									}
									if (!found)
									{
										addToCommonList = false;
										break;
									}
								}

								if (addToCommonList)
								{
									final WrapperMapVariantAttributes wrapper = new WrapperMapVariantAttributes();
									wrapper.setName(name);
									wrapper.setQualifier(qualifier);
									wrapper.getProductAttrValueMap().putAll(valueMap);
									result.add(wrapper);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
}

class ProductComparisonClassificationComparator implements Comparator<ClassificationData>
{
	@Override
	public int compare(final ClassificationData a, final ClassificationData b)
	{
		return a.getName().compareTo(b.getName());
	}
}

class ProductComparisonClassificationFeatureComparator implements Comparator<FeatureData>
{
	@Override
	public int compare(final FeatureData a, final FeatureData b)
	{
		return a.getName().compareTo(b.getName());
	}
}
