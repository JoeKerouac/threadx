package com.joe.threadx;

import java.util.function.Consumer;

/**
 * 测试基类
 *
 * @author JoeKerouac
 * @version 2019年08月27日 15:18
 */
public abstract class BaseTest {

    protected void execEmpty(InterceptableThreadPoolExecutor executor) {
        // 执行一个空任务触发拦截器
        executor.execute(() -> {

        });
    }

    /**
     * 执行指定操作，每次执行都会为该操作提供一个InterceptableThreadPoolExecutor
     * 
     * @param consumer
     *            待执行的操作
     */
    protected void run(Consumer<InterceptableThreadPoolExecutor> consumer) {
        InterceptableThreadPoolExecutor executor =
            InterceptableThreadPoolExecutorFactory.build(InterceptableThreadPoolExecutorFactory.PoolType.Calc);
        consumer.accept(executor);
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
