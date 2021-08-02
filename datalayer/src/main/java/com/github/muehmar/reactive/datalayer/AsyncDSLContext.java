package com.github.muehmar.reactive.datalayer;

import org.jooq.DSLContext;

public class AsyncDSLContext {
  private final DSLContext context;

  public AsyncDSLContext(DSLContext context) {
    this.context = context;
  }

  public DSLContext getContext() {
    return context;
  }
}
