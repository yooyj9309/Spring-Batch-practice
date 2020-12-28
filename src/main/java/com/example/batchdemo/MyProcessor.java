package com.example.batchdemo;

import org.springframework.batch.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyProcessor implements ItemProcessor<Integer, String> {

  @Override
  public String process(Integer integer) {
    try {
      return "나누기 값은: " + integer / integer + " 원본 값: " + integer;
    } catch (ArithmeticException e) {
      log.error("0으로 나누는 건 에러이므로 해당 integer가 0인것은 제외합니다.");
      return null;
    }
  }
}
