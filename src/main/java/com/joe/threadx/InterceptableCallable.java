package com.joe.threadx;

import java.util.Objects;
import java.util.concurrent.Callable;

import com.joe.threadx.util.ThreadxUtils;

/**
 * 可拦截的线程任务
 *
 * @see InterceptableRunnable
 *
 * @author JoeKerouac
 * @version 2019年08月21日 17:25
 */
public class InterceptableCallable<T> implements Callable<T> {
    /**
     * 任务拦截器
     */
    private TaskInterceptor interceptor;

    /**
     * 实际要运行的任务
     */
    private Callable<T>     task;

    /**
     * 构造器
     * @param task 实际任务，不能为空
     * @param interceptor 拦截器，不能为空
     */
    public InterceptableCallable(Callable<T> task, TaskInterceptor interceptor) {
        Objects.requireNonNull(task, "task must not be null");
        Objects.requireNonNull(interceptor, "interceptor must not be null");
        this.task = task;
        this.interceptor = interceptor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T call() {
        Object realTask = null;
        try {
            realTask = interceptor.before(task);
            interceptor.check(task, realTask);
            T result = ((Callable<T>) realTask).call();
            interceptor.after(realTask, result);
            return result;
        } catch (Throwable e) {
            ThreadxUtils.processException(e, interceptor, realTask == null ? task : realTask);
        }
        return null;
    }
}
