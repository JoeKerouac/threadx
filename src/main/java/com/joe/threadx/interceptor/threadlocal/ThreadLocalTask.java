package com.joe.threadx.interceptor.threadlocal;

/**
 * ThreadLocal任务接口
 *
 * @author JoeKerouac
 * @version 2019年08月27日 15:22
 */
public interface ThreadLocalTask {

    /**
     * 获取实际要执行的任务
     * @return 实际要执行的任务，{@link Runnable}或者{@link java.util.concurrent.Callable}的子类
     */
    Object getTask();
}
