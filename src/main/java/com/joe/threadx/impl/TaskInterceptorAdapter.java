package com.joe.threadx.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import com.joe.threadx.TaskInterceptor;
import com.joe.threadx.exception.UncaughtException;
import com.joe.utils.common.Assert;

/**
 * RunnableInterceptor适配器
 *
 * @author JoeKerouac
 * @version 2019年08月20日 15:30
 */
public class TaskInterceptorAdapter implements TaskInterceptor {

    /**
     * 实际的RunnableInterceptor队列
     */
    private List<TaskInterceptor> interceptors;

    /**
     * 构造器
     * @param interceptors 实现会对该队列进行遍历，请保证该List是线程安全的或者保证不会并发
     */
    public TaskInterceptorAdapter(List<TaskInterceptor> interceptors) {
        Assert.notNull(interceptors, "interceptors must not be null");
        this.interceptors = interceptors;
    }

    @Override
    public Object beforeAccept(Object task) {
        return invoke(task, TaskInterceptor::beforeAccept);
    }

    @Override
    public Object before(Object task) {
        return invoke(task, TaskInterceptor::before);
    }

    /**
     * 执行函数
     * @param task 任务
     * @param function 转换函数
     * @return 结果
     */
    private Object invoke(Object task, BiFunction<TaskInterceptor, Object, Object> function) {
        AtomicReference<Object> taskRef = new AtomicReference<>();
        taskRef.set(task);
        interceptors.forEach(interceptor -> {
            Object result = function.apply(interceptor, taskRef.get());
            interceptor.check(task, result);
            taskRef.set(result);
        });
        return taskRef.get();
    }

    @Override
    public void after(Object task, Object result) {
        interceptors.forEach(interceptor -> interceptor.after(task, result));
    }

    @Override
    public RuntimeException exception(Object task, Throwable e) {
        AtomicReference<Throwable> reference = new AtomicReference<>();
        reference.set(e);
        interceptors.forEach(interceptor -> {
            if (reference.get() != null) {
                reference.set(interceptor.exception(task, reference.get()));
            }
        });
        Throwable result = reference.get();
        if (result instanceof RuntimeException) {
            return (RuntimeException) result;
        } else {
            return new UncaughtException(result);
        }
    }
}