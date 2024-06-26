package com.mj.mysns.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import javax.sql.DataSource;
import org.testcontainers.containers.JdbcDatabaseContainer;

/**
 * @author Vlad Mihalcea
 */
public abstract class AbstractContainerDataSourceProvider implements DataSourceProvider {

    private JdbcDatabaseContainer container;

    public JdbcDatabaseContainer getContainer() {
        return container;
    }

    public void initContainer(String username, String password) {
        container = (JdbcDatabaseContainer) newJdbcDatabaseContainer()
            .withReuse(true)
            .withEnv(Collections.singletonMap("ACCEPT_EULA", "Y"))
            .withTmpFs(Collections.singletonMap("/testtmpfs", "rw"));
        if (supportsDatabaseName()) {
            container.withDatabaseName("mj");
        }
        if (supportsCredentials()) {
            container.withUsername(username).withPassword(password);
        }
        container.start();
    }

    @Override
    public DataSource dataSource() {
        DataSource dataSource = newDataSource();
        try (Connection connection = dataSource.getConnection()) {
            return dataSource;
        } catch (SQLException e) {
            if (container == null) {
                initContainer(username(), password());
            }
            return newDataSource();
        }
    }

    @Override
    public String url() {
        return container != null ?
            container.getJdbcUrl() :
            defaultJdbcUrl();
    }

    protected abstract String defaultJdbcUrl();

    protected abstract DataSource newDataSource();
}