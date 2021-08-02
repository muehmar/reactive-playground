package com.github.muehmar.reactive.config;

import com.github.muehmar.reactive.config.properties.DatabaseConfigProperties;
import com.github.muehmar.reactive.datalayer.Migrations;
import com.github.muehmar.reactive.datalayer.RepoFactory;
import com.github.muehmar.reactive.datalayer.config.DatabaseConfig;
import com.github.muehmar.reactive.domain.repo.SyncTaskRepo;
import com.github.muehmar.reactive.domain.repo.TaskRepo;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

@Import(RepoFactory.class)
@Configuration
@EnableConfigurationProperties(DatabaseConfigProperties.class)
public class DataLayerConfig {

  @Bean
  public DatabaseConfig databaseConfig(DatabaseConfigProperties databaseConfigProperties) {
    return databaseConfigProperties.asDatabaseConfig();
  }

  @Bean
  public TaskRepo taskRepo(RepoFactory repoFactory) {
    return repoFactory.createTaskRepo();
  }

  @Bean
  public SyncTaskRepo syncTaskRepo(RepoFactory repoFactory) {
    return repoFactory.createSyncTaskRepo();
  }

  @Bean
  public Migrations migrations(RepoFactory repoFactory) {
    final Migrations migrations = repoFactory.getMigrations();
    migrations.migrate();
    return migrations;
  }

  @Bean
  public PlatformTransactionManager dataSourceTransactionManager(RepoFactory repoFactory) {
    return new DataSourceTransactionManager(repoFactory.getDataSource());
  }

  @Bean
  public ReactiveTransactionManager reactiveTransactionManager(RepoFactory repoFactory) {
    return new R2dbcTransactionManager(repoFactory.getConnectionFactory());
  }
}
