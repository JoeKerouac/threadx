package com.joe.threadx.interceptor.threadlocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.joe.threadx.util.ThreadxUtils;

/**
 * 线程上下文环境，ThreadLocal包装
 *
 * @see HierarchicalThreadLocalRunnable
 * @see HierarchicalThreadLocalCallable
 *
 * @author JoeKerouac
 * @version 2019年08月22日 09:36
 */
public class ThreadLocalEnv {

    /**
     * 实际容器
     */
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 销毁清空当前线程上下文，重复调用幂等返回
     */
    public static void destory() {
        THREAD_LOCAL.remove();
    }

    /**
     * 获取当前线程上下文所有环境配置
     * 
     * @return 当前线程上下文所有环境配置，可能为null
     */
    public static Map<String, Object> getAll() {
        return getAndInit();
    }

    /**
     * 将配置全部放入当前线程环境配置
     * 
     * @param env
     *            配置
     */
    public static void putAll(Map<String, Object> env) {
        Objects.requireNonNull(env, "env must not be null");
        getAndInit().putAll(env);
    }

    /**
     * 往当前线程环境放置一个配置
     * 
     * @param key
     *            配置的key，不能为空
     * @param value
     *            配置的value
     * @param <T>
     *            配置value的类型
     * @return 当前key对应的value，如果当前没有值则为null
     */
    @SuppressWarnings("unchecked")
    public static <T> T put(String key, T value) {
        ThreadxUtils.assertTrue(key != null && !key.isEmpty(), "key must not be empty");
        return (T)getAndInit().put(key, value);
    }

    /**
     * 获取当前线程环境中的配置
     * 
     * @param key
     *            要获取的配置的key，不能为空
     * @param <T>
     *            配置value类型
     * @return 配置value
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        ThreadxUtils.assertTrue(key != null && !key.isEmpty(), "key must not be empty");
        Map<String, Object> map = getAndInit();
        return map == null ? null : (T)map.get(key);
    }

    /**
     * 获取当前线程环境，重复调用幂等返回，如果当前线程上下文未初始化则初始化
     *
     * @return 初始化后的map
     */
    private static Map<String, Object> getAndInit() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new HashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }
}
