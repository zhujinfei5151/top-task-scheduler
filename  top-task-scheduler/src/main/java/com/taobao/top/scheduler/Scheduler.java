/**
 * top-task-scheduler
 * 
 */
package com.taobao.top.scheduler;

import com.taobao.top.scheduler.common.Key;
import com.taobao.top.scheduler.common.LifeCycle;

/**
 * <p>
 * 任务调度实例节点
 * </p>
 * 
 * @author <a href="mailto:sihai@taobao.com">sihai</a>
 *
 */
public interface Scheduler extends LifeCycle {

	String DEFAULT_GROUP = Key.DEFAULT_GROUP;	// 默认分组名称
}
