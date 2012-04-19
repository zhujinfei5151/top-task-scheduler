/**
 * top-task-scheduler
 * 
 */
package com.taobao.top.scheduler.demo.slave;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.top.scheduler.demo.job.DefaultJobResult;
import com.taobao.top.scheduler.job.Job;
import com.taobao.top.scheduler.job.JobConsumer;
import com.taobao.top.scheduler.job.JobResult;
import com.taobao.top.scheduler.job.SlaveJobCompletedCallback;


/**
 * 
 * demo的任务消费器
 * 
 * @author sihai
 *
 */
public class DefaultJobConsumer implements JobConsumer {
	private final static Log logger = LogFactory.getLog(DefaultJobConsumer.class);
	
	@Override
	public JobResult work(Job job) throws Exception {
		//logger.info(new StringBuilder("Do job: jobKey = ").append(job.getKey()).append(", jobConent = ").append(job.getJobContent().toString()));
		return new DefaultJobResult(new StringBuilder("Job result of ").append(job.getKey()).toString());
	}

	@Override
	public void workNoBlocking(Job job, SlaveJobCompletedCallback callback) throws Exception {
		logger.info(new StringBuilder("Do job: id = ").append(job.getId()));
		
		callback.callback(true, job, new DefaultJobResult(new StringBuilder("Job result of ").append(job.getJobContent()).toString()));
	}
}
