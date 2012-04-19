/**
 * top-task-scheduler
 * 
 */

package com.taobao.top.scheduler.internal;

import com.taobao.top.scheduler.job.internal.Job;


/**
 * <p>
 * 任务调度Slave实例节点, 内部接口
 * </p>
 * 
 * @author <a href="mailto:sihai@taobao.com">sihai</a>
 *
 */
public interface SlaveScheduler extends com.taobao.top.scheduler.SlaveScheduler {

	String SLAVE_PULL_JOB_COMMAND_PROVIDER_NAME = "Top-Task-Scheduler-Slave-Pull-Job-Command-Provider";
	String SLAVE_PUSH_JOB_RESULT_COMMAND_PROVIDER_NAME = "Top-Task-Scheduler-Slave-Push-Job-Result-Command-Provider";
	
	/**
	 * 
	 * @param job
	 */
	void handle(Job job);
	
	/**
	 * 
	 */
	//void pullJob();
}
