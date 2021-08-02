package com.github.muehmar.reactive.datalayer;

import com.github.muehmar.reactive.datalayer.config.DatabaseConfig;
import com.github.muehmar.reactive.datalayer.repo.jooq.JooqSyncTaskRepo;
import com.github.muehmar.reactive.datalayer.repo.jooq.JooqTaskRepo;
import com.github.muehmar.reactive.domain.repo.SyncTaskRepo;
import com.github.muehmar.reactive.domain.repo.TaskRepo;
import com.zaxxer.hikari.HikariDataSource;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import javax.sql.DataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class RepoFactory {
  private final DataSource dataSource;
  private final SyncDSLContext syncDSLContext;

  private final ConnectionFactory connectionFactory;
  private final AsyncDSLContext asyncDSLContext;

  public RepoFactory(DatabaseConfig databaseConfig) {
    dataSource = createDataSource(databaseConfig);
    syncDSLContext = new SyncDSLContext(DSL.using(dataSource, SQLDialect.MARIADB));

    connectionFactory = createConnectionFactory(databaseConfig);
    asyncDSLContext = new AsyncDSLContext(DSL.using(connectionFactory));
  }

  private ConnectionFactory createConnectionFactory(DatabaseConfig databaseConfig) {
    final ConnectionFactoryOptions options =
        ConnectionFactoryOptions.parse(databaseConfig.getR2dbcUrl())
            .mutate()
            .option(ConnectionFactoryOptions.USER, databaseConfig.getUsername())
            .option(ConnectionFactoryOptions.PASSWORD, databaseConfig.getPassword())
            .build();

    return ConnectionFactories.get(options);
  }

  private static DataSource createDataSource(DatabaseConfig databaseConfig) {
    final var ds = new HikariDataSource();
    ds.setJdbcUrl(databaseConfig.getJdcbUrl());
    ds.setUsername(databaseConfig.getUsername());
    ds.setPassword(databaseConfig.getPassword());
    ds.setAutoCommit(false);

    return ds;
  }

  /** Get the {@link DataSource} of the synchronous, blocking access to the DB. */
  public DataSource getDataSource() {
    return dataSource;
  }

  /** Get the {@link ConnectionFactory} of the asynchronous, non-blocking access to the DB. */
  public ConnectionFactory getConnectionFactory() {
    return connectionFactory;
  }

  public Migrations getMigrations() {
    return Migrations.fromDataSource(dataSource);
  }

  public TaskRepo createTaskRepo() {
    return new JooqTaskRepo(asyncDSLContext);
  }

  public SyncTaskRepo createSyncTaskRepo() {
    return new JooqSyncTaskRepo(syncDSLContext);
  }
}
