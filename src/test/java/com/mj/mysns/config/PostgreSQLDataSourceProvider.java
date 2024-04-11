package com.mj.mysns.config;

import javax.sql.DataSource;
import org.hibernate.dialect.Database;
import org.hibernate.dialect.PostgreSQLDialect;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Vlad Mihalcea
 */
public class PostgreSQLDataSourceProvider extends AbstractContainerDataSourceProvider {

    public static final DataSourceProvider INSTANCE = new PostgreSQLDataSourceProvider();

    @Override
    public Database database() {
        return Database.POSTGRESQL;
    }

    @Override
    public String hibernateDialect() {
        return PostgreSQLDialect.class.getName();
    }

    @Override
    protected String defaultJdbcUrl() {
        return "jdbc:postgresql://localhost/mj";
    }

    protected DataSource newDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(url());
        dataSource.setUser(username());
        dataSource.setPassword(password());

        return dataSource;
    }

    @Override
    public String username() {
        return "postgres";
    }

    @Override
    public String password() {
        return "1234";
    }

    @Override
    public JdbcDatabaseContainer newJdbcDatabaseContainer() {
        return new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:16-master").asCompatibleSubstituteFor("postgres"));
    }
}