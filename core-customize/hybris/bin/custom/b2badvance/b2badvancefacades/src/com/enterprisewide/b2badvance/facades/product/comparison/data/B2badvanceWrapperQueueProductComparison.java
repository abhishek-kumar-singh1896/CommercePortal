/**
 *
 */
package com.enterprisewide.b2badvance.facades.product.comparison.data;

import java.util.LinkedList;
import java.util.Queue;


/**
 * @author Enterprise Wide
 *
 */
public class B2badvanceWrapperQueueProductComparison
{
	private Queue queue = new LinkedList<String>();

	/** @return the dffdsfqueue */
	public Queue getQueue()
	{
		return queue;
	}

	/**
	 * @param queue
	 *           the queue to set
	 */
	public void setQueue(final Queue queue)
	{
		this.queue = queue;
	}
}
