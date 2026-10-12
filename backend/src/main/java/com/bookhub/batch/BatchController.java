package com.bookhub.batch;

import com.bookhub.batch.config.ImportBooksJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Endpoint pour déclencher manuellement les jobs Spring Batch.
 * Réservé aux ADMIN.
 */
@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job importBooksJob;

    public BatchController(JobLauncher jobLauncher,
                           @Qualifier(ImportBooksJobConfig.JOB_NAME) Job importBooksJob) {
        this.jobLauncher = jobLauncher;
        this.importBooksJob = importBooksJob;
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> triggerImport() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(importBooksJob, params);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "jobId", execution.getId(),
                "status", execution.getStatus().toString(),
                "startedAt", execution.getStartTime() != null
                        ? execution.getStartTime().toString() : ""
        ));
    }
}