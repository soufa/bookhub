package com.bookhub.batch;

import com.bookhub.batch.config.ComputeStatisticsJobConfig;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
class ComputeStatisticsJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier(ComputeStatisticsJobConfig.JOB_NAME)
    private Job computeStatisticsJob;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void should_compute_statistics_and_persist_snapshot() throws Exception {
        jobLauncherTestUtils.setJob(computeStatisticsJob);

        long countBefore = countStatistics();

        JobParameters params = new JobParametersBuilder()
                .addLong("test-timestamp", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchJob(params);

        assertThat(execution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        long countAfter = countStatistics();
        assertThat(countAfter).isEqualTo(countBefore + 1);

        var stats = jdbcTemplate.queryForMap(
                """
                SELECT total_books, available_books, borrowed_books
                FROM book_statistics
                ORDER BY computed_at DESC
                LIMIT 1
                """);
        assertThat((Integer) stats.get("total_books")).isGreaterThanOrEqualTo(0);
    }

    private long countStatistics() {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM book_statistics", Long.class);
        return count != null ? count : 0L;
    }
}