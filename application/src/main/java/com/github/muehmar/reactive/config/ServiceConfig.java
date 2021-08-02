package com.github.muehmar.reactive.config;

import com.github.muehmar.reactive.domain.service.ServiceFactory;
import com.github.muehmar.reactive.domain.service.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({ServiceFactory.class})
@Configuration
public class ServiceConfig {
  @Bean
  public TaskService taskService(ServiceFactory serviceFactory) {
    return serviceFactory.createTaskService();
  }
}
