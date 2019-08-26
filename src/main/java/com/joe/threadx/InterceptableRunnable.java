package com.joe.threadx;

import com.joe.utils.common.Assert;

/**
 * 可拦截的线程任务
 *
 * @see InterceptableCallable
 *
 * @author JoeKerouac
 * @version 2019年08月20日 14:04
 */
public class InterceptableRunnable implements Runnable {

    /**
     * 任务拦截器
     */
    private final TaskInterceptor interceptor;

    /**
     * 实际要运行的任务
     */
    private final Runnable        task;

    /**
     * 用户提交的结果，详细见{@link InterceptableThreadPoolExecutor#submit(Runnable, Object)}
     */
    private final Object          result;

    /**
     * 构造器
     * @param task 实际任务，不能为空
     * @param interceptor 拦截器，不能为空
     */
    public InterceptableRunnable(Runnable task, TaskInterceptor interceptor, Object result) {
        Assert.notNull(task, "task must not be null");
        Assert.notNull(interceptor, "interceptor must not be null");
        this.task = task;
        this.interceptor = interceptor;
        this.result = result;
    }

    @Override
    public void run() {
        Object realTask = null;
        try {
            realTask = interceptor.before(task);
            interceptor.check(task, realTask);
            ((Runnable) realTask).run();
            interceptor.after(realTask, result);
        } catch (Throwable e) {
            RuntimeException result = interceptor.exception(realTask == null ? task : realTask, e);
            if (result != null) {
                throw result;
            }
        }
    }
}
