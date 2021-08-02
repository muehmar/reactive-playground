package com.github.muehmar.reactive.domain.service;

import com.github.muehmar.reactive.domain.data.Task;
import com.github.muehmar.reactive.domain.repo.SyncTaskRepo;
import com.github.muehmar.reactive.domain.repo.TaskRepo;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class TaskServiceImpl implements TaskService {
  private final TaskRepo taskRepo;
  private final SyncTaskRepo syncTaskRepo;

  public TaskServiceImpl(TaskRepo taskRepo, SyncTaskRepo syncTaskRepo) {
    this.taskRepo = taskRepo;
    this.syncTaskRepo = syncTaskRepo;
  }

  @Override
  public Mono<Integer> insertRandomTasks(int numberOfTasks) {
    return taskRepo.insertRandomTasks(numberOfTasks);
  }

  @Override
  public Flux<Task> loadAll() {
    return taskRepo.findAll();
  }

  @Override
  public List<Task> loadAllSync() {
    return syncTaskRepo.findAll();
  }
}
