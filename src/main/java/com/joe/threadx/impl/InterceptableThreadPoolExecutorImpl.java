package com.joe.threadx.impl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import com.joe.threadx.*;

/**
 * 可对线程任务拦截的线程池基础实现
 * 
 * @author JoeKerouac
 * @version 2019年08月20日 15:04
 */
public class InterceptableThreadPoolExecutorImpl implements InterceptableThreadPoolExecutor {

    /**
     * 业务线程池
     */
    private final ThreadPoolExecutor executor;

    /**
     * 拦截器队列
     */
    private CopyOnWriteArrayList<TaskInterceptor> interceptors;

    /**
     * 拦截器代理
     */
    private final TaskInterceptorAdapter interceptorAdapter;

    public InterceptableThreadPoolExecutorImpl(ThreadPoolConfig config) {
        this.executor =
            new ThreadPoolExecutor(config.getCorePoolSize(), config.getMaximumPoolSize(), config.getKeepAliveTime(),
                config.getUnit(), config.getWorkQueue(), config.getThreadFactory(), config.getHandler());
        this.interceptors = new CopyOnWriteArrayList<>();
        this.interceptorAdapter = new TaskInterceptorAdapter(interceptors);
    }

    @Override
    public void addLastTaskInterceptor(TaskInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    @Override
    public void addFirstTaskInterceptor(TaskInterceptor interceptor) {
        interceptors.add(0, interceptor);
    }

    @Override
    public boolean removeAllRunnableInterceptor(TaskInterceptor interceptor) {
        boolean flag = false;
        while (interceptors.remove(interceptor)) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean removeFirstRunnableInterceptor(TaskInterceptor interceptor) {
        return interceptors.remove(interceptor);
    }

    @Override
    public void removeAllRunnableInterceptor() {
        interceptors.clear();
    }

    @Override
    public TaskInterceptor[] getAllRunnableInterceptor() {
        return interceptors.toArray(new TaskInterceptor[0]);
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(wrap(beforeAccept(task)));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return executor.submit(wrap(beforeAccept(task), result), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executor.submit(wrap(beforeAccept(task), null));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executor.invokeAll(wrap(beforeAccept(tasks)));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException {
        return executor.invokeAll(wrap(beforeAccept(tasks)), timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return executor.invokeAny(wrap(beforeAccept(tasks)));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
        return executor.invokeAny(wrap(beforeAccept(tasks)), timeout, unit);
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(wrap(beforeAccept(task), null));
    }

    /**
     * 包装任务，将其包装为InterceptableRunnable类型
     * 
     * @param task
     *            任务
     * @param result
     *            结果
     * @return 包装后的InterceptableRunnable
     */
    private InterceptableRunnable wrap(Runnable task, Object result) {
        // 如果不是InterceptableRunnable类型那么更改为该类型
        if (task instanceof InterceptableRunnable) {
            return (InterceptableRunnable)task;
        } else {
            return new InterceptableRunnable(task, interceptorAdapter, result);
        }
    }

    /**
     * 包装任务，将其包装为InterceptableRunnable类型
     * 
     * @param command
     *            任务
     * @return 包装后的InterceptableRunnable
     */
    private <T> InterceptableCallable<T> wrap(Callable<T> command) {
        // 如果不是InterceptableRunnable类型那么更改为该类型
        if (command instanceof InterceptableCallable) {
            return (InterceptableCallable<T>)command;
        } else {
            return new InterceptableCallable<>(command, interceptorAdapter);
        }
    }

    /**
     * 包装任务，将其包装为InterceptableRunnable类型
     * 
     * @param tasks
     *            任务列表
     * @return 包装后的InterceptableRunnable
     */
    private <T> Collection<InterceptableCallable<T>> wrap(Collection<? extends Callable<T>> tasks) {
        return tasks.stream().map(this::wrap).collect(Collectors.toList());
    }

    /**
     * 任务入池前拦截
     * 
     * @param tasks
     *            任务列表
     * @return 要执行的任务列表
     */
    private <T> Collection<Callable<T>> beforeAccept(Collection<? extends Callable<T>> tasks) {
        return tasks.stream().map(this::beforeAccept).collect(Collectors.toList());
    }

    /**
     * 任务入池前拦截
     * 
     * @param task
     *            任务
     * @return 要执行的任务
     */
    private Runnable beforeAccept(Runnable task) {
        Object result = interceptorAdapter.beforeAccept(task);
        interceptorAdapter.check(task, result);
        return (Runnable)result;
    }

    /**
     * 任务入池前拦截
     * 
     * @param task
     *            任务
     * @return 要执行的任务
     */
    @SuppressWarnings("unchecked")
    private <T> Callable<T> beforeAccept(Callable<T> task) {
        Object result = interceptorAdapter.beforeAccept(task);
        interceptorAdapter.check(task, result);
        return (Callable<T>)result;
    }
}
