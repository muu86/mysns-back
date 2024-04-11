package com.mj.mysns.config;

import javax.sql.DataSource;
import org.hibernate.dialect.Database;
import org.testcontainers.containers.JdbcDatabaseContainer;

/**
 * @author Vlad Mihalcea
 */
public interface DataSourceProvider {

    Database database();

    String hibernateDialect();

    DataSource dataSource();

    String url();

    String username();

    String password();

    default JdbcDatabaseContainer newJdbcDatabaseContainer() {
        throw new UnsupportedOperationException(
            String.format(
                "The [%s] database was not configured to use Testcontainers!",
                database()
            )
        );
    }

    default boolean supportsDatabaseName() {
        return true;
    }

    default boolean supportsCredentials() {
        return true;
    }
}