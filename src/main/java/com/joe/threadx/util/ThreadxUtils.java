package com.joe.threadx.util;

import com.joe.threadx.TaskInterceptor;

/**
 * @author JoeKerouac
 * @version 2019年09月17日 14:27
 */
public class ThreadxUtils {

    /**
     * 断言参数true
     *
     * @param flag 参数
     * @param msg 为false时的异常提示
     */
    public static void assertTrue(boolean flag, String msg) {
        if (!flag) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 处理运行时异常
     * @param e 异常
     * @param interceptor 拦截器
     * @param task 任务
     */
    public static void processException(Throwable e, TaskInterceptor interceptor, Object task) {
        RuntimeException result = interceptor.exception(task, e);
        if (result != null) {
            throw result;
        }
    }
}
