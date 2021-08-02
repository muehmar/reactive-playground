package com.github.muehmar.reactive.controller.api;

import com.github.muehmar.reactive.data.HttpResponse;
import com.github.muehmar.reactive.data.HttpResponses;
import com.github.muehmar.reactive.domain.service.TaskService;
import com.github.muehmar.reactive.restapi.model.TaskDto;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/v1")
public class TestController {
  private static final Logger logger = LoggerFactory.getLogger(TestController.class);
  private final TaskService taskService;

  public TestController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("/tasks")
  @Transactional(readOnly = true, transactionManager = "reactiveTransactionManager")
  public Mono<HttpResponse> getTasks() {
    logger.info("Incoming Request getTasks");
    final Flux<TaskDto> fluxTasks =
        taskService
            .loadAll()
            .map(
                task ->
                    TaskDto.newBuilder()
                        .setId(task.getId().asString())
                        .setTitle(task.getTaskTitle().asString())
                        .setCreation(task.getInsertion())
                        .build());

    return fluxTasks
        .collectList()
        .map(
            l -> {
              logger.info("Mapping list");
              return l;
            })
        .map(HttpResponses::okJson);
  }

  @GetMapping("/tasks-sync")
  @Transactional(readOnly = true, transactionManager = "dataSourceTransactionManager")
  public HttpResponse getTasksSync() {
    logger.info("Incoming Request getTasksSync");
    final List<TaskDto> tasks =
        taskService.loadAllSync().stream()
            .map(
                task ->
                    TaskDto.newBuilder()
                        .setId(task.getId().asString())
                        .setTitle(task.getTaskTitle().asString())
                        .setCreation(task.getInsertion())
                        .build())
            .collect(Collectors.toList());
    return HttpResponses.okJson(tasks);
  }

  @GetMapping("/tasks-sync-deferred")
  @Transactional(readOnly = true, transactionManager = "reactiveTransactionManager")
  public Mono<HttpResponse> getTasksSyncDeferred() {
    logger.info("Incoming Request getTasksSync");
    return Mono.fromFuture(
            CompletableFuture.supplyAsync(
                () ->
                    taskService.loadAllSync().stream()
                        .map(
                            task ->
                                TaskDto.newBuilder()
                                    .setId(task.getId().asString())
                                    .setTitle(task.getTaskTitle().asString())
                                    .setCreation(task.getInsertion())
                                    .build())
                        .collect(Collectors.toList())))
        .map(
            t -> {
              logger.info("Sync tasks loaded via future");
              return HttpResponses.okJson(t);
            });
  }

  @GetMapping("/tasks/insert")
  @Transactional(transactionManager = "reactiveTransactionManager")
  public Mono<HttpResponse> insertTasks(@RequestParam("taskCount") int taskCount) {
    return taskService.insertRandomTasks(taskCount).map(HttpResponses::okJson);
  }

  @GetMapping("/long-running-reactive")
  public Mono<HttpResponse> longRunningReactive() {
    logger.info("Start long running async task");
    return Mono.just("Hello")
        .delayElement(Duration.ofSeconds(15))
        .map(
            str -> {
              logger.info("Long running async task endend");
              return HttpResponses.okJson(str);
            });
  }

  @GetMapping("/long-running-blocking")
  public HttpResponse longRunningBlocking() throws InterruptedException {
    logger.info("Start long running blocking task");
    Thread.sleep(15000);
    logger.info("Long running blocking task ended");
    return HttpResponses.okJson("Hello");
  }

  @GetMapping("/threads")
  public HttpResponse getThreads() {
    logger.info("Get Threads");
    final List<String> threads =
        Thread.getAllStackTraces().keySet().stream()
            .map(
                thread ->
                    String.format("%s, state: %s", thread.getName(), thread.getState().name()))
            .collect(Collectors.toList());
    return HttpResponses.okJson(threads);
  }
}
