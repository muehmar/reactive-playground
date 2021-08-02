package com.github.muehmar.reactive.datalayer.config;

import java.util.Objects;

public class DatabaseConfig {
  private static final String JDBC_SCHEME = "jdbc";
  private static final String R2DBC_SCHEME = "r2dbc";

  private final String username;
  private final String password;
  private final String hostAndPath;
  private final String driver;
  private final String jooqDialect;

  public DatabaseConfig(
      String username, String password, String hostAndPath, String driver, String jooqDialect) {
    this.username = username;
    this.password = password;
    this.hostAndPath = hostAndPath;
    this.driver = driver;
    this.jooqDialect = jooqDialect;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getR2dbcUrl() {
    return getUrl(R2DBC_SCHEME);
  }

  public String getJooqDialect() {
    return jooqDialect;
  }

  public String getJdcbUrl() {
    return getUrl(JDBC_SCHEME);
  }

  private String getUrl(String scheme) {
    return String.format("%s:%s://%s", scheme, driver, hostAndPath);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DatabaseConfig that = (DatabaseConfig) o;
    return Objects.equals(username, that.username)
        && Objects.equals(password, that.password)
        && Objects.equals(hostAndPath, that.hostAndPath)
        && Objects.equals(driver, that.driver);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, hostAndPath, driver);
  }

  @Override
  public String toString() {
    return "DatabaseConfig{"
        + "username='"
        + username
        + '\''
        + ", password='"
        + "***"
        + '\''
        + ", hostAndPath='"
        + hostAndPath
        + '\''
        + ", driver='"
        + driver
        + '\''
        + '}';
  }
}
