package com.github.muehmar.reactive.domain.repo;

import com.github.muehmar.reactive.domain.data.Task;
import java.util.List;

public interface SyncTaskRepo {
  List<Task> findAll();
}
