package com.joe.threadx;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 常量
 *
 * @author JoeKerouac
 * @version 2019年08月22日 20:02
 */
public class ThreadxConst {

    /**
     * IO线程池配置函数获取
     */
    public static final Supplier<ThreadPoolConfig> IO_THREAD_POO_CONFIG_SUPPLIER;

    /**
     * CPU密集型任务线程池配置
     */
    public static final Supplier<ThreadPoolConfig> CALC_THREAD_POO_CONFIG_SUPPLIER;

    /**
     * 默认任务队列堆积长度
     */
    private static final int                       DEFAULT_QUEUE_SIZE = 100000;

    static {
        IO_THREAD_POO_CONFIG_SUPPLIER = () -> {
            String format = "threadx-io-%d";
            //线程工厂
            ThreadFactory factory = new ThreadFactory() {
                AtomicInteger counter = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format(format, counter.getAndAdd(1)));
                }
            };
            int corePoolSize = Runtime.getRuntime().availableProcessors() * 10;
            int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 20;
            long keepAliveTime = 3;
            TimeUnit unit = TimeUnit.MINUTES;
            BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE);
            return new ThreadPoolConfig(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue, factory, new ThreadPoolExecutor.AbortPolicy());
        };

        CALC_THREAD_POO_CONFIG_SUPPLIER = () -> {
            String format = "threadx-calc-%d";
            //线程工厂
            ThreadFactory factory = new ThreadFactory() {
                AtomicInteger counter = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format(format, counter.getAndAdd(1)));
                }
            };
            int corePoolSize, maximumPoolSize;
            // CPU密集型任务线程不应该太多，过多线程只会带来线程上下文切换开销，并不太会带来性能提升
            corePoolSize = maximumPoolSize = Runtime.getRuntime().availableProcessors() + 1;
            long keepAliveTime = 3;
            TimeUnit unit = TimeUnit.MINUTES;
            BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE);
            return new ThreadPoolConfig(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue, factory, new ThreadPoolExecutor.AbortPolicy());
        };
    }
}
