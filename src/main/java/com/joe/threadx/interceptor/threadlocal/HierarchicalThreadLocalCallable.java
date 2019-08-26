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
public class HierarchicalThreadLocalCallable<V> extends HierarchicalThreadLocalTask
                                            implements Callable<V> {

    private Callable<V> task;

    public HierarchicalThreadLocalCallable(Callable<V> task) {
        this.task = task;
    }

    @Override
    public V call() throws Exception {
        initThreadLocalEnv();
        try {
            return task.call();
        } finally {
            destroyThreadLocalEnv();
        }
    }
}
