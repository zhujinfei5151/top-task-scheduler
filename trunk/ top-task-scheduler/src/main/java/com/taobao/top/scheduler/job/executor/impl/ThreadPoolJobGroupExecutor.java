/**
 * top-task-scheduler
 *  
 */

package com.taobao.top.scheduler.job.executor.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.top.scheduler.common.TopTaskSchedulerThreadFactory;
import com.taobao.top.scheduler.config.ResourceConfig;
import com.taobao.top.scheduler.job.JobConsumer;
import com.taobao.top.scheduler.job.executor.AbstractJobGroupExecutor;
import com.taobao.top.scheduler.job.executor.JobGroupExecutorBusyException;

/**
 * <p>
 * 基于线程池的任务组执行单元
 * </p>
 * 
 * @author <a href="mailto:sihai@taobao.com">sihai</a>
 *
 */
public class ThreadPoolJobGroupExecutor extends AbstractJobGroupExecutor {

	private static final Log logger = LogFactory.getLog(ThreadPoolJobGroupExecutor.class);
	
	private BlockingQueue<Runnable> workQueue;		// 任务队列
	private ThreadPoolExecutor threadPool;			// 线程池
	
	public ThreadPoolJobGroupExecutor(final String group, JobConsumer jobConsumer, ResourceConfig rconfig) {
		super(group, rconfig, jobConsumer);
		workQueue = new LinkedBlockingQueue<Runnable>(rconfig.getMaxQueueSize());
		threadPool = new ThreadPoolExecutor(rconfig.getMinThread(), rconfig.getMaxThread(), 120, TimeUnit.SECONDS, workQueue, new TopTaskSchedulerThreadFactory("Top-Task-Scheduler-Job-Consumer-Group(" + group + ")-ThreadPool", null, true));
	}
	@Override
	public void execute(Runnable task) throws JobGroupExecutorBusyException {
		try {
			threadPool.execute(task);
		} catch (RejectedExecutionException e) {
			throw new JobGroupExecutorBusyException(e);
		}
 	}

	@Override
	public int availableCapacity() {
		return workQueue.remainingCapacity();
	}

	@Override
	public void shutdownExecute() {
		threadPool.shutdown();
		workQueue.clear();
	}

	@Override
	public void shutdownExecuteNow() {
		threadPool.shutdownNow();
		workQueue.clear();
	}
}
