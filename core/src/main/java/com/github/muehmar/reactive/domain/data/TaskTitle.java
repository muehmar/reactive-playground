package com.github.muehmar.reactive.domain.data;

import java.util.Objects;
import java.util.Optional;

public class TaskTitle {
  private final String title;

  private TaskTitle(String title) {
    this.title = title;
  }

  public static Optional<TaskTitle> ofString(String title) {
    return Optional.ofNullable(title).filter(t -> !t.isBlank()).map(TaskTitle::new);
  }

  public static TaskTitle ofStringUnsafe(String title) {
    return ofString(title)
        .orElseThrow(() -> new IllegalArgumentException("Invalid title:" + title));
  }

  public String asString() {
    return title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TaskTitle that = (TaskTitle) o;
    return Objects.equals(title, that.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }

  @Override
  public String toString() {
    return asString();
  }
}
