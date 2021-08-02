package com.github.muehmar.reactive.datalayer.repo.jooq;

import static ch.mm.reactive.datalayer.jooq.Tables.TASK;

import ch.mm.reactive.datalayer.jooq.tables.records.TaskRecord;
import com.github.muehmar.reactive.datalayer.SyncDSLContext;
import com.github.muehmar.reactive.domain.data.Task;
import com.github.muehmar.reactive.domain.data.TaskId;
import com.github.muehmar.reactive.domain.data.TaskTitle;
import com.github.muehmar.reactive.domain.repo.SyncTaskRepo;
import java.util.List;
import java.util.stream.Collectors;
import org.jooq.DSLContext;

public class JooqSyncTaskRepo implements SyncTaskRepo {
  private final DSLContext context;

  public JooqSyncTaskRepo(SyncDSLContext syncDSLContext) {
    this.context = syncDSLContext.getContext();
  }

  @Override
  public List<Task> findAll() {
    return context.selectFrom(TASK).fetchInto(TaskRecord.class).stream()
        .map(JooqSyncTaskRepo::toTask)
        .collect(Collectors.toList());
  }

  private static Task toTask(TaskRecord taskRecord) {
    return Task.ofAll(
        TaskId.fromStringUnsafe(taskRecord.getId()),
        TaskTitle.ofStringUnsafe(taskRecord.getTitle()),
        taskRecord.getInsertion());
  }
}
