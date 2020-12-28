package com.example.batchdemo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class JobController {

  private final JobLauncher jobLauncher;
  private final Job myJob;

  /**
   * API 호출을 통한 MyJob 실행하기
   */
  @PostMapping(value = "/job")
  public void startJob(@RequestParam(value = "startNumber") String startNumber) throws Exception {
    jobLauncher.run(myJob, getJobParameters(startNumber));
  }

  private static JobParameters getJobParameters(String startNumber) {
    JobParametersBuilder builder = new JobParametersBuilder();
    builder.addString("startNumber", startNumber);

    return builder.toJobParameters();
  }
}
