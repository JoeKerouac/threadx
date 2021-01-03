package com.joe.threadx.interceptor.threadlocal;

import java.util.concurrent.Callable;

/**
 * 可以继承父线程环境配置的任务
 * 
 * @see HierarchicalThreadLocalRunnable
 * 
 * @author JoeKerouac
 * @version 2019年08月22日 11:16
 */
class HierarchicalThreadLocalCallable<V> extends HierarchicalThreadLocalTask implements ThreadLocalTask, Callable<V> {

    private Callable<V> task;

    HierarchicalThreadLocalCallable(Callable<V> task) {
        super(task);
        this.task = task;
    }

    @Override
    public V call() throws Exception {
        return task.call();
    }
}
