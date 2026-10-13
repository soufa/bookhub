package com.bookhub.batch.config;

import com.bookhub.batch.tasklet.ComputeStatisticsTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration du job Spring Batch qui calcule les statistiques.
 * Utilise un Tasklet (pas de chunk) car l'action est unique.
 */
@Configuration
public class ComputeStatisticsJobConfig {

    public static final String JOB_NAME = "computeStatisticsJob";
    public static final String STEP_NAME = "computeStatisticsStep";

    @Bean
    public Step computeStatisticsStep(JobRepository jobRepository,
                                      PlatformTransactionManager txManager,
                                      ComputeStatisticsTasklet tasklet) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(tasklet, txManager)
                .build();
    }

    @Bean
    public Job computeStatisticsJob(JobRepository jobRepository,
                                    Step computeStatisticsStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(computeStatisticsStep)
                .build();
    }
}