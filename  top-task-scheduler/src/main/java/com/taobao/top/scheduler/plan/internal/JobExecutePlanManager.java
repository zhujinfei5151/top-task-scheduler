/**
 * top-task-scheduler
 * 
 */

package com.taobao.top.scheduler.plan.internal;

import java.util.Date;

import com.taobao.top.scheduler.common.LifeCycle;
import com.taobao.top.scheduler.exception.SchedulerException;
import com.taobao.top.scheduler.plan.JobExecutePlan;

/**
 * <p>
 * 任务触发接口
 * </p>
 * 
 * @author <a href="mailto:sihai@taobao.com">sihai</a>
 *
 */
public interface JobExecutePlanManager extends LifeCycle, PlanFiredObserver, JobExecutePlanSubject {
	
	String JOB_EXECUTE_PLAN_MGR_QUARTZ_GROUP_NAME = "Top-Task-Scheduler-Quartz-Group";
	
	/**
	 * <p>
	 * 添加触发器管理的任务
	 * </p>
	 * @param  plan
	 * @param  priority
	 */
	void addPlan(JobExecutePlan plan) throws SchedulerException;
	
	/**
	 * 移除触发器管理的任务
	 * @param  plan
	 */
	void removePlan(JobExecutePlan plan);
	
	/**
	 * 查询任务的执行计划是否会再次执行
	 * @param plan
	 * @return
	 */
	boolean mayExecuteAgain(JobExecutePlan plan);
	
	/**
	 * 获取执行计划下一次执行时间
	 * @return
	 */
	Date nextExecuteTime(JobExecutePlan plan);
}
