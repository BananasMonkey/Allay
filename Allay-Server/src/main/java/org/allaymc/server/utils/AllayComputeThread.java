package org.allaymc.server.utils;

/**
 * Allay Project 2024/5/18
 *
 * @author daoge_cmd
 */
public class AllayComputeThread extends Thread {
    public static final String ALLAY_COMPUTATION_THREAD_PREFIX = "allay-compute-thread-";

    public AllayComputeThread(Runnable task) {
        super(task);
        setName(ALLAY_COMPUTATION_THREAD_PREFIX + threadId());
    }
}
