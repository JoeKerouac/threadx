package com.joe.threadx;

import com.joe.threadx.impl.InterceptableThreadPoolExecutorImpl;
import com.joe.threadx.interceptor.threadlocal.ThreadLocalTaskInterceptor;
import com.joe.utils.common.Assert;

/**
 * InterceptableThreadPoolExecutor工厂
 * 
 * @author JoeKerouac
 * @version 2019年08月21日 21:11
 */
public class InterceptableThreadPoolExecutorFactory {

    /**
     * 构建指定类型的线程池
     * @param type 线程池类型
     * @return 线程池
     */
    public static InterceptableThreadPoolExecutor build(PoolType type) {
        switch (type) {
            case IO:
                return build(ThreadxConst.IO_THREAD_POO_CONFIG_SUPPLIER.get());
            case Calc:
                return build(ThreadxConst.CALC_THREAD_POO_CONFIG_SUPPLIER.get());
            default:
                throw new IllegalArgumentException(
                    String.format("内部异常，未知线程池类型[%s]", type.toString()));
        }
    }

    /**
     * 构建线程池
     * @param config 线程池配置
     * @return 线程池
     */
    public static InterceptableThreadPoolExecutor build(ThreadPoolConfig config) {
        Assert.notNull(config, "config must not be null");
        InterceptableThreadPoolExecutor executor = new InterceptableThreadPoolExecutorImpl(config);
        executor.addFirstTaskInterceptor(new ThreadLocalTaskInterceptor());
        return executor;
    }

    public enum PoolType {
                          IO, Calc
    }
}
