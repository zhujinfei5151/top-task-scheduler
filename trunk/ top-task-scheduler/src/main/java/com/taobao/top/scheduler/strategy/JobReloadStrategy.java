/**
 * top-task-scheduler
 *  
 */

package com.taobao.top.scheduler.strategy;


/**
 * <p>
 * 任务重置策略
 * </p>
 * 
 * @author <a href="mailto:sihai@taobao.com">sihai</a>
 *
 */
public class JobReloadStrategy {
	
	private int condition = 0;
	
	public JobReloadStrategy(int condition) {
		this.condition = condition;
	}
	
	public boolean isReload(JobReloadConditionEnum condition) {
		return (this.condition & condition.value()) == condition.value();
	}
}
