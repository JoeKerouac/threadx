package com.joe.threadx.interceptor.mdc;

import java.util.Map;

import org.slf4j.MDC;

import com.joe.threadx.TaskInterceptor;
import com.joe.threadx.interceptor.threadlocal.ThreadLocalEnv;

/**
 * 基于{@link com.joe.threadx.interceptor.threadlocal.ThreadLocalTaskInterceptor ThreadLocalTaskInterceptor}做MDC内容
 * 跨线程传递，要想启用该内容必须要使用{@link com.joe.threadx.interceptor.threadlocal.ThreadLocalTaskInterceptor ThreadLocalTaskInterceptor}
 *
 * 注意：该插件必须添加到{@link com.joe.threadx.interceptor.threadlocal.ThreadLocalTaskInterceptor ThreadLocalTaskInterceptor}插件之后，否则不会生效
 * 
 * @author JoeKerouac
 * @version 2019年09月17日 16:34
 */
public class MDCTaskInterceptor implements TaskInterceptor {

    /**
     * MDC上下文在ThreadLocalEnv中的key
     */
    private static final String KEY = "MDC_CONTEXT";

    @Override
    public Object beforeAccept(Object task) {
        ThreadLocalEnv.put(KEY, MDC.getCopyOfContextMap());
        return task;
    }

    @Override
    public Object before(Object task) {
        Object obj = ThreadLocalEnv.get(KEY);
        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, String> mdcContext = (Map<String, String>) obj;
            mdcContext.forEach(MDC::put);
        }
        return task;
    }
}
