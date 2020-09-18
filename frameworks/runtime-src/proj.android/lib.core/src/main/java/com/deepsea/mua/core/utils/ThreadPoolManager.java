package com.deepsea.mua.core.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by JUN on 2018/12/28
 */
public class ThreadPoolManager {

    private static volatile ThreadPoolManager mInstance;

    public static ThreadPoolManager getInstance() {
        if (mInstance == null) {
            synchronized (ThreadPoolManager.class) {
                if (mInstance == null) {
                    mInstance = new ThreadPoolManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 核心线程数量，同时能够执行的线程数量
     */
    private int corePoolSize;
    /**
     * 最大线程数量，表示当缓冲队列满的时候能继续容纳的等待任务数量
     */
    private int maximumPoolSize;
    /**
     * 存活时间
     */
    private long keepAliveTime = 1;
    /**
     * 存活时间单位
     */
    private TimeUnit unit = TimeUnit.SECONDS;

    private Executor executor;

    private Executor mainExecutor;

    private ThreadPoolManager() {
        /**
         * 给corePoolSize赋值：当前设备可用处理器核心数 * 2 + 1，能够让cpu的效率得到最大执行
         */
        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        maximumPoolSize = corePoolSize;
        executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new LinkedBlockingDeque<Runnable>(),
                new DefaultThreadFactory(Thread.NORM_PRIORITY, "yizhua-pool-")) {

            @Override
            public void execute(Runnable command) {
                if (command == null) {
                    return;
                }
                super.execute(command);
            }
        };
    }

    public Executor mainExecutor() {
        if (mainExecutor == null) {
            mainExecutor = new MainThreadExecutor();
        }
        return mainExecutor;
    }

    /**
     * 执行
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        if (executor != null) {
            executor.execute(runnable);
        }
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        /**
         * 线程池计数
         */
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        /**
         * 线程计数
         */
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        private final int threadPriority;
        private final String namePrefix;
        private final ThreadGroup group;

        public DefaultThreadFactory(int threadPriority, String namePrefix) {
            this.threadPriority = threadPriority;
            this.group = Thread.currentThread().getThreadGroup();
            this.namePrefix = namePrefix + poolNumber.getAndIncrement() + " -thread- ";
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(group, r, namePrefix + threadNumber.getAndIncrement());
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            thread.setPriority(threadPriority);
            return thread;
        }
    }

    private static class MainThreadExecutor implements Executor {

        private Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }
    }
}
