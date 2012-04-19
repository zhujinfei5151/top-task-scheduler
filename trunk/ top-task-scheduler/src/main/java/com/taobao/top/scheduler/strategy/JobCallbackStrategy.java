/**
 * top-task-scheduler
 *  
 */

package com.taobao.top.scheduler.strategy;


/**
 * <p>
 * 任务回调策略
 * </p>
 * 
 * @author <a href="mailto:sihai@taobao.com">sihai</a>
 *
 */
public class JobCallbackStrategy {
	
	private int condition = 0;
	
	public JobCallbackStrategy(int condition) {
		this.condition = condition;
	}
	
	public boolean isCallback(JobCallbackConditionEnum condition) {
		return (this.condition & condition.value()) == condition.value();
	}
}
