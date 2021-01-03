package com.joe.threadx.interceptor.mdc;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.MDC;

import com.joe.threadx.TaskInterceptor;
import com.joe.threadx.interceptor.threadlocal.ThreadLocalEnv;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于{@link com.joe.threadx.interceptor.threadlocal.ThreadLocalTaskInterceptor ThreadLocalTaskInterceptor}做MDC内容
 * 跨线程传递，要想启用该内容必须要使用{@link com.joe.threadx.interceptor.threadlocal.ThreadLocalTaskInterceptor
 * ThreadLocalTaskInterceptor}
 *
 * 注意：该插件必须添加到{@link com.joe.threadx.interceptor.threadlocal.ThreadLocalTaskInterceptor
 * ThreadLocalTaskInterceptor}插件之后，否则不会生效
 * 
 * @author JoeKerouac
 * @version 2019年09月17日 16:34
 */
@Slf4j
public class MDCTaskInterceptor implements TaskInterceptor {

    /**
     * MDC上下文在ThreadLocalEnv中的key
     */
    private static final String MDC_CONTEXT_KEY = "MDC_CONTEXT";

    @Override
    public Object beforeAccept(Object task) {
        ThreadLocalEnv.put(MDC_CONTEXT_KEY, getCopyOfContextMap());
        return task;
    }

    @Override
    public Object before(Object task) {
        Object obj = ThreadLocalEnv.get(MDC_CONTEXT_KEY);
        // 防御性校验，防止其他拦截器修改
        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, String> mdcContext = (Map<String, String>)obj;
            mdcContext.forEach(this::putMdc);
        } else if (obj != null) {
            log.warn("当前线程上下文的[MDC_CONTEXT_KEY]数据被修改，MDC拦截器无法生效");
        }
        return task;
    }

    /**
     * 获取当前MDC实现class
     * 
     * @return 默认使用当前线程上下文的ClassLoader加载
     */
    private Class<?> getMdcClass() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            // 默认使用当前线程上下文的ClassLoader加载MDC
            try {
                return loader.loadClass(MDC.class.getName());
            } catch (Exception e) {
                // 忽略该异常
            }
        }
        return MDC.class;
    }

    /**
     * 获取MDC的ContextMap
     * 
     * @return MDC的ContextMap
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getCopyOfContextMap() {
        // 默认使用当前线程上下文的ClassLoader加载MDC
        Class<?> mdcClass = getMdcClass();
        try {
            Method getCopyOfContextMapMethod = mdcClass.getDeclaredMethod("getCopyOfContextMap");
            return (Map<String, String>)getCopyOfContextMapMethod.invoke(mdcClass);
        } catch (Exception e) {
            return MDC.getCopyOfContextMap();
        }
    }

    /**
     * 往MDC中放置内容
     * 
     * @param key
     *            key
     * @param value
     *            value
     */
    public void putMdc(String key, String value) {
        Class<?> mdcClass = getMdcClass();
        try {
            Method putMethod = mdcClass.getDeclaredMethod("put", String.class, String.class);
            putMethod.invoke(mdcClass, key, value);
        } catch (Exception e) {
            MDC.put(key, value);
        }
    }

}
