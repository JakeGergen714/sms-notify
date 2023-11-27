package com.jake.smsgateway.configuration;

import jakarta.transaction.TransactionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
@EnableScheduling
public class JobScheduler {
    private final JobLauncher jobLauncher;
    private final Job job;

    @Scheduled(fixedDelay = 4, timeUnit = TimeUnit.SECONDS)
    @StepScope
    public void run () throws Exception {
        JobExecution jobExecution = jobLauncher.run(job, new JobParametersBuilder()
                .toJobParameters());
    }
}
