package com.joe.threadx.interceptor.threadlocal;

import java.util.Map;
import java.util.Objects;

/**
 * 可从父线程继承环境上下文的任务
 * 
 * @author JoeKerouac
 * @version 2019年08月22日 11:16
 */
abstract class HierarchicalThreadLocalTask implements ThreadLocalTask {

    private Map<String, Object> threadLocalEnv;

    /**
     * 实际任务
     */
    private Object              task;

    HierarchicalThreadLocalTask(Object task) {
        Objects.requireNonNull(task, "执行任务不能为空");
        this.task = task;
        threadLocalEnv = ThreadLocalEnv.getAll();
    }

    void initThreadLocalEnv() {
        if (threadLocalEnv != null) {
            ThreadLocalEnv.putAll(threadLocalEnv);
        }
    }

    void destroyThreadLocalEnv() {
        ThreadLocalEnv.destory();
    }

    @Override
    public Object getTask() {
        return task;
    }
}
