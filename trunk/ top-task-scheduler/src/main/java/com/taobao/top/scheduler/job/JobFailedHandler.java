/**
 * top-task-scheduler
 * 
 */

package com.taobao.top.scheduler.job;

import java.util.List;

/**
 * <p>
 * Master节点任务失败处理器接口
 * </p>
 * 
 * @author <a href="mailto:sihai@taobao.com">sihai</a>
 *
 */
public interface JobFailedHandler {
	
	/**
	 * 处理执行失败任务
	 * @param jobManager
	 * @param job
	 */
	void handle(JobManager jobManager, Job job);
	
	/**
	 * 批量处理执行失败任务
	 * @param jobManager
	 * @param jobList
	 */
	void handle(JobManager jobManager, List<Job> jobList); 
}
