package com.github.muehmar.reactive.config.properties;

import com.github.muehmar.reactive.datalayer.config.DatabaseConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.database")
public class DatabaseConfigProperties {
  private String username;
  private String password;
  private String hostAndPath;
  private String driver;
  private Jooq jooq;

  public static class Jooq {
    private String dialect;

    public void setDialect(String dialect) {
      this.dialect = dialect;
    }
  }

  public void setJooq(Jooq jooq) {
    this.jooq = jooq;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setHostAndPath(String hostAndPath) {
    this.hostAndPath = hostAndPath;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public DatabaseConfig asDatabaseConfig() {
    return new DatabaseConfig(username, password, hostAndPath, driver, jooq.dialect);
  }
}
