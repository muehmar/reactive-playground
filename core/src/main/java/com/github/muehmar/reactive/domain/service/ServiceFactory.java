package com.github.muehmar.reactive.domain.service;

import com.github.muehmar.reactive.domain.repo.SyncTaskRepo;
import com.github.muehmar.reactive.domain.repo.TaskRepo;

public class ServiceFactory {
  private final TaskRepo taskRepo;
  private final SyncTaskRepo syncTaskRepo;

  public ServiceFactory(TaskRepo taskRepo, SyncTaskRepo syncTaskRepo) {
    this.taskRepo = taskRepo;
    this.syncTaskRepo = syncTaskRepo;
  }

  public TaskService createTaskService() {
    return new TaskServiceImpl(taskRepo, syncTaskRepo);
  }
}
