package com.joe.threadx.interceptor.threadlocal;

/**
 * 可以继承父线程环境配置的任务
 *
 * @see HierarchicalThreadLocalCallable
 * 
 * @author JoeKerouac
 * @version 2019年08月22日 11:14
 */
class HierarchicalThreadLocalRunnable extends HierarchicalThreadLocalTask
                                             implements ThreadLocalTask, Runnable {

    private Runnable task;

    HierarchicalThreadLocalRunnable(Runnable task) {
        super(task);
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
