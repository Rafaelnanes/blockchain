package com.rbn.blockchain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@EnableAsync
@Configuration
public class BlockchainConfiguration {

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }

}
