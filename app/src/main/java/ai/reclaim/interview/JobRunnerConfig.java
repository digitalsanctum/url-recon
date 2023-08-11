package ai.reclaim.interview;

import java.util.concurrent.RejectedExecutionHandler;

public class JobRunnerConfig {
    private final int queueCapacity;
    private final int corePoolSize;
    private final int maxPoolSize;
    private final long keepAliveTimeInSeconds;
    private final long shutdownTimeoutInSeconds;
    private final RejectedExecutionHandler rejectedExecutionHandler;

    public JobRunnerConfig(int queueCapacity,
                           int corePoolSize,
                           int maxPoolSize,
                           long keepAliveTimeInSeconds,
                           long shutdownTimeoutInSeconds,
                           RejectedExecutionHandler rejectedExecutionHandler) {
        this.queueCapacity = queueCapacity;
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTimeInSeconds = keepAliveTimeInSeconds;
        this.shutdownTimeoutInSeconds = shutdownTimeoutInSeconds;
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public long getKeepAliveTimeInSeconds() {
        return keepAliveTimeInSeconds;
    }

    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return rejectedExecutionHandler;
    }

    public long getShutdownTimeoutInSeconds() {
        return shutdownTimeoutInSeconds;
    }

    @Override
    public String toString() {
        return "JobRunnerConfig{" +
                "queueCapacity=" + queueCapacity +
                ", corePoolSize=" + corePoolSize +
                ", maxPoolSize=" + maxPoolSize +
                ", keepAliveTimeInSeconds=" + keepAliveTimeInSeconds +
                ", shutdownTimeoutInSeconds=" + shutdownTimeoutInSeconds +
                '}';
    }
}
