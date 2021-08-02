package com.github.muehmar.reactive.domain.service;

import com.github.muehmar.reactive.domain.data.Task;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
  Mono<Integer> insertRandomTasks(int numberOfTasks);

  Flux<Task> loadAll();

  List<Task> loadAllSync();
}
