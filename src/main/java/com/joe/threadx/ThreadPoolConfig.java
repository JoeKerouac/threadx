package com.joe.threadx;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线程池配置
 * 
 * @author JoeKerouac
 * @version 2019年08月21日 21:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadPoolConfig {

    /**
     * 线程池核心大小
     */
    private int                      corePoolSize;

    /**
     * 线程池最大大小
     */
    private int                      maximumPoolSize;

    /**
     * 线程存活时间
     */
    private long                     keepAliveTime;

    /**
     * 线程存活时间单位，不能为空
     */
    private TimeUnit                 unit;

    /**
     * 阻塞队列，不能为空
     */
    private BlockingQueue<Runnable>  workQueue;

    /**
     * 线程工厂，不能为空
     */
    private ThreadFactory            threadFactory;

    /**
     * 被拒绝任务的处理器，不能为空
     */
    private RejectedExecutionHandler handler;
}
