package com.digitalsanctum.recon;

import com.digitalsanctum.recon.job.Job;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class JobRunnerThreadPoolExecutor implements JobRunner {

    private final ExecutorService executor;

    public JobRunnerThreadPoolExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void submit(Job job) {
        System.out.println("Submitting job: " + job);
        executor.submit(() -> {
            Optional<List<Job>> jobs = job.execute();
            jobs.ifPresent(jobList -> jobList.forEach(this::submit));
        });
    }
}
