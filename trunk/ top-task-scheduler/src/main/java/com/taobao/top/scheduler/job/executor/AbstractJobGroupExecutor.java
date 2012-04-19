/**
 * top-task-scheduler
 *  
 */

package com.taobao.top.scheduler.job.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.top.scheduler.config.ResourceConfig;
import com.taobao.top.scheduler.job.JobConsumer;

/**
 * <p>
 * 抽象的任务组执行单元
 * </p>
 * 
 * @author <a href="mailto:sihai@taobao.com">sihai</a>
 *
 */
public abstract class AbstractJobGroupExecutor implements JobGroupExecutor {
	
	private static final Log logger = LogFactory.getLog(AbstractJobGroupExecutor.class);
	
	private String group;							// 任务分组名
	private ResourceConfig rconfig;					// 资源配置
	private JobConsumer jobConsumer;				// 业务任务执行器
	private AtomicLong completed;					// 已经执行的任务数量
	// 下面是为了提供过载保护
	private AtomicBoolean jobAssignThreadStared;	// 任务下放线程启动了吗
	private Thread jobAssignThread;					// 任务下放，过载保护
	private BlockingQueue<Runnable> taskBuffer;		// 等待下放的任务缓冲
	private AtomicBoolean isBusy;					// 执行器是否方忙
	private ReentrantLock assignLock;				// 下放任务开始执行信号, 保护锁
	private Condition assignCondition;				// 下放任务开始执行信号
	private boolean isAssign;						// 下放任务是否执行

	public AbstractJobGroupExecutor(String group, ResourceConfig rconfig, JobConsumer jobConsumer) {
		this.group = group;
		this.rconfig = rconfig;
		this.jobConsumer = jobConsumer;
		this.completed = new AtomicLong(0L);
		this.jobAssignThreadStared = new AtomicBoolean(false);
		this.isBusy = new AtomicBoolean(false);
		this.assignLock = new ReentrantLock();
		this.assignCondition = this.assignLock.newCondition();
		this.isAssign = false;
	}
	
	@Override
	public String getGroup() {
		return group;
	}
	
	@Override
	public ResourceConfig getResourceConfig() {
		return rconfig;
	}

	@Override
	public JobConsumer getJobConsumer() {
		return jobConsumer;
	}
	
	@Override
	public long completed() {
		return completed.get();
	}
	
	@Override
	public void doJob(Runnable task) {
		before();
		try {
			execute(task);
		} catch(JobGroupExecutorBusyException e) {
			logger.warn(new StringBuilder("Job Executor Group(").append(group).append(") is busy."));
			// 进入buffer区
			bufferTask(task);
		}
		after();
	}
	
	@Override
	public final int remainingCapacity() {
		if(isBusy.get()) {
			return 0;
		}
		
		return availableCapacity();
	}
	
	@Override
	public final void shutdown() {
		// TODO
		shutdownNow();
	}

	@Override
	public final void shutdownNow() {
		this.jobAssignThreadStared.set(false);
		this.jobAssignThread.interrupt();
		this.taskBuffer.clear();
		this.isBusy.set(false);
		shutdownExecuteNow();
	}
	
	/*
	 * 
	 */
	protected abstract void execute(Runnable task) throws JobGroupExecutorBusyException;
	
	/*
	 * 
	 */
	protected abstract int availableCapacity();
	
	/*
	 * 
	 */
	protected abstract void shutdownExecute();
	
	/*
	 * 
	 */
	protected abstract void shutdownExecuteNow();
	
	
	/**
	 * 事前处理
	 */
	private void before() {
		if(!jobAssignThreadStared.getAndSet(true)) {
			startJobAssignThread();
		}
	}
	
	/**
	 * 事后处理 
	 */
	private void after() {
		completed.incrementAndGet();
	}
	
	/**
	 * 
	 * @param task
	 */
	private void bufferTask(Runnable task) {
		
		try {
			isBusy.set(true);				// 标识任务组执行器繁忙
			assignLock.lock();
			assignCondition.signalAll();	// 
			taskBuffer.put(task);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			logger.error(e);
		} finally {
			assignLock.unlock();
		}
	}
	
	/**
	 * 
	 */
	private void startJobAssignThread() {
		this.taskBuffer = new LinkedBlockingQueue<Runnable>();		// 先不限制大小，这个缓冲不会过大
		this.jobAssignThread = new Thread(new JobAssignThreadTask(), "Top-Task-Scheduler-Job-Consumer-Group(" + group + ")-Job-Assign-Thread");
		this.jobAssignThread.setDaemon(true);
		this.jobAssignThread.start();
	}
	
	// 
	private class JobAssignThreadTask implements Runnable {

		@Override
		public void run() {
			Runnable task = null;
			while(!Thread.currentThread().isInterrupted()) {
				try {
					assignLock.lock();
					while(!isAssign) {
						// 等待信号
						assignCondition.await();
					}
					
					// 下放任务
					while(true) {
						task = taskBuffer.poll();
						if(task == null) {
							break;
						}
						try {
							execute(task);
						} catch(JobGroupExecutorBusyException e) {
							logger.warn(new StringBuilder("Job Executor Group()").append(group).append(" still busy, try to sleep 1 second"));
							Thread.sleep(1000);
						}
					}
					
					// buffer区清理干净了, 可以拉新任务了
					isBusy.set(false);
					logger.warn(new StringBuilder("Job Executor Group()").append(group).append(" released"));
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					logger.error(e);
				} finally {
					assignLock.unlock();
				}	
			}
		}
	}
}
