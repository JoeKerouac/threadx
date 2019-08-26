package com.joe.threadx.interceptor.threadlocal;

/**
 * 可以继承父线程环境配置的任务
 *
 * @see HierarchicalThreadLocalCallable
 * 
 * @author JoeKerouac
 * @version 2019年08月22日 11:14
 */
public class HierarchicalThreadLocalRunnable extends HierarchicalThreadLocalTask
                                             implements Runnable {

    private Runnable task;

    public HierarchicalThreadLocalRunnable(Runnable task) {
        this.task = task;
    }

    @Override
    public void run() {
        initThreadLocalEnv();
        try {
            task.run();
        } finally {
            destroyThreadLocalEnv();
        }
    }

}
