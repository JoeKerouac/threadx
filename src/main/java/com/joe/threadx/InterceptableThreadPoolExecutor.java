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
     * 移除全部指定拦截器
     * @param interceptor 要移除的拦截器
     * @return 移除成功返回true，不存在时返回false
     */
    boolean removeAllRunnableInterceptor(TaskInterceptor interceptor);

    /**
     * 移除第一个匹配到的指定拦截器
     * @param interceptor 要移除的拦截器
     * @return 移除成功返回true，不存在时返回false
     */
    boolean removeFirstRunnableInterceptor(TaskInterceptor interceptor);

    /**
     * 移除全部拦截器
     */
    void removeAllRunnableInterceptor();

    /**
     * 获取当前线程池任务所有拦截器
     *
     * @return 当前线程池任务所有的拦截器（该数组为copy出来的副本）
     */
    TaskInterceptor[] getAllRunnableInterceptor();
}
