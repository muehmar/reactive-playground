package com.github.muehmar.reactive.domain.data;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class TaskId {
  private final String id;

  private TaskId(String id) {
    this.id = id.toLowerCase();
  }

  public static TaskId random() {
    return new TaskId(createRandomIdString());
  }

  public static TaskId fromStringUnsafe(String id) {
    return fromString(id)
        .orElseThrow(
            () -> new IllegalArgumentException("ID '" + id + "' cannot be parsed to a TaskId"));
  }

  public static Optional<TaskId> fromString(String id) {
    return Optional.of(id).filter(str -> str.length() == 20).map(TaskId::new);
  }

  public String asString() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TaskId taskId = (TaskId) o;
    return Objects.equals(id, taskId.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return asString();
  }

  private static String createRandomIdString() {
    final int leftLimit = 97; // letter 'a'
    final int rightLimit = 122; // letter 'z'
    final int targetStringLength = 20;
    final Random random = new Random();

    return random
        .ints(leftLimit, rightLimit + 1)
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }
}
