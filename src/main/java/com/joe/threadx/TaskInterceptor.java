package com.joe.threadx;

import java.text.MessageFormat;

import com.joe.threadx.exception.ThreadxException;

/**
 * 线程任务拦截，执行顺序：如果beforeAccept、before最先执行那么after、exception、finalTask将最后执行
 * 
 * @author JoeKerouac
 * @version 2019年08月21日 17:43
 */
public interface TaskInterceptor {

    /**
     * 加入线程池前调用
     * 
     * @param task
     *            将要执行的任务，不能为空，可能是{@link Runnable}，也可能是{@link java.util.concurrent.Callable}
     * @return task，不能为空，类型必须与入参一致
     */
    default Object beforeAccept(Object task) {
        return task;
    }

    /**
     * 任务执行前拦截
     *
     * 注意：禁止抛出异常，抛出异常将导致不可预测错误，不能为空
     *
     * @param task
     *            将要执行的任务
     * @return task，不能为空，类型必须与入参一致
     */
    default Object before(Object task) {
        return task;
    }

    /**
     * 任务执行后拦截，如果发生异常将不会调用该方法
     *
     * 注意：禁止抛出异常，抛出异常将导致不可预测错误
     *
     * @param task
     *            将要执行的任务，不能为空，可能是{@link Runnable}，也可能是{@link java.util.concurrent.Callable}
     * @param result
     *            执行结果，如果没有则是null
     */
    default void after(Object task, Object result) {

    }

    /**
     * 发生异常时的拦截，如果发生异常
     *
     * @param task
     *            任务，不能为空
     * @param e
     *            异常，不能为空，可能是{@link Runnable}，也可能是{@link java.util.concurrent.Callable}
     * @return 如果返回null那么将会忽略该异常，如果返回不为null将继续调用其他处理器直到没有其他可以处理的处理器，最终如果不为空则会抛出该异常
     */
    default RuntimeException exception(Object task, Throwable e) {
        return null;
    }

    /**
     * 无论是否异常最终都会调用该方法
     * 
     * @param task
     *            执行的任务
     */
    default void finalTask(Object task) {

    }

    /**
     * 对{@link #beforeAccept(Object)}和{@link #before(Object)}方法返回值类型进行校验，不建议重写
     * 
     * @param task
     *            {@link #beforeAccept(Object)}或{@link #before(Object)}方法入参
     * @param result
     *            {@link #beforeAccept(Object)}或{@link #before(Object)}方法结果
     * @throws ThreadxException
     *             校验失败时抛出异常
     */
    default void check(Object task, Object result) throws ThreadxException {
        if (result == null) {
            throw new ThreadxException("result 不能返回null，当前TaskInterceptor为：" + this);
        }

        if (result.getClass() != task.getClass() && task.getClass().isAssignableFrom(result.getClass())) {
            throw new ThreadxException(MessageFormat.format("类型检查异常，入参类型：{0}，返回类型：{1}，当前TaskInterceptor为：{2}",
                task.getClass(), result.getClass(), this));
        }
    }
}
