package com.joe.threadx.interceptor.threadlocal;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;

import com.joe.threadx.BaseTest;

/**
 * ThreadLocal继承插件测试
 * 
 * @author JoeKerouac
 * @version 2019年08月27日 15:17
 */
public class ThreadLocalTaskInterceptorTest extends BaseTest {

    private static final String KEY = "test-key";

    private static final String VALUE = "test-value";

    /**
     * 测试内置的ThreadLocal插件，默认提供该插件
     */
    @Test
    public void doTest() {
        run(executor -> {
            try {
                ThreadLocalEnv.put(KEY, VALUE);

                AtomicBoolean flag = new AtomicBoolean(false);
                CountDownLatch latch = new CountDownLatch(1);
                executor.execute(() -> {
                    flag.set(VALUE.equalsIgnoreCase(ThreadLocalEnv.get(KEY)));
                    latch.countDown();
                });
                Assert.assertTrue(latch.await(1, TimeUnit.SECONDS));
                Assert.assertTrue(flag.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
