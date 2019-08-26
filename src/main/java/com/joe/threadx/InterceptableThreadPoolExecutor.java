package com.joe.threadx;

import java.util.concurrent.ExecutorService;

/**
 * 可对线程任务拦截的线程池
 *
 * @author JoeKerouac
 * @version 2019年08月20日 14:02
 */
public interface InterceptableThreadPoolExecutor extends ExecutorService {

    /**
     * 将指定拦截器添加到当前拦截器的队尾
     *
     * @param interceptor 拦截器
     */
    void addLastTaskInterceptor(TaskInterceptor interceptor);

    /**
     * 将指定拦截器添加到当前拦截器的队头
     *
     * @param interceptor 拦截器
     */
    void addFirstTaskInterceptor(TaskInterceptor interceptor);

    /**
     * 移除指定拦截器
     * @param interceptor 要移除的拦截器
     * @return 如果要移除的拦截器存在，那么移除并返回，否则返回null
     */
    TaskInterceptor removeRunnableInterceptor(TaskInterceptor interceptor);

    /**
     * 获取当前线程池任务所有拦截器
     *
     * @return 当前线程池任务所有的拦截器（该数组为copy出来的副本）
     */
    TaskInterceptor[] getAllRunnableInterceptor();
}
