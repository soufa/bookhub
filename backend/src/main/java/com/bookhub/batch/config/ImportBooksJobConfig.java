package com.bookhub.batch.config;

import com.bookhub.batch.dto.BookCsvRecord;
import com.bookhub.batch.processor.BookItemProcessor;
import com.bookhub.book.Book;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration du job Spring Batch qui importe des livres depuis un CSV.
 * Chunk-oriented processing : 10 items par transaction.
 */
@Configuration
public class ImportBooksJobConfig {

    public static final String JOB_NAME = "importBooksJob";
    public static final String STEP_NAME = "importBooksStep";

    @Bean
    public FlatFileItemReader<BookCsvRecord> bookCsvReader() {
        return new FlatFileItemReaderBuilder<BookCsvRecord>()
                .name("bookCsvReader")
                .resource(new ClassPathResource("data/books-import.csv"))
                .delimited()
                .delimiter(",")
                .quoteCharacter('"')
                .names("title", "author", "isbn", "price", "currency")
                .targetType(BookCsvRecord.class)
                .linesToSkip(1)
                .build();
    }

    @Bean
    public JpaItemWriter<Book> bookJpaWriter(EntityManagerFactory emf) {
        JpaItemWriter<Book> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public Step importBooksStep(JobRepository jobRepository,
                                PlatformTransactionManager txManager,
                                FlatFileItemReader<BookCsvRecord> bookCsvReader,
                                BookItemProcessor bookItemProcessor,
                                JpaItemWriter<Book> bookJpaWriter) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<BookCsvRecord, Book>chunk(10, txManager)
                .reader(bookCsvReader)
                .processor(bookItemProcessor)
                .writer(bookJpaWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(20)
                .build();
    }

    @Bean
    public Job importBooksJob(JobRepository jobRepository,
                              Step importBooksStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(importBooksStep)
                .build();
    }
}