package com.taobao.top.scheduler.job;

import com.taobao.top.scheduler.exception.JobKeyDuplicatedException;
import com.taobao.top.scheduler.exception.JobNotFoundException;
import com.taobao.top.scheduler.exception.SchedulerException;
import com.taobao.top.scheduler.job.impl.JobKey;

/**
 * 
 * @author sihai
 * 
 */
public interface JobManager {
	
	/**
	 * 新建默认的job(DefaultJob)
	 * 
	 * @param key
	 * @return
	 * @throws JobKeyDuplicatedException
	 */
	Job newJob(JobKey key) throws JobKeyDuplicatedException;

	/**
	 * 新建Dependency的job(DependencyJob)
	 * 
	 * @param key
	 * @return
	 * @throws JobKeyDuplicatedException
	 */
	Job newDependencyJob(JobKey key) throws JobKeyDuplicatedException;

	/**
	 * 添加新的job，添加到 waitingQueue
	 * 
	 * @param job
	 * @throws SchedulerException
	 */
	void addJob(Job job) throws SchedulerException;

	/**
	 * 获取未完成的job的个数
	 * 
	 * @return
	 */
	public int getUndoJobCount();

	/**
	 * 获取完成的job的个数
	 * 
	 * @return
	 */
	public int getDoingJobCount();

	/**
	 * 获取完成的job的个数
	 * 
	 * @return
	 */
	public int getDoneJobCount();

	/**
	 * 
	 * @param key
	 * @throws JobNotFoundException
	 * @return
	 */
	public Job removeJob(JobKey key) throws JobNotFoundException;
}
