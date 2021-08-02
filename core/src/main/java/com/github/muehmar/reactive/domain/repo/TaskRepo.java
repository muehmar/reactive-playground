package com.github.muehmar.reactive.domain.repo;

import com.github.muehmar.reactive.domain.data.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskRepo {
  Mono<Integer> insertRandomTasks(int numberOfTasks);

  Flux<Task> findAll();
}
