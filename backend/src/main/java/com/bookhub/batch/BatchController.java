package com.bookhub.batch;

import com.bookhub.batch.config.ComputeStatisticsJobConfig;
import com.bookhub.batch.config.ImportBooksJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job importBooksJob;
    private final Job computeStatisticsJob;
    private final JobExplorer jobExplorer;
    private final JdbcTemplate jdbcTemplate;

    public BatchController(JobLauncher jobLauncher,
                           @Qualifier(ImportBooksJobConfig.JOB_NAME) Job importBooksJob,
                           @Qualifier(ComputeStatisticsJobConfig.JOB_NAME) Job computeStatisticsJob,
                           JobExplorer jobExplorer,
                           JdbcTemplate jdbcTemplate) {
        this.jobLauncher = jobLauncher;
        this.importBooksJob = importBooksJob;
        this.computeStatisticsJob = computeStatisticsJob;
        this.jobExplorer = jobExplorer;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> triggerImport() throws Exception {
        return runJob(importBooksJob, "Import démarré");
    }

    @PostMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> triggerStatistics() throws Exception {
        return runJob(computeStatisticsJob, "Statistics démarré");
    }

    @GetMapping("/runs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> listRuns(
            @RequestParam(defaultValue = "10") int limit) {

        var executions = jdbcTemplate.queryForList(
                """
                SELECT job_execution_id, job_name, status, exit_code,
                       start_time, end_time
                FROM batch_job_execution bje
                JOIN batch_job_instance bji ON bje.job_instance_id = bji.job_instance_id
                ORDER BY job_execution_id DESC
                LIMIT ?
                """, limit);

        return ResponseEntity.ok(executions);
    }

    @GetMapping("/statistics/latest")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> latestStatistics() {
        var stats = jdbcTemplate.queryForMap(
                """
                SELECT computed_at, total_books, available_books, borrowed_books,
                       average_price_eur, top_author, top_author_count
                FROM book_statistics
                ORDER BY computed_at DESC
                LIMIT 1
                """);
        return ResponseEntity.ok(stats);
    }

    private ResponseEntity<Map<String, Object>> runJob(Job job, String message) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, params);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "jobId", execution.getId(),
                "jobName", execution.getJobInstance().getJobName(),
                "status", execution.getStatus().toString(),
                "startedAt", execution.getStartTime() != null
                        ? execution.getStartTime().toString() : "",
                "message", message
        ));
    }
}