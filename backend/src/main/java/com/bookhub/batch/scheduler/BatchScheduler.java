package com.bookhub.batch.scheduler;

import com.bookhub.batch.config.ComputeStatisticsJobConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Déclenche automatiquement le job de statistiques chaque nuit à 2h.
 * Peut être désactivé via app.batch.scheduler.enabled=false.
 */
@Component
@ConditionalOnProperty(
        name = "app.batch.scheduler.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class BatchScheduler {

    private static final Logger log = LoggerFactory.getLogger(BatchScheduler.class);

    private final JobLauncher jobLauncher;
    private final Job computeStatisticsJob;

    public BatchScheduler(JobLauncher jobLauncher,
                          @Qualifier(ComputeStatisticsJobConfig.JOB_NAME) Job computeStatisticsJob) {
        this.jobLauncher = jobLauncher;
        this.computeStatisticsJob = computeStatisticsJob;
    }

    @Scheduled(cron = "${app.batch.scheduler.statistics-cron:0 0 2 * * *}")
    public void runComputeStatisticsJob() {
        log.info(">>> [Scheduler] Triggering computeStatisticsJob");
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("scheduled-at", System.currentTimeMillis())
                    .toJobParameters();

            var execution = jobLauncher.run(computeStatisticsJob, params);
            log.info(">>> [Scheduler] Job finished with status: {}", execution.getStatus());

        } catch (Exception e) {
            log.error(">>> [Scheduler] Job failed", e);
        }
    }
}