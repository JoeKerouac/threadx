package com.joe.threadx.exception;

/**
 * 线程环境异常
 *
 * @author JoeKerouac
 * @version 2019年08月22日 10:16
 */
public class ThreadLocalEnvException extends ThreadxException {

    public ThreadLocalEnvException(String message) {
        super(message);
    }

    public ThreadLocalEnvException(String message, Throwable cause) {
        super(message, cause);
    }
}
