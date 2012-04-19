/**
 * top-task-scheduler
 *  
 */

package com.taobao.top.scheduler.job.internal.impl;

import java.util.LinkedList;
import java.util.List;

import com.taobao.top.scheduler.job.JobStatusEnum;
import com.taobao.top.scheduler.job.impl.JobKey;
import com.taobao.top.scheduler.job.impl.MultiJobResultJobContent;
import com.taobao.top.scheduler.job.internal.Job;
import com.taobao.top.waverider.SlaveWorker;

/**
 * <p>
 * 依赖job的实现，一个job依赖多个job的jobRestul作为自己的jobContent
 * </p>
 * 
 * @author <a href="mailto:sihai@taobao.com">sihai</a>
 * 
 */
public class DependencyJob extends DefaultJob implements Job {

	private static final long serialVersionUID = 8626640186630978090L;

	private List<com.taobao.top.scheduler.job.Job> dependedJobList = new LinkedList<com.taobao.top.scheduler.job.Job>();

	public DependencyJob(Long id , JobKey key) {
		super(id, key);
	}
	
	@Override
	public void dispatch(SlaveWorker worker) {
		super.dispatch(worker);

		MultiJobResultJobContent jobContent = new MultiJobResultJobContent();
		for (com.taobao.top.scheduler.job.Job job : dependedJobList) {
			jobContent.add(job);
		}

		setJobContent(jobContent);
	}

	@Override
	public void addDependedJob(com.taobao.top.scheduler.job.Job job) {
		dependedJobList.add(job);
	}

	@Override
	public boolean isDependencySatisfied() {
		for (com.taobao.top.scheduler.job.Job job : dependedJobList) {
			if (job.getStatus() != JobStatusEnum.JOB_STATUS_COMPLETED) {
				return false;
			}
		}

		return true;
	}
}
