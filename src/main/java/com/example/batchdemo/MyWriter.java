package com.example.batchdemo;

import org.springframework.batch.item.ItemWriter;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MyWriter implements ItemWriter<String> {

  @Override
  public void write(List<? extends String> list) {
    log.info("Writer로 들어온 데이터의 수: {}", list.size());

    list.forEach(log::info);

    log.info(this + "write finish");
  }
}
