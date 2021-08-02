package com.github.muehmar.reactive.datalayer;

import org.jooq.DSLContext;

public class SyncDSLContext {
  private final DSLContext context;

  public SyncDSLContext(DSLContext context) {
    this.context = context;
  }

  public DSLContext getContext() {
    return context;
  }
}
