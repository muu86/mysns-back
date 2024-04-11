package com.mj.mysns.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.sql.DataSource;
import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 *
 * @author Vlad Mihalcea
 */
@Configuration
@Testcontainers
public class TestConfig {

    private DataSourceProvider dataSourceProvider = new PostgreSQLDataSourceProvider();

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySources() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource actualDataSource() {
        Properties properties = new Properties();
        properties.setProperty("maximumPoolSize", String.valueOf(3));
        HikariConfig hikariConfig = new HikariConfig(properties);
        hikariConfig.setAutoCommit(false);
        hikariConfig.setDataSource(dataSourceProvider.dataSource());
        return new HikariDataSource(hikariConfig);
    }

    // use hibernate to format queries
    private static class PrettyQueryEntryCreator extends DefaultQueryLogEntryCreator {
        private Formatter formatter = FormatStyle.BASIC.getFormatter();

        @Override
        protected String formatQuery(String query) {
            return this.formatter.format(query);
        }
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        PrettyQueryEntryCreator creator1 = new PrettyQueryEntryCreator();
        creator1.setMultiline(true);

        SystemOutQueryLoggingListener loggingListener = new SystemOutQueryLoggingListener();
        loggingListener.setQueryLogEntryCreator(creator1);

        DataSource dataSource = ProxyDataSourceBuilder
            .create(actualDataSource())
            .name("DATA_SOURCE_PROXY")
            .countQuery()
//            .multiline()
            .listener(loggingListener)
            .build();
        return dataSource;
    }
}