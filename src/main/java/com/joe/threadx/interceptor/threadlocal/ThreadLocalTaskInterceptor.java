package com.joe.threadx.interceptor.threadlocal;

import com.joe.threadx.TaskInterceptor;
import com.joe.threadx.exception.ThreadxException;

import java.util.concurrent.Callable;

/**
 * 在任务入池前执行拦截，支持线程环境继承功能，使用了该拦截任务线程将自动复制父线程的环境配置
 * 
 * @author JoeKerouac
 * @version 2019年08月22日 10:25
 */
public class ThreadLocalTaskInterceptor implements TaskInterceptor {

    @Override
    public Object beforeAccept(Object task) {
        if (task instanceof Runnable) {
            if (!(task instanceof HierarchicalThreadLocalRunnable)) {
                task = new HierarchicalThreadLocalRunnable((Runnable) task);
            }
        } else if (task instanceof Callable) {
            if (!(task instanceof HierarchicalThreadLocalCallable)) {
                task = new HierarchicalThreadLocalCallable<>((Callable<?>) task);
            }
        } else {
            throw new ThreadxException("未知任务类型：" + task);
        }
        return task;
    }
}
