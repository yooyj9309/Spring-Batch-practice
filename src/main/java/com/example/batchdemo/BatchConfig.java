package com.example.batchdemo;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job myJob() {
    return jobBuilderFactory.get("myJob")
        .listener(jobExecutionListener())
        .start(myStep())
        .next(myStep())
        .build();
  }

  @Bean
  @JobScope
  public Step myStep() {
    SimpleStepBuilder<Integer, String> stepBuilder = stepBuilderFactory.get("myStep")
        .<Integer, String>chunk(3)
        .reader(myReader(null))
        .processor(myProcessor())
        .writer(myWriter());

    return stepBuilder.build();
  }

  @Bean
  @StepScope
  public ListItemReader<Integer> myReader(@Value("#{jobParameters[startNumber]}") String startNumber) {
    log.info("show job parameter: {}", startNumber);
    int num = Objects.isNull(startNumber) ? 0 : Integer.parseInt(startNumber);
    List<Integer> integers = IntStream.range(num, num + 10).boxed().collect(Collectors.toList());

    return new ListItemReader<>(integers);
  }

  @Bean
  @StepScope
  public MyProcessor myProcessor() {
    return new MyProcessor();
  }

  @Bean
  @StepScope
  public MyWriter myWriter() {
    return new MyWriter();
  }

  /**
   * Batch 작업 시작 전과 완료 후 필요한 비즈니스 로직을 정의 하는 곳 입니다.
   */
  @Bean
  public JobExecutionListener jobExecutionListener() {
    return new JobExecutionListener() {
      @Override
      public void beforeJob(JobExecution jobExecution) {
        log.info(jobExecution.getJobParameters() + " job 시작전");
      }

      @Override
      public void afterJob(JobExecution jobExecution) {
        log.info(jobExecution.getJobParameters() + " job 끝난 후");
      }
    };
  }
}
