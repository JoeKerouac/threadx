package com.joe.threadx;

import java.util.Objects;

import com.joe.threadx.impl.InterceptableThreadPoolExecutorImpl;
import com.joe.threadx.interceptor.threadlocal.ThreadLocalTaskInterceptor;

/**
 * InterceptableThreadPoolExecutor工厂
 * 
 * @author JoeKerouac
 * @version 2019年08月21日 21:11
 */
public class InterceptableThreadPoolExecutorFactory {

    /**
     * 构建指定类型的线程池
     * 
     * @param type
     *            线程池类型
     * @param supportThreadLocalExt
     *            是否支持{@link ThreadLocalTaskInterceptor}扩展，true表示支持
     * @return 线程池
     */
    public static InterceptableThreadPoolExecutor build(PoolType type, boolean supportThreadLocalExt) {
        ThreadPoolConfig config;
        switch (type) {
            case IO:
                config = ThreadxConst.IO_THREAD_POO_CONFIG_SUPPLIER.get();
                break;
            case Calc:
                config = ThreadxConst.CALC_THREAD_POO_CONFIG_SUPPLIER.get();
                break;
            default:
                throw new IllegalArgumentException(String.format("内部异常，未知线程池类型[%s]", type.toString()));
        }
        return build(config, supportThreadLocalExt);
    }

    /**
     * 构建指定类型的线程池，支持ThreadLocal插件，插件详见{@link ThreadLocalTaskInterceptor}
     * 
     * @param type
     *            线程池类型
     * @return 线程池
     */
    public static InterceptableThreadPoolExecutor build(PoolType type) {
        return build(type, true);
    }

    /**
     * 构建线程池
     * 
     * @param config
     *            线程池配置
     * @param supportThreadLocalExt
     *            是否支持{@link ThreadLocalTaskInterceptor}扩展
     * @return 线程池
     */
    public static InterceptableThreadPoolExecutor build(ThreadPoolConfig config, boolean supportThreadLocalExt) {
        Objects.requireNonNull(config, "config must not be null");
        InterceptableThreadPoolExecutor executor = new InterceptableThreadPoolExecutorImpl(config);
        if (supportThreadLocalExt) {
            executor.addFirstTaskInterceptor(new ThreadLocalTaskInterceptor());
        }
        return executor;
    }

    public enum PoolType {
        IO, Calc
    }
}
