package com.digitalsanctum.recon;

import com.digitalsanctum.recon.job.UrlReconJob;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        JobRunnerConfig jobRunnerConfig = new JobRunnerConfig(
                100,
                10,
                10,
                60,
                10,
                new CustomRejectedExecutionHandler()
        );

        System.out.println("JobRunnerConfig: " + jobRunnerConfig);

        ExecutorService executor = new ThreadPoolExecutor(
                jobRunnerConfig.getCorePoolSize(),
                jobRunnerConfig.getMaxPoolSize(),
                jobRunnerConfig.getKeepAliveTimeInSeconds(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(jobRunnerConfig.getQueueCapacity()),
                jobRunnerConfig.getRejectedExecutionHandler()
        );


        JobRunner runner = new JobRunnerThreadPoolExecutor(executor);

        final String userId = System.getProperty("user.name") + "_" + System.currentTimeMillis();

        Stream.of(
                        "https://yahoo.com",
                        "https://news.ycombinator.com"
                )
                .flatMap(url -> Stream.of(
                                // TODO config for UrlReconJob to toggle features
                                new UrlReconJob(userId, url)
                        )
                )
                .forEach(runner::submit);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            executor.shutdown();

            System.out.println("Waiting for all jobs to complete...");
            // Wait until all tasks are completed or until the timeout expires
            try {
                if (!executor.awaitTermination(jobRunnerConfig.getShutdownTimeoutInSeconds(), TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }));

    }
}
