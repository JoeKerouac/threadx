package com.joe.threadx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

/**
 * 线程池测试
 * 
 * @author JoeKerouac
 * @version 2019年08月21日 21:10
 */
public class InterceptableThreadPoolExecutorTest extends BaseTest {

    @Test
    public void doTestAddLastTaskInterceptor() {
        run(executor -> {
            try {
                CountDownLatch latch = new CountDownLatch(1);
                executor.addLastTaskInterceptor(new TaskInterceptor() {
                    @Override
                    public void after(Object task, Object result) {
                        latch.countDown();
                    }
                });

                execEmpty(executor);

                Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void doTestAddFirstTaskInterceptor() {
        run(executor -> {
            try {
                CountDownLatch latch = new CountDownLatch(1);
                executor.addFirstTaskInterceptor(new TaskInterceptor() {
                    @Override
                    public void after(Object task, Object result) {
                        latch.countDown();
                    }
                });

                execEmpty(executor);

                Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 测试TaskInterceptor执行顺序，应该是按照添加顺序执行
     */
    @Test
    public void doTestAddTaskInterceptor() {
        run(executor -> {
            try {
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

                execEmpty(executor);

                Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
                Assert.assertTrue(flag.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 测试删除拦截器
     */
    @Test
    public void doTestRemoveRunnableInterceptor() {
        AtomicBoolean flag = new AtomicBoolean(true);

        TaskInterceptor interceptor = new TaskInterceptor() {
            @Override
            public void after(Object task, Object result) {
                flag.set(false);
            }
        };

        // removeAllRunnableInterceptor
        run(executor -> {
            try {
                flag.set(true);
                executor.addLastTaskInterceptor(interceptor);

                executor.removeAllRunnableInterceptor(interceptor);

                execEmpty(executor);

                executor.shutdown();

                // 首先要确保线程任务执行完毕
                Assert.assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
                // 如果没有被移除这里将会是false
                Assert.assertTrue(flag.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // removeAllRunnableInterceptor
        run(executor -> {
            try {
                flag.set(true);
                executor.addLastTaskInterceptor(interceptor);

                executor.removeAllRunnableInterceptor();

                execEmpty(executor);

                executor.shutdown();

                // 首先要确保线程任务执行完毕
                Assert.assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
                // 如果没有被移除这里将会是false
                Assert.assertTrue(flag.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // removeFirstRunnableInterceptor
        run(executor -> {
            try {
                flag.set(true);
                executor.addLastTaskInterceptor(interceptor);

                executor.removeFirstRunnableInterceptor(interceptor);

                execEmpty(executor);

                executor.shutdown();

                // 首先要确保线程任务执行完毕
                Assert.assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
                // 如果没有被移除这里将会是false
                Assert.assertTrue(flag.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
