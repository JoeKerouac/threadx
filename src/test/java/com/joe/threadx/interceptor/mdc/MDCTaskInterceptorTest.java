package com.joe.threadx.interceptor.mdc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.MDC;

import com.joe.threadx.BaseTest;

/**
 * MDC测试
 *
 * @author JoeKerouac
 * @version 2019年09月17日 16:45
 */
public class MDCTaskInterceptorTest extends BaseTest {

    private static final String KEY = "test-key";

    private static final String VALUE = "test-value";

    /**
     * 测试MDC跨线程传递
     */
    @Test
    public void doTest() {
        run(executor -> {
            try {
                executor.addLastTaskInterceptor(new MDCTaskInterceptor());

                MDC.put(KEY, VALUE);

                AtomicBoolean flag = new AtomicBoolean(false);
                CountDownLatch latch = new CountDownLatch(1);
                executor.execute(() -> {
                    System.out.println(MDC.get(KEY));
                    flag.set(VALUE.equalsIgnoreCase(MDC.get(KEY)));
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
