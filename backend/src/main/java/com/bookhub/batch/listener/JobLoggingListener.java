package com.bookhub.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JobLoggingListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobLoggingListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(">>> [JOB START] name={}, params={}, executionId={}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getJobParameters(),
                jobExecution.getId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long duration = 0L;
        if (jobExecution.getStartTime() != null && jobExecution.getEndTime() != null) {
            duration = Duration.between(
                    jobExecution.getStartTime(),
                    jobExecution.getEndTime()).toMillis();
        }
        log.info(">>> [JOB END] name={}, status={}, exitCode={}, duration={}ms",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus(),
                jobExecution.getExitStatus().getExitCode(),
                duration);
    }
}