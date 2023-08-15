package com.digitalsanctum.recon;

import com.digitalsanctum.recon.job.Job;

public interface JobRunner {
    void submit(Job job);
}
