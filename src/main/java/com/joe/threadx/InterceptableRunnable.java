package com.joe.threadx;

import java.util.Objects;

import com.joe.threadx.util.ThreadxUtils;

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
        Objects.requireNonNull(task, "task must not be null");
        Objects.requireNonNull(interceptor, "interceptor must not be null");
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
            ThreadxUtils.processException(e, interceptor, realTask == null ? task : realTask);
        }
    }
}
