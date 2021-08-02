package com.github.muehmar.reactive.domain.data;

import java.time.LocalDateTime;
import java.util.Objects;

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

  public TaskId getId() {
    return id;
  }

  public TaskTitle getTaskTitle() {
    return taskTitle;
  }

  public LocalDateTime getInsertion() {
    return insertion;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Task task = (Task) o;
    return Objects.equals(id, task.id)
        && Objects.equals(taskTitle, task.taskTitle)
        && Objects.equals(insertion, task.insertion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, taskTitle, insertion);
  }

  @Override
  public String toString() {
    return "Task{" + "id=" + id + ", taskTitle=" + taskTitle + ", insertion=" + insertion + '}';
  }
}
