package com.joe.threadx.impl;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.joe.threadx.TaskInterceptor;
import com.joe.threadx.exception.UncaughtException;

/**
 * RunnableInterceptor适配器
 *
 * @author JoeKerouac
 * @version 2019年08月20日 15:30
 */
public class TaskInterceptorAdapter implements TaskInterceptor {

    /**
     * 前置执行队列
     */
    private List<TaskInterceptor> interceptors;

    /**
     * 构造器
     *
     * @param interceptors 实现会对该队列进行遍历，请保证该List是线程安全的或者保证不会并发
     */
    public TaskInterceptorAdapter(List<TaskInterceptor> interceptors) {
        Objects.requireNonNull(interceptors, "beforeInterceptors must not be null");
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
     * 执行函数，将任务进行转换
     *
     * @param task     任务
     * @param function 转换函数，提供TaskInterceptor和任务，将任务转换为新的任务
     * @return 结果
     */
    private Object invoke(Object task, BiFunction<TaskInterceptor, Object, Object> function) {
        AtomicReference<Object> taskRef = new AtomicReference<>();
        taskRef.set(task);
        beforeInterceptor(interceptor -> {
            Object result = function.apply(interceptor, taskRef.get());
            interceptor.check(task, result);
            taskRef.set(result);
        });
        return taskRef.get();
    }

    @Override
    public void after(Object task, Object result) {
        afterInterceptor(interceptor -> interceptor.after(task, result));
    }

    @Override
    public RuntimeException exception(Object task, Throwable e) {
        AtomicReference<Throwable> reference = new AtomicReference<>();
        reference.set(e);
        afterInterceptor(interceptor -> {
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

    @Override
    public void finalTask(Object task) {
        afterInterceptor(interceptor -> interceptor.finalTask(task));
    }

    /**
     * 前置执行
     * @param consumer 执行器
     */
    private void beforeInterceptor(Consumer<TaskInterceptor> consumer) {
        this.interceptors.forEach(consumer);
    }

    /**
     * 后置执行
     * @param consumer 执行器
     */
    private void afterInterceptor(Consumer<TaskInterceptor> consumer) {
        for (int i = (this.interceptors.size() - 1); i >= 0; i--) {
            consumer.accept(this.interceptors.get(i));
        }
    }
}
