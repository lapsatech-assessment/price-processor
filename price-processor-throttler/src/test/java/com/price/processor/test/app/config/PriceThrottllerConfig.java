package com.price.processor.test.app.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.price.processor.test.app.services.Consumers;
import com.price.processor.test.app.services.NamePrefixThreadFactory;
import com.price.processor.throttler.PriceThrottler;

@Configuration
@Import(ConsumersConfig.class)
public class PriceThrottllerConfig {

  @Bean
  public ExecutorService consumersPool() {
    return Executors.newCachedThreadPool(new NamePrefixThreadFactory("price-throttler-thread"));
  }

  @Autowired
  private Consumers consumers;

  @Bean
  public PriceThrottler priceThrottler() {
    PriceThrottler pt = new PriceThrottler(consumersPool());
    consumers.stream().forEach(pt::subscribe);
    return pt;
  }

}