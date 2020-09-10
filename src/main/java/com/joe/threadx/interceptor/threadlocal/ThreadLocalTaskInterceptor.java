package com.joe.threadx.interceptor.threadlocal;

import com.joe.threadx.TaskInterceptor;
import com.joe.threadx.exception.ThreadxException;

import java.util.concurrent.Callable;

/**
 * ThreadLocal插件，在任务入池前执行拦截，支持线程环境继承功能，使用了该拦截任务线程将自动复制当前父线程的环境配置
 * <p>
 * 注意：复制的是任务入池时当前线程的环境配置，后续如果环境有更改是不会感知到的，并且环境配置指的是通过{@link ThreadLocalEnv}添加的，自定义的不行
 *
 * @author JoeKerouac
 * @version 2019年08月22日 10:25
 */
public class ThreadLocalTaskInterceptor implements TaskInterceptor {

    @Override
    public Object beforeAccept(Object task) {
        if (task instanceof HierarchicalThreadLocalTask) {
            return task;
        } else if (task instanceof Runnable) {
            return new HierarchicalThreadLocalRunnable((Runnable) task);
        } else if (task instanceof Callable) {
            return new HierarchicalThreadLocalCallable<>((Callable<?>) task);
        } else {
            throw new ThreadxException("未知任务类型：" + task);
        }
    }

    @Override
    public Object before(Object task) {
        ((HierarchicalThreadLocalTask) task).initThreadLocalEnv();
        return task;
    }

    @Override
    public void finalTask(Object task) {
        ((HierarchicalThreadLocalTask) task).destroyThreadLocalEnv();
    }
}
