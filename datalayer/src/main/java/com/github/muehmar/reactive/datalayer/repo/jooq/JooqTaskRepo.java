package com.github.muehmar.reactive.datalayer.repo.jooq;

import static ch.mm.reactive.datalayer.jooq.Tables.TASK;

import ch.mm.reactive.datalayer.jooq.tables.records.TaskRecord;
import com.github.muehmar.reactive.datalayer.AsyncDSLContext;
import com.github.muehmar.reactive.domain.data.Task;
import com.github.muehmar.reactive.domain.data.TaskId;
import com.github.muehmar.reactive.domain.data.TaskTitle;
import com.github.muehmar.reactive.domain.repo.TaskRepo;
import java.util.UUID;
import org.jooq.DSLContext;
import org.jooq.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class JooqTaskRepo implements TaskRepo {
  private final DSLContext context;

  public JooqTaskRepo(AsyncDSLContext asyncDSLContext) {
    this.context = asyncDSLContext.getContext();
  }

  @Override
  public Mono<Integer> insertRandomTasks(int numberOfTasks) {
    final Mono<Integer> delete = Mono.from(context.deleteFrom(TASK));

    final Mono<Integer> inserts =
        Flux.range(0, numberOfTasks)
            .flatMap(
                ignore -> {
                  final Task task =
                      Task.createNew(TaskTitle.ofStringUnsafe(UUID.randomUUID().toString()));
                  return context.insertInto(TASK, TASK.fields()).valuesOfRecords(fromTask(task));
                })
            .reduce(Integer::sum);

    return delete.flatMap(ignore -> inserts);
  }

  @Override
  public Flux<Task> findAll() {
    final Publisher<TaskRecord> taskRecords = context.selectFrom(TASK).orderBy(TASK.TITLE.asc());
    return Flux.from(taskRecords).map(JooqTaskRepo::toTask);
  }

  private static Task toTask(TaskRecord taskRecord) {
    return Task.ofAll(
        TaskId.fromStringUnsafe(taskRecord.getId()),
        TaskTitle.ofStringUnsafe(taskRecord.getTitle()),
        taskRecord.getInsertion());
  }

  private static TaskRecord fromTask(Task task) {
    return new TaskRecord(
        task.getId().asString(), task.getTaskTitle().asString(), task.getInsertion());
  }
}
