package com.github.muehmar.reactive.datalayer;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;

public class Migrations {
  private final Flyway flyway;

  private Migrations(Flyway flyway) {
    this.flyway = flyway;
  }

  public static Migrations fromDataSource(DataSource dataSource) {
    final var flyway = Flyway.configure().dataSource(dataSource).load();
    return new Migrations(flyway);
  }

  public void migrate() {
    flyway.repair();
    flyway.migrate();
  }
}
