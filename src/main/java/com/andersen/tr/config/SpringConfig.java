package com.andersen.tr.config;

import com.andersen.tr.Main;
import com.andersen.tr.dao.impl.TicketDao;
import com.andersen.tr.dao.impl.UserDao;
import com.andersen.tr.service.impl.TicketService;
import com.andersen.tr.service.impl.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"com.andersen.tr.dao", "com.andersen.tr.model", "com.andersen.tr.service", "com.andersen.tr"})
@PropertySource("classpath:application.properties")
@EnableTransactionManagement(proxyTargetClass = true)
public class SpringConfig {
    @Value("${entity.update.enabled}")
    boolean isUpdateEnabled;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${db.driver}")
    private String connectionDriverClass;

    @Value("${db.url}")
    private String connectionUrl;

    @Value("${db.user}")
    private String connectionUsername;

    @Value("${db.password}")
    private String connectionPassword;

    @Value("${db.poolsize}")
    private int poolSize;

    @Bean
    public UserService userService() {
        UserDao userDao = new UserDao(dataSource(), new TicketDao(dataSource()));
        return new UserService(isUpdateEnabled, ticketService(), userDao);
    }

    @Bean
    public TicketService ticketService() {
        TicketDao ticketDao = new TicketDao(dataSource());
        return new TicketService(ticketDao);
    }

    @Bean
    public Main main(TicketService ticketService, UserService userService) {
        return new Main(ticketService, userService);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(connectionDriverClass);
        dataSource.setUrl(connectionUrl);
        dataSource.setUsername(connectionUsername);
        dataSource.setPassword(connectionPassword);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
