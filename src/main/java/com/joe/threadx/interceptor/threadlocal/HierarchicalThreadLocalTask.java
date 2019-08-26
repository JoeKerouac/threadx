package com.joe.threadx.interceptor.threadlocal;

import java.util.Map;

/**
 * 可从父线程继承环境上下文的任务
 * 
 * @author JoeKerouac
 * @version 2019年08月22日 11:16
 */
public abstract class HierarchicalThreadLocalTask {

    private Map<String, Object> threadLocalEnv;

    protected HierarchicalThreadLocalTask() {
        threadLocalEnv = ThreadLocalEnv.getAll();
    }

    protected void initThreadLocalEnv() {
        if (threadLocalEnv != null) {
            ThreadLocalEnv.putAll(threadLocalEnv);
        }
    }

    protected void destroyThreadLocalEnv() {
        ThreadLocalEnv.destory();
    }
}
