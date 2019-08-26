package com.joe.threadx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.joe.threadx.interceptor.threadlocal.ThreadLocalEnv;
import com.joe.utils.common.string.StringUtils;

/**
 * 线程池测试
 * 
 * @author JoeKerouac
 * @version 2019年08月21日 21:10
 */
public class InterceptableThreadPoolExecutorTest {

    private static final String                                       KEY                 = "test-key";

    private static final String                                       VALUE               = "test-value";

    private static final ThreadLocal<InterceptableThreadPoolExecutor> executorThreadLocal = new ThreadLocal<>();

    /**
     * 测试内置的ThreadLocal插件
     * @throws Exception
     */
    @Test
    public void doTest() throws Exception {
        InterceptableThreadPoolExecutor executor = executorThreadLocal.get();

        ThreadLocalEnv.put(KEY, VALUE);

        AtomicBoolean flag = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);
        executor.execute(() -> {
            flag.set(StringUtils.equals(VALUE, ThreadLocalEnv.get(KEY)));
            latch.countDown();
        });
        Assert.assertTrue(latch.await(1, TimeUnit.SECONDS));
        Assert.assertTrue(flag.get());
    }

    @Test
    public void doTestAddLastTaskInterceptor() throws Exception {
        InterceptableThreadPoolExecutor executor = executorThreadLocal.get();

        CountDownLatch latch = new CountDownLatch(1);
        executor.addLastTaskInterceptor(new TaskInterceptor() {
            @Override
            public void after(Object task, Object result) {
                latch.countDown();
            }
        });
        executor.execute(() -> {

        });

        Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void doTestAddFirstTaskInterceptor() throws Exception {
        InterceptableThreadPoolExecutor executor = executorThreadLocal.get();

        CountDownLatch latch = new CountDownLatch(1);
        executor.addFirstTaskInterceptor(new TaskInterceptor() {
            @Override
            public void after(Object task, Object result) {
                latch.countDown();
            }
        });
        executor.execute(() -> {

        });

        Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    /**
     * 测试TaskInterceptor执行顺序，应该是按照添加顺序执行
     * @throws Exception
     */
    @Test
    public void doTestAddTaskInterceptor() throws Exception {
        InterceptableThreadPoolExecutor executor = executorThreadLocal.get();

        CountDownLatch latch = new CountDownLatch(2);

        AtomicBoolean flag = new AtomicBoolean(true);
        AtomicInteger counter = new AtomicInteger();

        // 顺序添加，按照添加顺序执行
        executor.addLastTaskInterceptor(new TaskInterceptor() {
            @Override
            public void after(Object task, Object result) {
                flag.set(flag.get() && counter.getAndIncrement() == 0);
                latch.countDown();
            }
        });

        executor.addLastTaskInterceptor(new TaskInterceptor() {
            @Override
            public void after(Object task, Object result) {
                flag.set(flag.get() && counter.getAndIncrement() == 1);
                latch.countDown();
            }
        });
        executor.execute(() -> {

        });

        Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
        Assert.assertTrue(flag.get());
    }

    @Before
    public void init() {
        executorThreadLocal.set(InterceptableThreadPoolExecutorFactory
            .build(InterceptableThreadPoolExecutorFactory.PoolType.Calc));
        ThreadLocalEnv.init();
    }

    @After
    public void after() {
        InterceptableThreadPoolExecutor executor = executorThreadLocal.get();
        executorThreadLocal.remove();
        executor.shutdown();
    }
}
