package com.taobao.top.scheduler.job.executor;

import com.taobao.top.scheduler.exception.SchedulerException;

public class JobGroupExecutorBusyException extends SchedulerException {

	public JobGroupExecutorBusyException() {
        super();
    }

    public JobGroupExecutorBusyException(String msg) {
        super(msg);
    }

    public JobGroupExecutorBusyException(Throwable cause) {
        super(cause);
    }

    public JobGroupExecutorBusyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
