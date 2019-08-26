package com.joe.threadx.exception;

/**
 * 未被捕获的异常
 * 
 * @author JoeKerouac
 * @version 2019年08月21日 18:02
 */
public class UncaughtException extends ThreadxException {

    public UncaughtException(Throwable cause) {
        super("异常未被处理", cause);
    }
}
