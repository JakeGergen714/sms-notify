package com.jake.smsgateway.configuration;

import com.jake.smsgateway.jpa.domain.SmsRequest;
import com.jake.smsgateway.reader.SmsRequestReader;
import com.jake.smsgateway.service.SmsRequestService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class JobConfiguration {

  @Bean
  public Job job(JobRepository jobRepository, Step step) {
    return new JobBuilder("sms gateway job", jobRepository).start(step).build();
  }

  @Bean
  @StepScope
  public ItemReader<SmsRequest> reader(SmsRequestService service) {
    return new SmsRequestReader(service);
  }

  @Bean
  public Step processSmsRequests(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<SmsRequest> reader,
      ItemProcessor<SmsRequest, SmsRequest> processor,
      ItemWriter<SmsRequest> writer) {
    return new StepBuilder("process sms requests step", jobRepository)
        .<SmsRequest, SmsRequest>chunk(32, transactionManager).allowStartIfComplete(true)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }
}
