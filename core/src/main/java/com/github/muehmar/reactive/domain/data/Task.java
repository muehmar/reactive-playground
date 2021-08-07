package com.github.muehmar.reactive.domain.data;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.With;

@Getter
@With
@EqualsAndHashCode
public class Task {
  private final TaskId id;
  private final TaskTitle taskTitle;
  private final LocalDateTime insertion;

  private Task(TaskId id, TaskTitle taskTitle, LocalDateTime insertion) {
    this.id = id;
    this.taskTitle = taskTitle;
    this.insertion = insertion;
  }

  public static Task createNew(TaskTitle taskTitle) {
    return new Task(TaskId.random(), taskTitle, LocalDateTime.now());
  }

  public static Task ofAll(TaskId id, TaskTitle taskTitle, LocalDateTime insertion) {
    return new Task(id, taskTitle, insertion);
  }

  @Override
  public String toString() {
    return "Task{" + "id=" + id + ", taskTitle=" + taskTitle + ", insertion=" + insertion + '}';
  }
}
