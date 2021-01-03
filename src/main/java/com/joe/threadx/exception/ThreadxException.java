package com.joe.threadx.exception;

/**
 * Threadx框架顶层异常
 *
 * @author JoeKerouac
 * @version 2019年08月21日 18:01
 */
public class ThreadxException extends RuntimeException {

    public ThreadxException() {
        super();
    }

    public ThreadxException(String message) {
        super(message);
    }

    public ThreadxException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThreadxException(Throwable cause) {
        super(cause);
    }

    protected ThreadxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
